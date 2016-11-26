package com.hd.wlj.duohaowan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hd.wlj.duohaowan.been.Load;
import com.wlj.base.bean.Banner;
import com.wlj.base.bean.Base;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.web.asyn.AsyncCall;
import com.wlj.base.widget.AutoScrollViewPager;
import com.wlj.base.widget.SwitchViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadingActivity extends AppCompatActivity {

    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_loading);
//
        mData = new ArrayList<>();
        Load load = new Load(this);
        load.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {

                for (Base base1 : list) {
                    JSONObject jsonObject = base1.getResultJsonObject();
                    String pic = jsonObject.optString("pic");
                    mData.add(Urls.HOST+pic);
                }

                SwitchViewPager<String> switchViewPager = new SwitchViewPager<String>(LoadingActivity.this,mData){
                    /**
                     * 给滑视图添加页面
                     *
                     * @param autoviewPager
                     */
                    @Override
                    protected void addSwitchPage(AutoScrollViewPager autoviewPager) {
                        super.addSwitchPage(autoviewPager);

                        setScrollLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                        autoviewPager.stopAutoScroll();
                        autoviewPager.setCycle(false);
                    }

                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);

                        if( position%mData.size() >= mData.size()-1 ){

                            GoToHelp.go(LoadingActivity.this,MainActivity.class);
                            finish();
                        }

                    }
                };
                View createview = switchViewPager.createview();
                setContentView(createview);
            }

            @Override
            public void fail(Exception paramException) {

            }
        });




    }

}
