package com.wlj.base.ob;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class ObserversScrollView extends ScrollView implements Subject {

	public ObserversScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		observers = new ArrayList<Observer>();
	}

	private ArrayList<Observer> observers;

	@Override
	public void registerObserver(Observer arg0) {
		if (arg0 == null) {
			throw new NullPointerException("observer == null");
		}
		synchronized (this) {
			if (!observers.contains(arg0))
				observers.add(arg0);
		}
	}

	@Override
	public synchronized void removeObserver(Observer arg0) {
		if (observers.contains(arg0)) {
			observers.remove(arg0);
		}
	}

	@Override
	public void notifyObservers(Object o) {
		int size = 0;
		Observer[] arrays = null;
		synchronized (this) {
			size = observers.size();
			arrays = new Observer[size];
			observers.toArray(arrays);
		}
		if (arrays != null) {
			for (Observer observer : arrays) {
				observer.update(this, o);
			}
		}
	}
	@Override
	public synchronized void deleteObservers() {
		observers.clear();
	}
	
	/**
	 * 界面加载完后，调用的方法
	 */
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		init(this);
	}
	/**
	 *  界面加载完毕
	 * @param sv01
	 */
	private void init(ViewGroup sv01 ){
		int childCount = sv01.getChildCount();

		for (int i = 0; i < childCount; i++) {
			View view = sv01.getChildAt(i);

			if(view instanceof ObserversImageView ){
				registerObserver((ObserversImageView)view);
			}
			if(view instanceof  ViewGroup){
				 init((ViewGroup)view );
			}
			
		}
		touch();
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
	}	
	
	public boolean isChildVisible(View child){
		  if(child==null){
		      return false;
		  }
		  Rect scrollBounds = new Rect();
		  getHitRect(scrollBounds);//getHitRect 获取控件所在的矩阵范围函数
		  return child.getLocalVisibleRect(scrollBounds);
	}
	int currentScroll;

	/**
	 * 设置本身的touch事件
	 */
	private void touch() {
		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					currentScroll = getScrollY();
					postDelayed(scrollCheckTask, 300);
				}
				return false;
			}
		});
	}

	Runnable scrollCheckTask = new Runnable() {
		@Override
		public void run() {
			int newScroll = getScrollY();
			if (currentScroll == newScroll) {
				notifyObservers(null);
				// if(onWaterfallScrollListener !=null){
				// if(isAtTop()){
				// //TODO: onScrollStoppedAtTop;
				// }
				// if(isScrollViewAtBottom(sv)){
				// //TODO: onScrollStoppedAtBottom;
				// }
				// }
			} else {
				currentScroll = getScrollY();
				postDelayed(scrollCheckTask, 300);
				// }
			}
		}
	};
}