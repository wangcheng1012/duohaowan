package com.hd.wlj.duohaowan;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.ui.follow.FollowFragment;
import com.hd.wlj.duohaowan.ui.home.HomeFragment;
import com.hd.wlj.duohaowan.ui.my.MyFragment;
import com.hd.wlj.duohaowan.ui.publish.ImageAdjustmentActivity;
import com.hd.wlj.duohaowan.ui.publish.adjustmentmore.sencemore.SenceMoreActivity;
import com.hd.wlj.duohaowan.ui.seach.SeachFragment;
import com.hd.wlj.duohaowan.util.TakePhotoCrop;
import com.jph.takephoto.model.TResult;
import com.lling.photopicker.PhotoPickerActivity;
import com.orhanobut.logger.Logger;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.GoToHelp;

import java.util.ArrayList;

import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends BaseFragmentActivity implements OnClickListener, HomeFragment.OnFragmentInteractionListener, TakePhotoCrop.CropBack {

    // 定义布局对象
    private FrameLayout homeFl, seachFl, followFl, myFl;
    // 定义图片组件对象
    private ImageView homeIv, seachIv, followIv, myIv;
    // 定义按钮图片组件
    private ImageView /*toggleImageView, */publishImageView;
    // 定义PopupWindow
    private PopupWindow popWindow;
    // 获取手机屏幕分辨率的类
    private DisplayMetrics dm;
    private Fragment curFragment;
    private View preSelected;
    private TakePhotoCrop takePhotoCrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        takePhotoCrop = new TakePhotoCrop(this,this);
        takePhotoCrop .getTakePhoto().onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        initData();

        homeFl.performClick();

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
        seachFl = (FrameLayout) findViewById(R.id.layout_seach);
        followFl = (FrameLayout) findViewById(R.id.layout_follow);
        myFl = (FrameLayout) findViewById(R.id.layout_my);

        // 实例化图片组件对象
        homeIv = (ImageView) findViewById(R.id.image_home);
        seachIv = (ImageView) findViewById(R.id.image_seach);
        followIv = (ImageView) findViewById(R.id.image_follow);
        myIv = (ImageView) findViewById(R.id.image_my);

        // 实例化按钮图片组件
//        toggleImageView = (ImageView) findViewById(R.id.toggle_btn);
        publishImageView = (ImageView) findViewById(R.id.publish_fabu);

        Glide.with(this).load(R.drawable.logo_wan).bitmapTransform(new CropCircleTransformation(this)).into(publishImageView);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 给布局对象设置监听
        homeFl.setOnClickListener(this);
        seachFl.setOnClickListener(this);
        followFl.setOnClickListener(this);
        myFl.setOnClickListener(this);

        // 给按钮图片设置监听
        publishImageView.setOnClickListener(this);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击s首页按钮
            case R.id.layout_home:
                fragmentTransation(HomeFragment.class);
                tabBarClick(homeIv);
                break;
            // 点击seach关按钮
            case R.id.layout_seach:
                fragmentTransation(SeachFragment.class);
                tabBarClick(seachIv);
                break;
            // 点击follow按钮
            case R.id.layout_follow:
                fragmentTransation(FollowFragment.class);
                tabBarClick(followIv);
                break;
            // 点击my按钮
            case R.id.layout_my:
                fragmentTransation(MyFragment.class);
                tabBarClick(myIv);

                break;
            // 点击中间按钮
//            case R.id.toggle_btn:
//                clickToggleBtn();
//
//                break;

            case R.id.publish_choosework:
                takePhotoCrop.photoPicker();
                break;
            case R.id.publish_choosescene:

                Bundle bundle = new Bundle();
                GoToHelp.go(this, SenceMoreActivity.class, bundle);

                popWindow.dismiss();
                break;
            case R.id.publish_fabu:
                clickToggleBtn();
//                popWindow.dismiss();
                break;
//            case R.id.publish_bianji:
//
////                popWindow.dismiss();
//                break;


        }
    }

    /**
     * 触发顺序:
     * detach()->onPause()->onStop()->onDestroyView()
     * attach()->onCreateView()->onActivityCreated()->onStart()->onResume()
     * 使用hide()方法只是隐藏了fragment的view并没有将view从viewtree中删除,随后可用show()方法将view设置为显示
     * 而使用detach()会将view从viewtree中删除,和remove()不同,此时fragment的状态依然保持着,在使用 attach()时会再次调用onCreateView()来重绘视图,注意使用detach()后fragment.isAdded()方法将返回 false,在使用attach()还原fragment后isAdded()会依然返回false(需要再次确认)
     * 执行detach()和replace()后要还原视图的话, 可以在相应的fragment中保持相应的view,并在onCreateView()方法中通过view的parent的removeView()方法将view和parent的关联删除后返回
     *
     * @param fragment
     */
    private void fragmentTransation(Class<?> fragment) {

        String simpleName = fragment.getSimpleName();
        if (curFragment != null && simpleName.equals(curFragment.getClass().getSimpleName())) {
            return;
        }

        // 得到Fragment事务管理器
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment fragmentByTag = fragmentManager.findFragmentByTag(simpleName);
        if (fragmentByTag != null) {
//            Logger.e("fragmentByTag  "+simpleName);
            if (curFragment != null) {
                if (curFragment == fragmentByTag) {
                    return;
                }
                transaction.detach(curFragment);
            }
            transaction.attach(fragmentByTag);
            curFragment = fragmentByTag;
        } else {
            try {
                curFragment = (Fragment) fragment.newInstance();
                transaction.add(R.id.frame_content, curFragment, simpleName);
                // 替换当前的页面
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        transaction.replace(R.id.frame_content, curFragment,simpleName);
        int i = transaction.commitAllowingStateLoss();
    }

    private void tabBarClick(View view) {
        if (preSelected != null) {
            preSelected.setSelected(false);
        }
        view.setSelected(true);
        preSelected = view;
    }

    /**
     * 点击了中间按钮
     */
    private void clickToggleBtn() {
        if (popWindow != null && popWindow.isShowing()) {

            popWindow.dismiss();
        } else {
            showPopupWindow(publishImageView);
        }
        // 改变按钮显示的图片为按下时的状态
        publishImageView.setSelected(true);
    }

    /**
     * 改变显示的按钮图片为正常状态
     */
    private void changeButtonImage() {
        publishImageView.setSelected(false);
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
            popWindow = new PopupWindow(view, dm.widthPixels, LinearLayout.LayoutParams.MATCH_PARENT);

            view.findViewById(R.id.publish_choosework).setOnClickListener(MainActivity.this);
            view.findViewById(R.id.publish_choosescene).setOnClickListener(MainActivity.this);
//            view.findViewById(R.id.publish_fabu).setOnClickListener(MainActivity.this);
//            view.findViewById(R.id.publish_bianji).setOnClickListener(MainActivity.this);
        }

        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        // PopupWindow的显示及位置设置
        // popWindow.showAtLocation(parent, Gravity.FILL, 0, 0);
        popWindow.showAsDropDown(parent, 0, 0);

        popWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // 改变显示的按钮图片为正常状态
                changeButtonImage();
            }
        });

        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
//        popWindow.setFocusable(true);
        // 监听触屏事件
//        popWindow.setTouchInterceptor(new OnTouchListener() {
//            public boolean onTouch(View view, MotionEvent event) {
//                // 改变显示的按钮图片为正常状态
//                changeButtonImage();
//                popWindow.dismiss();
//                return false;
//            }
//        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        takePhotoCrop .getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        takePhotoCrop .getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TakePhotoCrop.IMAGE) {
            if (resultCode == RESULT_OK) {
//                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                ArrayList<String> uristr = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT_URLSTR);

                takePhotoCrop.onCrop(Uri.parse(uristr.get(0)));
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        takePhotoCrop.onRequestPermissionsResult_(requestCode, permissions, grantResults);
    }



    @Override
    public void cropback(TResult result) {

        Logger.i("takeSuccess：" + result.getImage().getPath());

        Bundle bundle = new Bundle();
        bundle.putString("path", result.getImage().getPath());
       bundle.putInt("from", ImageAdjustmentActivity.from_main_choosework);
       GoToHelp.go(this, ImageAdjustmentActivity.class, bundle);
    }
}
