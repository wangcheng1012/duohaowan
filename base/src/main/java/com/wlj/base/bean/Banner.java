package com.wlj.base.bean;

import android.app.Activity;

import com.wlj.base.web.BaseURL;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 *
 */
public class Banner extends BaseAsyncModle {

    // SwitchViewPager 获取图片路径
    private String picPath;

    public Banner() {
    }

    public Banner(Activity paramActivity) {
        super(paramActivity);
    }

    public Banner(JSONObject jo) {
        super(jo);
        setPicPath(BaseURL.HOST+jo.optString("pic"));
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new Banner(jsonObject);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {

    }
}
