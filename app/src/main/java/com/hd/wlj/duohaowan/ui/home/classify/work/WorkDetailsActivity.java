package com.hd.wlj.duohaowan.ui.home.classify.work;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.been.ShouCang;
import com.hd.wlj.duohaowan.been.Zan;
import com.hd.wlj.duohaowan.view.MyDialogFragment;
import com.hd.wlj.duohaowan.view.RectClickImageView;
import com.hd.wlj.third.share.ShareModle;
import com.hd.wlj.third.share.ShareUI;
import com.orhanobut.logger.Logger;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.CyptoUtils;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.MathUtil;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class WorkDetailsActivity extends BaseFragmentActivity implements DetailsView, View.OnClickListener {

    @BindView(R.id.workdetails_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.workdetails_comment)
    EditText workdetailsComment;
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.title_right)
    TextView titleRight;

    private DetailsPresenterImpl presenter;
    private ViewHolder holder;
    private List<String> mData;
    private JSONObject jsonData;
    private HeaderAndFooterWrapper header;
    private ShareUI shareUI;
    private ShareModle shareModle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workdetails);
        ButterKnife.bind(this);

        presenter = new DetailsPresenterImpl(this);
        presenter.attachView(this);

        Intent intent = getIntent();
        init();

        String id = intent.getStringExtra("id");
        presenter.loadDetailsData(id);

        share(savedInstanceState);
        initTitle();
    }

    private void share(Bundle savedInstanceState) {

        shareModle = new ShareModle();
        shareUI = new ShareUI(WorkDetailsActivity.this, shareModle, savedInstanceState);
    }

    /**
     * 添加分享的数据
     */
    private void makeShareData() {
        String intro = jsonData.optString("intro");
        if (StringUtils.isEmpty(intro)) {
            intro = "暂无简介";
        }
        shareModle.setId(jsonData.optString("pub_id"));
        shareModle.setName(jsonData.optString("name"));
        shareModle.setContent(CyptoUtils.decryptBASE64(intro));
        shareModle.setResPic(R.mipmap.ic_launcher);
        shareModle.setPic(Urls.HOST + jsonData.optString("pic"), WorkDetailsActivity.this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareUI.mSinaShare.onNewIntent(intent);
    }

    private void initTitle() {
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTitle.setText("详情");
        titleRight.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf"));
        titleRight.setTextSize(23f);
        titleRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shareUI.showdialog(true);
            }
        });
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View view = LayoutInflater.from(this).inflate(R.layout.activity_work_details, null);
        holder = new ViewHolder(view);
        holder.setListenter(this);

        mData = new ArrayList<>();
        // 评论列表数据
        CommonAdapter<String> commonAdapter = new CommonAdapter<String>(this, R.layout.item_work_recyclerview, mData) {

            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, String str, int position) {

                try {
                    JSONObject jo = new JSONObject(str);

                    //头像
                    ImageView headimage = holder.getView(R.id.item_work_headimage);
                    String pic = jo.optString("user_logo");

                    Glide.with(WorkDetailsActivity.this)
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

        header = new HeaderAndFooterWrapper(commonAdapter);
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

    @Override
    public void showComment() {

        workdetailsComment.setText("");
        //
        UIHelper.toastMessage(this, "评论成功");
        //刷新数据
        presenter.loadDetailsData(getIntent().getStringExtra("id"));

    }

    private void show(String json) {
        try {
            jsonData = new JSONObject(json);
            JSONObject artist = jsonData.optJSONObject("artist");

            String pic = jsonData.optString("pic");
            // 图片区域点击
            imageRectOnClick(jsonData);

            Glide.with(WorkDetailsActivity.this)
                    .load(StringUtils.isEmpty(pic) ? R.drawable.project_bg : Urls.HOST + pic)
//                    .placeholder(R.drawable.project_bg)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            int width = resource.getIntrinsicWidth();
                            int height = resource.getIntrinsicHeight();
                            Logger.e("width:height = %s:%s",width,height);
                            holder.img.setImageDrawable(resource);
                        }
                    });
            //艺术家
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
            String name = jsonData.optString("name");
            if (!StringUtils.isEmpty(name)) {
                holder.workname.setText("⟪ " + name + " ⟫");
            } else {
                holder.workname.setText("");
            }

            //标签
            holder.taglayout.removeAllViews();
            JSONArray showTag_list = jsonData.optJSONArray("showTag_list");
            if (showTag_list != null) {
                int toPx4 = DpAndPx.dpToPx(this, 4);
                int toPx8 = DpAndPx.dpToPx(this, 8);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.rightMargin = toPx8;
                for (int i = 0; i < showTag_list.length(); i++) {
                    String tag = showTag_list.optString(i);
                    TextView textView = new TextView(this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setText(tag);
                    textView.setPadding(toPx8, toPx4, toPx8, toPx4);
                    textView.setTextColor(getResources().getColor(R.color.black1));
                    textView.setBackgroundResource(R.drawable.shape_huang_corners8);
                    textView.setLayoutParams(layoutParams);
                    holder.taglayout.addView(textView);
                }
            }
            //浏览 记录
            JSONArray user_see_jsonArray = jsonData.optJSONArray("user_see_jsonArray");
            if (user_see_jsonArray != null) {
                holder.workDetailsLayout.removeAllViews();
                holder.linearLayout.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams2.rightMargin = DpAndPx.dpToPx(this, 8);
                for (int i = 0; i < user_see_jsonArray.length(); i++) {
                    JSONObject jsonObject = user_see_jsonArray.optJSONObject(i);

                    String picname = jsonObject.optString("picname");
                    ImageView imageView = new ImageView(this);
                    imageView.setAdjustViewBounds(true);
                    imageView.setLayoutParams(layoutParams2);
                    Glide.with(this).load(Urls.HOST + picname).bitmapTransform(new CropCircleTransformation(this)).into(imageView);
                    holder.workDetailsLayout.addView(imageView);
                }
            } else {
                holder.linearLayout.setVisibility(View.GONE);
            }
            //~~
            String cicun = jsonData.optString("cicun");
            String showTime = jsonData.optString("showTime");
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
            BigDecimal price_fen = MathUtil.divide(jsonData.optString("price_fen"), 100, 2);
            holder.price.setText("¥" + price_fen.toString());
//            Logger.d("intValue:" + price_fen.intValue() + "doubleValue:" + price_fen.doubleValue());

            holder.vewcount.setText(jsonData.optInt("viewcount", 0) + "");

            String intro = jsonData.optString("intro");
            if (!StringUtils.isEmpty(intro)) {
                byte[] decode = Base64.decode(intro, Base64.DEFAULT);
                holder.intro.setText(new String(decode));
            }
            //评论
            JSONArray comment_list = jsonData.optJSONArray("comment_list");
            if (comment_list != null) {
                mData.clear();
                holder.comment.setText("评论（" + comment_list.length() + "）");

                for (int i = 0; i < comment_list.length(); i++) {
                    mData.add(comment_list.optJSONObject(i) + "");
                }
                header.notifyDataSetChanged();
            } else {
                holder.comment.setText("评论（" + 0 + "）");
            }


//            holder.zan.setTag(jsonData.optInt("nice_count", 0));

            //分享
            makeShareData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * imageRectOnClick
     *
     * @param jo
     * @return
     */
    private void imageRectOnClick(final JSONObject jo) {
        JSONArray jsonArray = jo.optJSONArray("aartworksCompoment_jsonArray");
        Map<Rect, String> rectList = null;
        if (jsonArray != null) {
            rectList = new HashMap();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject == null) return;

                String artworks_pic = jsonObject.optString("artworks_pic");
                JSONObject artworks_position = jsonObject.optJSONObject("artworks_position");
                if (artworks_position == null) return;

                int x = artworks_position.optInt("x");
                int y = artworks_position.optInt("y");
                int width = artworks_position.optInt("width");
                int height = artworks_position.optInt("height");

                Rect rect = new Rect(x, y, x + width, y + height);
                rectList.put(rect, artworks_pic);
            }
        }
        if (rectList != null) {
            Set<Rect> rects = rectList.keySet();
            holder.img.setRectList(new ArrayList<>(rects));

            final Map<Rect, String> finalRectList = rectList;
            holder.img.setRectAreaOnClickListioner(new RectClickImageView.RectAreaOnClickListioner() {

                @Override
                public void onClick(Rect rect) {
                    String s = finalRectList.get(rect);

                    MyDialogFragment myDialogFragment = MyDialogFragment.newInstance(Urls.HOST + s);
                    myDialogFragment.show(getFragmentManager(), getClass().getSimpleName());
                }

                @Override
                public void onClickOutSide() {
                    MyDialogFragment myDialogFragment = MyDialogFragment.newInstance(Urls.HOST + jo.optString("pic"));
                    myDialogFragment.show(getFragmentManager(), getClass().getSimpleName());
                }

            });
        }

    }

    @Override
    public void onClick(final View v) {

        String artistId = null;
        switch (v.getId()) {
            case R.id.work_details_zan:
//                UIHelper.toastMessage(this, "work_details_zan");

                new Zan(this).nice(jsonData.optString("pub_id")).setZview(new Zan.zanView() {
                    @Override
                    public void niceSuccess(List<Base> paramList, Base paramBase, int type) {
                        //点赞成功
                        UIHelper.toastMessage(WorkDetailsActivity.this, "点赞成功");
                        if (v != null) {
                            TextView tv = (TextView) v;
                            v.setEnabled(false);
//                            int count = StringUtils.toInt(tv.getText());
                            tv.setText("已赞");
                        }
                    }
                });
                break;
            case R.id.work_detail_head:
                JSONObject artist = jsonData.optJSONObject("artist");
                if(artist != null){
                      artistId = artist.optString("pub_id");
                }
            case R.id.work_details_guanzhu:
                String shouCangId = artistId;
                if(artistId == null){
                    shouCangId = jsonData.optString("pub_id");
                }
                ShouCang shouCang = new ShouCang(this);
                shouCang.setId(shouCangId);
                shouCang.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                    @Override
                    public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                        JSONObject jsonObject1 = base.getResultJsonObject();
                        UIHelper.toastMessage(WorkDetailsActivity.this, jsonObject1.optString("message"));
                        if(v instanceof TextView) {
                            TextView tv = (TextView) v;
                            tv.setText("已关注");
                        }
                    }

                    @Override
                    public void fail(Exception paramException) {
                        UIHelper.toastMessage(WorkDetailsActivity.this, "关注失败");
                    }
                });


                break;
            case R.id.work_details_song:

                break;
            case R.id.work_details_more:

                break;


        }
    }

    @OnClick(R.id.workdetails_send)
    public void onClick() {

        presenter.comment(jsonData, workdetailsComment);
    }

    static class ViewHolder {

        @BindView(R.id.work_detail_head)
        ImageView workDetailHead;
        @BindView(R.id.work_details_ll)
        LinearLayout linearLayout;
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
        RectClickImageView img;

        View.OnClickListener listenter;

        ViewHolder(View view) {

            ButterKnife.bind(this, view);
            img.setAutoHeight(true);
        }

        @OnClick({R.id.work_detail_head,R.id.work_details_zan, R.id.work_details_guanzhu, R.id.work_details_song, R.id.work_details_more})
        public void onClick(View view) {
            listenter.onClick(view);
        }

        public void setListenter(View.OnClickListener listenter) {
            this.listenter = listenter;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, shareUI.mQQshare.loginListener);
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, shareUI.mQQshare);
        }

    }
}


