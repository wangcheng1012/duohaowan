package com.hd.wlj.duohaowan.ui.publish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.hd.wlj.duohaowan.R;
import com.lling.photopicker.utils.ImageLoader;
import com.wlj.base.ui.BaseFragmentActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片调整
 */
public class ImageAdjustmentActivity extends BaseFragmentActivity {

    @BindView(R.id.adjustment_image)
    ImageView image;
    //    @BindView(R.id.adjustment_viewPagerIndicator)
//    ViewPagerIndicator mViewPagerIndicator;
    @BindView(R.id.adjustment_viewpager)
    ViewPager mViewpager;
    @BindView(R.id.adjustment_tabLayout)
    TabLayout mTabLayout;

    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_adjustment);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");

        ImageLoader.getInstance(this).display(path, image, 500, 500);

        fragments = new ArrayList<Fragment>();
        fragments.add(new AdjustImageFragment());
        fragments.add(new CardFragment());
        fragments.add(new CardFragment());
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
                        return "调整";
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

        mTabLayout.setupWithViewPager(mViewpager);

//        mViewPagerIndicator.setViewPager(mViewpager, 0);
    }
}
