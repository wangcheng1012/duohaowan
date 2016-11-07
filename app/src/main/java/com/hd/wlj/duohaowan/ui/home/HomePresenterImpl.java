package com.hd.wlj.duohaowan.ui.home;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.hd.wlj.duohaowan.ui.home.HomeView;
import com.hd.wlj.duohaowan.ui.home.HomeModelImpl;
import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.wlj.base.bean.Base;
import com.wlj.base.web.asyn.AsyncCall;

import java.util.List;

/**
* Created by wlj on 2016/10/26
*/

public class HomePresenterImpl extends BasePresenter<HomeView> {


    private final HomeModelImpl homeModel;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            if (view != null) {//有可能要销毁
                view.showNews((Base) msg.obj);
            }

        }
    };

    public HomePresenterImpl(Activity activity){
          homeModel = new HomeModelImpl(activity);
    }

    @Override
    public void detachView() {

        handler.removeCallbacks(null);
        view = null;
    }

    public void loadBannerData() {

        homeModel.loadBannerData(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int paramInt) {
                if (view != null) {
                    view.showBanner(paramList);
                }
            }

            @Override
            public void fail(Exception paramException) {

            }
        });

    }

    public void loadNews() {

        homeModel.loadNews(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int paramInt) {

                tinner(paramList);
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }

    public void loadHot(){

        homeModel.Request(HomeModelImpl.hot).setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int paramInt) {
                if(view != null) {
                    view.showHot(paramList);
                }
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }

    public  void loadNewest(){

        homeModel.Request(HomeModelImpl.newest ).setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int paramInt) {
                if(view != null) {
                    view.showNewest(paramList);
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
    private void tinner(final List<Base> paramList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {

                    for (int i = 0; i < paramList.size(); i++) {

                        Message message = handler.obtainMessage();
                        message.obj = paramList.get(i);
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