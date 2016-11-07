package com.hd.wlj.duohaowan;

import com.hd.wlj.duohaowan.ui.LoginActivity;
import com.wlj.base.util.AppConfig;
import com.wlj.base.util.AppContext;

/**
 */
public class App extends AppContext {

    @Override
    protected void init() {
        //网络请求 出现301  时自动跳转到登录页面
        AppConfig.getAppConfig().setLoginClass(LoginActivity.class);
    }

}
