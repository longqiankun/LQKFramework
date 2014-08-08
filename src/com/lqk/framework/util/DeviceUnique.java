package com.lqk.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

/** 
 * @Title: DeviceUnique.java
 * @Description: 描述:获取android的唯一标识：需要以下权限：
 *  <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 *  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
 *  <uses-permission android:name="android.permission.BLUETOOTH"/>
 * @author longqiankun
 * @email qiankun.long@dilitech.com
 * @version 1.0  
 * @created 2013-12-10 下午6:04:10 
 */

public class DeviceUnique {
	/**
	 * 
	* @Title: getDeviceUnique
	* @Description: 获取设备的唯一标示
	* @param @param context
	* @param @return
	* @return String
	* @throws
	 */
	public static String getDeviceUnique(Context context){
		
		//1并且用户应当允许安装此应用。作为手机来讲，IMEI是唯一的，它应该类似于 359881030314356（除非你有一个没有量产的手机（水货）它可能有无
		TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		String m_szImei = TelephonyMgr.getDeviceId();
		
		//2通过取出ROM版本、制造商、CPU型号、以及其他硬件信息来实现这一点。这样计算出来的ID不是唯一的（因为如果两个手机应用了同样的硬件以及Rom 镜像）
		String m_szDevIDShort = "35" + //we make this look like a valid IMEI 
		Build.BOARD.length()%10+ Build.BRAND.length()%10 + Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 + Build.DISPLAY.length()%10 + Build.HOST.length()%10 + Build.ID.length()%10 + Build.MANUFACTURER.length()%10 + Build.MODEL.length()%10 + Build.PRODUCT.length()%10 + Build.TAGS.length()%10 + Build.TYPE.length()%10 + Build.USER.length()%10 ; //13 digits  
		
		//3通常被认为不可信，因为它有时为null。开发文档中说明了：这个ID会改变如果进行了出厂设置。并且，如果某个Andorid手机被Root过的话，这个ID也可以被任意改变。
		String m_szAndroidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		
		//4是另一个唯一ID。但是你需要为你的工程加入android.permission.ACCESS_WIFI_STATE 权限，否则这个地址会为null。
		WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE); 
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
		
		//5只在有蓝牙的设备上运行
		BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter  
		try{
		m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    
		}catch (Exception e) {
			// TODO: handle exception
		}
		String m_szBTMAC ="";
		if(m_BluetoothAdapter!=null){
			m_szBTMAC=m_BluetoothAdapter.getAddress();
		}
		
		String m_szLongID = m_szImei + m_szDevIDShort 
				+ m_szAndroidID+ m_szWLANMAC + m_szBTMAC;  
		
			// compute md5  
			 MessageDigest m = null;   
			try {
			 m = MessageDigest.getInstance("MD5");
			 } catch (NoSuchAlgorithmException e) {
			 e.printStackTrace();   
			}    
			m.update(m_szLongID.getBytes(),0,m_szLongID.length());   
			// get md5 bytes   
			byte p_md5Data[] = m.digest();   
			// create a hex string   
			String m_szUniqueID = new String();   
			for (int i=0;i<p_md5Data.length;i++) {   
			     int b =  (0xFF & p_md5Data[i]);    
			// if it is a single digit, make sure it have 0 in front (proper padding)    
			    if (b <= 0xF) 
			        m_szUniqueID+="0";    
			// add number to string    
			    m_szUniqueID+=Integer.toHexString(b); 
			   }   // hex string to uppercase   
			m_szUniqueID = m_szUniqueID.toUpperCase();
			
			return m_szUniqueID;
	}
}
