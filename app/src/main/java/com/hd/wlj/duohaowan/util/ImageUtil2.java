package com.hd.wlj.duohaowan.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by wlj on 2016/11/11.
 */

public class ImageUtil2 {

    private static Rect RealRect;

    /**
     * 合成
     *
     * @param firstBitmap  选中的作品
     * @param secondBitmap 背景
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(secondBitmap.getWidth(), secondBitmap.getHeight(), secondBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);

        Matrix matrix = new Matrix();
        matrix.setScale(0.2f, 0.2f);

        canvas.drawBitmap(secondBitmap, new Matrix(), null);
        canvas.drawBitmap(firstBitmap, matrix, null);
//        canvas.drawBitmap(secondBitmap,new Rect(2,2,13,13),new Rect(2,2,3,3),null);

        return bitmap;
    }


    public static Rect resizeRectangle(Rect rectangle, Double bili) {

        int l = rectangle.left;

        int t = rectangle.top;

        int height = rectangle.height();

        int width = rectangle.width();

        l += width * (1 - bili) / 2;
        t += height * (1 - bili) / 2;

        height = (int) (height * bili);
        width = (int) (width * bili);

        Rect rectangle_return = new Rect(l, t, l + width, t + height);

        return rectangle_return;
    }

    /**
     * 根据根据最大宽高 保持比例缩放图片
     *
     * @param bitmap
     * @param max_width
     * @param max_height
     * @return
     */
    public static Bitmap getScaledInstanceMax(Bitmap bitmap, Integer max_width, Integer max_height) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //Max 大
        if (width < max_width && height < max_height) {

            width = width * max_height / height;
            height = max_height;

//            if(width > max_width){
//                height = height*max_width/width;
//                width = max_width;
//            }

        }

        //图大
        if (width > max_width) {
            height = max_width * height / width;
            width = max_width;
        }
        if (height > max_height) {
            width = max_height * width / height;
            height = max_height;
        }

        if (width < 1) width = 1;
        if (height < 1) height = 1;

        Bitmap scaledInstances = Bitmap.createScaledBitmap(bitmap, width, height, true);
//        bitmap.recycle();
//        Image scaledInstance = bitmap.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
        return scaledInstances;
    }

    /**
     * 图片合成
     *
     * @param backGroudImage      图片背景 原图
     * @param bufferedImage_input 合成进入的图
     * @return 合成后的 BufferedImage	被合成的图像会自动居中
     */
    public static Bitmap imageSynthesis(Bitmap backGroudImage, Bitmap bufferedImage_input, Rect findRectangle, boolean save) {

        Bitmap bitmap = Bitmap.createBitmap(backGroudImage.getWidth(), backGroudImage.getHeight(), backGroudImage.getConfig());
        Canvas canvas = new Canvas(bitmap);

        Integer width_zuopin = findRectangle.width();
        Integer height_zuopin = findRectangle.height();
        //根据最大宽高 保持比例缩放图片
        Bitmap scaledInstance = getScaledInstanceMax(bufferedImage_input, width_zuopin, height_zuopin);

        int width_zuopin_scale = scaledInstance.getWidth();
        int height_zuopin_scale = scaledInstance.getHeight();

        Integer x_zuopin = findRectangle.left;
        Integer y_zuopin = findRectangle.top;

        if (width_zuopin > width_zuopin_scale) {
            x_zuopin += (width_zuopin - width_zuopin_scale) / 2;
        }
        if (height_zuopin > height_zuopin_scale) {
            y_zuopin += (height_zuopin - height_zuopin_scale) / 2;
        }

        if (save) {
            canvas.drawBitmap(backGroudImage, new Matrix(), null);
            canvas.drawBitmap(scaledInstance, x_zuopin, y_zuopin, null);
        } else {
            //加背景 反过来
            canvas.drawBitmap(scaledInstance, x_zuopin, y_zuopin, null);
            canvas.drawBitmap(backGroudImage, new Matrix(), null);
        }

        if (save) {
            RealRect = new Rect(x_zuopin, y_zuopin, x_zuopin + width_zuopin_scale, y_zuopin + height_zuopin_scale);
        }
        return bitmap;
    }


    /**
     * @param backGroudImage 画框
     * @param card1Rect      返回card1Rect的
     * @param space          两条线的间距宽 0-10
     * @param color          颜色
     * @param lineinner      内线宽
     * @param lineoutside    外线宽
     * @return
     */
    public static Bitmap mergeCard1(Bitmap backGroudImage, Rect card1Rect, int space, int color, float lineinner, float lineoutside) {
        int w = 2;
        Bitmap bitmap = Bitmap.createBitmap(backGroudImage.getWidth(), backGroudImage.getHeight(), backGroudImage.getConfig());
        Canvas canvas = new Canvas(bitmap);

        canvas.drawBitmap(backGroudImage, new Matrix(), null);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);//空心矩形框
//        paint.setStyle(Paint.Style.FILL);//实心矩形框
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(lineinner);
        canvas.drawRect(RealRect.left - w, RealRect.top - w, RealRect.right + w, RealRect.bottom + w, paint);
        paint.setColor(color);
        w += space;
//        paint.setAlpha((int)(127 - 12.5*space  + 50 ));
        paint.setStrokeWidth(lineoutside);
        card1Rect.set(RealRect.left - w, RealRect.top - w, RealRect.right + w, RealRect.bottom + w);
        canvas.drawRect(card1Rect, paint);
        return bitmap;
    }

    /**
     * cacard2线位置
     *
     * @param backGroudImage
     * @param space          0-10
     * @return
     */
    public static Bitmap mergeCard2_line(Bitmap backGroudImage, Rect borderRect, Rect card2Rect, int space, int card1Color) {

        Bitmap bitmap = Bitmap.createBitmap(backGroudImage.getWidth(), backGroudImage.getHeight(), backGroudImage.getConfig());
        Canvas canvas = new Canvas(bitmap);

        canvas.drawBitmap(backGroudImage, new Matrix(), null);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);//空心矩形框
//        paint.setStyle(Paint.Style.FILL);//实心矩形框
        paint.setAntiAlias(true);
        paint.setColor(card1Color);
        card2Rect.set(borderRect.left + space, borderRect.top + space, borderRect.right - space, borderRect.bottom - space);
        canvas.drawRect(card2Rect, paint);
//        canvas.drawRect(borderRect, paint);

        return bitmap;
    }

    /**
     * cacard2区域填充
     *
     * @param backGroudImage
     * @param color
     * @return
     */
    public static Bitmap mergeCard2(Bitmap backGroudImage, Rect borderRect, int card2Space, int color) {

        Bitmap bitmap = Bitmap.createBitmap(backGroudImage.getWidth(), backGroudImage.getHeight(), backGroudImage.getConfig());
        Canvas canvas = new Canvas(bitmap);

        canvas.drawBitmap(backGroudImage, new Matrix(), null);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);//空心矩形框
//        paint.setStyle(Paint.Style.FILL);//实心矩形框
        paint.setAntiAlias(true);
        paint.setStrokeWidth(card2Space);
        paint.setColor(color);
        int space = card2Space / 2;
        canvas.drawRect(borderRect.left + space, borderRect.top + space, borderRect.right - space, borderRect.bottom - space, paint);

        return bitmap;
    }
}
