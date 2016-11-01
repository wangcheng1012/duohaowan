package com.hd.wlj.duohaowan.been;

import android.app.Activity;

import com.wlj.base.bean.Base;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by wlj on 2016/10/29.
 */

public class News extends BaseAsyncModle {


    public News() {
    }

    public News(Activity paramActivity) {
        super(paramActivity);
    }

    public News(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new News(jsonObject);
    }
}
