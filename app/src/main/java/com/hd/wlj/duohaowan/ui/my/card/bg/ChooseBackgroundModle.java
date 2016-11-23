package com.hd.wlj.duohaowan.ui.my.card.bg;

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
 * 背景 模型
 */
public class ChooseBackgroundModle extends BaseAsyncModle {

    public ChooseBackgroundModle() {
        super();
    }

    public ChooseBackgroundModle(Activity paramActivity) {
        super(paramActivity);
    }

    public ChooseBackgroundModle(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.list_pub);
        httpPost.addParemeter("rootPubConlumnId","58214a01d6c45965757937d3");
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(false);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new ChooseBackgroundModle(jsonObject);
    }
}
