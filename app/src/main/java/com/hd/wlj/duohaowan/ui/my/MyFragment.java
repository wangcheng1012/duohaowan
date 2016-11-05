package com.hd.wlj.duohaowan.ui.my;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.LoginActivity;
import com.hd.wlj.duohaowan.view.ImgInpImg;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.GoToHelp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends BaseFragment {


    @BindView(R.id.my_dy)
    TextView myDy;
    @BindView(R.id.my_guanzhu)
    TextView myGuanzhu;
    @BindView(R.id.my_fensi)
    TextView myFensi;
    @BindView(R.id.my_card)
    ImgInpImg myCard;
    @BindView(R.id.my_work)
    ImgInpImg myWork;
    @BindView(R.id.my_comment)
    ImgInpImg myComment;
    @BindView(R.id.my_jiaoyijilu)
    ImgInpImg myJiaoyijilu;
    @BindView(R.id.my_suggest)
    ImgInpImg mySuggest;
    @BindView(R.id.my_headimage)
    ImageView myHeadimage;
    @BindView(R.id.my_loginLayout_text)
    TextView myLoginLayoutText;

    public MyFragment() {
    }

    @Override
    protected int getlayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, view);

        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .placeholder(R.drawable.project_bg)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .crossFade(1000)
                .into(myHeadimage);

        initImgInpImg(myCard, R.drawable.my_card, "我的名片");
        initImgInpImg(myWork, R.drawable.my_work, "我的作品");
        initImgInpImg(myComment, R.drawable.my_comment, "我的评论");
        initImgInpImg(myJiaoyijilu, R.drawable.my_transactionrecord, "我的交易记录");
        initImgInpImg(mySuggest, R.drawable.my_suggestion, "意见反馈");
    }


    /**
     * 图片-text-右箭头
     *
     * @param iii                      ImgInpImg
     * @param iconFontView1drawableRes iconFontView1 的图片资源
     * @param editText                 editTextView的text
     */
    private void initImgInpImg(ImgInpImg iii, int iconFontView1drawableRes, String editText) {

        Drawable drawable = getmDrawable(iconFontView1drawableRes);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        iii.getIconFontView1().setCompoundDrawables(drawable, null, null, null);

        EditText editTextView = iii.getEditTextView();
        editTextView.setText(editText);
        editTextView.setEnabled(false);
        iii.setEditTextViewMaginLeft(20);

        Drawable drawable1 = getmDrawable(R.drawable.right_hui);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        iii.getIconFontView2().setCompoundDrawables(drawable1, null, null, null);
    }

    private Drawable getmDrawable(int drawableId) {
        Drawable drawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(drawableId, null);
        } else {
            drawable = getResources().getDrawable(drawableId);
        }
        return drawable;
    }


    @OnClick(R.id.my_loginLayout)
    public void onClick() {
        GoToHelp.go(getActivity(), LoginActivity.class);
    }


}
