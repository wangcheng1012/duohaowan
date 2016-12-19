package com.wlj.base.web.asyn;

import com.wlj.base.util.AppContext;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.Md5Util;

import java.io.UnsupportedEncodingException;

public class Encrpt {
    public static final Integer client_type_json = Integer.valueOf(2);
    public static final Integer client_type_xml = Integer.valueOf(1);

    public static void encrypt(AsyncRequestModle paramAsyncRequestModle, HttpPost httpPost) {

        httpPost.setJiami(false);
        String key = AppContext.getAppContext().getProperty("key");
        String name = AppContext.getAppContext().getProperty("name");
//        String type = AppContext.getAppContext().getProperty("type");

        String data = httpPost.getJSONObjectParemeter().toString();

        String randCode = System.currentTimeMillis() + "";

        String tempKey = Md5Util.MD5Normal(key + randCode);
        String mac = null;
        try {
            mac = Md5Util.MD5Normal(tempKey + HexM.StringToHex(name) + data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpPost.addParemeter("name", name);
        httpPost.addParemeter("randCode", randCode);
        httpPost.addParemeter("encrpt", "enAes");
        httpPost.addParemeter("sign_only", Boolean.TRUE.toString());
        httpPost.addParemeter("mode", client_type_json + "");
//        httpPost.addParemeter("type", type);
        httpPost.addParemeter("data", data);
        httpPost.addParemeter("mac", mac);
    }

}
