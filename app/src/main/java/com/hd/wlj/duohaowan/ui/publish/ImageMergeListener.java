package com.hd.wlj.duohaowan.ui.publish;

import android.graphics.Bitmap;
import android.graphics.Rect;

public interface ImageMergeListener {

    /**
     * @param pub_id
     * @param bitmap 需要合成的图片
     * @param scale  缩放
     * @param type
     */
    void merge(String pub_id, Bitmap bitmap, double scale, MergeBitmap.MergeType type);

    void mergeCard(int space, MergeBitmap.MergeType type);

    void setOriginalRect(Rect rect);

    /**
     * 卡纸颜色填充和边框线宽
     *
     * @param color
     * @param card1
     */
    void cardFitColorAndSize(String pub_id, int color, float innerSize, float innerOutside, MergeBitmap.MergeType card1);
}
