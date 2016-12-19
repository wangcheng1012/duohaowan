package com.wlj.base.web.asyn;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.wlj.base.bean.BaseList;
import com.wlj.base.util.AppContext;
import com.wlj.base.util.ListUtils;
import com.wlj.base.util.RequestException;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.Md5Util;
import com.wlj.base.web.MsgContext;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONObject;

public class AsyncRequestWebClient {
    private static AsyncRequestWebClient requestWebClient;

    public AsyncRequestWebClient() {
    }

    public static AsyncRequestWebClient getInstall() {

        if (requestWebClient == null) {
            requestWebClient = new AsyncRequestWebClient();
        }
        return requestWebClient;
    }

    /**
     * 访问网络
     *
     * @param paramAsyncRequestModle
     * @return
     * @throws Exception
     */
    private BaseList callWeb(AsyncRequestModle paramAsyncRequestModle) throws Exception {

        try {
            JSONObject localJSONObject = encryptionCall(paramAsyncRequestModle);
            return parse(localJSONObject, paramAsyncRequestModle);
        } catch (Exception localException) {
            if ((localException instanceof FileNotFoundException)) {
                throw new RequestException("(文件没找到)");
            }
            throw localException;
        }
    }

    /**
     * 给你网络请求加密
     *
     * @param paramAsyncRequestModle
     * @return
     * @throws Exception
     */
    private JSONObject encryptionCall(AsyncRequestModle paramAsyncRequestModle) throws Exception {
        HttpPost localHttpPost = paramAsyncRequestModle.getHttpPost();
        if (localHttpPost == null) {
            throw new RequestException("HttpPost为空");
        }
        String url = localHttpPost.getUrl().getPath();

        String str2 = url.substring(url.lastIndexOf("/"), url.length());

        if (paramAsyncRequestModle.isJiami()) {
            Encrpt.encrypt(paramAsyncRequestModle, localHttpPost);
        } else {
            //不加密
//            String key = AppContext.getAppContext().getProperty("key");
//            String name = AppContext.getAppContext().getProperty("name");
//            String cur = "" + System.currentTimeMillis();
//            String mac = Md5Util.MD5Normal(key + cur);
//            localHttpPost.addParemeter("login_name", name);
//            localHttpPost.addParemeter("login_rand", cur);
//            localHttpPost.addParemeter("login_mac", mac);
        }
        String parem = localHttpPost.getJSONObjectParemeter().toString();

        try {
            String str8 = localHttpPost.getResult();
            Logger.d(str2 + "：" + parem + "\n" + str8);
//        Logger.json(str8);
            return new JSONObject(str8);
        } catch (IOException e) {
            throw new RequestException("网络链接异常");
        }
    }

    /**
     * 解析
     *
     * @param paramJSONObject
     * @param requestModle
     * @return
     * @throws Exception
     */
    private BaseList parse(JSONObject paramJSONObject, AsyncRequestModle requestModle)
            throws Exception {
        int i = paramJSONObject.optInt("statusCode");
        if (i == 0) {
            i = paramJSONObject.optInt("state");
        }

        if (MsgContext.request_loginError == i) {
            //登陆
            throw new RequestException(MsgContext.request_loginError + "");
        } else if ((MsgContext.request_success == i) || (MsgContext.request_success2 == i)) {
            BaseAsyncModle asyncModle = requestModle.getParserBase();
            if (asyncModle == null) {
                return new BaseList().parse(paramJSONObject, requestModle.getParserCla());
            }
            asyncModle.parseThis(paramJSONObject.optJSONObject("data"));
            BaseList localBaseList = new BaseList();
            localBaseList.setBaseData(asyncModle);
            return localBaseList;
        } else if ((MsgContext.request_false2 == i) || (MsgContext.request_false == i) || (MsgContext.request_system_error2 == i)
                || (MsgContext.request_system_error == i)) {

            String str = paramJSONObject.optString("message");
            if ("".equals(str)) {
                str = paramJSONObject.optString("description");
            }
            throw new RequestException(str);
        }

        throw new RequestException("未知异常");
    }

    /**
     * 如果 requestModle.isSave() 就会缓存请求的数据
     *
     * @param requestModle
     * @return
     * @throws Exception
     */
    public BaseList Request(AsyncRequestModle requestModle) throws Exception {

        String str = requestModle.getCachekey();
        AppContext appContext = AppContext.getAppContext();
        Logger.w("key: " + str);
        BaseList baseList = null;
        if (requestModle.isRefresh()) {
            baseList = RequestConnected(requestModle);
            if ((baseList != null) && (requestModle.isSave())) {
                appContext.saveObject(baseList, str);
            }
        } else {
            baseList = getLocaData(requestModle, appContext);
        }

        return baseList;
    }

    /**
     * 不带缓存的请求
     *
     * @param requestModle
     * @return
     * @throws Exception
     */
    public BaseList RequestConnected(AsyncRequestModle requestModle) throws Exception {

        AppContext appContext = AppContext.getAppContext();
        boolean networkConnected = NetWorkUtils.isNetworkConnected(appContext);

        if (networkConnected) {
            return callWeb(requestModle);
        }
        return getLocaData(requestModle, appContext);
    }

    /**
     * 获取本地数据
     *
     * @param requestModle
     * @param appContext
     * @return
     * @throws RequestException
     */
    @NonNull
    private BaseList getLocaData(AsyncRequestModle requestModle, AppContext appContext) throws RequestException {
        BaseList localBaseList = (BaseList) appContext.readObject(requestModle.getCachekey());
        if (localBaseList == null || (localBaseList.getBaseData() == null && ListUtils.isEmpty(localBaseList.getList()) ) ) {

            throw new RequestException("网络异常");
        }
        return localBaseList;
    }
}
