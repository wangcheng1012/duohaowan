package com.hd.wlj.duohaowan.ui.publish.adjustmentmore.sencemore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.hd.wlj.duohaowan.App;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.LoginActivity;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
import com.hd.wlj.duohaowan.ui.publish.PublishModel;
import com.hd.wlj.duohaowan.ui.publish.adjustmentmore.AdjustmentImageActivity;
import com.hd.wlj.duohaowan.ui.publish.border.BorderModel;
import com.hd.wlj.duohaowan.util.TakePhotoCrop;
import com.hd.wlj.duohaowan.view.RectClickImageView;
import com.jph.takephoto.model.TResult;
import com.lling.photopicker.PhotoPickerActivity;
import com.lling.photopicker.utils.ImageLoader;
import com.orhanobut.logger.Logger;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.util.img.LoadImage;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 多图 场景选择
 */
public class SenceMoreActivity extends BaseFragmentActivity implements SenceMoreView, TakePhotoCrop.CropBack {

    public final static int CHOOSE = 2013;

    @BindView(R.id.sence_more_image)
    RectClickImageView image;
    @BindView(R.id.sence_more_actionButton)
    ImageView black;
    @BindView(R.id.sence_more_recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.sence_more__tabLayout)
    TabLayout tabLayout;

    private SenceMorePresenter presenter;
    private PublishModel publishModel;
    private CommonAdapter<Base> commonAdapter;
    private LinearLayout.LayoutParams layoutParams;
    private TakePhotoCrop takePhotoCrop;
    private Rect clickRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        takePhotoCrop = new TakePhotoCrop(this, this);
        takePhotoCrop.getTakePhoto().onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sence_more);
        ButterKnife.bind(this);

        publishModel = new PublishModel(this);

        initTitle();

        presenter = new SenceMorePresenter(this);
        presenter.attachView(this);

        initRecyclerview();

        rectClickImageViewClick();

        //单图
        presenter.load(BorderModel.count_single);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                String text = tab.getText() + "";
//                UIHelper.toastMessage(SenceMoreActivity.this,text);
                if ("单图".equals(text)) {
                    presenter.load(BorderModel.count_single);
                } else {
                    presenter.load(BorderModel.count_more);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void rectClickImageViewClick() {

        image.setRectAreaOnClickListioner(new RectClickImageView.RectAreaOnClickListioner() {
            @Override
            public void onClick(Rect rect) {

                clickRect = rect;
                //选图 裁剪
                takePhotoCrop.photoPicker();
            }

            @Override
            public void onClickOutSide() {

            }
        });

    }

    private void initRecyclerview() {

        int width = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        layoutParams = new LinearLayout.LayoutParams(width / 2 - width / 10, DpAndPx.dpToPx(this, 180));

        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Base> mDate = new ArrayList<>();

        CommonAdapter<Base> commonAdapter = new CommonAdapter<Base>(getApplicationContext(), R.layout.fragment_home_hot, mDate) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, Base base, int position) {
                View convertView = holder.getConvertView();
                convertView.setTag(R.id.tag_first, base);
                convertView.setLayoutParams(layoutParams);

                ImageView mImg = holder.getView(R.id.home_hot_img);
                mImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
                JSONObject resultJsonObject = base.getResultJsonObject();

                String pics = resultJsonObject.optString("pic");
                if (pics != null && pics.length() > 0) {
                    Glide.with(SenceMoreActivity.this).load(Urls.HOST + pics).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(mImg);
                    // 这里用裁剪了 要私人
//                    LoadImage.getinstall().addTask(Urls.HOST + pics, mImg).doTask();
                }
            }
        };

        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                itemClick(view);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        //loadmore
        presenter.LoadMoreWrapper(commonAdapter, recyclerview, mDate);

    }

    /**
     * Recyclerview 的 Click
     *
     * @param view
     */
    private void itemClick(View view) {

        Base base = (Base) view.getTag(R.id.tag_first);
        JSONObject resultJsonObject = base.getResultJsonObject();
        //
        JSONArray backgroundWallWhiteList = resultJsonObject.optJSONArray("backgroundWallWhiteList");

        if (backgroundWallWhiteList != null && backgroundWallWhiteList.length() > 0) {

            List<Rect> list = new ArrayList<Rect>();
            List<MergeBitmap> listMergeBitmap = new ArrayList<>();

            //Rect
            for (int i = 0; i < backgroundWallWhiteList.length(); i++) {

                JSONObject jsonObject = backgroundWallWhiteList.optJSONObject(i);

                int position_x = jsonObject.optInt("position_x", 0);
                int position_y = jsonObject.optInt("position_y", 0);
                int inner_width = jsonObject.optInt("width", 100);
                int inner_height = jsonObject.optInt("height", 100);
                Rect rect = new Rect(position_x, position_y, position_x + inner_width, position_y + inner_height);
                list.add(rect);
                handleMergeBitmap(resultJsonObject, listMergeBitmap, rect);
            }
            publishModel.setMergeBitmaps(listMergeBitmap);
            image.setRectList(list);
            // 显示
            String pics = resultJsonObject.optString("pic");
            Glide.with(SenceMoreActivity.this).load(Urls.HOST + pics).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).fitCenter().into(image);
//            LoadImage.getinstall().addTask(Urls.HOST + pics, image).doTask();
            //
        } else {
            UIHelper.toastMessage(SenceMoreActivity.this, "没找到放作品的位置");
        }
    }

    /**
     * chu初始化MergeBitmap 和添加 背景数据
     *
     * @param resultJsonObject
     * @param listMergeBitmap
     * @param rect
     */
    private void handleMergeBitmap(JSONObject resultJsonObject, List<MergeBitmap> listMergeBitmap, Rect rect) {
        // 添加背景数据
        MergeBitmap mergeBitmap = new MergeBitmap();
        mergeBitmap.setId(rect);
        mergeBitmap.setBackgroundRect(rect);
        mergeBitmap.setBackgroundId(resultJsonObject.optString("pub_id"));
        mergeBitmap.setBackgroundPath(resultJsonObject.optString("pic"));
        listMergeBitmap.add(mergeBitmap);
    }

    private void initTitle() {
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //-------------- 选图裁剪-----
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        takePhotoCrop.getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        takePhotoCrop.getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == TakePhotoCrop.IMAGE) {
            //选图
            ArrayList<String> uristr = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT_URLSTR);
            takePhotoCrop.onCrop(Uri.parse(uristr.get(0)));
        } else if (requestCode == CHOOSE) {
            //裁剪
            Rect rect = (Rect) data.getParcelableExtra("rect");
            publishModel = null;
            publishModel = (PublishModel) data.getParcelableExtra("publishModel");
            MergeBitmap mergeBitmap = publishModel.getMergeBitmap(rect);
            publishModel.onClone(mergeBitmap);//clone
            //合成
            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            mergeBitmap.setBackgroundBitmap(bitmap);

            mergeBitmap.buildBackgroundMore(SenceMoreActivity.this, new ImageLoader.BackBitmap() {
                @Override
                public void back(Bitmap mBitmap) {
                    image.setImageBitmap(mBitmap);
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePhotoCrop.onRequestPermissionsResult_(requestCode, permissions, grantResults);
    }

    /**
     * 裁剪返回
     *
     * @param result
     */
    @Override
    public void cropback(TResult result) {

        Logger.i("takeSuccess：" + result.getImage().getPath());

        MergeBitmap mergeBitmap = publishModel.getMergeBitmap(clickRect);
        mergeBitmap.setWorkPath(result.getImage().getPath());

        Bundle bundle = new Bundle();
        bundle.putParcelable("publishModel", publishModel);
        bundle.putParcelable("rect", clickRect);
        Intent intent = new Intent(SenceMoreActivity.this, AdjustmentImageActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CHOOSE);

    } //end

    /**
     * 发布- 分段上传
     */
    @OnClick(R.id.sence_save)
    public void onClick() {
        if ( StringUtils.isEmpty(publishModel.getName()) ||
                        StringUtils.isEmpty(publishModel.getYears()) ||
                        StringUtils.isEmpty(publishModel.getPrice()) ||
                        StringUtils.isEmpty(publishModel.getWidth()) ||
                        StringUtils.isEmpty(publishModel.getHeight()) ||
                        StringUtils.isEmpty(publishModel.getTag())
                ) {
            UIHelper.toastMessage(getApplicationContext(), "请填完作品信息");
            return;
        }

        App applicationContext = (App) getApplicationContext();
        if (!applicationContext.islogin()) {
            UIHelper.toastMessage(applicationContext, "请先登录");
            GoToHelp.go(this, LoginActivity.class);
            return;
        }
        List<MergeBitmap> mergeBitmaps = publishModel.getMergeBitmaps();
        for (int i = 0; i < mergeBitmaps.size(); i++) {
            MergeBitmap mergeBitmap = mergeBitmaps.get(i);
            final String workPath = mergeBitmap.getWorkPath();
            if (StringUtils.isEmpty(workPath)) {
                UIHelper.toastMessage(getApplicationContext(), "请选完作品");
                return;
            }

            new up(this).execute(new File(workPath), i);

        }
    }

    /**
     * 发布- 分段上传成功 - save
     *
     * @param fileName
     * @param i
     */
    @Override
    public void uploadComplte(String fileName, int i) {

        List<MergeBitmap> mergeBitmaps = publishModel.getMergeBitmaps();
        mergeBitmaps.get(i).setWorkPath(fileName);

        if (i + 1 == mergeBitmaps.size()) {
            //
            presenter.save(publishModel);
        }
    }

    @Override
    public void proess(long chunks, long chunk) {

    }

    /**
     * 分段上传 任务
     */
    private class up extends AsyncTask<Object, Void, Object> {

        private Activity mActivity;

        public up(Activity mActivity) {

            this.mActivity = mActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            UIHelper.loading("图片上传中……", mActivity);
        }

        @Override
        protected Object doInBackground(Object... params) {

            try {

                presenter.uploadChucks((File) params[0], (int) params[1]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            UIHelper.loadingClose();
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}