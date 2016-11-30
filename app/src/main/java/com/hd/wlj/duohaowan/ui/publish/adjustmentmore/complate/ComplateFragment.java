package com.hd.wlj.duohaowan.ui.publish.adjustmentmore.complate;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.publish.PublishModel;
import com.hd.wlj.duohaowan.ui.publish.adjustmentmore.AdjustmentImageActivity;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;

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
    @BindView(R.id.complate_width)
    EditText complateWidth;
    @BindView(R.id.complate_height)
    EditText complateHeight;
    @BindView(R.id.complate_tag)
    EditText complateTag;
    private ComplatePresenter presenter;
    private AdjustmentImageActivity activity;
    private PublishModel publishModel;

    @Override
    protected int getlayout() {
        return R.layout.fragment_complate2;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, view);
        activity = (AdjustmentImageActivity) getActivity();


        publishModel = (PublishModel) activity.getIntent().getParcelableExtra("publishModel");
        complateName.setText(publishModel.getName());
        complateYear.setText(publishModel.getYears());
        complateMoney.setText(publishModel.getPrice());
        complateWidth.setText(publishModel.getWidth());
        complateHeight.setText(publishModel.getHeight());
        complateTag.setText(publishModel.getTag());

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

                String width = complateWidth.getText() + "";
                String height = complateHeight.getText() + "";
                String tag = complateTag.getText() + "";


                if (StringUtils.isEmpty(name) ||
                        StringUtils.isEmpty(year) ||
                        StringUtils.isEmpty(money) ||
                        StringUtils.isEmpty(width) ||
                        StringUtils.isEmpty(height) ||
                        StringUtils.isEmpty(tag)
                        ) {
                    UIHelper.toastMessage(activity, "请填完作品信息");
                    return;
                }

                publishModel.setName(name);
                publishModel.setYears(year);
                publishModel.setPrice(money);
                publishModel.setWidth(width);
                publishModel.setHeight(height);
                publishModel.setTag(tag);

                getActivity().setResult(Activity.RESULT_OK, activity.getIntent());
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
