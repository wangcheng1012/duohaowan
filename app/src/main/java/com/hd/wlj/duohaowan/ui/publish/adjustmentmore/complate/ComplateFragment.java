package com.hd.wlj.duohaowan.ui.publish.adjustmentmore.complate;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.hd.wlj.duohaowan.App;
import com.hd.wlj.duohaowan.MainActivity;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.LoginActivity;
import com.hd.wlj.duohaowan.ui.publish.ImageAdjustmentActivity;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
import com.hd.wlj.duohaowan.ui.publish.PublishModel;
import com.hd.wlj.duohaowan.ui.publish.adjustmentmore.AdjustmentImageActivity;
import com.hd.wlj.duohaowan.util.UploadChucks;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
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
    private AdjustmentImageActivity activity;

    @Override
    protected int getlayout() {
        return R.layout.fragment_complate2;
    }

    @Override
    protected void initView() {
        view.setMinimumHeight(0);
        ButterKnife.bind(this, view);
        activity = (AdjustmentImageActivity) getActivity();

        presenter = new ComplatePresenter(getActivity());
    }

    @OnClick({R.id.complate_backgound})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.complate_backgound:

                String name = complateName.getText() + "";
                String year = complateYear.getText() + "";
                String money = complateMoney.getText() + "";
                String intro = complateIntro.getText() + "";

                Intent intent1 = activity.getIntent();
                PublishModel publishModel = (PublishModel)intent1.getParcelableExtra("publishModel");
                publishModel.setName(name);
                publishModel.setYears(year);
                publishModel.setPrice(money);

                getActivity().setResult(Activity.RESULT_OK,intent1);
                getActivity().finish();
//                Bundle bundle = new Bundle();
//                bundle.putInt("from", ImageAdjustmentActivity.from_border_sece);
//                Intent intent = new Intent(getActivity(), ImageAdjustmentActivity.class);
//                intent.putExtras(bundle);
//
//                startActivity(intent);


                break;
        }
    }

}
