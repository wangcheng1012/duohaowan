package com.hd.wlj.duohaowan.ui.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.orhanobut.logger.Logger;
import com.wlj.base.util.MathUtil;

import java.math.BigDecimal;


/*如果不需要支持Android2.3，可以将代码中所有KamLinearLayout替换为ViewGroup*/
public class KamHorizontalScrollView extends HorizontalScrollView {

    private CreatItem mCreatItem;
    private Context context;
    /*记录当前的页数标识（做日视图的时候可以和该值今日的日期作差）*/
    private int PageNo = 0;
    /*保存ScrollView中的ViewGroup，如果不需要支持Android2.3，可以将KamLinearLayout替换为ViewGroup*/
    private KamLinearLayout childGroup = null;
    /*这是判断左右滑动用的（个人喜好，其实不需要这么麻烦）*/
    private int poscache[] = new int[4];

    private float startpos;
    private KamLinearLayout.kamLayoutChangeListener listener;
    private int childWidth = 1;
    private float yushu = 0;


    public KamHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public KamHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public KamHorizontalScrollView(Context context) {
        super(context);
        this.context = context;
    }

    /*重写触摸事件，判断左右滑动*/
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startpos = ev.getX();
                /*用于判断触摸滑动的速度*/
//                initSpeedChange((int) ev.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                /*更新触摸速度信息*/
//                movingSpeedChange((int) ev.getX());

            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                /*先根据速度来判断向左或向右*/
//                int speed = releaseSpeedChange((int) ev.getX());

                float movex =  ev.getX() - startpos;

                if(childWidth  == 1) {
                    if (childGroup.getChildCount() > 0) {

                        View childAt = childGroup.getChildAt(0);
                        childWidth = childAt.getWidth();
                    }
                }
//                Logger.e("movex:" + movex + "  yushu:" + yushu);
                movex += yushu;
                yushu = movex % childWidth;
                BigDecimal bigDecimal = MathUtil.divideDwon(movex,childWidth);
                int i = Math.abs(bigDecimal.intValue());
//                Logger.e("movex:" + movex + "   yushu:" + yushu+ " 个数： "+i  +"  "+  bigDecimal);
                if (ev.getX() - startpos < 0) {

                    for (int j = 0; j < i; j++) {

                        nextPage();
                    }
                    return true;
                }else if (ev.getX() - startpos > 0) {
                    for (int j = 0; j < i; j++) {

                        prevPage();
                    }
                    return true;
                }
            }
        }
        return super.onTouchEvent(ev);
    }

    /*下面的方法仅仅是个人喜好加上的，用于判断用户手指左右滑动的速度。*/
    private void initSpeedChange(int x) {
        if (poscache.length <= 1) return;
        poscache[0] = 1;
        for (int i = 1; i < poscache.length; i++) {

        }
    }

    /*完成实例化*/
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        Logger.i( "onFinishInflate Called!");
        init();
    }

    /*初始化，加入三个子View*/
    private void init() {
        if (this.childGroup == null) {
            View childAt = getChildAt(0);
            if (childAt instanceof KamLinearLayout) {
                childGroup = (KamLinearLayout) childAt;
            } else {
                throw new RuntimeException("childGroup为空");
            }
        }

        /*添加LayoutChange监听器*/
        childGroup.addKamLayoutChangeListener(listener);

        /*调用其自身的LayoutChange监听器（不支持Android2.3）*/
        /*childGroup.addOnLayoutChangeListener(listener);*/

     /*添加监听器*/
        listener = new KamLinearLayout.kamLayoutChangeListener() {
            @Override
            public void onLayoutChange() {
                Logger.i( "onLayoutChanged Called!");
                scrollToPage(1);
            }

        };
    }


    /*
      //注意，如果不需要支持Android2.3，可以将上面的listener替换成下方listener
      OnLayoutChangeListener listener = new OnLayoutChangeListener() {
    @Override
    public void onLayoutChange(View arg0, int arg1, int arg2, int arg3,
                               int arg4, int arg5, int arg6, int arg7, int arg8) {
        Logger.i(tag, "onLayoutChanged Called!");
        scrollToPage(1);
    }
    }
    */

    /*左翻页*/
    public void prevPage() {
        Logger.d("prevPage");
        PageNo--;
        addLeft(getView(PageNo ));
        removeRight();
    }

    /*右翻页*/
    public void nextPage() {
        Logger.d("nextPage");
        PageNo++;
        addRight(getView(PageNo ));
        removeLeft();
    }

    private View getView(int index) {

        int childCount = childGroup.getChildCount()/2;
        int i = (index % childCount + childCount) % childCount;
        return mCreatItem.getView(i);
    }

    /*获取某个孩子的X坐标*/
    private int getChildLeft(int index) {
        if (index >= 0 && childGroup != null) {
            if (index < childGroup.getChildCount())
                return childGroup.getChildAt(index).getLeft();
        }
        return 0;
    }

    /**
     * 向右边添加View
     *
     * @param view 需要添加的View
     * @return true添加成功|false添加失败
     */

    public boolean addRight(View view) {

        if (view == null || childGroup == null) return false;
        childGroup.addView(view);
        return true;
    }

    /**
     * 删除右边的View
     *
     * @return true成功|false失败
     */
    public boolean removeRight() {
        if (childGroup == null || childGroup.getChildCount() <= 0) return false;
        childGroup.removeViewAt(childGroup.getChildCount() - 1);
        return true;
    }

    /**
     * 向左边添加View
     * @param view 需要添加的View1
     * @return true添加成功|false添加失败
     */
    public boolean addLeft(View view) {
        if (view == null || childGroup == null) return false;
        childGroup.addView(view, 0);
        /*因为在左边增加了View，因此所有View的x坐标都会增加，因此需要让ScrollView也跟着移动，才能从屏幕看来保持平滑。*/
        int tmpwidth = view.getLayoutParams().width;
        if (tmpwidth == 0) tmpwidth = getWinWidth();
        Logger.i( "the new view's width = " + view.getLayoutParams().width);
        this.scrollTo(this.getScrollX() + tmpwidth, 0);
        return true;
    }

    /**
     * 删除左边的View
     * @return true成功|false失败
     */
    public boolean removeLeft() {
        if (childGroup == null || childGroup.getChildCount() <= 0) return false;
        /*因为在左边删除了View，因此所有View的x坐标都会减少，因此需要让ScrollView也跟着移动。*/
        int tmpwidth = childGroup.getChildAt(0).getWidth();
        childGroup.removeViewAt(0);
        this.scrollTo((int) (this.getScrollX() - tmpwidth), 0);
        return true;
    }

    /**
     * 跳转到指定的页面
     *
     * @param index 跳转的页码
     * @return
     */
    public boolean scrollToPage(int index) {
        if (childGroup == null) return false;
        if (index < 0 || index >= childGroup.getChildCount()) return false;
        smoothScrollTo(getChildLeft(index), 0);
        return true;
    }

    private int getWinWidth() {
        // 获取屏幕信息
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    private int getWinHeight() {
        // 获取屏幕信息
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

//    private void movingSpeedChange(int x) {
//        poscache[0] %= poscache.length - 1;
//        poscache[0]++;
//        //Logger.i(tag, "touch speed:"+(x-poscache[poscache[0]]));
//        poscache[poscache[0]] = x;
//    }
//
//    private int releaseSpeedChange(int x) {
//        return releaseSpeedChange(x, 30);
//    }
//
//    private int releaseSpeedChange(int x, int limit) {
//        poscache[0] %= poscache.length - 1;
//        poscache[0]++;
//        /*检测到向左的速度很大*/
//        if (poscache[poscache[0]] - x > limit) return 1;
//        /*检测到向右的速度很大*/
//        if (x - poscache[poscache[0]] > limit) return -1;
//
//        return 0;
//    }

    public void setCreatItem(CreatItem mCreatItem) {
        this.mCreatItem = mCreatItem;
    }

    public interface CreatItem {

        View getView(int index);
    }

}
