package com.hd.wlj.duohaowan.ui.publish;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import com.hd.wlj.duohaowan.util.ImageUtil2;
import com.wlj.base.util.img.BitmapUtil;

public class MergeBitmap {


    /**
     * 间隙
     */
    private int card1space;
    /**
     * 颜色
     */
    private int card1Color;
    /**
     * 内线宽
     */
    private float lineinner;
    /**
     * 外线宽
     */
    private float lineoutside;
    /**
     * 宽
     */
    private int card2space;
    /**
     * 颜色
     */
    private int card2Color;
    /**
     * 原始框大小
     */
    private Rect borkRect_old;
    /**
     * 作品
     */
    private Bitmap workBitmap;
    /**
     * 作品位置
     */
    private Rect workRect;
    /**
     * 卡纸1位置
     */
    private Rect card1Rect;
    /**
     * 卡纸2位置
     */
    private Rect card2Rect;
    /**
     * 卡纸2位置
     */
    private Rect backgroundRect;
    /**
     * 画框
     */
    private Bitmap borderBitmap;
    /**
     * 背景
     */
    private Bitmap backgroundBitmap;
    /**
     * 最终 图片
     */
    private Bitmap endBitmap;
    private String card1Id;
    private String card2Id;
    private String BackgroundId;
    private String borderId;

    public Rect getBorkRect_old() {
        return borkRect_old;
    }

    public void setBorkRect_old(Rect borkRect_old) {
        this.borkRect_old = borkRect_old;
    }

    public Bitmap buildFinalBitmap() {

        if (borderBitmap == null) {
            return workBitmap;
        }
        if (workBitmap == null) {
            return null;
        }
        //Rect resizeRectangle = ImageUtil2.resizeRectangle(rect, scale);
        //最终的 合成图片
        Bitmap bitmap_final = borderBitmap;

        //——————————卡纸2
        if (card2space != 0) {
            if (card2Rect == null) {
                card2Rect = new Rect();
            }
            if (card1Color == 0) {
                card1Color = Color.BLACK;
            }
            bitmap_final = ImageUtil2.mergeCard2_line(bitmap_final, borkRect_old, card2Rect, card2space, card1Color);
        }
        if (card2Color != 0) {
            //卡纸2颜色
            bitmap_final = ImageUtil2.mergeCard2(bitmap_final, borkRect_old, card2space, card2Color);
        }

        //——————————卡纸1
        if (card1space != 0) {
            if (card1Rect == null) {
                card1Rect = new Rect();
            }
            if (card1Color == 0) {
                card1Color = Color.BLACK;
            }

            bitmap_final = ImageUtil2.mergeCard1(bitmap_final, card1Rect, card1space, card1Color, lineinner, lineoutside);
        }

        //-----------加作品
        bitmap_final = ImageUtil2.imageSynthesis(bitmap_final, workBitmap, workRect, true);
        //加背景
        if (backgroundBitmap != null) {
            bitmap_final = ImageUtil2.imageSynthesis(backgroundBitmap, bitmap_final, backgroundRect, false);
        }

        endBitmap = bitmap_final;
        return bitmap_final;
    }

    public Bitmap getWorkBitmap() {
        return workBitmap;
    }

    public void setWorkBitmap(Bitmap workBitmap) {
        this.workBitmap = workBitmap;
    }

    public Rect getWorkRect() {
        return workRect;
    }

    public void setWorkRect(Rect workRect) {
        this.workRect = workRect;
    }

    public Rect getCard1Rect() {
        return card1Rect;
    }

    public void setCard1Rect(Rect card1Rect) {
        this.card1Rect = card1Rect;
    }

    public Rect getCard2Rect() {
        return card2Rect;
    }

    public void setCard2Rect(Rect card2Rect) {
        this.card2Rect = card2Rect;
    }

    public Bitmap getBorderBitmap() {
        return borderBitmap;
    }

    public void setBorderBitmap(Bitmap borderBitmap) {
        this.borderBitmap = borderBitmap;
    }

    public String getCard1Id() {
        return card1Id;
    }

    public void setCard1Id(String card1Id) {
        this.card1Id = card1Id;
    }

    public String getCard2Id() {
        return card2Id;
    }

    public void setCard2Id(String card2Id) {
        this.card2Id = card2Id;
    }

    public String getBackgroundId() {
        return BackgroundId;
    }

    public void setBackgroundId(String backgroundId) {
        BackgroundId = backgroundId;
    }

    public String getBorderId() {
        return borderId;
    }

    public void setBorderId(String borderId) {
        this.borderId = borderId;
    }

    public Bitmap getEndBitmap() {
        return endBitmap;
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
        this.backgroundBitmap = backgroundBitmap;
    }

    public void setCard1space(int card1space) {
        this.card1space = card1space;
    }

    public void setCard1Color(int card1Color) {
        this.card1Color = card1Color;
    }

    public Rect getBackgroundRect() {
        return backgroundRect;
    }

    public void setBackgroundRect(Rect backgroundRect) {
        this.backgroundRect = backgroundRect;
    }

    public void setCard2space(int card2space) {
        this.card2space = card2space;
    }

    public void setCard2Color(int card2Color) {
        this.card2Color = card2Color;
    }

    public void setLineinner(float lineinner) {
        this.lineinner = lineinner;
    }

    public void setLineoutside(float lineoutside) {
        this.lineoutside = lineoutside;
    }

    /**
     * 获取作平二进制流
     *
     * @return
     */
    public String getworkBitmapByte() {

        if (workBitmap == null) return null;

        return BitmapUtil.getInstall().bitmaptoString(workBitmap);


    }

    public enum MergeType {background, border, card1, card2}
}
