package com.hd.wlj.duohaowan.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hd.wlj.duohaowan.MainActivity;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.been.User;
import com.hd.wlj.duohaowan.view.ImgInpImg;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.AppConfig;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 */
public class RegesterActivity extends BaseFragmentActivity {

    @BindView(R.id.regester_phone)
    ImgInpImg regesterPhone;
    @BindView(R.id.regester_verifyCode)
    EditText regesterVerifyCode;
    @BindView(R.id.regester_psw)
    ImgInpImg regesterPsw;
    @BindView(R.id.regester_repsw)
    ImgInpImg regesterRepsw;
    @BindView(R.id.regester_submit)
    Button regesterSubmit;
    @BindView(R.id.regester_repsw_ll)
    LinearLayout regesterRepswLl;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        regesterRepswLl.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        regesterPhone.getIconFontView1().setIconPath(getString(R.string.phonePath));
        regesterPhone.getIconFontView1().setTextSize(30f);
        regesterPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
        regesterPhone.getEditTextView().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        regesterPhone.getIconFontView2().setTextSize(18f);

        regesterVerifyCode.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        regesterPsw.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        regesterPsw.getIconFontView1().setTextSize(30f);

//        regesterPhone.getIconFontView2().setTextSize(16f);
        regesterPhone.getIconFontView2().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoneRand();
            }
        });
    }

    /**
     * 获取手机验证码
     */
    private void getPhoneRand() {

        String phone = regesterPhone.getText()+"";

        if (StringUtils.isEmpty(phone)) {
            UIHelper.toastMessage(getApplicationContext(),"电话号码为空");
            return;
        }
        if (phone.length() != 11) {
            UIHelper.toastMessage(getApplicationContext(),"电话号码格式错误");
            return;
        }

        User user = new User(this);
        user.setPhone(phone);
        AsyncCall request = user.Request();
        request.setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base base, int paramInt) {
                JSONObject jsonObject = base.getResultJsonObject();
                UIHelper.toastMessage(getApplicationContext(),jsonObject.optString("message"));
                if(jsonObject != null){

                    rand = jsonObject.optString("rand");
                    regesterVerifyCode.setText(rand);

                }

            }

            @Override
            public void fail(Exception paramException) {

            }
        });

    }

    @OnClick(R.id.regester_submit)
    public void onClick() {

        String phone = regesterPhone.getText();
        String verify = regesterVerifyCode.getText() + "";
        String psw = regesterPsw.getText();

        if (verify(phone, verify, psw)) return;

        regester(phone,verify,psw);
    }

    private boolean verify(String phone, String verify, String psw) {
        //phone 验证
        if (StringUtils.isEmpty(phone)) {
            UIHelper.toastMessage(getApplicationContext(),"电话号码为空");
            return true;
        }
        if (phone.length() != 11) {
            UIHelper.toastMessage(getApplicationContext(),"电话号码格式错误");
            return true;
        }
        // 验证码 验证
        if (StringUtils.isEmpty(verify)) {
            UIHelper.toastMessage(getApplicationContext(),"短信验证码为空");
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

    private void regester(final String phone, String verify, String psw) {

        User user = new User(this);
        user.setPhone(phone);
        user.setVerify(verify);
        user.setPsw(psw);

        AsyncCall request = user.Request(User.REGESTER);
        request.setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int paramInt) {
                //注册成功 ->登陆成功
                JSONObject jsonObject = paramBase.getResultJsonObject();

                AppConfig appConfig = AppConfig.getAppConfig();
                appConfig.set(AppConfig.CONF_KEY,jsonObject.optString("key"));
                appConfig.set(AppConfig.CONF_NAME, phone);
                appConfig.set(AppConfig.CONF_ID, jsonObject.optString("userId"));
//                GoToHelp.go(RegesterActivity.this, MainActivity.class);
                finish();

                UIHelper.toastMessage(getApplicationContext(), "登陆成功");
            }

            @Override
            public void fail(Exception paramException) {
                UIHelper.toastMessage(getApplicationContext(),paramException.getMessage());
            }
        });
    }


}

