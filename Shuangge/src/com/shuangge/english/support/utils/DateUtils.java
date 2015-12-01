package com.shuangge.english.support.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Jeffrey
 * 
 */
public class DateUtils {
	
	public static Long offsetTime;

	private static ThreadLocal<DateFormat> THREADLOCAL_TIMESTAMP_FMT = new ThreadLocal<DateFormat>();
	private static ThreadLocal<DateFormat> THREADLOCAL_DATETIME_FMT = new ThreadLocal<DateFormat>();
	private static ThreadLocal<DateFormat> THREADLOCAL_DATE_FMT = new ThreadLocal<DateFormat>();
	private static ThreadLocal<DateFormat> THREADLOCAL_TIME_FMT = new ThreadLocal<DateFormat>();
	private static ThreadLocal<DateFormat> THREADLOCAL_YEAR_FMT = new ThreadLocal<DateFormat>();

	public static String convert(Date date) {
		Date now = getServerTime();
		int minutes = minutesBetween(date, now);
		//一个小时内
		if (minutes < 60) {
			return minutes + "分钟前";
		}
		int days = daysBetween(date, now);
		//当天
		if (days == 0) {
			return getTimeFmt().format(date);
		}
		//昨天
		if (days == 1) {
			return "昨天" + getTimeFmt().format(date);
		}
		//日期
		return getDateFmt().format(date);
	}
	
	public static String convertToDatetime(Date date) {
		Date now = new Date();
		int minutes = minutesBetween(date, now);
		//一个小时内
		if (minutes < 60) {
			return minutes + "分钟前";
		}
		int days = daysBetween(date, now);
		//当天
		if (days == 0) {
			return getTimeFmt().format(date);
		}
		//昨天
		if (days == 1) {
			return "昨天" + getTimeFmt().format(date);
		}
		//日期
		return getDateTimeFmt().format(date);
	}
	
	public static DateFormat getTIMESTAMP_FORMAT() {
		DateFormat df = THREADLOCAL_TIMESTAMP_FMT.get();
		if (null == df) {
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			THREADLOCAL_TIMESTAMP_FMT.set(df);  
		}
		return df;
	}
	
	public static DateFormat getDateTimeFmt() {
		DateFormat df = THREADLOCAL_TIMESTAMP_FMT.get();
		if (null == df) {
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
			THREADLOCAL_DATETIME_FMT.set(df);  
		}
		return df;
	}
	
	public static DateFormat getDateFmt() {
		DateFormat df = THREADLOCAL_DATE_FMT.get();
		if (null == df) {
			df = new SimpleDateFormat("yyyy-MM-dd");  
			THREADLOCAL_DATE_FMT.set(df);  
		}
		return df;
	}
	
	public static DateFormat getYearFmt() {
		DateFormat df = THREADLOCAL_YEAR_FMT.get();
		if (null == df) {
			df = new SimpleDateFormat("yyyy");  
			THREADLOCAL_YEAR_FMT.set(df);  
		}
		return df;
	}
	
	public static DateFormat getTimeFmt() {
		DateFormat df = THREADLOCAL_TIME_FMT.get();
		if (null == df) {
			df = new SimpleDateFormat("HH:mm");  
			THREADLOCAL_YEAR_FMT.set(df);  
		}
		return df;
	}
	
	public static int minutesBetween(Date early, Date late) {
		long time = Math.abs(late.getTime() - early.getTime());
		// long m = time / 1000 / 60 / 60 / 24; //天
		long m = time / 1000 / 60; // 分钟
		return (int) Math.abs(m);
	}

	public static int daysBetween(Date early, Date late) {
		try {
			return minutesBetween(getDateFmt().parse(getDateFmt().format(early)),
					getDateFmt().parse(getDateFmt().format(late)));
		} catch (ParseException e) {
//			logger.error("DateUtils.daysBetween 日期转换错误  msg:" + e);
		}
		return 0;
	}

	public static Date timeToDay(Date date) {
		try {
			return getDateFmt().parse(getDateFmt().format(date));
		} catch (ParseException e) {
//			logger.error("DateUtils.timeToDay 日期转换错误  msg:" + e);
		}
		return date;
	}

	/**
	 * 在有效期间内
	 * 
	 * @param startDate
	 * @param endDate
	 * @return -1 未开始 0 进行中 1结束
	 */
	public static int timeOfEffect(Date startDate, Date endDate) {
		Date now = new Date();
		if (getDayBefore(startDate).after(now)) {
			return -1;
		}
		if (getDayAfter(endDate).before(now)) {
			return 1;
		}
		return 0;
	}

	public static Date getDayBefore(Date specifiedDay) {
		Calendar c = Calendar.getInstance();
		c.setTime(specifiedDay);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);
		return c.getTime();
	}

	public static Date getDayAfter(Date specifiedDay) {
		Calendar c = Calendar.getInstance();
		c.setTime(specifiedDay);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);
		return c.getTime();
	}

	public static String getYearAndWeekNumber() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		return getYearFmt().format(date) + "_" + calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static int getAge(Date date) {
		long day = (new Date().getTime() - date.getTime())/(24*60*60*1000) + 1;
		return (int) (day / 365f);
	}
	
	public static Date getServerTime() {
		return new Date(System.currentTimeMillis() + offsetTime);
	}

}
