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

public class ShouCang extends BaseAsyncModle {

    public final static int type_list_shoucang_arist = 2;

    /**
     * 备注
     */
    private String  memo;

    public ShouCang() {
        super();
    }

    public ShouCang(Activity paramActivity) {
        super(paramActivity);
    }

    public ShouCang(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.shoucang_pub);
        httpPost.addParemeter("pub_id",getId());
        httpPost.addParemeter("memo",getMemo());

        asRequestModle.setHttpPost(httpPost);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle, int type) throws IOException {
       if(type == type_list_shoucang_arist) {
           HttpPost post = new HttpPost(Urls.list_shoucang_pub);
           post.addParemeter("secondPubConlumnId", getId());
           asRequestModle.setHttpPost(post);
       }
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new ShouCang(jsonObject);
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
