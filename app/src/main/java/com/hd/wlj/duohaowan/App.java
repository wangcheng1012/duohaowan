package com.hd.wlj.duohaowan;

import android.Manifest;

import com.hd.wlj.duohaowan.ui.LoginActivity;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.wlj.base.util.AppConfig;
import com.wlj.base.util.AppContext;
import com.wlj.base.util.UIHelper;

import java.util.List;

/**
 */
public class App extends AppContext {



    @Override
    protected void init() {

        //网络请求 出现301  时自动跳转到登录页面
        AppConfig.getAppConfig().setLoginClass(LoginActivity.class);
        Logger.init().methodCount(2).methodOffset(0).logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);

        //权限
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.READ_PHONE_STATE).build()
                , new AcpListener() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        UIHelper.toastMessage(getApplicationContext(), permissions.toString() + "权限拒绝");
                        System.exit(0);
                    }
                });

    }

}
