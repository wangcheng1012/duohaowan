package com.hd.wlj.duohaowan.ui.home.classify.artview.ask;

import android.app.Activity;

import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.wlj.base.bean.Base;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import java.util.List;

/**
 * Created by wlj on 2016/12/4.
 */

public class AskedPresenter extends BasePresenter<AskedView> {

    private final AskedModle askedModle;
    private Activity activity;

    public AskedPresenter(Activity activity) {

        this.activity = activity;
          askedModle = new AskedModle(activity);
    }

    public void Asked(String teacher,String title,String context) {
        askedModle.setTeacherid(teacher);
        askedModle.setTitle(title);
        askedModle.setContext(context);
        askedModle.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                UIHelper.toastMessage(activity,"提交成功");
            }

            @Override
            public void fail(Exception paramException) {

            }
        });

    }


    public void loadTeacher() {
        askedModle.Request(AskedModle.TEACHER).setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                if(view != null){

                    view.showTeacher(list);
                }
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }
}
