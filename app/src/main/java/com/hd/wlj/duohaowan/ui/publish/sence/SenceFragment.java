package com.hd.wlj.duohaowan.ui.publish.sence;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hd.wlj.duohaowan.MainActivity;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.publish.ImageAdjustmentActivity;
import com.hd.wlj.duohaowan.ui.publish.ImageMergeListener;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
import com.hd.wlj.duohaowan.ui.publish.border.BorderPresenter;
import com.hd.wlj.duohaowan.ui.publish.border.BorderView;
import com.hd.wlj.duohaowan.ui.publish.PublishModel;
import com.hd.wlj.duohaowan.util.UploadChucks;
import com.hd.wlj.duohaowan.view.RectClickImageView;
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
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.orhanobut.logger.Logger;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.AppConfig;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.util.img.ImageFileCache;
import com.wlj.base.util.img.LoadImage;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SenceFragment extends BaseFragment implements BorderView, InvokeListener,TakePhoto.TakeResultListener {

    @BindView(R.id.adjust_Scroll_LinearLayout)
    LinearLayout adjustScrollLinearLayout;
    @BindView(R.id.sence_save)
    Button senceSave;

    private BorderPresenter presenter;
    private ImageMergeListener mListener;
    private Bitmap backgroundBitmap;
    private int from;
    private String pub_id;
    private RectClickImageView rectClickImageView;
    public  final int SenceIMAGE = 44;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    public SenceFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        return  super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getlayout() {
        return R.layout.fragment_sence_image;
    }

    @Override
    protected void initView() {
        view.setMinimumHeight(0);
        ButterKnife.bind(this, view);

        presenter = new BorderPresenter(getActivity());
        presenter.attachView(this);

        Intent intent = getActivity().getIntent();
        from = intent.getIntExtra("from", 1);

        int width = 0;
        int height  = 0;
        ImageAdjustmentActivity activity = (ImageAdjustmentActivity) getActivity();

        MergeBitmap mergeBitmap = activity.getMergeBitmap();
        Bitmap bitmap = mergeBitmap.getBorderBitmap();
        if (bitmap == null) {
            bitmap = mergeBitmap.getWorkBitmap();
        }
        if(bitmap != null){
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        }

        presenter.loadSenceData( width ,  height );
        rectClickImageView = activity.getmRectClickImageView();

        rectClickImageView.setRectAreaOnClickListioner(new RectClickImageView.RectAreaOnClickListioner() {
            @Override
            public void onClick(Rect rect) {
                // 场景 imageview 上的Rect 点击

//                PicChoose();
//
//                mListener.setOriginalRect(rect);
//                //这里其实不会 合成 只是把rect、 pub_id和 backgroundBitmap 设置进去了，
//                merge(pub_id, backgroundBitmap, 1d);
            }

            @Override
            public void onClickOutSide() {

            }
        });

    }

    private void PicChoose() {
        //
        Acp.getInstance(mcontext).request(new AcpOptions.Builder().setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.READ_PHONE_STATE
        ).build(), new AcpListener() {
            @Override
            public void onGranted() {
                //选择 图片
                Intent intent = new Intent(mcontext, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);
                startActivityForResult(intent, SenceIMAGE);
            }

            @Override
            public void onDenied(List<String> permissions) {
                UIHelper.toastMessage(mcontext, permissions.toString() + "权限拒绝");
            }
        });
    }

    private View createHotItem(Base base) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_hot, null);
        final ImageView mImg = (ImageView) view.findViewById(R.id.home_hot_img);

        final JSONObject resultJsonObject = base.getResultJsonObject();

        final String pics = resultJsonObject.optString("pic");
        if (pics != null && pics.length() > 0) {
//            Glide.with(this).load(Urls.HOST + pics).asBitmap().fitCenter().into(mImg);
            // 这里用裁剪了 要私人
            LoadImage.getinstall().addTask(Urls.HOST + pics, mImg).doTask();
        }

        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drawable drawable = ((ImageView) v).getDrawable();
                if(drawable == null){
                    UIHelper.toastMessage(mcontext,"场景未加载完成");
                    return;
                }
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                backgroundBitmap = bitmapDrawable.getBitmap();

                JSONArray backgroundWallWhiteList = resultJsonObject.optJSONArray("backgroundWallWhiteList");

                if (backgroundWallWhiteList != null && backgroundWallWhiteList.length() > 0) {

                    List<Rect>  list = new ArrayList<Rect>();
                    for (int i = 0; i < backgroundWallWhiteList.length(); i++) {

                        JSONObject jsonObject = backgroundWallWhiteList.optJSONObject(i);

                        int position_x = jsonObject.optInt("position_x", 0);
                        int position_y = jsonObject.optInt("position_y", 0);
                        int inner_width = jsonObject.optInt("width", 100);
                        int inner_height = jsonObject.optInt("height", 100);
                        Rect rect = new Rect(position_x, position_y, position_x + inner_width, position_y + inner_height);
                        list.add(rect);

//                        MergeBitmap mergeBitmap = new MergeBitmap();
//                        mergeBitmap.setId(rect);
//                        ImageAdjustmentActivity.mergeBitmaps.add(mergeBitmap);
                    }

//                    if(from == ImageAdjustmentActivity.from_main_choosesece) {
                        rectClickImageView.setImageBitmap(backgroundBitmap);
                        rectClickImageView.setRectList(list);
                        //
                        mListener.setOriginalRect(list.get(0));

                        pub_id = resultJsonObject.optString("pub_id");
                        merge(pub_id, backgroundBitmap, 1d);
                } else {

                    UIHelper.toastMessage(mcontext, "没找到放作品的位置");
                }

            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showBorder(List<Base> list) {

        int width = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 2 - width / 10, LinearLayout.LayoutParams.MATCH_PARENT);
        //
        for (Base base : list) {

            adjustScrollLinearLayout.addView(createHotItem(base), layoutParams);
        }

    }

    @Override
    public void showBorderBili(List<Base> list) {

    }

    // ------------回掉
    private void merge(String pub_id, Bitmap bitmap, double scale) {
        if (mListener != null) {
            mListener.merge(pub_id, bitmap, scale, MergeBitmap.MergeType.background);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ImageMergeListener) {
            mListener = (ImageMergeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ImageMergeListener");
        }
    }//end

    @OnClick(R.id.sence_save)
    public void onClick() {
        Intent intent = getActivity().getIntent();
        PublishModel publishModel = intent.getParcelableExtra("publishModel");

        if ( StringUtils.isEmpty(publishModel.getName()) ||
                StringUtils.isEmpty(publishModel.getYears()) ||
                StringUtils.isEmpty(publishModel.getPrice()) ||
                StringUtils.isEmpty(publishModel.getWidth()) ||
                StringUtils.isEmpty(publishModel.getHeight()) ||
                StringUtils.isEmpty(publishModel.getTag())
                ) {
            UIHelper.toastMessage(getContext(), "请填完作品信息");
            return;
        }


        for ( MergeBitmap tmp : ImageAdjustmentActivity.mergeBitmaps) {

            try {

                uploadChucks( tmp);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 分段上传文件
     *
     * @return
     * @throws Exception
     * @param tmp
     */
    public  void uploadChucks(final MergeBitmap tmp) throws Exception {

        UIHelper.loading("图片上传中……",getActivity());

        Integer file_size = 1024 * 200;

        File file_input = new File(tmp.getWorkPath());

        FileInputStream fis = new FileInputStream(file_input);

        byte[] file_bytes = new byte[file_size];

        Long chunks = file_input.length() / file_size;
        if (file_input.length() % file_size != 0) {
            chunks += 1;
        }

        Long chunk = 0l;
        String name = file_input.getName();

        Long had_upload = 0l;
        while (had_upload < file_input.length()) {
            fis.read(file_bytes);

            final UploadChucks uploadChucks = new UploadChucks(getActivity());
            uploadChucks.setName(name);
            uploadChucks.setFile_bytes(file_bytes);
            uploadChucks.setChunks(chunks);
            uploadChucks.setChunk(chunk);

            AsyncCall request = uploadChucks.Request();
            request.setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                @Override
                public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                    JSONObject jsonObject = base.getResultJsonObject();
                    String fileName = jsonObject.optString("fileName");
                    if(!StringUtils.isEmpty(fileName)){
                        tmp.setWorkPath(fileName);

                        save(tmp);
                    }
                }

                @Override
                public void fail(Exception paramException) {
                    uploadChucks.Request();
                }
            });

//            String uploadChuckOne = uploadChuckOne(file_bytes, name, chunk, chunks);
            /**
             * {"statusCode":"200","message":"上传成功!"}	上传单片
             {"data":{"fileName":"attachFiles/temp/bbd093b570e1201aa17b9f0b3be7960a"},"statusCode":"200","message":"上传成功!"}//上传完成
             */

//            System.out.println(uploadChuckOne);

            had_upload += file_size;
            chunk++;
        }

        fis.close();
    }

    private void save(MergeBitmap tmp) {

//        String name = complateName.getText() + "";
//        String year = complateYear.getText() + "";
//        String money = complateMoney.getText() + "";
//        String intro = complateIntro.getText() + "";
        Intent intent = getActivity().getIntent();
        PublishModel model = intent.getParcelableExtra("publishModel");

        model.setActivity(getActivity());
        model.setHavebachground(true);

        ArrayList<MergeBitmap> objects = new ArrayList<>();
        objects.add(tmp);
        model.setMergeBitmaps(objects);

        model.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {

                UIHelper.loadingClose();
                UIHelper.toastMessage(getContext(), "发布成功");
                GoToHelp.go(getActivity(), MainActivity.class);
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SenceIMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                ArrayList<String> uristr = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT_URLSTR);

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

            }
        }

    }

    //-------------裁剪回掉 等
    @Override
    public void takeSuccess(TResult result) {
        Logger.i("takeSuccess：" + result.getImage().getPath());

//        Bundle bundle = new Bundle();
//        bundle.putString("path", result.getImage().getPath());
//        bundle.putInt("from", ImageAdjustmentActivity.from_sece_work);
//
//        Intent intent = new Intent(getActivity(), ImageAdjustmentActivity.class);
//        intent.putExtras(bundle);
//
//        startActivity(intent);
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
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
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
    }//end
}
