package com.hd.wlj.duohaowan.ui.publish.border;

import android.app.Activity;

import com.hd.wlj.duohaowan.Urls;
import com.wlj.base.bean.Base;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 边框
 */
public class BorderModel extends BaseAsyncModle {

    private final static String Border_Classify = "58293eadef722c116fe22066";
    public final static String Border_bili = "58214abdd6c45965757937e5";
    private final static String baakground = "58214a7dd6c45965757937d9";
    public final static int type_border_bili = 22;
    public final static int type_sence = 21;
    public final static int count_more = 2;
    public final static int count_single = 1;

    private String paintingFrameConlumn_id;
    private String pubConlumnId;
    private String width;
    private String height;
    /**
     * 单图 多图
     */
    private int  count;

    public BorderModel() {
        super();
    }

    public BorderModel(Activity paramActivity) {
        super(paramActivity);
    }

    public BorderModel(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        //边框类别
        HttpPost httpPost = new HttpPost(Urls.list_pub);
        httpPost.addParemeter("pubConlumnId", Border_Classify);
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(false);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle, int type) throws IOException {
        if (type == type_border_bili) {
            //边框 有哪些比例的
            HttpPost httpPost = new HttpPost(Urls.list_pub);
            httpPost.addParemeter("pubConlumnId", Border_bili);
            httpPost.addParemeter("width", width);
            httpPost.addParemeter("height", height);
            httpPost.addParemeter("paintingFrameConlumn_id", paintingFrameConlumn_id);
            asRequestModle.setHttpPost(httpPost);
            asRequestModle.setJiami(false);
            asRequestModle.setShowLoading(true);
        }else if(type_sence == type){

            //场景
            HttpPost httpPost = new HttpPost(Urls.list_pub);
            httpPost.addParemeter("rootPubConlumnId", baakground);
            httpPost.addParemeter("width", width);
            httpPost.addParemeter("height", height);
            httpPost.addParemeter("count", count);
            asRequestModle.setHttpPost(httpPost);
            asRequestModle.setJiami(false);

        }

    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new BorderModel(jsonObject);
    }

    public void setPaintingFrameConlumn_id(String paintingFrameConlumn_id) {
        this.paintingFrameConlumn_id = paintingFrameConlumn_id;
    }

    public void setPubConlumnId(String pubConlumnId) {
        this.pubConlumnId = pubConlumnId;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
