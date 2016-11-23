package com.hd.wlj.duohaowan.been;

import android.app.Activity;
import android.graphics.Bitmap;

import com.hd.wlj.duohaowan.Urls;
import com.wlj.base.bean.Base;
import com.wlj.base.util.img.BitmapUtil;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.Md5Util;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 *
 */
public class User extends BaseAsyncModle {


    /**
     * 获取验证码
     */
    public static final int getRand = -1;

    /**
     * 注册
     */
    public static final int REGESTER = 1;
    /**
     * 登陆
     */
    public static final int LOGIN = 2;
    /**
     * 重置密码
     */
    public static final int RESSTPASSWORD = 3;

    /**
     * 获取用户验证码
     */
    public static final int getUserPhoneRand = 4;

    /**
     * 获取用户详情
     */
    public static final int getUserInfo = 5;

    private String phone;
    /**
     * 短信验证码
     */
    private String verify;

    private String psw;

    private String nickname;    //昵称
    /**
     * 名片上的头像
     */
    private String cardPic;//		头像(先将图片读入二进制数组/然后再base64)
    /**
     * 名片上的背景
     */
    private String cardbg;
    /**
     * 简介
     */
    private String intro;
    /**
     * 头衔
     */
    private String  touxian;

    //    private String realname	;//真实姓名
    //    private String  phone;//	手机号码
//    private String  addr;//		联系地址
//    private String  sheng;//	省
//    private String  shi	;//	市
//    private String  birthDate;// 生日
//    private String  idCard;//	证件号码


    public User() {
        super();
    }

    public User(Activity paramActivity) {
        super(paramActivity);
    }

    public User(JSONObject paramJSONObject) {
        super(paramJSONObject);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle paramAsyncRequestModle) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.getPhoneRand);
        httpPost.addParemeter("phone", phone);

        paramAsyncRequestModle.setHttpPost(httpPost);
        paramAsyncRequestModle.setJiami(false);
        paramAsyncRequestModle.setShowLoading(true);

    }

    @Override
    public void addRequestParemeter(AsyncRequestModle paramAsyncRequestModle, int type) throws IOException {
        if (type == REGESTER) {

            HttpPost httpPost = new HttpPost(Urls.register);
            httpPost.addParemeter("username", phone);
            httpPost.addParemeter("randCode", verify);
            httpPost.addParemeter("userpwd", psw);
            httpPost.addParemeter("userpwd_ok", psw);

            paramAsyncRequestModle.setHttpPost(httpPost);
            paramAsyncRequestModle.setJiami(false);
            paramAsyncRequestModle.setShowLoading(true);
        } else if (type == LOGIN) {

            String md5Final = Md5Util.MD5Normal(psw);
            String randCode = System.currentTimeMillis() + "";
            String userpwd = Md5Util.MD5Normal(md5Final + randCode.toLowerCase());

            HttpPost httpPost = new HttpPost(Urls.login);
            httpPost.addParemeter("username", phone);
            httpPost.addParemeter("randCode", randCode);
            httpPost.addParemeter("userpwd", userpwd);

            paramAsyncRequestModle.setHttpPost(httpPost);
            paramAsyncRequestModle.setJiami(false);
            paramAsyncRequestModle.setShowLoading(true);

        } else if (type == RESSTPASSWORD) {
            HttpPost httpPost = new HttpPost(Urls.resetPassword);
            httpPost.addParemeter("username", phone);
            httpPost.addParemeter("randCode", verify);
            httpPost.addParemeter("userpwd", psw);

            paramAsyncRequestModle.setHttpPost(httpPost);
            paramAsyncRequestModle.setJiami(false);
            paramAsyncRequestModle.setShowLoading(true);

        } else if (type == getUserPhoneRand) {
            HttpPost httpPost = new HttpPost(Urls.getUserPhoneRand);
            httpPost.addParemeter("username", phone);

            paramAsyncRequestModle.setHttpPost(httpPost);
            paramAsyncRequestModle.setJiami(false);
            paramAsyncRequestModle.setShowLoading(true);
        } else if (type == getUserInfo) {

            HttpPost httpPost = new HttpPost(Urls.getUserInfo);

            paramAsyncRequestModle.setHttpPost(httpPost);
            paramAsyncRequestModle.setShowLoading(true);
        }


    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new User(jsonObject);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCardPic() {
        return cardPic;
    }

    public void setCardPic(String cardPic) {
        this.cardPic = cardPic;
    }

    public String getCardbg() {
        return cardbg;
    }

    public void setCardbg(String cardbg) {
        this.cardbg = cardbg;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getTouxian() {
        return touxian;
    }

    public void setTouxian(String touxian) {
        this.touxian = touxian;
    }

    public void setCardPicBitmap(Bitmap cardPicBitmap) {
        if(cardPicBitmap != null){
            String bitmaptoString = BitmapUtil.getInstall().bitmaptoString(cardPicBitmap);
            setCardPic(bitmaptoString);
        }

    }
}
