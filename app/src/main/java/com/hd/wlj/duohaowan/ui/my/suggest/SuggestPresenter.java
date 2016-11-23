package com.hd.wlj.duohaowan.ui.my.suggest;

import android.app.Activity;

import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.wlj.base.bean.Base;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by wlj on 2016/11/16.
 */

public class SuggestPresenter extends BasePresenter<SuggestView> {

    private final SuggestModle suggestModle;
    private Activity mActivity;

    public SuggestPresenter(Activity mActivity){

        this.mActivity = mActivity;
          suggestModle = new SuggestModle(mActivity);
    }


    public void loadData(String title, String content) {

        suggestModle.setProblem(title);
        suggestModle.setContent(content);
        suggestModle.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                JSONObject jsonObject = base.getResultJsonObject();
                UIHelper.toastMessage(mActivity,jsonObject.optString("message"));
                mActivity.finish();
            }

            @Override
            public void fail(Exception paramException) {

            }
        });

    }
}
