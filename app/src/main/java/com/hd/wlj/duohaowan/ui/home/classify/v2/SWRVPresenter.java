package com.hd.wlj.duohaowan.ui.home.classify.v2;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.wlj.base.bean.Base;
import com.wlj.base.util.AppContext;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;
import com.wlj.base.web.asyn.BaseAsyncModle;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SWRVPresenter extends BasePresenter<SWRVContract.View> {

    private Activity mActivity;
    private RecyclerView recycerview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Base> datas;
    private AsyncCall asyncCall;
    private TextView loadmoretext;

    private SWRVContract.SWRVPresenterAdapter presenterAdapter;
    private BaseAsyncModle modle;
    private RecyclerView.Adapter adapter;

    public SWRVPresenter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void initRecycerview(RecyclerView recycerview, SwipeRefreshLayout swipeRefreshLayout) {

        this.recycerview = recycerview;
        this.swipeRefreshLayout = swipeRefreshLayout;

        if (presenterAdapter == null) {
            throw new RuntimeException("presenterAdapter 不能为空");
        }
        int itemlayout = presenterAdapter.getRecycerviewItemlayoutRes();

        if (itemlayout == 0) {
            throw new RuntimeException("recycerview的item layout 不能为空");
        }

        RecyclerView.LayoutManager layoutManager = presenterAdapter.getLayoutManager();

        if (layoutManager == null) {
            throw new RuntimeException("layoutManager 不能为空");
        }

        recycerview.setLayoutManager(layoutManager);

        recycerview(itemlayout);
    }

    private void recycerview(int layout) {

        datas = new ArrayList<>();
        CommonAdapter<Base> commonAdapter = new CommonAdapter<Base>(mActivity, layout, datas) {

            @Override
            protected void convert(ViewHolder viewHolder, Base item, int position) {
                if (view == null) return;
                viewHolder.getConvertView().setTag(R.id.tag_first, item);

                presenterAdapter.convert(viewHolder, item, position);
            }
        };

        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                Object tag = view.getTag(R.id.tag_first);

                if (tag != null) {

                    presenterAdapter.onItemClick(view, holder, position, tag);
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                Object tag = view.getTag(R.id.tag_first);

                if (tag != null) {
                    presenterAdapter.onItemLongClick(view, holder, position, tag);
                }
                return false;
            }
        });

        //空 EmptyWrapper
        View emptyView = presenterAdapter.getEmptyView();
        if (emptyView == null) {
            TextView empty = new TextView(mActivity);
            empty.setText("点击刷新");
            empty.setTextColor(Color.BLACK);
            empty.setGravity(Gravity.CENTER);
            empty.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
            emptyView = empty;
        }
        EmptyWrapper emptyWrapper = new EmptyWrapper(commonAdapter);
        emptyWrapper.setEmptyView(emptyView);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        //loadMoreWrapper
        loadmoretext = new TextView(mActivity);
//        loadmoretext.setText(" ");
        loadmoretext.setTextColor(Color.BLACK);
        loadmoretext.setGravity(Gravity.CENTER);
        loadmoretext.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        LoadMoreWrapper loadMoreWrapper = new LoadMoreWrapper(emptyWrapper, recycerview);
        loadMoreWrapper.setLoadMoreView(loadmoretext);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!asyncCall.isComplate()) {
                    loadMore();
                }
            }
        });
        adapter = loadMoreWrapper;
        recycerview.setAdapter(adapter);
    }//Recycerview  end

    private void loadMore() {

        loadData(asyncCall.getPageIndex() + 1);
    }

    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData(1);
    }

    private void loadData(int page) {

        modle = presenterAdapter.getBaseAsyncModle();

        if (modle == null) {
            throw new RuntimeException("网路请求modle 不能为空");
        }

        modle.setPage(page);
        asyncCall = modle.Request(presenterAdapter.getRequestType());
        asyncCall.setShowToast(false);

        loadBack();
    }

    private void loadBack() {
        asyncCall.setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int requesttype) {
                //以此判定为刷新 就清除原油数据
                if (asyncCall.getPageIndex() == 1) {
                    datas.clear();
                }
                List<Base> list = presenterAdapter.handleBackData(paramList, paramBase, requesttype);

                if (list == null) {
                    throw new RuntimeException("list==null");
                }
                datas.addAll(list);
                adapter.notifyDataSetChanged();
                loadComplate();

//                if(asyncCall.getPageIndex() == 1 && paramList.size() == 0){
//                    //数据为空
//                    loadmoretext.setText("数据为空");
//                }

                if (asyncCall.isComplate() && datas.size() != 0) {
                    //加载完
                    loadmoretext.setText("已经到底了");
                }
            }

            @Override
            public void fail(Exception paramException) {

                if ((paramException instanceof SocketTimeoutException)) {
                    UIHelper.toastMessage(AppContext.getAppContext(), " 链接超时");
                } else {
                    UIHelper.toastMessage(AppContext.getAppContext(), paramException.getMessage());
                }
                loadComplate();
                loadmoretext.setText("异常");
            }
        });
    }

    public void loadComplate() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void onRefresh(BaseAsyncModle modle) {
        this.modle = modle;
        onRefresh();
    }

    public SWRVContract.SWRVPresenterAdapter getPresenterAdapter() {
        return presenterAdapter;
    }

    public void setPresenterAdapter(SWRVContract.SWRVPresenterAdapter presenterAdapter) {
        this.presenterAdapter = presenterAdapter;
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }
}