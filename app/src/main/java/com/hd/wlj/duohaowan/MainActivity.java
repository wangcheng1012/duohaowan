package com.hd.wlj.duohaowan;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.hd.wlj.duohaowan.ui.LoginActivity;
import com.hd.wlj.duohaowan.ui.follow.FollowFragment;
import com.hd.wlj.duohaowan.ui.home.HomeFragment;
import com.hd.wlj.duohaowan.ui.my.MyFragment;
import com.hd.wlj.duohaowan.ui.seach.SeachFragment;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.statusbar.StatusBarUtil;

public class MainActivity extends BaseFragmentActivity implements OnClickListener,HomeFragment.OnFragmentInteractionListener
{
    // 定义Fragment页面
    private HomeFragment fragmentHome;
    private SeachFragment fragmentSearch;
    private FollowFragment fragmentSpace;
    private MyFragment fragmentMy;
    // 定义布局对象
    private FrameLayout homeFl, authFl, spaceFl, moreFl;

    // 定义图片组件对象
    private ImageView atIv, authIv, spaceIv, moreIv;

    // 定义按钮图片组件
    private ImageView toggleImageView, plusImageView;

    // 定义PopupWindow
    private PopupWindow popWindow;
    // 获取手机屏幕分辨率的类
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();

        //初始化默认为选中点击了“动态”按钮
        clickAtBtn();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
//        StatusBarUtil.setTransparentForImageViewInFragment(this,null);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        // 实例化布局对象
        homeFl = (FrameLayout) findViewById(R.id.layout_home);
        authFl = (FrameLayout) findViewById(R.id.layout_seach);
        spaceFl = (FrameLayout) findViewById(R.id.layout_follow);
        moreFl = (FrameLayout) findViewById(R.id.layout_my);

        // 实例化图片组件对象
        atIv = (ImageView) findViewById(R.id.image_home);
        authIv = (ImageView) findViewById(R.id.image_seach);
        spaceIv = (ImageView) findViewById(R.id.image_follow);
        moreIv = (ImageView) findViewById(R.id.image_my);

        // 实例化按钮图片组件
        toggleImageView = (ImageView) findViewById(R.id.toggle_btn);
        plusImageView = (ImageView) findViewById(R.id.plus_btn);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 给布局对象设置监听
        homeFl.setOnClickListener(this);
        authFl.setOnClickListener(this);
        spaceFl.setOnClickListener(this);
        moreFl.setOnClickListener(this);

        // 给按钮图片设置监听
        toggleImageView.setOnClickListener(this);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击s首页按钮
            case R.id.layout_home:
                clickAtBtn();
                break;
            // 点击seach关按钮
            case R.id.layout_seach:
                clickAuthBtn();
                break;
            // 点击follow按钮
            case R.id.layout_follow:
                clickFollowBtn();
                break;
            // 点击my按钮
            case R.id.layout_my:
                clickMoreBtn();
                break;
            // 点击中间按钮
            case R.id.toggle_btn:
//                clickToggleBtn();
                break;
        }
    }

    /**
     * 点击了“动态”按钮
     */
    private void clickAtBtn() {
        // 实例化Fragment页面
        if(fragmentHome == null) {
            fragmentHome = new HomeFragment();
        }
        // 得到Fragment事务管理器
        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();
        // 替换当前的页面
        fragmentTransaction.replace(R.id.frame_content, fragmentHome);
        // 事务管理提交
        fragmentTransaction.commit();
        // 改变选中状态
        homeFl.setSelected(true);
        atIv.setSelected(true);

        authFl.setSelected(false);
        authIv.setSelected(false);

        spaceFl.setSelected(false);
        spaceIv.setSelected(false);

        moreFl.setSelected(false);
        moreIv.setSelected(false);
    }

    /**
     * 点击了“与我相关”按钮
     */
    private void clickAuthBtn() {
        // 实例化Fragment页面
        if(fragmentSearch == null) {
            fragmentSearch = new SeachFragment();
        }
        // 得到Fragment事务管理器
        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();
        // 替换当前的页面
        fragmentTransaction.replace(R.id.frame_content, fragmentSearch);
        // 事务管理提交
        fragmentTransaction.commit();

        homeFl.setSelected(false);
        atIv.setSelected(false);

        authFl.setSelected(true);
        authIv.setSelected(true);

        spaceFl.setSelected(false);
        spaceIv.setSelected(false);

        moreFl.setSelected(false);
        moreIv.setSelected(false);
    }

    /**
     * 点击了“我的空间”按钮
     */
    private void clickFollowBtn() {
        // 实例化Fragment页面
        if(fragmentSpace == null) {
            fragmentSpace = new FollowFragment();
        }
        // 得到Fragment事务管理器
        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();
        // 替换当前的页面
        fragmentTransaction.replace(R.id.frame_content, fragmentSpace);
        // 事务管理提交
        fragmentTransaction.commit();

        homeFl.setSelected(false);
        atIv.setSelected(false);

        authFl.setSelected(false);
        authIv.setSelected(false);

        spaceFl.setSelected(true);
        spaceIv.setSelected(true);

        moreFl.setSelected(false);
        moreIv.setSelected(false);
    }

    /**
     * 点击了“更多”按钮
     */
    private void clickMoreBtn() {
        GoToHelp.go(this, LoginActivity.class);


//        // 实例化Fragment页面
//        if(fragmentMy  == null) {
//            fragmentMy = new MyFragment();
//        }
//        // 得到Fragment事务管理器
//        FragmentTransaction fragmentTransaction = this
//                .getSupportFragmentManager().beginTransaction();
//        // 替换当前的页面
//        fragmentTransaction.replace(R.id.frame_content, fragmentMy);
//        // 事务管理提交
//        fragmentTransaction.commit();
//
//        homeFl.setSelected(false);
//        atIv.setSelected(false);
//
//        authFl.setSelected(false);
//        authIv.setSelected(false);
//
//        spaceFl.setSelected(false);
//        spaceIv.setSelected(false);
//
//        moreFl.setSelected(true);
//        moreIv.setSelected(true);
    }

    /**
     * 点击了中间按钮
     */
    private void clickToggleBtn() {
        showPopupWindow(toggleImageView);
        // 改变按钮显示的图片为按下时的状态
        plusImageView.setSelected(true);
    }

    /**
     * 改变显示的按钮图片为正常状态
     */
    private void changeButtonImage() {
        plusImageView.setSelected(false);
    }

    /**
     * 显示PopupWindow弹出菜单
     */
    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.popwindow_layout, null);
            dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            // 创建一个PopuWidow对象
            popWindow = new PopupWindow(view, dm.widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        // PopupWindow的显示及位置设置
        // popWindow.showAtLocation(parent, Gravity.FILL, 0, 0);
        popWindow.showAsDropDown(parent, 0,0);

        popWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // 改变显示的按钮图片为正常状态
                changeButtonImage();
            }
        });

        // 监听触屏事件
        popWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                // 改变显示的按钮图片为正常状态
                changeButtonImage();
                popWindow.dismiss();
                return false;
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
