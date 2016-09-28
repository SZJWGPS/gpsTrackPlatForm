package com.hengdian.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hengdian.entity.NormalPoint;
import com.hengdian.utils.JDBCUtils;

/*
 * 皇岗口岸=114.07306=22.520069=114.073060-22.520069
 深圳西站=113.908211=22.527223=113.908211-22.527223
 福田口岸=114.068475=22.515561=114.068475-22.515561
 机场=113.814581=22.623508=113.814581-22.623508
 蛇口港=113.912364=22.476156=113.912364-22.476156
 深圳站=114.117241=22.529551=114.117241-22.529551
 深圳东站=114.120675=22.601396=114.120675-22.601396
 深圳北站=114.027486=22.608017=114.027486-22.608017
 * 
 * */
public class Test1 {

	public static void main(String[] args) {
		JDBCUtils jdbcUtils = new JDBCUtils();
		String sql2 = "select name,longitude,latitude,lnglat from hotspot_info"; // where
																					// district=?";
		String sql3 = "select";
		ResultSet rs;
		rs = jdbcUtils.executeQuery(sql2, null);
		try {
			List<NormalPoint> points = new ArrayList<NormalPoint>();
			int i = 0;
			while (rs.next()) {
				String lo_la = rs.getString(4);
				points.add(new NormalPoint(rs.getString(1), rs.getDouble(2), rs
						.getDouble(3), lo_la));

				sql3 += " sum (case when lnglat='" + lo_la + "' then 1 end) st"
						+ i + ",";
				i++;
			}
			sql3 = sql3.substring(0, sql3.lastIndexOf(","));
			sql3 += " from key_point_area where status=3";
			System.out.println(sql3);

			rs = jdbcUtils.executeQuery(sql3, null);

			NormalPoint point;
			while (rs.next()) {
				for (int p = 0; p < i; p++) {
					point = points.get(p);
					rs.getInt(p+1);
					System.out.println(point.lo_la + "==" + rs.getInt(p+1));
				}
				// System.out.println(rs.getInt(1)+"="+rs.getInt(2)+"="+rs.getInt(3)+"="+rs.getInt(4)+"="+rs.getInt(5)+"="+rs.getInt(6)+"="+rs.getInt(7)+"="+rs.getInt(8));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
}
