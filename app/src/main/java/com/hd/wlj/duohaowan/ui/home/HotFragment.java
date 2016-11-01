package com.hd.wlj.duohaowan.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.orhanobut.logger.Logger;
import com.wlj.base.bean.Base;
import com.wlj.base.util.img.LoadImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  分类item fragment
 */


public class HotFragment extends Fragment {


    @BindView(R.id.home_hot_img)
    ImageView homeClassifyImg;
    private Base base;


    public HotFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_hot, container, false);

        ButterKnife.bind(this, view);

        showData();
        return view;
    }

    public static HotFragment newInstance(Base homeModel) {

        HotFragment classiryFragment = new HotFragment();
        classiryFragment.setHot(homeModel);

        return classiryFragment;
    }

    private void showData() {

        if(base  != null){
            JSONObject resultJsonObject = base.getResultJsonObject();

            String pics = resultJsonObject.optString("pic");
            if(pics != null && pics.length() > 0 ){
                LoadImage.getinstall().addTask(Urls.HOST + pics,homeClassifyImg);
                LoadImage.getinstall().doTask();
            }
        }else{
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showData();
        }

    }

    public void setHot(Base base) {
        this.base = base;
    }
}
