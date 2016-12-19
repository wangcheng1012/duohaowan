package com.hd.wlj.duohaowan.ui.seach;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.home.classify.ClassifyListFragment;
import com.hd.wlj.duohaowan.ui.home.classify.ClassifyListModel;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.DpAndPx;
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
    private ClassifyListFragment fragment;

    @Override
    protected int getlayout() {
        return R.layout.fragment_seach;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, view);

        initRecyclerView();

        String[] spin_arry = getResources().getStringArray(R.array.seachSpenner);
        CustomArrayAdapter<String> mAdapter = new CustomArrayAdapter<String>(getContext(), spin_arry);
        seachSpinner.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void initRecyclerView() {
        if (fragment == null) {

            fragment = ClassifyListFragment.newInstance(null, ClassifyListModel.request_type_seach);

            FragmentManager manager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(R.id.seach_recyclerciew, fragment);
            fragmentTransaction.commitAllowingStateLoss();

        }

    }

    @OnClick({R.id.seach_tv, R.id.seach_tag1, R.id.seach_tag2, R.id.seach_tag3, R.id.seach_tag4})
    public void onClick(View view) {


        type = MsgContext.seachtag;
        String text = seachText.getText() + "";
        switch (view.getId()) {

            case R.id.seach_tv:

                int position = seachSpinner.getSelectedItemPosition();
                if (position == 0) {
                    type = MsgContext.seachWork;
                } else if (position == 1) {
                    type = MsgContext.seachArtist;
                }

                if (StringUtils.isEmpty(text)) {
                    UIHelper.toastMessage(getContext(), "关键字为空");
                    return;
                }
                break;
            case R.id.seach_tag1:
                text = "国画";
                break;
            case R.id.seach_tag2:
                text= "油画";
                break;
            case R.id.seach_tag3:
                text = "书法";
                break;
            case R.id.seach_tag4:
                text = "其他";
                break;
        }

        fragment.seach(type, text);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(seachText.getWindowToken(), 0);
    }


    static class CustomArrayAdapter<T> extends ArrayAdapter<T> {
        public CustomArrayAdapter(Context ctx, T[] objects) {
            super(ctx, android.R.layout.simple_spinner_item, objects);
        }

        //其它构造函数

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            // simple_spinner_item 有 android.R.id.text1 TextView视图:

        /* if(isDroidX) {*/
            TextView text = (TextView) view.findViewById(android.R.id.text1);
            text.setGravity(Gravity.CENTER_VERTICAL);
            text.setHeight(DpAndPx.dpToPx(parent.getContext(), 45));
//            int dpToPx_5 = DpAndPx.dpToPx(convertView.getContext(), 5);
//            int dpToPx_5 = DpAndPx.dpToPx(convertView.getContext(), 10);
//            text.setPadding(dpToPx_5,);
            text.setTextColor(Color.BLACK);//choose your color :)
        /*}*/

            return view;

        }
    }
}
