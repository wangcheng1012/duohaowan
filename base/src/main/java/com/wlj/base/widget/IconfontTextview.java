package com.wlj.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wlj.base.R;
import com.wlj.base.util.StringUtils;

/**
 *  IconfontTextview
 */

public class IconfontTextview extends TextView {


    public IconfontTextview(Context context) {
        this(context,null);
    }

    public IconfontTextview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IconfontTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr,0);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IconfontTextview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr,defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconfontTextview, defStyleAttr, defStyleRes);

        String iconPath = a.getString(R.styleable.IconfontTextview_assetspath);
//        Logger.d(iconPath+"");
        initIconfont(iconPath);

        a.recycle();
    }

    private void initIconfont(String path) {

        if(StringUtils.isEmpty(path) || !path.endsWith(".ttf")){
            path = "fonts/iconfont.ttf";
        }

        Typeface t = Typeface.createFromAsset(getContext().getAssets(), path);

        setTypeface(t);
    }

    public void setIconPath(String iconPath) {
        initIconfont(iconPath);
    }
}
