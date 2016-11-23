package com.hd.wlj.duohaowan.ui.home.classify.arist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.hd.wlj.duohaowan.ui.home.classify.work.WorkDetailsActivity;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.CyptoUtils;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.web.asyn.BaseAsyncModle;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 艺术家详情页
 */
public class AristDetailsActivity extends BaseFragmentActivity implements SWRVContract.SWRVPresenterAdapter, AristDetailsView {

    private SWRVFragment fragment;
    private HeadViewHolder headerHolder;
    private AristDetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_arist_details);
//
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        presenter = new AristDetailsPresenter(this);
        presenter.attachView(this);

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

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        presenter.loadData(id);
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
        //
        holder.setText(R.id.item_home_viewcount, resultJsonObject.optInt("viewcount", 0) + "");
        holder.setText(R.id.item_home_zan, resultJsonObject.optInt("nice_count", 0) + "");
//                holder.setText(R.id.item_home_guanzhu,resultJsonObject.optString("shoucang_count"));
        holder.setText(R.id.item_home_song, resultJsonObject.optInt("", 0) + "");
    }

    @Override
    public void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position, Object item) {

    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, Object item) {
        Bundle bundle = new Bundle();
        Base base = (Base) item;
        JSONObject jsonObject = base.getResultJsonObject();
        bundle.putString("id", jsonObject.optString("pub_id"));
        GoToHelp.go(AristDetailsActivity.this, WorkDetailsActivity.class, bundle);
    }

    @Override
    public BaseAsyncModle getBaseAsyncModle() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("artist_id");

        AristDetailsModle detailsModel = new AristDetailsModle(this);
        detailsModel.setArtist_id(id);
        return detailsModel;
    }

    @Override
    public int getRequestType() {
        return -1;
    }

    /**
     * @param list
     * @param base
     * @param requestType
     * @return
     */
    @Override
    public List<Base> handleBackData(List<Base> list, Base base, int requestType) {
        return list;
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

    //______________-----------head

    private void addHead(RecyclerView recyclerview) {

        View header = LayoutInflater.from(this).inflate(R.layout.part_arist_details_head, null);
        RecyclerView.Adapter loadMoreWrapper = fragment.getAdapter();

        HeaderAndFooterWrapper headerAdapter = new HeaderAndFooterWrapper(loadMoreWrapper);
        headerAdapter.addHeaderView(header);
        recyclerview.setAdapter(headerAdapter);
        headerAdapter.notifyDataSetChanged();
        fragment.setAdapter(headerAdapter);

        headerHolder = new HeadViewHolder(header);
        headerHolder.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initHeadData(Base base) {
        JSONObject jsonObject = base.getResultJsonObject();
        Glide.with(this).load(Urls.HOST + jsonObject.optString("pic_back")).into(headerHolder.backgroundimg);

        Glide.with(this).load(Urls.HOST + jsonObject.optString("pic"))
//                    .error(R.drawable.project_bg)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(headerHolder.headimage);
        headerHolder.name.setText(jsonObject.optString("nickname", "昵称"));
        headerHolder.other.setText(jsonObject.optString("touxian") + "  " + jsonObject.optString("shi"));
        headerHolder.nice.setText(jsonObject.optString("dongtai_count"));
        headerHolder.myGuanzhu.setText(jsonObject.optString("guanzhu_count"));
        headerHolder.myFensi.setText(jsonObject.optString("shoucang_count"));
        headerHolder.intro.setText(CyptoUtils.decryptBASE64(jsonObject.optString("intro")));
    }

    @Override
    public void showData(Base base) {
        initHeadData(base);
    }

    static class HeadViewHolder {

        @BindView(R.id.p_arist_head_back)
        ImageView back;
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
        @BindView(R.id.my_intro)
        TextView intro;

        HeadViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}

