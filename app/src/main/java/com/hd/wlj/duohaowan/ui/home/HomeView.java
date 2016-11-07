package com.hd.wlj.duohaowan.ui.home;

import com.wlj.base.bean.Base;

import java.util.List;

public interface HomeView {

    void showBanner(List<Base> list);

    void showNews(Base news);

    void showHot(List<Base> hot);

    void showNewest(List<Base> newest);
}