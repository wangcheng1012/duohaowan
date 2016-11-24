package com.hd.wlj.duohaowan.ui.publish;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.util.UploadChucks;
import com.wlj.base.bean.Base;
import com.wlj.base.util.MathUtil;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.asyn.AsyncCall;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class PublishModel extends BaseAsyncModle implements Parcelable {

    private String name;
    private String years;
    private String price;
    private boolean havebachground;

    private List<MergeBitmap> mergeBitmaps;


    public PublishModel() {
        super();
    }

    public PublishModel(Activity paramActivity) {
        super(paramActivity);
    }

    public PublishModel(JSONObject jo) {
        super(jo);
    }

    protected PublishModel(Parcel in) {
        name = in.readString();
        years = in.readString();
        price = in.readString();
        havebachground = in.readByte() != 0;
        mergeBitmaps = in.createTypedArrayList(MergeBitmap.CREATOR);
    }

    public static final Creator<PublishModel> CREATOR = new Creator<PublishModel>() {
        @Override
        public PublishModel createFromParcel(Parcel in) {
            return new PublishModel(in);
        }

        @Override
        public PublishModel[] newArray(int size) {
            return new PublishModel[size];
        }
    };

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        //卡纸
        HttpPost httpPost = new HttpPost(Urls.publish_artworks);
        httpPost.addParemeter("artworks_name", name);
        httpPost.addParemeter("years", years);
        httpPost.addParemeter("price_fen", MathUtil.parseInteger(price) * 100 + "");
        httpPost.addParemeter("true_width", "30");
        httpPost.addParemeter("true_height", "50");
        httpPost.addParemeter("pubConlumnId", "5812ef8078e0802052dd7a31");//类别编号

        JSONArray artworksCompoment = new JSONArray();
        for (MergeBitmap tmp : mergeBitmaps) {

            httpPost.addParemeter("backgroundWall_id", havebachground ? tmp.getBackgroundId() : "");//背景编号

            JSONObject jsonObject = getArtWorkJsonObject(tmp);
            if (jsonObject != null) {
                artworksCompoment.put(jsonObject);
            }
        }

        httpPost.addParemeter("artworksCompoment", artworksCompoment);

        asRequestModle.setHttpPost(httpPost);
//        asRequestModle.setShowLoading(true);
    }


    /**
     * 组装作平数据jsonobject
     *
     * @param merger
     * @return
     */
    private JSONObject getArtWorkJsonObject(MergeBitmap merger) {

        Rect workRect = merger.getWorkRect();
        Rect card1Rect = merger.getCard1Rect();
        Rect card2Rect = merger.getCard2Rect();
        //
        try {
            return new JSONObject()
                    .put("artworks_pic", merger.getWorkPath())
                    .put("kazhi_1_id", merger.getCard1Id())//卡纸的编号
                    .put("kazhi_2_id", merger.getCard2Id())//卡纸的编号
                    .put("paintingFrame_id", merger.getBorderId()) //画框编号
                    .put("artworks_position", workRect == null ? "" : new JSONObject()  //作品的位置
                            .put("x", workRect.left)
                            .put("y", workRect.top)
                            .put("width", workRect.width())
                            .put("height", workRect.height()))
                    .put("kazhi_1_pic_position", card1Rect == null ? "" : new JSONObject() //卡纸的坐标
                            .put("x", card1Rect.left)
                            .put("y", card1Rect.top)
                            .put("width", card1Rect.width())
                            .put("height", card1Rect.height()))
                    .put("kazhi_2_pic_position", card2Rect == null ? "" : new JSONObject() //卡纸的坐标
                            .put("x", card2Rect.left)
                            .put("y", card2Rect.top)
                            .put("width", card2Rect.width())
                            .put("height", card2Rect.height()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new PublishModel(jsonObject);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setHavebachground(boolean havebachground) {
        this.havebachground = havebachground;
    }

    public void setMergeBitmaps(List<MergeBitmap> mergeBitmaps) {
        this.mergeBitmaps = mergeBitmaps;
    }

    public MergeBitmap getMergeBitmap(Rect rect) {
        for (MergeBitmap bitmap : mergeBitmaps) {
            Rect id = bitmap.getId();
            if (id.equals(rect)) {
                return bitmap;
            }
        }
        return null;
    }

    public List<MergeBitmap> getMergeBitmaps() {
        return mergeBitmaps;
    }

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
        dest.writeString(name);
        dest.writeString(years);
        dest.writeString(price);
        dest.writeByte((byte) (havebachground ? 1 : 0));
        dest.writeTypedList(mergeBitmaps);
    }

}
