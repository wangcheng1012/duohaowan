package com.hd.wlj.duohaowan.ui.home.classify.work;

import android.app.Activity;

import com.hd.wlj.duohaowan.Urls;
import com.wlj.base.bean.Base;
import com.wlj.base.util.AppConfig;
import com.wlj.base.util.AppContext;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.Md5Util;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by wlj on 2016/10/30
 */

public class DetailsModelImpl extends BaseAsyncModle {


    public DetailsModelImpl() {
    }

    public DetailsModelImpl(Activity paramActivity) {
        super(paramActivity);
    }

    public DetailsModelImpl(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {

        String login_key = AppConfig.getAppConfig().get(AppConfig.CONF_KEY);
        String randCode = System.currentTimeMillis() + "";
        String user_sign = Md5Util.MD5Normal(login_key + randCode.toLowerCase());

        HttpPost httpPost = new HttpPost(Urls.get_pub);
        httpPost.addParemeter("pub_id", getId());
        if (AppContext.getAppContext().islogin()) {
            httpPost.addParemeter("login_key", login_key);
            httpPost.addParemeter("randCode", login_key);
            httpPost.addParemeter("user_sign", user_sign);
        }
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(false);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new DetailsModelImpl(jsonObject);
    }
}