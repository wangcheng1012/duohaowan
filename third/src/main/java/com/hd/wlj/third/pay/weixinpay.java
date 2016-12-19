package com.hd.wlj.third.pay;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hd.wlj.third.share.Constants;
import com.sina.weibo.sdk.utils.MD5;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wlj.base.util.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wlj on 2016/12/18.
 */

public class WeiXinPay {

    public static void pay(Context context, JSONObject json) {

        IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        // 将该app注册到微信
        api.registerApp(Constants.WX_APP_ID);

        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            UIHelper.toastMessage(context, "版本不支持");
            return;
        }

        try {
            if (null != json && !json.has("retcode")) {
                PayReq req = new PayReq();
                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                req.appId = json.getString("appid");
                req.partnerId = json.getString("partnerid");
                req.prepayId = json.getString("prepay_id");
                req.nonceStr = json.getString("noncestr");
                req.timeStamp = json.getString("timestamp");
//                req.timeStamp = System.currentTimeMillis()+"";
                req.packageValue = json.getString("package");//"Sign=WXPay";
//                req.sign = json.getString("sign");

                List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                signParams.add(new NameValuePair("appid", req.appId));
                signParams.add(new NameValuePair("noncestr", req.nonceStr));
                signParams.add(new NameValuePair("package", req.packageValue));
                signParams.add(new NameValuePair("partnerid", req.partnerId));
                signParams.add(new NameValuePair("prepayid", req.prepayId));
                signParams.add(new NameValuePair("timestamp", req.timeStamp));
//
                req.sign = genAppSign(signParams);

                req.extData = "app data"; // optional
//                Toast.makeText(context, "正常调起支付", Toast.LENGTH_SHORT).show();
                Log.d("签名",req.sign + "  " + json.getString("sign"));
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                api.sendReq(req);
            } else {
                Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                Toast.makeText(context, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
//            e.printStackTrace();
            Log.e("PAY_GET", "异常："+e.getMessage());
            Toast.makeText(context, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private static String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.WX_APP_KEY);
//        this.sb.append("sign str\n"+sb.toString()+"\n\n");
        String appSign = MD5.hexdigest(sb.toString().getBytes()).toUpperCase();
//        Log.e("orion",appSign);
        return appSign;
    }

    private static class NameValuePair {

        private final String name;
        private final String value;

        public NameValuePair(String name, String value) {

            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
