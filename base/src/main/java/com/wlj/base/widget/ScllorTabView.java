package com.wlj.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;


public class ScllorTabView extends View {

    private int mTabNum, mCurrentNum;
    private float mWidth, mTabWidth, mOffset;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mBeginColor;
    private int mEndColor;
    LinearGradient gradient;

    public ScllorTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTabNum(int n) {
        mTabNum = n;
    }

    public void setCurrentNum(int n) {
        mCurrentNum = n;
        mOffset = 0;
    }

    public void setOffset(int position, float offset) {
        if (offset == 0) {
            return;
        }
        mCurrentNum = position;
        mOffset = offset;
        invalidate();
    }
    
    public void setmTabWidth(float mTabWidth) {
		this.mTabWidth = mTabWidth;
	}

	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTabWidth == 0) {
            mWidth = getWidth();
            mTabWidth = mWidth / mTabNum;
        }
        //根据位置和偏移量来计算滑动条的位置
        float left = (mCurrentNum + mOffset) * mTabWidth;
        final float right = left + mTabWidth;
        final float top = getPaddingTop();
        final float bottom = getHeight() - getPaddingBottom();

        // if (gradient == null) {
        LinearGradient gradient = new LinearGradient(left, getHeight(), right,
                getHeight(), mBeginColor, mEndColor, TileMode.CLAMP);
        mPaint.setShader(gradient);
        // }
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    public void setSelectedColor(int color, int color2) {
        mBeginColor = color;
        mEndColor = color2;

    }
}

