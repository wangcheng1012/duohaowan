package com.hd.wlj.duohaowan.ui.home.classify.artview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsPresenterImpl;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsView;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.CyptoUtils;
import com.wlj.base.util.StringUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 现实篇 详情页
 */
public class ArtViewRealityActivity extends BaseFragmentActivity implements DetailsView {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.item_reality_name)
    TextView name;
    @BindView(R.id.item_reality_time)
    TextView time;
    @BindView(R.id.item_reality_intro)
    TextView intro;
    @BindView(R.id.view)
    View lineview;
    @BindView(R.id.item_reality_imageLL)
    LinearLayout imagell;
    @BindView(R.id.artview_reality_recyclerview)
    RecyclerView recyclerview;
    private List<String> mData;
    private DetailsPresenterImpl presenter;
    private CommonAdapter<String> commonAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artview_reality);
        ButterKnife.bind(this);

        initTitle();
        initRecyclerview();

        presenter = new DetailsPresenterImpl(this);
        presenter.attachView(this);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        presenter.loadDetailsData(id);
    }

    private void initRecyclerview() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mData = new ArrayList<>();

        // 评论列表数据
         commonAdapter = new CommonAdapter<String>(this, R.layout.item_work_recyclerview, mData) {

            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, String str, int position) {

                try {
                    JSONObject jo = new JSONObject(str);

                    //头像
                    ImageView headimage = holder.getView(R.id.item_work_headimage);
                    String pic = jo.optString("user_logo");

                    Glide.with(ArtViewRealityActivity.this)
                            .load(StringUtils.isEmpty(pic) ? R.drawable.project_bg : Urls.HOST + pic)
//                            .thumbnail(0.5f)
                            .error(R.drawable.project_bg)
                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                            .crossFade(1000)
                            .into(headimage);
                    //
                    holder.setText(R.id.item_work_name, jo.optString("user_show_name"));
                    holder.setText(R.id.item_work_time, jo.optString("showTime"));
                    holder.setText(R.id.item_work_zan, jo.optInt("nice_count", 0) + "");
                    //content
                    String content = jo.optString("content");
                    if (!StringUtils.isEmpty(content)) {
                        holder.setText(R.id.item_work_content, new String(Base64.decode(content, Base64.DEFAULT)));
                    }
                    //赞
                    holder.getView(R.id.item_work_zan).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //点赞

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

//        header = new HeaderAndFooterWrapper(commonAdapter);
//        header.addHeaderView(headerview);

        recyclerview.setAdapter(commonAdapter);
    }

    private void initTitle() {

        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleTitle.setText("现实篇");
    }

    @Override
    public void showDetailsData(Base item) {

        JSONObject jsonObject = item.getResultJsonObject();

        name.setText( jsonObject.optString("name"));
        time.setText( StringUtils.getTime(jsonObject.optLong("time"), "yyyy-MM-dd"));
        intro.setText(  CyptoUtils.decryptBASE64(jsonObject.optString("intro")));

        JSONArray pics = jsonObject.optJSONArray("pics");

        imagell.removeAllViews();

        if (pics != null) {
            lineview.setVisibility(View.GONE);
            for (int i = 0; i < pics.length(); i++) {
                String s = pics.optString(i);

                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setAdjustViewBounds(true);

                imagell.addView(imageView);

                Glide.with(this).load(Urls.HOST + s).into(imageView);
            }
        } else {

            lineview.setVisibility(View.VISIBLE);
        }

        //评论
        JSONArray comment_list = jsonObject.optJSONArray("comment_list");
        if (comment_list != null) {
            mData.clear();

            for (int i = 0; i < comment_list.length(); i++) {
                mData.add(comment_list.optJSONObject(i) + "");
            }
            commonAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showComment() {

    }



}
