package com.hd.wlj.duohaowan.ui.publish.border;

import android.app.Activity;
import android.media.Image;

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

    public final static String Border = "58214a83d6c45965757937da";
    public final static String baakground = "58214a7dd6c45965757937d9";

    private String rootPubConlumnId;

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
        //边框
        HttpPost httpPost = new HttpPost(Urls.list_pub);
        httpPost.addParemeter("rootPubConlumnId", rootPubConlumnId);
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(false);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new BorderModel(jsonObject);
    }

    public void setRootPubConlumnId(String rootPubConlumnId) {
        this.rootPubConlumnId = rootPubConlumnId;
    }
}
