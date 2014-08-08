package com.lqk.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**   
 * @Title: RegularUtils.java 
 * @Package com.dilitech.qiyebao.utils 
 * @Description:  正则工具类
 * @author longqiankun   
 * @date 2013-7-5 下午12:59:08 
 * @version V1.0  
 * @Email:qiankun.long@dilitech.com
 */
public class RegularUtils {
	/**
	 * @description 验证ip地址是否合法
	 * @param ip ip地址  格式为：111.111.111.111
	 * @return 返回只有true 或false ,如果为true,则传入的Ip地址是合法，否则不合法。
	 */
	public static boolean isIp(String ip){
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ip); //以验证127.400.600.2为例
		return matcher.matches();
	}
	/**
	 * @description 验证邮箱地址是否合法
	 * @param email 邮箱地址
	 * @return 返回值为true时合法，false则不合法。
	 */
	 public static boolean isEmail(String email){     
		  boolean flag = false;  
	      try{  
	       String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";  
	       Pattern regex = Pattern.compile(check);  
	       Matcher matcher = regex.matcher(email);  
	       flag = matcher.matches();  
	      }catch(Exception e){  
	       flag = false;  
	      }  
	        
	      return flag;   
	    } 
	 /**
	  * @description 验证手机号码是否合法
	  * @param mobiles 手机号码
	  * @return 返回值为true时合法，false则不合法。
	  */
	 public static boolean isMobileNO(String mobiles){     
//		 String pattern="^[1][3-8]+\\d{9}";
		 String pattern="((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
	        Pattern p = Pattern.compile(pattern);     
	        Matcher m = p.matcher(mobiles);     
	        return m.matches();     
	    }
	 /**
	  * @description 验证传入的字符是否是数字
	  * @param digital 需要验证的字符串
	  * @return 返回值为true时表示全是数字，false则不全是数字。
	  */
		public static boolean isDigital(String digital){	
			if(TextUtils.isEmpty(digital)){
				return false;
			}
			return digital.matches("^[+-]?\\d+(\\.\\d+)?$");
			}


}
