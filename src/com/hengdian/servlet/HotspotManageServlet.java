package com.hengdian.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hengdian.entity.HotspotJson;
import com.hengdian.entity.LoLatitude;
import com.hengdian.utils.JDBCUtils;
import com.hengdian.utils.JudgeDistrictsOfShenZhen;

/**
 * 重点区域管理
 */
public class HotspotManageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public HotspotManageServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		int daoCode = 10; // 数据库操作结果代码
		String msg = ""; // 返回的提示信息

		String spotName = request.getParameter("spotName");// 地点名称
		String longitudeStr = request.getParameter("longitude");
		String latitudeStr = request.getParameter("latitude");
		String excuteCodeStr = request.getParameter("excuteCode");

		System.out.println("名称：" + spotName + "，经度：" + longitudeStr + "，纬度："
				+ latitudeStr + "，操作码：" + excuteCodeStr);

		double longitude = 0.00;
		double latitude = 0.00;

		// 判断点所属区域
		int districtCode = 7;
		if (longitudeStr.length() > 0 && latitudeStr.length() > 0) {
			longitude = Double.parseDouble(longitudeStr);
			latitude = Double.parseDouble(latitudeStr);
			districtCode = JudgeDistrictsOfShenZhen.isInPolygon(new LoLatitude(
					latitude, longitude));
		}

		String sql;
		Object[] valArr;
		ResultSet rs;
		JDBCUtils jdbcUtils = new JDBCUtils();
		// 如果提交的点不是市外的点
		if (0 != districtCode) {
			switch (excuteCodeStr) {
			case "add":
				sql = "select count(*) from sz_hotspot where name=? or (longitude=? and latitude=?)";
				valArr = new Object[] { spotName, longitude, latitude };
				rs = jdbcUtils.executeQuery(sql, valArr);
				try {
					while (rs.next()) {
						System.out.println();
						if (rs.getInt(1) > 0) {
							daoCode = 0;
							msg = "名称或经纬度相同的点已存在！";
							
						} else {
							sql = "insert into sz_hotspot values('"
									+ spotName + "'," 
									+ longitude + ","
									+ latitude + "," 
									+ districtCode + ","
									+ "ST_GeomFromText('POINT(" + longitude
									+ " " + latitude + ")', 4326),'"
									+ longitude + "-" + latitude + "')";							
							jdbcUtils.executeUpdateNotClose(sql, null);
							
							daoCode = 1;
							msg = "添加地点成功！名称：" + spotName + "，经纬度：" + longitude
									+ "," + latitude;
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
					daoCode = 2;
					msg = "输入的名称或经纬度类型有误！";
				}
				break;

			case "delete":
				//sql = "delete from hotspot_info t1,key_point_area t2 where t1.longitude=? and t1.latitude=? and t1.lnglat=t2.lnglat";
				sql = "delete from sz_hotspot where longitude=? and latitude=?";
				
				valArr = new Object[] {longitude, latitude};
				System.out.println("'" + longitude + "-" + latitude + "'");
				int delcounter = jdbcUtils.executeUpdateNotClose(sql, valArr);
				if(delcounter >0){
					daoCode = 3;
					msg = "删除地点成功！经纬度：" + longitude + "," + latitude;
				}else{
					daoCode = 7;
					msg = "删除地点失败！经纬度：" + longitude + "," + latitude;
				}
				
				break;
			}
		} else {
			daoCode = 6;
			msg = "该点不在深圳市范围内，禁止添加！";
		}

		// 如果切换到重点区域管理标签，则查询已有区域中心名称
		String tableTrStr = "";
		if (daoCode == 10) {
			sql = "select * from sz_hotspot order by name";
			rs = jdbcUtils.executeQuery(sql, null);
			try {
				while (rs.next()) {
					tableTrStr += "<tr>"
							+ "<td>"
							+ rs.getString(1)
							+ "</td>"
							+ "<td>"
							+ rs.getString(2)
							+ "</td>"
							+ "<td>"
							+ rs.getString(3)
							+ "</td>"
							+ "<td>"
							+ "<button type='button' class='btn btn-mini btn-danger'"
							+ ">删除</button>" + "</td>" + "</tr>";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		jdbcUtils.closeAll();
		JSONObject hotspotJson = new HotspotJson().makeHotspotJson(tableTrStr, daoCode, msg);
		
		// String url = "/index.jsp?msg=";
		// response.sendRedirect(request.getContextPath() + url + msg);
		
		// response.setContentType("text/text;charset=utf-8");
		response.setContentType("text/json; charset=UTF-8");
		// response.setContentType("text/html;charset=utf-8");
		// response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		// response.getOutputStream().write(tableStr.getBytes("utf-8"));
		PrintWriter pw = response.getWriter();
		pw.write(hotspotJson.toString());

		System.out.println(hotspotJson.toString());
//		String url = "/addOrDelSpotPointsServlet?msg=";
//		response.sendRedirect(request.getContextPath() + url);
		
		if (daoCode == 3) {
			
			final String sql2 = "delete from key_point_area where lnglat=?";
			final Object[] valArr2 = new Object[] {"'" + longitude + "-" + latitude + "'"};
//			new Thread(new Runnable() {			
//				@Override
//				public void run() {		
//					System.out.println("del=======0");
//					System.out.println(sql2+"=="+valArr2[0]);
//					//new JDBCUtils().executeUpdate(sql2, valArr2);
//					System.out.println("del=======1");
//				}
//			}).start();			
		}else if(daoCode == 1){
			//从基本表筛选半径一公里内的点插入key_point_area表中
			final String sql2 = "insert into key_point_area select" 
				+ " taxi_no,longitude,latitude,status,district,record_date,record_time,'"
				+ longitude + "-" + latitude+"' from sz_taxi_track where"
				+ " ST_DWithin(ST_Transform(ST_GeomFromText('POINT("+longitude + " " + latitude+")', 4326), 26986),"
				+ "ST_Transform(ST_GeomFromText('POINT(' || longitude || ' ' ||latitude|| ')', 4326), 26986), 500)";
			System.out.println(sql2);
//			new Thread(new Runnable() {			
//				@Override
//				public void run() {						
//					System.out.println("add=======0");
//					int c = 0;// = new JDBCUtils().executeUpdate(sql2, null);	
//					System.out.println("影响："+c);
//					System.out.println("add=======1");
//				}
//			}).start();				
		}		
	}
}
