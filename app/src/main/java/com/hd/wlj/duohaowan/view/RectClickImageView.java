package com.hd.wlj.duohaowan.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * ImageView的缩放方式为fitcenter
 * 获取点击区域，不同区域，相应不同的事件
 */
public class RectClickImageView extends ImageView {


    private float bi;
    private List<Rect> rectList;
    private RectAreaOnClickListioner mRectAreaOnClickListioner;
    private boolean autoHeight;
    private float startx;
    private float starty;
    private boolean inface;

    public RectClickImageView(Context context) {
        this(context, null);
    }

    public RectClickImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectClickImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                  startx = event.getX();
                  starty = event.getY();

                break;
            case MotionEvent.ACTION_UP:

                float eventX = event.getX();
                float eventY = event.getY();
                Logger.e("startx  %s starty %s eventX %s eventY %s",startx,starty,eventX,eventY);
                if(eventX - startx < 100f || eventY- starty < 100f) {

                    touch(eventX, eventY);
                }

                break;
        }
        return true;
    }

    private void touch(float eventX, float eventY) {

        if(rectList == null || mRectAreaOnClickListioner == null )return;

//        if (rectF == null) {
        RectF rectF = drawableRectF();
//        }

        if(rectF == null)return;

        if (rectF.contains(eventX, eventY)) {

            //要把 view点击的x、y转换成 Drawable 的x、y
            float drawableX = (eventX - rectF.left) / bi;
            float drawableY = (eventY - rectF.top) / bi;
            inface = false;
            for (Rect rect : rectList) {
                    Logger.e("MotionEvent eventX %f eventY %f ,Rect %s  ,DrawableWidth %s DrawableHeight %s ",
                                                    eventX, eventY, rect.toString() ,drawableX,drawableY);
                if (rect.contains((int) drawableX, (int) drawableY)) {
                    mRectAreaOnClickListioner.onClick(rect);
                    inface = true;
                    break;
                }

            }

            if(!inface) {
                mRectAreaOnClickListioner.onClickOutSide();
            }
        } else {
            //没点击在 drawable上
            Logger.i("没点击在 drawable上");
            mRectAreaOnClickListioner.onClickOutSide();
        }

    }

    /**
     * drawable缩放后 在view上的显示范围
     * 可以优化 相同Drawable 不重新计算
     */
    private RectF drawableRectF() {

        float viewWidth = getWidth();
        float viewHeight = getHeight();
        Drawable drawable = getDrawable();
        if(drawable == null)return null;

        float drawableWidth = drawable.getIntrinsicWidth();
        float drawableHeight = drawable.getIntrinsicHeight();
        //缩放 比例
        bi = drawableWidth / drawableWidth;
        if (viewWidth / viewHeight > drawableWidth / drawableHeight) {
            //View 比 大于  Drawable 比，则高相等，宽缩放，那么现在高的比例就是缩放比
            bi = viewHeight / drawableHeight;
        }
        //转换成了 view 上的宽高
        float drawableWidth_show = drawableWidth * bi;
        float drawableHeight_show = drawableHeight * bi;

        //drawable缩放后 在view上的显示范围
        float drawableLeft = (viewWidth - drawableWidth_show) / 2;
        float drawableTop = (viewHeight - drawableHeight_show) / 2;
        float drawabRight = drawableLeft + drawableWidth_show;
        float drawabBottom = drawableTop + drawableHeight_show;
        RectF rectF = new RectF(drawableLeft, drawableTop, drawabRight, drawabBottom);

        return rectF;
    }


    /**
     * Rect 区域 点击回掉
     */
    public interface RectAreaOnClickListioner{

       void onClick(Rect rect);

       void onClickOutSide( );

    }

    public void setRectList(List<Rect> rectList) {
        this.rectList = rectList;
    }

    public void setRectAreaOnClickListioner(RectAreaOnClickListioner rectAreaOnClickListioner) {
        this.mRectAreaOnClickListioner = rectAreaOnClickListioner;
    }

    /**
     *
     *  根据宽 自动设置高，setAutoHeight 控制是否启用
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable != null && autoHeight) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int diw = drawable.getIntrinsicWidth();
            if (diw > 0) {
                int height = width * drawable.getIntrinsicHeight() / diw;
                setMeasuredDimension(width, height);
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setAutoHeight(boolean autoHeight) {
        this.autoHeight = autoHeight;
    }
}
