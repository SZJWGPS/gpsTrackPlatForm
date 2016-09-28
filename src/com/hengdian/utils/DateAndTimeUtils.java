package com.hengdian.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * 获取两个日期相差天数，获取日期对应的星期、季度
 * 
 * 对于创建SimpleDateFormat传入的参数：
 * EEEE代表星期，如“星期四”;
 * MMMM代表中文月份，如“十一月”;
 * MM代表月份，如“11”;
 * yyyy代表年份，如“2010”;
 * dd代表天，如“25”   
 * 
 */

public class DateAndTimeUtils {

	public static void main(String[] args) {
		//System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(getJumpDate(new Date(), -1)));
	}

	/**
	 * 考虑时间，计算两个日期相差天数
	 * 
	 * @param dateBefore
	 * @param dateAfter
	 * @return
	 */
	public static int getIntervalDays(Date dateBefore, Date dateAfter) {
		if (null == dateBefore || null == dateAfter) {
			return -1;
		}

		long intervalMilli = dateAfter.getTime() - dateBefore.getTime();
		return (int) (intervalMilli / (24 * 60 * 60 * 1000));// 86400000=60*60*24*1000
																// 用立即数，减少乘法计算的开销
	}

	/**
	 * 忽略时间和年份，计算两个日期相差天数,不推荐
	 * 
	 * @param dateBefore
	 * @param dateAfter
	 * @return
	 */
	public static int getDaysOfTwoDate(Date dateBefore, Date dateAfter) {

		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(dateBefore);
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(dateAfter);
		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		return day2 - day1;
	}

	/**
	 * 获得指定日期的前或后几天
	 * 
	 * @param specifiedDay
	 *            指定的日期
	 * @param jump
	 *            跳跃几天，正数向后，负数向前
	 * @return
	 */

	public static Date getJumpDate(Date specifiedDate, int jump) {

		Calendar c = Calendar.getInstance();
		c.setTime(specifiedDate);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + jump);
		// Date jumpDate = new SimpleDateFormat("yyyy-MM-dd")
		c.getTime();
		return c.getTime();
	}

	/**
	 * 获取当前时间的前/后N天的日期
	 * 
	 * @param jump
	 * @return
	 */
	public static Date getJumpDate(int jump) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, jump);
		return calendar.getTime();
	}

	/**
	 * 获取日期对应的星期
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekByDate(Date date) {
		// 根据日期取得星期几,使用Calendar类
		// String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(date);
		// int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		// if(week_index<0){
		// week_index = 0;
		// }
		// return weeks[week_index];

		// SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		// String week = sdf.format(date);
		return new SimpleDateFormat("E").format(date);
	}

	/**
	 * 获取日期对应的“日”
	 * 
	 * @param date
	 * @return
	 */
	public static String getDayByDate(Date date) {
		return new SimpleDateFormat("d").format(date);
	}

	// 取得日期是某年的第几周
	// 根据日期入得日期是某年的第几周。

	public static int getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_of_year = cal.get(Calendar.WEEK_OF_YEAR);
		return week_of_year;
	}

	// 已知年份和月份，取得该月有多少天。

	public static int getDaysOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		int days_of_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days_of_month;
	}

}
