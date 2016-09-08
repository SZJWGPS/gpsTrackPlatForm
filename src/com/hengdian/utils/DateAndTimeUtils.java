package com.hengdian.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateAndTimeUtils {

	/**
	 * 忽略时间年份，计算两个日期相差天数
	 * 
	 * @param fDate
	 * @param oDate
	 * @return
	 * 
	 */

	public static int getDaysOfTwoDate(String fDateStr, String oDateStr) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date fDate = null;
		Date oDate = null;
		try {
			fDate = format.parse(fDateStr);
			oDate = format.parse(oDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(fDate);
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(oDate);
		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		return day2 - day1;
	}

	/**
	 * 考虑时间，计算两个日期相差天数
	 * 
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int getIntervalDays(String fDateStr, String oDateStr) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date fDate = null;
		Date oDate = null;

		try {
			fDate = format.parse(fDateStr);
			oDate = format.parse(oDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (null == fDate || null == oDate) {
			return -1;
		}

		long intervalMilli = oDate.getTime() - fDate.getTime();
		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
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

	public static String getJumpDate(String specifiedDay, int jump) {

		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + jump);

		String jumpDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return jumpDate;
	}

}
