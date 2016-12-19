package com.hd.wlj.duohaowan.ui.home.classify;

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
import com.hd.wlj.duohaowan.ui.home.classify.arist.AristDetailsActivity;
import com.hd.wlj.duohaowan.ui.home.classify.artview.ArtViewAskedandLearnActivity;
import com.hd.wlj.duohaowan.ui.home.classify.artview.ArtViewHistoryActivity;
import com.hd.wlj.duohaowan.ui.home.classify.artview.ArtViewRealityActivity;
import com.hd.wlj.duohaowan.ui.home.classify.gallery.GalleryActivity;
import com.hd.wlj.duohaowan.ui.home.classify.work.WorkDetailsActivity;
import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.wlj.base.bean.Base;
import com.wlj.base.util.AppContext;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.MsgContext;
import com.wlj.base.web.asyn.AsyncCall;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlj on 2016/11/03
 */

public class ClassifyListPresenter extends BasePresenter<ClassifyListView> {

    private final String tabBarStr;
    private final int classify;
    private final ClassifyListModel mSWRVModel;
    private Activity mActivity;
    private RecyclerView recycerview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Base> datas;
    private LoadMoreWrapper loadMoreWrapper;
    private AsyncCall asyncCall;
    private TextView loadmoretext;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String seachType;
    private String seachText;

    public ClassifyListPresenter(Activity mActivity, Bundle arguments) {

        this.mActivity = mActivity;
        mSWRVModel = new ClassifyListModel(mActivity);
        tabBarStr = arguments.getString("tabBarStr");
        classify = arguments.getInt("classify", -1);
    }

    // initRecycerview
    public void initRecycerview(RecyclerView recycerview, SwipeRefreshLayout swipeRefreshLayout) {

        this.recycerview = recycerview;
        this.swipeRefreshLayout = swipeRefreshLayout;

        int itemlayout = R.layout.item_classify_workofart;//默认 艺术品 瀑布流
        switch (classify) {

            case R.id.home_artist:
                //艺术家
                recycerview.setLayoutManager(new LinearLayoutManager(mActivity));
                itemlayout = R.layout.item_classify_artist;
                break;
            case R.id.home_artgallery:
                //艺术馆
                recycerview.setLayoutManager(new LinearLayoutManager(mActivity));
                itemlayout = R.layout.item_classify_artgallery;
                break;
            case R.id.home_workofart:
                //艺术品 瀑布流
                staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recycerview.setLayoutManager(staggeredGridLayoutManager);
                itemlayout = R.layout.item_classify_workofart;
                break;
            case R.id.home_artview:
                //艺术观
                recycerview.setLayoutManager(new LinearLayoutManager(mActivity));
                if ("史学篇".equals(tabBarStr)) {
                    itemlayout = R.layout.item_classify_artview_history;
                } else if ("现实篇".equals(tabBarStr)) {
                    itemlayout = R.layout.item_classify_artview_reality;
                } else {
                    //问学篇
                    itemlayout = R.layout.item_classify_artview_askandlearn;
                }

                break;
            case ClassifyListModel.request_type_seach:
                // 这里写  只能是默认，每次搜索都可能不同，所以放到setseachtype
                if (MsgContext.seachArtist.equals(seachType)) {
                    //艺术家 和上面的艺术家一样
                    recycerview.setLayoutManager(new LinearLayoutManager(mActivity));
                    itemlayout = R.layout.item_classify_artist;
                } else {
//                    seachType = MsgContext.seachWork;
                    //默认是 艺术品 和上面的艺术品一样
                    staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    recycerview.setLayoutManager(staggeredGridLayoutManager);
                    itemlayout = R.layout.item_classify_workofart;
                }
                break;

        }
        recycerview(classify, itemlayout);
    }

    private void recycerview(final int classify, int layout) {

        //item create
        datas = new ArrayList<>();
        CommonAdapter<Base> commonAdapter = new CommonAdapter<Base>(mActivity, layout, datas) {

            @Override
            protected void convert(ViewHolder viewHolder, Base item, int position) {
                if (view == null) return;
                viewHolder.getConvertView().setTag(R.id.tag_first, item);

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
                    case R.id.home_artview:
                        //艺术观
                        if ("史学篇".equals(tabBarStr)) {
                            view.artviewHistoryRecycerview(viewHolder, item, position);
                        } else if ("现实篇".equals(tabBarStr)) {
                            view.artviewRealityRecycerview(viewHolder, item, position);
                        } else {
                            //问学篇
                            view.artviewAskandLearnRecycerview(viewHolder, item, position);
                        }
                        break;

                    case ClassifyListModel.request_type_seach:
                        //搜索
                        if (MsgContext.seachArtist.equals(seachType)) {
                            //艺术家 和上面的艺术家一样
                            view.artistRecycerview(viewHolder, item, position);
                        } else
//                              if (MsgContext.seachWork.equals(seachType))
                        {
                            //艺术品 和上面的艺术品一样
                            view.workOfArtRecycerview(viewHolder, item, position);
                        }
                        break;
                }

            }
        };
        //Click
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Object tag = view.getTag(R.id.tag_first);

                if (tag != null) {
                    Class cla = WorkDetailsActivity.class;
                    switch (classify) {

                        case R.id.home_artist:
                            //艺术家
                            cla = AristDetailsActivity.class;
                            break;
                        case R.id.home_artgallery:
                            //艺术馆
                            cla = GalleryActivity.class;
                            break;
                        case R.id.home_workofart:
                            //艺术品 瀑布流
                            cla = WorkDetailsActivity.class;
                            break;
                        case R.id.home_artview:
                            //艺术观
                            if ("史学篇".equals(tabBarStr)) {
                                cla = ArtViewHistoryActivity.class;

                            } else if ("现实篇".equals(tabBarStr)) {
                                cla = ArtViewRealityActivity.class;
                            } else {
                                cla = ArtViewAskedandLearnActivity.class;
                            }
                            break;
                        case ClassifyListModel.request_type_seach:
                            // 这里写  只能是默认，每次搜索都可能不同，所以放到setseachtype
                            if (MsgContext.seachArtist.equals(seachType)) {
                                //艺术家 和上面的艺术家一样
                                cla = AristDetailsActivity.class;
                            } else {
                                seachType = MsgContext.seachWork;
                                //默认是 艺术品 和上面的艺术品一样
                                cla = WorkDetailsActivity.class;
                            }
                            break;
                    }

                    Bundle bundle = new Bundle();
                    Base base = (Base) tag;
                    JSONObject jsonObject = base.getResultJsonObject();
                    bundle.putString("id", jsonObject.optString("pub_id"));
                    bundle.putString("artist_id", jsonObject.optString("artist_id"));

                    GoToHelp.go(mActivity, cla, bundle);
                }

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

                if (asyncCall != null && !asyncCall.isComplate()) {
                    loadMore();
                }
            }
        });

        recycerview.setAdapter(loadMoreWrapper);
    }//Recycerview  end

    private void loadMore() {

        loadData(asyncCall.getPageIndex() + 1);
    }

    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData(1);
    }

    private void loadData(int page) {

        mSWRVModel.setPage(page);
        mSWRVModel.setPageSize(6);

        if (classify == ClassifyListModel.request_type_seach) {
            // 搜索
            mSWRVModel.setSeachName(seachText);
            mSWRVModel.setSeachType(seachType);
            asyncCall = mSWRVModel.Request(ClassifyListModel.request_type_seach);
        } else {
            mSWRVModel.setTabBarStr(tabBarStr);
            mSWRVModel.setClassify(classify);
            asyncCall = mSWRVModel.Request();
        }
        asyncCall.setShowToast(false);

        loadBack();
    }

    private void loadBack() {
        asyncCall.setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int paramInt) {

                if (asyncCall.getPageIndex() == 1) {
                    datas.clear();
                }
                datas.addAll(paramList);
                paramList = null;
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

    public void setSeachType(String seachType) {
//        if(!seachType.equals(seachType)  ){
        this.seachType = seachType;
        initRecycerview(recycerview, swipeRefreshLayout);
//        }
    }

    public void setSeachText(String seachText) {
        this.seachText = seachText;
    }
}