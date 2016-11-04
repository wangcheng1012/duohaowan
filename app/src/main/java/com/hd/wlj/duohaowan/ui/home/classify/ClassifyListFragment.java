package com.hd.wlj.duohaowan.ui.home.classify;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.home.classify.contract.ClassifyListContract;
import com.hd.wlj.duohaowan.ui.home.classify.presenter.ClassifyListPresenterImpl;
import com.wlj.base.bean.Base;
import com.wlj.base.util.img.LoadImage;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 */
public class ClassifyListFragment extends Fragment implements ClassifyListContract.View {


    @BindView(R.id.classify_list_recycerview)
    RecyclerView recycerview;
    @BindView(R.id.classify_list_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String tabBarStr;
    private int classify;
    private ClassifyListPresenterImpl presenter;

    public ClassifyListFragment() {
    }

    public static Fragment newInstance(String tabBarStr, int classify) {

        ClassifyListFragment classifyListFragment = new ClassifyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tabBarStr", tabBarStr);
        bundle.putInt("classify", classify);
        classifyListFragment.setArguments(bundle);

        return classifyListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify_list, container, false);
        ButterKnife.bind(this, view);

        init();
        initSwipeRefreshLayout();
        return view;
    }

    private void initSwipeRefreshLayout() {

        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
    }

    private void init() {
        Bundle arguments = getArguments();

        presenter = new ClassifyListPresenterImpl(getActivity(), arguments);
        presenter.attachView(this);

        presenter.initRecycerview(recycerview, swipeRefreshLayout);

        presenter.onRefresh();
    }

    /**
     * 艺术家 Recycerview
     *
     * @param viewHolder
     * @param item
     * @param position
     */
    @Override
    public void artistRecycerview(ViewHolder viewHolder, Base item, int position) {

    }

    /**
     * 艺术馆
     *
     * @param viewHolder
     * @param item
     * @param position
     */
    @Override
    public void artGalleryRecycerview(ViewHolder viewHolder, Base item, int position) {

    }

    /**
     * 艺术品 Recycerview
     *
     * @param viewHolder
     * @param item
     * @param position
     */
    @Override
    public void workOfArtRecycerview(ViewHolder viewHolder, Base item, int position) {

        JSONObject jsonObject = item.getResultJsonObject();

        JSONObject artist = jsonObject.optJSONObject("artist");

        viewHolder.setText(R.id.classify_workofart_intro, jsonObject.optString("name"));
        viewHolder.setText(R.id.classify_workofart_zan, jsonObject.optInt("nice_count", 0) + "");
        viewHolder.setText(R.id.classify_workofart_workname, jsonObject.optString("showTag_list"));
        viewHolder.setText(R.id.classify_workofart_name, artist != null ? artist.optString("realname") : "");

        ImageView head = viewHolder.getView(R.id.classify_workofart_head);
        ImageView image = viewHolder.getView(R.id.classify_workofart_image);

        Glide.with(ClassifyListFragment.this)
                .load(artist != null ? Urls.HOST + artist.optString("picname") : R.drawable.project_bg)
                .placeholder(R.drawable.project_bg)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .crossFade(1000).into(head);

        LoadImage.getinstall().addTask(Urls.HOST + jsonObject.optString("pic"), image);
        LoadImage.getinstall().doTask();
    }

}
