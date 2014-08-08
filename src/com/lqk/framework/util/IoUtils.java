package com.lqk.framework.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**   
 * @Title: IoUtils.java 
 * @Package com.dilitech.qiyebao.utils 
 * @Description:  
 * @author longqiankun   
 * @date 2013-6-26 下午3:45:39 
 * @version V1.0  
 * @Email:qiankun.long@dilitech.com
 */
public class IoUtils {
	/**
	 * @description:将字符串转换成流
	 * @param str 字符串
	 * @return io流
	 */
	public static InputStream String2InputStream(String str){
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
		}
	/**
	 * @description:将IO流转换成字符串
	 * @param is  io流
	 * @return 字符串
	 */
		public static String inputStream2String(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null){
		buffer.append(line);
		}
		return buffer.toString();
		}
}
