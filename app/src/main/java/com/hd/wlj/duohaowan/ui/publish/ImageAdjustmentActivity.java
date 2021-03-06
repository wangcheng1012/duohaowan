package com.hd.wlj.duohaowan.ui.publish;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.publish.border.BorderFragment;
import com.hd.wlj.duohaowan.ui.publish.card.CardFragment;
import com.hd.wlj.duohaowan.ui.publish.complate.ComplateFragment;
import com.hd.wlj.duohaowan.ui.publish.sence.SenceFragment;
import com.hd.wlj.duohaowan.util.ImageUtil2;
import com.hd.wlj.duohaowan.view.RectClickImageView;
import com.hd.wlj.duohaowan.view.WrapContentHeightViewPager;
import com.lling.photopicker.utils.ImageLoader;
import com.orhanobut.logger.Logger;
import com.wlj.base.ui.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 图片调整
 */
public class ImageAdjustmentActivity extends BaseFragmentActivity implements ImageMergeListener {

    /**
     * 主界面 选择作品
     */
    public final static int from_main_choosework = 1;

    /**
     * 完成界面 选择场景
     */
    public final static int from_border_sece = 3;

    public static ArrayList<MergeBitmap> mergeBitmaps = new ArrayList<>();
    @BindView(R.id.adjustment_image)
    RectClickImageView mRectClickImageView;
    @BindView(R.id.adjustment_viewpager)
    WrapContentHeightViewPager mViewpager;
    @BindView(R.id.adjustment_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.adjustment_actionButton)
    ImageView actionButton;
    private MergeBitmap mergeBitmap;
    private String path;
    private int from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_adjustment);
        ButterKnife.bind(this);
//        返回按钮
        Glide.with(this).load(R.drawable.icon_back_white).bitmapTransform(new CropCircleTransformation(this)).into(actionButton);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        initView();

        mTabLayout.setupWithViewPager(mViewpager);
        mViewpager.setScanScroll(false);
    }

    /**
     * 根据 intent 设置显示 和确认操作的 mergeBitmap
     */
    private void initView() {

        Intent intent = getIntent();

        //冲 MainActivity 作品 过来
        from = intent.getIntExtra("from", 1);

        if (from == from_main_choosework) {
            mergeBitmap = new MergeBitmap();
            mergeBitmaps.clear();
            mergeBitmaps.add(mergeBitmap);
            path = intent.getStringExtra("path");
            mergeBitmap.setWorkPath(path);

            ImageLoader.getInstance(this).getBitmap(path, 500, 350, new ImageLoader.BackBitmap() {

                @Override
                public void back(Bitmap mBitmap) {
                    mRectClickImageView.setImageBitmap(mBitmap);
                    mergeBitmap.setWorkBitmap(mBitmap);
                }
            });

            workFragmentAdapter();

        } else if (from == from_border_sece) {
            //相框选择的 完成 到这个页面选场景
            mergeBitmap = mergeBitmaps.get(0);

            if (mergeBitmap != null) {
                //设置 上面图片显示
                mRectClickImageView.setImageBitmap(mergeBitmap.getEndBitmap());
            } else {
                //
            }
            seceFragmentAdapter();

        }

    }

    /**
     * 选择作品的 Fragment和Adapter
     */
    private void workFragmentAdapter() {
        final List<Fragment> fragments = new ArrayList<Fragment>();

        fragments.add(new BorderFragment());
        fragments.add(CardFragment.newInstance(CardFragment.Card1Type));
        fragments.add(CardFragment.newInstance(CardFragment.Card2Type));
        fragments.add(new ComplateFragment());

        mViewpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
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

                switch (position) {
                    case 0:
                        return "画框";
                    case 1:
                        return "卡纸1";
                    case 2:
                        return "卡纸2";
                    case 3:
                        return "完成";
                }
                return super.getPageTitle(position);
            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    mRectClickImageView.setVisibility(View.GONE);
                } else {
                    mRectClickImageView.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mRectClickImageView.getWindowToken(),0);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 选择场景的 Fragment和Adapter
     */
    private void seceFragmentAdapter() {
        final List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new SenceFragment());

        mViewpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
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

                switch (position) {
                    case 0:
                        return "背景";
                }
                return super.getPageTitle(position);
            }
        });
    }


    // --------- 接口回掉 --

    /**
     * @param pub_id
     * @param bitmap 需要合成的图片
     * @param scale  缩放
     * @param type   画框、背景
     */
    @Override
    public void merge(String pub_id, Bitmap bitmap, double scale, MergeBitmap.MergeType type) {
        // 画框，卡纸等为空
        if (bitmap == null) {
            Logger.e("background bitmap为空");
            return;
        }

        switch (type) {
            case background:

                mergeBitmap.setBackgroundId(pub_id);

                mergeBitmap.setBackgroundBitmap(bitmap);

                Bitmap mBitmap = mergeBitmap.buildBackgroundBitmap();
                if (mBitmap != null) {
                    mRectClickImageView.setImageBitmap(mBitmap);
                }
                break;
            case border:

                Rect rect_canzhao = mergeBitmap.getBorkRect_old();

                // 画框的原始Rect没设置
                if (rect_canzhao == null) {
                    return;
                }

                mergeBitmap.setBorderId(pub_id);
                Rect resizeRect = ImageUtil2.resizeRectangle(rect_canzhao, scale);
                mergeBitmap.setBorderBitmap(bitmap);
                mergeBitmap.setWorkRect(resizeRect);
                // 去掉 选中的卡纸效果
                mergeBitmap.setCard1space(0);
                mergeBitmap.setCard2space(0);

                Bitmap mBitmap2 = mergeBitmap.buildFinalBitmap();
                mRectClickImageView.setImageBitmap(mBitmap2);

                break;

        }


    }

    /**
     * space
     *
     * @param space
     * @param type
     */
    @Override
    public void mergeCard(float space, MergeBitmap.MergeType type) {

        Rect rect_canzhao = mergeBitmap.getBorkRect_old();

        // 画框的原始Rect没设置
        if (rect_canzhao == null) {
            return;
        }

        switch (type) {

            case card1:
                mergeBitmap.setCard1space(space);

                break;
            case card2:

                mergeBitmap.setCard2space(space);

                break;
        }

        Bitmap mBitmap = mergeBitmap.buildFinalBitmap();
        mRectClickImageView.setImageBitmap(mBitmap);

    }

    /**
     * 卡纸颜色填充和边框线宽、id
     *
     * @param color
     * @param innerSize
     * @param innerOutside
     * @param type
     */
    @Override
    public void cardFitColorAndSize(String pub_id, int color, float innerSize, float innerOutside, MergeBitmap.MergeType type) {
        Rect rect_canzhao = mergeBitmap.getBorkRect_old();

        // 画框的原始Rect没设置
        if (rect_canzhao == null) {
            return;
        }

        if (type == MergeBitmap.MergeType.card2) {
            mergeBitmap.setCard2Color(color);
            mergeBitmap.setCard2Id(pub_id);

        } else if (type == MergeBitmap.MergeType.card1) {
            mergeBitmap.setCard1Color(color);
            mergeBitmap.setCard1Id(pub_id);
        }

        mergeBitmap.setLineinner(innerSize);
        mergeBitmap.setLineoutside(innerOutside);

        Bitmap mBitmap = mergeBitmap.buildFinalBitmap();
        mRectClickImageView.setImageBitmap(mBitmap);
    }

    /**
     * 设置原始 Rect
     *
     * @param rect
     */
    @Override
    public void setOriginalRect(Rect rect) {

        if (from == from_main_choosework) {
            mergeBitmap.setBorkRect_old(rect);
        } else {
            mergeBitmap.setBackgroundRect(rect);
        }
    }//end

    public MergeBitmap getMergeBitmap() {
        return mergeBitmap;
    }


    @Override
    public void onBackPressed() {
        //冲场景返回 画框 卡兹界面
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f instanceof SenceFragment) {
                int from = getIntent().getIntExtra("from", -1);
                if (from == from_border_sece) {
                    mergeBitmap.setBackgroundBitmap(null);
                    break;
                }
            }

        }

        super.onBackPressed();
    }

    public RectClickImageView getmRectClickImageView() {
        return mRectClickImageView;
    }

}

