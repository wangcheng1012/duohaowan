package com.hd.wlj.third.share;

import com.wlj.base.web.BaseURL;

public class Constants {

    public static String Code = "code";
    public static final String QQ_APP_ID = "1105778991";
    public static final String QQ_APP_key = "JxzW1u2cTLv1WoPP";
    public static final String SS_APP_KEY = "4064438015";
    public static final String SS_APP_Secret = "ca10d74848fdcbdcdc9b6034d2ebf221";
    public static final String SS_SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";
    public static final String WX_APP_ID = "wx42eafde4835f9b84";
    public static final String WX_AppSecret = "a995731fbbef9629583b4055ba64a9c9";
    public static String getAccess_tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
            "appid=" + WX_APP_ID +
            "&secret=" + WX_AppSecret +
            "&grant_type=authorization_code";


    public static String shoucang;
    public static String shoucang_remove;

    public static String fenxiangtarget = BaseURL.HOST + "share_product.jsp?id=";
    public static String qq_targetUrl = BaseURL.HOST + "share_product.jsp?id=";
    public static String SS_REDIRECT_URL = qq_targetUrl;

}