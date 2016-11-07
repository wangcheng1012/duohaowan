package com.hd.wlj.duohaowan.been;

import android.app.Activity;

import com.hd.wlj.duohaowan.Urls;
import com.wlj.base.bean.Base;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.asyn.AsyncCall;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 *  点赞
 */
public class Zan extends BaseAsyncModle {


    private zanView zview;

    public Zan() {
    }

    public Zan(Activity paramActivity) {
        super(paramActivity);
    }

    public Zan(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.nice_pub);
        httpPost.addParemeter("pub_id", getId());
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(true);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new Zan(jsonObject);
    }

    /**
     * @param pubid
     */
    public Zan nice(String pubid) {
        setId(pubid);
        Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int type) {
                if (zview != null) {
                    zview.niceSuccess(paramList, paramBase, type);
                }
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
        return this;
    }

    public void setZview(zanView zview) {
        this.zview = zview;
    }

    public interface zanView {
        void niceSuccess(List<Base> paramList, Base paramBase, int type);
    }
}
