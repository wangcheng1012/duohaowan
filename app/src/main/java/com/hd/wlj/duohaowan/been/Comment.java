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
 * Created by wlj on 2016/11/21.
 */

public class Comment extends BaseAsyncModle {

    /**
     * neirong
     */
    private String  content;

    public Comment() {
        super();
    }

    public Comment(Activity paramActivity) {
        super(paramActivity);
    }

    public Comment(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.comment_pub);
        httpPost.addParemeter("pub_id",getId());
        httpPost.addParemeter("content",getContent());

        asRequestModle.setHttpPost(httpPost);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new Comment(jsonObject);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
