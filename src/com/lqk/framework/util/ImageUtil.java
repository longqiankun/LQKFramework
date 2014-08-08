package com.lqk.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;

import com.lqk.framework.image.ImageFileCache;
import com.lqk.framework.image.ImageLocalLoader;
import com.lqk.framework.image.ImageLocalLoader.ImageCallback;
/**
 * 
* @ClassName: ImageUtil
* @Description:图片工具
* @author longqiankun
* @date 2014-7-7 上午11:40:22
*
 */
public class ImageUtil {
/**
 * 根据资源ID获取资源
 */
	public static Drawable getDrawableById(Context context, int resId) {

		if (context == null) {

			return null;

		}

		return context.getResources().getDrawable(resId);

	}
	/**
	 * 根据资源ID获取资源
	 */
	public static Bitmap getBitmapById(Context context, int resId) {

		if (context == null) {

			return null;

		}

		return BitmapFactory.decodeResource(context.getResources(), resId);

	}

	/**
	 * 
	 * 将bitmap转换出字节
	 * 
	 * 
	 * 
	 * @param bitmap
	 * 
	 * @return
	 */

	public static byte[] bitmap2byte(Bitmap bitmap) {

		ByteArrayOutputStream baos = null;

		try {

			baos = new ByteArrayOutputStream();

			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

			byte[] array = baos.toByteArray();

			baos.flush();

			baos.close();

			return array;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	/**
	 * 
	 * 将字节转换成bitmap
	 * 
	 * 
	 * 
	 * @param data
	 * 
	 * @return
	 */

	public static Bitmap byte2bitmap(byte[] data) {

		if (null == data) {

			return null;

		}

		return BitmapFactory.decodeByteArray(data, 0, data.length);

	}

	/**
	 * 
	 * 将Drawable转换Bitmap
	 * 
	 * 
	 * 
	 * @param drawable
	 * 
	 * @return
	 */

	public static Bitmap drawable2bitmap(Drawable drawable) {

		if (null == drawable) {

			return null;

		}

		int width = drawable.getIntrinsicWidth();

		int height = drawable.getIntrinsicHeight();

		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable

		.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

		: Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);

		drawable.setBounds(0, 0, width, height);

		drawable.draw(canvas);// �ص�

		return bitmap;

	}

	/**
	 * 
	 * 将bitmap转换drawable
	 * 
	 * 
	 * 
	 * @param bitmap
	 * 
	 * @return
	 */

	public static Drawable bitmap2Drawable(Bitmap bitmap) {

		if (bitmap == null) {

			return null;

		}

		return new BitmapDrawable(bitmap);

	}

	/**
	 * 
	 * 缩放bitmap
	 * 
	 * 
	 * 
	 * @param bitmap
	 * 
	 * @param w
	 * 
	 * @param h
	 * 
	 * @return
	 */

	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {

		if (bitmap == null) {

			return null;

		}

		int width = bitmap.getWidth();

		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();

		float scaleWidht = ((float) w / width);

		float scaleHeight = ((float) h / height);

		matrix.postScale(scaleWidht, scaleHeight);

		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,

		matrix, true);

		return newbmp;

	}

	/**
	 * 保存bitmap到指定目录
	 * @param bitmap
	 * 
	 * @param path
	 */

	public static boolean saveBitmap(Bitmap bitmap, String path) {

		try {

			File file = new File(path);

			File parent = file.getParentFile();

			if (!parent.exists()) {

				parent.mkdirs();

			}

			FileOutputStream fos = new FileOutputStream(file);

			boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

			fos.flush();

			fos.close();

			return b;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return false;

	}

	/**
	 * 
	 *将bitmap压缩后保存到指定的目录
	 * @param bitmap
	 * @param quality
	 * 
	 *            Hint to the compressor, 0-100. 0 meaning compress for small
	 * 
	 *            size, 100 meaning compress for max quality. Some formats, like
	 * 
	 *            PNG which is lossless, will ignore the quality setting
	 * 
	 * @return
	 */

	public static boolean saveBitmap(Bitmap bitmap, String path,

	CompressFormat format, int quality) {

		try {

			File file = new File(path);

			File parent = file.getParentFile();

			if (!parent.exists()) {

				parent.mkdirs();

			}

			FileOutputStream fos = new FileOutputStream(file);

			boolean b = bitmap.compress(format, quality, fos);

			fos.flush();

			fos.close();

			return b;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return false;

	}

	/**
	 * 
	 *获取圆角的bitmap
	 * @param bitmap
	 * 
	 * @param roundPx
	 * 
	 * @return
	 */

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		if (bitmap == null) {

			return null;

		}

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),

		bitmap.getHeight(), Config.ARGB_8888);

		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;

		final Paint paint = new Paint();

		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);

		paint.setColor(color);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;

	}

	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {

		if (bitmap == null) {

			return null;

		}

		final int reflectionGap = 4;

		int width = bitmap.getWidth();

		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();

		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,

		width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,

		(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);

		canvas.drawBitmap(bitmap, 0, 0, null);

		Paint deafalutPaint = new Paint();

		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();

		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,

		bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,

		0x00ffffff, TileMode.CLAMP);

		paint.setShader(shader);

		// Set the Transfer mode to be porter duff and destination in

		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		// Draw a rectangle using the paint with our linear gradient

		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()

		+ reflectionGap, paint);

		return bitmapWithReflection;

	}
	 public static final int ALL = 347120;
	    public static final int TOP = 547120;
	    public static final int LEFT = 647120;
	    public static final int RIGHT = 747120;
	    public static final int BOTTOM = 847120;
	   
	   /**
	    * 
	    * @param type 画圆角的位置，该类中的ALL 表示4个面，分别：TOP LEFT RIGHT BOTTOM
	    * @param bitmap 要被更改的图片
	    * @param roundPx 圆角的角度像素
	    * @return
	    */
	    public static Bitmap fillet(int type,Bitmap bitmap,int roundPx) {
	        try {
	         // 其原理就是：先建立一个与图片大小相同的透明的Bitmap画板
	         // 然后在画板上画出一个想要的形状的区域。
	         // 最后把源图片帖上。
	         final int width = bitmap.getWidth();
	         final int height = bitmap.getHeight();
	         
	            Bitmap paintingBoard = Bitmap.createBitmap(width,height, Config.ARGB_8888);
	            Canvas canvas = new Canvas(paintingBoard);
	            canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
	           
	            final Paint paint = new Paint();
	            paint.setAntiAlias(true);
	            paint.setColor(Color.BLACK);  
	           
	            if( TOP == type ){
	             clipTop(canvas,paint,roundPx,width,height);
	            }else if( LEFT == type ){
	              clipLeft(canvas,paint,roundPx,width,height);
	            }else if( RIGHT == type ){
	             clipRight(canvas,paint,roundPx,width,height);
	            }else if( BOTTOM == type ){
	             clipBottom(canvas,paint,roundPx,width,height);
	            }else{
	             clipAll(canvas,paint,roundPx,width,height);
	            }
	           
	            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	            //帖子图
	            final Rect src = new Rect(0, 0, width, height);
	            final Rect dst = src;
	            canvas.drawBitmap(bitmap, src, dst, paint);  
	            return paintingBoard;
	        } catch (Exception exp) {       
	            return bitmap;
	        }
	    }
	   
	    private static void clipLeft(final Canvas canvas,final Paint paint,int offset,int width,int height){
	        final Rect block = new Rect(offset,0,width,height);
	        canvas.drawRect(block, paint);
	        final RectF rectF = new RectF(0, 0, offset * 2 , height);
	        canvas.drawRoundRect(rectF, offset, offset, paint);
	    }
	   
	    private static void clipRight(final Canvas canvas,final Paint paint,int offset,int width,int height){
	        final Rect block = new Rect(0, 0, width-offset, height);
	        canvas.drawRect(block, paint);
	        final RectF rectF = new RectF(width - offset * 2, 0, width , height);
	        canvas.drawRoundRect(rectF, offset, offset, paint);
	    }
	   
	    private static void clipTop(final Canvas canvas,final Paint paint,int offset,int width,int height){
	        final Rect block = new Rect(0, offset, width, height);
	        canvas.drawRect(block, paint);
	        final RectF rectF = new RectF(0, 0, width , offset * 2);
	        canvas.drawRoundRect(rectF, offset, offset, paint);
	    }
	   
	    private static void clipBottom(final Canvas canvas,final Paint paint,int offset,int width,int height){
	        final Rect block = new Rect(0, 0, width, height - offset);
	        canvas.drawRect(block, paint);
	        final RectF rectF = new RectF(0, height - offset * 2 , width , height);
	        canvas.drawRoundRect(rectF, offset, offset, paint);
	    }
	   
	    private static void clipAll(final Canvas canvas,final Paint paint,int offset,int width,int height){
	     final RectF rectF = new RectF(0, 0, width , height);
	        canvas.drawRoundRect(rectF, offset, offset, paint);
	    }
/**
 * 
* @Description: 给bitmap添加边框
* @param source 
* @param newHeight
* @param newWidth
* @return
* see_to_target
 */
	    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
		    return scaleCenterCrop(source,newHeight,newWidth,Color.TRANSPARENT,10);
		}
	    /**
	     * 
	    * @Description:给bitmap添加边框
	    * @param source
	    * @param newHeight
	    * @param newWidth
	    * @param color 边框的颜色
	    * @return
	    * see_to_target
	     */
	    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth,int color,int edgewidth) {
		    int sourceWidth = source.getWidth();
		    int sourceHeight = source.getHeight();

		    // Compute the scaling factors to fit the new height and width, respectively.
		    // To cover the final image, the final scaling will be the bigger 
		    // of these two.
		    float xScale = (float) newWidth / sourceWidth;
		    float yScale = (float) newHeight / sourceHeight;
		    float scale = Math.max(xScale, yScale);

		    // Now get the size of the source bitmap when scaled
		    float scaledWidth = scale * sourceWidth;
		    float scaledHeight = scale * sourceHeight;

		    // Let's find out the upper left coordinates if the scaled bitmap
		    // should be centered in the new size give by the parameters
		    float left = (newWidth - scaledWidth) / 2;
		    float top = (newHeight - scaledHeight) / 2;

		    // The target rectangle for the new, scaled version of the source bitmap will now
		    // be
		    RectF targetRect = new RectF(left+edgewidth, top+edgewidth, left + scaledWidth, top + scaledHeight);
		    RectF clipRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
		    // Finally, we create a new bitmap of the specified size and draw our new,
		    // scaled bitmap onto it.
		    Bitmap dest = Bitmap.createBitmap(newWidth+edgewidth, newHeight+edgewidth, source.getConfig());
		    Canvas canvas = new Canvas(dest);
//		    canvas.drawColor(color);
//		    Path p=new Path();
//		    p.addRect(clipRect,Direction.CW);
//		    canvas.clipRect(clipRect);
//		    canvas.save();
//		    canvas.clipPath(p);
		    Paint paint=new Paint();
		    paint.setColor(color);
		    paint.setAntiAlias(true);
		    paint.setStrokeWidth(2);
		    paint.setStyle(Style.STROKE);
		    
		    canvas.drawRect(clipRect, paint);
		    canvas.drawBitmap(source, null, targetRect, null);
//		    canvas.restore();
		    return dest;
		}
/**
 * 旋转bitmap的角度
 * @param b
 * @param degrees 旋转的角度
 * @return 
 */
public static Bitmap rotate(Bitmap b, int degrees) {    
	        if (degrees != 0 && b != null) {    
	            Matrix m = new Matrix();    
	            m.setRotate(degrees,    
	                    (float) b.getWidth() / 2, (float) b.getHeight() / 2);    
	            try {    
	                Bitmap b2 = Bitmap.createBitmap(    
	                        b, 0, 0, b.getWidth(), b.getHeight(), m, true);    
	                if (b != b2) {    
	                    b.recycle();  //Android开发网再次提示Bitmap操作完应该显示的释放    
	                    b = b2;    
	                }    
	            } catch (OutOfMemoryError ex) {    
	                // 建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.    
	            }    
	        }    
	        return b;    
	    }  
/**
 * 获取缩放的bitmap
 * @param bitmap
 * @param screenWidth 
 * @param screenHight
 * @return
 */
public static Bitmap getScacleBitmap(Bitmap bitmap, int screenWidth,
		int screenHight) {
	int w = bitmap.getWidth();
	int h = bitmap.getHeight();
	Matrix matrix = new Matrix();
	float scale = (float) screenWidth / w;
	float scale2 = (float) screenHight / h;
	matrix.postScale(scale, scale);
	return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
}
Bitmap bitmap;
public  Bitmap getLocalImage(String path,Context context){
	
	ImageLocalLoader mImageLoader=ImageLocalLoader.getInstance(context);
	try {
		path=SdCardUtils.getImgPath(context,path);
	
		bitmap=new ImageFileCache(context).getImage(path, 0, 0,false);
		if(bitmap==null){
			mImageLoader.getBitmapInTask(path, 0, 0, false, new ImageCallback() {
		@Override
		public void imageLoaded(Bitmap b) {
			bitmap=b;
		}
		});
		}
		
	} catch (NameNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return bitmap;
}

/** 
 * 根据指定的图像路径和大小来获取缩略图 
 * 此方法有两点好处： 
 *     1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度， 
 *        第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 
 *     2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 
 *        用这个工具生成的图像不会被拉伸。 
 * @param imagePath 图像的路径 
 * @param width 指定输出图像的宽度 
 * @param height 指定输出图像的高度 
 * @return 生成的缩略图 
 */  
public static  Bitmap getImageThumbnail(String imagePath, int width, int height) {  
    Bitmap bitmap = null;  
    BitmapFactory.Options options = new BitmapFactory.Options();  
    options.inJustDecodeBounds = true;  
    // 获取这个图片的宽和高，注意此处的bitmap为null  
    bitmap = BitmapFactory.decodeFile(imagePath, options);  
    options.inJustDecodeBounds = false; // 设为 false  
    // 计算缩放比  
    int h = options.outHeight;  
    int w = options.outWidth;  
    int beWidth = w / width;  
    int beHeight = h / height;  
    int be = 1;  
    if (beWidth < beHeight) {  
        be = beWidth;  
    } else {  
        be = beHeight;  
    }  
    if (be <= 0) {  
        be = 1;  
    }  
    options.inSampleSize = be;  
    // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false  
    bitmap = BitmapFactory.decodeFile(imagePath, options);  
    // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象  
    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
    return bitmap;  
}  

/** 
 * 获取视频的缩略图 
 * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。 
 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。 
 * @param videoPath 视频的路径 
 * @param width 指定输出视频缩略图的宽度 
 * @param height 指定输出视频缩略图的高度度 
 * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。 
 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96 
 * @return 指定大小的视频缩略图 
 */  
public static Bitmap getVideoThumbnail(String videoPath, int width, int height,  
        int kind) {  
    Bitmap bitmap = null;  
    // 获取视频的缩略图  
    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
    return bitmap;  
}  

/**
 * 图片创建倒影 TODO(这里用一句话描述这个方法的作用)
 * 
 * @param originalImage
 * @param number
 * @return Bitmap
 */
public static Bitmap createReflectedImage(Bitmap originalImage, int number) {
	final int reflectionGap = 0; // 倒影和原图片间的距离
	int width = originalImage.getWidth();
	int height = originalImage.getHeight();

	Matrix matrix = new Matrix();
	matrix.preScale(1, -1);

	double reflectHeight = number / 100.00;

	number = (int) (height * reflectHeight);
	// 倒影部分
	Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, number, width, number, matrix, false);
	// 要返回的倒影图片
	Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + number), Config.ARGB_8888);

	Canvas canvas = new Canvas(bitmapWithReflection);
	// 画原来的图片
	canvas.drawBitmap(originalImage, 0, 0, null);

	// Paint defaultPaint = new Paint();
	// //倒影和原图片间的距离
	// canvas.drawRect(0, height, width, height + reflectionGap,
	// defaultPaint);
	// 画倒影部分
	canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

	Paint paint = new Paint();
	LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.MIRROR);
	paint.setShader(shader);
	paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
	return bitmapWithReflection;
}

/**
 * 图片增加边框
 * 
 * @param bitmap
 * @param color
 * @return Bitmap
 */
public static Bitmap addFrame(Bitmap bitmap, int color) {
	Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	Canvas canvas = new Canvas(bitmap2);
	Rect rect = canvas.getClipBounds();
	rect.bottom--;
	rect.right--;
	Paint recPaint = new Paint();
	recPaint.setColor(color);
	recPaint.setStyle(Paint.Style.STROKE);
	canvas.drawRect(rect, recPaint);
	canvas.drawBitmap(bitmap, 0, 0, null);
	return bitmap2;
}

}
