package com.wlj.base.web.asyn;

import com.wlj.base.util.AppContext;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.Md5Util;

import java.io.UnsupportedEncodingException;

public class Encrpt {
    public static final Integer client_type_json = Integer.valueOf(2);
    public static final Integer client_type_xml = Integer.valueOf(1);

    public static void encrypt(AsyncRequestModle paramAsyncRequestModle, HttpPost paramHttpPost) {

        String key = AppContext.getAppContext().getProperty("key");
        String name = AppContext.getAppContext().getProperty("name");
//        String type = AppContext.getAppContext().getProperty("type");

        String data = paramHttpPost.getJSONObjectParemeter().toString();

        String randCode = System.currentTimeMillis() + "";

        String tempKey = Md5Util.MD5Normal(key + randCode);
        String mac = null;
        try {
            mac = Md5Util.MD5Normal(tempKey + HexM.StringToHex(name) + data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        paramHttpPost.addParemeter("name", name);
        paramHttpPost.addParemeter("randCode", randCode);
        paramHttpPost.addParemeter("encrpt", "enAes");
        paramHttpPost.addParemeter("sign_only", Boolean.TRUE.toString());
        paramHttpPost.addParemeter("mode", client_type_json + "");
//        paramHttpPost.addParemeter("type", type);
        paramHttpPost.addParemeter("data", data);
        paramHttpPost.addParemeter("mac", mac);
    }

}
