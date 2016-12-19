package com.hd.wlj.duohaowan.ui.my;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.hd.wlj.duohaowan.App;
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
import com.wlj.base.util.AppConfig;
import com.wlj.base.util.AppContext;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONException;
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
    @BindView(R.id.my_loginLayout_jiantou)
    ImageView jiantou;

    private Base user;
    private boolean islogin;

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

        islogin = AppContext.getAppContext().islogin();
        if (islogin) {

            initData();
        }


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
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 网络 返回 数据处理
     *
     * @param base
     */
    private void showUserInfo(Base base) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        JSONObject jsonObject = LoginStateChange(base);

        Glide.with(this)
                .load(Urls.HOST + jsonObject.optString("pic"))
                .bitmapTransform(new CropCircleTransformation(getContext()))
//                .placeholder(R.drawable.project_bg)
                .crossFade(1000)
                .error(R.drawable.shape_oval)
                .into(myHeadimage);
        //
        Glide.with(this)
                .load(Urls.HOST + jsonObject.optString("pic_back"))
                .into(backpic);

        name.setText(jsonObject.optString("touxian") + "   " + jsonObject.optString("nickname", "立即登录"));
        myDy.setText(jsonObject.optInt("dongtai_count", 0) + "");
        myGuanzhu.setText(jsonObject.optInt("shoucang_count", 0) + "");
        myFensi.setText(jsonObject.optInt("guanzhu_count", 0) + "");
    }

    /**
     * 登录状态改变
     *
     * @param base
     * @return
     */
    @NonNull
    private JSONObject LoginStateChange(Base base) {
        JSONObject jsonObject = base.getResultJsonObject();
        if (jsonObject.length() > 0) {
            user = base;
            name.setEnabled(false);
            jiantou.setVisibility(View.GONE);
            this.islogin = true;
        } else {
            user = null;
            name.setEnabled(true);
            jiantou.setVisibility(View.VISIBLE);
            this.islogin = false;
        }
        return jsonObject;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        boolean islogin = AppContext.getAppContext().islogin();
        if (this.islogin != islogin) {
            initData();
            this.islogin = islogin;
        }
    }

    @OnClick({R.id.my_about,R.id.my_exit, R.id.my_headimage, R.id.my_loginLayout_jiantou, R.id.my_loginLayout_text, R.id.my_card, R.id.my_work, R.id.my_comment, R.id.my_jiaoyijilu, R.id.my_suggest})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_about:
                GoToHelp.go(getActivity(), AboutActivity.class);
                break;
            case R.id.my_exit:
                App app = (App) getActivity().getApplicationContext();
                app.loginOut();

                //改变显示状态
                showUserInfo(new Base(new JSONObject()) {
                    @Override
                    public Base parse(JSONObject jsonObject) throws JSONException {
                        return null;
                    }
                });

                break;
            case R.id.my_headimage:

                break;
            case R.id.my_loginLayout_jiantou:
            case R.id.my_loginLayout_text:

                GoToHelp.go(getActivity(), LoginActivity.class);
                break;
            case R.id.my_card:
                if (user != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("json", user.getResultStr());
                    GoToHelp.go(getActivity(), CardActivity.class, bundle);
                } else {
                    UIHelper.toastMessage(getContext(), "用户信息为空");
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
