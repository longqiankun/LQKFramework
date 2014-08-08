package com.lqk.framework.util;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
/**
 * 
* @ClassName: ServiceUtils
* @Description: 服务工具类
* @author longqiankun
* @date 2014-7-7 下午12:06:45
*
 */
public class ServiceUtils {

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		//获取正在运行的服务
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			//检查接受的服务器名是否在正在运行的服务中
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

}
