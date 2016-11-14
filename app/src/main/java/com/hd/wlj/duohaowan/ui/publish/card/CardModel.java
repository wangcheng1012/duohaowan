package com.hd.wlj.duohaowan.ui.publish.card;

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
 * 边框
 */
public class CardModel extends BaseAsyncModle {

    public CardModel() {
        super();
    }

    public CardModel(Activity paramActivity) {
        super(paramActivity);
    }

    public CardModel(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        //卡纸
        HttpPost httpPost = new HttpPost(Urls.list_pub);
        httpPost.addParemeter("rootPubConlumnId", "58253865ef722c174838108a");
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(false);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new CardModel(jsonObject);
    }
}
