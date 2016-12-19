package com.hd.wlj.third.share.qq;

import org.json.JSONObject;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.wlj.base.util.UIHelper;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class BaseUIListener implements IUiListener {
    private Context mContext;
    private String mScope;
    private boolean mIsCaneled;
    private static final int ON_COMPLETE = 0;
    private static final int ON_ERROR = 1;
    private static final int ON_CANCEL = 2;

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

    public BaseUIListener(Context mContext) {
        super();
        this.mContext = mContext;
    }


    public BaseUIListener(Context mContext, String mScope) {
        super();
        this.mContext = mContext;
        this.mScope = mScope;
    }

    public void cancel() {
        mIsCaneled = true;
    }


    @Override
    public void onComplete(Object response) {
        if (mIsCaneled) return;
        Message msg = mHandler.obtainMessage();
        msg.what = ON_COMPLETE;
        msg.obj = response;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onError(UiError e) {
        if (mIsCaneled) return;
        Message msg = mHandler.obtainMessage();
        msg.what = ON_ERROR;
        msg.obj = e;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onCancel() {
        if (mIsCaneled) return;
        Message msg = mHandler.obtainMessage();
        msg.what = ON_CANCEL;
        mHandler.sendMessage(msg);
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

}
