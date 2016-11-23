package com.hd.wlj.duohaowan.ui.home.classify;

import com.wlj.base.bean.Base;
import com.zhy.adapter.recyclerview.base.ViewHolder;


public interface ClassifyListView {

    void workOfArtRecycerview(ViewHolder viewHolder, Base item, int position);

    void artistRecycerview(ViewHolder viewHolder, Base item, int position);

    void artGalleryRecycerview(ViewHolder viewHolder, Base item, int position);


    void artviewHistoryRecycerview(ViewHolder viewHolder, Base item, int position);

    void artviewRealityRecycerview(ViewHolder viewHolder, Base item, int position);
}


