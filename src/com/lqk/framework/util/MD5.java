package com.lqk.framework.util;

import java.security.MessageDigest;
/**
 * 
* @ClassName: MD5
* @Description: md5加密
* @author longqiankun
* @date 2014-7-7 上午11:50:42
*
 */
public class MD5 {
/**
 * 
* @Title: encode
* @Description: 对字符串进行md5加密
* @param @param str
* @param @return
* @return String
* @throws
 */
	public static String encode(String str){
		 try {
				MessageDigest digest = MessageDigest.getInstance("md5");
				byte[] data = digest.digest(str.getBytes());
				StringBuilder sb = new StringBuilder();
				for(int i = 0;i< data.length;i++){
					String result = Integer.toHexString(data[i]&0xff);
					String temp = null;
					if(result.length() == 1){
						temp = "0" + result;
					}else{
						temp = result;
					}
					sb.append(temp);
				}
				return sb.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 return null;
	}
}
