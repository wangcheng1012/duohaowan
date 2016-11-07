package com.hd.wlj.duohaowan.ui.home.classify.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.orhanobut.logger.Logger;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.MathUtil;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class WorkDetailsActivity extends BaseFragmentActivity implements DetailsView, View.OnClickListener {


    private RecyclerView recyclerView;
    private DetailsPresenterImpl presenter;
    private ViewHolder holder;
    private List<String> mData;
    private String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = new RecyclerView(this);

        setContentView(recyclerView);

        presenter = new DetailsPresenterImpl(this);
        presenter.attachView(this);

        Intent intent = getIntent();

        init();

        String id = intent.getStringExtra("id");
        presenter.loadDetailsData(id);


    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View view = LayoutInflater.from(this).inflate(R.layout.activity_work_details, null);
        holder = new ViewHolder(view);
        holder.setListenter(this);

        mData = new ArrayList<>();
        //json 解析评论数据
        if (!StringUtils.isEmpty(jsonStr)) {
            try {

                JSONObject json = new JSONObject(jsonStr);

                JSONArray comments = json.optJSONArray("comment_list");
                if (comments != null) {
                    for (int i = 0; i < comments.length(); i++) {
                        mData.add(comments.optJSONObject(i).toString());
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //
        CommonAdapter<String> commonAdapter = new CommonAdapter<String>(this, R.layout.item_work_recyclerview, mData) {

            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, String str, int position) {

                try {
                    JSONObject jo = new JSONObject(str);

                    //头像
                    ImageView headimage = holder.getView(R.id.item_work_headimage);
                    String pic = jo.optString("pic");

                    Glide.with(WorkDetailsActivity.this)
                            .load(StringUtils.isEmpty(pic) ? R.drawable.project_bg : Urls.HOST + pic)
                            .thumbnail(0.5f)
                            .placeholder(R.drawable.project_bg)
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

        HeaderAndFooterWrapper header = new HeaderAndFooterWrapper(commonAdapter);
        header.addHeaderView(view);

        recyclerView.setAdapter(header);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showDetailsData(Base paramBase) {

        show(paramBase.getResultStr());
    }

    private void show(String json) {
        try {
            JSONObject jo = new JSONObject(json);
            JSONObject artist = jo.optJSONObject("artist");

            String pic = jo.optString("pic");
//          LoadImage.getinstall().addTask(Urls.HOST + pic, holder.img).doTask();

            Glide.with(WorkDetailsActivity.this).load(R.drawable.project_bg).into(holder.img);

            Glide.with(WorkDetailsActivity.this)
                    .load(StringUtils.isEmpty(pic) ? R.drawable.project_bg : Urls.HOST + pic)
                    .placeholder(R.drawable.project_bg)
//                    .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {

                            int intrinsicWidth = resource.getIntrinsicWidth();
                            int height = holder.img.getWidth() * resource.getIntrinsicHeight() / intrinsicWidth;

                            holder.img.setMinimumWidth(intrinsicWidth);
                            holder.img.setMinimumHeight(height);

                            holder.img.setImageDrawable(resource);
                        }
                    });
            
            if (artist != null) {

                String touxian = artist.optString("touxian");

                holder.userName.setText(artist.optString("realname") + (StringUtils.isEmpty(touxian) ? "" : "(" + touxian + ")"));

                String zhiwei = artist.optString("zhiwei");
                if (!StringUtils.isEmpty(zhiwei)) {
                    holder.zhiwei.setText(zhiwei);
                }

                String picname = artist.optString("picname");
                Glide.with(this)
                        .load(StringUtils.isEmpty(picname) ? R.drawable.project_bg : Urls.HOST + picname)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .thumbnail(0.5f)
                        .crossFade(1000)
                        .into(holder.workDetailHead);
            } else {

                Glide.with(this)
                        .load(R.drawable.project_bg)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .thumbnail(0.5f)
                        .crossFade(1000)
                        .into(holder.workDetailHead);
            }
            String name = jo.optString("name");
            if (!StringUtils.isEmpty(name)) {
                holder.workname.setText("⟪ " + name + " ⟫");
            } else {
                holder.workname.setText("⟪ " + "作品名称" + " ⟫");
            }

            //标签
//            taglayout.add

            String cicun = jo.optString("cicun");
            String showTime = jo.optString("showTime");
            String showTime1 = showTime;
            if (!StringUtils.isEmpty(showTime)) {
                showTime = showTime.substring(0, 4);
                showTime1 = showTime1.split(" ")[0].replace(showTime, "").substring(1);

                holder.time.setText(showTime1);
            }
            if (StringUtils.isEmpty(cicun) || StringUtils.isEmpty(showTime)) {

                holder.chicun.setText(cicun + showTime);
            } else {

                holder.chicun.setText(cicun + "/" + showTime);
            }
            BigDecimal price_fen = MathUtil.divide(jo.optString("price_fen"), 100, 2);
            holder.price.setText("¥" + price_fen.toString());
            Logger.d("intValue:" + price_fen.intValue() + "doubleValue:" + price_fen.doubleValue());

            holder.vewcount.setText(jo.optInt("viewcount", 0) + "");

            String intro = jo.optString("intro");
            if (!StringUtils.isEmpty(intro)) {
                byte[] decode = Base64.decode(intro, Base64.DEFAULT);
                holder.intro.setText(new String(decode));
            }
            holder.comment.setText("评论（" + mData.size() + "）");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.work_details_zan:
                UIHelper.toastMessage(this, "work_details_zan");
                break;
            case R.id.work_details_guanzhu:

                break;
            case R.id.work_details_song:

                break;
            case R.id.work_details_more:

                break;
        }
    }

    static class ViewHolder {

        @BindView(R.id.work_detail_head)
        ImageView workDetailHead;
        @BindView(R.id.work_details_name)
        TextView userName;
        @BindView(R.id.work_details_zhiwei)
        TextView zhiwei;
        @BindView(R.id.work_details_workname)
        TextView workname;
        @BindView(R.id.work_details_taglayout)
        LinearLayout taglayout;
        @BindView(R.id.work_details_chicun)
        TextView chicun;
        @BindView(R.id.work_details_pice)
        TextView price;
        @BindView(R.id.work_details_viewcount)
        TextView vewcount;
        @BindView(R.id.work_details_time)
        TextView time;
        @BindView(R.id.work_details_zan)
        TextView zan;
        @BindView(R.id.work_details_guanzhu)
        TextView guanzhu;
        @BindView(R.id.work_details_song)
        TextView song;
        @BindView(R.id.work_details_layout)
        LinearLayout workDetailsLayout;
        @BindView(R.id.work_details_more)
        ImageView more;
        @BindView(R.id.work_details_intro)
        TextView intro;
        @BindView(R.id.work_details_comment)
        TextView comment;
        @BindView(R.id.work_details_img)
        ImageView img;
        View.OnClickListener listenter;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.work_details_zan, R.id.work_details_guanzhu, R.id.work_details_song, R.id.work_details_more})
        public void onClick(View view) {
            listenter.onClick(view);
        }

        public void setListenter(View.OnClickListener listenter) {
            this.listenter = listenter;
        }
    }


}


