package com.hengdian.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hengdian.entity.GpsJson;
import com.hengdian.utils.DateAndTimeUtils;
import com.hengdian.utils.JDBCUtils;

public class GpsServLet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GpsServLet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String startDate = (String) request.getParameter("startDate");
		String endtDate = (String) request.getParameter("endDate");
		String startTime = (String) request.getParameter("startTime");
		String endTime = (String) request.getParameter("endTime");
		String sql = "select * from taxi_track where (record_date>=? and record_date<=?)"
				+ " and (status_change_piont=2 or status_change_piont=3)"
				+ " and (record_time>=? and record_time<=?) limit 10000";
		if (startDate == null || startDate.length() < 8) {
			startDate = "";
		}
		if (endtDate == null || endtDate.length() < 8) {
			endtDate = "";
		}
		if (startTime != null && startTime.length() != 8) {
			startTime = "00:00:00";
		}
		if (endTime != null && endTime.length() != 8) {
			endTime = "23:59:59";
		}

		HttpSession session = request.getSession();
		session.setAttribute("startDate", startDate);
		session.setAttribute("endtDate", endtDate);
		session.setAttribute("startTime", startTime);
		session.setAttribute("endTime", endTime);

		Object[] valueArr = new Object[4];
		valueArr[0] = "'" + startDate + "'";
		valueArr[1] = "'" + endtDate + "'";
		valueArr[2] = "'" + startTime + "'";
		valueArr[3] = "'" + endTime + "'";

		JDBCUtils jdbcUtils = new JDBCUtils();
		ResultSet rs = jdbcUtils.executeQuery(sql, valueArr);

		// 判断查询的时段长度
		int startHour = Integer.parseInt(startTime.substring(0, 2));
		int endHour = Integer.parseInt(endTime.substring(0, 2));
		int hourLength = endHour - startHour + 1;
		int[] timeDataArr = new int[hourLength];
		for (int i = 0; i < timeDataArr.length; i++) {
			timeDataArr[i] = 0;
		}

		// 获取时段显示小时横坐标
		String[] hourFramexAxi = new String[hourLength];
		for (int i = startHour; i <= endHour; i++) {
			hourFramexAxi[i] = i + "点";
		}

		// 计算两个日期相差天数
		int days = DateAndTimeUtils.getIntervalDays(startDate, endtDate) + 1;
		int[] dayDataArr = new int[days];
		for (int i = 0; i < days; i++) {
			dayDataArr[i] = 0;
		}

		// 获取按天显示的横坐标日期
		String[] trendchart_day_xAxiArr = new String[days];
		for (int i = 0; i < days; i++) {
			String dayStr = DateAndTimeUtils.getJumpDate(startDate, i);
			trendchart_day_xAxiArr[i] = dayStr;
		}

		// 创建封装返回数据的json对象
		GpsJson gpsJson = new GpsJson();

		int on = 0; // 上客次数
		int off = 0; // 下客次数
		try {
			while (rs.next()) {
				// 如果是下客点
				if (rs.getInt(7) == 2) {
					off++; // 统计下客次数
					// 如果是上客点
				} else if (rs.getInt(7) == 3) {
					on++; // 统计上客次数
					double jingdu_lo = rs.getDouble(2);
					double weidu_la = rs.getDouble(3);

					gpsJson.addSanDian(jingdu_lo, weidu_la); // 添加散点
					gpsJson.addHeatMapPoint(jingdu_lo, weidu_la, 1); // 添加热力图点
					// 获取时间小时数
					int time = Integer.parseInt(rs.getString(9).substring(0, 2));
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

		gpsJson.setHeatMapMax(1);
		gpsJson.setOnOffValue(on, off);
		// 按时段显示数据数组字符串
		for (int i = 0; i < timeDataArr.length; i++) {
			gpsJson.addPoint_trendChart_byHourFrame(timeDataArr[i], hourFramexAxi[i]);
		}
		// 按天显示数据数组字符串
		for (int i = 0; i < dayDataArr.length; i++) {
			gpsJson.addPoint_trendChart_byday(dayDataArr[i], trendchart_day_xAxiArr[i].substring(5));
		}

		System.out.println(gpsJson.getGpsJson());
		// request.setAttribute("gpsjson", gpsJson.getGpsJson());
		session.setAttribute("gpsjson", gpsJson.getGpsJson().toString());

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
