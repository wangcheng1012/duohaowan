package com.hd.wlj.third.share.qq;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.wlj.base.util.UIHelper;

import org.json.JSONObject;

public class BaseUIListener implements IUiListener {
    private static final int ON_CANCEL = 2;
    private static final int ON_COMPLETE = 0;
    private static final int ON_ERROR = 1;
    private Context mContext;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message paramAnonymousMessage) {
            switch (paramAnonymousMessage.what) {
                case 0:
                    JSONObject localJSONObject = (JSONObject) paramAnonymousMessage.obj;
                    UIHelper.toastMessage(mContext, localJSONObject.toString());
                    UIHelper.loadingClose();
                    return;
                case 1:
                    UiError localUiError = (UiError) paramAnonymousMessage.obj;
                    UIHelper.toastMessage(mContext, "errorMsg:" + localUiError.errorMessage + "errorDetail:" + localUiError.errorDetail);
                    UIHelper.loadingClose();
                    return;
                case 2:
            }
            UIHelper.toastMessage(mContext, "onCancel");
        }
    };
    private boolean mIsCaneled;
    private String mScope;

    public BaseUIListener(Context paramContext) {
        this.mContext = paramContext;
    }

    public BaseUIListener(Context paramContext, String mScope) {
        this.mContext = paramContext;
        this.mScope = mScope;
    }

    public void cancel() {
        this.mIsCaneled = true;
    }

    public Context getmContext() {
        return this.mContext;
    }

    public void onCancel() {
        Message localMessage = this.mHandler.obtainMessage();
        localMessage.what = 2;
        this.mHandler.sendMessage(localMessage);
    }

    public void onComplete(Object paramObject) {

        Message localMessage = this.mHandler.obtainMessage();
        localMessage.what = 0;
        localMessage.obj = paramObject;
        this.mHandler.sendMessage(localMessage);
    }

    public void onError(UiError paramUiError) {
        Message localMessage = this.mHandler.obtainMessage();
        localMessage.what = 1;
        localMessage.obj = paramUiError;
        this.mHandler.sendMessage(localMessage);
    }

    public void setmContext(Context paramContext) {

        this.mContext = paramContext;
    }
}