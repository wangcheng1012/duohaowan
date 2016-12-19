package com.hd.wlj.duohaowan.ui.my.card.bg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVContract;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVFragment;
import com.hd.wlj.duohaowan.util.TakePhotoCrop;
import com.jph.takephoto.model.TResult;
import com.lling.photopicker.PhotoPickerActivity;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.web.asyn.BaseAsyncModle;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChooseBackgroundActivity extends BaseFragmentActivity implements SWRVContract.SWRVPresenterAdapter, TakePhotoCrop.CropBack {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.local_pic)
    Button localPic;

    private SWRVFragment fragment;
    private TakePhotoCrop takePhotoCrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        takePhotoCrop = new TakePhotoCrop(this, this);
        takePhotoCrop.getTakePhoto().onCreate(savedInstanceState);

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
        localPic.setVisibility(View.VISIBLE);
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
        viewHolder.getConvertView().setTag(R.id.tag_first,jsonObject.optString("pic"));

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
        String picpath = (String) view.getTag(R.id.tag_first);

        Intent intent = getIntent();
        intent.putExtra("picpath", picpath);
        setResult(RESULT_OK, intent);
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

    @OnClick(R.id.local_pic)
    public void onClick() {
        takePhotoCrop.photoPicker();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        takePhotoCrop.getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        takePhotoCrop.getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TakePhotoCrop.IMAGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> uristr = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT_URLSTR);

                takePhotoCrop.onCrop(Uri.parse(uristr.get(0)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePhotoCrop.onRequestPermissionsResult_(requestCode, permissions, grantResults);
    }

    /**
     * 裁剪返回
     *
     * @param result
     */
    @Override
    public void cropback(TResult result) {
        String picpath = result.getImage().getPath();
        Intent intent = getIntent();
        intent.putExtra("picpath", picpath);
        intent.putExtra("local", true);
        setResult(RESULT_OK, intent);
        finish();

    }
}
