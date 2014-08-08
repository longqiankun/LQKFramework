package com.lqk.framework.internet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.lqk.framework.util.Logger;
import com.lqk.framework.util.PreferencesUtils;
/**
 * 
* @ClassName: ConnectionClient
* @Description: 链接服务器的客户端操作
* @author longqiankun
* @date 2014-7-7 上午11:13:30
*
 */
public class ConnectionClient {

	/**
	 * 执行POST方法 不含有 文件传送
	 * 
	 * @param severMethod
	 *            服务器的方法接口
	 * @param p
	 *            传递给服务器的参数
	 * @param json
	 *            POST给服务器的参数
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	public static String Tag = "net";
//	 public static int CONNECTION_TIMEOUT = 2*60*1000;  
//	    public static int SOCKET_TIMEOUT  = 2*60*1000;  
	    public static int CONNECTION_TIMEOUT = 2147483647;  
	    public static int SOCKET_TIMEOUT  = 2147483647;  
	    /**
	     * 
	    * @Title: doGet
	    * @Description: get请求
	    * @param json json对象封装的参数
	    * @param method get请求的方法
	    * @param @throws IOException
	    * @param @throws JSONException
	    * @return String 服务器返回的json串
	    * @throws
	     */
	public static String doGet(String url,JSONObject json,String methodName,String methodValue) throws IOException, JSONException {
		int f=Integer.MAX_VALUE;
		Logger.getLogger(Tag).i( "url is "+url);
		StringBuilder sb=new StringBuilder();
		sb.append("\r\n");
		sb.append(url);
		sb.append("\r\n");
		if (json != null) {
			url+="?";
			url=url+methodName+"="+methodValue+"&";
			@SuppressWarnings("unchecked")
			Iterator<String> iter = json.keys();
			int i=0;
			while (iter.hasNext()) {
				String key = iter.next();
				if(i!=0)url+="&";
				else i++;
				url+=(key+"="+json.getString(key));
				sb.append(json.getString(key));
				sb.append("\r\n");
				Logger.getLogger(Tag).i( "key="+key+" value="+json.getString(key));
			}
		}
		url = url.replaceAll(" ", "%20");
//		url = url.replaceAll("&", "%26");
		Logger.getLogger(Tag).i( "url is "+url);
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		 HttpConnectionParams.setConnectionTimeout(httpGet.getParams(), CONNECTION_TIMEOUT);
		 HttpConnectionParams.setSoTimeout(httpGet.getParams(), SOCKET_TIMEOUT); 
		 Logger.getLogger(Tag).i( "status result is "+ httpResponse.getStatusLine().getStatusCode());
		 if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity he = httpResponse.getEntity();
			String response = EntityUtils.toString(he);
			Logger.getLogger(Tag).i( "result is "+ response);
			sb.append(response);
			sb.append("\r\n\r\n\r\n\r\n\r\n");
			return response;
		}else{
			return "";
		}
	}
	/**
	 * 
	* @Title: doPost
	* @Description: post请求
	* @param task 异步操作类
	* @param json json对象封装的参数
	* @param method 请求方法
	* @param @throws IOException
	* @param @throws JSONException
	* @return String 服务器返回的json串
	* @throws
	 */
	public static String doPost(String url,JSONObject json,String method) throws IOException, JSONException {
		Logger.getLogger(Tag).i( "url is "+url);
		HttpPost httpPost = new HttpPost(url);
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		if (json != null) {
			@SuppressWarnings("unchecked")
			Iterator<String> iter = json.keys();
			params.add(new BasicNameValuePair("RequestName", method));
			Logger.getLogger(Tag).i( "key is "+" type "+" value is "+method);
			while (iter.hasNext()) {
				String key = iter.next();
				params.add(new BasicNameValuePair(key, json.getString(key)));
				Logger.getLogger(Tag).i( "key is "+key+"   value is "+json.getString(key));
			}
		}
		HttpEntity httpEntity = new UrlEncodedFormEntity(params,"UTF-8");
		httpPost.setEntity(httpEntity);
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpResponse httpResponse = httpClient.execute(httpPost);
//		 HttpConnectionParams.setConnectionTimeout(httpPost.getParams(), CONNECTION_TIMEOUT);
//		 HttpConnectionParams.setSoTimeout(httpPost.getParams(), SOCKET_TIMEOUT); 
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity he = httpResponse.getEntity();
            long length = he.getContentLength();  
            InputStream is = he.getContent();  
            String s = "";  
            if(is != null) {  
                ByteArrayOutputStream baos = new ByteArrayOutputStream();  
                byte[] buf = new byte[1024];  
                int ch = -1;  
                int count = 0;  
                while((ch = is.read(buf)) != -1) {  
                   baos.write(buf, 0, ch);  
                   baos.flush();
                   count += ch;  
                   if(length > 0) {  
                       // 如果知道响应的长度，调用publicPro（）更新进度  
                
                   }
                }  
                s = new String(baos.toByteArray());     
            }
			return s;
		}else{
			 return "";
		}
		
	}
	/**
	 * 
	* @Title: doPost
	* @Description: post请求
	* @param json json对象的参数封装类
	* @param timeout 超时时间
	* @param @throws IOException
	* @param @throws JSONException
	* @return String 服务器返回的json串
	* @throws
	 */
	public static String doPost(String url,JSONObject json,int timeout) throws IOException, JSONException {
		HttpPost httpPost = new HttpPost(url);
		
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		if (json != null) {
			@SuppressWarnings("unchecked")
			Iterator<String> iter = json.keys();
			while (iter.hasNext()) {
				String key = iter.next();
				params.add(new BasicNameValuePair(key, json.getString(key)));
			}
		}
		
		
		HttpEntity httpEntity = new UrlEncodedFormEntity(params,
				"UTF-8");
		httpPost.setEntity(httpEntity);

		HttpParams hp = new BasicHttpParams();
		
		HttpConnectionParams.setConnectionTimeout(hp,timeout);
		
		HttpClient httpClient = new DefaultHttpClient(hp);
		
		
		HttpResponse httpResponse = httpClient.execute(httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity he = httpResponse.getEntity();
			String response = EntityUtils.toString(he);
			Logger.getLogger(Tag).i( "result is "+ response);
			return response;
		}else return "";

	}
	/**
	 * 
	* @Title: getConnection
	* @Description: 获取服务器请求连接
	* @param url 请求地址
	* @param @throws MalformedURLException
	* @param @throws IOException
	* @return HttpURLConnection
	* @throws
	 */
	static HttpURLConnection getConnection(String url)
			throws MalformedURLException, IOException {
		Logger.getLogger(Tag).i( "request url is :" + url);
		String proxyHost = android.net.Proxy.getDefaultHost();
		if (proxyHost != null) {
			java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP,
					new InetSocketAddress(android.net.Proxy.getDefaultHost(),
							android.net.Proxy.getDefaultPort()));

			return (HttpURLConnection) new URL(url).openConnection(p);

		} else {
			return (HttpURLConnection) new URL(url).openConnection();
		}
	}
	/**
	 * 
	* @Title: doPostMethod
	* @Description: 上传文件
	* @param @param fileUrl 请求地址
	* @param @param json 参数信息
	* @param @param filePath 文件路径
	* @param @throws Exception
	* @return String
	* @throws
	 */
	public static String doPostMethod(String fileUrl,JSONObject json,String filePath)
			throws Exception {
		File f = new File(filePath);
		HttpURLConnection request = getConnection(fileUrl);
		request.setDoOutput(true);
		request.setRequestMethod("POST");
		String boundary = "---------------------------37531613912423";
		String name = "pic";
		request.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);

		String pic = "\r\nContent-Disposition: form-data; name=\""
				+ name
				+ "\"; filename=\"postpic.jpg\"\r\nContent-Type: image/jpeg\r\n\r\n";
		byte[] end_data = ("\r\n--" + boundary + "--\r\n").getBytes();
		FileInputStream stream = new FileInputStream(f);
		byte[] file = new byte[(int) f.length()];
		stream.read(file);

		OutputStream ot = request.getOutputStream();
		ot.write(("\r\n--" + boundary).getBytes());
		if (json != null) {
			@SuppressWarnings("rawtypes")
			Iterator iter = json.keys();
			@SuppressWarnings("unused")
			int i = 0;
			while (iter.hasNext()) {
				String key = (String) iter.next();
				ot.write(contentType(key).getBytes());
				// ot.write(json.getString(key).getBytes());
				ot.write(json.getString(key).getBytes());
				ot.write(("\r\n--" + boundary).getBytes());
			}
		}
		ot.write(pic.getBytes());
		ot.write(file);
		ot.write(end_data);
		ot.flush();
		ot.close();
		// stream.close();
		Logger.getLogger(Tag).i( "sending request....");
		request.setConnectTimeout(10000);
		request.setReadTimeout(10000);
		request.connect();
		Logger.getLogger(Tag).i( request.getResponseCode() + " "
				+ request.getResponseMessage());
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				request.getInputStream(), "utf-8"));
		String b = null;
		String s = "";
		while ((b = reader.readLine()) != null) {
			Logger.getLogger(Tag).i( b);
			s += b;
		}
		if ("".equals(s)) {
		} else
			b = s;
		return b;
	}
	private static String contentType(String key) {
		return "\r\nContent-Disposition: form-data; name=\"" + key
				+ "\"\r\n\r\n";
	}
	/**
	* HttpConnection提交数据
	* @param str URL
	* @param params 提交参数
	* @param fileName 提交文件名
	* @return
	*/
	public static String httpConnPost(String str,Context context,Map<String, String> params,String method,String fileName){
		Logger.getLogger(Tag).i(  "url="+str);
		Logger.getLogger(Tag).i( "method="+method);
		String s = "";
		if(params!=null){
			params.put("type", method);
		}
	URL url ;
	HttpURLConnection conn = null ;
	InputStream is = null;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	StringBuilder sb = null;
	String BOUNDARY = java.util.UUID.randomUUID().toString();
	String PREFIX = "--", LINEND = "\r\n";
	String MULTIPART_FROM_DATA = "multipart/form-data";
	String CHARSET = "UTF-8";
	try {
		 String cookie=PreferencesUtils.getString(context, "Cookie");
	System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
//	System.getProperties().setProperty("http.proxyHost",proxy); 
//	System.getProperties().setProperty("http.proxyPort",port); 
	url = new URL(str);
	conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("POST");
	conn.setUseCaches(false); 
	conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
	conn.setRequestProperty("Cookie", cookie);
	conn.setRequestProperty("Charsert", CHARSET);
	conn.setRequestProperty("Content-Type",MULTIPART_FROM_DATA+ ";boundary=" + BOUNDARY);
	conn.setDoOutput(true);
	conn.setDoInput(true);
	conn.setUseCaches(false);
	conn.setConnectTimeout(CONNECTION_TIMEOUT);
	conn.setReadTimeout(SOCKET_TIMEOUT);
	dos = new DataOutputStream(conn.getOutputStream());
	sb = new StringBuilder();
	for (Map.Entry<String, String> entry : params.entrySet()){
	sb.append(PREFIX);
	sb.append(BOUNDARY);
	sb.append(LINEND);
	sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
	sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
	sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
	sb.append(LINEND);
	sb.append(entry.getValue());
	sb.append(LINEND);
	Logger.getLogger(Tag).i( "key="+entry.getKey()+"----- value="+entry.getValue());
	}
//	if(log.isInfoEnabled()) 
//	log.info(sb);
	dos.write(sb.toString().getBytes());
	sb.delete(0, sb.length());
	sb.reverse();
	if(fileName!=null){
	sb.append(PREFIX);
	sb.append(BOUNDARY);
	sb.append(LINEND);
	sb.append("Content-Disposition: form-data; name=\"pic\"; filename=\"" + fileName + "\"" + LINEND);
	sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
	sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
	sb.append(LINEND);
//	if(log.isInfoEnabled()) 
//	log.info(sb);
	dos.write(sb.toString().getBytes()); 
	InputStream img = new FileInputStream(fileName);
	byte[] buffer = new byte[1024];
	int len = 0;
	while ((len = img.read(buffer)) != -1){
	dos.write(buffer, 0, len);
	}
	img.close();
	dos.write(LINEND.getBytes());
	}
	// 请求结束标志
	String end_data = PREFIX + BOUNDARY + PREFIX + LINEND;
//	if(log.isInfoEnabled()) 
//	log.info(end_data); 
	dos.write(end_data.getBytes());
	dos.flush();
	//dos.close();
	int code = conn.getResponseCode();
	if(code==200){
	is = conn.getInputStream();
	}else{
//	log.error(conn.getResponseCode()+conn.getResponseMessage());
	is = conn.getErrorStream();
	}
	/*dis = new DataInputStream(is);
	byte b[] = new byte[dis.available()];
	dis.read(b);
	result = new String(b);*/
	long length=conn.getContentLength();
	if("EmployeeLogin".equals(method)){
		Map fnResponses = conn.getHeaderFields();
	    Set responseKeys = fnResponses.keySet();
	    Iterator iter = responseKeys.iterator();
	    while(iter.hasNext()) {
	            String key = (String) iter.next();
	            if("Set-Cookie".equals(key)){
	                String value = conn.getHeaderField(key);
	                PreferencesUtils.setString(context, "Cookie", value);
	                break;
	            }
	        
	    } 
	}

     if(is != null) {  
         ByteArrayOutputStream baos = new ByteArrayOutputStream();  
         byte[] buf = new byte[1024];  
         int ch = -1;  
         int count = 0;  
         while((ch = is.read(buf)) != -1) {  
            baos.write(buf, 0, ch);  
            baos.flush();
            count += ch;  
            if(length > 0) {  
       
            }
         }  
         s = new String(baos.toByteArray());     
     }
	} catch (IOException e) {
	e.printStackTrace();
	}finally{
	try {
		if(is!=null)
	is.close();
//	dis.close();
		if(conn!=null)
	conn.disconnect();
	} catch (IOException e) {
	e.printStackTrace();
	}

	}
	return s ;
	}



}
