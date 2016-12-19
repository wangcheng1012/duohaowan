package com.hd.wlj.duohaowan;

import com.wlj.base.web.BaseURL;

/**
 *
 */
public class Urls extends BaseURL {

    public static final String getImageRand = HOST+ "rand/getImageRand.do";
    public static final String getPhoneRand = HOST+ "rand/getPhoneRand.do";
    public static final String getUserPhoneRand = HOST+ "rand/getUserPhoneRand.do";
    public static final String register = HOST+ "front/register.do";
    public static final String register_third_user = HOST+ "front/register_third_user.do";
    public static final String login = HOST+ "front/login_simple.do";
    public static final String resetPassword = HOST+ "front/resetPassword.do";

    public static final String list_pub = HOST + "front/list_pub.do";//
    public static final String get_pub = HOST + "front/get_pub.do";//
    public static final String nice_pub = HOST + "face/user/nice_pub.do";//点赞
    public static final String publish_artworks = HOST + "face/user/publish_artworks.do";//发布作品
    public static final String list_artworks = HOST + "face/user/list_artworks.do";//作品列表

    public static final String getUserInfo = HOST + "face/user/getUserInfo.do";//查询当前登录用户的信息
    public static final String updateSelf = HOST + "face/user/updateSelf.do";// 更新用户的信息
    public static final String comment_list = HOST + "face/user/comment_list.do";// 更新用户的信息
    public static final String leaveMessage = HOST + "face/user/leaveMessage.do";//  提交意见反馈
    public static final String uploadChucks = HOST + "front/uploadChucks.do";//  提交意见反馈
    public static final String shoucang_pub = HOST + "face/user/shoucang_pub.do";//  收藏作品
    public static final String list_shoucang_pub = HOST + "face/user/list_shoucang_pub.do";//  提交意见反馈
    public static final String comment_pub = HOST + "face/user/comment_pub.do";//  提交意见反馈
    public static final String updateArtistCard = HOST + "face/user/updateArtistCard.do";//  提交意见反馈
    public static final String delete_artworks = HOST + "face/user/delete_artworks.do";//  提交意见反馈
    public static final String createAskInstructor = HOST + "face/user/createAskInstructor.do";//  提交意见反馈
    public static final String list_artist = HOST + "front/list_artist.do";//  提交意见反馈
    public static final String shoucang_artistCard = HOST + "front/list_artist.do";//  收藏艺术家（卡片）

    public static final String save_order = HOST + "face/user/save_order.do";//  购买画框_创建支付订单.js

}
