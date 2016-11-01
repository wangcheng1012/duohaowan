package com.wlj.base.web.asyn;

import com.wlj.base.util.AppContext;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.Md5Util;

public class Encrpt
{
  public static final Integer client_type_json = Integer.valueOf(2);
  public static final Integer client_type_xml = Integer.valueOf(1);

  public static void encrypt(AsyncRequestModle paramAsyncRequestModle, HttpPost paramHttpPost)
  {

    String str1 = AppContext.getAppContext().getProperty("key");
    String str2 = AppContext.getAppContext().getProperty("name");
    String str3 = AppContext.getAppContext().getProperty("type");
    String str4 = System.currentTimeMillis() + "";
    String str5 = Md5Util.MD5Normal(str1 + str4);
    String str6 = paramHttpPost.getJSONObjectParemeter().toString();
    String str7 = Md5Util.MD5Normal(str5 + str2 + str6);
    paramHttpPost.addParemeter("name", str2);
    paramHttpPost.addParemeter("randCode", str4);
    paramHttpPost.addParemeter("encrpt", "enAes");
    paramHttpPost.addParemeter("isEncryption", Boolean.FALSE.toString());
    paramHttpPost.addParemeter("mode", client_type_json + "");
    paramHttpPost.addParemeter("type", str3);
    paramHttpPost.addParemeter("data", str6);
    paramHttpPost.addParemeter("mac", str7);
  }
}
