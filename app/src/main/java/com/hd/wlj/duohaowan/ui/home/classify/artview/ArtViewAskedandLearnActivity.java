package com.hd.wlj.duohaowan.ui.home.classify.artview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVContract;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVPresenter;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsModelImpl;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsPresenterImpl;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ArtViewAskedandLearnActivity extends BaseFragmentActivity implements SWRVContract.View, SWRVContract.SWRVPresenterAdapter, DetailsView {

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
    private DetailsPresenterImpl presenter;
    private View headview;
    private JSONObject jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artview_askedandlearn);
        ButterKnife.bind(this);

        title();
        comment();

        presenter = new DetailsPresenterImpl(this);
        presenter.attachView(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText editText = new EditText(getApplicationContext());
                Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_INDEFINITE).setAction("发送", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.comment(jsonData,editText);
                    }
                });
                editText.setHint("说点什么吧！");
                SnackbarUtil.SnackbarAddView(snackbar,editText,0);
                snackbar.show();
            }
        });

    }

    private void head() {

        headview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.part_skedandlearn_heard, null);
        headview.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT));

        HeaderAndFooterWrapper headerAndFooterWrapper = new HeaderAndFooterWrapper(swrvPresenter.getAdapter());
        headerAndFooterWrapper.addHeaderView(headview);

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
        titleTitle.setText("");
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
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        DetailsModelImpl workModle = new DetailsModelImpl(this);
        workModle.setId(id);
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
        jsonData = base.getResultJsonObject();
        titleTitle.setText(jsonData.optString("name"));
        String intro = jsonData.optString("intro");
        long time = jsonData.optLong("time");
        JSONArray comment_list = jsonData.optJSONArray("comment_list");

        TextView context = (TextView) headview.findViewById(R.id.part_askandlearn_context);
        TextView huifu = (TextView) headview.findViewById(R.id.part_askandlearn_huifu);
        TextView timeTV = (TextView) headview.findViewById(R.id.part_askandlearn_time);

        context.setText(CyptoUtils.decryptBASE64(intro));
        timeTV.setText(StringUtils.getTime(time,"yyyy/MM/dd"));

        ArrayList<Base> bases = new ArrayList<>();
        if(comment_list != null){
            huifu.setText(comment_list.length()+"");
            for (int i = 0; i < comment_list.length(); i++) {
                JSONObject object = comment_list.optJSONObject(i);

                bases.add(new Base(object){
                    @Override
                    public Base parse(JSONObject jsonObject) throws JSONException {
                        return null;
                    }
                });
            }
        }else{
            huifu.setText("0");
        }

        return bases;
    }

    /**
     * 当数据为空时显示的view
     *
     * @return
     */
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

    //~~
    @Override
    public void showDetailsData(Base paramBase) {

    }

    @Override
    public void showComment() {
        swrvPresenter.onRefresh();
    }
}
