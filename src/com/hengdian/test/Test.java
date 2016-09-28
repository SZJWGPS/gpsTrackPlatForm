package com.hengdian.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static void main(String[] args) {

		// 今天是2014-12-25 星期四

		String weekOfDate = null;

		weekOfDate = getWeekOfDate(null);
		System.out.println(weekOfDate);

		// 输出 星期四		
		SimpleDateFormat sdf= new SimpleDateFormat("d");
		Date date=null;
		try {
			date = new SimpleDateFormat("yy-MM-DD h").parse("2016-09-08 13");
			System.out.println(date.toString());
		} catch (ParseException e) {			
			e.printStackTrace();
		} 

		

		weekOfDate = getWeekOfDate(date);

		System.out.println(sdf.format(date));

		// 输出 星期三

	}

	/**
	 * 获取指定日期是星期几,参数为null时表示获取当前日期是星期几
	 * 
	 * @param date
	 * 
	 * @return
	 */

	public static String getWeekOfDate(Date date) {

		String[] weekOfDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekOfDays[w];
	}
}
