package com.hd.wlj.duohaowan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.hd.wlj.duohaowan.BuildConfig;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * ImageView的缩放方式为fitcenter
 * 获取点击区域，不同区域，相应不同的事件
 */
public class RectClickImageView extends ImageView {

    /**
     * drawable与view的缩放比
     */
    private float scale;
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
//                Logger.e("startx  %s starty %s eventX %s eventY %s", startx, starty, eventX, eventY);
                if (eventX - startx < 100f || eventY - starty < 100f) {

                    touch(eventX, eventY);
                }

                break;
        }
        return true;
    }

    private void touch(float eventX, float eventY) {

        if (rectList == null || mRectAreaOnClickListioner == null) return;

        if (autoHeight) {

            touchAutoHeight(eventX, eventY);
        } else {

            touchNormal(eventX, eventY);
        }

    }

    /**
     * view固定，drawable缩放时触摸的处理
     * @param eventX
     * @param eventY
     */
    private void touchNormal(float eventX, float eventY) {
        //        if (rectF == null) {
        RectF rectF = drawableRectF();
//        }
        if (rectF == null) return;

        if (rectF.contains(eventX, eventY)) {
            //rectF.left 是drawable在view上的左边，(eventX - rectF.left )把view上的点击left转换成draw的left好和drawable的Rect比较
            float drawableX = (eventX - rectF.left) / scale;
            float drawableY = (eventY - rectF.top) / scale;
            touchBack(drawableX, drawableY);
        } else {
            //在view上，没点击在 drawable上
//            Logger.i("没点击在 drawable上");
            mRectAreaOnClickListioner.onClickOutSide();
        }
    }


    /**
     * AutoHeight缩放时触摸的处理
     * @param eventX
     * @param eventY
     */
    private void touchAutoHeight(float eventX, float eventY) {
        float drawableX = eventX / scale;
        float drawableY = eventY / scale;

        touchBack(drawableX,drawableY);
    }

    /**
     *  touch事件返回前段
     * @param drawableX
     * @param drawableY
     */
    private void touchBack(float drawableX, float drawableY) {
        inface = false;
        for (Rect rect : rectList) {
//         Logger.e("scale%f, 点击位置X:y=%f :%f ,Rect %s ,转换后的点击x:y=%s:%s ", scale, eventX, eventY, rect.toString(), drawableX, drawableY);

            if (rect.contains((int) drawableX, (int) drawableY)) {
                mRectAreaOnClickListioner.onClick(rect);
                inface = true;
                break;
            }
        }

//        if (BuildConfig.DEBUG) {
//            invalidate();
//        }
        if (!inface) {
            mRectAreaOnClickListioner.onClickOutSide();
        }
    }

//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
//
//        if (BuildConfig.DEBUG && rectList != null) {
//
//            int blue = Color.BLUE;
//            Paint paint = new Paint();
//            for (Rect rect : rectList) {
//                //这画的只管了缩放 没管偏移
//                float left = (rect.left * scale);
//                float top = (rect.top * scale);
//                float right = (rect.right * scale);
//                float bottom = (rect.bottom * scale);
//
//                paint.setColor(blue);
//                canvas.drawRect(left, top, right, bottom, paint);
//
//            }
//        }
//
//    }

    /**
     * 图片的 显示区域，
     * drawable缩放后 在view上的显示范围
     * 可以优化 相同Drawable 不重新计算
     */
    private RectF drawableRectF() {

        float viewWidth = getWidth();
        float viewHeight = getHeight();
        Drawable drawable = getDrawable();
        if (drawable == null) return null;

        float drawableWidth = drawable.getIntrinsicWidth();
        float drawableHeight = drawable.getIntrinsicHeight();
        Logger.e("View的Width：Height= %s ：%s ，drawable的Width：Height= %s：%s", viewWidth, viewHeight, drawableWidth, drawableHeight);
        //缩放 比例
        scale = viewWidth / drawableWidth;
        if (viewWidth / viewHeight > drawableWidth / drawableHeight) {
            //View 比 大于  Drawable 比，则高相等，宽缩放，那么现在高的比例就是缩放比
            scale = viewHeight / drawableHeight;
        }
        //转换成了 view 上的宽高
        float drawableWidth_show = drawableWidth * scale;
        float drawableHeight_show = drawableHeight * scale;

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
    public interface RectAreaOnClickListioner {

        void onClick(Rect rect);

        void onClickOutSide();

    }

    public void setRectList(List<Rect> rectList) {
        this.rectList = rectList;
    }

    public void setRectAreaOnClickListioner(RectAreaOnClickListioner rectAreaOnClickListioner) {
        this.mRectAreaOnClickListioner = rectAreaOnClickListioner;
    }

    /**
     * 根据宽 自动设置高，setAutoHeight 控制是否启用
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable != null && autoHeight) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();
            if (dw > 0) {
                int height = width * dh / dw;
                setMeasuredDimension(width, height);
                scale =  width / (float)dw;
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

//    public void setScale(float scale) {
//        this.scale = scale;
//    }
}
