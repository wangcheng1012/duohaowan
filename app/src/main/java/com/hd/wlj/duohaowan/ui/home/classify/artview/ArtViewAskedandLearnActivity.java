package com.hd.wlj.duohaowan.ui.home.classify.artview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVContract;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVPresenter;
import com.hd.wlj.duohaowan.ui.my.comment.CommentModle;
import com.hd.wlj.duohaowan.util.SnackbarUtil;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.CyptoUtils;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.StringUtils;
import com.wlj.base.web.asyn.BaseAsyncModle;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ArtViewAskedandLearnActivity extends BaseFragmentActivity implements SWRVContract.View, SWRVContract.SWRVPresenterAdapter {


    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.askedandLearn_recycerview)
    RecyclerView recycerview;
    @BindView(R.id.askedandLearn_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.askedandLearn_fab)
    FloatingActionButton fab;
    private SWRVPresenter swrvPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artview_askedandlearn);
        ButterKnife.bind(this);

        title();
        comment();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "fake", Snackbar.LENGTH_LONG).setAction("发送", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ArtViewAskedandLearnActivity.this, "你点击了action", Toast.LENGTH_SHORT).show();
                    }
                });
//                EditText editText = new EditText(getApplicationContext());

//                SnackbarUtil.SnackbarAddView(snackbar,editText,0);
                snackbar.show();
            }
        });

    }

    private void head() {
        TextView textView = new TextView(getApplicationContext());
        textView.setText("sfjksdlnfjks");

        HeaderAndFooterWrapper headerAndFooterWrapper = new HeaderAndFooterWrapper(swrvPresenter.getAdapter());
        headerAndFooterWrapper.addHeaderView(textView);

        recycerview.setAdapter(headerAndFooterWrapper);
        swrvPresenter.setAdapter(headerAndFooterWrapper);//里面刷新
        headerAndFooterWrapper.notifyDataSetChanged();

    }

    private void title() {
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTitle.setText("sd");
    }

    /**
     * 评论
     */
    private void comment() {
        swrvPresenter = new SWRVPresenter(this);
        swrvPresenter.attachView(this);

        swrvPresenter.setPresenterAdapter(this);
        swrvPresenter.initRecycerview(recycerview,swipeRefreshLayout);
        head();
        swrvPresenter.onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        swrvPresenter.detachView();
    }

    @Override
    public int getRecycerviewItemlayoutRes() {
        return R.layout.item_mycomment;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getApplicationContext());
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
                .error(R.drawable.project_bg)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .crossFade(1000)
                .into(headimage);
        //
        holder.setText(R.id.item_work_name, jo.optString("user_show_name"));
        holder.setText(R.id.item_work_time, showTime);
//        holder.getView(R.id.item_work_zan).setVisibility(View.GONE);

        //content
        String content = jo.optString("content");
        if (!StringUtils.isEmpty(content)) {
            holder.setText(R.id.item_work_content, CyptoUtils.decryptBASE64(content) );
        }
//        //间距
//        View view = holder.getView(R.id.item_mycomment);
//        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
//        layoutParams.topMargin = DpAndPx.dpToPx(this,8);
//        view.setLayoutParams(layoutParams);
        //
//        holder.getView(R.id.item_work_line).setVisibility(View.GONE);
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
