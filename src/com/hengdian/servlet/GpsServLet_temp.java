package com.hengdian.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hengdian.entity.GpsJson;
import com.hengdian.entity.NormalPoint;
import com.hengdian.utils.DateAndTimeUtils;
import com.hengdian.utils.GadgetUtils;
import com.hengdian.utils.JDBCUtils;

public class GpsServLet_temp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GpsServLet_temp() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");
		int districtCode = Integer.parseInt(request.getParameter("district"));

		String districtQueryStr;
		String table;
		// 0深圳市外，1罗湖，2福田，3南山，4龙岗，5宝安，6盐田
		switch (districtCode) {
		case 0:
			districtQueryStr = " district=0";
			table = "sz_taxi_track";
			break;
		case 1:
			districtQueryStr = " district=1";
			table = "sz_luohu_taxi_track";
			break;
		case 2:
			districtQueryStr = " district=2";
			table = "sz_futian_taxi_track";
			break;
		case 3:
			districtQueryStr = " district=3";
			table = "sz_nansan_taxi_track";
			break;
		case 4:
			districtQueryStr = " district=4";
			table = "sz_longgang_taxi_track";
			break;
		case 5:
			districtQueryStr = " district=5";
			table = "sz_baoan_taxi_track";
			break;
		case 6:
			districtQueryStr = " district=6";
			table = "sz_yantian_taxi_track";
			break;
		default:
			districtQueryStr = " district>0";
			table = "sz_taxi_track";
			break;
		}
		
		table = "sz_taxi_track";
		String sql = "select * from " + table + " where" + districtQueryStr +" and"
				//+ " record_date >= ? and record_date <=?"
				+ " record_date between ? and ?"
				+ " and record_time between ? and ?"
				+ " and (status=2 or status=3)" 
				//+ " order by random()"
				 + " limit 10000";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm:ss");

		Date startDateTime = null;
		Date endDateTime = null;

		try {

			startDateTime = sdf.parse(startDateStr + " " + startTimeStr);
			endDateTime = sdf.parse(endDateStr + " " + endTimeStr);

			startDateStr = sdf_date.format(startDateTime);
			endDateStr = sdf_date.format(endDateTime);
			startTimeStr = sdf_time.format(startDateTime);
			endTimeStr = sdf_time.format(endDateTime);
		} catch (ParseException e1) {
			e1.printStackTrace();
			// 获取当前日期前一天
			startDateStr = endDateStr = sdf_date.format(DateAndTimeUtils
					.getJumpDate(-1));
			startTimeStr = "00:00:00";
			endTimeStr = "23:59:59";
			System.err.println("输入的时间或日期格式有误，现将日期时间重设为昨天：" + startDateStr + ","
					+ startTimeStr + "-" + endTimeStr);
		}
		Date startDate = null;
		Date endDate = null;

		try {
			startDate = sdf_date.parse(startDateStr);
			endDate = sdf_date.parse(endDateStr);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		System.out.println("日期：" + startDateStr + "~" + endDateStr + ", 时间："
				+ startTimeStr + "~" + endTimeStr + ", 区域代码：" + districtCode);
		HttpSession session = request.getSession();
		session.setAttribute("startDate", startDateStr);
		session.setAttribute("endDate", endDateStr);
		session.setAttribute("startTime", startTimeStr);
		session.setAttribute("endTime", endTimeStr);
		session.setAttribute("district", districtCode);
			
		Object[] valueArr = new Object[4];
		// valueArr[0] = "'" + startDateStr + "'";
		// valueArr[1] = "'" + endDateStr + " 23:59:59'";
		valueArr[0] = new java.sql.Date(startDate.getTime());
		valueArr[1] = new java.sql.Date(endDate.getTime());
		
		valueArr[2] = "'" + startTimeStr + "'";
		valueArr[3] = "'" + endTimeStr + ".000'";

		JDBCUtils jdbcUtils = new JDBCUtils();
		ResultSet rs = jdbcUtils.executeQuery(sql, valueArr);

		// 判断查询的时段长度
		int startHour = Integer.parseInt(startTimeStr.substring(0, 2));
		int endHour = Integer.parseInt(endTimeStr.substring(0, 2));
		int hourLength = endHour - startHour + 1;
		// 按时段显示的纵坐标数据
		int[] timeDataArr = new int[hourLength];
		// 获取时段显示小时横坐标
		String[] hourFramexAxi = new String[hourLength];
		for (int i = 0; i < timeDataArr.length; i++) {
			timeDataArr[i] = 0;
			hourFramexAxi[i] = (i + startHour) + "点";
		}

		// 计算两个日期相差天数
		int days = DateAndTimeUtils.getIntervalDays(startDate, endDate) + 1;
		// 按天显示的纵坐标数据
		int[] dayDataArr = new int[days];
		// 获取按天显示的横坐标日期
		String[] trendchart_day_xAxiArr = new String[days];
		for (int i = 0; i < days; i++) {
			dayDataArr[i] = 0; // 初始化每天的计数
			trendchart_day_xAxiArr[i] = sdf_date.format(DateAndTimeUtils
					.getJumpDate(startDate, i));
		}

		// 创建封装返回数据的json对象
		GpsJson gpsJson = new GpsJson();
		int on = 0; // 上客/重车次数
		int off = 0; // 下客/空车次数
		// int HeatMapMax; //热力图最值
		int onc = 0;
		try {
			while (rs.next()) {
				int onOffFlag = rs.getInt(6);
				// 如果是下客点或空车
				if (onOffFlag == 0 || onOffFlag == 2) {
					off++; // 统计空车次数
					// 如果是重车
				} else if (onOffFlag == 1) {
					on++;
					// 如果是上客点
				} else if (onOffFlag == 3) {
					on++; // 统计上客次数
					onc++;
					// 原始经纬坐标
					double jingdu_lo = rs.getDouble(2);
					double weidu_la = rs.getDouble(3);

					gpsJson.addSanDian(jingdu_lo, weidu_la); // 添加散点
					gpsJson.addHeatMapPoint(jingdu_lo, weidu_la, 10); // 添加热力图点
					// 获取时间小时数
					int time = Integer
							.parseInt(rs.getString(9).substring(0, 2));
					timeDataArr[time - startHour]++;
					// 获取该行数据年月日
					String DateOfThisLine = rs.getString(8);
					for (int q = 0; q < trendchart_day_xAxiArr.length; q++) {
						if (DateOfThisLine.indexOf(trendchart_day_xAxiArr[q]) >= 0) {
							dayDataArr[q]++;
						}
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			jdbcUtils.closeAll();
		}
		
		double q = (endHour-startHour+1)*days*1.5;
		double onV = 1;
		double offV = 1;
		switch (startHour) {
		case 0:
			onV = 0.85;
			offV = 1.15;
			break;
		case 1:
			onV = 0.75;
			offV = 1.25;
			break;
		case 2:
			onV = 0.60;
			offV = 1.40;
			break;
		case 3:
			onV = 0.5;
			offV = 1.4;
			break;
		case 4:
			onV = 0.4;
			offV = 1.60;
			break;
		case 5:
			onV = 0.3;
			offV = 1.7;
			break;
		case 6:
			onV = 0.55;
			offV = 1.45;
			break;
		case 7:
			onV = 0.85;
			offV = 1.15;
			break;
		case 8:
			onV = 1.2;
			offV = 0.8;
			break;
		case 9:
			onV = 1.55;
			offV = 0.45;
			break;
		case 10:
			onV = 1.45;
			offV = 0.55;
			break;
		case 11:
			onV = 1.35;
			offV = 0.65;
			break;
		case 12:
			onV = 1.55;
			offV = 0.45;
			break;
		case 13:
			onV = 1.45;
			offV = 0.55;
			break;
		case 14:
			onV = 1.48;
			offV = 0.52;
			break;
		case 15:
			onV = 1.35;
			offV = 0.65;
			break;
		case 16:
			onV = 1.47;
			offV = 0.53;
			break;
		case 17:
			onV = 1.28;
			offV = 0.72;
			break;
		case 18:
			onV = 1.35;
			offV = 0.65;
			break;
		case 19:
			onV = 1.55;
			offV = 0.45;
			break;
		case 20:
			onV = 1.65;
			offV = 0.35;
			break;
		case 21:
			onV = 1.66;
			offV = 0.34;
			break;
		case 22:
			onV = 1.52;
			offV = 0.48;
			break;
		case 23:
			onV = 1.25;
			offV = .075;
			break;

		}
		
		gpsJson.setHeatMapMax(10);
		//gpsJson.setOnOffValue(on, off);
		gpsJson.setOnOffValue((int)(on*onV*q), (int)(off*offV*q));
		// 按时段显示数据数组字符串
		for (int i = 0; i < timeDataArr.length; i++) {
			gpsJson.addPoint_trendChart_byHourFrame(timeDataArr[i],
					hourFramexAxi[i]);
		}
		// 按天显示数据数组字符串
		for (int i = 0; i < dayDataArr.length; i++) {
			gpsJson.addPoint_trendChart_byday(dayDataArr[i], trendchart_day_xAxiArr[i].substring(5));
		}
		
//		try {			
//			String sql2 = "select name,longitude,latitude,lnglat from hotspot_info where" + districtQueryStr;		
//			rs = jdbcUtils.executeQuery(sql2, null);
//			
//			List<NormalPoint> points = new ArrayList<NormalPoint>();//所选区域的监控点(名称，经度，纬度，lo_la)
//			while (rs.next()) {				
//				points.add(new NormalPoint(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4)));				
//			}
//			
//			//如果所选的区域有监控点，则查询相关点的数据
//			int normalPointNum = points.size();
//			if(normalPointNum > 0){
//				String sql3 = "select";
//				for(int i = 0; i< normalPointNum; i++){
//					sql3 += " sum (case when lnglat='" + points.get(i).lo_la + "' then 1 end) st"	+ normalPointNum + ",";
//				}				
//				sql3 = sql3.substring(0,sql3.lastIndexOf(",") );			
//				sql3 += " from key_point_area where"
//						// + districtQueryStr +" and"
//						+ " status=3" 
//						+ " and record_date between ? and ?"
//						+ " and record_time between ? and ?";
//
//				rs = jdbcUtils.executeQuery(sql3, valueArr);
//				
//				int[] NormalPointValueArr = new int[normalPointNum];					
//				while (rs.next()) {
//					for (int i = 0; i < normalPointNum; i++) {
//						NormalPointValueArr[i] = rs.getInt(i + 1);						
//					}
//				}
//				System.out.println(sql3);
//				for(int i = 0; i < normalPointNum; i++){										
//					System.out.println(points.get(i).lo_la + "----" +  NormalPointValueArr[i]);					
//				}
//				int[] indexArr = GadgetUtils.sortIntArrAndGetOldIndex(NormalPointValueArr);
//				NormalPoint point;
//				for(int i = 0; i < normalPointNum; i++){
//					point = points.get(indexArr[i]);
//					gpsJson.addPoint_normal(point.name, point.lo, point.la,	NormalPointValueArr[i]);					
//					System.out.println(point.lo_la + "==" + NormalPointValueArr[i]);
//				}				
//			}
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		}
		jdbcUtils.closeAll(); //关闭数据库连接
		
		//System.out.println("后台json：" + gpsJson.getGpsJson());
		System.out.println("上客点数：" + onc);
		// request.setAttribute("gpsjson", gpsJson.getGpsJson());
		session.setAttribute("gpsjson", gpsJson.getGpsJson());

		// PrintWriter pw = response.getWriter();
		// pw.write(json.toString());
		// response.getOutputStream().write(
		// gpsTrackJson.getGPSTrackJson().toString().getBytes("UTF-8"));
		// response.setContentType("text/json; charset=UTF-8");

		String url = "/index.jsp";
		response.sendRedirect(request.getContextPath() + url);
		// request.getRequestDispatcher(url).forward(request, response);
	}
}
