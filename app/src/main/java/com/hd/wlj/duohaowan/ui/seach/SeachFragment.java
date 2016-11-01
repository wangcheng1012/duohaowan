package com.hd.wlj.duohaowan.ui.seach;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hd.wlj.duohaowan.R;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.statusbar.StatusBarUtil;

/**
 * Created by wlj on 2016/10/27.
 */

public class SeachFragment  extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seach, container, false);

//        StatusBarUtil.setTransparentForImageViewInFragment(getActivity(),null);
        return view;
    }


}
