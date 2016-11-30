package com.hd.wlj.duohaowan.ui.home.classify.v2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hd.wlj.duohaowan.R;
import com.wlj.base.ui.BaseFragment;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SwipeRefreshLayout下拉刷新 和RecyclerView加载跟多实现的一个fragment，多个地方都可以调用
 */
public class SWRVFragment extends BaseFragment implements SWRVContract.View {

    @BindView(R.id.classify_list_recycerview)
    RecyclerView recycerview;
    @BindView(R.id.classify_list_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private SWRVPresenter presenter;
    private SWRVInterface myInterface;

    public SWRVFragment() {
    }

    public static SWRVFragment newInstance() {
        SWRVFragment fragment = new SWRVFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getlayout() {
        return R.layout.fragment_classify_list;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, view);

        init();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
    }

    private void init() {

        presenter = new SWRVPresenter(getActivity());
        presenter.attachView(this);

        presenter.setPresenterAdapter(myInterface.getPresenterAdapter());

        presenter.initRecycerview(recycerview, swipeRefreshLayout);

        myInterface.onCreateViewExtract(recycerview, swipeRefreshLayout);
        presenter.onRefresh();

    }

    public SWRVPresenter getPresenter() {
        return presenter;
    }

    public RecyclerView getRecycerview() {
        return recycerview;
    }

    public RecyclerView.Adapter getAdapter() {
        return presenter.getAdapter();
    }

    public void setMyInterface(SWRVInterface myInterface) {
        this.myInterface = myInterface;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        presenter.setAdapter(adapter);
    }

    public interface SWRVInterface {

        /**
         * onCreateView的扩展，可以对recyclerview、swipeRefreshLayout做补充,如recyclerview家head
         *
         * @param recyclerview
         * @param swipeRefreshLayout
         */
        void onCreateViewExtract(RecyclerView recyclerview, SwipeRefreshLayout swipeRefreshLayout);

        SWRVContract.SWRVPresenterAdapter getPresenterAdapter();
    }

}
