package com.hd.wlj.third.share.sina;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.hd.wlj.third.share.Constants;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.wlj.base.util.UIHelper;

public abstract class SinaShare {
    private String dateText;
    private Bitmap image;
    private Activity mActivity;
    private ImageView mImageView;
    private IWeiboShareAPI mWeiboShareAPI;

    public SinaShare(Activity paramActivity, Bundle savedInstanceState) {
        this.mActivity = paramActivity;
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mActivity.getApplicationContext(), Constants.SS_APP_KEY);

        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();

        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(mActivity.getIntent(), new myResponse(mActivity));
        }

        onnewintent(mWeiboShareAPI);
    }

    //	   @Override
//	    protected void onNewIntent(Intent intent) {
//	        super.onNewIntent(intent);
//
//	        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
//	        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
//	        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
//	        mWeiboShareAPI.handleWeiboResponse(intent, this);
//	    }
    private ImageObject getImageObj() {
        ImageObject localImageObject = new ImageObject();
        localImageObject.setImageObject(this.image);
        return localImageObject;
    }

    private TextObject getTextObj() {
        this.dateText = this.dateText.replaceAll("<[^>]*>", "");
        TextObject localTextObject = new TextObject();
        localTextObject.text = this.dateText.substring(0, Math.min(140, this.dateText.length()));
        return localTextObject;
    }

    public abstract void onnewintent(IWeiboShareAPI paramIWeiboShareAPI);

    public void sendMultiMessage() {
        WeiboMultiMessage localWeiboMultiMessage = new WeiboMultiMessage();
        localWeiboMultiMessage.textObject = getTextObj();

        if (this.image != null)
            localWeiboMultiMessage.imageObject = getImageObj();

        SendMultiMessageToWeiboRequest localSendMultiMessageToWeiboRequest = new SendMultiMessageToWeiboRequest();
        localSendMultiMessageToWeiboRequest.transaction = String.valueOf(System.currentTimeMillis());
        localSendMultiMessageToWeiboRequest.multiMessage = localWeiboMultiMessage;
        AuthInfo localAuthInfo = new AuthInfo(this.mActivity, Constants.SS_APP_KEY, Constants.SS_REDIRECT_URL, Constants.SS_SCOPE);
        Oauth2AccessToken localOauth2AccessToken = AccessTokenKeeper.readAccessToken(this.mActivity);
        String str = "";
        if (localOauth2AccessToken != null)
            str = localOauth2AccessToken.getToken();
        this.mWeiboShareAPI.sendRequest(this.mActivity, localSendMultiMessageToWeiboRequest, localAuthInfo, str, new WeiboAuthListener() {
            public void onCancel() {

                UIHelper.toastMessage(mActivity, "取消Auth");
            }

            public void onComplete(Bundle paramAnonymousBundle) {
                Oauth2AccessToken localOauth2AccessToken = Oauth2AccessToken.parseAccessToken(paramAnonymousBundle);
                AccessTokenKeeper.writeAccessToken(mActivity, localOauth2AccessToken);
                UIHelper.toastMessage(mActivity, "成功");
            }

            public void onWeiboException(WeiboException paramAnonymousWeiboException) {

                UIHelper.toastMessage(mActivity, "Auth异常" + paramAnonymousWeiboException.getLocalizedMessage());
            }
        });
    }

    public void setDateText(String paramString) {

        this.dateText = paramString;
    }

    public void setmBitmap(Bitmap paramBitmap) {

        this.image = paramBitmap;
    }
}