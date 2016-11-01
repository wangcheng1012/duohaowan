package com.hd.wlj.third.quicklogin.wx;

import android.content.Context;

import com.hd.wlj.third.share.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by wlj on 2016/10/26.
 */

public class WXLogin {


    private final IWXAPI api;

    public WXLogin(Context context) {
        api = WXAPIFactory.createWXAPI(context, Constants.WX_APP_ID);
    }

    public void  login(){
        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "login";
        api.sendReq(req);

    }
}
