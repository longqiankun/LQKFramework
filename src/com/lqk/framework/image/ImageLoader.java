package com.lqk.framework.image;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
/**
 * 
* @ClassName: ImageLoader
* @Description: 图片加载类
* @author longqiankun
* @date 2014-7-7 上午11:36:52
*
 */
public class ImageLoader {   
	private static ImageLoader instance;   
	private ExecutorService executorService;  
	public static final int ScalSize = 2;
	//线程池    
	private ImageMemoryCache memoryCache;     //内存缓存   
	private ImageFileCache fileCache;        //文件缓存    
	private Map<String, ImageView> taskMap; //存放任务 
	private int px;
	private boolean isround=false;
	private boolean allowLoad = true; //是否允许加载图片 
	Context context;
	private ImageLoader(Context context) {         // 获取当前系统的CPU数目       
		int cpuNums = Runtime.getRuntime().availableProcessors();         //根据系统资源情况灵活定义线程池大小       
		this.executorService = Executors.newFixedThreadPool(cpuNums + 1);    
		this.memoryCache = new ImageMemoryCache(context);      
		this.fileCache = new ImageFileCache(context);     
		this.taskMap = new HashMap<String, ImageView>();  
		this.context=context;
		}                                                     
	/**      * 使用单例，保证整个应用中只有一个线程池和一份内存缓存和文件缓存      */  
	public static ImageLoader getInstance(Context context) {     
		if (instance == null)          
			instance = new ImageLoader(context);    
		return instance;     }                  
	/**      * 恢复为初始可加载图片的状态      */
   public void restore() {      
	   this.allowLoad = true;    
}     
   /**      * 锁住时不允许加载图片      */  
   public void lock() {       
	   this.allowLoad = false;    
	   }                                     
   /**      * 解锁时加载图片      */    public void unlock() {    
	   this.allowLoad = true;       
	   doTask();   
	   }                             
   /**      * 添加任务      */  
public void addTask(String url, ImageView img) {  
	   //先从内存缓存中获取，取到直接加载 
	   Bitmap bitmap = null; 
	   SoftReference<Bitmap> sb=memoryCache.getBitmapFromCache(url);
	   if(sb!=null){
		   bitmap=sb.get();
	   }else{
		   bitmap=null;
	   }
	   if (bitmap != null) {
		   View v=(View) img.getParent();
		   img.setImageBitmap(bitmap);   
	   } else {        
		   synchronized (taskMap) {       
			   /**                  * 因为ListView或GridView的原理是用上面移出屏幕的item去填充下面新显示的item,  
			    *                 * 这里的img是item里的内容，所以这里的taskMap保存的始终是当前屏幕内的所有ImageView。       
			    *                            */          
			   img.setTag(url);                
			   taskMap.put(Integer.toString(img.hashCode()), img);     
			   }      
		   if (allowLoad) {       
			   doTask();        
    }         }     }      
   
   /**      * 添加任务      */  
   public void addTask(String url, ImageView img,boolean isround,int px) {  
	   this.isround=isround;
	   this.px=px;
	   //先从内存缓存中获取，取到直接加载 
	   Bitmap bitmap = null; 
	   SoftReference<Bitmap> sb=memoryCache.getBitmapFromCache(url);
	   if(sb!=null){
		   bitmap=sb.get();
	   }else{
		   bitmap=null;
	   }    
	   if (bitmap != null) {  
		   View v=(View) img.getParent();
		   if(isround){
			   img.setImageBitmap(bitmap);  
		   }else{
			   img.setImageBitmap(bitmap);  
		   }
		   
	   } else {        
		   synchronized (taskMap) {       
			   /**                  * 因为ListView或GridView的原理是用上面移出屏幕的item去填充下面新显示的item,  
			    *                 * 这里的img是item里的内容，所以这里的taskMap保存的始终是当前屏幕内的所有ImageView。       
			    *                            */          
			   img.setTag(url);                
			   taskMap.put(Integer.toString(img.hashCode()), img);     
			   }      
		   if (allowLoad) {       
			   doTask();        
    }         }     }    
   
   /**      * 加载存放任务中的所有图片      */  
   private void doTask() {     
	   synchronized (taskMap) {       
		   Collection<ImageView> con = taskMap.values();     
		   for (ImageView i : con) {          
			   if (i != null) {                
				   if (i.getTag() != null) {              
					   loadImage((String) i.getTag(), i);      
					   
				   }                 }             }          
		   taskMap.clear();     
		   }     }                      
   private void loadImage(String url, ImageView img) {   
	   this.executorService.submit(new TaskWithResult(new TaskHandler(url, img), url));     }        
   /*** 获得一个图片,从三个地方获取,首先是内存缓存,然后是文件缓存,最后从网络获取 ***/ 
   public Bitmap getBitmap(String url) {     
	   // 从内存缓存中获取图片  
	   Bitmap result = null; 
	   SoftReference<Bitmap> sb=memoryCache.getBitmapFromCache(url);
	   if(sb!=null){
		   result=sb.get();
	   }else{
		   result=null;
	   }
	   if (result == null) {  
		   // 文件缓存中获取        
		   result = fileCache.getImage(url);          
		  if (result == null) {
			   // 从网络获取            
			 result=loadImageFromUrl(context, url, 480, 800);
			   if (result != null) {   
				   fileCache.saveBitmap(result, url);     
				   memoryCache.addBitmapToCache(url, new SoftReference<Bitmap>(result));          
				   }             } else {  
					   // 添加到内存缓存             
					   memoryCache.addBitmapToCache(url, new SoftReference<Bitmap>(result));   
					   }   
		   }else{
		   }
	   return result;     }     
   /*** 子线程任务 ***/
   private class TaskWithResult implements Callable<String> { 
	   private String url;    
	   private Handler handler;  
	   public TaskWithResult(Handler handler, String url) {    
		   this.url = url;        
		   
		   this.handler = handler;         }     
	   @Override  
	   public String call() throws Exception {     
		   
		   Message msg = new Message();       
		   msg.obj = getBitmap(url);         
		   if (msg.obj != null) {              
			   handler.sendMessage(msg);   
			   }       
		   return url;  
      }     }          
   /*** 完成消息 ***/  
   private class TaskHandler extends Handler {     
	   String url;     
	   ImageView img;     
	   public TaskHandler(String url, ImageView img) {  
		   this.url = url;         
		   this.img = img;     
		   }                       
	   @Override    
	   public void handleMessage(Message msg) {    
		   /*** 查看ImageView需要显示的图片是否被改变  ***/     
		   if (img.getTag().equals(url)) {       
			   if (msg.obj != null) {              
				   Bitmap bitmap = (Bitmap) msg.obj;  
				   View v=(View) img.getParent();
				   img.setImageBitmap(bitmap);             
				   }        
			   }else{
			   }
		   }    
	   }                      
   /**
    * 
   * @Title: getStreamFromURL
   * @Description: 将请求的返回流转换的图片
   * @param @param imageURL
   * @param @return
   * @return Bitmap
   * @throws
    */
	public static Bitmap getStreamFromURL(String imageURL){ 
		InputStream in=null; 
		try{ 
		URL url=new URL(imageURL); 
		HttpURLConnection connection=(HttpURLConnection)url.openConnection(); 
		in=connection.getInputStream(); 
		}catch(Exception e){ 
		e.printStackTrace(); 
		} 
		Bitmap bitmap=BitmapFactory.decodeStream(in);
		return bitmap; 
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
	    * @Title: loadImageFromUrl
	    * @Description: 加载图片
	    * @param @param context
	    * @param @param url
	    * @param @param width
	    * @param @param height
	    * @param @return
	    * @return Bitmap
	    * @throws
	     */
	    public  Bitmap loadImageFromUrl(Context context, String url, int width,
				int height) {
			URL m;
			InputStream i = null;
			try {
				m = new URL(url);
				i = m.openStream();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	 

			Options bitmapFactoryOptions = new BitmapFactory.Options();
			// 下面这个设置是将图片边界不可调节变为可调节
			bitmapFactoryOptions.inJustDecodeBounds = true;
			bitmapFactoryOptions.inSampleSize = 2;
			int outWidth = bitmapFactoryOptions.outWidth;
			int outHeight = bitmapFactoryOptions.outHeight;
			Bitmap bmap = BitmapFactory.decodeStream(i, new Rect(0, 0, 0, 0),
					bitmapFactoryOptions);

			float imagew = width/ScalSize;
			float imageh = height/ScalSize;
			int yRatio = (int) Math.ceil(bitmapFactoryOptions.outHeight / imageh);
			int xRatio = (int) Math.ceil(bitmapFactoryOptions.outWidth / imagew);
			if (yRatio > 1 || xRatio > 1) {
				if (yRatio > xRatio) {
					bitmapFactoryOptions.inSampleSize = yRatio;
				} else {
					bitmapFactoryOptions.inSampleSize = xRatio;
				}
			}
			bitmapFactoryOptions.inJustDecodeBounds = false;
			try {
				m = new URL(url);
				i = m.openStream();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bmap = BitmapFactory.decodeStream(i, new Rect(0, 0, 0, 0),
					bitmapFactoryOptions);
			if (bmap != null) {
			
				return bmap;
			}

			return null;
		}
}
