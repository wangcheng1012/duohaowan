package com.wlj.base.web.asyn;

import android.app.Activity;
import android.util.Base64;

import com.wlj.base.bean.Base;
import com.wlj.base.util.AppConfig;
import com.wlj.base.util.AppContext;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.Md5Util;
import com.wlj.base.web.MsgContext;

import java.io.Serializable;
import java.util.Iterator;

import org.json.JSONObject;

public class AsyncRequestModle
        implements Serializable {
    private static final long serialVersionUID = -7241890269318232468L;
    private Activity activity;
    private boolean autoToLoginPage = true;
    private String cachekey;
    private HttpPost httpPost;
    private boolean jiami;
    private BaseAsyncModle parserBase;
    private Class<?> parserCla;
    private boolean refresh = true;
    private boolean save = true;
    private boolean showLoading;
    private int type;

    public AsyncRequestModle() {
    }

    public Activity getActivity() {

        return this.activity;
    }

    public String getCachekey() {
        JSONObject paremeter = httpPost.getJSONObjectParemeter();

//        Iterator<String> keys = paremeter.keys();

        String str =  paremeter.toString();
        int i = Math.min(340,str.length());
        return
                Md5Util.MD5(
//                Base64.encodeToString((
                AppContext.getAppContext().getProperty(AppConfig.CONF_TYPT )
                        + AppContext.getAppContext().getProperty(AppConfig.CONF_NAME)
                        + parserCla.getSimpleName()
                        + httpPost.getUrl().getPath()
                        + paremeter.optString(MsgContext.key_page)
                        + paremeter.optString(MsgContext.key_pageSize)
                        + cachekey
                        + str.substring(0, i)
                )
//        ).getBytes(), Base64.DEFAULT)
 ;
    }

    public HttpPost getHttpPost() {

        return this.httpPost;
    }

    public BaseAsyncModle getParserBase() {

        return this.parserBase;
    }

    public Class<?> getParserCla() {

        return this.parserCla;
    }

    public int getType() {

        return this.type;
    }

    public boolean isAutoToLoginPage() {

        return this.autoToLoginPage;
    }

    public boolean isJiami() {

        return this.jiami;
    }

    public boolean isRefresh() {

        return this.refresh;
    }

    public boolean isSave() {

        return this.save;
    }

    public boolean isShowLoading() {

        return this.showLoading;
    }

    public void setActivity(Activity paramActivity) {

        this.activity = paramActivity;
    }

    public void setAutoToLoginPage(boolean paramBoolean) {

        this.autoToLoginPage = paramBoolean;
    }

    public void setCachekey(String paramString) {

        this.cachekey = paramString;
    }

    public void setHttpPost(HttpPost paramHttpPost) {

        this.httpPost = paramHttpPost;
    }

    public void setJiami(boolean paramBoolean) {

        this.jiami = paramBoolean;
    }

    public void setParserBase(BaseAsyncModle paramBaseAsyncModle) {

        this.parserBase = paramBaseAsyncModle;
    }

    public void setParserCla(Class<?> paramClass) {

        this.parserCla = paramClass;
    }

    public void setRefresh(boolean save) {

        this.refresh = save;
    }

    public void setSave(boolean save) {

        this.save = save;
    }

    public void setShowLoading(boolean paramBoolean) {

        this.showLoading = paramBoolean;
    }

    public void setType(int paramInt) {

        this.type = paramInt;
    }
}
