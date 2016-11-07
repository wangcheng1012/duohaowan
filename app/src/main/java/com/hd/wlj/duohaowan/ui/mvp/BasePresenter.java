package com.hd.wlj.duohaowan.ui.mvp;

/**
 * Created by wlj on 2016/11/5.
 */

public class BasePresenter<V> implements Presenter<V> {

    protected V view;

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

}
