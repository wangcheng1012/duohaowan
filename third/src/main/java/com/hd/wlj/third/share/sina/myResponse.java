package com.hd.wlj.third.share.sina;

import android.content.Context;
import android.widget.Toast;

import com.hd.wlj.third.R;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

public class myResponse
  implements IWeiboHandler.Response
{
  private Context mContext;

  public myResponse(Context paramContext)
  {
    this.mContext = paramContext;
    Logger.e("myResponse", new Object[] { "还不调用啊" });
  }

  public void onResponse(BaseResponse paramBaseResponse)
  {
    switch (paramBaseResponse.errCode)
    {

    case 0:
      Toast.makeText(this.mContext, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_SHORT).show();
      return;
    case 1:
      Toast.makeText(this.mContext, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_SHORT).show();
      return;
    case 2:
    }
    Toast.makeText(this.mContext, this.mContext.getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: " + paramBaseResponse.errMsg, Toast.LENGTH_SHORT).show();
  }
}