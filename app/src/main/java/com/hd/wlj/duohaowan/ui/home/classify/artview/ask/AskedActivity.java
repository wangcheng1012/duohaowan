package com.hd.wlj.duohaowan.ui.home.classify.artview.ask;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class AskedActivity extends BaseFragmentActivity implements AskedView {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.asked_title)
    EditText askedTitle;
    @BindView(R.id.asked_context)
    EditText askedContext;
    @BindView(R.id.asked_teacher_layout)
    LinearLayout askedTeacherLayout;
//    @BindView(R.id.work_details_ll)
//    LinearLayout workDetailsLl;
    private AskedPresenter presenter;
    private String artist_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asked);
        ButterKnife.bind(this);

        initTitle();
        presenter = new AskedPresenter(this);
        presenter.attachView(this);

        presenter.loadTeacher();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    private void initTitle() {
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTitle.setText("提问");
    }

    @OnClick(R.id.asked_submit)
    public void onClick() {

        String title = askedTitle.getText() + "";
        String context = askedContext.getText() + "";
        String teacher = askedContext.getText() + "";

        if (StringUtils.isEmpty(title)) {
            UIHelper.toastMessage(getApplicationContext(), "请输入你的问题");
            return;
        }
        if (StringUtils.isEmpty(artist_id)) {
            UIHelper.toastMessage(getApplicationContext(), "请选择导师");
            return;
        }

        presenter.Asked(artist_id, title, context);
    }

    @Override
    public void showTeacher(List<Base> list) {

        askedTeacherLayout.removeAllViews();
        int toPx4 = DpAndPx.dpToPx(this, 4);
        int toPx8 = DpAndPx.dpToPx(this, 8);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = toPx8;
        for (int i = 0; i < list.size(); i++) {
            Base base = list.get(i);
            JSONObject jsonObject = base.getResultJsonObject();

            String picname = jsonObject.optString("pic_touxiang");
            String nickname = jsonObject.optString("nickname");
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_teacher, null);
            TextView name = (TextView) view.findViewById(R.id.teacher_name);
            ImageView imageView = (ImageView) view.findViewById(R.id.teacher_image);
            name.setText(nickname);
            view.setLayoutParams(layoutParams);
            imageView.setTag(R.id.tag_first,base);
            Glide.with(this).load(Urls.HOST + picname).bitmapTransform(new CropCircleTransformation(this)).into(imageView);
            askedTeacherLayout.addView(view);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Base tag = (Base) v.getTag(R.id.tag_first);
                    JSONObject resultJsonObject = tag.getResultJsonObject();
                      artist_id = resultJsonObject.optString("artist_id");
                }
            });
        }
    }


}
