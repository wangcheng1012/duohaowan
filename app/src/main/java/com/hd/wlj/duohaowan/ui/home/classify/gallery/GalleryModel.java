package com.hd.wlj.duohaowan.ui.home.classify.gallery;

import android.app.Activity;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.wlj.base.bean.Base;
import com.wlj.base.web.HttpPost;
import com.wlj.base.web.asyn.AsyncRequestModle;
import com.wlj.base.web.asyn.BaseAsyncModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by wlj on 2016/11/03
 */

public class GalleryModel extends BaseAsyncModle {

    public final static int request_type_seach = 1;

    private String tabBarStr;
    private int classify;

    //搜索
    private String seachType;
    private String seachName;

    public GalleryModel() {
    }

    public GalleryModel(Activity paramActivity) {
        super(paramActivity);
    }

    public GalleryModel(JSONObject jo) {
        super(jo);
    }


    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle) throws IOException {

        HttpPost httpPost = new HttpPost(Urls.list_pub);

        switch (classify) {
            case R.id.home_artist:
                //艺术家

                /**
                 *  secondPubConlumnId			581407b20e9f110d8cbbdb94	//搜索作家
                 * tag_string							标签名称	国画,油画,书法
                 * tag_type						标签 1/推荐	2/热门	3(热门+推荐)
                 */
                switch (tabBarStr) {
//                    case "推荐":
//                        httpPost.addParemeter("secondPubConlumnId", "581407b20e9f110d8cbbdb94");
//                        httpPost.addParemeter("tag_type", "1");
//                        break;
                    case "热门":
                        httpPost.addParemeter("secondPubConlumnId", "581407b20e9f110d8cbbdb94");
                        httpPost.addParemeter("tag_type", "2");
                        break;
                    case "最新":
                        httpPost.addParemeter("secondPubConlumnId", "581407b20e9f110d8cbbdb94");
                        break;
                    default:
                        //国画,油画,书法
                        httpPost.addParemeter("secondPubConlumnId", "581407b20e9f110d8cbbdb94");
                        httpPost.addParemeter("tag_string", tabBarStr);
                        break;

                }
                break;
            case R.id.home_artgallery:
                //艺术馆
                switch (tabBarStr) {

                    case "热门":
                        httpPost.addParemeter("secondPubConlumnId", "581407b20e9f110d8cbbdb94");
                        httpPost.addParemeter("tag_type", "2");
                        break;
                    case "最新":
                        httpPost.addParemeter("secondPubConlumnId", "581407b20e9f110d8cbbdb94");
                        break;
                    default:
                        //画廊,艺术馆
                        httpPost.addParemeter("secondPubConlumnId", "581407b20e9f110d8cbbdb94");
                        httpPost.addParemeter("tag_string", tabBarStr);
                        break;
                }

                break;

            case R.id.home_workofart:
                //标签 1/推荐	2/热门	3(热门+推荐)
                //艺术品
                switch (tabBarStr) {
                    case "最新":
                        httpPost.addParemeter("secondPubConlumnId", "5812ef5478e0802052dd7a2f");
                        break;
                    default:
                        httpPost.addParemeter("secondPubConlumnId", "5812ef5478e0802052dd7a2f");
                        httpPost.addParemeter("tag_string", tabBarStr);
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
    public void addRequestParemeter(AsyncRequestModle asRequestModle, int type) throws IOException {

        if (type == request_type_seach) {

            HttpPost httpPost = new HttpPost(Urls.list_pub);
            httpPost.addParemeter("name", seachName);
            httpPost.addParemeter("secondPubConlumnId", seachType);

            asRequestModle.setHttpPost(httpPost);
            asRequestModle.setJiami(false);
        }

    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new GalleryModel(jsonObject);
    }

    public void setTabBarStr(String tabBarStr) {

        this.tabBarStr = tabBarStr;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public void setSeachType(String seachType) {
        this.seachType = seachType;
    }

    public void setSeachName(String seachName) {
        this.seachName = seachName;
    }
}
