package com.hd.wlj.duohaowan.ui.home.contract;

import com.wlj.base.bean.Base;

import java.util.List;

/**
 * Created by wlj on 2016/10/26.
 */

public class HomeContract {

    public interface View {

        void showBanner(List<Base> list);

        void showNews(Base news);

        void showHot(List<Base> hot);

        void showNewest(List<Base> newest);
    }

    public interface Presenter<V> {

        void attachView(V view);

        void detachView();

    }

    public interface Model {

    }


}