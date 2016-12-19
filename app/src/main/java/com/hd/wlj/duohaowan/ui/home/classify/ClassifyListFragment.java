package com.hd.wlj.duohaowan.ui.home.classify;


import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.been.ShouCang;
import com.hd.wlj.duohaowan.been.Zan;
import com.hd.wlj.duohaowan.ui.home.classify.work.WorkDetailsActivity;
import com.orhanobut.logger.Logger;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.CyptoUtils;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.util.img.LoadImage;
import com.wlj.base.web.asyn.AsyncCall;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.hd.wlj.duohaowan.R.id.view;

/**
 * SwipeRefreshLayout下拉刷新 和RecyclerView加载跟多实现的一个fragment，多个地方都可以调用
 */
public class ClassifyListFragment extends Fragment implements ClassifyListView {

    @BindView(R.id.classify_list_recycerview)
    RecyclerView recycerview;
    @BindView(R.id.classify_list_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ClassifyListPresenter presenter;

    public ClassifyListFragment() {
    }

    public static ClassifyListFragment newInstance(String tabBarStr, int classify) {

        ClassifyListFragment classifyListFragment = new ClassifyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tabBarStr", tabBarStr);
        bundle.putInt("classify", classify);

        classifyListFragment.setArguments(bundle);

        return classifyListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = super.onCreateView(inflater, container, savedInstanceState);

        View inflate = inflater.inflate(R.layout.fragment_classify_list, container, false);
        ButterKnife.bind(this, inflate);
        init();
        initSwipeRefreshLayout();

        return inflate;
    }

//    @Override
//    protected int getlayout() {
//        return R.layout.fragment_classify_list;
//    }
//
//    @Override
//    protected void initView() {
//        ButterKnife.bind(this, view);
//        init();
//        initSwipeRefreshLayout();
//    }
    private void initSwipeRefreshLayout() {

        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
        presenter = null;
    }

    private void init() {
        Bundle arguments = getArguments();
        int classify = arguments.getInt("classify", -1);

        presenter = new ClassifyListPresenter(getActivity(), arguments);
        presenter.attachView(this);

        presenter.initRecycerview(recycerview, swipeRefreshLayout);

        if (classify != ClassifyListModel.request_type_seach) {
            // 分类列表 才初始化刷新 ，seach手动  手动掉
            presenter.onRefresh();
        }

    }

    public void seach(String seachType, String seachtext) {
        presenter.setSeachType(seachType);
        presenter.setSeachText(seachtext);
        presenter.onRefresh();
    }

    /**
     * 艺术家 Recycerview
     *
     * @param holder
     * @param item
     * @param position
     */
    @Override
    public void artistRecycerview(final ViewHolder holder, Base item, int position) {
        final JSONObject jsonObject = item.getResultJsonObject();

        //背景
        String picBack = jsonObject.optString("pic_back");
        Glide.with(this)
                .load(Urls.HOST + picBack)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            holder.getView(R.id.classify_artist_background).setBackground(resource);
                        } else {
                            holder.getView(R.id.classify_artist_background).setBackgroundDrawable(resource);
                        }
                    }
                });
        //头像
        ImageView headimage = holder.getView(R.id.classify_artist_headimage);
        Glide.with(this).load(Urls.HOST + jsonObject.optString("pic_touxiang"))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(headimage);
//        LoadImage.getinstall().addTask(Urls.HOST + jsonObject.optString("picname"), headimage).doTask();

        holder.setText(R.id.classify_artist_name, jsonObject.optString("nickname"));
        holder.setText(R.id.classify_artist_intro, CyptoUtils.decryptBASE64(jsonObject.optString("intro")));

        holder.setText(R.id.classify_artist_liulan, jsonObject.optInt("viewcount", 0) + "");
        holder.setText(R.id.classify_artist_zan, jsonObject.optInt("nice_count", 0) + "");
        holder.setText(R.id.classify_artist_guanzhu, jsonObject.optInt("shoucang_count", 0) + "");
        //song

        //点赞
        holder.getView(R.id.classify_artist_zan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                new Zan(getActivity()).nice(jsonObject.optString("pub_id")).setZview(new Zan.zanView() {
                    @Override
                    public void niceSuccess(List<Base> paramList, Base paramBase, int type) {
                        //点赞成功
                        UIHelper.toastMessage(getContext(), "点赞成功");
                        if (v != null) {
                            TextView tv = (TextView) v;
                            int count = StringUtils.toInt(tv.getText());
                            tv.setText(count + 1 + "");
                        }
                    }
                });
            }
        });

        holder.getView(R.id.classify_artist_guanzhu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShouCang shouCang = new ShouCang(getActivity());
                shouCang.setId(jsonObject.optString("pub_id"));
                shouCang.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                    @Override
                    public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                        JSONObject jsonObject1 = base.getResultJsonObject();
                        UIHelper.toastMessage(getActivity(), jsonObject1.optString("message"));
//                        v.setEnabled(false);
//                        TextView tv = (TextView) v;
//                        tv.setText("已关注");
                    }

                    @Override
                    public void fail(Exception paramException) {
                        UIHelper.toastMessage(getActivity(), "关注失败");
                    }
                });

            }
        });
    }

    /**
     * 艺术展
     *
     * @param viewHolder
     * @param item
     * @param position
     */
    @Override
    public void artGalleryRecycerview(ViewHolder viewHolder, Base item, int position) {
        final JSONObject jsonObject = item.getResultJsonObject();

        String logo = jsonObject.optString("logo");
        ImageView logoview = viewHolder.getView(R.id.classify_artgallery_logo);
        Glide.with(this).load(Urls.HOST+logo).bitmapTransform(new CropCircleTransformation(getContext())).into(logoview);

        viewHolder.setText(R.id.classify_artgallery_name,jsonObject.optString("name"));
        viewHolder.setText(R.id.classify_artgallery_jubanfang,jsonObject.optString("jubanfang"));

        String pic = jsonObject.optString("pic");
        ImageView picview = viewHolder.getView(R.id.classify_artgallery_pic);
        Glide.with(this).load(Urls.HOST+pic).into(picview);

        View follow = viewHolder.getView(R.id.classify_artgallery_follow);

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShouCang shouCang = new ShouCang(getActivity());
                shouCang.setId(jsonObject.optString("pub_id"));
                shouCang.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                    @Override
                    public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                        JSONObject object = base.getResultJsonObject();
                        UIHelper.toastMessage(getContext(),object.optString("message"));

                    }

                    @Override
                    public void fail(Exception paramException) {

                    }
                });
            }
        });

    }

    /**
     * 艺术品 Recycerview
     *
     * @param viewHolder
     * @param item
     * @param position
     */
    @Override
    public void workOfArtRecycerview(ViewHolder viewHolder, Base item, int position) {

        JSONObject jsonObject = item.getResultJsonObject();

        JSONObject artist = jsonObject.optJSONObject("artist");

        JSONArray showTag_list = jsonObject.optJSONArray("showTag_list");
        if(showTag_list != null){
            String s = showTag_list.optString(0);
            viewHolder.setText(R.id.classify_workofart_workname, s);
        }
        viewHolder.setText(R.id.classify_workofart_intro, jsonObject.optString("name"));
        viewHolder.setText(R.id.classify_workofart_zan, jsonObject.optInt("nice_count", 0) + "");

        viewHolder.setText(R.id.classify_workofart_name, artist != null ? artist.optString("realname") : "");

        ImageView head = viewHolder.getView(R.id.classify_workofart_head);
        final ImageView image = viewHolder.getView(R.id.classify_workofart_image);

        int pic_width = jsonObject.optInt("pic_width", 50);
        int pic_height = jsonObject.optInt("pic_height", 50);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        int width1 = point.x/2 - DpAndPx.dpToPx(getContext(),20);
        int height1 = pic_height * width1 /pic_width;

        image.setMinimumHeight(height1);

        Glide.with(ClassifyListFragment.this)
                .load(artist != null ? Urls.HOST + artist.optString("picname") : R.drawable.project_bg)
                .error(R.drawable.project_bg)
                .bitmapTransform(new CropCircleTransformation(getContext()))
//                .crossFade(1000)
                .into(head);

        Glide.with(ClassifyListFragment.this)
                .load(Urls.HOST + jsonObject.optString("pic"))
//                .error(R.drawable.project_bg)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(image);
//                        new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
////                        int width = resource.getIntrinsicWidth();
////                        int height = resource.getIntrinsicHeight();
////
////                        int width1 = image.getWidth();
////                        int height1 = height * width1 /width;
////
////                        image.setMinimumHeight(height1);
////
////                        Logger.i("width:%s,height:%s",width,height);
//                        image.setImageDrawable(resource);
//                    }
//                });

    }

    /**
     * 艺术观——史学
     * @param viewHolder
     * @param item
     * @param position
     */
    @Override
    public void artviewHistoryRecycerview(ViewHolder viewHolder, Base item, int position) {
        JSONObject jsonObject = item.getResultJsonObject();
        View point = viewHolder.getView(R.id.artview_history_point);

        viewHolder.setText(R.id.artview_history_text,jsonObject.optString("name"));
        long time = jsonObject.optLong("time", 0);
        viewHolder.setText(R.id.artview_history_time, StringUtils.getTime(time, "yyyy/MM/dd"));
    }

    /**
     * 艺术观——现实观
     * @param viewHolder
     * @param item
     * @param position
     */
    @Override
    public void artviewRealityRecycerview(ViewHolder viewHolder, Base item, int position) {
        JSONObject jsonObject = item.getResultJsonObject();

        viewHolder.setText(R.id.item_reality_name,jsonObject.optString("name"));
        viewHolder.setText(R.id.item_reality_time,StringUtils.getTime(jsonObject.optLong("time"), "yyyy-MM-dd"));
        viewHolder.setText(R.id.item_reality_intro,CyptoUtils.decryptBASE64(jsonObject.optString("intro")));
        viewHolder.setText(R.id.item_reality_share,jsonObject.optInt("shoucang_count",0)+"");
        viewHolder.setText(R.id.item_reality_viewcount,jsonObject.optInt("viewcount",0)+"");
        viewHolder.setText(R.id.item_reality_zan,jsonObject.optInt("nice_count",0)+"");

        JSONArray pics = jsonObject.optJSONArray("pics");

        LinearLayout imagell = viewHolder.getView(R.id.item_reality_imageLL);
        imagell.removeAllViews();

        if(pics != null){
            viewHolder.getView(view).setVisibility(View.GONE);
            for (int i = 0; i < pics.length(); i++) {
                String s = pics.optString(i);

                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setAdjustViewBounds(true);

                imagell.addView(imageView);

                Glide.with(this).load(Urls.HOST+ s).into(imageView);
            }
        }else{

            viewHolder.getView(view).setVisibility(View.VISIBLE);
        }

    }

    /**
     * 艺术观——问答篇
     * @param viewHolder
     * @param item
     * @param position
     */
    @Override
    public void artviewAskandLearnRecycerview(ViewHolder viewHolder, Base item, int position) {

        JSONObject jsonObject = item.getResultJsonObject();

        viewHolder.setText(R.id.artview_askedandlearn_text,CyptoUtils.decryptBASE64(jsonObject.optString("intro")));
        viewHolder.setText(R.id.artview_askedandlearn_name,jsonObject.optString("name"));
        viewHolder.setText(R.id.artview_askedandlearn_statetime,StringUtils.getTime(jsonObject.optLong("time"), "yyyy-MM-dd"));
    }
}
