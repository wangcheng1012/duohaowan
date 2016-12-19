package com.hd.wlj.duohaowan.ui.publish;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.util.ImageUtil2;
import com.lling.photopicker.utils.ImageLoader;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.img.BitmapUtil;

import java.util.List;

public class MergeBitmap implements Parcelable, Cloneable {

    private Rect id;

    private String workPath;
    private String borderPath;
    private String backgroundPath;

    /**
     * 间隙
     */
    private float card1space;
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
    private float card2space;
    /**
     * 颜色
     */
    private int card2Color;
    /**
     * 原始框大小
     */
    private Rect borkRect_old;
    /**
     * 画框
     */
    private Bitmap borderBitmap;
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
     * 背景位置
     */
    private Rect backgroundRect;
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
    private String backgroundId;
    private String borderId;

    private boolean haveBackground;

    public MergeBitmap() {
    }

    public MergeBitmap Clone(MergeBitmap mergeBitmap) {

//        mergeBitmap.setBackgroundId(backgroundId);
//        mergeBitmap.setBackgroundPath(backgroundPath);

        mergeBitmap.setWorkRect(workRect);

        mergeBitmap.setBorderPath(borderPath);
        mergeBitmap.setBorderId(borderId);
        mergeBitmap.setBorkRect_old(borkRect_old);

        mergeBitmap.setCard2Color(card2Color);
        mergeBitmap.setCard2Id(card2Id);
        mergeBitmap.setCard2Rect(card2Rect);
        mergeBitmap.setCard2space(card2space);

        mergeBitmap.setCard1Color(card1Color);
        mergeBitmap.setCard1Id(card1Id);
        mergeBitmap.setCard1Rect(card1Rect);
        mergeBitmap.setCard1space(card1space);

        return mergeBitmap;
    }

    public Bitmap buildBackgroundBitmap() {
        haveBackground = true;

        Bitmap bitmap = buildFinalBitmap();
        //加背景
        if (backgroundBitmap != null && bitmap != null) {
            bitmap = ImageUtil2.imageSynthesis(backgroundBitmap, bitmap, backgroundRect, false);
        }
        return bitmap;
    }

    /**
     * 场景多图
     */
    public synchronized void buildBackgroundMore(final Context mContext, final ImageLoader.BackBitmap backBitmap) {

        buildMore(mContext, new ImageLoader.BackBitmap() {
            @Override
            public void back(Bitmap mBitmap) {
                //加背景
                if (backgroundBitmap != null && mBitmap != null) {

                    mBitmap = ImageUtil2.imageSynthesis(backgroundBitmap, mBitmap, backgroundRect, false);

                    backBitmap.back(mBitmap);
                }
            }
        });

    }

    /**
     * 合成图片
     *
     * @param mContext
     * @param backBitmap
     */
    public synchronized void buildMore(final Context mContext, final ImageLoader.BackBitmap backBitmap) {

        haveBackground = false;

        // 作平为空 没的返回
        if (workBitmap == null && StringUtils.isEmpty(workPath)) {
            return;
        }
        //边框为空 取作品
        if (borderBitmap == null && StringUtils.isEmpty(borderPath)) {

            if (!StringUtils.isEmpty(workPath)) {
                getWorkBitmap(mContext, new ImageLoader.BackBitmap() {
                    @Override
                    public void back(Bitmap mBitmap) {
                        workBitmap = mBitmap;
                        backBitmap.back(mBitmap);
                    }
                });
            }
            return;
        }
        // 画框
        if (borderBitmap != null) {

            //最终的 合成图片
            buildAll(borderBitmap, mContext, backBitmap);
        } else if (borderPath != null) {

            Glide.with(mContext).load(Urls.HOST + borderPath).asBitmap().into(new SimpleTarget<Bitmap>(500, 500) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                    buildAll(resource, mContext, backBitmap);
                }
            });
        }

    }

    /**
     * 分别 buildAll
     *
     * @param bitmap_final
     * @param mContext
     * @param backBitmap
     */
    private void buildAll(Bitmap bitmap_final, Context mContext, ImageLoader.BackBitmap backBitmap) {
        //——————————卡纸2
        bitmap_final = buildCard2(bitmap_final);

        //——————————卡纸1
        bitmap_final = buildCard1(bitmap_final);

        //-----------加作品
        buildWork(mContext, backBitmap, bitmap_final);

    }

    /**
     * 作品
     *
     * @param mContext
     * @param backBitmap
     * @param bitmap_final
     * @return
     */
    private void buildWork(Context mContext, final ImageLoader.BackBitmap backBitmap, Bitmap bitmap_final) {

        if (workBitmap != null) {
            bitmap_final = ImageUtil2.imageSynthesis(bitmap_final, workBitmap, workRect, true);
            backBitmap.back(bitmap_final);
        } else if (!StringUtils.isEmpty(workPath)) {

            final Bitmap finalBitmap_final = bitmap_final;

            getWorkBitmap(mContext, new ImageLoader.BackBitmap() {
                @Override
                public void back(Bitmap mBitmap) {

                    Bitmap bitmap = ImageUtil2.imageSynthesis(finalBitmap_final, mBitmap, workRect, true);

                    backBitmap.back(bitmap);
                }
            });
        }
    }

    /**
     * ——————————卡纸1
     *
     * @param bitmap_final
     * @return
     */
    private Bitmap buildCard1(Bitmap bitmap_final) {
        if (card1space != 0f) {
            if (card1Rect == null) {
                card1Rect = new Rect();
            }
            if (card1Color == 0) {
                card1Color = Color.BLACK;
            }

            bitmap_final = ImageUtil2.mergeCard1(bitmap_final, card1Rect, card1space, card1Color, lineinner, lineoutside);
        }
        return bitmap_final;
    }

    /**
     * //——————————卡纸2
     *
     * @param bitmap_final
     * @return
     */
    private Bitmap buildCard2(Bitmap bitmap_final) {
        if (card2space != 0f) {
            if (card2Rect == null) {
                card2Rect = new Rect();
            }
            if (card1Color == 0) {
                card1Color = Color.BLACK;
            }

            Rect card2_inner_end_rect = null;

            if (card2_inner_end_rect == null) {
                card2_inner_end_rect = workRect;
            }

            bitmap_final = ImageUtil2.mergeCard2_line(bitmap_final, borkRect_old, card2_inner_end_rect, card2Rect, card2space, card1Color);
        }
        if (card2Color != 0) {
            Rect card2_inner_end_rect = null;

            if (card2_inner_end_rect == null) {
                card2_inner_end_rect = workRect;
            }
            //卡纸2颜色
            bitmap_final = ImageUtil2.mergeCard2(bitmap_final, borkRect_old, card2_inner_end_rect, card2space, card2Color);
        }
        return bitmap_final;
    }

    public Bitmap buildFinalBitmap() {

        haveBackground = false;

        if (borderBitmap == null) {
            return workBitmap;
        }
        if (workBitmap == null) {
            return null;
        }
        //最终的 合成图片
        Bitmap bitmap_final = borderBitmap;

        //——————————卡纸2
        bitmap_final = buildCard2(bitmap_final);

        //——————————卡纸1
        bitmap_final = buildCard1(bitmap_final);

        //-----------加作品
        bitmap_final = ImageUtil2.imageSynthesis(bitmap_final, workBitmap, workRect, true);

        endBitmap = bitmap_final;
        return bitmap_final;
    }

    public Bitmap getWorkBitmap() {
        return workBitmap;
    }

    public void getWorkBitmap(Context mContext, ImageLoader.BackBitmap backBitmap) {
        ImageLoader.getInstance(mContext).getBitmap(workPath, 500, 350, backBitmap);
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
        return backgroundId;
    }

    public void setBackgroundId(String backgroundId) {
        this.backgroundId = backgroundId;
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

    public void setCard1space(float card1space) {
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

    public void setCard2space(float card2space) {
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

    public Rect getBorkRect_old() {
        return borkRect_old;
    }

    public void setBorkRect_old(Rect borkRect_old) {
        this.borkRect_old = borkRect_old;
    }

    public boolean isHaveBackground() {
        return haveBackground;
    }

    public void setHaveBackground(boolean haveBackground) {
        this.haveBackground = haveBackground;
    }

    public Rect getId() {
        return id;
    }

    public void setId(Rect id) {
        this.id = id;
    }

    public String getBorderPath() {
        return borderPath;
    }

    public void setBorderPath(String borderPath) {
        this.borderPath = borderPath;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    /**
     * 获取作平二进制流
     *
     * @param activity
     * @return
     */
    public String getworkBitmapByte(Activity activity) {

        if (workBitmap == null || workPath == null) return null;
        String s = BitmapUtil.getInstall().bitmaptoString(workBitmap);
        return s;
//
    }


    protected MergeBitmap(Parcel in) {

        id = in.readParcelable(Rect.class.getClassLoader());
        workPath = in.readString();
        borderPath = in.readString();
        backgroundPath = in.readString();
        card1space = in.readFloat();
        card1Color = in.readInt();
        lineinner = in.readFloat();
        lineoutside = in.readFloat();
        card2space = in.readFloat();
        card2Color = in.readInt();
        borkRect_old = in.readParcelable(Rect.class.getClassLoader());
//        borderBitmap = in.readParcelable(Bitmap.class.getClassLoader());
//        workBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        workRect = in.readParcelable(Rect.class.getClassLoader());
        card1Rect = in.readParcelable(Rect.class.getClassLoader());
        card2Rect = in.readParcelable(Rect.class.getClassLoader());
        backgroundRect = in.readParcelable(Rect.class.getClassLoader());
//        backgroundBitmap = in.readParcelable(Bitmap.class.getClassLoader());
//        endBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        card1Id = in.readString();
        card2Id = in.readString();
        backgroundId = in.readString();
        borderId = in.readString();
        haveBackground = in.readByte() != 0;

    }

    public static final Creator<MergeBitmap> CREATOR = new Creator<MergeBitmap>() {
        @Override
        public MergeBitmap createFromParcel(Parcel in) {
            return new MergeBitmap(in);
        }

        @Override
        public MergeBitmap[] newArray(int size) {
            return new MergeBitmap[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(id, flags);
        dest.writeString(workPath);
        dest.writeString(borderPath);
        dest.writeString(backgroundPath);
        dest.writeFloat(card1space);
        dest.writeInt(card1Color);
        dest.writeFloat(lineinner);
        dest.writeFloat(lineoutside);
        dest.writeFloat(card2space);
        dest.writeInt(card2Color);
        dest.writeParcelable(borkRect_old, flags);
//        dest.writeParcelable(borderBitmap, flags);
//        dest.writeParcelable(workBitmap, flags);
        dest.writeParcelable(workRect, flags);
        dest.writeParcelable(card1Rect, flags);
        dest.writeParcelable(card2Rect, flags);
        dest.writeParcelable(backgroundRect, flags);
//        dest.writeParcelable(backgroundBitmap, flags);
//        dest.writeParcelable(endBitmap, flags);
        dest.writeString(card1Id);
        dest.writeString(card2Id);
        dest.writeString(backgroundId);
        dest.writeString(borderId);
        dest.writeByte((byte) (haveBackground ? 1 : 0));
    }


    public enum MergeType {background, border, card1, card2}

    public void setWorkPath(String workPath) {
        this.workPath = workPath;
    }

    public String getWorkPath() {
        return workPath;
    }
}
