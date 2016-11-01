package com.wlj.base.util.img;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.wlj.base.util.ExecutorServices;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

public class LoadImage {

	private ExecutorService executorService; // 固定五个线程来

//	private ImageMemoryCache memoryCache;// 内存缓存
//	private BitmapCache bitmapCache;
	private ImageLrucache imageLrucache;

	private ImageFileCache fileCache;// 文件缓存

	private Map<String, ImageView> taskMap;// 存放任务
	
	private static LoadImage loadImage;

	/**
	 * 在getview时先addtask 当滚动停止的时候调用doTask
	 */
	private LoadImage() {

		executorService = ExecutorServices.getExecutorService();

//		memoryCache = new ImageMemoryCache();
//		bitmapCache = BitmapCache.getInstance();
		imageLrucache = ImageLrucache.getInstance();
		
		fileCache = new ImageFileCache();

		taskMap = new HashMap<String, ImageView>();
		futureList = new ArrayList<String>();
	}

	public static LoadImage getinstall() {
		
		if(loadImage == null){
			loadImage = new LoadImage();
		}
		return loadImage;
	}

	/**
	 * 
	 * @param url
	 * @param img
	 *            imageview的tag是需要下载的url
	 */
	public void addTask(String url, ImageView img) {
		futureList.clear();
		loadcomplete = null;
		if (url == null)
			return;
		img.setTag(url);
		Bitmap bitmap = imageLrucache.getBitmapFromMemCache(url);

		if (bitmap != null && img != null)
		{
			img.setImageBitmap(bitmap);
			img.setVisibility(View.VISIBLE);
		} else {
			synchronized (taskMap) {
				taskMap.put(Integer.toString(img.hashCode()), img);
			}

		}

	}
	
	private LoadComplete loadcomplete;
	private int taskSize;
	private List<String>   futureList;
	
	/**
	 *  统计用的
	 * @param url
	 * @param img
	 * @param loadcomplete
	 */
	public void addTask(String url, ImageView img,LoadComplete loadcomplete) {
		this.loadcomplete = loadcomplete;
		futureList.clear();
		if (url == null)
			return;
		img.setTag(url);
		synchronized (taskMap) {
			taskMap.put(Integer.toString(img.hashCode()), img);
		}
		
	}
	/**
	 * 如果是调用的含统计功能的addTask，需要把所有要统计的task加入（addTask）完，
	 * 最后再doTask，这样才能正确统计任务完成
	 */
	public void doTask() {
		taskSize = taskMap.size();
		synchronized (taskMap) {

			Collection<ImageView> con = taskMap.values();

			for (ImageView i : con) {

				if (i != null) {

					if (i.getTag() != null) {

						loadImage((String) i.getTag(), i);

					}

				}

			}
			taskMap.clear();
		}

	}

	private void loadImage(String url, ImageView img) {

		/*** 加入新的任务 ***/

		Future<String> submit = executorService.submit(new TaskWithResult(new TaskHandler(url, img),url));
		
	}

	/*** 获得一个图片,从三个地方获取,首先是内存缓存,然后是文件缓存,最后从网络获取 ***/

	private Bitmap getBitmap(String url) {

		// 从内存缓存中获取图片
		Bitmap result;

		result = imageLrucache.getBitmapFromMemCache(url);

		if (result == null) {

			// 文件缓存中获取
			result = fileCache.getImage(url);

			if (result == null) {

				// 从网络获取
				result = ImageGetForHttp.downloadBitmap(url);

				if (result != null) {

					imageLrucache.addBitmapToMemoryCache(url, result);

					fileCache.saveBmpToSd(result, url);
//					result = fileCache.getImage(url);
				}

			} else {
				// 添加到内存缓存
				imageLrucache.addBitmapToMemoryCache(url,result);
			}

		}

		return result;

	}

	/*** 完成消息 ***/

	private class TaskHandler extends Handler {

		String url = "";

		ImageView img;

		public TaskHandler(String url, ImageView img) {

			this.url = url;

			this.img = img;

		}

		@Override
		public void handleMessage(Message msg) {

			/*** 查看imageview需要显示的图片是否被改变 ***/

			if (img != null && url.equals(img.getTag())) {

				if (msg.obj != null && img != null) {

					Bitmap bitmap = (Bitmap) msg.obj;

					img.setImageBitmap(bitmap);
					img.setVisibility(View.VISIBLE);
//					bitmap.recycle();
					futureList.add("");
					if(loadcomplete!= null && futureList.size() == taskSize){
						loadcomplete.onComplete();
					}
					
				}

			}

		}

	}

	/*** 子线程任务 ***/

	private class TaskWithResult implements Callable<String> {

		private String url;

		private Handler handler;

		public TaskWithResult(Handler handler, String url) {

			this.url = url;

			this.handler = handler;

		}

		@Override
		public String call() throws Exception {

			Message msg = new Message();

			msg.obj = getBitmap(url);

			if (msg.obj != null) {

				handler.sendMessage(msg);

			}
			
			return url;

		}

	}

	public interface LoadComplete{
		
		void onComplete();
		
	}
	
}
