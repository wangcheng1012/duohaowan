package com.wlj.base.widget;

import java.util.LinkedList;
import java.util.List;

import com.wlj.base.util.Log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
/**
 * 仿qq好友的布局 悬浮（在需要悬浮在view的tag=sticky）
 * @author wlj
 *
 */
public class StickListView extends ListView {

	private String TAG = "StickListView";
	private static final String STICKY = "sticky";
	private View mCurrentStickyView;
	private GradientDrawable mShadowDrawable;
	private List<View> mStickyViews;
	/**
	 * nextStickyView与curStickyView的距离
	 * 
	 */
	private int mStickyViewTopOffset;
	private int defaultShadowHeight = 5;// 阴影高度
	private float density;
	private boolean redirectTouchToStickyView;

	/**
	 * 当点击Sticky的时候，实现某些背景的渐变
	 */
	private Runnable mInvalidataRunnable = new Runnable() {

		@Override
		public void run() {
			if (mCurrentStickyView != null) {
				int left = mCurrentStickyView.getLeft();
				int top = 0; //mCurrentStickyView.getTop();
				int right = mCurrentStickyView.getRight();
				int bottom = (mCurrentStickyView.getHeight() + mStickyViewTopOffset);

				invalidate(left, top, right, bottom);//直接调用invalidate()方法，请求重新draw()，但只会绘制调用者本身。
				Log.w(TAG, left+" ,"+ top+" ,"+ right+" ,"+ bottom);
			}

//			postDelayed(this, 150);
		}
	};
	
	private boolean viewloadedcompled;
	

	public StickListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StickListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
//		int strokeWidth = 5; // 3dp 边框宽度
//	    int roundRadius = 15; // 8dp 圆角半径
//	    int strokeColor = Color.parseColor("#2E3135");//边框颜色
	    int fillColor = Color.parseColor("#FFFFFF");//内部填充颜色
//
	    mShadowDrawable = new GradientDrawable();//创建drawable
	    mShadowDrawable.setColor(fillColor);
//	    gd.setCornerRadius(roundRadius);
//	    gd.setStroke(strokeWidth, strokeColor);
	    
	    //渐变色设置方法
//	    int colors[] = { 22222222 , 0xAA222222, 0x00222222 };///分别为开始颜色，中间夜色，结束颜色
//		mShadowDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
		
		mStickyViews = new LinkedList<View>();
		density = context.getResources().getDisplayMetrics().density;
	}

	/**
	 * 找到设置tag的View
	 * 
	 * @param viewGroup
	 */
	private void findViewByStickyTag(ViewGroup viewGroup) {
		int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = viewGroup.getChildAt(i);

			if (getStringTagForView(child).contains(STICKY) && !mStickyViews.contains(child)) {
				
				mStickyViews.add(child);
			}	

			if (child instanceof ViewGroup) {
				findViewByStickyTag((ViewGroup) child);
			}
		}

	}
	
	/**
	 * 界面加载完后，调用的方法
	 */
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if(!viewloadedcompled){
			findViewByStickyTag(this);
		}
		viewloadedcompled = true;
	}
	
//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		onLayout方法是ViewGroup中子View的布局方法，用于放置子View的位置
//		
//		super.onLayout(changed, l, t, r, b);
//		
//		if (changed) {//可以用 getChildAt(0)是因为 ScrollView 只能有一个子布局
//			findViewByStickyTag((ViewGroup) getChildAt(0));
//			showStickyView();
//		}
//	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		
		if(viewloadedcompled){
			findViewByStickyTag(this);
		}
		
		showStickyView();
	}

	/**
	 * 
	 */
	private void showStickyView() {
		
		
		View curStickyView = null;
		View nextStickyView = null;

		for (View v : mStickyViews) {
			
			int topOffset = offsetListViewTop(v);
			
//			Rect localVisibleRect = new Rect();
//			v.getLocalVisibleRect(localVisibleRect);
//			int topOffset2  = -localVisibleRect.top;
//			Log.e(TAG, "topOffset="+topOffset+"  ,topOffset2="+topOffset2);
//			
//			Rect r = new Rect();
//			boolean b = v.getLocalVisibleRect(r);//获取视图本身可见的坐标区域，坐标以自己的左上角为原点
//			// v.localVisibleRect=true, Rect=Rect(0, 0 - 768, 70)     localVisibleRect=true, Rect=Rect(0, 0 - 768, 70)
//			
//			int[] location = new int[2];
//			v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标，注意这个值是要从屏幕顶端算起，也就是包括了通知栏的高度。
//			// getLocationOnScreen=0:130    getLocationOnScreen=0:483
//			// v.getLocationOnScreen=0:430  v. getLocationOnScreen=0:595
//			
//			Rect g = new Rect();
//			v.getGlobalVisibleRect(g);//方法的作用是获取视图在屏幕坐标中的可视区域
//			// getGlobalVisibleRect=Rect(0, 130 - 768, 1280)   getGlobalVisibleRect=Rect(0, 483 - 768, 1280)
//			// v.getGlobalVisibleRect=Rect(0, 430 - 768, 500)  v. getGlobalVisibleRect=Rect(0, 595 - 768, 665)
//			
//			Log.i(TAG, "top="+top); //top=300
//			Log.i(TAG," localVisibleRect=" + b +", Rect=" + r.toString() ); 
//			Log.i(TAG," getLocationOnScreen="+ location[0]+":"+location[1] );
//			Log.i(TAG," getGlobalVisibleRect="+g.toString());
//			Log.i(TAG," scrollY="+scrollY);  // scrollY=0
			
			if (topOffset <= 0) {
				if (curStickyView == null
						|| topOffset > offsetListViewTop(curStickyView)// 当前v近于curStickyView，此情况为重顶部出来一个v替换curStickyView
						) {
					curStickyView = v;
				}
			} else {
				if (nextStickyView == null
						|| topOffset < offsetListViewTop(nextStickyView)) {
					nextStickyView = v;
				}
			}
		}

		if (curStickyView != null) {
			mStickyViewTopOffset = nextStickyView == null ? 0 : Math.min(
					0,
					offsetListViewTop(nextStickyView)
							- curStickyView.getHeight());
			mCurrentStickyView = curStickyView;
			
			removeCallbacks(mInvalidataRunnable);
			post(mInvalidataRunnable);
		} else {
			mCurrentStickyView = null;
			removeCallbacks(mInvalidataRunnable);
		}

	}
	/**
	 * v到本listview顶部的偏移,负数表示v到了listview顶部的上方。
	 * @param v
	 * @return
	 */
	private int offsetListViewTop(View v) {
		int[] locationOnScreen_listview = new int[2];
		getLocationOnScreen(locationOnScreen_listview);//locationOnScreen_listview[1] ＝listview到屏幕顶端的距离
		
		int[] locationOnScreen_view = new int[2];
		v.getLocationOnScreen(locationOnScreen_view);//locationOnScreen_view[1] ＝view到屏幕顶端的距离
		
		int viewTopOffsetScreen = locationOnScreen_view[1];
		int listviewTopOffsetScreen = locationOnScreen_listview[1];

		int topOffset = viewTopOffsetScreen - listviewTopOffsetScreen;//view到listview 偏移
		return topOffset;
	}
	
//	private int getScrollY_(View v) {
//		int offsetListViewTop = offsetListViewTop(v);
//		return Math.abs(offsetListViewTop);
//	}
	private String getStringTagForView(View v) {
		Object tag = v.getTag();
		return String.valueOf(tag);
	}

	/**
	 * 将sticky画出来
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
//		绘制VIew本身的内容，通过调用View.onDraw(canvas)函数实现
//		绘制自己的孩子通过dispatchDraw（canvas）实现
		/**	
		 * View组件的绘制会调用draw(Canvas canvas)方法，draw过程中主要是先画Drawable背景，
		 * 对 drawable调用setBounds()然后是draw(Canvas c)方法.有点注意的是背景drawable的实际大小会影响view组件的大小，
		 * drawable的实际大小通过getIntrinsicWidth()和getIntrinsicHeight()获取，当背景比较大时view组件大小等于背景drawable的大小
		 *  画完背景后，draw过程会调用onDraw(Canvas canvas)方法，然后就是dispatchDraw(Canvas canvas)方法, 
		 *  dispatchDraw()主要是分发给子组件进行绘制，我们通常定制组件的时候重写的是onDraw()方法。值得注意的是ViewGroup容器组件的绘制，
		 *  当它没有背景时直接调用的是dispatchDraw()方法, 而绕过了draw()方法，当它有背景的时候就调用draw()方法，
		 *  而draw()方法里包含了dispatchDraw()方法的调用。因此要在ViewGroup上绘制东西的时候往往重写的是dispatchDraw()方法而不是onDraw()方法，
		 *  或者自定制一个Drawable，重写它的draw(Canvas c)和 getIntrinsicWidth(), 
		 */
		super.dispatchDraw(canvas);
		if (mCurrentStickyView != null) {
			// 先保存起来
			canvas.save();
			// 将坐标原点移动到(0, getScrollY() + mStickyViewTopOffset)
			canvas.translate(0, 0 + mStickyViewTopOffset);

			if (mShadowDrawable != null) {
				int left = 0;
				int top = mCurrentStickyView.getHeight() + mStickyViewTopOffset;
				int right = mCurrentStickyView.getWidth();
				int bottom = top + (int) (density * defaultShadowHeight + 0.5f);
				mShadowDrawable.setBounds(left, top, right, bottom);
				mShadowDrawable.draw(canvas);
			}

			canvas.clipRect(0, mStickyViewTopOffset,
					mCurrentStickyView.getWidth(),
					mCurrentStickyView.getHeight());

			mCurrentStickyView.draw(canvas);
			
			// 重置坐标原点参数
			canvas.restore();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//mCurrentStickyView点击事件分发
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			redirectTouchToStickyView = true;
		}

		if (redirectTouchToStickyView) {
			redirectTouchToStickyView = mCurrentStickyView != null;

			if (redirectTouchToStickyView) {
				redirectTouchToStickyView =  ev.getY() <= (mCurrentStickyView.getHeight() + mStickyViewTopOffset)
										  && ev.getX() >= mCurrentStickyView.getLeft()
										  && ev.getX() <= mCurrentStickyView.getRight();
			}
		}

		boolean dispatchTouchEvent ;
		if (redirectTouchToStickyView) {
			
			dispatchTouchEvent = mCurrentStickyView.dispatchTouchEvent(ev);
			
		}else{
			
			dispatchTouchEvent = super.dispatchTouchEvent(ev);
		}
		
		return dispatchTouchEvent;
	}

	private boolean hasNotDoneActionDown = true;

	@SuppressLint("Recycle")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			hasNotDoneActionDown = false;
		}

		if (hasNotDoneActionDown) {
			MotionEvent down = MotionEvent.obtain(ev);
			down.setAction(MotionEvent.ACTION_DOWN);
			super.onTouchEvent(down);
			hasNotDoneActionDown = false;
		}

		if (ev.getAction() == MotionEvent.ACTION_UP
				|| ev.getAction() == MotionEvent.ACTION_CANCEL) {
			hasNotDoneActionDown = true;
		}
		return super.onTouchEvent(ev);
	}

}
