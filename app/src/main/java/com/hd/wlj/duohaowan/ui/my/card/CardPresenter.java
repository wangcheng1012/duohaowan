package com.hd.wlj.duohaowan.ui.my.card;

import android.app.Activity;

import com.hd.wlj.duohaowan.been.User;
import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.wlj.base.bean.Base;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import java.util.List;

/**
 * Created by wlj on 2016/11/16.
 */

public class CardPresenter extends BasePresenter<CardView> {

    private final CardModle cardModle;
    private Activity mActivity;

    public CardPresenter(Activity mActivity){

        this.mActivity = mActivity;
         cardModle = new CardModle(mActivity);
    }

    public void save(User user) {
        cardModle.setUser(user);
        AsyncCall request = cardModle.Request();
        request.setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                UIHelper.toastMessage(mActivity,"保存成功");
                mActivity.finish();
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }
}
