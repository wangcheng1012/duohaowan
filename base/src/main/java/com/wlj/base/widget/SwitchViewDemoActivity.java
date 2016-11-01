package com.wlj.base.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.wlj.base.R;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.Log;
import com.wlj.base.widget.MyScrollLayout.OnViewChangeListener;

/**
 * 滑动切换图片和点。
 * 1.可以通过addSwitchPage添加切换layout，scrollPoint修改Point和添加layout
 * 2.可以设置Point的图片selector资源等
 * 
 * @author wlj
 * @deprecated 用SwitchViewPagerDemoActivity代替，这个的图片管理没做好
 */
public abstract class SwitchViewDemoActivity implements OnViewChangeListener, OnClickListener{
 
	protected MyScrollLayout mScrollLayout;   
    private ImageView[] mImageViews;    
    private int mViewCount; 
    private int mCurSel;
    private Context mContext;
    /**
     * 圆点的图片资源路径（这里必须是Enabled的selector）
     */
    private int pointResId = R.drawable.loading_point_selector ;
    /**
     * 圆点的容器
     */
    private LinearLayout pointlinearLayout;
    
    /**
     * 圆点的容器的 LayoutParams
     */
    private RelativeLayout.LayoutParams params;
    
    private LayoutParams scrollLayoutParams;
    
//    private ScrollView sv01;
    
    private SwitchViewDemoActivity(Context mContext){
    	this.mContext = mContext;
    	scrollLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    	
    	params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
      	params.setMargins(0, 0, 0, DpAndPx.dpToPx(mContext, 24));
      	params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//必须加这个，否则setMargins的24没用
    	
    }
    /**
     * 有外层滚动视图
     * @param mContext
     * @param sv01
     */
    public SwitchViewDemoActivity(Context mContext,ScrollView sv01){
    	this(mContext);
    	mScrollLayout = new MyScrollLayout(mContext,sv01);
    }
    float mLastMotionX;
    float mLastMotionY;
    long mLastTime;
    public View createview() {
    	RelativeLayout layout = new RelativeLayout(mContext);
    	pointlinearLayout =  new LinearLayout(mContext);
    	addSwitchPage(mScrollLayout);
    	
    	layout.addView(mScrollLayout, scrollLayoutParams);
    	scrollPoint(layout);
    	layout.setLayoutParams(scrollLayoutParams);
    	
		return layout;
	}

    /**
     * 可以继承重写Point，也可以用layout，往布局里面添加其他布局
     * @param layout 包裹ScrollLayout和pointlinearLayout的最外层layout
     */
    protected void scrollPoint(RelativeLayout layout){
    	
    	pointlinearLayout.setLayoutParams(params);
    	pointlinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
    	pointlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
    	
        mViewCount = mScrollLayout.getChildCount();
        mImageViews = new ImageView[mViewCount];
        if(mViewCount <= 0 ){
        	return;
        }
        ImageView imageView;
        for(int i = 0; i < mViewCount; i++){
        	imageView = new ImageView(mContext);
        	imageView.setImageResource(pointResId);
        	int px15 = DpAndPx.dpToPx(mContext, 20);
        	int px5 = DpAndPx.dpToPx(mContext, 5);
        	imageView.setPadding(px5, px5, px5, px15);
        	imageView.setOnClickListener(this);
        	mImageViews[i] = imageView;
            mImageViews[i].setEnabled(true);//这里必须设置true，否则点击不起
            mImageViews[i].setOnClickListener(this);
            mImageViews[i].setTag(i);
            pointlinearLayout.addView(imageView);
        }       
        mCurSel = 0;
        mImageViews[mCurSel].setEnabled(false);     
        mScrollLayout.SetOnViewChangeListener(this);
        
        layout.addView(pointlinearLayout);
    }
 
    private void setCurPoint(int index)
    {
        if (index < 0 || index > mViewCount - 1 || mCurSel == index)      {
            return ;
        }       
        mImageViews[mCurSel].setEnabled(true);
        mImageViews[index].setEnabled(false);       
        mCurSel = index;
    }
 
    @Override
    public void OnViewChange(int pos) {
        // TODO Auto-generated method stub
        setCurPoint(pos);
    }
 
    @Override
    public void onClick(View v) {
        int pos = (Integer)(v.getTag());
        mScrollLayout.snapToScreen(pos);
    }
    
	public int getPointResId() {
		return pointResId;
	}

	public void setPointResId(int pointResId) {
		this.pointResId = pointResId;
	}

	public ImageView[] getmImageViews() {
		return mImageViews;
	}

	public void setmImageViews(ImageView[] mImageViews) {
		this.mImageViews = mImageViews;
	}

	public LinearLayout getPointlinearLayout() {
		return pointlinearLayout;
	}

	public void setPointlinearLayout(LinearLayout pointlinearLayout) {
		this.pointlinearLayout = pointlinearLayout;
	}

	public RelativeLayout.LayoutParams getParams() {
		return params;
	}

	public void setParams(RelativeLayout.LayoutParams params) {
		this.params = params;
	}

	public LayoutParams getScrollLayoutParams() {
		return scrollLayoutParams;
	}

	public void setScrollLayoutParams(LayoutParams scrollLayoutParams) {
		this.scrollLayoutParams = scrollLayoutParams;
	}

	/**
     * 给滑视图添加页面
     * @param mScrollLayout
     */
    protected abstract  void addSwitchPage(MyScrollLayout mScrollLayout);
    
    
    
}