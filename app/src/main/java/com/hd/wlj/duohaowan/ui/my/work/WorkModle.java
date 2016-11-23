package com.hd.wlj.duohaowan.ui.my.work;

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
 * Created by wlj on 2016/11/16.
 */

public class WorkModle extends BaseAsyncModle{

    public final static  int type_del = 1;
    public WorkModle() {
        super();
    }

    public WorkModle(Activity paramActivity) {
        super(paramActivity);
    }

    public WorkModle(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.list_artworks);
        asRequestModle.setHttpPost(httpPost);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle, int type) throws IOException {

        HttpPost httpPost = new HttpPost(Urls.delete_artworks);
        httpPost.addParemeter("artworks_id",getId());

        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setShowLoading(true);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new WorkModle(jsonObject);
    }
}
