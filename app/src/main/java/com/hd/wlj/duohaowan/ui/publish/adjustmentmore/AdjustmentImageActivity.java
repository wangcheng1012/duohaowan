package com.hd.wlj.duohaowan.ui.publish.adjustmentmore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.publish.ImageMergeListener;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
import com.hd.wlj.duohaowan.ui.publish.PublishModel;
import com.hd.wlj.duohaowan.ui.publish.adjustmentmore.border.BorderFragment;
import com.hd.wlj.duohaowan.ui.publish.adjustmentmore.card.CardFragment;
import com.hd.wlj.duohaowan.ui.publish.adjustmentmore.complate.ComplateFragment;
import com.hd.wlj.duohaowan.view.RectClickImageView;
import com.hd.wlj.duohaowan.view.WrapContentHeightViewPager;
import com.lling.photopicker.utils.ImageLoader;
import com.wlj.base.ui.BaseFragmentActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class AdjustmentImageActivity extends BaseFragmentActivity {

    @BindView(R.id.adjustment_image)
    RectClickImageView mRectClickImageView;
    @BindView(R.id.adjustment_viewpager)
    WrapContentHeightViewPager mViewpager;
    @BindView(R.id.adjustment_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.adjustment_actionButton)
    ImageView actionButton;
//    private MergeBitmap mergeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_adjustment);
        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.icon_back_white).bitmapTransform(new CropCircleTransformation(this)).into(actionButton);

        initView();

        mTabLayout.setupWithViewPager(mViewpager);
        mViewpager.setScanScroll(false);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed();  }
        });

    }

    private void initView() {

        ImageLoader.getInstance(this).getBitmap(getMergeBitmap().getWorkPath(), 500, 350, new ImageLoader.BackBitmap() {
            @Override
            public void back(Bitmap mBitmap) {
                mRectClickImageView.setImageBitmap(mBitmap);
                getMergeBitmap().setWorkBitmap(mBitmap);
            }
        });

        workFragmentAdapter();
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
    }

    public MergeBitmap getMergeBitmap() {

        Intent intent = getIntent();
        // 场景界面 选择作品
        Rect rect = (Rect)intent.getParcelableExtra("rect");
        PublishModel publishModel = (PublishModel)intent.getParcelableExtra("publishModel");
//        String path = intent.getStringExtra("path");
        MergeBitmap mergeBitmap = publishModel.getMergeBitmap(rect);
        return mergeBitmap;
    }

    public RectClickImageView getmRectClickImageView() {
        return mRectClickImageView;
    }
}
