package com.hd.wlj.duohaowan.ui.home.classify.v2;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wlj.base.bean.Base;
import com.wlj.base.web.asyn.BaseAsyncModle;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by wlj on 2016/11/7.
 */

public class SWRVContract {

    public interface View {
    }

    public interface SWRVPresenterAdapter {

        int getRecycerviewItemlayoutRes();

        RecyclerView.LayoutManager getLayoutManager();

        void convert(ViewHolder viewHolder, Base item, int position);

        void onItemLongClick(android.view.View view, RecyclerView.ViewHolder holder, int position, Object item);

        void onItemClick(android.view.View view, RecyclerView.ViewHolder holder, int position, Object item);


        BaseAsyncModle getBaseAsyncModle();

        /**
         * 和 getBaseAsyncModle结合请求网络，request(Type)的Type
         *
         * @return
         */
        int getRequestType();

        /**
         * 异步请求 返回来的数据有些要先处理了才能用
         *
         * @param list
         * @param base
         * @param requestType
         * @return
         */
        List<Base> handleBackData(List<Base> list, Base base, int requestType);

        /**
         * 当数据为空时显示的view
         *
         * @return
         */
        android.view.View getEmptyView();
    }


}