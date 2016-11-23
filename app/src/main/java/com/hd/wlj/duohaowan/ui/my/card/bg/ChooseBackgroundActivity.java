package com.hd.wlj.duohaowan.ui.my.card.bg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVContract;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVFragment;
import com.hd.wlj.duohaowan.ui.my.work.WorkModle;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.web.asyn.BaseAsyncModle;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChooseBackgroundActivity extends BaseFragmentActivity implements SWRVContract.SWRVPresenterAdapter {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_title)
    TextView titleTitle;

    private SWRVFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywork);
        ButterKnife.bind(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        fragment = SWRVFragment.newInstance();
        fragment.setMyInterface(new SWRVFragment.SWRVInterface() {
            @Override
            public void onCreateViewExtract(RecyclerView recyclerview, SwipeRefreshLayout swipeRefreshLayout) {
//                addHead(recyclerview);
            }

            @Override
            public SWRVContract.SWRVPresenterAdapter getPresenterAdapter() {
                return ChooseBackgroundActivity.this;
            }
        });

        transaction.add(R.id.mywork_container, fragment);
        transaction.commitAllowingStateLoss();

        initTitle();
    }

    private void initTitle() {
        titleTitle.setText("背景列表");
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getRecycerviewItemlayoutRes() {
        return R.layout.item_choosebackground;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public void convert(ViewHolder viewHolder, Base item, int position) {

        JSONObject jsonObject = item.getResultJsonObject();
        viewHolder.getConvertView().setTag(R.id.tag_first,  item);

        ImageView view = viewHolder.getView(R.id.item_choose_background);
        Glide.with(this)
                .load(Urls.HOST + jsonObject.optString("pic"))
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade(500)
                .into(view);
    }

    @Override
    public void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position, Object item) {

    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, Object item) {
        Base tag = (Base)view.getTag(R.id.tag_first);
        Intent intent = getIntent();
        intent.putExtra("base",tag);
        setResult(RESULT_OK ,intent);
        finish();
    }

    /**
     * 现在的请求是放在modle里的
     *
     * @return
     */
    @Override
    public BaseAsyncModle getBaseAsyncModle() {
        ChooseBackgroundModle workModle = new ChooseBackgroundModle(this);
        workModle.setPageSize(6);

        return workModle;
    }

    /**
     * 和 getBaseAsyncModle结合请求网络，request(Type)的Type
     *
     * @return
     */
    @Override
    public int getRequestType() {
        return -1;
    }

    /**
     * 异步请求 返回来的数据有些要先处理了才能用
     *
     * @param list
     * @param base
     * @param requestType
     * @return
     */
    @Override
    public List<Base> handleBackData(List<Base> list, Base base, int requestType) {

        return list;
    }

    /**
     * 当数据为空时显示的view
     *
     * @return
     */
    @Override
    public View getEmptyView() {
        return null;
    }

}
