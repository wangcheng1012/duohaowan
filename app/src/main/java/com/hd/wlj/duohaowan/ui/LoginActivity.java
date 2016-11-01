package com.hd.wlj.duohaowan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hd.wlj.duohaowan.MainActivity;
import com.hd.wlj.duohaowan.been.User;
import com.hd.wlj.duohaowan.view.ImgInpImg;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.third.quicklogin.wx.WXLogin;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.AppConfig;
import com.wlj.base.util.AppContext;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.util.statusbar.StatusBarUtil;
import com.wlj.base.web.asyn.AsyncCall;
import com.wlj.base.widget.IconfontTextview;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登陆
 *
 * @author wlj
 */

public class LoginActivity extends BaseFragmentActivity {

    @BindView(R.id.login_back)
    IconfontTextview loginBack;
    @BindView(R.id.login_account)
    ImgInpImg loginAccount;
    @BindView(R.id.login_psw)
    ImgInpImg loginPsw;
    @BindView(R.id.login_login_button)
    Button loginLoginButton;
    @BindView(R.id.login_forgetPswTV)
    TextView loginForgetPswTV;
    @BindView(R.id.login_qq_imageView)
    ImageView loginQqImageView;
    @BindView(R.id.login_wx_imageView)
    ImageView loginWxImageView;
    @BindView(R.id.login_rg_tv)
    TextView loginRgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        AppContext.getAppContext().loginOut();

        loginAccount.getIconFontView1().setTextSize(20f);
        String digists = "abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        loginAccount.getEditTextView().setKeyListener(DigitsKeyListener.getInstance(digists));
//        loginAccount.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        loginAccount.getEditTextView().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

        loginPsw.getIconFontView1().setTextSize(30f);
        loginPsw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
    }

    @OnClick({R.id.login_back, R.id.login_login_button, R.id.login_forgetPswTV, R.id.login_qq_imageView, R.id.login_wx_imageView, R.id.login_rg_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_back:
                finish();
                break;
            case R.id.login_login_button:
                login();

//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.login_forgetPswTV:
                //忘记密码
                startActivity(new Intent(getApplicationContext(), ForgetPSWActivity.class));
                break;
            case R.id.login_qq_imageView:

                break;

            case R.id.login_wx_imageView:
                WXLogin wxLogin = new WXLogin(getApplicationContext());
                wxLogin.login();
                break;
            case R.id.login_rg_tv:
                //注册
                startActivity(new Intent(getApplicationContext(), RegesterActivity.class));

                break;
        }
    }

    private void login() {

        String phone = loginAccount.getText();
        String psw = loginPsw.getText();

//        phone = "15310315193";
//        psw = "123456";

        if(verify(phone,psw))return;

        User user = new User(this);
        user.setPhone(phone);
        user.setPsw(psw);
        AsyncCall request = user.Request(User.LOGIN);
        request.setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int paramInt) {

                JSONObject jsonObject = paramBase.getResultJsonObject();

                AppConfig appConfig = AppConfig.getAppConfig();
                appConfig.set(AppConfig.CONF_KEY,jsonObject.optString("key"));
                GoToHelp.go(LoginActivity.this, MainActivity.class);
                finish();
            }

            @Override
            public void fail(Exception paramException) {

            }
        });

    }

    private boolean verify(String phone, String psw) {
        //phone 验证
        if (StringUtils.isEmpty(phone)) {
            UIHelper.toastMessage(getApplicationContext(),"电话号码为空");
            return true;
        }
        if (phone.length() != 11) {
            UIHelper.toastMessage(getApplicationContext(),"电话号码格式错误");
            return true;
        }

        //密码格式验证
        if (StringUtils.isEmpty(psw)) {
            UIHelper.toastMessage(getApplicationContext(),"密码为空");
            return true;
        }
        if (6 > psw.length() || psw.length() > 16) {
            UIHelper.toastMessage(getApplicationContext(),"密码长度6-16");
            return true;
        }
        return false;
    }
}
