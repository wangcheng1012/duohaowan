package com.wlj.base.ob;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wlj.base.R;
import com.wlj.base.util.Log;
import com.wlj.base.util.img.ImageLrucache;
import com.wlj.base.util.img.LoadImage;

public class ObserversImageView extends ImageView implements Observer {

	public ObserversImageView(Context context) {
		super(context);
	}

	public ObserversImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * 上一次可见状态
	 */
	boolean lastVisible ;
	@Override
	public void update(Subject subject, Object o) {
		
		ObserversScrollView os = 	(ObserversScrollView)subject;
		if(os.isChildVisible(this)  ){
			if( !lastVisible){
				lastVisible = true;
				if(getTag() == null){
					//==nul的原来就是lproject_bg 不用在此加载
//					setImageBitmap(BitmapCache.getInstance().getBitmap( R.drawable.project_bg, getContext()));
					Log.w("dd","加载project_bg"+ getTag());
				}else{
					LoadImage.getinstall().addTask(getTag()+"", this);
					LoadImage.getinstall().doTask();
					Log.w("dd","加载view ");
				}
			}
		} else {
			if(lastVisible){
				lastVisible = false;
				 setImageBitmap(ImageLrucache.getInstance().getResourceBitmap( R.drawable.project_bg, getContext()));
				 Log.w("dd","view    拜拜 " +getTag() );
			}
		}
		
//		//获取在整个屏幕内的绝对坐标，注意这个值是要从屏幕顶端算起，也就是包括了通知栏的高度。
//			int[] location = new int[2];
//			getLocationOnScreen(location);
//			Rect r = new Rect();
//			Rect g = new Rect();
//			getLocalVisibleRect(r);//获取视图本身可见的坐标区域，坐标以自己的左上角为原点
//			getGlobalVisibleRect(g);//方法的作用是获取视图在屏幕坐标中的可视区域
//			if (o instanceof Integer) {
//				int   statusbarheight =   StatusBarHeight.getStatusBarHeight3(getContext());//通知栏高
//				int curViewTopForScreenTop = location[1] ;//当前view距屏幕顶部高
//				int curViewTop = getMeasuredHeight();// view的高度
//				int scrollY =	(Integer) o;//pix scroll滚动到的位置
//				
//				DisplayMetrics metric = new DisplayMetrics();  
//				Activity a = (Activity)getContext();
//				a.getWindowManager().getDefaultDisplay().getMetrics(metric);
//				
//				 int hh = curViewTopForScreenTop- statusbarheight;
//				 Log.d("dd",scrollY+" hh  ="+hh);
//				 if(hh+curViewTop > 0&& hh < metric.heightPixels){
//					 Log.w("dd","view  可见");
//				 }else{
//					 Log.w("dd","view    bujianle ");
//				 }
//				 
//		}

	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		
		super.setImageBitmap(bm);
	}
}
