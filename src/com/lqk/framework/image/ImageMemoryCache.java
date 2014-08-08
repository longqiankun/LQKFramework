package com.lqk.framework.image;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;

/**   
 * @Title: ImageMemoryCache.java 
 * @Package com.dilitech.parentchilld.parents.utils 
 * @Description:  
 * @author longqiankun   
 * @date 2013-6-14 下午5:15:15 
 * @version V1.0  
 * @Email:qiankun.long@dilitech.com
 */
public class ImageMemoryCache {
	private Context context;
	private Map<String, SoftReference<Bitmap>> map;
	public ImageMemoryCache(Context context) {
		super();
		this.context = context;
		map=new HashMap<String, SoftReference<Bitmap>>();
	}
	/**
	 * 
	* @Title: getBitmapFromCache
	* @Description: 获取软引用中的图片
	* @param @param url
	* @param @return
	* @return SoftReference<Bitmap>
	* @throws
	 */
	public SoftReference<Bitmap> getBitmapFromCache(String url){
		return map.get(url);
	}
	/**
	 * 
	* @Title: addBitmapToCache
	* @Description: 向缓存中添加图片
	* @param @param url
	* @param @param result
	* @return void
	* @throws
	 */
		public void addBitmapToCache(String url,SoftReference<Bitmap> result){
			if(map.size()>50){
				Set<String> keySet = map.keySet();
				Iterator<String> iterator = keySet.iterator();
				if(iterator.hasNext()){
					String key=iterator.next();
					SoftReference<Bitmap> sb=map.get(key);
					Bitmap b=sb.get();
					if(b!=null){
							b=null;
					}
					map.remove(key);
					System.gc();
				}
				
			}
			map.put(url, result);
		}
}
