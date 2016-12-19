package com.hd.wlj.duohaowan.ui.home.classify.artview.ask;

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
public class AskedModle extends BaseAsyncModle{

    public final static int TEACHER =  232;
    private String teacherid;
    private String title;
    private String context;

    public AskedModle() {
        super();
    }

    public AskedModle(Activity paramActivity) {
        super(paramActivity);
    }

    public AskedModle(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.createAskInstructor);
        httpPost.setJiami(asRequestModle.isJiami());

        httpPost.addParemeter("artist_instructor_id",teacherid);
        httpPost.addParemeter("name",title);
        httpPost.addParemeter("intro",context);
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setShowLoading(true);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle, int type) throws IOException {

        HttpPost httpPost = new HttpPost(Urls.list_artist);
        httpPost.addParemeter("userGroup_id","583c39bcef722c0aeeeff7c0");
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(false);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new AskedModle(jsonObject);
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContext(String context) {
        this.context = context;
    }


}
