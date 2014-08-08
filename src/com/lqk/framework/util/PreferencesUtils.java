package com.lqk.framework.util;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * PreferencesUtils, easy to get or put data
 * <ul>
 * <strong>Preference Name</strong>
 * <li>you can change preference name by {@link #PREFERENCE_NAME}</li>
 * </ul>
 * <ul>
 * <strong>Put Value</strong>
 * <li>put string {@link #putString(Context, String, String)}</li>
 * <li>put int {@link #putInt(Context, String, int)}</li>
 * <li>put long {@link #putLong(Context, String, long)}</li>
 * <li>put float {@link #putFloat(Context, String, float)}</li>
 * <li>put boolean {@link #putBoolean(Context, String, boolean)}</li>
 * </ul>
 * <ul>
 * <strong>Get Value</strong>
 * <li>get string {@link #getString(Context, String)}, {@link #getString(Context, String, String)}</li>
 * <li>get int {@link #getInt(Context, String)}, {@link #getInt(Context, String, int)}</li>
 * <li>get long {@link #getLong(Context, String)}, {@link #getLong(Context, String, long)}</li>
 * <li>get float {@link #getFloat(Context, String)}, {@link #getFloat(Context, String, float)}</li>
 * <li>get boolean {@link #getBoolean(Context, String)}, {@link #getBoolean(Context, String, boolean)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-3-6
 */
public class PreferencesUtils {
    public static final String APP_DATA = "preference";
	/**
	 * 
	* @Title: setBool
	* @Description: 保存bool类型数据
	* @param @param context
	* @param @param key
	* @param @param b
	* @return void
	* @throws
	 */
	public static void setBool(Context context,String key,boolean b){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		Editor ed = sp.edit();
		ed.putBoolean(key, b);
		ed.commit();
	}
	/**
	 * 
	* @Title: setfunCount
	* @Description: 设置功能操作次数
	* @param @param context
	* @param @param key
	* @param @param defaultTime
	* @return void
	* @throws
	 */
	public static void setfunCount(Context context,String key,int defaultTime){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		int f=sp.getInt(key, defaultTime);
		Editor edit = sp.edit();
		edit.putInt(key, f+1);
		edit.commit();
	}
	/**
	 * 
	* @Title: getfunCount
	* @Description: 获取功能操作次数
	* @param @param context
	* @param @param key
	* @param @param defaultTime
	* @param @return
	* @return int
	* @throws
	 */
	public static int getfunCount(Context context,String key,int defaultTime){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		return sp.getInt(key, defaultTime);
	}
	/**
	 * 
	* @Title: getBool
	* @Description: 获取bool类型数据
	* @param @param context
	* @param @param key
	* @param @return
	* @return boolean
	* @throws
	 */
	public static boolean getBool(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		return sp.getBoolean(key, false);
	}
	/**
	 * 
	* @Title: setString
	* @Description: 保存字符串数据
	* @param @param context
	* @param @param key
	* @param @param b
	* @return void
	* @throws
	 */
	public static void setString(Context context,String key,String b){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		Editor ed = sp.edit();
		ed.putString(key, b);
		ed.commit();
	}
	/**
	 * 
	* @Title: getString
	* @Description: 获取字符串数据
	* @param @param context
	* @param @param key
	* @param @return
	* @return String
	* @throws
	 */
	public static String getString(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		return sp.getString(key, "");
	}
	/**
	 * 
	* @Title: setInt
	* @Description: 保存整形数据
	* @param @param context
	* @param @param key
	* @param @param b
	* @return void
	* @throws
	 */
	public static void setInt(Context context,String key,int b){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		Editor ed = sp.edit();
		ed.putInt(key, b);
		ed.commit();
	}
	/**
	 * 
	* @Title: getInt
	* @Description: 获取整形数据
	* @param @param context
	* @param @param key
	* @param @return
	* @return int
	* @throws
	 */
	public static int getInt(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		return sp.getInt(key, 0);
	}
/**
 * 
* @Title: setLong
* @Description: 保存Long类型数据
* @param @param context
* @param @param key
* @param @param b
* @return void
* @throws
 */
	public static void setLong(Context context,String key,long b){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		Editor ed = sp.edit();
		ed.putLong(key, b);
		ed.commit();
	}
	/**
	 * 
	* @Title: getLong
	* @Description: 获取Long类型数据
	* @param @param context
	* @param @param key
	* @param @return
	* @return long
	* @throws
	 */
	public static long getLong(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		return sp.getLong(key, 0);
	}
	/**
	 * 
	* @Title: setFloat
	* @Description: 保存float类型数据
	* @param @param context
	* @param @param key
	* @param @param b
	* @return void
	* @throws
	 */
	public static void setFloat(Context context,String key,float b){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		Editor ed = sp.edit();
		ed.putFloat(key, b);
		ed.commit();
	}
	/**
	 * 
	* @Title: getFloat
	* @Description: 获取float类型数据
	* @param @param context
	* @param @param key
	* @param @return
	* @return float
	* @throws
	 */
	public static float getFloat(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		return sp.getFloat(key, 0);
	}
	/**
	 * 
	* @Title: removeKeys
	* @Description:删除指定的数据
	* @param @param context
	* @param @param keys
	* @return void
	* @throws
	 */
	public static void removeKeys(Context context,List<String> keys){
		if(keys!=null&&keys.size()>0){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		Editor ed = sp.edit();
		for (int i = 0; i < keys.size(); i++) {
			ed.remove(keys.get(i));
		}
		ed.commit();
		}
	}
	/**
	 * 
	* @Title: removeKeys
	* @Description:删除指定的数据
	* @param @param context
	* @param @param keys
	* @return void
	* @throws
	 */
	public static void removeKey(Context context,String key){
		if(key!=null){
		SharedPreferences sp = context.getSharedPreferences(APP_DATA, 0);
		Editor ed = sp.edit();
			ed.remove(key);
		ed.commit();
		}
	}
}
