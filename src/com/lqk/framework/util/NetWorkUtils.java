package com.lqk.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.TrafficStats;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/** 
 * @Company: Dilitech
 * @author longqiankun
 * @email qiankun.long@dilitech.com
 * @Title: NetWorkUtils.java
 * @Description: 网络工具类
 * @version 1.0  
 * @created 2013-12-21 上午10:03:59 
 */

public class NetWorkUtils {
	final static String TAG = "NetEnvorimentUtils";
	private   static   final  String REQUEST_MOTHOD = "POST" ;
	 private   static   final  String REQUEST_URL = "http://www.ip138.com/ips.asp" ;
     private   static  HttpURLConnection httpConn = null ;
	private TelephonyManager telephonyManager;
	private String IMSI;
	private Context mContext;
	public static final int NETWORK_TYPE_NONE = -0x1; 
	public static final int NETWORK_TYPE_WIFI = 0x1; 
	public static final int NETWOKR_TYPE_MOBILE = 0x2; 
	
	public NetWorkUtils(Context mContext) {
		super();
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	/**
	 * 检测手机是否开启GPRS网络,需要调用ConnectivityManager,TelephonyManager 服务.
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean checkGprsNetwork(Context context) {
		boolean has = false;
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming()) {
			has = info.isConnected();
		}
		return has;

	}

	/**
	 * 检测手机是否开启WIFI网络,需要调用ConnectivityManager服务.
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean checkWifiNetwork(Context context) {
		boolean has = false;
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_WIFI) {
			has = info.isConnected();
		}
		return has;
	}

	/**
	 * 检测当前手机是否联网
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 手机是否处在漫游
	 * 
	 * @param mCm
	 * @return boolean
	 */
	public boolean isNetworkRoaming(Context mCm) {
		ConnectivityManager connectivity = (ConnectivityManager) mCm.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		boolean isMobile = (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE);
		TelephonyManager mTm = (TelephonyManager) mCm.getSystemService(Context.TELEPHONY_SERVICE);
		boolean isRoaming = isMobile && mTm.isNetworkRoaming();
		return isRoaming;
	}

	/**
	 * 
	* @Title: isNetWork
	* @Description: 检查是否链接网络
	* @param @param context
	* @param @return
	* @return boolean
	* @throws
	 */
	public static boolean isNetWork(Context context) {
		WifiManager mWifiManager = (WifiManager) context

		.getSystemService(Context.WIFI_SERVICE);
		WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
		SupplicantState  state=mWifiInfo.getSupplicantState();
		if (mWifiInfo == null){
			Logger.getLogger("NetWorkUtils").i( " wifi info is " + "null");
			return isOtherNet(context);
		}
		Logger.getLogger("NetWorkUtils").i( " wifi info is " + mWifiInfo.toString());
		return true;
	}
/**
 * 
* @Title: isOtherNet
* @Description: 检查是否连接其他网络
* @param @param context
* @param @return
* @return boolean
* @throws
 */
	public static boolean isOtherNet(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		String message = "null";
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info != null)
			message = info.toString();
		Log.d("wifi", "infonet is :" + message);
		return true;
	}
/**
 * 
* @Title: getCurrentNetType
* @Description: 获取当前网络类型
* @param @param mContext
* @param @return
* @return int
* @throws
 */
	public int getCurrentNetType(Context mContext) {
		ConnectivityManager connManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // wifi
		NetworkInfo gprs = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // gprs
		if (wifi != null && wifi.getState() == State.CONNECTED) {
			Log.d(TAG, "Current net type:  WIFI.");
			 return NETWORK_TYPE_WIFI;
//			return "WIFI模式";
		} else if (gprs != null && gprs.getState() == State.CONNECTED) {
			Log.d(TAG, "Current net type:  MOBILE.");
			 return NETWOKR_TYPE_MOBILE;
//			return "GPRS模式";
		}
		Log.e(TAG, "Current net type:  NONE.");
		 return NETWORK_TYPE_NONE;
//		return "断开网络";
	}
	/**
	 * 
	* @Title: getCurrentNetInfo
	* @Description: 获取当前网络信息
	* @param @param mContext
	* @param @return
	* @return String[]
	* @throws
	 */
	public static String[] getCurrentNetInfo(Context mContext) {
		String[] infos=new String[3];
		ConnectivityManager connManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		if(networkInfo!=null){
			infos[0]=networkInfo.getTypeName();
			infos[1]="可连接";
			long c=TrafficStats.getTotalRxBytes();
			long d=TrafficStats.getTotalRxPackets();
			int myUid = android.os.Process.myUid();
			long receiver=TrafficStats.getUidRxBytes(myUid);
			infos[2]=(receiver/1024)+"ms";
		/*	
			long preRece=ShareData.getLong(mContext, "bytes");
			if(receiver!=preRece){
				long offset=preRece-receiver;
				infos[2]=(offset/1024)+"ms";
			}else{
				long offset=receiver-preRece;
				infos[2]=(offset/1024)+"ms";
			}
			ShareData.setLong(mContext, "bytes", receiver);*/
			
			
		}else{
			infos[0]="无网络";
			infos[1]="无连接";
			infos[2]="0ms";
		}
		return infos;
}
/**
 * 
* @Title: getNativePhoneNumber
* @Description: 获取本地电话号码
* @param @return
* @return String
* @throws
 */
	public String getNativePhoneNumber() {
		String NativePhoneNumber = null;
		NativePhoneNumber = telephonyManager.getLine1Number();
		return NativePhoneNumber;
	}
/**
 * 
* @Title: getProvidersName
* @Description: 获取运营商
* @param @return
* @return String
* @throws
 */
	public String getProvidersName() {

		String ProvidersName = null;

		// 返回唯一的用户ID;就是这张卡的编号神马的

		IMSI = telephonyManager.getSubscriberId();

		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {

			ProvidersName = "中国移动";

		} else if (IMSI.startsWith("46001")) {

			ProvidersName = "中国联通";

		} else if (IMSI.startsWith("46003")) {

			ProvidersName = "中国电信";

		}

		return ProvidersName;

	}
/**
 * 
* @Title: getWifiIpAddress
* @Description: 获取wifi的ip地址
* @param @return
* @return String
* @throws
 */
	public String getWifiIpAddress() {
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);

		// 判断wifi是否开启

		if (!wifiManager.isWifiEnabled()) {

			wifiManager.setWifiEnabled(true);
		}
		String ip="";
		WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());  
		if (null != info) {  
		    ip= info.getMacAddress();  
		  
		} else{
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		ip = intToIp(ipAddress);
		}
		return ip;
	}
/**
 * 
* @Title: intToIp
* @Description: 转换Ip地址
* @param @param i
* @param @return
* @return String
* @throws
 */
	private String intToIp(int i) {

		return (i & 0xFF) + "." +

		((i >> 8) & 0xFF) + "." +

		((i >> 16) & 0xFF) + "." +

		(i >> 24 & 0xFF);

	}
/**
 * 
* @Title: getLocalIpAddress
* @Description: 获取本地Ip地址
* @param @return
* @return String
* @throws
 */
	public String getLocalIpAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();)

			{

				NetworkInterface intf = en.nextElement();

				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress().toString();
					}
				}
			}

		}

		catch (SocketException ex)

		{

			Log.e("WifiPreference IpAddress", ex.toString());

		}

		return null;

	}
/**
 * 
* @Title: getIPArea
* @Description: 获取ip地址
* @param @param ip
* @param @return
* @return String
* @throws
 */
    public   static  String getIPArea(String ip)
    {
       String requestParameter = "ip=" + ip + "&action=2 " ;
       String IPArea = "" ;
       BufferedReader br = null ;
        try 
         {
           httpConn = (HttpURLConnection) new  URL(REQUEST_URL).openConnection();
           httpConn.setRequestMethod(REQUEST_MOTHOD);
           httpConn.setDoOutput( true );
           httpConn.getOutputStream().write(requestParameter.getBytes());
           httpConn.getOutputStream().flush();
           httpConn.getOutputStream().close();
           
           br = new  BufferedReader( new  InputStreamReader(httpConn.getInputStream(), "utf-8" ));
           String lineStr = null ;
            while ((lineStr = br.readLine()) != null )
            {
                if (lineStr.contains( "<td align=\"center\"><ul class=\"ul1\"><li>"))
                {
                   IPArea = lineStr.substring(lineStr.indexOf( ":" ) + 1 ,lineStr.indexOf("</"));
                    break ;
               } 
           } 
       } 
         catch (IOException e)  {
           e.printStackTrace();
       } 
        finally 
         {
            if (br != null )
                try   {
                   br.close();
               }   catch  (IOException e)  {
                   e.printStackTrace();
               } 
       } 
        return  IPArea;
   } 
/**
 * 
* @Title: getipaddress
* @Description: 通过百度搜索当前的ip地址
* @param @return
* @param @throws Exception
* @return String[]
* @throws
 */
	public static String[] getipaddress() throws Exception {
		// TODO Auto-generated method stub
		String[] ipArr=new String[2];
		URL url=new URL("http://www.baidu.com/s?wd=ip&rsv_bp=0&rsv_spt=3&rsv_sug3=2&rsv_sug=0&rsv_sug1=2&rsv_sug4=125&inputT=2192");
		HttpURLConnection conn=(HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
            InputStream is = conn.getInputStream();	
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();
            String line=null;
            while((line=br.readLine())!=null){
//            	sb.append(line);
            	if(line.contains("本机IP:")){
            	String s1=line.substring(line.indexOf("本机IP:"),line.indexOf("</td></tr></table>"));
            	String s2=s1.substring(s1.indexOf("\">"));
            	String ip=s2.substring(s2.indexOf("\">")+2,s2.indexOf("</span>"));
            	String ipaddress=s2.substring(s2.indexOf("</span>")+7);
            	System.out.println("ip地址"+ip);
            	System.out.println("ip地点:"+ipaddress);
            	ipArr[0]=ip;
            	ipArr[1]=ipaddress;
            	return ipArr;
            	}
            }
            is.close();
		}
		return null;
	}
}
