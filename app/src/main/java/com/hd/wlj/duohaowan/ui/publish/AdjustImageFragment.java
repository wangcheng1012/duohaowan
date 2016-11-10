package com.hd.wlj.duohaowan.ui.publish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hd.wlj.duohaowan.R;
import com.wlj.base.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdjustImageFragment extends BaseFragment {


    @BindView(R.id.adjust_height_tv)
    TextView adjustHeightTv;
    @BindView(R.id.adjust_height_sb)
    SeekBar adjustHeightSb;
    @BindView(R.id.adjust_width_tv)
    TextView adjustWidthTv;
    @BindView(R.id.adjust_width_sb)
    SeekBar adjustWidthSb;

    public AdjustImageFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getlayout() {
        return R.layout.fragment_adjust_image;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, view);


    }

}
