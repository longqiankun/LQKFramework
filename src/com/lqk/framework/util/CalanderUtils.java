package com.lqk.framework.util;

import java.util.ArrayList;
import java.util.Calendar;
/**
 * 
* @ClassName: CalanderUtils
* @Description: 日历操作工具
* @author longqiankun
* @date 2014-7-7 上午11:09:15
*
 */
public class CalanderUtils {
	private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
	private Calendar calSelected = Calendar.getInstance(); // 选择的日历
	private Calendar calToday = Calendar.getInstance(); // 今日
	private int iMonthViewCurrentMonth = 0; // 当前视图月
	private int iMonthViewCurrentYear = 0; // 当前视图年
	private int iFirstDayOfWeek = Calendar.MONDAY;
	
	private static CalanderUtils instance;
	private CalanderUtils(){}
	/**
	 * 
	* @Title: getInstance
	* @Description: 日历工具的单例
	* @return CalanderUtils
	* @throws
	 */
	public static synchronized CalanderUtils getInstance(){
		if(instance==null){
			instance=new CalanderUtils();
		}
		return instance;
	}
	/**
	 * 
	* @Title: getDates
	* @Description: 获取当前周日期
	* @return ArrayList<java.util.Date>
	* @throws
	 */
	public ArrayList<java.util.Date> getDates() {
		UpdateStartDateForMonth();
		ArrayList<java.util.Date> alArrayList = new ArrayList<java.util.Date>();
		for (int i = 1; i <= 7; i++) {
			alArrayList.add(calStartDate.getTime());
			calStartDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		return alArrayList;
	}
	
	
	/**
	 * 
	* @Title: UpdateStartDateForMonth
	* @Description: 根据改变的日期更新日历,填充日历控件用
	* @param 
	* @return void
	* @throws
	 */
	private void UpdateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月
		iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);// 得到当前日历显示的年
		// 星期一是2 星期天是1 填充剩余天数
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
		calStartDate.add(Calendar.DAY_OF_MONTH, -1);// 周日第一位
	}
	
	/**
	 * 
	* @Title: setPrevViewItem
	* @Description: 上一个月
	* @param 
	* @return void
	* @throws
	 */
	private void setPrevViewItem() {
		iMonthViewCurrentMonth--;// 当前选择月--
		// 如果当前月为负数的话显示上一年
		if (iMonthViewCurrentMonth == -1) {
			iMonthViewCurrentMonth = 11;
			iMonthViewCurrentYear--;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1); // 设置日为当月1日
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth); // 设置月
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear); // 设置年
	}

	/**
	* @Title: setToDayViewItem
	* @Description: 当月
	* @param 
	* @return void
	* @throws
	 */
	private void setToDayViewItem() {

		calSelected.setTimeInMillis(calToday.getTimeInMillis());
		calSelected.setFirstDayOfWeek(iFirstDayOfWeek);
		calStartDate.setTimeInMillis(calToday.getTimeInMillis());
		calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
	}

	/**
	* @Title: setNextViewItem
	* @Description: 下一个月
	* @param 
	* @return void
	* @throws
	 */
	private void setNextViewItem() {
		iMonthViewCurrentMonth++;
		if (iMonthViewCurrentMonth == 12) {
			iMonthViewCurrentMonth = 0;
			iMonthViewCurrentYear++;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
	}
}
