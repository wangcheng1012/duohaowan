package com.hd.wlj.duohaowan.been;

import android.app.Activity;

import com.hd.wlj.duohaowan.Urls;
import com.wlj.base.bean.Base;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 订单
 */
public class Order extends BaseAsyncModle {

    //支付宝支付
    public static final Integer pay_method_alipay = 3;
    //微信支付
    public static final Integer pay_method_weipay = 4;

    public Order() {
        super();
    }

    public Order(Activity paramActivity) {
        super(paramActivity);
    }

    public Order(JSONObject jo) {
        super(jo);
    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        HttpPost httpPost = new HttpPost(Urls.save_order);
        httpPost.setJiami(true);

        httpPost.addParemeter("peyMethod",pay_method_weipay);
        httpPost.addParemeter("product_id", getId());
        asRequestModle.setHttpPost(httpPost);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new Order(jsonObject);
    }
}
