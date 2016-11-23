package com.hd.wlj.duohaowan.ui.my;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.been.User;
import com.hd.wlj.duohaowan.ui.LoginActivity;
import com.hd.wlj.duohaowan.ui.my.card.CardActivity;
import com.hd.wlj.duohaowan.ui.my.comment.CommentActivity;
import com.hd.wlj.duohaowan.ui.my.suggest.SuggestActivity;
import com.hd.wlj.duohaowan.ui.my.work.WorkActivity;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends BaseFragment {


    @BindView(R.id.my_dy)
    TextView myDy;
    @BindView(R.id.my_guanzhu)
    TextView myGuanzhu;
    @BindView(R.id.my_fensi)
    TextView myFensi;
    @BindView(R.id.my_headimage)
    ImageView myHeadimage;
    @BindView(R.id.head_backpic)
    ImageView backpic;
    @BindView(R.id.my_loginLayout_text)
    TextView name;
    @BindView(R.id.my_scrollView)
    ScrollView scrollView;
    @BindView(R.id.my_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private Base user;


    public MyFragment() {
    }

    @Override
    protected int getlayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, view);

        //滑动冲突解决
        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
                    }
                }
            });
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        initData();

    }

    /**
     * 网络请求 userinfo
     */
    private void initData() {

        User user = new User(getActivity());
        user.Request(User.getUserInfo).setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                showUserInfo(base);
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }

    /**
     * 网络 返回 数据处理
     * @param base
     */
    private void showUserInfo(Base base) {
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }

        user = base;
        JSONObject jsonObject = user.getResultJsonObject();

        Glide.with(this)
                .load(Urls.HOST+ jsonObject.optString("pic"))
                .bitmapTransform(new CropCircleTransformation(getContext()))
//                .placeholder(R.drawable.project_bg)
                .crossFade(1000)
                .error(R.drawable.project_bg)
                .into(myHeadimage);
        //
        Glide.with(this)
                .load(Urls.HOST+jsonObject.optString("pic_back"))
                .into(backpic);

        name.setText(jsonObject.optString("touxian") +"   "+ jsonObject.optString("nickname","未设置昵称"));
        myDy.setText(jsonObject.optInt("dongtai_count",0) +"");
        myGuanzhu.setText(jsonObject.optInt("shoucang_count",0) +"");
        myFensi.setText(jsonObject.optInt("guanzhu_count",0)+"");

    }

    @Override
    public void onResume() {
        super.onResume();

        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @OnClick({R.id.my_headimage,R.id.my_loginLayout, R.id.my_card, R.id.my_work, R.id.my_comment, R.id.my_jiaoyijilu, R.id.my_suggest})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_headimage:


                break;
            case R.id.my_loginLayout:

                GoToHelp.go(getActivity(), LoginActivity.class);
                break;
            case R.id.my_card:
                if(user != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("json", user.getResultStr());
                    GoToHelp.go(getActivity(), CardActivity.class, bundle);
                }else{
                    UIHelper.toastMessage(getContext(),"用户信息为空");
                }
                break;
            case R.id.my_work:
                GoToHelp.go(getActivity(), WorkActivity.class);
                break;
            case R.id.my_comment:
                GoToHelp.go(getActivity(), CommentActivity.class);
                break;
            case R.id.my_jiaoyijilu:

                break;
            case R.id.my_suggest:
                GoToHelp.go(getActivity(), SuggestActivity.class);
                break;
        }
    }


}
