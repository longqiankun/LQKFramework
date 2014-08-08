package com.lqk.framework.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Context;
import android.text.TextUtils;
/**
 * 
 * @Company: Dilitech
 * @author longqiankun
 * @email qiankun.long@dilitech.com
 * @Title: DateUtil.java
 * @Description: 时间工具类
 * @version 1.0  
 * @created 2014-4-1 上午10:57:08
 */
public class DateUtil {
	private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat datetimeFormat2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat(
			"HH:mm:ss");
	
	private static final SimpleDateFormat cndateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日");

	 public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");

	    /**
	     * long time to string
	     * 
	     * @param timeInMillis
	     * @param dateFormat
	     * @return
	     */
	    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
	        return dateFormat.format(new Date(timeInMillis));
	    }

	    /**
	     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	     * 
	     * @param timeInMillis
	     * @return
	     */
	    public static String getTime(long timeInMillis) {
	        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	    }

	    /**
	     * get current time in milliseconds
	     * 
	     * @return
	     */
	    public static long getCurrentTimeInLong() {
	        return System.currentTimeMillis();
	    }

	    /**
	     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	     * 
	     * @return
	     */
	    public static String getCurrentTimeInString() {
	        return getTime(getCurrentTimeInLong());
	    }

	    /**
	     * get current time in milliseconds
	     * 
	     * @return
	     */
	    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
	        return getTime(getCurrentTimeInLong(), dateFormat);
	    }
	/**
	 * 获得当前日期时间
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String currentDatetime() {
		return datetimeFormat.format(now());
	}
	/**
	 * 得到本月的第一天
	 * @param c
	 * @return
	 */
	public static boolean is24Hour(Context context){
		return android.text.format.DateFormat.is24HourFormat(context);

	}
	/**
	 * @description 
	 * @param time 日期
	 * @return 返回 "yyyy年MM月dd日  HH:mm"这种格式的时间格式
	 * @throws ParseException
	 */
	public static String formatDateByFormat(String time) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date parse = sdf.parse(time);
		SimpleDateFormat sdfs=new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String format = sdfs.format(parse);
		/* GregorianCalendar ca = new GregorianCalendar();
		    System.out.println(ca.get(GregorianCalendar.AM_PM));*/
Calendar c=Calendar.getInstance();
c.setTime(parse);
int type=c.get(Calendar.AM_PM);
StringBuilder sb=new StringBuilder();
sb.append(format);
if(type==0){
sb.append("am");
}else{
sb.append("pm");
}
return sb.toString();
	}
	/**
	 * @description 
	 * @param time 日期
	 * @return 返回 "MM月dd日  HH:mm pm"这种格式的时间格式
	 * @throws ParseException
	 */
	public static String formatDateByFormat3(String time) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date parse = sdf.parse(time);
		SimpleDateFormat sdfs=new SimpleDateFormat("MM月dd日  HH:mm");
		String format = sdfs.format(parse);
		Calendar c=Calendar.getInstance();
		c.setTime(parse);
		int type=c.get(Calendar.AM_PM);
		StringBuilder sb=new StringBuilder();
		sb.append(format);
		if(type==0){
		sb.append("am");
		}else{
		sb.append("pm");
		}
		return sb.toString();
		
	}
	/**
	 * @description 根据指定的时间返回指定的格式
	 * @param pattern 时间格式
	 * @param time 要更改的时间
	 * @return  返回 指定de这种格式的时间格式
	 * @throws ParseException
	 */
	public static String formatDateByFormat(String pattern,String time) throws ParseException{
		if(TextUtils.isEmpty(time))return " ";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date parse = sdf.parse(time);
		SimpleDateFormat sdfs=new SimpleDateFormat(pattern);
		String format = sdfs.format(parse);
	
		return format;
	}
	/**
	 * @description 两种格式时间的转换
	 * @param pattern 已经转换的时间格式
	 * @param time 要转换的时间
	 * @param pattern2 要转换的时间格式
	 * @return
	 * @throws ParseException
	 */
	public static String formatDateByFormat(String pattern,String time,String pattern2) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat(pattern2);
		Date parse = sdf.parse(time);
		SimpleDateFormat sdfs=new SimpleDateFormat(pattern);
		String format = sdfs.format(parse);
return format;
	}

	public static String formatDateByFormat2(String time) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date parse = sdf.parse(time);
		SimpleDateFormat sdfs=new SimpleDateFormat("MM/dd HH:mm");
		String format = sdfs.format(parse);
		/* GregorianCalendar ca = new GregorianCalendar();
		    System.out.println(ca.get(GregorianCalendar.AM_PM));*/
/*Calendar c=Calendar.getInstance();
c.setTime(parse);
int type=c.get(Calendar.AM_PM);
StringBuilder sb=new StringBuilder();
sb.append(format);
if(type==0){
sb.append("am");
}else{
sb.append("pm");format.replace("-", "/")
}*/
return format;
	}
/**
 * @description 获取两个时间的间隔天数
 * @param time1 要比较的时间
 * @param time2 要比较的时间
 * @param pattern 比较的时间格式
 * @return 时间相隔的天数
 */
	public static long getQuot(String time1, String time2,String pattern) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat(pattern);
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}
	/**
	 * @description 根据指定的时间返回指定的格式
	 * @param pattern 时间格式
	 * @param time 要更改的时间
	 * @return  返回 指定de这种格式的时间格式
	 * @throws ParseException
	 */
	public static String formatDateByFormat(String pattern,Date time) throws ParseException{
		SimpleDateFormat sdfs=new SimpleDateFormat(pattern);
		String format = sdfs.format(time);
		return format;
/*Calendar c=Calendar.getInstance();
c.setTime(parse);
int type=c.get(Calendar.AM_PM);
StringBuilder sb=new StringBuilder();
sb.append(format);
if(type==0){
sb.append("am");
}else{
sb.append("pm");
}
return sb.toString();*/
	}
public static String firstdayofcurrentmonth(Calendar c){
	 c.set( Calendar.DATE,  1 );
	 return formatDate(c.getTime());
}
public static String yearmonthweek(){
	Calendar c=Calendar.getInstance();
	int year=c.get(Calendar.YEAR);
	int month=c.get(Calendar.MONTH)+1;
	int week=c.get(Calendar.WEEK_OF_MONTH);
	StringBuilder sb=new StringBuilder();
	sb.append(year);
	sb.append("_");
	sb.append(month);
	sb.append("_");
	sb.append(week);
	 return sb.toString();
}
public static String getbeforeday(String time){
	Calendar   c   =   Calendar.getInstance(); 
	try {
		c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(time));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	c.add(Calendar.DAY_OF_MONTH, -1);
	String strStart=DateUtil.formatDate(c.getTime());
	return strStart;
}
public static String getlastday(String time){
	Calendar   c   =   Calendar.getInstance(); 
	try {
		c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(time));
	} catch (ParseException e) {
		e.printStackTrace();
	}
	c.add(Calendar.DAY_OF_MONTH, 1);
	String strStart=DateUtil.formatDate(c.getTime());
	return strStart;
}
/**
 * 得到本月的第后一天
 * @param c
 * @return
 */
public static String lastdayofcurrentmonth(Calendar c){
 c.set( Calendar.DATE,  1 );
 c.roll(Calendar.DATE,  - 1 );
 return formatDate(c.getTime());
}
//获得下个月第一天的日期
public static String getNextMonthFirst(Calendar lastDate){
	  lastDate.add(Calendar.MONTH,1);//减一个月
	  lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天 
	   return formatDate(lastDate.getTime()); 
}
	/**
	 * 格式化日期时间
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String formatDatetime(Date date) {
		return datetimeFormat.format(date);
	}
	/**
	 * 格式化日期时间
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm
	 * 
	 * @return
	 */
	public static String formatDatetime2(Date date) {
		return datetimeFormat2.format(date);
	}

	/**
	 * 格式化日期时间
	 * 
	 * @param date
	 * @param pattern
	 *            格式化模式，详见{@link SimpleDateFormat}构造器
	 *            <code>SimpleDateFormat(String pattern)</code>
	 * @return
	 */
	public static String formatDatetime(Date date, String pattern) {
		SimpleDateFormat customFormat = (SimpleDateFormat) datetimeFormat
				.clone();
		customFormat.applyPattern(pattern);
		return customFormat.format(date);
	}

	/**
	 * 获得当前日期
	 * <p>
	 * 日期格式yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String currentDate() {
		return dateFormat.format(now());
	}

	/**
	 * 获得当前日期
	 * <p>
	 * 日期格式yyyy-MM-dd HH:mm
	 * 
	 * @return
	 */
	public static String currentDateTimeNoSecond() {
		return datetimeFormat2.format(now());
	}
	/**
	 * 格式化日期
	 * <p>
	 * 日期格式yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String formatDateTimeNoSecond(Date date) {
		return datetimeFormat2.format(date);
	}
	/**
	 * 格式化日期
	 * <p>
	 * 日期格式yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}
	
	/**
	 * 格式化日期 中文方式
	 * <p>
	 * 日期格式yyyy年MM月dd日
	 * 
	 * @return
	 */
	public static String formatCndate(Date date) {
		return cndateFormat.format(date);
	}

	/**
	 * 获得当前时间
	 * <p>
	 * 时间格式HH:mm:ss
	 * 
	 * @return
	 */
	public static String currentTime() {
		return timeFormat.format(now());
	}

	/**
	 * 格式化时间
	 * <p>
	 * 时间格式HH:mm:ss
	 * 
	 * @return
	 */
	public static String formatTime(Date date) {
		return timeFormat.format(date);
	}


	public static Calendar calendar() {
		Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		return cal;
	}

	/**
	 * 获得当前时间的毫秒数
	 * <p>
	 * 详见{@link System#currentTimeMillis()}
	 * 
	 * @return
	 */
	public static long millis() {
		return System.currentTimeMillis();
	}

	/**
	 * 
	 * 获得当前Chinese月份
	 * 
	 * @return
	 */
	public static int month() {
		return calendar().get(Calendar.MONTH) + 1;
	}

	/**
	 * 获得月份中的第几天
	 * 
	 * @return
	 */
	public static int dayOfMonth() {
		return calendar().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 今天是星期的第几天
	 * 
	 * @return
	 */
	public static int dayOfWeek() {
		return calendar().get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 今天是年中的第几天
	 * 
	 * @return
	 */
	public static int dayOfYear() {
		return calendar().get(Calendar.DAY_OF_YEAR);
	}

	/**
	 *判断原日期是否在目标日期之前
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean isBefore(Date src, Date dst) {
		return src.before(dst);
	}

	/**
	 *判断原日期是否在目标日期之后
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean isAfter(Date src, Date dst) {
		return src.after(dst);
	}

	/**
	 *判断两日期是否相同
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isEqual(Date date1, Date date2) {
		return date1.compareTo(date2) == 0;
	}

	/**
	 * 判断某个日期是否在某个日期范围
	 * 
	 * @param beginDate
	 *            日期范围开始
	 * @param endDate
	 *            日期范围结束
	 * @param src
	 *            需要判断的日期
	 * @return
	 */
	public static boolean between(Date beginDate, Date endDate, Date src) {
		return beginDate.before(src) && endDate.after(src);
	}

	/**
	 * 获得当前月的最后一天
	 * <p>
	 * HH:mm:ss为0，毫秒为999
	 * 
	 * @return
	 */
	public static Date lastDayOfMonth() {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_MONTH, 0); // M月置零
		cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
		cal.set(Calendar.MINUTE, 0);// m置零
		cal.set(Calendar.SECOND, 0);// s置零
		cal.set(Calendar.MILLISECOND, 0);// S置零
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// 月份+1
		cal.set(Calendar.MILLISECOND, -1);// 毫秒-1
		return cal.getTime();
	}

	/**
	 * 获得当前月的第一天
	 * <p>
	 * HH:mm:ss SS为零
	 * 
	 * @return
	 */
	public static Date firstDayOfMonth() {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
		cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
		cal.set(Calendar.MINUTE, 0);// m置零
		cal.set(Calendar.SECOND, 0);// s置零
		cal.set(Calendar.MILLISECOND, 0);// S置零
		return cal.getTime();
	}

	private static Date weekDay(int week) {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_WEEK, week);
		return cal.getTime();
	}

	/**
	 * 获得周五日期
	 * <p>
	 * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	 * 
	 * @return
	 */
	public static Date friday() {
		return weekDay(Calendar.FRIDAY);
	}

	/**
	 * 获得周六日期
	 * <p>
	 * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	 * 
	 * @return
	 */
	public static Date saturday() {
		return weekDay(Calendar.SATURDAY);
	}

	/**
	 * 获得周日日期
	 * <p>
	 * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	 * 
	 * @return
	 */
	public static Date sunday() {
		return weekDay(Calendar.SUNDAY);
	}

	/**
	 * 将字符串日期时间转换成java.util.Date类型
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 * @return
	 */
	public static Date parseDatetime(String datetime) throws ParseException {
		return datetimeFormat.parse(datetime);
	}

	/**
	 * 将字符串日期转换成java.util.Date类型
	 *<p>
	 * 日期时间格式yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date) throws ParseException {
		return dateFormat.parse(date);
	}

	/**
	 * 将字符串日期转换成java.util.Date类型
	 *<p>
	 * 时间格式 HH:mm:ss
	 * 
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTime(String time) throws ParseException {
		return timeFormat.parse(time);
	}

	/**
	 * 根据自定义pattern将字符串日期转换成java.util.Date类型
	 * 
	 * @param datetime
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDatetime(String datetime, String pattern)
			throws ParseException {
		SimpleDateFormat format = (SimpleDateFormat) datetimeFormat.clone();
		format.applyPattern(pattern);
		return format.parse(datetime);
	}
	/**
     * @return the long value of {@link System#currentTimeMillis()}
     */
    public static long getCurrentTimeMillis() {
        return Long.valueOf(System.currentTimeMillis());
    }
    
    /**
     * 
     * @param context
     * @param date 传入参数为时间字符串，格式为： yyyy-MM-dd HH:mm:ss，比如 2012-08-06 12:12:12
     * @return 根据传入的时间字符串判断是今天，昨天还是更早
     * @throws ParseException
     */
    public static String getDay(Context context,String date) {
    	//2012-08-06 12:12:12
    	
    	try {
			String current = currentDate();
			
			Date currentDate = (Date) dateFormat.parse(current);
			Date date2 = (Date) dateFormat.parse(date.substring(0,10));
			
			if (currentDate.hashCode() - date2.hashCode() == 0 ) {
				return "今天";
			} else if (currentDate.hashCode() - date2.hashCode() > 0 && currentDate.hashCode() - date2.hashCode() <= 86400000) {
				return "昨天";
			} else if (currentDate.hashCode() - date2.hashCode() < 0) {
//				return context.getString(R.string.from_future);
				return "更早";
			}else {
				return "更早";
			}
		} catch (ParseException e) {
			return date;
		}
    }
	//用来全局控制 上一周，本周，下�?��的周数变�?


	/**
	    * 得到二个日期间的间隔天数
	    */
	public static String getTwoDay(String sj1, String sj2) {
	    SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
	    long day = 0;
	    try {
	     java.util.Date date = myFormatter.parse(sj1);
	     java.util.Date mydate = myFormatter.parse(sj2);
	     day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
	    } catch (Exception e) {
	     return "";
	    }
	    return day + "";
	}

/**
 * @description: 获取当月第一天
 * @param lastDate
 * @return
 */
	public static String getFirstDayOfMonth( Calendar lastDate){  
	   String str = "";
	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    
	   lastDate.set(Calendar.DATE,1);//设为当前月的1�?
	   str=sdf.format(lastDate.getTime());
	   return str;  
	}
	/**
	    * @description: 根据日期，返回是星期几的字符
	    * 
	    * @param sdate
	    * @return
	    */
	public static String getWeek(String sdate) {
	    // 再转换为时间
//	    Date date = DataFLDay.strToDate(sdate);
		 Date date =strToDate(sdate);
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    // int hour=c.get(Calendar.DAY_OF_WEEK);
	    // hour中存的就是星期几了，其范�?1~7
	    // 1=星期�?7=星期六，其他类推
	    return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	/**
	    * 将短时间格式字符串转换为时间 yyyy-MM-dd 
	    * 
	    * @param strDate
	    * @return
	    */
	public static Date strToDate(String strDate) {
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    ParsePosition pos = new ParsePosition(0);
	    Date strtodate = formatter.parse(strDate, pos);
	    return strtodate;
	}

	/**
	    * 两个时间之间的天�?
	    * 
	    * @param date1
	    * @param date2
	    * @return
	    */
	public static long getDays(String date1, String date2) {
	    if (date1 == null || date1.equals(""))
	     return 0;
	    if (date2 == null || date2.equals(""))
	     return 0;
	    // 转换为标准时�?
	    SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
	    java.util.Date date = null;
	    java.util.Date mydate = null;
	    try {
	     date = myFormatter.parse(date1);
	     mydate = myFormatter.parse(date2);
	    } catch (Exception e) {
	    }
	    long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
	    return day;
	}



	
	// 计算当月�?���?��,返回字符�?
	public static String getDefaultDay(){  
	   String str = "";
	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    

	   Calendar lastDate = Calendar.getInstance();
	   lastDate.set(Calendar.DATE,1);//设为当前月的1�?
	   lastDate.add(Calendar.MONTH,1);//加一个月，变为下月的1�?
	   lastDate.add(Calendar.DATE,-1);//减去�?��，变为当月最后一�?
	   
	   str=sdf.format(lastDate.getTime());
	   return str;  
	}
	
	// 上月第一�?
	public static String getPreviousMonthFirst(){  
	   String str = "";
	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    

	   Calendar lastDate = Calendar.getInstance();
	   lastDate.set(Calendar.DATE,1);//设为当前月的1�?
	   lastDate.add(Calendar.MONTH,-1);//减一个月，变为下月的1�?
	   //lastDate.add(Calendar.DATE,-1);//减去�?��，变为当月最后一�?
	   
	   str=sdf.format(lastDate.getTime());
	   return str;  
	}
	
	//获取当月第一�?
	public static String getFirstDayOfMonth(){  
	   String str = "";
	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    

	   Calendar lastDate = Calendar.getInstance();
	   lastDate.set(Calendar.DATE,1);//设为当前月的1�?
	   str=sdf.format(lastDate.getTime());
	   return str;  
	}
	
	// 获得本周星期日的日期  
    public static String getCurrentWeekday() {
          int weeks = 0;
         int MaxDate;//�?���?��天数
         int MaxYear;//�?���?��天数
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus+6);
        Date monday = currentDate.getTime();
        
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }
    
    
	//获取当天时间 
    public static String getNowTime(String dateformat){
		Date   now   =   new   Date();   
		SimpleDateFormat   dateFormat   =   new   SimpleDateFormat(dateformat);//可以方便地修改日期格�?  
		String  hehe  = dateFormat.format(now);   
		return hehe;
	}
    
    // 获得当前日期与本周日相差的天�?
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二�?.....
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK)-1; 		//因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }
    
    //获得本周日期
    public static String getMondayOFWeek(){
    	 int weeks = 0;
         int MaxDate;
         int MaxYear;
    	 weeks = 0;
         int mondayPlus = getMondayPlus();
         GregorianCalendar currentDate = new GregorianCalendar();
         currentDate.add(GregorianCalendar.DATE, mondayPlus);
         Date monday = currentDate.getTime();
         
         DateFormat df = DateFormat.getDateInstance();
         String preMonday = df.format(monday);
         return preMonday;
    }
    
  //获得相应周的周六
    public static String getSaturday() {
    	 int weeks = 0;
         int MaxDate;
         int MaxYear;
        int mondayPlus =getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }
    
 // 获得上周星期日的日期
    public static String getPreviousWeekSunday() {
    	 int weeks = 0;
         int MaxDate;
         int MaxYear;
    	weeks=0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus+weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

 // 获得上周星期
    public static String getPreviousWeekday() {
    	 int weeks = 0;
         int MaxDate;
         int MaxYear;
        weeks--;
        int mondayPlus =getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }
    
    // 获得下周星期
    public static String getNextMonday() {
    	 int weeks = 0;
         int MaxDate;
         int MaxYear;
        weeks++;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }
    
 // 获得下周星期日的日期
    public static String getNextSunday() {
        
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7+6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }
    
    
    
    private static int getMonthPlus(){
    	 int weeks = 0;
         int MaxDate;
         int MaxYear;
    	Calendar cd = Calendar.getInstance();
    	int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
    	cd.set(Calendar.DATE, 1);
    	cd.roll(Calendar.DATE, -1);
    	MaxDate=cd.get(Calendar.DATE); 
    	if(monthOfNumber == 1){
    		return -MaxDate;
    	}else{
    		return 1-monthOfNumber;
    	}
    }
    
/**
 * @desription: 获取上个月最后一天
 * @return
 */
    public static String getPreviousMonthEnd(){
    	String str = "";
 	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    

 	   Calendar lastDate = Calendar.getInstance();
 	  lastDate.add(Calendar.MONTH,-1);//减一个月
 	  lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一�?
 	  lastDate.roll(Calendar.DATE, -1);//日期回滚�?��，也就是本月�?���?�� 
 	   str=sdf.format(lastDate.getTime());
 	   return str;  
    }
    
    /**
     * @desription: 获得下个月第一天的日期
     * @return
     */
    public static String getNextMonthFirst(){
    	String str = "";
 	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    

 	   Calendar lastDate = Calendar.getInstance();
 	  lastDate.add(Calendar.MONTH,1);//减一个月
 	  lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一�?
 	   str=sdf.format(lastDate.getTime());
 	   return str;  
    }
/*    
    public static String getNextMonthFirst(Calendar lastDate){
    	String str = "";
 	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    
 	  lastDate.add(Calendar.MONTH,1);//减一个月
 	  lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一�?
 	   str=sdf.format(lastDate.getTime());
 	   return str;  
    }*/
  //获得下个月最后一天的日期
    public static String getNextMonthEnd(){
    	String str = "";
 	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    

 	   Calendar lastDate = Calendar.getInstance();
 	  lastDate.add(Calendar.MONTH,1);//加一个月
 	  lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一�?
 	  lastDate.roll(Calendar.DATE, -1);//日期回滚�?��，也就是本月�?���?�� 
 	   str=sdf.format(lastDate.getTime());
 	   return str;  
    }
    
    /**
     * @description : 获得明年最后的日期
     * @return
     */
    public static String getNextYearEnd(){
    	String str = "";
 	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    

 	  Calendar lastDate = Calendar.getInstance();
 	  lastDate.add(Calendar.YEAR,1);//加一个年
 	  lastDate.set(Calendar.DAY_OF_YEAR, 1);
 	 lastDate.roll(Calendar.DAY_OF_YEAR, -1);
 	   str=sdf.format(lastDate.getTime());
 	   return str;  
    }
    
    /**
     * @descrioption : 获得明年第一天的日期
     * @return
     */
    public static String getNextYearFirst(){
    	String str = "";
  	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    

  	  Calendar lastDate = Calendar.getInstance();
  	  lastDate.add(Calendar.YEAR,1);//加一个年
  	  lastDate.set(Calendar.DAY_OF_YEAR, 1);
           str=sdf.format(lastDate.getTime());
  	  return str;  
    	
    }
    
    /**
     * @descrioption :获得本年有多少天
     * @return
     */
    private static int getMaxYear(){
    	Calendar cd = Calendar.getInstance();
    	cd.set(Calendar.DAY_OF_YEAR,1);
    	cd.roll(Calendar.DAY_OF_YEAR,-1);
    	int MaxYear = cd.get(Calendar.DAY_OF_YEAR);	
    	return MaxYear;
    }
    /**
     * @descrioption :获得当天是一年中的第几天
     * @return
     */
    private static int getYearPlus(){
    	Calendar cd = Calendar.getInstance();
    	int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);//
    	cd.set(Calendar.DAY_OF_YEAR,1);
    	cd.roll(Calendar.DAY_OF_YEAR,-1);
    	int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
    	if(yearOfNumber == 1){
    		return -MaxYear;
    	}else{
    		return 1-yearOfNumber;
    	}
    }
    /**
     * @descrioption :获得本年第一天的日期
     * @return
     */
    public static String getCurrentYearFirst(){
    	int yearPlus =getYearPlus();
    	GregorianCalendar currentDate = new GregorianCalendar();
    	currentDate.add(GregorianCalendar.DATE,yearPlus);
    	Date yearDay = currentDate.getTime();
    	DateFormat df = DateFormat.getDateInstance();
    	String preYearDay = df.format(yearDay);
    	return preYearDay;
    }
    
    
    public static String getCurrentYearEnd(){
    	Date date = new Date();
		SimpleDateFormat   dateFormat   =   new   SimpleDateFormat("yyyy");//可以方便地修改日期格�?  
		String  years  = dateFormat.format(date);   
		return years+"-12-31";
    }
    
    
  //获得上年第一天的日期 *
    public static String getPreviousYearFirst(){
    	Date date = new Date();
		SimpleDateFormat   dateFormat   =   new   SimpleDateFormat("yyyy");//可以方便地修改日期格�?  
		String  years  = dateFormat.format(date); int years_value = Integer.parseInt(years);  
		years_value--;
		return years_value+"-1-1";
    }
    
    public static String getPreviousYearEnd(){
    	 int weeks = 0;
         int MaxDate=0;
         int MaxYear=0;
    	weeks--;
    	int yearPlus = getYearPlus();
    	GregorianCalendar currentDate = new GregorianCalendar();
    	currentDate.add(GregorianCalendar.DATE,yearPlus+MaxYear*weeks+(MaxYear-1));
    	Date yearDay = currentDate.getTime();
    	DateFormat df = DateFormat.getDateInstance();
    	String preYearDay = df.format(yearDay);
    	getThisSeasonTime(11);
    	return preYearDay;
    }
    
	public static String getThisSeasonTime(int month){
		int array[][] = {{1,2,3},{4,5,6},{7,8,9},{10,11,12}};
		int season = 1;
		if(month>=1&&month<=3){
			season = 1;
		}
		if(month>=4&&month<=6){
			season = 2;
		}
		if(month>=7&&month<=9){
			season = 3;
		}
		if(month>=10&&month<=12){
			season = 4;
		}
		int start_month = array[season-1][0];
		int end_month = array[season-1][2];
		
		Date date = new Date();
		SimpleDateFormat   dateFormat   =   new   SimpleDateFormat("yyyy");//可以方便地修改日期格�?  
		String  years  = dateFormat.format(date);   
		int years_value = Integer.parseInt(years);
		
		int start_days =1;//years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
		int end_days = getLastDayOfMonth(years_value,end_month);
		String seasonDate = years_value+"-"+start_month+"-"+start_days+";"+years_value+"-"+end_month+"-"+end_days;
		return seasonDate;
		
	}
	
	/**
     * 获取某年某月的最后一天
     * @param year 年
     * @param month 月
     * @return 指定日期的最后一天
     */
   private static int getLastDayOfMonth(int year, int month) {
         if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                   || month == 10 || month == 12) {
               return 31;
         }
         if (month == 4 || month == 6 || month == 9 || month == 11) {
               return 30;
         }
         if (month == 2) {
               if (isLeapYear(year)) {
                   return 29;
               } else {
                   return 28;
               }
         }
         return 0;
   }
   /**
    * 是否闰年
    * @param year 年
    * @return true : 闰年 false : 不是闰年
    */
  public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
  }
  
  private Calendar cal;

	private DateUtil() {
		this.cal = Calendar.getInstance();
	}

	public static DateUtil getInstance() {
		return new DateUtil();
	}

	public static DateUtil getInstance(String year, String month) {
		DateUtil th = new DateUtil();
		if (year != null && !year.equals("") && !year.equals("null") && month != null && !month.equals("") && !month.equals("null")) {
			th.cal.set(Calendar.YEAR, Integer.parseInt(year));
			th.cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		}
		return th;
	}

	public static DateUtil getInstance(String year, String month, String day) {
		DateUtil th = new DateUtil();
		if (year != null && !year.equals("") && !year.equals("null") && month != null && !month.equals("") && !month.equals("null") && day != null && !day.equals("") && !day.equals("null")) {
			th.cal.set(Calendar.YEAR, Integer.parseInt(year));
			th.cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
			th.cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		}
		return th;
	}

	/**
	 * month is start with 1
	 * 
	 * @author LiuSong | mailto:liu@ezcom.net.cn | 2007/06/05 14:20:09
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static DateUtil getInstance(int year, int month, int day) {
		DateUtil th = new DateUtil();
		th.cal.set(Calendar.YEAR, year);
		th.cal.set(Calendar.MONTH, month - 1);
		th.cal.set(Calendar.DAY_OF_MONTH, day);

		return th;
	}

	/**
	 * @author 潘城 gdpancheng@gmail.com 2012-5-7 下午5:25:14
	 * @Title: getInstance
	 * @Description: 根据TimeStamp格式的字符串来获得TimeHandler的实例<br>
	 *               字符串格式可以为以下多种格式<br>
	 *               年：2007<br>
	 *               年月：2007-05<br>
	 *               年月日：2007-05-08<br>
	 *               年月日小时：2007-05-08 12<br>
	 *               年月日小时分：2007-05-08 12:10<br>
	 *               年月日小时分秒：2007-05-08 12:10:08
	 * @param @param date_time
	 * @param @return 设定文件
	 * @return TimeHandler 返回类型
	 */
	public static DateUtil getInstance(String date_time) {
		int stringLength = date_time.length();
		if (stringLength < 4) {
			return null;
		}
		DateUtil th = new DateUtil();
		int year = Integer.parseInt(date_time.substring(0, 4));
		int month = 0;
		int day = 1;
		if (stringLength >= 7) {
			month = Integer.parseInt(date_time.substring(5, 7)) - 1;
		}
		if (stringLength >= 10) {
			day = Integer.parseInt(date_time.substring(8, 10));
		}
		// ------------------------------------------------------------
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (stringLength >= 13) {
			hour = Integer.parseInt(date_time.substring(11, 13));
		}
		if (stringLength >= 16) {
			minute = Integer.parseInt(date_time.substring(14, 16));
		}
		if (stringLength == 19) {
			second = Integer.parseInt(date_time.substring(17, 19));
		}
		th.set(year, month, day, hour, minute, second);
		return th;
	}

	public static DateUtil getInstance(long timeInMillis) {
		DateUtil th = new DateUtil();
		th.set(timeInMillis);
		return th;
	}

	public Timestamp getTimestamp() {
		return new Timestamp(cal.getTimeInMillis());
	}

	public String getTimestampStr() {
		String str = (new Timestamp(cal.getTimeInMillis())).toString();
		while (str.length() < 23) {
			str += 0;
		}
		return str;
	}
	
	public String getTimestampSecond() {
		String str = (new Timestamp(cal.getTimeInMillis())).toString();
		return str.substring(0,19);
	}

	public Timestamp getTimestampPlus(long timeInMillis) {
		return new Timestamp(cal.getTimeInMillis() + timeInMillis);
	}

	public Timestamp getTimestamp(int year, int month, int day, int hour, int minute) {
		cal.set(year, month, day, hour, minute, 0);
		return new Timestamp(cal.getTimeInMillis());
	}

	public int getYear() {
		return cal.get(Calendar.YEAR);
	}

	/**
	 * @author 潘城 gdpancheng@gmail.com 2012-5-7 下午5:25:43
	 * @Title: getYearSimple
	 * @Description: 获得年的后两位数字<br>
	 *               例如，2008获得8，1998获得98<br>
	 * @param @return 设定文件
	 * @return int 返回类型
	 */
	public int getYearSimple() {
		int year = cal.get(Calendar.YEAR);
		int yearSimple;
		if (year > 2000) {
			yearSimple = year - 2000;
		} else if (year > 1900) {
			yearSimple = year - 1900;
		} else if (year > 1800) {
			yearSimple = year - 1800;
		} else {
			yearSimple = year - 1700;
		}
		return yearSimple;
	}

	/**
	 * 获得年的后两位数字的字符串<br>
	 * 例如，2008获得08，1998获得98<br>
	 * 
	 * @return
	 */
	public String getYearSimpleStr() {
		int year = getYearSimple();
		return StringUtils.addPrefixZero(year);

	}

	public int getYearPrev() {
		if (this.getMonth() == 1) {
			return this.getYear() - 1;
		} else {
			return this.getYear();
		}
	}

	public int getYearNext() {
		if (this.getMonth() == 12) {
			return this.getYear() + 1;
		} else {
			return this.getYear();
		}
	}

	public String getYearStr() {
		return String.valueOf(this.getYear());
	}

	public int getMonth() {
		return cal.get(Calendar.MONTH) + 1;
	}

	public int getMonthPrev() {
		if (this.getMonth() == 1) {
			return 12;
		} else {
			return this.getMonth() - 1;
		}
	}

	public int getMonthNext() {
		if (this.getMonth() == 12) {
			return 1;
		} else {
			return this.getMonth() + 1;
		}
	}

	public String getMonthStr() {
		return StringUtils.addPrefixZero(this.getMonth());
	}

	public int getDay() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public String getDayStr() {
		return StringUtils.addPrefixZero(this.getDay());
	}

	public int getHour() {
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public String getHourStr() {
		return StringUtils.addPrefixZero(this.getHour());
	}

	public int getMinute() {
		return cal.get(Calendar.MINUTE);
	}

	public String getMinuteStr() {
		return StringUtils.addPrefixZero(this.getMinute());
	}

	public int getSecond() {
		return cal.get(Calendar.SECOND);
	}

	public String getSecondStr() {
		return StringUtils.addPrefixZero(this.getSecond());
	}

	/**
	 * 获得TimeStamp格式的年月日字符串<br>
	 * 例：2007-05-08
	 * 
	 * @return
	 */
	public String getYYYYMMDD() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(this.getYearStr());
		sbf.append("-");
		sbf.append(this.getMonthStr());
		sbf.append("-");
		sbf.append(this.getDayStr());
		return sbf.toString();
	}

	/**
	 * 获得中日文的年月日字符串<br>
	 * 例：2007年05月08日
	 * 
	 * @return
	 */
	public String getYYYYMMDDLabel() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(this.getYearStr()).append("年");
		sbf.append(this.getMonthStr()).append("月");
		sbf.append(this.getDayStr()).append("日");
		return sbf.toString();
	}

	/**
	 * 获得前一天的TimeStamp格式的年月日字符串<br>
	 * 例：2007-05-08
	 * 
	 * @return
	 */
	public String getYYYYMMDDPrevious() {
		DateUtil previousDay = DateUtil.getInstance();
		return previousDay.getYYYYMMDD();
	}

	/**
	 * 获得后一天的TimeStamp格式的年月日字符串<br>
	 * 例：2007-05-08
	 * 
	 * @return
	 */
	public String getYYYYMMDDNext() {
		DateUtil nextDay = getTimeHandlerNext();
		return nextDay.getYYYYMMDD();
	}

	/**
	 * 获得前一天的TimeHandler实例<br>
	 * 
	 * @return
	 */
	public DateUtil getTimeHandlerPrevious() {
		long todayTimeInMillis = this.getTimeInMillis();
		DateUtil previousDay = DateUtil.getInstance(todayTimeInMillis - 24 * 3600 * 1000l);
		return previousDay;
	}

	/**
	 * 获得后一天的TimeHandler实例<br>
	 * 
	 * @return
	 */
	public DateUtil getTimeHandlerNext() {
		long todayTimeInMillis = this.getTimeInMillis();
		DateUtil nextDay = DateUtil.getInstance(todayTimeInMillis + 24 * 3600 * 1000l);
		return nextDay;
	}

	/**
	 * 获得TimeStamp格式的年月字符串<br>
	 * 例：2007-05
	 * 
	 * @return
	 */
	public String getYYYYMM() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(this.getYearStr());
		sbf.append("-");
		sbf.append(this.getMonthStr());
		return sbf.toString();
	}

	/**
	 * 获得中日文的年月字符串<br>
	 * 例：2007年05月
	 * 
	 * @return
	 */
	public String getYYYYMMLabel() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(this.getYearStr()).append("年");
		sbf.append(this.getMonthStr()).append("月");
		return sbf.toString();
	}

	/**
	 * 获得前一个月的TimeStamp格式的年月字符串<br>
	 * 例：2007-05
	 * 
	 * @return
	 */
	public String getYYYYMMPrevious() {
		int year = this.getYear();
		int previousMonth = getMonthPrev();
		if (previousMonth == 12) {
			year = year - 1;
		}
		StringBuffer sbf = new StringBuffer();
		sbf.append(year).append("-").append(StringUtils.addPrefixZero(previousMonth));
		return sbf.toString();
	}

	/**
	 * 获得下一个月的TimeStamp格式的年月字符串<br>
	 * 例：2007-05
	 * 
	 * @return
	 */
	public String getYYYYMMNext() {
		int year = this.getYear();
		int nextMonth = getMonthNext();
		if (nextMonth == 1) {
			year = year + 1;
		}
		StringBuffer sbf = new StringBuffer();
		sbf.append(year).append("-").append(StringUtils.addPrefixZero(nextMonth));
		return sbf.toString();
	}

	public String getYyyyMmKanji() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(this.getYearStr());
		sbf.append(" 年 ");
		sbf.append(this.getMonth());
		sbf.append(" 月 ");
		return sbf.toString();
	}

	public void set(int year, int month, int day, int hour, int minute, int second) {
		cal.set(year, month, day, hour, minute, second);
	}

	public void set(int year, int month, int day, int hour, int minute) {
		cal.set(year, month, day, hour, minute, 0);
	}

	public void set(int field, int value) {
		cal.set(field, value);
	}

	public void set(long timeInMillis) {
		cal.setTimeInMillis(timeInMillis);
	}

	public long getTimeInMillis() {
		return cal.getTimeInMillis();
	}

	/**
	 * 获得UNIX_TIMESTAMP的秒的INT型数字的值<br>
	 * 值的长度应该和MYSQL的UNIX_TIMESTAMP()函数的值的长度是一致的<br>
	 * 例如：1196440210<br>
	 * 例如：1246026128<br>
	 * 
	 * @return UNIX_TIMESTAMP的秒的INT型数字的值
	 */
	public int getTimeInSeconds() {
		return (int) (this.getTimeInMillis() / 1000);
	}

	public boolean checkDate(int year, int month, int day) {
		cal.set(year, month, 1);
		if (day > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得当月的天数
	 * 
	 * @return
	 */
	public int getMaxDayOfTheMonth() {
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 
	 * @return
	 */
	public int getDayOfTheWeek() {
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public Date getDate() {
		return new Date(this.getTimeInMillis());
	}

	public Calendar getCalendar() {
		return cal;
	}

	public static String now() {
		return DateUtil.getInstance().getTimestamp().toString().substring(0, 19);
	}

	public String getPeriodStr(long timeInMillis) {
		long result = 0;
		StringBuffer sbf = new StringBuffer();
		//
		result = (timeInMillis / 1000) / 3600;
		if (result > 0) {
			sbf.append(StringUtils.addPrefixZero((int) result)).append(":");
			timeInMillis = timeInMillis - result * 3600 * 1000;
		} else {
			sbf.append("00:");
		}
		//
		result = (timeInMillis / 1000) / 60;
		if (result > 0) {
			sbf.append(StringUtils.addPrefixZero((int) result)).append(":");
			timeInMillis = timeInMillis - result * 60 * 1000;
		} else {
			sbf.append("00:");
		}
		//
		result = timeInMillis / 1000;
		sbf.append(StringUtils.addPrefixZero((int) result));
		return sbf.toString();
	}

	public static DateUtil linuxTimeToWinTime(DateUtil timeHandler) {
		int year = timeHandler.getYear();
		int month = timeHandler.getMonth();
		int day = timeHandler.getDay();
		int hour = timeHandler.getHour();
		int minute = timeHandler.getMinute();
		int second = timeHandler.getSecond();
		hour = hour + 8;
		timeHandler.set(year, month - 1, day, hour, minute, second);
		return timeHandler;
	}

	public String toString() {
		return this.getTimestampStr();
	}
	
	public static String formatDate(int year, int month, int day) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(year);
		sbf.append("-");
		sbf.append(StringUtils.addPrefixZero(month + 1));
		sbf.append("-");
		sbf.append(StringUtils.addPrefixZero(day));
		return sbf.toString();
	}
	
	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		return days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
	}
}