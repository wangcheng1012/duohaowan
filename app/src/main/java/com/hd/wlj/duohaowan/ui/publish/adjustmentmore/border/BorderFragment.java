package com.hd.wlj.duohaowan.ui.publish.adjustmentmore.border;


import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
import com.hd.wlj.duohaowan.ui.publish.adjustmentmore.AdjustmentImageActivity;
import com.hd.wlj.duohaowan.util.ImageUtil2;
import com.orhanobut.logger.Logger;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.MathUtil;
import com.wlj.base.util.UIHelper;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorderFragment extends BaseFragment implements BorderView, SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.adjust_height_sb)
    SeekBar adjustHeightSb;
    @BindView(R.id.adjust_text)
    TextView adjustText;
    @BindView(R.id.adjust_recyclerView)
    RecyclerView adjustRecyclerView;
    @BindView(R.id.adjust_recyclerView_bili)
    RecyclerView biliRecyclerView;

    private BorderPresenter presenter;
    private int min = 100;
    private List mData;
    private CommonAdapter commonAdapter;
    private List<Base> biliData;
    private CommonAdapter biliAdapter;
    private View preSelectBiliView;
    private View preSelectClassifyView;
    private AdjustmentImageActivity activity;
    private int progress = 100;

    public BorderFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        presenter = new BorderPresenter(getActivity());
        presenter.attachView(this);

//        View view = inflater.inflate(R.layout.fragment_adjust_image, container, false);
//        ButterKnife.bind(this, view);
//        initView();
        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    protected int getlayout() {
        return R.layout.fragment_adjust_image;
    }

    protected void initView() {
        ButterKnife.bind(this, view);
        view.setMinimumHeight(0);

        activity = (AdjustmentImageActivity) getActivity();

        presenter.loadBorderClassifyData();

        adjustHeightSb.setOnSeekBarChangeListener(this);
        adjustHeightSb.setProgress(progress);
        adjustHeightSb.setMax(min);

        initRecyclerView();
        initRecyclerViewBIli();
    }

    private void initRecyclerViewBIli() {
        biliRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));

        biliRecyclerView.setAdapter(initBiliAdapter());
    }

    private RecyclerView.Adapter initBiliAdapter() {

        if (biliData == null) {
            biliData = new ArrayList<>();
        }
        biliAdapter = new CommonAdapter(getContext(), R.layout.item_vorde_bili, biliData) {

            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                createBiliItem(holder, (Base) o, position);
            }
        };

        biliAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                if (preSelectBiliView != null) {
                    preSelectBiliView.setSelected(false);
                }
                preSelectBiliView = view;
                preSelectBiliView.setSelected(true);

                Base tag = (Base) view.getTag(R.id.tag_first);
                final JSONObject resultJsonObject = tag.getResultJsonObject();

                String pic = resultJsonObject.optString("pic");
                UIHelper.showProgressbar(getActivity(), null);

                WindowManager windowManager = getActivity().getWindowManager();
                Point point = new Point();
                windowManager.getDefaultDisplay().getSize(point);

                Glide.with(BorderFragment.this).load(Urls.HOST + pic).asBitmap().into(new SimpleTarget<Bitmap>(500, 500) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                        UIHelper.closeProgressbar();

                        merge(resource, resultJsonObject);

                        //seekbar
                        adjustHeightSb.setMax(min);
                        adjustHeightSb.setProgress(min);
                    }
                });

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        return biliAdapter;
    }

    /**
     * onclick点击 调整
     *
     * @param resource
     * @param resultJsonObject
     */
    private void merge(Bitmap resource, JSONObject resultJsonObject) {
        String pic = resultJsonObject.optString("pic");
        MergeBitmap mergeBitmap = activity.getMergeBitmap();
        mergeBitmap.setBorderBitmap(resource);
        // Rect
        int position_x = resultJsonObject.optInt("position_x", 0);
        int position_y = resultJsonObject.optInt("position_y", 0);
        int inner_width = resultJsonObject.optInt("inner_width", 100);
        int inner_height = resultJsonObject.optInt("inner_height", 100);
        Rect rect = new Rect(position_x, position_y, position_x + inner_width, position_y + inner_height);
        //set
        mergeBitmap.setBorkRect_old(rect);
        mergeBitmap.setWorkRect(rect);
        mergeBitmap.setBorderId(resultJsonObject.optString("pub_id"));
        mergeBitmap.setBorderPath(pic);
        mergeBitmap.setBorderBitmap(resource);
        // 去掉 选中的卡纸效果
        mergeBitmap.setCard1space(0);
        mergeBitmap.setCard2space(0);

        Bitmap bitmap = mergeBitmap.buildFinalBitmap();
        activity.getmRectClickImageView().setImageBitmap(bitmap);

        //seekbar
        min = Math.min(inner_width, inner_height);
    }

    /**
     * SeekBar 的调整
     *
     * @param divide
     */
    private void seekBarMerge(double divide) {
        Rect borkRectOld = activity.getMergeBitmap().getBorkRect_old();
        if (borkRectOld == null) {
            return;
        }
        Rect rect = ImageUtil2.resizeRectangle(borkRectOld, divide);
        activity.getMergeBitmap().setWorkRect(rect);

        Bitmap bitmap = activity.getMergeBitmap().buildFinalBitmap();
        activity.getmRectClickImageView().setImageBitmap(bitmap);
    }

    private void createBiliItem(ViewHolder holder, Base o, int position) {

        holder.getConvertView().setTag(R.id.tag_first, o);

        TextView textview = holder.getView(R.id.border_bili_textview);

        final JSONObject resultJsonObject = o.getResultJsonObject();

        textview.setText(resultJsonObject.optString("name"));

    }

    private void initRecyclerView() {

        adjustRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        adjustRecyclerView.setAdapter(initAdapter());
    }

    /**
     * 画框 种类 Adapter
     *
     * @return
     */
    private RecyclerView.Adapter initAdapter() {
        if (mData == null) {
            mData = new ArrayList();
        }
        commonAdapter = new CommonAdapter(getContext(), R.layout.item_border_recyclerview, mData) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                createBorderItem(holder, (Base) o, position);
            }
        };

        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                //选中效果添加
                if (preSelectClassifyView != null) {
                    preSelectClassifyView.setSelected(false);
                }
                if (preSelectBiliView != null) {
                    preSelectBiliView.setSelected(false);
                }
                preSelectClassifyView = view;
                preSelectClassifyView.setSelected(true);


                presenter.loadBorderBiliData(view, activity.getMergeBitmap());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return commonAdapter;
    }

    private void createBorderItem(ViewHolder holder, Base base, int position) {

        ImageView mImg = holder.getView(R.id.item_border);

        final JSONObject resultJsonObject = base.getResultJsonObject();

        final String pics = resultJsonObject.optString("pic");
        if (pics != null && pics.length() > 0) {
            Glide.with(this).load(Urls.HOST + pics).into(mImg);
        }
        holder.getConvertView().setTag(R.id.tag_first, base);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showBorder(List<Base> list) {
        Logger.i("showBorder" + getId());
        mData.addAll(list);
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void showBorderBili(List<Base> list) {
        biliData.clear();
        biliData.addAll(list);
        biliAdapter.notifyDataSetChanged();
    }

    //=---------- SeekBarChangeListener
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


        if (fromUser && min != 0) {
            this.progress = progress;
            double divide = MathUtil.divide(progress, min, 2).doubleValue();

            seekBarMerge(divide);

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }//end


}
