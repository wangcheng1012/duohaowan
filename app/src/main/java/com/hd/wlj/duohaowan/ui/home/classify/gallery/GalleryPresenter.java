package com.hd.wlj.duohaowan.ui.home.classify.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.home.classify.arist.AristDetailsActivity;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsModelImpl;
import com.hd.wlj.duohaowan.ui.home.classify.work.WorkDetailsActivity;
import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.wlj.base.bean.Base;
import com.wlj.base.util.AppContext;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.MsgContext;
import com.wlj.base.web.asyn.AsyncCall;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlj on 2016/11/03
 */

public class GalleryPresenter extends BasePresenter<GalleryView> {

    private final DetailsModelImpl model;
    private Activity mActivity;
    private RecyclerView recycerview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Base> datas;
    private LoadMoreWrapper loadMoreWrapper;
    private AsyncCall asyncCall;
    private TextView loadmoretext;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String seachType;
    private String seachText;

    public GalleryPresenter(Activity mActivity) {

        this.mActivity = mActivity;
        model = new DetailsModelImpl(mActivity);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            if (view != null) {//有可能要销毁
                view.showNews((Base) msg.obj);
            }

        }
    };
    @Override
    public void detachView() {
        super.detachView();
        handler.removeCallbacks(null);
    }

    public void loadData(Intent intent) {
        String id = intent.getStringExtra("id");
        model.setId(id);
        model.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                if(view != null){
                    view.showData(base);

                    JSONObject resultJsonObject = base.getResultJsonObject();
                    JSONArray news = resultJsonObject.optJSONArray("news");
                    if(news == null)return;
                    tinner(news);
                }
            }

            @Override
            public void fail(Exception paramException) {

            }
        });

    }


    /**
     * 定时切换新闻
     *
     * @param paramList
     */
    private void tinner(final JSONArray paramList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true ) {

                    for (int i = 0; i < paramList.length(); i++) {

                        Message message = handler.obtainMessage();
                        message.obj = paramList.optJSONObject(i);
                        message.sendToTarget();

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

}