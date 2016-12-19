package com.hd.wlj.third.quicklogin.qq;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.hd.wlj.third.share.Constants;
import com.hd.wlj.third.share.qq.BaseUIListener;
import com.hd.wlj.third.share.qq.QQshare;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.Tencent;
import com.wlj.base.util.UIHelper;

import org.json.JSONObject;

/**
 */
public class QQLogin {

    private final UserInfo mInfo;
    public final BaseUIListener authListener;
    public Tencent mTencent;
    private Activity mContext;
    private UserInfoBack userInfoBack;

    public QQLogin(Activity mContext) {
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, mContext);
        this.mContext = mContext;

        authListener = new BaseUIListener(mContext) {
            @Override
            public void onComplete(Object values) {

                if (values == null) {
                    UIHelper.toastMessage(QQLogin.this.mContext, "返回为空, 授权失败");
                    return;
                }
                JSONObject localJSONObject = (JSONObject) values;
                if ((localJSONObject != null) && (localJSONObject.length() == 0)) {
                    UIHelper.toastMessage(QQLogin.this.mContext, "返回为空,授权失败");
                    return;
                }
                UIHelper.toastMessage(QQLogin.this.mContext, "登录成功");
                QQshare.initOpenidAndToken(localJSONObject, mTencent);
                getUserInfo();
            }
        };

        mInfo = new UserInfo(mContext, mTencent.getQQToken());
    }

    public void login(UserInfoBack userInfoBack) {
        this.userInfoBack = userInfoBack;

        if (mTencent.isReady()) {
            getUserInfo();
        } else {
            mTencent.login(mContext, "all",authListener);
        }
    }

    private void getUserInfo() {

        mInfo.getUserInfo(new BaseUIListener(mContext, "get_simple_userinfo") {
            @Override
            public void onComplete(Object paramObject) {
//                super.onComplete(paramObject);

                if(userInfoBack != null){
                    userInfoBack.back(paramObject);
                }
            }
        });
    }
}
