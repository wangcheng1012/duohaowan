package com.wlj.base.widget;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.wlj.base.R;
import com.wlj.base.adapter.ImagePagerAdapter;
import com.wlj.base.bean.Banner;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.ListUtils;
import com.wlj.base.util.img.LoadImage;

/**
 * @author wlj
 */
public class SwitchViewPager<T> implements
        OnPageChangeListener {

    private AutoScrollViewPager autoScrollViewPager;
    private ImageView[] mImageViews;
    private int mViewCount;
    private int mCurSel;
    private Context mContext;
    private List<T> list;
    /**
     * 圆点的图片资源路径（这里必须是Enabled的selector）
     */
    private int pointResId = R.drawable.loading_point_selector;
    /**
     * 圆点的容器
     */
    private LinearLayout pointlinearLayout;

    /**
     * 圆点的容器的 LayoutParams
     */
    private RelativeLayout.LayoutParams params;

    private LayoutParams scrollLayoutParams;
    private final int TUANGOU = 1;

    /**
     * @param list 图片url的list
     */
    public SwitchViewPager(Context mContext, List<T> list) {


        this.mContext = mContext;
        this.list = list;
        this.mViewCount = ListUtils.getSize(list);

        scrollLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, DpAndPx.dpToPx(mContext, 200));
//        scrollLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, DpAndPx.dpToPx(mContext, 2));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);// 必须加这个，否则setMargins的24没用

    }

    public View createview() {

        RelativeLayout layout = new RelativeLayout(mContext);
        if (list.size() <= 0) {
            layout.setLayoutParams(scrollLayoutParams);
            return layout;
        }

        autoScrollViewPager = new AutoScrollViewPager(mContext);

        autoScrollViewPager.setInterval(3000);// 设置自动滚动的间隔时间，单位为毫秒
        autoScrollViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);// 滑动到第一个或最后一个Item的处理方式，支持没有任何操作、轮播以及传递到父View三种模式
        autoScrollViewPager.setAutoScrollDurationFactor(4);//设置ViewPager滑动动画间隔时间的倍率，达到减慢动画或改变动画速度的效果
//		autoScrollViewPager.setDirection(AutoScrollViewPager.RIGHT); // 设置自动滚动的方向，默认向右
//		viewPager.setCycle(true);// 是否自动循环轮播，默认为true
//		viewPager.setStopScrollWhenTouch(true);// 当手指碰到ViewPager时是否停止自动滚动，默认为true
//		viewPager.setBorderAnimation(true);// 设置循环滚动时滑动到从边缘滚动到下一个是否需要动画，默认为true

        addSwitchPage(autoScrollViewPager);

        layout.addView(autoScrollViewPager, scrollLayoutParams);
        scrollPoint(layout);
        layout.setLayoutParams(scrollLayoutParams);
        //Integer.MAX_VALUE 的中间数 且 为mViewCount倍数的位置
        autoScrollViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mViewCount);
        return layout;
    }

    /**
     * 可以继承重写Point，也可以用layout，往布局里面添加其他布局
     *
     * @param layout 包裹ScrollLayout和pointlinearLayout的最外层layout
     */
    protected void scrollPoint(RelativeLayout layout) {

        pointlinearLayout = new LinearLayout(mContext);
        pointlinearLayout.setLayoutParams(params);
        pointlinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        pointlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//		pointlinearLayout.setBackgroundColor(Color.parseColor("magenta"));

        mImageViews = new ImageView[mViewCount];
        if (mViewCount <= 0) {
            return;
        }
        ImageView imageView;
        for (int i = 0; i < mViewCount; i++) {
            imageView = new ImageView(mContext);
            imageView.setImageResource(pointResId);
            int px10 = DpAndPx.dpToPx(mContext, 5);
            imageView.setPadding(px10, px10, px10, px10);
            mImageViews[i] = imageView;
            mImageViews[i].setEnabled(true);// 这里必须设置true，否则点击不起
            mImageViews[i].setTag(i);
            pointlinearLayout.addView(imageView);
        }
        mCurSel = 0;
        mImageViews[mCurSel].setEnabled(false);
        autoScrollViewPager.setOnPageChangeListener(this);

        layout.addView(pointlinearLayout);
    }

    private void setCurPoint(int index) {
        if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
            return;
        }
        mImageViews[mCurSel].setEnabled(true);
        mImageViews[index].setEnabled(false);
        mCurSel = index;
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

    public AutoScrollViewPager getAutoScrollViewPager() {
        return autoScrollViewPager;
    }

//	public void setAutoScrollViewPager(AutoScrollViewPager autoScrollViewPager) {
//		this.autoScrollViewPager = autoScrollViewPager;
//	}

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

    @Override
    public void onPageSelected(int position) {
        setCurPoint(position % mViewCount);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    /**
     * 给滑视图添加页面
     *
     * @param autoviewPager
     */
    protected void addSwitchPage(AutoScrollViewPager autoviewPager) {
        autoviewPager.setAdapter(new ImagePagerAdapter<T>(list) {
            @Override
            public View getPageItemview(T item, View view, ViewGroup container) {
                if (view == null) {
                    view = new ImageView(mContext);
                }
                final ImageView imageView1 = (ImageView) view;

//                LoadImage loadImage = LoadImage.getinstall();
                imageView1.setAdjustViewBounds(true);
                imageView1.setScaleType(ScaleType.FIT_XY);

                Glide.with(mContext).
                        load(getContentText(item))
                        .placeholder(R.drawable.project_bg)
                        .crossFade(1000)
                        .into(imageView1);

//                loadImage.addTask(getContentText(item), imageView1);
//                loadImage.doTask();
                imageView1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (onItemPagerViewClickListener != null){
                            onItemPagerViewClickListener.ItemClickListener(imageView1);
                    }
                    }
                });

                return imageView1;
            }
        }.setInfiniteLoop(true));
        autoviewPager.startAutoScroll();
    };

    private String getContentText(T item) {

        if(item instanceof Banner){
            return  ((Banner) item).getPicPath();
        }
        return item.toString();
    }

    private PageritemClickListener onItemPagerViewClickListener;

    public void setOnItemPagerViewClickListener(
            PageritemClickListener onItemPagerViewClickListener) {
        this.onItemPagerViewClickListener = onItemPagerViewClickListener;
    }

    public interface PageritemClickListener {

        void ItemClickListener(ImageView imageView1);

    }

}