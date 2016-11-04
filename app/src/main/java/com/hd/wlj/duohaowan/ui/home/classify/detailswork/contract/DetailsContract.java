package com.hd.wlj.duohaowan.ui.home.classify.detailswork.contract;

import com.wlj.base.bean.Base;

/**
 * Created by wlj on 2016/10/26.
 */

public class DetailsContract {


    public interface View {
        void showDetailsData(Base paramBase);
    }

    public interface Presenter<V> {
        void attachView(V view);

        void detachView();
    }


}