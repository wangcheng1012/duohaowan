package com.wlj.base.web.asyn;

import android.app.Activity;

import com.wlj.base.bean.Base;
import com.wlj.base.util.AppContext;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.HttpPost;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseAsyncModle extends Base implements AsyncCall.callWeb {
    private static final long serialVersionUID = -764627344493414453L;
    public transient Activity activity;
    private int page = -1;
    private int pageSize = 10;
    private boolean refresh = true;


    public BaseAsyncModle() {
    }

    public BaseAsyncModle(Activity paramActivity) {

        this.activity = paramActivity;
    }

    public BaseAsyncModle(JSONObject jo) {
        super(jo);
    }

    public AsyncCall Request() {
        return Request(-1);
    }

    public AsyncCall Request(int type) {

        try {
            AsyncRequestModle localAsyncRequestModle = new AsyncRequestModle();
            localAsyncRequestModle.setActivity(this.activity);
            localAsyncRequestModle.setParserCla(getClass());
            localAsyncRequestModle.setJiami(true);
            localAsyncRequestModle.setRefresh(this.refresh);
            if (type == -1) {
                addRequestParemeter(localAsyncRequestModle);
            }else{
                localAsyncRequestModle.setType(type);
                addRequestParemeter(localAsyncRequestModle, type);
            }

            HttpPost localHttpPost = localAsyncRequestModle.getHttpPost();
            if (getPage() != -1) {
                localHttpPost.addParemeter("page", Integer.valueOf(getPage()));
                localHttpPost.addParemeter("pageSize", Integer.valueOf(getPageSize()));
            }
            AsyncCall localAsyncCall = new AsyncCall(localAsyncRequestModle);
            localAsyncCall.execute();
            return localAsyncCall;

        } catch (IOException localIOException) {
            localIOException.printStackTrace();
            UIHelper.toastMessage(AppContext.getAppContext(), "请求异常");
        }
        return null;
    }

    public abstract void addRequestParemeter(AsyncRequestModle asRequestModle)
            throws IOException;

    public void addRequestParemeter(AsyncRequestModle asRequestModle, int type)
            throws IOException {

    }

    public Activity getActivity() {

        return this.activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getPage() {

        return this.page;
    }

    public int getPageSize() {

        return this.pageSize;
    }

    public AsyncCall getThisfromid() {

        return null;
    }

    public boolean isRefresh() {

        return this.refresh;
    }

    public BaseAsyncModle parseThis(JSONObject paramJSONObject)
            throws JSONException {

        return null;
    }

    public void setPage(int paramInt) {

        this.page = paramInt;
    }

    public void setPageSize(int paramInt) {

        this.pageSize = paramInt;
    }

    public void setRefresh(boolean paramBoolean) {

        this.refresh = paramBoolean;
    }
}
