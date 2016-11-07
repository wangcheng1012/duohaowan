package com.hd.wlj.duohaowan.ui.home.classify;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.been.Zan;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.CyptoUtils;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.util.img.LoadImage;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * SwipeRefreshLayout下拉刷新 和RecyclerView加载跟多实现的一个fragment，多个地方都可以调用
 */
public class SWRVFragment extends BaseFragment implements SWRVView {

    @BindView(R.id.classify_list_recycerview)
    RecyclerView recycerview;
    @BindView(R.id.classify_list_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private SWRVPresenter presenter;

    public SWRVFragment() {
    }

    public static SWRVFragment newInstance(String tabBarStr, int classify) {

        SWRVFragment classifyListFragment = new SWRVFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tabBarStr", tabBarStr);
        bundle.putInt("classify", classify);

        classifyListFragment.setArguments(bundle);

        return classifyListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getlayout() {
        return R.layout.fragment_classify_list;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, view);

        init();

        initSwipeRefreshLayout();
    }

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
    }

    private void init() {
        Bundle arguments = getArguments();
        int classify = arguments.getInt("classify", -1);

        presenter = new SWRVPresenter(getActivity(), arguments);
        presenter.attachView(this);

        presenter.initRecycerview(recycerview, swipeRefreshLayout);

        if (classify != SWRVModel.request_type_seach) {
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
        LoadImage.getinstall().addTask(Urls.HOST + jsonObject.optString("picname"), headimage).doTask();

        holder.setText(R.id.classify_artist_name, jsonObject.optString("name"));
        holder.setText(R.id.classify_artist_intro, CyptoUtils.decryptBASE64(jsonObject.optString("intro")));

        holder.setText(R.id.classify_artist_liulan, jsonObject.optInt("viewcount", 0) + "");
        holder.setText(R.id.classify_artist_zan, jsonObject.optInt("nice_count", 0) + "");
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


    }

    /**
     * 艺术馆
     *
     * @param viewHolder
     * @param item
     * @param position
     */
    @Override
    public void artGalleryRecycerview(ViewHolder viewHolder, Base item, int position) {

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

        viewHolder.setText(R.id.classify_workofart_intro, jsonObject.optString("name"));
        viewHolder.setText(R.id.classify_workofart_zan, jsonObject.optInt("nice_count", 0) + "");
        viewHolder.setText(R.id.classify_workofart_workname, jsonObject.optString("showTag_list"));
        viewHolder.setText(R.id.classify_workofart_name, artist != null ? artist.optString("realname") : "");

        ImageView head = viewHolder.getView(R.id.classify_workofart_head);
        ImageView image = viewHolder.getView(R.id.classify_workofart_image);

        Glide.with(SWRVFragment.this)
                .load(artist != null ? Urls.HOST + artist.optString("picname") : R.drawable.project_bg)
                .placeholder(R.drawable.project_bg)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .crossFade(1000).into(head);

        LoadImage.getinstall().addTask(Urls.HOST + jsonObject.optString("pic"), image);
        LoadImage.getinstall().doTask();
    }

}
