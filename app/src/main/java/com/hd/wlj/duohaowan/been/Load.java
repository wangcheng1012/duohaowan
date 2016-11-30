package com.hd.wlj.duohaowan.been;

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
 */
public class Load extends BaseAsyncModle {

    public Load() {
        super();
    }

    public Load(Activity paramActivity) {
        super(paramActivity);
    }

    public Load(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {

        HttpPost httpPost = new HttpPost(Urls.list_pub);
        httpPost.addParemeter("pubConlumnId","5837f4a9d6c459629f57d307");
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(false);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new Load(jsonObject);
    }
}
