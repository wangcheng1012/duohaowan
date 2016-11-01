package com.wlj.base.util;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.lang.ref.SoftReference;  
import java.net.URL;  
import java.util.HashMap;  

import com.wlj.base.util.img.ImageGetForHttp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;  
import android.os.Environment;  
import android.os.Handler;  

/**
 * 异步加载图片
 * @author wlj
 *
 */
public class SyncImageLoader {  
  
    private Object lock = new Object();  
      
    private boolean mAllowLoad = true;  
      
    private boolean firstLoad = true;  
      
    private int mStartLoadLimit = 0;  
      
    private int mStopLoadLimit = 0;  
      
    final Handler handler = new Handler();  
      
    private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();     
      
    public interface OnImageLoadListener {  
        public void onImageLoad(Integer t, Drawable drawable);  
        public void onError(Integer t);  
    }  
      
    public void setLoadLimit(int startLoadLimit,int stopLoadLimit){  
        if(startLoadLimit > stopLoadLimit){  
            return;  
        }  
        mStartLoadLimit = startLoadLimit;  
        mStopLoadLimit = stopLoadLimit;  
    }  
      
    public void restore(){  
        mAllowLoad = true;  
        firstLoad = true;  
    }  
          
    public void lock(){  
        mAllowLoad = false;  
        firstLoad = false;  
    }  
      
    public void unlock(){  
        mAllowLoad = true;  
        synchronized (lock) {  
            lock.notifyAll();  
        }  
    }  
    /**
     * 
     * @param t mStartLoadLimit、mStopLoadLimit，listener会返回次参数
     * @param imageUrl url
     * @param listener 图片加载完后的返回
     * @param author1  图片名称
     */
    public void loadImage(Integer t, String imageUrl,OnImageLoadListener listener,String author1) {  
        final OnImageLoadListener mListener = listener;  
        final String mImageUrl = imageUrl;  
        final Integer mt = t;  
        final String author = author1;  
          
        ExecutorServices.getExecutorService().execute(new Runnable() {  
  
            @Override  
            public void run() {  
                if(!mAllowLoad){  
                    synchronized (lock) {  
                        try {  
                            lock.wait();  
                        } catch (InterruptedException e) {  
                            e.printStackTrace();  
                        }  
                    }  
                }  
                  
                if(mAllowLoad && firstLoad){  
                    loadImage(mImageUrl, mt, mListener,author);  
                }  
                  
                if(mAllowLoad && mt <= mStopLoadLimit && mt >= mStartLoadLimit){  
                    loadImage(mImageUrl, mt, mListener,author);  
                }  
            }  
  
        });  
    }  
      
    private void loadImage(final String mImageUrl,final Integer mt,final OnImageLoadListener mListener,final String author){  
          
        if (imageCache.containsKey(mImageUrl)) {    
            System.out.println("drawable");  
            SoftReference<Drawable> softReference = imageCache.get(mImageUrl);    
            final Drawable d = softReference.get();    
            if (d != null) {    
                handler.post(new Runnable() {  
                    @Override  
                    public void run() {  
                        if(mAllowLoad){  
                            mListener.onImageLoad(mt, d);  
                        }  
                    }  
                });  
                return;    
            }    
        }    
        try {  
            final Drawable d = loadImageFromUrl(mImageUrl,author);  
            if(d != null){  
                imageCache.put(mImageUrl, new SoftReference<Drawable>(d));  
            }  
            handler.post(new Runnable() {  
                @Override  
                public void run() {  
                    if(mAllowLoad){  
                        mListener.onImageLoad(mt, d);  
                    }  
                }  
            });  
        } catch (IOException e) {  
            handler.post(new Runnable() {  
                @Override  
                public void run() {  
                    mListener.onError(mt);  
                }  
            });  
            e.printStackTrace();  
        }  
    }  

    
   /**
    * 
    * @param url 图片url
    * @param author 图片名称
    * @return Drawable
    * @throws IOException
    */
    public static Drawable loadImageFromUrl(String url,String author) throws IOException {  
        //是否SD卡可用  
        if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  
            //检查是或有保存图片的文件夹，没有就穿件一个  
            String FileUrl = AppConfig.getAppConfig().getImagePath();
            File folder = new File(FileUrl);  
            if(!folder.exists()){  
                folder.mkdir();  
            }  
            File f = new File(FileUrl+author+".jpg");  
            //SD卡中是否有该文件，有则直接读取返回  
            if(f.exists()){  
                FileInputStream fis = new FileInputStream(f);  
                Drawable d = Drawable.createFromStream(fis, "src");  
                return d;  
            }  
            //没有的话则去连接下载，并写入到SD卡中  
            Bitmap bitmap = ImageGetForHttp.downloadBitmap(url);
            
            return new BitmapDrawable(null,bitmap);  
        }  
        //SD卡不可用则直接加载使用  
        else{  
            URL m = new URL(url);  
            InputStream i = (InputStream) m.getContent();  
            Drawable d = Drawable.createFromStream(i, "src");  
            return d;  
        }  
          
    }  
}  