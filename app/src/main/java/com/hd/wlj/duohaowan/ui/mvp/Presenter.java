package com.hd.wlj.duohaowan.ui.mvp;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}