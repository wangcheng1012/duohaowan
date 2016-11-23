package com.hd.wlj.duohaowan.ui.follow;


import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.been.ShouCang;
import com.hd.wlj.duohaowan.been.Zan;
import com.hd.wlj.duohaowan.ui.home.HomeFragment;
import com.hd.wlj.duohaowan.ui.home.classify.arist.AristDetailsActivity;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVContract;
import com.hd.wlj.duohaowan.ui.home.classify.v2.SWRVFragment;
import com.hd.wlj.duohaowan.ui.home.classify.work.WorkDetailsActivity;
import com.wlj.base.bean.Base;
import com.wlj.base.util.CyptoUtils;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.BaseAsyncModle;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowFragment extends Fragment {


//    @BindView(R.id.toolbar_title)
//    TextView toolbarTitle;
    @BindView(R.id.follow_tablayout)
    TabLayout tablayout;
    @BindView(R.id.follow_viewpager)
    ViewPager viewpager;
    private List<Fragment> fragments;
    private List<String> tabBarList;

    public FollowFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        ButterKnife.bind(this, view);
//        toolbarTitle.setText("关注");

        initdata();
        initViewPage();
        return view;
    }

//    @Override
//    protected int getlayout() {
//        return R.layout.fragment_follow;
//    }
//
//    @Override
//    protected void initView() {
//        ButterKnife.bind(this, view);
//        toolbarTitle.setText("关注");
//
//        initdata();
//        initViewPage();
//    }

    private void initdata() {
        tabBarList = new ArrayList<>();
        tabBarList.add("作品");
        tabBarList.add("作家");

        fragments = new ArrayList<>();

        fragments.add(getSwrvFragment("5812ef5478e0802052dd7a2f"));
        fragments.add(getSwrvFragment("581407b20e9f110d8cbbdb94"));
    }

    @NonNull
    private SWRVFragment getSwrvFragment(final String id) {

        SWRVFragment swrvFragment = new SWRVFragment();
        swrvFragment.setMyInterface(new SWRVFragment.SWRVInterface() {
            @Override
            public void onCreateViewExtract(RecyclerView recyclerview, SwipeRefreshLayout swipeRefreshLayout) {
            }

            @Override
            public SWRVContract.SWRVPresenterAdapter getPresenterAdapter() {
                return new SWRVContract.SWRVPresenterAdapter() {
                    @Override
                    public int getRecycerviewItemlayoutRes() {
                        if("581407b20e9f110d8cbbdb94".equals(id)) {
                            return R.layout.item_classify_artist;
                        }else {
                            return R.layout.item_home_recyclerview;
                        }
                    }

                    @Override
                    public RecyclerView.LayoutManager getLayoutManager() {
                        return new LinearLayoutManager(getContext());
                    }

                    @Override
                    public void convert(final com.zhy.adapter.recyclerview.base.ViewHolder holder, Base item, int position) {
                        final JSONObject jsonObject = item.getResultJsonObject();
                        if("581407b20e9f110d8cbbdb94".equals(id)) {
                            //作家
                            creatArtistItem(holder, jsonObject);
                        }else{
                            //作品
                            creatWorkItem(holder, jsonObject);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position, Object item) {

                    }

                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, Object tag) {
                        Class cla;
                        if("581407b20e9f110d8cbbdb94".equals(id)) {
                            //作家
                           cla =  AristDetailsActivity.class;
                        }else{
                            //作品
                            cla =  WorkDetailsActivity.class;
                        }
                        Bundle bundle = new Bundle();
                        Base base = (Base) tag;
                        JSONObject jsonObject = base.getResultJsonObject();
                        bundle.putString("id", jsonObject.optString("pub_id"));
                        GoToHelp.go(getActivity(),   cla, bundle);
                    }

                    @Override
                    public BaseAsyncModle getBaseAsyncModle() {
                        ShouCang shouCang = new ShouCang(getActivity());
                        shouCang.setId(id);
                        return shouCang;
                    }

                    @Override
                    public int getRequestType() {
                        return ShouCang.type_list_shoucang_arist;
                    }

                    @Override
                    public List<Base> handleBackData(List<Base> list, Base base, int requestType) {
                        return list;
                    }

                    @Override
                    public View getEmptyView() {
                        return null;
                    }
                };
            }

            private void creatWorkItem(ViewHolder holder, JSONObject jsonObject) {

                final ImageView view = holder.getView(R.id.item_home_recyclerView_image);
                holder.setImageResource(R.id.item_home_recyclerView_image, R.drawable.project_bg);
                Glide.with(FollowFragment.this)
                        .load(Urls.HOST + jsonObject.optString("pic"))
                        .asBitmap()
                        .placeholder(R.drawable.project_bg)
//                        .thumbnail(0.4f)
                        .into(new SimpleTarget<Bitmap>(500,500) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                int intrinsicWidth = resource.getWidth();
                                int height = view.getWidth() * resource.getHeight() / intrinsicWidth;

                                view.setMinimumWidth(intrinsicWidth);
                                view.setMinimumHeight(height);

                                view.setImageBitmap(resource);
                            }
                        });

                 holder.getView(R.id.home_fat_in).setVisibility(View.GONE);
                 holder.getView(R.id.home_fat_out).setVisibility(View.GONE);

                //
                holder.setText(R.id.item_home_viewcount, jsonObject.optInt("viewcount", 0) + "");
                holder.setText(R.id.item_home_zan, jsonObject.optInt("nice_count", 0) + "");
//                holder.setText(R.id.item_home_guanzhu,resultJsonObject.optString("shoucang_count"));
                holder.setText(R.id.item_home_song, jsonObject.optInt("", 0) + "");

            }

            private void creatArtistItem(final com.zhy.adapter.recyclerview.base.ViewHolder holder, final JSONObject jsonObject) {
                //背景
                String picBack = jsonObject.optString("pic_back");
                Glide.with(FollowFragment.this)
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
                Glide.with(FollowFragment.this).load(Urls.HOST + jsonObject.optString("picname"))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(headimage);
//        LoadImage.getinstall().addTask(Urls.HOST + jsonObject.optString("picname"), headimage).doTask();

                holder.setText(R.id.classify_artist_name, jsonObject.optString("nickname"));
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

        });
        return swrvFragment;
    }

    private void initViewPage() {

        tablayout.setTabMode(TabLayout.MODE_FIXED);

        viewpager.setAdapter(
                new FragmentStatePagerAdapter(getChildFragmentManager()) {

                    @Override
                    public Fragment getItem(int position) {
                        return fragments.get(position);
                    }

                    @Override
                    public int getCount() {
                        return fragments.size();
                    }

                    @Override
                    public CharSequence getPageTitle(int position) {

                        return tabBarList.get(position % fragments.size());
                    }
                }
        );

        tablayout.setupWithViewPager(viewpager);
    }
}
