package com.hd.wlj.duohaowan.ui.publish.card;

import android.app.Activity;

import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.wlj.base.bean.Base;
import com.wlj.base.web.asyn.AsyncCall;

import java.util.List;

/**
 * Created by wlj on 2016/11/10.
 */

public class CardPresenter extends BasePresenter<CardView> {


    private final CardModel borderModel;
    private Activity activity;

    public CardPresenter(Activity activity) {
        this.activity = activity;
        borderModel = new CardModel();
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
