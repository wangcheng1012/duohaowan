package com.hd.wlj.duohaowan.ui.my.work;

import android.content.DialogInterface;
import android.os.Build;
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
import com.hd.wlj.duohaowan.view.MyDialogFragment;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;
import com.wlj.base.web.asyn.BaseAsyncModle;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkActivity extends BaseFragmentActivity implements SWRVContract.SWRVPresenterAdapter {

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
            }

            @Override
            public SWRVContract.SWRVPresenterAdapter getPresenterAdapter() {
                return WorkActivity.this;
            }
        });

        transaction.add(R.id.mywork_container, fragment);
        transaction.commitAllowingStateLoss();

        initTitle();
    }

    private void initTitle() {
        titleTitle.setText("我的作品");
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getRecycerviewItemlayoutRes() {
        return R.layout.item_mywork;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public void convert(ViewHolder viewHolder, Base item, int position) {

        final JSONObject jsonObject = item.getResultJsonObject();

        String showTime = jsonObject.optString("showTime");
        long time = StringUtils.getTime(showTime);
        String dd = StringUtils.getTime(time, "dd");

        ////(1/已发布)/(2/未发布)
//        int state = jsonObject.optInt("state", 2);
        //已发布

        //删除
         viewHolder.getView(R.id.i_mywork_del).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                //

                 UIHelper.dialog(WorkActivity.this, "确认删除?", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                         WorkModle workModle = new WorkModle(WorkActivity.this);
                         workModle.setId(jsonObject.optString("pub_id"));
                         workModle.Request(WorkModle.type_del).setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                             @Override
                             public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                                 JSONObject jo = base.getResultJsonObject();
                                 UIHelper.toastMessage(WorkActivity.this,jo.optString("message"));

                                 fragment.getPresenter().onRefresh();
                             }

                             @Override
                             public void fail(Exception paramException) {

                             }
                         });

                     }
                 },null);


             }
         });

        viewHolder.setText(R.id.i_mywork_day, dd);
        viewHolder.setText(R.id.i_mywork_money, StringUtils.getTime(time, "MM") + "月");
        viewHolder.setText(R.id.i_mywork_name, jsonObject.optString("name"));
//        viewHolder.setText(R.id.i_mywork_del, statestr);

        ImageView view = viewHolder.getView(R.id.i_mywork_image);
        Glide.with(this)
                .load(Urls.HOST + jsonObject.optString("pic"))
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .error(R.drawable.project_bg)
                .crossFade(500)
                .into(view);
        view.setTag(R.id.tag_first,Urls.HOST + jsonObject.optString("pic"));
        view.setOnClickListener(new ItemClickListener());

        viewHolder.setText(R.id.i_mywork_zan, jsonObject.optInt("nice_count",0)+"");
        viewHolder.setText(R.id.i_mywork_guanzhu, jsonObject.optInt("shoucang_count",0)+"");
        viewHolder.setText(R.id.i_mywork_viewcount, jsonObject.optInt("viewcount",0)+"");


    }

    @Override
    public void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position, Object item) {

    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, Object item) {

    }

    /**
     * 现在的请求是放在modle里的
     *
     * @return
     */
    @Override
    public BaseAsyncModle getBaseAsyncModle() {
        WorkModle workModle = new WorkModle(this);
        workModle.setPageSize(5);

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

    private class ItemClickListener implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Object url = v.getTag(R.id.tag_first);//url

            MyDialogFragment myDialogFragment = MyDialogFragment.newInstance(url + "");
            myDialogFragment.show(getFragmentManager(),getClass().getSimpleName());

        }
    }
}
