package com.hd.wlj.duohaowan.ui.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hd.wlj.duohaowan.BuildConfig;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.been.Order;
import com.hd.wlj.third.pay.WeiXinPay;
import com.orhanobut.logger.Logger;
import com.wlj.base.bean.Base;
import com.wlj.base.util.MathUtil;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends AppCompatActivity {

    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.pay_name)
    TextView payName;
    @BindView(R.id.pay_money)
    TextView payMoney;
    private String jsonstr;
    private String borderid;
    private UpdateWorkBroadcastReceiver update;
    private LocalBroadcastManager localBroadcastManager;
    public static String WX_PAYRESULT_ACTION = "com.hd.wlj.duohaowan.ui.pay.WX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);

        initBroadcast();

        titleTitle.setText("选择支付方式");

        Intent intent = getIntent();
        jsonstr = intent.getStringExtra("jsonstr");
        String money = intent.getDoubleExtra("payMoneyFen",0)+"";
        String name = intent.getStringExtra("payName");
        borderid = intent.getStringExtra("borderid");

//        payName默认为相框
        if (StringUtils.isEmpty(name)) {
            payName.setText(name);
        }
        payMoney.setText(MathUtil.divide(money, 100, 2).doubleValue() + "元");
    }

    @OnClick({R.id.title_back, R.id.pay_weixin, R.id.pay_zhifubao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.pay_weixin:
                //创建支付订单
                creadOrder();
                break;
            case R.id.pay_zhifubao:
                break;
        }
    }

    /**
     * 点击收费的画框时 的 创建订单
     */
    public void creadOrder() {
        UIHelper.loading("创建订单中",this);
        Order order = new Order(this);
        order.setId(borderid);
        order.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {

                UIHelper.toastMessage(getApplicationContext(),"创建订单成功");
                UIHelper.loadingCir(PayActivity.this);

                WeiXinPay.pay(getApplicationContext(), base.getResultJsonObject().optJSONObject("pay_data"));
            }

            @Override
            public void fail(Exception paramException) {
                UIHelper.loadingClose();
            }
        });

    }

    /**
     * 微信支付后刷新的广播
     */
    private void initBroadcast() {
        update = new UpdateWorkBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WX_PAYRESULT_ACTION);
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.registerReceiver(update,intentFilter);
    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(update);
        super.onDestroy();
    }

    /**
     * 微信支付成功后 通过广播通知界面刷新
     */
    public class UpdateWorkBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int errCode = intent.getIntExtra("errCode",-1);
            //
            String result;
            if (errCode == 0) {
                result = "微信支付成功";
                PayActivity.this.setResult(RESULT_OK);
                PayActivity.this.finish();

            } else if (errCode == -2) {
                result = "用户取消";
                if(BuildConfig.DEBUG) {//测试环境下
                    PayActivity.this.setResult(RESULT_OK);
                    PayActivity.this.finish();
                }
            } else {
                result = "微信支付失败";
                //可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
            }//end
            UIHelper.toastMessage(PayActivity.this,result);

        }
    }


}
