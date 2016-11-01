package com.hd.wlj.duohaowan.ui.detailswork;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.wlj.base.ui.BaseFragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class WorkDetailsActivity extends BaseFragmentActivity {

    @BindView(R.id.work_detail_head)
    ImageView workDetailHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_details);
        ButterKnife.bind(this);

        Glide.with(this).load(R.mipmap.ic_launcher).bitmapTransform
                (new RoundedCornersTransformation(this, 90, 0))
                .into(workDetailHead);

    }
}
