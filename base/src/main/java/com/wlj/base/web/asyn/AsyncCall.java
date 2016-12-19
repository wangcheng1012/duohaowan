package com.wlj.base.web.asyn;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.orhanobut.logger.Logger;
import com.wlj.base.bean.Base;
import com.wlj.base.bean.BaseList;
import com.wlj.base.util.AppConfig;
import com.wlj.base.util.AppContext;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.RequestException;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.MsgContext;

import java.net.SocketTimeoutException;
import java.util.List;

public class AsyncCall
        extends AsyncTask<Void, Integer, Object> {
    private Activity activity;
    private AsyncRequestModle asyncRequestModle;
    private boolean autoToLoginPage;
    private int lastpage;
    private OnAsyncBackListener onBackListener;
    private boolean showLoading;
    private boolean showToast;
    private int pageIndex;

    public AsyncCall(AsyncRequestModle paramAsyncRequestModle) {
        this.showToast = true;
        this.autoToLoginPage = true;
        this.showLoading = paramAsyncRequestModle.isShowLoading();
        this.autoToLoginPage = paramAsyncRequestModle.isAutoToLoginPage();
        this.asyncRequestModle = paramAsyncRequestModle;
        this.activity = paramAsyncRequestModle.getActivity();
    }

    public void onPreExecute() {
        if ((!showLoading) || (this.activity == null)) {
            return;
        }

        UIHelper.showProgressbar(this.activity, new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface paramAnonymousDialogInterface) {

                AsyncCall.this.cancel(true);
            }
        });

    }

    public Object doInBackground(Void... paramVarArgs) {

        try {

            BaseList localObject = AsyncRequestWebClient.getInstall().Request(asyncRequestModle);

            if (!isCancelled()) {
                return localObject;
            } else {
                return new RequestException("取消");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }

    }

    public boolean isComplate() {

        if (pageIndex >= this.lastpage) {
            return true;
        }
        return false;
    }


    public void onCancelled() {
        super.onCancelled();
        Logger.e("onCancelled");
    }

    public void onCancelled(Object paramObject) {
        super.onCancelled(paramObject);
        Logger.e("onCancelled ~~~" + paramObject);
    }

    public void onPostExecute(Object paramObject) {
        super.onPostExecute(paramObject);

        String str = null;

        if ((paramObject instanceof Exception)) {
            // 异常
            Exception localException = (Exception) paramObject;
            str = localException.getMessage();

            if ((this.autoToLoginPage) && (str != null) && str.equals(MsgContext.request_loginError + "")) {
                //登录
                GoToHelp.go(this.activity, AppConfig.getAppConfig().getLoginClass());
                AppContext.getAppContext().loginOut();
            } else if (this.showToast) {
                //显示异常
                if ((localException instanceof SocketTimeoutException)) {
                    UIHelper.toastMessage(AppContext.getAppContext(), " 链接超时");
                } else {
                    UIHelper.toastMessage(AppContext.getAppContext(), str);
                }

            } else {
                //把异常返回前段

                if (this.onBackListener != null) {
                    this.onBackListener.fail(localException);
                }
            }

        }
//        返回正常数据 BaseList ／ base
        if (this.onBackListener != null) {
            if ((paramObject instanceof BaseList)) {

                BaseList baseList = (BaseList) paramObject;
                List localList = baseList.getList();
                Base localBase = baseList.getBaseData();
                this.lastpage = baseList.getLastpage();
                pageIndex = baseList.getPageIndex();

                baseList = null;//释放

                if ((localList != null) || (localBase != null)) {
                    this.onBackListener.OnAsyncBack(localList, localBase, this.asyncRequestModle.getType());
                } else {
                    UIHelper.toastMessage(AppContext.getAppContext(), "数据为空");
                }
            } else if ((paramObject instanceof Base)) {
                this.onBackListener.OnAsyncBack(null, (Base) paramObject, this.asyncRequestModle.getType());
            }
        }
        UIHelper.loadingClose();
        UIHelper.closeProgressbar();
    }



    public void onProgressUpdate(Integer... paramVarArgs) {
        super.onProgressUpdate(paramVarArgs);
    }

    public void setAutoToLoginPage(boolean paramBoolean) {

        this.autoToLoginPage = paramBoolean;
    }

    public void setOnAsyncBackListener(OnAsyncBackListener paramOnAsyncBackListener) {

        this.onBackListener = paramOnAsyncBackListener;
    }

    public void setShowLoading(boolean paramBoolean) {

        this.showLoading = paramBoolean;
    }

    public void setShowToast(boolean paramBoolean) {

        this.showToast = paramBoolean;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public interface OnAsyncBackListener {
        void OnAsyncBack(List<Base> list, Base base, int requestType);

        void fail(Exception paramException);
    }

    public interface callWeb {
        AsyncCall Request();
    }
}

