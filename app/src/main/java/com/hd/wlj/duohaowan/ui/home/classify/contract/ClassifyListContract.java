package com.hd.wlj.duohaowan.ui.home.classify.contract;

import com.wlj.base.bean.Base;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by wlj on 2016/10/26.
 */

public class ClassifyListContract {


    public interface View {

        void workOfArtRecycerview(ViewHolder viewHolder, Base item, int position);

        void artistRecycerview(ViewHolder viewHolder, Base item, int position);

        void artGalleryRecycerview(ViewHolder viewHolder, Base item, int position);
    }

    public interface Presenter<V> {

        void attachView(V view);

        void detachView();

    }


}