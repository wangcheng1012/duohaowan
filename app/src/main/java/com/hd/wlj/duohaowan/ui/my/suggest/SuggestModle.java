package com.hd.wlj.duohaowan.ui.my.suggest;

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

public class SuggestModle extends BaseAsyncModle{

    private String problem;
    private String content;

    public SuggestModle() {
        super();
    }

    public SuggestModle(Activity paramActivity) {
        super(paramActivity);
    }

    public SuggestModle(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.leaveMessage);
        httpPost.addParemeter("title",problem);
        httpPost.addParemeter("message",content);
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setShowLoading(true);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new SuggestModle(jsonObject);
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

