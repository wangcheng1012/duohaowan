package com.hd.wlj.duohaowan;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.hd.wlj.duohaowan.ui.follow.FollowFragment;
import com.hd.wlj.duohaowan.ui.home.HomeFragment;
import com.hd.wlj.duohaowan.ui.my.MyFragment;
import com.hd.wlj.duohaowan.ui.publish.ImageAdjustmentActivity;
import com.hd.wlj.duohaowan.ui.seach.SeachFragment;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TException;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.lling.photopicker.PhotoPickerActivity;
import com.lling.photopicker.utils.OtherUtils;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.orhanobut.logger.Logger;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.AppConfig;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.Log;
import com.wlj.base.util.UIHelper;
import com.wlj.base.util.img.ImageFileCache;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseFragmentActivity implements OnClickListener, HomeFragment.OnFragmentInteractionListener
        , TakePhoto.TakeResultListener, InvokeListener
{

    //调用系统相册-选择图片
    private static final int IMAGE = 166;
    // 定义布局对象
    private FrameLayout homeFl, seachFl, followFl, myFl;
    // 定义图片组件对象
    private ImageView homeIv, seachIv, followIv, myIv;
    // 定义按钮图片组件
    private ImageView toggleImageView, plusImageView;
    // 定义PopupWindow
    private PopupWindow popWindow;
    // 获取手机屏幕分辨率的类
    private DisplayMetrics dm;
    private Fragment curFragment;
    private View preSelected;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
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
        toggleImageView = (ImageView) findViewById(R.id.toggle_btn);
        plusImageView = (ImageView) findViewById(R.id.plus_btn);

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
            case R.id.toggle_btn:
                clickToggleBtn();

                break;

            case R.id.publish_choosework:

                Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.READ_PHONE_STATE
                ).build(), new AcpListener() {
                    @Override
                    public void onGranted() {

                        //选择 图片
                        Intent intent = new Intent(MainActivity.this, PhotoPickerActivity.class);
                        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
                        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);
                        startActivityForResult(intent, IMAGE);

                        popWindow.dismiss();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        UIHelper.toastMessage(MainActivity.this, permissions.toString() + "权限拒绝");
                    }
                });


                break;
            case R.id.publish_choosescene:

                Bundle bundle = new Bundle();
                bundle.putInt("from", ImageAdjustmentActivity.from_main_choosesece);
                GoToHelp.go(this, ImageAdjustmentActivity.class, bundle);

                popWindow.dismiss();
                break;
//            case R.id.publish_fabu:
//
////                popWindow.dismiss();
//                break;
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
            showPopupWindow(toggleImageView);
        }
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
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
//        //获取图片路径
//        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
//
//            Uri selectedImage = data.getData();
//            String[] filePathColumns = {MediaStore.Images.Media.DATA};
//            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
//            c.moveToFirst();
//            int columnIndex = c.getColumnIndex(filePathColumns[0]);
//            String imagePath = c.getString(columnIndex);
//            showImage(imagePath);
//
//            c.close();
//        }

        if (requestCode == IMAGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                ArrayList<String> uristr = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT_URLSTR);

//                UIHelper.toastMessage(this, uristr.get(0) + "");

                CropOptions cropOptions = new CropOptions.Builder()
//                        .setAspectX(1).setAspectY(1)
                        .setWithOwnCrop(false).create();
                try {
//                    Uri uri = Uri.parse("file://"+result.get(0));// 这样也可以，开始傻了，还加了uristr
                    Uri uri = Uri.parse(uristr.get(0));

                    String imagePath = AppConfig.getAppConfig().getImagePath() + ImageFileCache.CACHDIR + File.separator;
//
                    File file = new File(imagePath + String.valueOf(System.currentTimeMillis()).substring(3) + "crop" + ".jpg");
                    if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                    Uri imageUri = Uri.fromFile(file);

                    takePhoto.onCrop(uri, imageUri, cropOptions);

                } catch (TException e) {
                    e.printStackTrace();
                }

//                if(result.size() >0 ){
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("path",result.get(0));
//                    GoToHelp.go(this,ImageAdjustmentActivity.class,bundle);
//                }


            }
        }

    }

    @Override
    public void takeSuccess(TResult result) {
        Logger.i("takeSuccess：" + result.getImage().getPath());

        Bundle bundle = new Bundle();
        bundle.putString("path", result.getImage().getPath());
        bundle.putInt("from", ImageAdjustmentActivity.from_main_choosework);
        GoToHelp.go(this, ImageAdjustmentActivity.class, bundle);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Logger.i("takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Logger.i(getResources().getString(R.string.msg_operation_canceled));
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }
}
