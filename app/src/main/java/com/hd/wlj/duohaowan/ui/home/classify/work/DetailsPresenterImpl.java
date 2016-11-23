package com.hd.wlj.duohaowan.ui.home.classify.work;

import android.app.Activity;
import android.text.Editable;
import android.widget.EditText;

import com.hd.wlj.duohaowan.been.Comment;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsView;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsModelImpl;
import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.wlj.base.bean.Base;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by wlj on 2016/10/30
 */

public class DetailsPresenterImpl extends BasePresenter<DetailsView> {

    private final DetailsModelImpl detailsModel;
    private Activity mActivity;

    public DetailsPresenterImpl(Activity mActivity) {

        this.mActivity = mActivity;
        detailsModel = new DetailsModelImpl();

    }

    public void loadDetailsData(String pubid) {
        detailsModel.setId(pubid);
        detailsModel.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int paramInt) {
                if (view != null) {
                    view.showDetailsData(paramBase);
                }
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }

    /**
     * 评价
     * @param jsonData
     * @param s
     */
    public void comment(JSONObject jsonData, EditText s) {
        String text = s.getText() + "";
        if (StringUtils.isEmpty(text)) {
            UIHelper.toastMessage(mActivity, "评论内容不能为空");
            return;
        }
        if (jsonData == null) return;

        Comment comment = new Comment(mActivity);

        comment.setContent(text);
        comment.setId(jsonData.optString("pub_id"));
        comment.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                if (view != null) {
                    view.showComment();
                }
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }
}