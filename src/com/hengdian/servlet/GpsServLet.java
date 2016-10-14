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

public class GpsServLet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GpsServLet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf_hour = new SimpleDateFormat("HH");
		SimpleDateFormat sdf_week = new SimpleDateFormat("E");
		
		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");
		int districtCode = Integer.parseInt(request.getParameter("district"));		

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
			startDateStr = endDateStr = sdf_date.format(DateAndTimeUtils.getJumpDate(-1));
			startTimeStr = "00:00:00";
			endTimeStr = "23:59:59";
			System.err.println("输入的时间或日期格式有误，现将日期时间重设为昨天：" 
					+ startDateStr + "," + startTimeStr + "-" + endTimeStr);
		}
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf_date.parse(startDateStr);
			endDate = sdf_date.parse(endDateStr);
		} catch (ParseException e1) {
			e1.printStackTrace();
			return;
		}

		System.out.println("日期：" + startDateStr + "~" + endDateStr + ", 时间："
				+ startTimeStr + "~" + endTimeStr + ", 区域代码：" + districtCode);
		// 保存查询条件
		HttpSession session = request.getSession();
		session.setAttribute("startDate", startDateStr);
		session.setAttribute("endDate", endDateStr);
		session.setAttribute("startTime", startTimeStr);
		session.setAttribute("endTime", endTimeStr);
		session.setAttribute("district", districtCode);

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
			dayDataArr[i] = 0; // 初始化每天的计数,其实可以不用
			trendchart_day_xAxiArr[i] = sdf_date.format(DateAndTimeUtils
					.getJumpDate(startDate, i));
		}

		// 按天显示的纵坐标数据
		int[] weekDataArr = new int[7];
		// 获取按星期显示的横坐标日期
		String[] trendchart_week_xAxiArr = new String[]{
				"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

		// 创建封装返回数据的json对象
		GpsJson gpsJson = new GpsJson();
		long  on = 0; // 上重车次数
		long off = 0; // 空车次数
		// int HeatMapMax; //热力图最值
		
		String districtQueryStr;// 0深圳市外，1罗湖，2福田，3南山，4龙岗，5宝安，6盐田
		switch (districtCode) {
		case 0:
			districtQueryStr = " district=0";
			break;
		case 1:
			districtQueryStr = " district=1";
			break;
		case 2:
			districtQueryStr = " district=2";
			break;
		case 3:
			districtQueryStr = " district=3";
			break;
		case 4:
			districtQueryStr = " district=4";
			break;
		case 5:
			districtQueryStr = " district=5";
			break;
		case 6:
			districtQueryStr = " district=6";
			break;
		default:
			districtQueryStr = " district>0";
			break;
		}
		
		/*
		 * 查询上下客和空重车数据 
		 * 
		 */
		String sql = "select * from sz_taxi_track_sumbyhour"
				+ " where record_date between ? and ?" 
				+ " and" + districtQueryStr 
				+ " and (record_time>=? and record_time<=?)";
		
		Object[] valueArr = new Object[4];
		valueArr[0] = new java.sql.Date(startDate.getTime());
		valueArr[1] = new java.sql.Date(endDate.getTime());
		valueArr[2] = Integer.parseInt(sdf_hour.format(startDateTime));
		valueArr[3] = Integer.parseInt(sdf_hour.format(endDateTime));
		
		JDBCUtils jdbcUtils = new JDBCUtils();
		ResultSet rs = jdbcUtils.executeQuery(sql, valueArr);
		try {
			while (rs.next()) {				
				off += (rs.getInt(4) + rs.getInt(6))/10; // 统计空车次数				
				on += (rs.getInt(5) + rs.getInt(7))/10; //重车统计
				
				// 获取按时段统计所属纵坐标
				int hourInt = rs.getInt(3);
				//向对应时段列添加上客数据
				timeDataArr[hourInt - startHour]+=rs.getInt(7);

				// 获取该行数据年月日
				String dateOfThisLine = rs.getString(2);
				//向对应日期列添加上客数据
				for (int q = 0; q < trendchart_day_xAxiArr.length; q++) {
					if (dateOfThisLine.indexOf(trendchart_day_xAxiArr[q]) >= 0) {
						dayDataArr[q] += rs.getInt(7);
					}
				}
				// 获取该行数据星期
				String weekOfThisLine = sdf_week.format(sdf_date.parse(dateOfThisLine));
				//向对应星期列添加上客数据
				for (int q = 0; q < trendchart_week_xAxiArr.length; q++) {
					if (weekOfThisLine.indexOf(trendchart_week_xAxiArr[q]) >= 0) {
						weekDataArr[q] += rs.getInt(7);
					}
				}

			}

		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		
		/*
		 * 获取散点图和热度图数据			
		 * 
		 */
		sql = "select longitude, latitude from sz_taxi_track_on"
					//+ " where record_date between ? and ?" 
					+ " where" + districtQueryStr 
					+ " and (record_time>=? and record_time<=?)"
					+ " order by random()"
					+ " limit 3000";
		valueArr = new Object[2];	
		valueArr[0] = "'" + startTimeStr + "'";
		valueArr[1] = "'" + endTimeStr + ".000'";		
		rs = jdbcUtils.executeQuery(sql, valueArr);
		try {
			while (rs.next()) {	
				// 原始经纬坐标
				double jingdu_lo = rs.getDouble(1);
				double weidu_la = rs.getDouble(2);

				gpsJson.addSanDian(jingdu_lo, weidu_la); // 添加散点
				gpsJson.addHeatMapPoint(jingdu_lo, weidu_la, 10); // 添加热力图点
			}
		} catch (SQLException e1) {			
			e1.printStackTrace();
		}
		
		gpsJson.setHeatMapMax(10);
		gpsJson.setOnOffValue(on, off);
		// 按时段显示数据数组字符串
		for (int i = 0; i < timeDataArr.length; i++) {
			gpsJson.addPoint_trendChart_byHourFrame(timeDataArr[i],	hourFramexAxi[i]);
		}
		// 按天显示数据数组字符串
		String xAxis = "";
		String weekOfDate = "";
		for (int i = 0; i < dayDataArr.length; i++) {
			try {
				weekOfDate = sdf_week.format(sdf_date.parseObject(trendchart_day_xAxiArr[i]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if ("星期日".equals(weekOfDate)) {
				xAxis = trendchart_day_xAxiArr[i].substring(5) + "/周日";
			} else {
				xAxis = trendchart_day_xAxiArr[i].substring(5);
			}
			gpsJson.addPoint_trendChart_byday(dayDataArr[i], xAxis);
		}

		// 按星期显示数据数组字符串
		for (int i = 0; i < weekDataArr.length; i++) {
			gpsJson.addPoint_trendChart_byweek(weekDataArr[i],
					trendchart_week_xAxiArr[i]);
		}

		try {			
			//查询重点监控区域
			sql = "select name,longitude,latitude,lnglat from sz_hotspot where"	+ districtQueryStr;
			rs = jdbcUtils.executeQuery(sql, null);

			List<NormalPoint> points = new ArrayList<NormalPoint>();// 所选区域的监控点(名称，经度，纬度，lo_la)
			while (rs.next()) {
				points.add(new NormalPoint(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4)));
			}

			// 如果所选的区域有监控点，则查询相关点的数据
			int normalPointNum = points.size();
			if (normalPointNum > 0) {				
				sql = "select";
				for (int i = 0; i < normalPointNum; i++) {
					sql += " sum (case when lnglat='" + points.get(i).lo_la + "' then 1 end) st" + normalPointNum + ",";
				}
				sql = sql.substring(0, sql.lastIndexOf(","));
				sql += " from key_point_area where"
						// + districtQueryStr +" and"
						+ " status=3" 
						+ " and record_date between ? and ?"
						+ " and record_time between ? and ?";
				
				valueArr = new Object[4];		
				valueArr[0] = new java.sql.Date(startDate.getTime());
				valueArr[1] = new java.sql.Date(endDate.getTime());
				valueArr[2] = "'" + startTimeStr + "'";
				valueArr[3] = "'" + endTimeStr + ".000'";	
				
				rs = jdbcUtils.executeQuery(sql, valueArr);

				int[] NormalPointValueArr = new int[normalPointNum];
				while (rs.next()) {
					for (int i = 0; i < normalPointNum; i++) {
						NormalPointValueArr[i] = rs.getInt(i + 1);
					}
				}

				int[] indexArr = GadgetUtils
						.sortIntArrAndGetOldIndex(NormalPointValueArr);
				NormalPoint point;
				for (int i = 0; i < normalPointNum; i++) {
					point = points.get(indexArr[i]);
					gpsJson.addPoint_normal(point.name, point.lo, point.la,
							NormalPointValueArr[i]);

				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			jdbcUtils.closeAll(); // 关闭数据库连接
		}

		System.out.println("后台json：" + gpsJson.getGpsJson());
		
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
