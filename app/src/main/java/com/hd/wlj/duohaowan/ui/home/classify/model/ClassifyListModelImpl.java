package com.hd.wlj.duohaowan.ui.home.classify.model;

import android.app.Activity;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.home.classify.contract.ClassifyListContract;
import com.wlj.base.bean.Base;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.MsgContext;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by wlj on 2016/11/03
 */

public class ClassifyListModelImpl extends BaseAsyncModle {


    private String tabBarStr;
    private int classify;

    public ClassifyListModelImpl() {
    }

    public ClassifyListModelImpl(Activity paramActivity) {
        super(paramActivity);
    }

    public ClassifyListModelImpl(JSONObject jo) {
        super(jo);
    }


    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {
        //标签 1/推荐	2/热门	3(热门+推荐)
        HttpPost httpPost = new HttpPost(Urls.list_pub);

        switch (classify) {
            case R.id.home_artist:
                //艺术家


                break;
            case R.id.home_artgallery:
                //艺术馆

                break;

            case R.id.home_workofart:

                //艺术品
                switch (tabBarStr) {
                    case "推荐":
                        httpPost.addParemeter("secondPubConlumnId", "5812ef5478e0802052dd7a2f");
                        httpPost.addParemeter("tag_type", "1");
                        break;
                    case "热门":
                        httpPost.addParemeter("secondPubConlumnId", "5812ef5478e0802052dd7a2f");
                        httpPost.addParemeter("tag_type", "2");
                        break;
                    case "最新":
                        httpPost.addParemeter("secondPubConlumnId", "5812ef5478e0802052dd7a2f");
                        break;
                    case "国画":
                        httpPost.addParemeter("thirdPubConlumnId", "5812ef8078e0802052dd7a31");
                        break;
                    case "书法":
                        httpPost.addParemeter("thirdPubConlumnId", "5812ef7878e0802052dd7a30");
                        break;
                }

                break;

            case R.id.home_artview:
                //艺术观

                break;

        }

        //直接setpage  setpagesize就可以了
        asRequestModle.setHttpPost(httpPost);
        asRequestModle.setJiami(false);
    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new ClassifyListModelImpl(jsonObject);
    }

    public void setTabBarStr(String tabBarStr) {

        this.tabBarStr = tabBarStr;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }
}
