package com.hd.wlj.duohaowan.ui.publish.complate;

import android.app.Activity;

import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.hd.wlj.duohaowan.ui.publish.publishModel;
import com.wlj.base.bean.Base;
import com.wlj.base.web.asyn.AsyncCall;

import java.util.List;

/**
 * Created by wlj on 2016/11/10.
 */

public class ComplatePresenter extends BasePresenter<ComplateView> {


    private final publishModel borderModel;
    private Activity activity;

    public ComplatePresenter(Activity activity) {
        this.activity = activity;
        borderModel = new publishModel();
    }

    public void loadCardData() {
        borderModel.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                if (view != null) {
                    view.showData(list);
                }
            }

            @Override
            public void fail(Exception paramException) {

            }
        });

    }
}
