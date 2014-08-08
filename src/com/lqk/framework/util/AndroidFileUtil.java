package com.lqk.framework.util;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
/**
 * 
* @ClassName: AndroidFileUtil
* @Description: android 获取意图打开文件
* @author longqiankun
* @date 2014-7-7 上午10:54:29
*
 */
public class AndroidFileUtil {
	/**
	 * 
	* @Title: openFile
	* @Description: 打开文件相应的意图
	* @param filePath 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent openFile(String filePath){

		File file = new File(filePath);
		if(!file.exists()) return null;
		/* 取得扩展名 */
		String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase(); 
		/* 依扩展名的类型决定MimeType */
		if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
				end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
			return getAudioFileIntent(filePath);
		}else if(end.equals("3gp")||end.equals("mp4")){
			return getAudioFileIntent(filePath);
		}else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
				end.equals("jpeg")||end.equals("bmp")){
			return getImageFileIntent(filePath);
		}else if(end.equals("apk")){
			return getApkFileIntent(filePath);
		}else if(end.equals("ppt")){
			return getPptFileIntent(filePath);
		}else if(end.equals("xls")){
			return getExcelFileIntent(filePath);
		}else if(end.equals("doc")){
			return getWordFileIntent(filePath);
		}else if(end.equals("pdf")){
			return getPdfFileIntent(filePath);
		}else if(end.equals("chm")){
			return getChmFileIntent(filePath);
		}else if(end.equals("txt")){
			return getTextFileIntent(filePath,false);
		}else{
			return getAllIntent(filePath);
		}
	}
	/**
	 * 
	* @Title: openFilendroid获取一个用于打开APK文件的intent 打开文件相应的意图
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getAllIntent( String param ) {

		Intent intent = new Intent();  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		intent.setAction(android.content.Intent.ACTION_VIEW);  
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"*/*"); 
		return intent;
	}
	/**
	 * 
	* @Title: Android获取一个用于打开APK文件的intent
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getApkFileIntent( String param ) {

		Intent intent = new Intent();  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		intent.setAction(android.content.Intent.ACTION_VIEW);  
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"application/vnd.android.package-archive"); 
		return intent;
	}

	/**
	 * 
	* @Title: Android获取一个用于打开VIDEO文件的intent
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getVideoFileIntent( String param ) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	/**
	 * 
	* @Title: Android获取一个用于打开AUDIO文件的intent
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getAudioFileIntent( String param ){

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}
	/**
	 * 
	* @Title: Android获取一个用于打开Html文件的intent 
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getHtmlFileIntent( String param ){

		Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}
	/**
	* @Title: Android获取一个用于打开图片文件的intent
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getImageFileIntent( String param ) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	/**
	* @Title: Android获取一个用于打开PPT文件的intent
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getPptFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");   
		return intent;   
	}   
	/**
	* @Title: Android获取一个用于打开Excel文件的intent
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getExcelFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/vnd.ms-excel");   
		return intent;   
	}   
	/**
	* @Title: Android获取一个用于打开Word文件的intent   
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getWordFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/msword");   
		return intent;   
	}   

	/**
	* @Title: Android获取一个用于打开CHM文件的intent     
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getChmFileIntent( String param ){   

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/x-chm");   
		return intent;   
	}   
	/**
	* @Title: Android获取一个用于打开文本文件的intent    
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getTextFileIntent( String param, boolean paramBoolean){   

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		if (paramBoolean){   
			Uri uri1 = Uri.parse(param );   
			intent.setDataAndType(uri1, "text/plain");   
		}else{   
			Uri uri2 = Uri.fromFile(new File(param ));   
			intent.setDataAndType(uri2, "text/plain");   
		}   
		return intent;   
	}  
	/**
	* @Title: Android获取一个用于打开PDF文件的intent
	* @param param 文件路径
	* @return Intent 文件对应的意图
	* @throws
	 */
	public static Intent getPdfFileIntent( String param ){   

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/pdf");   
		return intent;   
	}
}
