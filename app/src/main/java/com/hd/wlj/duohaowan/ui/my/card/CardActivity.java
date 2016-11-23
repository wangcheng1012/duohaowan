package com.hd.wlj.duohaowan.ui.my.card;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.been.User;
import com.hd.wlj.duohaowan.ui.my.card.bg.ChooseBackgroundActivity;
import com.hd.wlj.duohaowan.util.TakePhotoCrop;
import com.hd.wlj.duohaowan.view.ImgInpImg;
import com.jph.takephoto.model.TResult;
import com.lling.photopicker.PhotoPickerActivity;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.CyptoUtils;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.util.StringUtils;
import com.wlj.base.widget.IconfontTextview;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardActivity extends BaseFragmentActivity implements CardView, TakePhotoCrop.CropBack {

    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.card_name)
    ImgInpImg cardName;
    @BindView(R.id.card_leibie)
    ImgInpImg cardLeibie;
    @BindView(R.id.card_intro)
    ImgInpImg cardIntro;
    @BindView(R.id.card_numberfilter)
    TextView cardNumberfilter;

    @BindView(R.id.classify_artist_headimage)
    ImageView artistHeadimage;
    @BindView(R.id.classify_artist_name)
    TextView artistName;
    @BindView(R.id.classify_artist_intro)
    TextView artistIntro;
    @BindView(R.id.lllayout)
    LinearLayout lllayout;
    @BindView(R.id.classify_artist_background)
    RelativeLayout classifyArtistBackground;

    private CardPresenter cardPresenter;
    private JSONObject jsonObject;
    private TakePhotoCrop takePhotoCrop;
    private int textlength = 50;
    public int requestcode = 500;
    private Base BackgroundBase;
    private EditText nickName;
    private EditText leibie;
    private EditText intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        takePhotoCrop = new TakePhotoCrop(this, this);
        takePhotoCrop.getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cardPresenter = new CardPresenter(this);
        cardPresenter.attachView(this);

        initCard();
        initTitle();
        initView();
    }

    private void initCard() {

        lllayout.setVisibility(View.GONE);

        Glide.with(this).load(Urls.HOST + jsonObject.optString("pic")).crossFade(1000).into(artistHeadimage);
        artistName.setText(jsonObject.optString("touxian", "未设置称谓") + "   " + jsonObject.optString("nickname", "未设置昵称"));
        String intro = jsonObject.optString("intro", "简介为空");

        String base64 = CyptoUtils.decryptBASE64(intro);
        if (base64.length() > textlength) {
            base64 = base64.substring(0, textlength);
        }
        artistIntro.setText(base64);

        Glide.with(this).load(Urls.HOST + jsonObject.optString("pic_back")).crossFade(1000)
//                .bitmapTransform(new BlurTransformation(this))
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        classifyArtistBackground.setBackgroundDrawable(resource);
                    }
                });
    }

    private void initView() {

        int toPx = DpAndPx.dpToPx(this, 11);
        //设置布局
        nickName = cardName.getEditTextView();
        nickName.setBackgroundResource(R.drawable.shape_hui_hui);
        nickName.setPadding(toPx + toPx, toPx, toPx, toPx);

        leibie = cardLeibie.getEditTextView();
        leibie.setBackgroundResource(R.drawable.shape_hui_hui);
        leibie.setPadding(toPx + toPx, toPx, toPx, toPx);

        intro = cardIntro.getEditTextView();
        intro.setBackgroundResource(R.drawable.shape_hui_hui);
        intro.setPadding(toPx, toPx, toPx, toPx);
        intro.setMinLines(3);
        intro.setGravity(Gravity.LEFT | Gravity.TOP);

        IconfontTextview intro_FontView1 = cardIntro.getIconFontView1();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        intro_FontView1.setLayoutParams(layoutParams);
        intro_FontView1.setGravity(Gravity.TOP);
        //设置 文字
        nickName.setText(jsonObject.optString("nickname"));
        leibie.setText(jsonObject.optString("touxian"));
        intro.setText(CyptoUtils.decryptBASE64(jsonObject.optString("intro")));
        //添加 TextChangedListener
        nickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = leibie.getText() + "";
                if (StringUtils.isEmpty(text)) {
                    artistName.setText(s);
                } else {
                    artistName.setText(text + "   " + s);
                }
            }
        });
        leibie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = nickName.getText() + "";
                if (StringUtils.isEmpty(s + "")) {
                    artistName.setText(s1);
                } else {
                    artistName.setText(s + "   " + s1);
                }

            }
        });
        intro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                String str = s + "";

                if (length > textlength) {
                    str = str.toString().substring(0, textlength);
                    intro.setText(str);
                    length = textlength;
                }
                cardNumberfilter.setText(length + "/" + textlength);
                artistIntro.setText(str);

            }
        });

    }

    private void initTitle() {
        titleTitle.setText("我的名片");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        takePhotoCrop.getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {
        cardPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        takePhotoCrop.getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TakePhotoCrop.IMAGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> uristr = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT_URLSTR);

                takePhotoCrop.onCrop(Uri.parse(uristr.get(0)));
            }
        } else if (requestCode == requestcode && resultCode == RESULT_OK && data != null) {
            //背景返回
            Serializable base = data.getSerializableExtra("base");
            if (base != null) {
                BackgroundBase = (Base) base;
                JSONObject jsonObject = BackgroundBase.getResultJsonObject();
                String pic = jsonObject.optString("pic");

                Glide.with(this).load(Urls.HOST + pic).crossFade(1000)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                classifyArtistBackground.setBackgroundDrawable(resource);
                            }
                        });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePhotoCrop.onRequestPermissionsResult_(requestCode, permissions, grantResults);
    }

    @OnClick({R.id.title_back, R.id.card_update_headimage, R.id.card_choosebg, R.id.card_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.card_update_headimage:

                takePhotoCrop.photoPicker();
                break;
            case R.id.card_choosebg:
                Intent intent = new Intent(this, ChooseBackgroundActivity.class);
                startActivityForResult(intent, requestcode);
                break;
            case R.id.card_save:
                //card头像
                Drawable drawable = artistHeadimage.getDrawable();
                Bitmap cardPicBitmap = null;
                if(drawable != null && drawable instanceof BitmapDrawable){
                    BitmapDrawable  bd = (BitmapDrawable)drawable;
                    cardPicBitmap = bd.getBitmap();
                }

                User user = new User();
                user.setNickname(nickName.getText()+"");
                user.setTouxian(leibie.getText()+"");
                user.setCardPicBitmap(cardPicBitmap);
                user.setCardbg(BackgroundBase == null ? jsonObject.optString("pic_back") : BackgroundBase.getResultJsonObject().optString("pic"));
                user.setIntro(intro.getText()+"");

                cardPresenter.save(user);
                break;
        }
    }

    @Override
    public void cropback(TResult result) {

        String image = result.getImage().getPath();
        Glide.with(this).load(image).asBitmap()
//                .bitmapTransform(new BlurTransformation(this))
                .into(artistHeadimage);

    }
}
