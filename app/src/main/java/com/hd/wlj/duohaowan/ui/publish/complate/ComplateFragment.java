package com.hd.wlj.duohaowan.ui.publish.complate;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hd.wlj.duohaowan.MainActivity;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.publish.ImageAdjustmentActivity;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComplateFragment extends BaseFragment {


    @BindView(R.id.complate_name)
    EditText complateName;
    @BindView(R.id.complate_year)
    EditText complateYear;
    @BindView(R.id.complate_money)
    EditText complateMoney;
    @BindView(R.id.complate_intro)
    EditText complateIntro;
    private ComplatePresenter presenter;
    private MergeBitmap mergeBitmap;

    @Override
    protected int getlayout() {
        return R.layout.fragment_complate;
    }

    @Override
    protected void initView() {
        view.setMinimumHeight(0);
        ButterKnife.bind(this, view);
        ImageAdjustmentActivity activity = (ImageAdjustmentActivity) getActivity();
        mergeBitmap = activity.getMergeBitmap();

        presenter = new ComplatePresenter(getActivity());
    }

    @OnClick({R.id.complate_save, R.id.complate_backgound})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.complate_save:
                save();

                break;

            case R.id.complate_backgound:

                Bundle bundle = new Bundle();
                bundle.putInt("from", ImageAdjustmentActivity.from_border_sece);
                Intent intent = new Intent(getActivity(), ImageAdjustmentActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);

                break;
        }
    }

    private void save() {
        String name = complateName.getText() + "";
        String year = complateYear.getText() + "";
        String money = complateMoney.getText() + "";
        String intro = complateIntro.getText() + "";

        ComplateModel model = new ComplateModel(getActivity());
        model.setName(name);
        model.setYears(year);
        model.setPrice(money);
        model.setMerger(ImageAdjustmentActivity.mergeBitmaps);

        model.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                UIHelper.toastMessage(getContext(), "发布成功");
                GoToHelp.go(getActivity(), MainActivity.class);
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }


}
