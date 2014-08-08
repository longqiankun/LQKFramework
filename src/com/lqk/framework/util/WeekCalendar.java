package com.lqk.framework.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * 
* @ClassName: WeekCalendar
* @Description: 周日历操作类
* @author longqiankun
* @date 2014-7-7 下午12:17:13
*
 */
public class WeekCalendar {

	private Calendar mCalendar;//当前日历
	//将当前类设置为单利
/*	private static WeekCalendar instance;
	private WeekCalendar(){
		mCalendar=Calendar.getInstance();
	}
	public synchronized static WeekCalendar getInstance(){
		if(instance==null){
			instance=new WeekCalendar();
		}
		return instance;
	}*/
	public WeekCalendar(){
		mCalendar=Calendar.getInstance();
	}
	/**
	 * @description 上一周
	 * @param pos
	 */
	public void setPreWeek(int pos){
		int offset=(Integer.MAX_VALUE/2)%pos;
		mCalendar.add(Calendar.WEEK_OF_YEAR, -offset);
	}
	/**
	 * @description 设置下一周
	 * @param pos
	 */
	public void setNextWeek(int pos){
		//如果当前的Pos当前整数一半，则将整数一半减去他们的差值。
		if(pos>Integer.MAX_VALUE/2){
			pos=Integer.MAX_VALUE/2-(pos-Integer.MAX_VALUE/2);
		}
		int offset=(Integer.MAX_VALUE/2)%pos;
		mCalendar.add(Calendar.WEEK_OF_YEAR, offset);
	}
	/**
	 * 
	* @Title: getDates
	* @Description:获取一周的日期
	* @param @return
	* @return List<Date>
	* @throws
	 */
	public List<Date> getDates(){
		List<Date> mDates=new ArrayList<Date>();
		//将周一设为本周的第一天
		mCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		 for (int i = Calendar.MONDAY; i <= (Calendar.MONDAY+6); i++) {	 
			 mCalendar.set(Calendar.DAY_OF_WEEK, i);	
			 mDates.add(mCalendar.getTime());
		 }
		 return mDates;
	}
	/**
	 * 
	* @Title: getDates
	* @Description: 获取指定的周日期
	* @param @param pos
	* @param @return
	* @return List<Date>
	* @throws
	 */
	public List<Date> getDates(int pos){
		Calendar	mCalendar=Calendar.getInstance();
		if(pos==Integer.MAX_VALUE/2){
			int offset=(Integer.MAX_VALUE/2)%pos;
			mCalendar.add(Calendar.WEEK_OF_YEAR, offset);
		}else if(pos>Integer.MAX_VALUE/2){
			pos=Integer.MAX_VALUE/2-(pos-Integer.MAX_VALUE/2);
			int offset=(Integer.MAX_VALUE/2)%pos;
			mCalendar.add(Calendar.WEEK_OF_YEAR, offset);
		}else{
			int offset=(Integer.MAX_VALUE/2)%pos;
			mCalendar.add(Calendar.WEEK_OF_YEAR, -offset);
		}
		
		List<Date> mDates=new ArrayList<Date>();
		//将周一设为本周的第一天
	/*	mCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		 for (int i = Calendar.MONDAY; i <= (Calendar.MONDAY+6); i++) {	 
			 mCalendar.set(Calendar.DAY_OF_WEEK, i);	
			 mDates.add(mCalendar.getTime());
		 }*/
		
		mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
		 for (int i = Calendar.SUNDAY; i <= (Calendar.SATURDAY); i++) {	 
			 mCalendar.set(Calendar.DAY_OF_WEEK, i);	
			 mDates.add(mCalendar.getTime());
		 }
		 return mDates;
	}
}
