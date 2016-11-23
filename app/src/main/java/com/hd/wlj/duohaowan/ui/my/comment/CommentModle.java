package com.hd.wlj.duohaowan.ui.my.comment;

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

public class CommentModle extends BaseAsyncModle{

    public CommentModle() {
        super();
    }

    public CommentModle(Activity paramActivity) {
        super(paramActivity);
    }

    public CommentModle(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.comment_list);
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(true);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new CommentModle(jsonObject);
    }
}
