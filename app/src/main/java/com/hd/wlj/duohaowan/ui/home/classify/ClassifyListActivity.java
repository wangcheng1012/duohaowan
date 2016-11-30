package com.hd.wlj.duohaowan.ui.home.classify;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hd.wlj.duohaowan.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassifyListActivity extends AppCompatActivity {

    @BindView(R.id.classify_list_tablayout)
    TabLayout tablayout;
    @BindView(R.id.classify_list_viewpager)
    ViewPager viewpager;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.classify_list_black)
    ImageView classifyListBlack;
    @BindView(R.id.toolbar_right)
    Button toolbarRight;
    private ArrayList<String> tabBarList;
    private ArrayList<Fragment> fragments;
    private int classify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_list);
        ButterKnife.bind(this);

        classifyListBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        initData();
        initViewPage();
    }

    private void initViewPage() {

        tablayout.setTabMode(TabLayout.MODE_FIXED);

        viewpager.setAdapter(
                new FragmentStatePagerAdapter(getSupportFragmentManager()) {

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

                        return tabBarList.get(position % fragments.size());
                    }
                }
        );

        tablayout.setupWithViewPager(viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (classify == R.id.home_artview && position == 2) {
                    toolbarRight.setVisibility(View.VISIBLE);
                } else {
                    toolbarRight.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Intent 中含有 id，title
     * 数据初始化
     */
    private void initData() {

        tabBarList = new ArrayList<>();
        fragments = new ArrayList<>();

        Intent intent = getIntent();
        classify = intent.getIntExtra("classify", -1);
        String title = intent.getStringExtra("title");
//title
        toolbarTitle.setText(title);
//        初始化tabbar
        switch (classify) {

            case R.id.home_artist:
//                艺术家
//              tabBarList.clear();
                tabBarList.add("热门");
                tabBarList.add("国画");
                tabBarList.add("油画");
                tabBarList.add("书法");
                tabBarList.add("其他");

                for (String tmpBarStr : tabBarList) {
                    fragments.add(ClassifyListFragment.newInstance(tmpBarStr, classify));
                }

                break;
            case R.id.home_artgallery:
                //艺术馆
//                tabBarList.add("推荐");
                tabBarList.add("预告");
                tabBarList.add("最新");
                tabBarList.add("艺术馆");
                tabBarList.add("得意艺术馆");
                for (String tmpBarStr : tabBarList) {
                    fragments.add(ClassifyListFragment.newInstance(tmpBarStr, classify));
                }
                break;
            case R.id.home_workofart:
//                艺术品
                tabBarList.add("最新");
                tabBarList.add("国画");
                tabBarList.add("油画");
                tabBarList.add("书法");
                tabBarList.add("其他");
                for (String tmpBarStr : tabBarList) {
                    fragments.add(ClassifyListFragment.newInstance(tmpBarStr, classify));
                }
                break;
            case R.id.home_artview:
                //艺术观
                tabBarList.add("史学篇");
                tabBarList.add("现实篇");
                tabBarList.add("问学篇");

                //不全用ClassifyListFragment
                for (String tmpBarStr : tabBarList) {
//                    if("问学篇".equals(tmpBarStr)) {
                    fragments.add(ClassifyListFragment.newInstance(tmpBarStr, classify));
//                    }else{
//                        fragments.add(ClassifyListFragment.newInstance(tmpBarStr, classify));
//
//                    }
                }

                break;
        }
    }


}
