package com.hd.wlj.duohaowan.ui.home.classify.arist;

import android.app.Activity;

import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsModelImpl;
import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.hd.wlj.duohaowan.ui.mvp.Presenter;
import com.wlj.base.bean.Base;
import com.wlj.base.web.asyn.AsyncCall;

import java.util.List;

/**
 * Created by wlj on 2016/11/7.
 */

public class AristDetailsPresenter extends BasePresenter<AristDetailsView> {


    private final DetailsModelImpl detailsModel;
    private Activity mActivity;

    public AristDetailsPresenter(Activity mActivity) {
        this.mActivity = mActivity;
        detailsModel = new DetailsModelImpl(mActivity);

    }


    public void loadData(String id) {
        detailsModel.setId(id);
        detailsModel.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                if(view != null ){
                    view.showData(base);
                }
            }

            @Override
            public void fail(Exception paramException) {

            }
        });

    }

}
