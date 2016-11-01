package com.hd.wlj.third.share.qq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.hd.wlj.third.R;
import com.hd.wlj.third.share.Constants;
import com.hd.wlj.third.share.ShareModle;
import com.tencent.open.t.Weibo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wlj.base.util.UIHelper;
import com.wlj.base.util.img.ImageFileCache;
import com.wlj.base.web.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QQshare implements IUiListener
{
  public static final int QZoneShare = 1;
  public static final int QqShare = 0;
  public static Tencent mTencent;
  public static final int weibo = 2;
  private String content = "";
  public IUiListener loginListener;
  private Activity mActivity;
  private ShareModle mShareModle;
  private Weibo mWeibo = null;
  private int shareType = 1;
  private int tempType = -1;

  public QQshare(Activity mActivity, ShareModle mShareModle)
  {
    this.mShareModle = mShareModle;
    this.mActivity = mActivity;

    // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
    // 其中APP_ID是分配给第三方应用的appid，类型为String。
    mTencent = Tencent.createInstance(Constants.QQ_APP_ID, mActivity);
    // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取

    loginListener = new BaseUiListener() {
      @Override
      public void doComplete(JSONObject values) {
        initOpenidAndToken(values);
        onClickAddPicUrlTweet();
      }
    };

    mWeibo = new Weibo(mActivity, mTencent.getQQToken());
  }

  private void QZoneShare()
  {

    Bundle localBundle = new Bundle();
    localBundle.putInt("req_type", 1);
    localBundle.putString("title", this.mShareModle.getName());
    localBundle.putString("summary", this.content);
    localBundle.putString("targetUrl", Constants.qq_targetUrl + this.mShareModle.getId());
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new ImageFileCache().getPath(this.mShareModle.getPic()));
    localBundle.putStringArrayList("imageUrl", localArrayList);
    mTencent.shareToQzone(this.mActivity, localBundle, this);
  }

  public static void initOpenidAndToken(JSONObject paramJSONObject)
  {
        String str1 = paramJSONObject.optString("access_token");
        String str2 = paramJSONObject.optString("expires_in");
        String str3 = paramJSONObject.optString("openid");
        if ((!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str2)) && (!TextUtils.isEmpty(str3)))
        {
          mTencent.setAccessToken(str1, str2);
          mTencent.setOpenId(str3);
        }


  }

  private void onClickAddPicUrlTweet()
  {
    String str = new ImageFileCache().getPath("http://121.40.177.251:6940picpath");
    this.mWeibo.sendPicText(this.content, str, new TQQApiListener("add_pic_t", false, this.mActivity));
    UIHelper.loading("请稍后……", this.mActivity);
  }

  public static boolean ready(Context paramContext)
  {
      if (mTencent == null)
        return false;
      if ((mTencent.isSessionValid()) && (mTencent.getQQToken().getOpenId() != null)){


        return true;
      }else{
        Toast.makeText(paramContext, "login and get openId first, please!", Toast.LENGTH_SHORT).show();
      }
    return false;

  }

  private void shareQQ()
  {

    Bundle localBundle = new Bundle();
    localBundle.putString("title", this.mShareModle.getName());
    localBundle.putString("targetUrl", Constants.qq_targetUrl+ this.mShareModle.getId());
    localBundle.putString("summary", this.content);
    localBundle.putString("imageUrl", BaseURL.HOST + this.mShareModle.getPic());
    localBundle.putString("appName", this.mActivity.getString(R.string.app_name));
    localBundle.putInt("req_type", this.shareType);
    mTencent.shareToQQ(this.mActivity, localBundle, this);
  }

  public void login(int paramInt) {
    while (true) {


      this.tempType = paramInt;
      switch (this.tempType) {
        case 0:
          shareQQ();
          break;
        case 1:
          QZoneShare();
          break;
        case 2:
          if (!mTencent.isSessionValid())
            mTencent.login(this.mActivity, "all", this.loginListener);
          else
            onClickAddPicUrlTweet();
          break;
      }


    }
  }

  public void onClickAddTweet()
  {
    this.mWeibo.sendText("test add tweet", new TQQApiListener("add_t", false, this.mActivity));
    UIHelper.loading("正在加载……", this.mActivity);
  }

  public void onComplete(Object paramObject)
  {
    UIHelper.toastMessage(this.mActivity.getApplicationContext(), "分享成功");
  }

  public void onError(UiError paramUiError)
  {
    UIHelper.toastMessage(this.mActivity.getApplicationContext(), paramUiError.errorMessage);
  }

  @Override
  public void onCancel() {
    UIHelper.toastMessage(mActivity, "onCancel: ");
  }

  public class BaseUiListener implements IUiListener
  {

    public BaseUiListener()
    {
    }

    public void doComplete(JSONObject paramJSONObject)
    {
    }

    public void onCancel()
    {
      UIHelper.toastMessage(mActivity, "onCancel: ");
    }

    public void onComplete(Object paramObject)
    {

      if (paramObject == null)
      {
        UIHelper.toastMessage(mActivity, "返回为空, 登录失败");
        return;
      }
      JSONObject localJSONObject = (JSONObject)paramObject;
      if ((localJSONObject != null) && (localJSONObject.length() == 0))
      {
        UIHelper.toastMessage(mActivity, "返回为空,登录失败");
        return;
      }
      UIHelper.toastMessage(mActivity, "登录成功");
      doComplete((JSONObject)paramObject);
    }

    public void onError(UiError paramUiError)
    {
      UIHelper.toastMessage(mActivity, "onError: " + paramUiError.errorDetail);
    }
  }

  public class TQQApiListener extends BaseUIListener
  {
    private Activity mActivity;
    private Boolean mNeedReAuth = Boolean.valueOf(false);
    private String mScope = "all";

    public TQQApiListener(String paramBoolean, boolean paramActivity, Activity arg4)
    {
      super(arg4);
      this.mScope = paramBoolean;
      this.mNeedReAuth = Boolean.valueOf(paramActivity);
      this.mActivity = arg4;
    }

    public void onComplete(Object paramObject)
    {
      final Activity localActivity = mActivity;
      try
      {
        int i = ((JSONObject)paramObject).getInt("ret");
        if (i == 0)
          UIHelper.toastMessage(localActivity, "分享成功");

          UIHelper.loadingClose();

          if ((i == 100030) && (this.mNeedReAuth.booleanValue()))
            localActivity.runOnUiThread(new Runnable()
            {
              public void run()
              {
                QQshare.mTencent.reAuth(localActivity, "all", new BaseUIListener(localActivity));
              }
            });

      }
      catch (JSONException localJSONException)
      {
          localJSONException.printStackTrace();
          UIHelper.toastMessage(localActivity, "onComplete() JSONException: " + paramObject.toString());
      }
    }
  }
}