package com.hd.wlj.duohaowan.ui.home.classify.presenter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.home.classify.contract.ClassifyListContract;
import com.hd.wlj.duohaowan.ui.home.classify.detailswork.WorkDetailsActivity;
import com.hd.wlj.duohaowan.ui.home.classify.model.ClassifyListModelImpl;
import com.wlj.base.bean.Base;
import com.wlj.base.util.AppContext;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlj on 2016/11/03
 */

public class ClassifyListPresenterImpl implements ClassifyListContract.Presenter<ClassifyListContract.View> {

    private final String tabBarStr;
    private final int classify;
    private final ClassifyListModelImpl classifyListModel;
    private ClassifyListContract.View view;
    private Activity mActivity;
    private RecyclerView recycerview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Base> datas;
    private LoadMoreWrapper loadMoreWrapper;
    private AsyncCall asyncCall;
    private TextView loadmoretext;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    public ClassifyListPresenterImpl(Activity mActivity, Bundle arguments) {

        this.mActivity = mActivity;
        classifyListModel = new ClassifyListModelImpl(mActivity);
        tabBarStr = arguments.getString("tabBarStr");
        classify = arguments.getInt("classify", -1);
    }

    @Override
    public void attachView(ClassifyListContract.View view) {

        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    public void initRecycerview(RecyclerView recycerview, SwipeRefreshLayout swipeRefreshLayout) {

        this.recycerview = recycerview;
        this.swipeRefreshLayout = swipeRefreshLayout;

        int item = R.layout.item_classify_workofart;//默认 艺术品 瀑布流
        switch (classify) {

            case R.id.home_artist:
                //艺术家
                recycerview.setLayoutManager(new LinearLayoutManager(mActivity));
                break;
            case R.id.home_artgallery:
                //艺术馆
                recycerview.setLayoutManager(new LinearLayoutManager(mActivity));
                break;
            case R.id.home_workofart:
                //艺术品 瀑布流
                staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recycerview.setLayoutManager(staggeredGridLayoutManager);
                item = R.layout.item_classify_workofart;
                break;
            case R.id.home_artview:
                //艺术观

                break;
        }
        recycerview(classify, item);
    }

    private void recycerview(final int classify, int layout) {

        datas = new ArrayList<>();
        CommonAdapter<Base> commonAdapter = new CommonAdapter<Base>(mActivity, layout, datas) {

            @Override
            protected void convert(ViewHolder viewHolder, Base item, int position) {

                switch (classify) {

                    case R.id.home_artist:
                        //艺术家
                        view.artistRecycerview(viewHolder, item, position);
                        break;
                    case R.id.home_artgallery:
                        //艺术馆
                        view.artGalleryRecycerview(viewHolder, item, position);
                        break;
                    case R.id.home_workofart:
                        //艺术品
                        view.workOfArtRecycerview(viewHolder, item, position);
                        //瀑布流
                        break;
                }

            }
        };

        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Bundle bundle = new Bundle();
                Base base = datas.get(position);
                bundle.putString("json", base.getResultStr());
                GoToHelp.go(mActivity, WorkDetailsActivity.class, bundle);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        //空 EmptyWrapper
        TextView empty = new TextView(mActivity);
        empty.setText("数据为空\n点击刷新");
        empty.setTextColor(Color.BLACK);
        empty.setGravity(Gravity.CENTER);
        empty.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));

        EmptyWrapper emptyWrapper = new EmptyWrapper(commonAdapter);
        emptyWrapper.setEmptyView(empty);
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        //loadMoreWrapper
        loadmoretext = new TextView(mActivity);
//        loadmoretext.setText("加载更多");
        loadmoretext.setTextColor(Color.BLACK);
        loadmoretext.setGravity(Gravity.CENTER);
        loadmoretext.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        loadMoreWrapper = new LoadMoreWrapper(emptyWrapper, recycerview);
        loadMoreWrapper.setLoadMoreView(loadmoretext);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });

        recycerview.setAdapter(loadMoreWrapper);
    }

    private void loadMore() {

        loadData(asyncCall.getPageIndex() + 1);
    }

    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData(1);
    }

    private void loadData(int page) {
        classifyListModel.setPage(page);
        classifyListModel.setTabBarStr(tabBarStr);
        classifyListModel.setClassify(classify);

        asyncCall = classifyListModel.Request();
        asyncCall.setShowToast(false);
        asyncCall.setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int paramInt) {

                if (asyncCall.getPageIndex() == 1) {
                    datas.clear();
                }
                datas.addAll(paramList);
                loadMoreWrapper.notifyDataSetChanged();
                loadComplate();

//                if(asyncCall.getPageIndex() == 1 && paramList.size() == 0){
//                    //数据为空
//                    loadmoretext.setText("数据为空");
//                }

                if (asyncCall.isComplate() && datas.size() != 0) {
                    //加载完
//                    loadmoretext.setText("已是最底部了");
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
}