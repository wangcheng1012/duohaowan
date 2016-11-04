package com.hd.wlj.duohaowan.ui.home.classify.detailswork.presenter;

import android.app.Activity;

import com.hd.wlj.duohaowan.ui.home.classify.detailswork.contract.DetailsContract;
import com.hd.wlj.duohaowan.ui.home.classify.detailswork.model.DetailsModelImpl;
import com.wlj.base.bean.Base;
import com.wlj.base.web.asyn.AsyncCall;

import java.util.List;

/**
 * Created by wlj on 2016/10/30
 */

public class DetailsPresenterImpl implements DetailsContract.Presenter<DetailsContract.View> {

    private final DetailsModelImpl detailsModel;
    private Activity mActivity;
    private DetailsContract.View view;

    public DetailsPresenterImpl(Activity mActivity) {

        this.mActivity = mActivity;
        detailsModel = new DetailsModelImpl();

    }

    @Override
    public void attachView(DetailsContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    public void loadDetailsData(String pubid) {
        detailsModel.setId(pubid);
        detailsModel.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int paramInt) {
                view.showDetailsData(paramBase);
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }


}