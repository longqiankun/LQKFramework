package com.lqk.framework.util;   
    
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
/**
 * 
 * @Company: Dilitech
 * @author longqiankun
 * @email qiankun.long@dilitech.com
 * @Title: CrashHandler.java
 * @Description: 捕获程序中出现的异常
 * @version 1.0  
 * @created 2014-4-1 上午10:53:49
 */
public class CrashHandler implements UncaughtExceptionHandler {   
    private static final String TAG = "CrashHandler";   
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler INSTANCE = new CrashHandler(); 
    private Context mContext;
    private Map<String, String> info = new HashMap<String, String>(); 
    private SimpleDateFormat format = new SimpleDateFormat(   
            "yyyy-MM-dd-HH-mm-ss");
    
    private CrashHandler() {   
    
    }   
    /**
     * 
    * @Title: getInstance
    * @Description: 异常捕获实例
    * @param @return
    * @return CrashHandler
    * @throws
     */
    public static CrashHandler getInstance() {   
        return INSTANCE;   
    }   
    /**
     * 
    * @Title: init
    * @Description: 初始化操作
    * @param @param context 上下文
    * @return void
    * @throws
     */
    public void init(Context context) {   
        mContext = context;   
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        Thread.setDefaultUncaughtExceptionHandler(this);
    }   
    /**
     * 异常
     */
    public void uncaughtException(Thread thread, Throwable ex) {   
        if (!handleException(ex) && mDefaultHandler != null) {   
            mDefaultHandler.uncaughtException(thread, ex);   
        } else {   
            try {   
                Thread.sleep(3000);
            } catch (InterruptedException e) {   
                e.printStackTrace();   
            }   
          
            android.os.Process.killProcess(android.os.Process.myPid());   
            System.exit(1);   
        }   
    }   
    /**
     * 
    * @Title: handleException
    * @Description:异常处理
    * @param @param ex
    * @param @return
    * @return boolean
    * @throws
     */
    public boolean handleException(Throwable ex) {   
        if (ex == null)   
            return false;   
        new Thread() {   
            public void run() {   
                Looper.prepare();   
//                Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出", 0).show();   
                Looper.loop();   
            }   
        }.start();   
        collectDeviceInfo(mContext);  
        saveCrashInfo2File(ex);   
        return true;   
    }   
/**
 * 
* @Title: collectDeviceInfo
* @Description:获取设备信息
* @param @param context
* @return void
* @throws
 */
    public void collectDeviceInfo(Context context) {   
        try {   
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),   
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {   
                String versionName = pi.versionName == null ? "null"  
                        : pi.versionName;   
                String versionCode = pi.versionCode + "";   
                info.put("versionName", versionName);   
                info.put("versionCode", versionCode);   
            }   
        } catch (NameNotFoundException e) {   
            e.printStackTrace();   
        }   
    
        Field[] fields = Build.class.getDeclaredFields();// �������    
        for (Field field : fields) {   
            try {   
                field.setAccessible(true);   
                info.put(field.getName(), field.get("").toString());   
                Log.d(TAG, field.getName() + ":" + field.get(""));   
            } catch (IllegalArgumentException e) {   
                e.printStackTrace();   
            } catch (IllegalAccessException e) {   
                e.printStackTrace();   
            }   
        }   
    }   
    /**
     * 
    * @Title: saveCrashInfo2File
    * @Description: 保存异常信息到文件
    * @param @param ex 异常
    * @return String 异常信息
    * @throws
     */
    private String saveCrashInfo2File(Throwable ex) {   
        StringBuffer sb = new StringBuffer();   
        for (Map.Entry<String, String> entry : info.entrySet()) {   
            String key = entry.getKey();   
            String value = entry.getValue();   
            sb.append(key + "=" + value + "\r\n");   
        }   
        Writer writer = new StringWriter();   
        PrintWriter pw = new PrintWriter(writer);   
        ex.printStackTrace(pw);   
        Throwable cause = ex.getCause();   
        while (cause != null) {   
            cause.printStackTrace(pw);   
            cause = cause.getCause();   
        }   
        pw.close();
        String result = writer.toString();   
        sb.append(result);   
        long timetamp = System.currentTimeMillis();   
        String time = format.format(new Date());   
        String fileName = "crash-" + time + "-" + timetamp + ".txt";   
        if (Environment.getExternalStorageState().equals(   
                Environment.MEDIA_MOUNTED)) {   
            try {   
               /* File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +                           File.separator + "crash");   
                Log.i("CrashHandler", dir.toString());   */
            	String sdir=SdCardUtils.getFilePath(mContext);
            	File dir = new File(sdir);
                if (!dir.exists())   
                    dir.mkdir();   
                FileOutputStream fos = new FileOutputStream(new File(dir,   
                         fileName));   
                fos.write(sb.toString().getBytes());   
                fos.close();   
                return fileName;   
            } catch (FileNotFoundException e) {   
                e.printStackTrace();   
            } catch (IOException e) {   
                e.printStackTrace();   
            } catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
        }   
        return null;   
    }   
}  