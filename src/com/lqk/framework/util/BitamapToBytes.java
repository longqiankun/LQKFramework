package com.lqk.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;

/**
 *@Description:
 *@Company:Diletch BJ
 *@author longqiankun
 * @EditAt :2013-8-20 下午3:10:55
 */
public class BitamapToBytes {
/**
 * 
* @Title: Bitmap2Bytes
* @Description: 将bitmap转换成字节数组
* @param  bm bitmap
* @return byte[] 转换后的字节数组
* @throws
 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos =new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		 
		return baos.toByteArray();
	}
	/**
	 * 
	* @Title: BytesToInStream
	* @Description:将字节数组转换的输入流
	* @param bytes 转换的字节数组
	* @return InputStream 输入流
	* @throws
	 */
	public static InputStream  BytesToInStream(byte[] bytes)
	{
		InputStream is = new ByteArrayInputStream(bytes); 
		return is;
	}
}
