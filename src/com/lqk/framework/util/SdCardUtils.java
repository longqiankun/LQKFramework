package com.lqk.framework.util;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StatFs;
/**
 * 
* @ClassName: SdCardUtils
* @Description: sd卡工具类
* @author longqiankun
* @date 2014-7-7 下午12:01:45
*
 */
public class SdCardUtils {
	/**
	 * 
	* @Title: ExistSDCard
	* @Description: 检查sd卡是否存在
	* @param @return
	* @return boolean
	* @throws
	 */
	public static  boolean ExistSDCard() {  
		  if (android.os.Environment.getExternalStorageState().equals(  
		    android.os.Environment.MEDIA_MOUNTED)) {  
		   return true;  
		  } else  
		   return false;  
		 }  
/**
 * 
* @Title: getSDAllSize
* @Description: 获取sd卡所有大小
* @param @return
* @return long
* @throws
 */
	public static long getSDAllSize(){  
	     //取得SD卡文件路径   
	     File path = Environment.getExternalStorageDirectory();   
	     StatFs sf = new StatFs(path.getPath());   
	     //获取单个数据块的大小(Byte)   
	     long blockSize = sf.getBlockSize();   
	     //获取所有数据块数   
	     long allBlocks = sf.getBlockCount();  
	     //返回SD卡大小   
	     //return allBlocks * blockSize; //单位Byte   
	     //return (allBlocks * blockSize)/1024; //单位KB   
	     return (allBlocks * blockSize)/1024/1024; //单位MB   
	   } 
	/**
	 * 
	* @Title: getSDFreeSize
	* @Description: 获取sd卡的剩余空间
	* @param @return
	* @return long
	* @throws
	 */
	public static long getSDFreeSize(){  
	     //取得SD卡文件路径   
	     File path = Environment.getExternalStorageDirectory();   
	     StatFs sf = new StatFs(path.getPath());   
	     //获取单个数据块的大小(Byte)   
	     long blockSize = sf.getBlockSize();   
	     //空闲的数据块的数量   
	     long freeBlocks = sf.getAvailableBlocks();  
	     //返回SD卡空闲大小   
	     //return freeBlocks * blockSize;  //单位Byte   
	     //return (freeBlocks * blockSize)/1024;   //单位KB   
	     return (freeBlocks * blockSize)/1024 /1024; //单位MB   
	   } 
	/**
	 * 
	* @Title: getSDRoot
	* @Description: 获取sd卡根目录
	* @param @return
	* @return String
	* @throws
	 */
	public  static String getSDRoot(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	/**
	 * 
	* @Title: getFilePath
	* @Description: 获取文件路径
	* @param @param context
	* @param @return
	* @param @throws NameNotFoundException
	* @return String
	* @throws
	 */
	public static String getFilePath(Context context) throws NameNotFoundException{
		String dir=getSDRoot()+"/"+getAppName(context)+"/file";
		File f=new File(dir);
		if(!f.exists()){
			f.mkdirs();
		}
		return dir;
	}
	/**
	 * 
	* @Title: getImagePath
	* @Description:获取图片路径
	* @param @param context
	* @param @return
	* @param @throws NameNotFoundException
	* @return String
	* @throws
	 */
	public static String getImagePath(Context context) throws NameNotFoundException{
		String dir=getSDRoot()+"/"+getAppName(context)+"/image";
		File f=new File(dir);
		if(!f.exists()){
			f.mkdirs();
		}
		return dir;
	}
	/**
	 * 
	* @Title: getAppName
	* @Description:获取应用程序的名称
	* @param @param context
	* @param @return
	* @param @throws NameNotFoundException
	* @return String
	* @throws
	 */
	public static String getAppName(Context context)throws NameNotFoundException{
		PackageManager pm=context.getPackageManager();
				ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
				return info.loadLabel(pm).toString();
	}
	/**
	 * 
	* @Title: getFileName
	* @Description:根据路径获取文件名
	* @param @param dir 路径
	* @param @return
	* @param @throws NameNotFoundException
	* @return String
	* @throws
	 */
	public static String getFileName(String dir)throws NameNotFoundException{
		if(dir.contains("/")){
			return dir.substring(dir.lastIndexOf("/")+1);
		}else{
			return dir;
		}
	
	}
	/**
	 * 
	 * 描述: 获取文件的全路径
	 * @param context
	 * @param dir
	 * @return
	 * @throws IOException
	 * @throws NameNotFoundException
	 */
	public static String getFilePath(Context context,String dir) throws IOException, NameNotFoundException{
		String path=getFilePath(context)+"/"+getFileName(dir);
		File file=new File(path);
		if(!file.exists()){
			file.createNewFile();
		}
		return path;
	}
	/**
	 * 
	 * 描述: 获取图片的全路径
	 * @param context
	 * @param dir
	 * @return
	 * @throws IOException
	 * @throws NameNotFoundException
	 */
	public static String getImgPath(Context context,String dir) throws IOException, NameNotFoundException{
		String path=getImagePath(context)+"/"+getFileName(dir);
		File file=new File(path);
		if(!file.exists()){
			file.createNewFile();
		}
		return path;
	}
	
	/**
	 * 
	 * 描述: 获取图片的全路径
	 * @param context
	 * @param dir
	 * @return
	 * @throws IOException
	 * @throws NameNotFoundException
	 */
	public static String getUnity3dPath(Context context,String dir) throws IOException, NameNotFoundException{
		String dirs=getSDRoot()+"/"+getAppName(context)+"/unity3d";
		File f=new File(dirs);
		if(!f.exists()){
			f.mkdirs();
		}
		String path=dirs+"/"+getFileName(dir);
		File file=new File(path);
		if(!file.exists()){
			file.createNewFile();
		}
		return path;
	}
	
	/**
	 * 
	 * 描述: 获取图片的全路径
	 * @param context
	 * @param dir
	 * @return
	 * @throws IOException
	 * @throws NameNotFoundException
	 */
	public static String getUnity3dDirs(Context context) throws IOException, NameNotFoundException{
		String dirs=getSDRoot()+"/"+getAppName(context)+"/unity3d";
		File f=new File(dirs);
		if(!f.exists()){
			f.mkdirs();
		}
		return dirs;
	}
}