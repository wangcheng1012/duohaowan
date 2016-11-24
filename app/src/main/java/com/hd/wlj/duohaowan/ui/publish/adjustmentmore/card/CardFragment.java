package com.hd.wlj.duohaowan.ui.publish.adjustmentmore.card;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.publish.ImageMergeListener;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
import com.hd.wlj.duohaowan.ui.publish.adjustmentmore.AdjustmentImageActivity;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.MathUtil;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends BaseFragment implements CardView, SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener {

    public final static int Card1Type = 2;
    public final static int Card2Type = 3;

    @BindView(R.id.card_radioGroup)
    RadioGroup cardRadioGroup;
    @BindView(R.id.card_seekBar)
    SeekBar cardSeekBar;
    @BindView(R.id.card_horizontalScrollView_ll)
    LinearLayout cardHorizontalScrollView;

    private CardPresenter presenter;
    private int cardType;
    private boolean checked;
    private AdjustmentImageActivity activity;

    public CardFragment() {
    }

    public static CardFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        CardFragment fragment = new CardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getlayout() {
        return R.layout.fragment_card;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, view);
        view.setMinimumHeight(0);

        Bundle arguments = getArguments();
        cardType = arguments.getInt("type", Card1Type);

        activity = (AdjustmentImageActivity) getActivity();

        presenter = new CardPresenter(getActivity());
        presenter.attachView(this);

        presenter.loadCardData();
        cardSeekBar.setOnSeekBarChangeListener(this);
        cardSeekBar.setMax(100);
        cardSeekBar.setProgress(1);

        cardRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void showData(List<Base> list) {

        int dpToPx = DpAndPx.dpToPx(getContext(), 60);
        int dpToPx_5 = DpAndPx.dpToPx(getContext(), 5);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx, dpToPx);
        params.setMargins(0, 0, dpToPx_5, 0);
        for (Base base : list) {
            cardHorizontalScrollView.addView(createHotItem(base), params);
        }

    }

    private View createHotItem(Base base) {

        final JSONObject resultJsonObject = base.getResultJsonObject();
        String font_color = resultJsonObject.optString("font_color");

        View view = new View(getContext());
        view.setBackgroundColor(Color.parseColor("#" + font_color));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fontColor = resultJsonObject.optString("font_color");
                String pub_id = resultJsonObject.optString("pub_id");
                double inner = resultJsonObject.optDouble("font_size", 3d);
                double outside = resultJsonObject.optDouble("font_size_aroud", 1.5d);

                int parseColor = Color.parseColor("#" + fontColor);

                if (checked) {

                    MergeBitmap mergeBitmap = activity.getMergeBitmap();

                    if (cardType == Card1Type) {

                        mergeBitmap.setCard1Color(parseColor);
                        mergeBitmap.setCard1Id(pub_id);
                    } else {
                        mergeBitmap.setCard2Color(parseColor);
                        mergeBitmap.setCard2Id(pub_id);
                    }
                    mergeBitmap.setLineinner((float) inner);
                    mergeBitmap.setLineoutside((float) outside);

                    Bitmap bitmap = mergeBitmap.buildFinalBitmap();
                    activity.getmRectClickImageView().setImageBitmap(bitmap);
                }

            }
        });

        return view;
    }

    // ------------seekbar
    private void merge(float space) {

        if (cardType == Card1Type) {
            activity.getMergeBitmap().setCard1space(space);
        } else {
            activity.getMergeBitmap().setCard2space(space);
        }

        Bitmap bitmap = activity.getMergeBitmap().buildFinalBitmap();
        activity.getmRectClickImageView().setImageBitmap(bitmap);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (fromUser && checked) {
            BigDecimal divide = MathUtil.divide(progress, 100, 2);
            merge(divide.floatValue());
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.card_yes) {
            checked = true;
        } else {
            checked = false;
            merge(0);
        }

    }
}
