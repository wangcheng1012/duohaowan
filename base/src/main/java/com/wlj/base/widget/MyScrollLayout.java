package com.wlj.base.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import com.wlj.base.util.Log;

import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * @ 来源：http://www.open-open.com/lib/view/open1326374891952.html
 * 
 * @1.默认为滑动模式，要用自动播放调startAutotPay。两种模式的动态切换还有bug
 * @2.当为手动滑动时，可设置是否可以滑到上一张（setPrescroll）
 * @3.当为自动播放时，lunliupay可设置重复播放，播放间隔时间waittime
 * 
 * @author wlj
 * 
 */
public class MyScrollLayout extends ViewGroup {

	private static final String TAG = "ScrollLayout";
	private VelocityTracker mVelocityTracker; // 用于判断甩动手势
	private static final int SNAP_VELOCITY = 600;
	private Scroller mScroller; // 滑动控制器
	private int mCurScreen;
	private int mDefaultScreen = 0;
	private float mLastMotionX;
	/**
	 * 是否可以回滚
	 */
	private boolean prescroll = true;

	// ---------------------autoplay--------------------------
	/**
	 * 自动切换间隔时间
	 */
	private long waittime = 5000;

	/**
	 * 自动切换
	 */
	private boolean autoplay = false;

	Object lock = new Object();
	/*
	 * 轮流播放
	 */
//	private boolean lunliupay = true;
	// ---------------------autoplay--end------------------------

	ScrollView sv01;
	
	private OnViewChangeListener mOnViewChangeListener;

	public MyScrollLayout(Context context,ScrollView sv01) {
		super(context);
		init(context);
		this.sv01 = sv01;
	}

	public MyScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mCurScreen = mDefaultScreen;
		mScroller = new Scroller(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			// 把childView 从左往右布局
			int childLeft = 0;
			final int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft + childWidth,
							childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}

	/**
	 * onMeasure:测量
	 * 
	 * 1. widthMeasureSpec和heightMeasureSpec这两个值是android:layout_width="200dp"
	 * android:layout_height="80dp"来定义的， 它由两部分构成，可通过int specModeHeight =
	 * MeasureSpec.getMode(heightMeasureSpec); int specSizeHeight =
	 * MeasureSpec.getSize(heightMeasureSpec)来得到各自的值。
	 * 如果android:layout_width="wrap_content"
	 * 或android:layout_width="fill_parent"，哪么得到的specMode为MeasureSpec
	 * .AT_MOST，(如果为精确的值则为MeasureSpec.EXACTLY)?
	 * 另外，specSize要想得到合适的值需要在Androidmanifest.xml中添加<uses-sdk
	 * android:minSdkVersion="10" />
	 * 2.系统默认的onMeasure调用方法是getDefaultSize来实现，有时候在自定义控件的时候多数采用
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		
		//不要下面的两个if，则为FILL_PARENT或者match_parent时 view显示不出来
		if (widthMode == MeasureSpec.AT_MOST) {

			widthMeasureSpec = MeasureSpec.getSize(widthMeasureSpec);
		}
		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {

			heightMeasureSpec = MeasureSpec.getSize(heightMeasureSpec);
		}
		
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {

			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurScreen * width, 0);
	}

	/**
	 * 速度为0的处理
	 */
	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	public void snapToScreen(int whichScreen) {
		// get the valid layout page
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * getWidth())) {
			final int delta = whichScreen * getWidth() - getScrollX();
			mScroller.startScroll(getScrollX(), 0, delta, 0,Math.abs(delta) * 2);
			mCurScreen = whichScreen;
			invalidate(); // Redraw the layout
			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.OnViewChange(mCurScreen);
			}
		}
	}

	private void snapToFirst() {
		// scrolled to the first
		int delta = (getChildCount() - 1) * getWidth();
		mScroller.startScroll(getScrollX(), 0, -delta, 0, Math.abs(delta));
		mCurScreen = 0;
		invalidate(); // Redraw the layout
		if (mOnViewChangeListener != null) {
			mOnViewChangeListener.OnViewChange(0);
		}
	}
	
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	float mFirstMotionX;
	float mFirstMotionY;
    long mFirstTime;
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		final int action = event.getAction();
		final float x = event.getX();
//		final float y = event.getY();
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			stopAutoPay();
			Log.i("", "onTouchEvent  ACTION_DOWN");
			// VelocityTracker:用于对触摸点的速度跟踪，方便获取触摸点的速度。
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			}
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			getParent().requestDisallowInterceptTouchEvent(false);
			
			mFirstMotionX = event.getX();
			mFirstMotionY = event.getY();
			mFirstTime = 0L;
			break;
		case MotionEvent.ACTION_MOVE:
			if(mFirstTime == 0L && sv01 != null ){
				mFirstTime =  System.currentTimeMillis();
			}
			if(sv01 != null && System.currentTimeMillis() - mFirstTime < 100){
				float absX = Math.abs(event.getX()-mFirstMotionX);
				float absY = Math.abs(event.getY()-mFirstMotionY);
				if(absY < absX ){
					getParent().requestDisallowInterceptTouchEvent(true);
				}else{
					getParent().requestDisallowInterceptTouchEvent(false);
				}
//				Log.d("dd", absX +" : "+absY);
			}else{
//				Log.e(TAG, "ACTION_MOVE");
				int deltaX = (int) (mLastMotionX - x);
				if (IsCanMove(deltaX)) {
					if (mVelocityTracker != null) {
						mVelocityTracker.addMovement(event);
					}
					mLastMotionX = x;
					scrollBy(deltaX, 0);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			startAutotPay();
			// x方向的速度
			int velocityX = 0;
			if (mVelocityTracker != null) {
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				velocityX = (int) mVelocityTracker.getXVelocity();
			}
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0 && prescroll) {
				Log.e(TAG, "snap left");
				snapToScreen(mCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1) {
				Log.e(TAG, "snap right");
				snapToScreen(mCurScreen + 1);
			} else {
				// 速度为0的处理
				snapToDestination();
			}
			Log.e(TAG, "滑动速度： "+velocityX+", SNAP_VELOCITY: "+SNAP_VELOCITY);
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			
			if(sv01 != null){
				sv01.requestDisallowInterceptTouchEvent(false);
			}
			break;
		}
		return true;
	}

	private boolean IsCanMove(int deltaX) {
		if (getScrollX() <= 0 && deltaX < 0) {
			return false;
		}
		if (getScrollX() >= (getChildCount() - 1) * getWidth() && deltaX > 0) {
			return false;
		}
		if (deltaX < 0 && !prescroll) {
			return false;
		}

		return true;
	}

	// ------------------------autopay---start--------------------------------------
	private final int SCROLL_WHAT = 1;
	
	private void sendScrollMessage(long delayTimeInMills) {
	        /** remove messages before, keeps one message is running at most **/
	        handler.removeMessages(SCROLL_WHAT);
	        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
	    }
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case SCROLL_WHAT:
				snapToScreen(mCurScreen + 1);
				sendScrollMessage(waittime);
				break;
			}
		}

	};

	public void startAutotPay() {
		autoplay = true;
		sendScrollMessage(waittime);
	}

	public void stopAutoPay() {
		autoplay = false;
		handler.removeMessages(SCROLL_WHAT);
	}

//	public boolean isLunliupay() {
//		return lunliupay;
//	}
//
//	public void setLunliupay(boolean lunliupay) {
//		this.lunliupay = lunliupay;
//	}

	public long getWaittime() {
		return waittime;
	}

	public void setWaittime(long waittime) {
		this.waittime = waittime;
	}

	public boolean getAutoplay() {
		return autoplay;
	}

	public void setAutoplay(boolean isautoplay) {

		this.autoplay = isautoplay;
	}

	// ---------------------------------autopay--end--------------------------------

	public void SetOnViewChangeListener(OnViewChangeListener listener) {
		mOnViewChangeListener = listener;
	}

	/**
	 * 滑动界面时的其他同步操作
	 * 
	 * @author wlj
	 * 
	 */
	public interface OnViewChangeListener {
		public void OnViewChange(int pos);
	}

	public boolean getPrescroll() {
		return prescroll;
	}

	public void setPrescroll(boolean prescroll) {
		this.prescroll = prescroll;
	}

	
}