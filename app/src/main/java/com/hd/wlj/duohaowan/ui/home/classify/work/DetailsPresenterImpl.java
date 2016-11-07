package com.hd.wlj.duohaowan.ui.home.classify.work;

import android.app.Activity;

import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsView;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsModelImpl;
import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.wlj.base.bean.Base;
import com.wlj.base.web.asyn.AsyncCall;

import java.util.List;

/**
 * Created by wlj on 2016/10/30
 */

public class DetailsPresenterImpl extends BasePresenter<DetailsView> {

    private final DetailsModelImpl detailsModel;
    private Activity mActivity;

    public DetailsPresenterImpl(Activity mActivity) {

        this.mActivity = mActivity;
        detailsModel = new DetailsModelImpl();

    }

    public void loadDetailsData(String pubid) {
        detailsModel.setId(pubid);
        detailsModel.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int paramInt) {
                if (view != null) {
                    view.showDetailsData(paramBase);
                }
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }


}