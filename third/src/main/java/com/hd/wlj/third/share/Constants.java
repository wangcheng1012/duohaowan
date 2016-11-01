package com.hd.wlj.third.share;

import com.wlj.base.web.BaseURL;

public class Constants
{

  public static String Code = "code";
  public static final String QQ_APP_ID = "1104829131";
  public static final String SS_APP_KEY = "2523262267";
  public static final String SS_REDIRECT_URL = "http://www.sina.com";
  public static final String SS_SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";
  public static final String WX_APP_ID = "wx3876877998bb22e6";
  public static final String WX_AppSecret = "edbea80d435d3f79fddacb1ba948225a";
  public static String getAccess_tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
          "appid="+ WX_APP_ID +
          "&secret="+WX_AppSecret +
          "&grant_type=authorization_code";


  public static String Host = BaseURL.HOST;
  public static String shoucang;
  public static String shoucang_remove;
  public static String fenxiangtarget;
  public static String qq_targetUrl = "";

}