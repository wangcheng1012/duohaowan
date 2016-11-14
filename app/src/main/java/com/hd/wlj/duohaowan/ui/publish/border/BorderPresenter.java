package com.hd.wlj.duohaowan.ui.publish.border;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.wlj.base.bean.Base;
import com.wlj.base.web.asyn.AsyncCall;

import java.util.List;

/**
 * Created by wlj on 2016/11/10.
 */

public class BorderPresenter extends BasePresenter<BorderView> {


    private final BorderModel borderModel;
    private Activity activity;

    public BorderPresenter(Activity activity) {
        this.activity = activity;
        borderModel = new BorderModel();
    }

    public void loadBorderData(String rootPubConlumnId) {
        borderModel.setRootPubConlumnId(rootPubConlumnId);
        borderModel.Request()
                .setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                    @Override
                    public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                        if (view != null) {
                            view.showBorder(list);
                        }
                    }

                    @Override
                    public void fail(Exception paramException) {

                    }
                });

    }
}
