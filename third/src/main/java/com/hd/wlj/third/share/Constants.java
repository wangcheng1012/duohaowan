package com.hd.wlj.third.share;

import com.wlj.base.web.BaseURL;

public class Constants {

    public static String Code = "code";
    public static final String QQ_APP_ID = "1105863702";
    public static final String QQ_APP_key = "BzHsDozx6p2q56YO";
    public static final String SS_APP_KEY = "4064438015";
    public static final String SS_APP_Secret = "ca10d74848fdcbdcdc9b6034d2ebf221";
    public static final String SS_SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";
    public static final String WX_APP_KEY = "660d3d65f1d4387ba2c3420138a2915c";
    public static final String WX_APP_ID = "wx622e032827e49132";
    public static final String WX_AppSecret = "b6ca2da48009579981d28e43d8dc1bcb";
    public static String WX_PAYID = "1420424402";
//    public static String getAccess_tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
//            "appid=" + WX_APP_ID +
//            "&secret=" + WX_AppSecret +
//            "&grant_type=authorization_code";

    public static String shoucang;
    public static String shoucang_remove;

    public static String fenxiangtarget = BaseURL.HOST + "share_product.jsp?id=";
    public static String qq_targetUrl = fenxiangtarget;
    public static String SS_REDIRECT_URL = fenxiangtarget;

}