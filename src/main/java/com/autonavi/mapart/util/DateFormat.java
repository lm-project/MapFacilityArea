package com.autonavi.mapart.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * Title: DateFormat
 * </p>
 * <p>
 * desc: 时间处理类
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-3-28 13:33
 * 
 */
public class DateFormat {

	
	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static Date getNow() {
		Date currentTime = new Date();
		return currentTime;
	}
	
	/**
	 * 获取时间戳
	 * 
	 * @return String
	 */
	public static String get13Now() {
		return System.currentTimeMillis()+"";
	}
	
	/**
	 * 将指定的时间转换成 指定的格式
	 * 
	 * @return 时间的自定义字符串表示
	 */
	public static String getStringCurrentTime(Date date, String formatStr) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间的 yyyy-MM-dd HH:mm:ss 格式
	 */
	public static String getStringCurrentDate() {
		return getStringCurrentTime(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间的 yyyyMMddHHmmss 格式
	 */
	public static String getStringCurrentDetialDate() {
		return getStringCurrentTime(new Date(), "yyyyMMddHHmmss");
	}

	
	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间的 yyyy-MM-dd 格式
	 */
	public static String getStringCurrentDateShort() {
		return getStringCurrentTime(new Date(), "yyyy-MM-dd");
	}
	
	
	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间的 yyyyMMdd 格式
	 */
	public static String getStringCurrentDateShort2() {
		return getStringCurrentTime(new Date(), "yyyyMMdd");
	}

	/**
	 * 获取时间 小时:分;秒 HH:mm:ss
	 * 
	 * @return 返回当前时间的时分秒
	 */
	public static String getStringCurrentTime() {
		return getStringCurrentTime(new Date(), "HH:mm:ss");
	}
	
	public static String StringToDate( String timeString) {
	    SimpleDateFormat formatter1=new SimpleDateFormat("yyyyMMddHHmmss");  
	    try {
			return getStringCurrentTime(formatter1.parse(timeString), "yyyy-HH-dd HH:mm:ss");
		} catch (ParseException e) {
			return "";
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(StringToDate("20140812142151"));
	}
}
