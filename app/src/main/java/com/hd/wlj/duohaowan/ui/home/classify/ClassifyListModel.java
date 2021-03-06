package com.hd.wlj.duohaowan.ui.home.classify;

import android.app.Activity;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.wlj.base.bean.Base;
import com.wlj.base.util.AppConfig;
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

public class ClassifyListModel extends BaseAsyncModle {

    public final static int request_type_seach = 1;

    private String tabBarStr;
    private int classify;

    //搜索
    private String seachType;
    private String seachName;

    public ClassifyListModel() {
    }

    public ClassifyListModel(Activity paramActivity) {
        super(paramActivity);
    }

    public ClassifyListModel(JSONObject jo) {
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
                    case "其他":
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

                    case "预告":
                        httpPost.addParemeter("secondPubConlumnId", "581ef1a1d6c4594f90fa046c");
                        httpPost.addParemeter("tag_type", "2");
                        break;
                    case "最新":
                        httpPost.addParemeter("secondPubConlumnId", "581ef1a1d6c4594f90fa046c");
                        break;
                    default:
                        //画廊,艺术馆
                        httpPost.addParemeter("secondPubConlumnId", "581ef1a1d6c4594f90fa046c");
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
                if ("史学篇".equals(tabBarStr)) {

                    httpPost.addParemeter("pubConlumnId", "5833e67ed6c4592b41d886ee");
                } else if ("现实篇".equals(tabBarStr)) {
                    httpPost.addParemeter("pubConlumnId", "5833e686d6c4592b41d886ef");
                } else {
                    //问学篇
                    httpPost.addParemeter("pubConlumnId", "5833e68dd6c4592b41d886f0");
                    String s = AppConfig.getAppConfig().get(AppConfig.CONF_ID);
                    httpPost.addParemeter("artist_id", s);
                }
                break;

        }

        if (classify == R.id.home_artview && "问答篇".equals(tabBarStr)) {

            asRequestModle.setJiami(true);
        } else {

            asRequestModle.setJiami(false);
        }
        //直接setpage  setpagesize就可以了
        asRequestModle.setHttpPost(httpPost);

    }

    @Override
    public void addRequestParemeter(AsyncRequestModle asRequestModle, int type) throws IOException {

        if (type == request_type_seach) {

            HttpPost httpPost = new HttpPost(Urls.list_pub);
            if (seachType == MsgContext.seachtag) {
                //热门标签点击搜索
                httpPost.addParemeter("tag_string", seachName);
                httpPost.addParemeter("secondPubConlumnId", MsgContext.seachWork);
            } else {
                httpPost.addParemeter("secondPubConlumnId", seachType);
                httpPost.addParemeter("name", seachName);
            }

            asRequestModle.setHttpPost(httpPost);
            asRequestModle.setJiami(false);
        }

    }

    @Override
    public Base parse(JSONObject jsonObject) throws JSONException {
        return new ClassifyListModel(jsonObject);
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
