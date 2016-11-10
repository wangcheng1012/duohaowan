package com.hd.wlj.duohaowan.ui.home.classify.arist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVContract;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVFragment;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsModelImpl;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.web.asyn.BaseAsyncModle;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class AristDetailsActivity extends BaseFragmentActivity implements SWRVContract.SWRVPresenterAdapter {


    private SWRVFragment fragment;
    private HeadViewHolder headerHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_arist_details);
//
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        fragment = SWRVFragment.newInstance();
        fragment.setMyInterface(new SWRVFragment.SWRVInterface() {
            @Override
            public void onCreateViewExtract(RecyclerView recyclerview, SwipeRefreshLayout swipeRefreshLayout) {
                addHead(recyclerview);
            }

            @Override
            public SWRVContract.SWRVPresenterAdapter getPresenterAdapter() {
                return AristDetailsActivity.this;
            }
        });

        transaction.add(R.id.arist_details, fragment);
        transaction.commitAllowingStateLoss();

    }

    @Override
    public int getRecycerviewItemlayoutRes() {
        return R.layout.item_home_recyclerview;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getApplicationContext());
    }

    @Override
    public void convert(ViewHolder holder, Base item, int position) {

        JSONObject resultJsonObject = item.getResultJsonObject();
        final ImageView view = holder.getView(R.id.item_home_recyclerView_image);
        holder.setImageResource(R.id.item_home_recyclerView_image, R.drawable.project_bg);
        Glide.with(AristDetailsActivity.this)
                .load(Urls.HOST + resultJsonObject.optString("pic"))
                .placeholder(R.drawable.project_bg)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {

                        int intrinsicWidth = resource.getIntrinsicWidth();
                        int height = view.getWidth() * resource.getIntrinsicHeight() / intrinsicWidth;

                        view.setMinimumWidth(intrinsicWidth);
                        view.setMinimumHeight(height);

                        view.setImageDrawable(resource);
                    }
                });

        ImageButton fat_in = holder.getView(R.id.home_fat_in);
        LinearLayout fat_out = holder.getView(R.id.home_fat_out);

        fat_out.setVisibility(View.GONE);
        fat_in.setVisibility(View.GONE);


    }

    @Override
    public void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position, Object item) {

    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, Object item) {

    }

    @Override
    public BaseAsyncModle getBaseAsyncModle() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        DetailsModelImpl detailsModel = new DetailsModelImpl();
        detailsModel.setId(id);
        return detailsModel;
    }

    @Override
    public int getRequestType() {
        return -1;
    }

    @Override
    public List<Base> handleBackData(List<Base> list, Base base, int requestType) {

        JSONObject jsonObject = base.getResultJsonObject();
        JSONArray jsonArray = jsonObject.optJSONArray("");

        List<Base> bases = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                bases.add(new Base(jsonArray.optJSONObject(i)) {
                    @Override
                    public Base parse(JSONObject jsonObject) throws JSONException {
                        return null;
                    }
                });
            }
        }
        initHeadData(base);

        return bases;
    }

    @Override
    public View getEmptyView() {
        TextView empty = new TextView(this);
        empty.setText("暂无数据");
        empty.setTextColor(Color.BLACK);
//      empty.setGravity(Gravity.CENTER);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        params.leftMargin = DpAndPx.dpToPx(this, 10);
        params.topMargin = DpAndPx.dpToPx(this, 10);
        empty.setLayoutParams(params);

        return empty;
    }

    private void addHead(RecyclerView recyclerview) {

        View header = LayoutInflater.from(this).inflate(R.layout.part_arist_details_head, null);
        RecyclerView.Adapter loadMoreWrapper = fragment.getLoadMoreWrapper();

        HeaderAndFooterWrapper headerAdapter = new HeaderAndFooterWrapper(loadMoreWrapper);
        headerAdapter.addHeaderView(header);
        recyclerview.setAdapter(headerAdapter);
        headerAdapter.notifyDataSetChanged();

        headerHolder = new HeadViewHolder(header);
    }

    private void initHeadData(Base base) {
        JSONObject jsonObject = base.getResultJsonObject();
        Glide.with(this).load(Urls.HOST + jsonObject.optString("pic_back")).into(headerHolder.backgroundimg);

        Glide.with(this).load(Urls.HOST + jsonObject.optString("pic"))
//                    .error(R.drawable.project_bg)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(headerHolder.headimage);
    }

    static class HeadViewHolder {
        @BindView(R.id.arist_details_head_backgroundimg)
        ImageView backgroundimg;
        @BindView(R.id.arist_details_headimage)
        ImageView headimage;
        @BindView(R.id.arist_details_name)
        TextView name;
        @BindView(R.id.arist_details_other)
        TextView other;
        @BindView(R.id.my_dy)
        TextView nice;
        @BindView(R.id.my_guanzhu)
        TextView myGuanzhu;
        @BindView(R.id.my_fensi)
        TextView myFensi;

        HeadViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}

