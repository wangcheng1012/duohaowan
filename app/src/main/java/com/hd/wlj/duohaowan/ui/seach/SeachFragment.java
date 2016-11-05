package com.hd.wlj.duohaowan.ui.seach;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.hd.wlj.duohaowan.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  @author wlj
 */
public class SeachFragment extends Fragment {

    @BindView(R.id.seach_spinner)
    Spinner seachSpinner;

    @BindView(R.id.seach_text)
    EditText seachText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seach, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.seach_tv})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.seach_tv:

                break;
        }
    }
}
