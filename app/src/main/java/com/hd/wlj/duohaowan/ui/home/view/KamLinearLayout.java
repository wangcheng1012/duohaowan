package com.hd.wlj.duohaowan.ui.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class KamLinearLayout extends LinearLayout {
    kamLayoutChangeListener listener = null;

    public void addKamLayoutChangeListener(kamLayoutChangeListener listener) {
        this.listener = listener;
    }


    public KamLinearLayout(Context context) {
        super(context);
    }

    public KamLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
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