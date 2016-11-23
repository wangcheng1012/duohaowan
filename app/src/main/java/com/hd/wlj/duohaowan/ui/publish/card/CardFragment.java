package com.hd.wlj.duohaowan.ui.publish.card;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.publish.ImageMergeListener;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
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
    private ImageMergeListener mListener;
    private int cardType;
    private boolean checked;

    public CardFragment() {
        // Required empty public constructor
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

                    if (cardType == Card1Type) {
                        mListener.cardFitColorAndSize(pub_id, parseColor, (float) inner, (float) outside, MergeBitmap.MergeType.card1);
                    } else {

                        mListener.cardFitColorAndSize(pub_id, parseColor, (float) inner, (float) outside, MergeBitmap.MergeType.card2);
                    }
                }

            }
        });

        return view;
    }

    // ------------回掉activity
    private void merge(float space) {

        if (mListener != null) {
            if (cardType == Card1Type) {
//                mListener.merge(pub_id, bitmap, scale, MergeBitmap.MergeType.card1);
                mListener.mergeCard(space, MergeBitmap.MergeType.card1);

            } else {
                mListener.mergeCard(space, MergeBitmap.MergeType.card2);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ImageMergeListener) {
            mListener = (ImageMergeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ImageMergeListener");
        }
    }//end

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar  The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range 0..max where max
     *                 was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (fromUser && checked) {
            BigDecimal divide = MathUtil.divide(progress, 100, 2);
            merge(divide.floatValue());
        }
    }

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * <p>Called when the checked radio button has changed. When the
     * selection is cleared, checkedId is -1.</p>
     *
     * @param group     the group in which the checked radio button has changed
     * @param checkedId the unique identifier of the newly checked radio button
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

//        UIHelper.toastMessage(getContext(),checkedId+"");

        if (checkedId == R.id.card_yes) {
            checked = true;
        } else {

            checked = false;
            merge(0);
        }

    }
}
