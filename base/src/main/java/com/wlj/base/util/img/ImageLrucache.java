package com.wlj.base.util.img;

import com.wlj.base.util.GetResourceImage;
import com.wlj.base.util.Log;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Lrucache 图片缓存
 * @author wlj
 *
 */
public class ImageLrucache {

	private LruCache<String, Bitmap> lrucache;
	private static ImageLrucache mImageLrucache;
	
	public ImageLrucache(){
		//获取系统分配给每个应用程序的最大内存，每个应用系统分配32M  
		long maxMemory = Runtime.getRuntime().maxMemory();
		
		//给LruCache分配1/4 8M  
		int cacheSize = (int) (maxMemory/4);
		
		lrucache =  new LruCache<String, Bitmap>(cacheSize){
			
			//必须重写此方法，来测量Bitmap的大小  
			@Override
			protected int sizeOf(String key, Bitmap value) {
				int size = value.getRowBytes()* value.getHeight();
				return size;
			}
			
		};
	}
	
	public static ImageLrucache getInstance(){
		if(mImageLrucache == null){
			mImageLrucache = new ImageLrucache();
		}
		return mImageLrucache;
	}
	
	 /** 
     * 添加Bitmap到内存缓存 
     * @param key 
     * @param bitmap 
     */ 
	public void addBitmapToMemoryCache(String key,Bitmap bitmap){
		 if (getBitmapFromMemCache(key) == null && bitmap != null) {   
			 lrucache.put(key, bitmap);
			 Log.i("ImageLrucache", lrucache.size()+"  "+lrucache.createCount());
		 }
	}
	
	 /** 
     * 从内存缓存中获取一个Bitmap 
     * @param key 
     * @return 
     */  
	public Bitmap getBitmapFromMemCache(String key) {
		
		return lrucache.get(key);
	}
	
	/**
	 * 获取 资源文件  图片
	 */
	public Bitmap getResourceBitmap(int resId, Context context) {

		Bitmap bitmapImage = null;
		bitmapImage = getBitmapFromMemCache(resId+"");
		
		if (bitmapImage == null) {
			bitmapImage = GetResourceImage.get(context, resId);
			this.addBitmapToMemoryCache(resId+"",bitmapImage);
		}
		return bitmapImage;
	}
	
	public void clearCache(){
		lrucache.evictAll();
		System.gc();
		System.runFinalization();
	}
	
}
