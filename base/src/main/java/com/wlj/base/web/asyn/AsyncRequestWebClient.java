package com.wlj.base.web.asyn;

import com.orhanobut.logger.Logger;
import com.wlj.base.bean.BaseList;
import com.wlj.base.util.AppContext;
import com.wlj.base.util.RequestException;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.Md5Util;
import com.wlj.base.web.MsgContext;

import java.io.FileNotFoundException;

import org.json.JSONObject;

public class AsyncRequestWebClient {
    private static AsyncRequestWebClient requestWebClient;

    public AsyncRequestWebClient() {
    }


    private BaseList callWeb(AsyncRequestModle paramAsyncRequestModle)
            throws Exception {

        try {
            JSONObject localJSONObject = encryptionCall(paramAsyncRequestModle);
            return parse(localJSONObject, paramAsyncRequestModle);
        } catch (Exception localException) {
            if ((localException instanceof FileNotFoundException)) {
                throw new RequestException("(FileNotFoundException)");
            }
            throw localException;
        }
    }

    private JSONObject encryptionCall(AsyncRequestModle paramAsyncRequestModle)
            throws Exception {
        HttpPost localHttpPost = paramAsyncRequestModle.getHttpPost();
        if (localHttpPost == null) {
            throw new RequestException("HttpPost为空");
        }
        String url = localHttpPost.getUrl().getPath();

        String str2 = url.substring(url.lastIndexOf("/"), url.length());

        String parem = localHttpPost.getJSONObjectParemeter().toString();
//        Logger.w("参数" + str2 +"： "+ parem);

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

        String str8 = localHttpPost.getResult();
        Logger.w(str2 +"："+ parem + " \n " +str8);

        return new JSONObject(str8);

    }

    public static AsyncRequestWebClient getInstall() {

        if (requestWebClient == null) {
            requestWebClient = new AsyncRequestWebClient();
        }
        return requestWebClient;
    }

    private BaseList parse(JSONObject paramJSONObject, AsyncRequestModle paramAsyncRequestModle)
            throws Exception {
        int i = paramJSONObject.optInt("statusCode");
        if (i == 0) {
            i = paramJSONObject.optInt("state");
        }
        if ((MsgContext.request_success == i) || (MsgContext.request_success2 == i)) {
            BaseAsyncModle localBaseAsyncModle = paramAsyncRequestModle.getParserBase();
            if (localBaseAsyncModle == null) {
                return new BaseList().parse(paramJSONObject, paramAsyncRequestModle.getParserCla());
            }
            localBaseAsyncModle.parseThis(paramJSONObject.optJSONObject("data"));
            BaseList localBaseList = new BaseList();
            localBaseList.setBaseData(localBaseAsyncModle);
            return localBaseList;
        }
        if ((MsgContext.request_false2 == i) || (MsgContext.request_false == i) || (MsgContext.request_system_error2 == i)
                || (MsgContext.request_system_error == i)) {
            String str = paramJSONObject.optString("message");
            if ("".equals(str)) {
                str = paramJSONObject.optString("description");
            }
            throw new RequestException(str);
        }
        throw new RequestException("未知异常");
    }

    public BaseList Request(AsyncRequestModle paramAsyncRequestModle)
            throws Exception {

        String str = paramAsyncRequestModle.getCachekey();
        AppContext localAppContext = AppContext.getAppContext();
        Logger.w("key", new Object[]{str});
        BaseList localBaseList = null;
        if ((!localAppContext.isReadDataCache(str)) || (paramAsyncRequestModle.isRefresh())) {
            localBaseList = RequestConnected(paramAsyncRequestModle);
            if ((localBaseList != null) && (paramAsyncRequestModle.isSave())) {
                localAppContext.saveObject(localBaseList, str);
            }
        }
        return localBaseList;
    }

    public BaseList RequestConnected(AsyncRequestModle paramAsyncRequestModle)
            throws Exception {
        AppContext localAppContext = AppContext.getAppContext();
        if (localAppContext.isNetworkAvailable()) {
            return callWeb(paramAsyncRequestModle);
        }
        BaseList localBaseList = (BaseList) localAppContext.readObject(paramAsyncRequestModle.getCachekey());
        if (localBaseList == null) {
            throw new RequestException("数据为空");
        }
        return localBaseList;
    }
}
