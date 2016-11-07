package com.hd.wlj.duohaowan.ui.home.classify.arist;

import android.app.Activity;

import com.wlj.base.bean.Base;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 艺术家 详情modle
 */
public class AristDetailsModle extends BaseAsyncModle {

    public AristDetailsModle() {
    }

    public AristDetailsModle(Activity paramActivity) {
        super(paramActivity);
    }

    public AristDetailsModle(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {

    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new AristDetailsModle(jsonObject);
    }
}
