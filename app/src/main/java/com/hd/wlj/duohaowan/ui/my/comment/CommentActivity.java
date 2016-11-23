package com.hd.wlj.duohaowan.ui.my.comment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVContract;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVFragment;
import com.hd.wlj.duohaowan.ui.home.classify.work.WorkDetailsActivity;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.CyptoUtils;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.StringUtils;
import com.wlj.base.web.asyn.BaseAsyncModle;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 评论
 */
public class CommentActivity extends BaseFragmentActivity implements SWRVContract.SWRVPresenterAdapter {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_title)
    TextView titleTitle;
    private SWRVFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FrameLayout frameLayout = new FrameLayout(this);
//        getWindow().getDecorView().findViewById(android.R.id.content);
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
                return CommentActivity.this;
            }
        });

        transaction.add(R.id.mywork_container, fragment);
        transaction.commitAllowingStateLoss();

        initTitle();
    }

    private void initTitle() {
        titleTitle.setText("我的评论");
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getRecycerviewItemlayoutRes() {
        return R.layout.item_mycomment;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public void convert(ViewHolder holder, Base item, int position) {

        JSONObject jo = item.getResultJsonObject();
        String showTime = jo.optString("showTime");
        long time2 = StringUtils.getTime(showTime);
        showTime = StringUtils.getTime(time2,"MM-dd HH:mm");
        //头像
        ImageView headimage = holder.getView(R.id.item_work_headimage);
        String pic = jo.optString("user_logo");

        Glide.with(this)
                .load(StringUtils.isEmpty(pic) ? R.drawable.project_bg : Urls.HOST + pic)
//                            .thumbnail(0.5f)
                .error(R.drawable.project_bg)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .crossFade(1000)
                .into(headimage);
        //
        holder.setText(R.id.item_work_name, jo.optString("user_show_name"));
        holder.setText(R.id.item_work_time, showTime);
        holder.getView(R.id.item_work_zan).setVisibility(View.GONE);

        //content
        String content = jo.optString("content");
        if (!StringUtils.isEmpty(content)) {
            holder.setText(R.id.item_work_content, CyptoUtils.decryptBASE64(content) );
        }
        //间距
        View view = holder.getView(R.id.item_mycomment);
         RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.topMargin = DpAndPx.dpToPx(this,8);
        view.setLayoutParams(layoutParams);
        //
        holder.getView(R.id.item_work_line).setVisibility(View.GONE);
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
        CommentModle workModle = new CommentModle(this);
//        workModle.set

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
