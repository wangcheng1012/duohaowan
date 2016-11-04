package com.hd.wlj.duohaowan.ui.home.classify.detailswork.model;

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
 * Created by wlj on 2016/10/30
 */

public class DetailsModelImpl extends BaseAsyncModle {


    public DetailsModelImpl() {
    }

    public DetailsModelImpl(Activity paramActivity) {
        super(paramActivity);
    }

    public DetailsModelImpl(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.get_pub);
        httpPost.addParemeter("pub_id", getId());
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(false);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new DetailsModelImpl(jsonObject);
    }
}