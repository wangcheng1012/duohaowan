package com.hd.wlj.duohaowan.ui.home.classify.arist;

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
 * 艺术家 （作品）详情modle
 */
public class AristDetailsModle extends BaseAsyncModle {

    private String artist_id;

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
        HttpPost httpPost = new HttpPost(Urls.list_pub);
        httpPost.addParemeter("secondPubConlumnId","5812ef5478e0802052dd7a2f");
        httpPost.addParemeter("artist_id",getArtist_id());

        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(false);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new AristDetailsModle(jsonObject);
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }
}
