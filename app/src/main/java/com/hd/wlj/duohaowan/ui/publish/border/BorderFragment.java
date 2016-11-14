package com.hd.wlj.duohaowan.ui.publish.border;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hd.wlj.duohaowan.MainActivity;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.publish.ImageAdjustmentActivity;
import com.hd.wlj.duohaowan.ui.publish.ImageMergeListener;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
import com.hd.wlj.duohaowan.ui.publish.complate.ComplateModel;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.MathUtil;
import com.wlj.base.util.UIHelper;
import com.wlj.base.util.img.LoadImage;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorderFragment extends BaseFragment implements BorderView, SeekBar.OnSeekBarChangeListener {


    //    @BindView(R.id.adjust_height_tv)
//    TextView adjustHeightTv;
    @BindView(R.id.adjust_height_sb)
    SeekBar adjustHeightSb;
    @BindView(R.id.adjust_Scroll_LinearLayout)
    LinearLayout adjustScrollLinearLayout;
    @BindView(R.id.adjust_text)
    TextView adjustText;
    @BindView(R.id.sence_save)
    Button sence_save;

    private BorderPresenter presenter;
    private ImageMergeListener mListener;
    private Bitmap borderBitmap;
    private Rect rect;
    private int from;
    private String pub_id;
    private MergeBitmap.MergeType type;
    private int min;

    public BorderFragment() {
    }

    @Override
    protected int getlayout() {
        return R.layout.fragment_adjust_image;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, view);
        view.setMinimumHeight(0);

        presenter = new BorderPresenter(getActivity());
        presenter.attachView(this);

        Intent intent = getActivity().getIntent();
        from = intent.getIntExtra("from", 1);
        if (from == ImageAdjustmentActivity.from_main_choosework) {

            presenter.loadBorderData(BorderModel.Border);
            sence_save.setVisibility(View.GONE);
        } else {
            presenter.loadBorderData(BorderModel.baakground);
            adjustHeightSb.setVisibility(View.GONE);
            adjustText.setVisibility(View.GONE);
            sence_save.setVisibility(View.VISIBLE);
        }

        adjustHeightSb.setOnSeekBarChangeListener(this);
        adjustHeightSb.setProgress(100);
        adjustHeightSb.setMax(100);

    }

    private View createHotItem(Base base) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_hot, null);
        final ImageView mImg = (ImageView) view.findViewById(R.id.home_hot_img);

        final JSONObject resultJsonObject = base.getResultJsonObject();

        final String pics = resultJsonObject.optString("pic");
        if (pics != null && pics.length() > 0) {
            LoadImage.getinstall().addTask(Urls.HOST + pics, mImg).doTask();
        }

        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drawable drawable = ((ImageView) v).getDrawable();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                borderBitmap = bitmapDrawable.getBitmap();


                int position_x;
                int position_y;
                int inner_width;
                int inner_height;

                if (from == ImageAdjustmentActivity.from_main_choosework) {

                    position_x = resultJsonObject.optInt("position_x", 0);
                    position_y = resultJsonObject.optInt("position_y", 0);
                    inner_width = resultJsonObject.optInt("inner_width", 100);
                    inner_height = resultJsonObject.optInt("inner_height", 100);
                    type = MergeBitmap.MergeType.border;
                } else {

                    JSONArray backgroundWallWhiteList = resultJsonObject.optJSONArray("backgroundWallWhiteList");

                    JSONObject jsonObject = backgroundWallWhiteList.optJSONObject(0);

                    position_x = jsonObject.optInt("position_x", 0);
                    position_y = jsonObject.optInt("position_y", 0);
                    inner_width = jsonObject.optInt("width", 100);
                    inner_height = jsonObject.optInt("height", 100);

                    type = MergeBitmap.MergeType.background;
                }
                rect = new Rect(position_x, position_y, position_x + inner_width, position_y + inner_height);
                //
                min = Math.min(inner_width, inner_height);

                adjustHeightSb.setMax(min);
                adjustHeightSb.setProgress(min);
                mListener.setOriginalRect(rect);

                pub_id = resultJsonObject.optString("pub_id");
                merge(pub_id, borderBitmap, 1d);


            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showBorder(List<Base> list) {

        int width = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 2 - width / 10, LinearLayout.LayoutParams.MATCH_PARENT);
        //
        for (Base base : list) {

            adjustScrollLinearLayout.addView(createHotItem(base), layoutParams);
        }

    }


    //=---------- SeekBarChangeListener

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
        if (fromUser) {
            BigDecimal divide = MathUtil.divide(progress, min, 2);
            merge(pub_id, borderBitmap, divide.doubleValue());
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

    }//end

    // ------------回掉
    private void merge(String pub_id, Bitmap bitmap, double scale) {
        if (mListener != null) {
            mListener.merge(pub_id, bitmap, scale, type);
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

    @OnClick(R.id.sence_save)
    public void onClick() {

        save();

    }

    private void save() {

        ComplateModel model = new ComplateModel(getActivity());
//        model.setName(name);
//        model.setYears(year);
//        model.setPrice(money);
        model.setMerger(ImageAdjustmentActivity.mergeBitmaps);

        model.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                UIHelper.toastMessage(getContext(), "发布成功");
                GoToHelp.go(getActivity(), MainActivity.class);
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }
}
