package com.hd.wlj.duohaowan.ui.seach;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.home.classify.SWRVFragment;
import com.hd.wlj.duohaowan.ui.home.classify.SWRVModel;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.MsgContext;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wlj
 */
public class SeachFragment extends BaseFragment {

    @BindView(R.id.seach_spinner)
    Spinner seachSpinner;
    @BindView(R.id.seach_text)
    EditText seachText;
    private String type;
    private SWRVFragment fragment;

    @Override
    protected int getlayout() {
        return R.layout.fragment_seach;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, view);

        initRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void initRecyclerView() {
        if (fragment == null) {

            fragment = SWRVFragment.newInstance(null, SWRVModel.request_type_seach);
        }

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.seach_recyclerciew, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.seach_tv})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.seach_tv:

                type = MsgContext.seachArtist;
                int position = seachSpinner.getSelectedItemPosition();
                if (position == 0) {
                    type = MsgContext.seachWork;
                }
                String text = seachText.getText() + "";
                if (StringUtils.isEmpty(text)) {
                    UIHelper.toastMessage(getContext(), "关键字为空");
                    return;
                }
                fragment.seach(type, text);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(seachText.getWindowToken(), 0);

                break;
        }
    }
}
