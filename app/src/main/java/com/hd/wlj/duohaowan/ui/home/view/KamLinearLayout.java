package com.hd.wlj.duohaowan.ui.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 和KamHorizontalScrollView 配合使用，达到水平一屏显示多张、无限循环滚动
 */
public class KamLinearLayout extends LinearLayout {
    kamLayoutChangeListener listener = null;

    public KamLinearLayout(Context context) {
        super(context);
    }


    public KamLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addKamLayoutChangeListener(kamLayoutChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onLayout(boolean changed,  int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (this.listener != null) this.listener.onLayoutChange();
    }


    /*自定义监听器*/
    public interface kamLayoutChangeListener {
        abstract void onLayoutChange();
    }
}