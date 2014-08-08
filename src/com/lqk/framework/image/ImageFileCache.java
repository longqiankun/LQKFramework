package com.lqk.framework.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.lqk.framework.util.Logger;
import com.lqk.framework.util.SdCardUtils;
/**   
 * @Title: ImageFileCache.java 
 * @Package com.dilitech.parentchilld.parents.utils 
 * @Description:  
 * @author longqiankun   
 * @date 2013-6-14 下午5:15:29 
 * @version V1.0  
 * @Email:qiankun.long@dilitech.com
 */
public class ImageFileCache {
	private static String dir;
	Context context;
	public ImageFileCache(Context context) {
		super();
		this.context=context;
		if(SdCardUtils.ExistSDCard()){
			try {
			
				dir=SdCardUtils.getImagePath(context);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
/**
 * 
* @Title: getImage
* @Description: 根据路径获取bitmap
* @param @param url 文件路径
* @return Bitmap 转换后的bitmap
* @throws
 */
	public Bitmap getImage(String url){
		if(SdCardUtils.ExistSDCard()){
		String bitmapName = url
				.substring(url.lastIndexOf("/")+1);
		File cacheDir = new File(dir);
		File[] cacheFiles = cacheDir.listFiles();
		int i = 0;
		if (null != cacheFiles) {
			for (; i < cacheFiles.length; i++) {
				if (bitmapName.equals(cacheFiles[i].getName())) {
					break;
				}
			}
			if (i < cacheFiles.length) {
				/*return BitmapFactory.decodeFile(dir
						+ File.separator+bitmapName);*/
				BitmapFactory.Options opts = new BitmapFactory.Options();  
				opts.inJustDecodeBounds = true;  
				Rect rect=new Rect();
				Bitmap b =BitmapFactory.decodeFile(dir
						+ File.separator+bitmapName,opts);
				opts.inSampleSize =1;  
				opts.inJustDecodeBounds = false; 
				opts.inPurgeable = true;
				opts.inInputShareable = true;
				b=BitmapFactory.decodeFile(dir
						+ File.separator+bitmapName,opts);
				return b;
			}
		}
		}
		return null;
//		return map.get(url);
	}
	/**
	 * 
	* @Title: saveBitmap
	* @Description: 保存图片到本地
	* @param @param url
	* @return void
	* @throws
	 */
	public void saveBitmap(Bitmap result,String url){
		InputStream bitmapIs =getStreamFromURL(url);
		BitmapFactory.Options opts = new BitmapFactory.Options();  
//		opts.inJustDecodeBounds = true;  
		Rect rect=new Rect();
//		Bitmap bitmap =BitmapFactory.decodeStream(bitmapIs,rect, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 90*90);  
		opts.inJustDecodeBounds = false; 
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		Bitmap bitmap = BitmapFactory.decodeStream(bitmapIs,rect, opts);
		if(bitmap!=null){
			File dirs = new File(dir);
			if (!dirs.exists()) {
				dirs.mkdirs();
			}
			File bitmapFile = new File(dir
					,url.substring(url.lastIndexOf("/") + 1));
			if (!bitmapFile.exists()) {
				try {
					bitmapFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(bitmapFile);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			Logger.getLogger(this).i("bitmap is null can't save");
		}
	}
	/**
	 * 
	* @Title: computeSampleSize
	* @Description: 计算图片的缩放比例
	* @param @param options
	* @param @param minSideLength
	* @param @param maxNumOfPixels
	* @param @return
	* @return int
	* @throws
	 */
	public static int computeSampleSize(BitmapFactory.Options options,  
	        int minSideLength, int maxNumOfPixels) {  
	    int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);  
	  
	    int roundedSize;  
	   if (initialSize <= 8 ) {  
	       roundedSize = 1;  
	       while (roundedSize < initialSize) {  
	           roundedSize <<= 1;  
	        }  
	    } else {  
	       roundedSize = (initialSize + 7) / 8 * 8;  
	    }  
	    return roundedSize;  
	}  
	  /**
	   * 
	  * @Title: computeInitialSampleSize
	  * @Description:计算图片的初始缩放比例
	  * @param @param options
	  * @param @param minSideLength
	  * @param @param maxNumOfPixels
	  * @param @return
	  * @return int
	  * @throws
	   */
private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
	    double w = options.outWidth;  
	    double h = options.outHeight;  
	 
	    int lowerBound = (maxNumOfPixels == -1) ? 1 :  
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
	    int upperBound = (minSideLength == -1) ? 128 :  
	            (int) Math.min(Math.floor(w / minSideLength),  
	           Math.floor(h / minSideLength));  
	  
	    if (upperBound < lowerBound) {  
	        // return the larger one when there is no overlapping zone.   
	       return lowerBound;  
	   }  
	  
	    if ((maxNumOfPixels == -1) &&  
	            (minSideLength == -1)) {  
	        return 1;  
	    } else if (minSideLength == -1) {  
	       return lowerBound;  
	    } else {  
	        return upperBound;  
	    }  
	}  
/**
 * 
* @Title: getStreamFromURL
* @Description: 根据路径获取输入流
* @param @param imageURL
* @param @return
* @return InputStream
* @throws
 */
public static InputStream getStreamFromURL(String imageURL){ 
	InputStream in=null; 
	try{ 
	URL url=new URL(imageURL); 
	HttpURLConnection connection=(HttpURLConnection)url.openConnection(); 
	in=connection.getInputStream(); 
	}catch(Exception e){ 
	e.printStackTrace(); 
	} 
	return in; 

	} 
/**
 * 
* @Title: compressImage
* @Description:图片压缩
* @param @param image 要压缩的图片
* @return Bitmap
* @throws
 */
public static Bitmap compressImage(Bitmap image) {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
	int options = 100;
	while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
	if(options<0){
		break;
	}
		baos.reset();//重置baos即清空baos
		image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
		options -= 10;//每次都减少10
	}
	ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
	Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
	return bitmap;
}
/**
 * 
 * 描述:
 * @param srcPath
 * @param width
 * @param height
 * @param isCompress 是否需要进行质量压缩
 * @return
 */
public static Bitmap getImage(String srcPath,float width,float height,boolean isCompress) {
	BitmapFactory.Options newOpts = new BitmapFactory.Options();
	//开始读入图片，此时把options.inJustDecodeBounds 设回true了
	newOpts.inJustDecodeBounds = true;
	Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
	newOpts.inJustDecodeBounds = false;
	if((width>0||height>0)){
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = width;
		float ww = height;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
	}else{
		newOpts.inSampleSize = 1;
	}
	//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
	bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
	if(isCompress){
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}else{
		return bitmap;
	}
	
}

/**
 * 
 * 描述: 将字节数组转换成图片，并进行压缩
 * @param srcPath 
 * @param width
 * @param height
 * @param isCompress 是否需要进行质量压缩
 * @return
 */
public Bitmap getImage(byte[] bCode,float width,float height,boolean isCompress) {
	BitmapFactory.Options newOpts = new BitmapFactory.Options();
	//开始读入图片，此时把options.inJustDecodeBounds 设回true了
	newOpts.inJustDecodeBounds = true;
	Bitmap bitmap = BitmapFactory.decodeByteArray(bCode, 0, bCode.length,newOpts);//此时返回bm为空
	newOpts.inJustDecodeBounds = false;
	if(width>0||height>0){
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = width;
		float ww = height;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
	}else{
		newOpts.inSampleSize = 1;
	}
	//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
	bitmap = BitmapFactory.decodeByteArray(bCode, 0, bCode.length,newOpts);
	if(isCompress){
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}else{
		return bitmap;
	}
	
}
/**
 * 
 * 描述: 根据路径获取图片
 * @param srcPath 路径 
 * @param width 图片的宽
 * @param height 图片的高度
 * @param isCompress 是否需要进行质量压缩
 * @return
 */
public Drawable getImageDrawable(String srcPath,float width,float height,boolean isCompress) {
	BitmapFactory.Options newOpts = new BitmapFactory.Options();
	//开始读入图片，此时把options.inJustDecodeBounds 设回true了
	newOpts.inJustDecodeBounds = true;
	Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
	newOpts.inJustDecodeBounds = false;
	if(width>0||height>0){
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = width;
		float ww = height;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
	}else{
		newOpts.inSampleSize = 1;
	}
	//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
	bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
	if(isCompress){
		Bitmap b=compressImage(bitmap);
		Drawable drawable = new BitmapDrawable(context.getResources(),b ); 
		return drawable;//压缩好比例大小后再进行质量压缩
	}else{
		Drawable drawable = new BitmapDrawable(context.getResources(),bitmap ); 
		return drawable;
	}
	
}
/**
 * 
* @Title: getimage
* @Description: 根据路径获取图片
* @param @param srcPath
* @param @return
* @return Bitmap
* @throws
 */
public Bitmap getimage(String srcPath) {
	BitmapFactory.Options newOpts = new BitmapFactory.Options();
	//开始读入图片，此时把options.inJustDecodeBounds 设回true了
	newOpts.inJustDecodeBounds = true;
	Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
	
	newOpts.inJustDecodeBounds = false;
	int w = newOpts.outWidth;
	int h = newOpts.outHeight;
	//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
	float hh = 800f;//这里设置高度为800f
	float ww = 480f;//这里设置宽度为480f
	//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
	int be = 1;//be=1表示不缩放
	if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
		be = (int) (newOpts.outWidth / ww);
	} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
		be = (int) (newOpts.outHeight / hh);
	}
	if (be <= 0)
		be = 1;
	newOpts.inSampleSize = be;//设置缩放比例
	//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
	bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
	return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
}
/**
 * 
* @Title: comp
* @Description: 对图片进行质量和尺寸压缩
* @param @param image
* @param @return
* @return Bitmap
* @throws
 */
public static Bitmap comp(Bitmap image) {
	
	ByteArrayOutputStream baos = new ByteArrayOutputStream();		
	image.compress(Bitmap.CompressFormat.PNG, 100, baos);
	if( baos.toByteArray().length / 1024>100) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出	
		baos.reset();//重置baos即清空baos
		image.compress(Bitmap.CompressFormat.PNG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
	}
	ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
	BitmapFactory.Options newOpts = new BitmapFactory.Options();
	//开始读入图片，此时把options.inJustDecodeBounds 设回true了
	newOpts.inJustDecodeBounds = true;
	Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
	newOpts.inJustDecodeBounds = false;
	int w = newOpts.outWidth;
	int h = newOpts.outHeight;
	//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
	float hh = 800f;//这里设置高度为800f
	float ww = 480f;//这里设置宽度为480f
	//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
	int be = 1;//be=1表示不缩放
	if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
		be = (int) (newOpts.outWidth / ww);
	} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
		be = (int) (newOpts.outHeight / hh);
	}
	if (be <= 0)
		be = 1;
	newOpts.inSampleSize = be;//设置缩放比例
	//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
	isBm = new ByteArrayInputStream(baos.toByteArray());
	bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
	return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
}
/**
 * 
* @Title: saveImgTOLocal
* @Description: 保存图片到本地
* @param @param path
* @param @param bitmap
* @return void
* @throws
 */
public static void saveImgTOLocal(String path,Bitmap bitmap){
	if(bitmap!=null){
		File dirs = new File(dir);
		if (!dirs.exists()) {
			dirs.mkdirs();
		}
		File bitmapFile = new File(dir
				,path.substring(path.lastIndexOf("/") + 1));
		if (!bitmapFile.exists()) {
			try {
				bitmapFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(bitmapFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}else{
		Logger.getLogger("ImageFileCache").i( "bitmap is null can't save");
	}
}
}
