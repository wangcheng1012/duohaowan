package com.hd.wlj.duohaowan.ui.home.classify.gallery;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.been.ShouCang;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.CyptoUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class GalleryActivity extends BaseFragmentActivity implements GalleryView {

    @BindView(R.id.gallary_headimage)
    ImageView headimage;
    @BindView(R.id.gallary_jubanfang)
    TextView jubanfang;
    @BindView(R.id.gallary_name)
    TextView name;
    @BindView(R.id.gallary_fensi)
    TextView fensi;
    @BindView(R.id.gallary_work)
    TextView work;
    @BindView(R.id.gallary_news)
    TextView news;
    @BindView(R.id.gallary_intro)
    TextView intro;
    @BindView(R.id.gallary_news_imageView)
    ImageView newsImageView;
    private GalleryPresenter presenter;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        presenter = new GalleryPresenter(this);
        presenter.attachView(this);

        presenter.loadData(getIntent());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showData(Base base) {
          jsonObject = base.getResultJsonObject();

        String head = jsonObject.optString("pic");
        Glide.with(this).load(Urls.HOST + head).bitmapTransform(new CropCircleTransformation(this)).into(headimage);
        name.setText(jsonObject.optString("name"));
        jubanfang.setText(jsonObject.optString("jubanfang"));
        fensi.setText(jsonObject.optInt("shoucang_count", 0) + "\n粉丝");
        work.setText(jsonObject.optInt("viewcount", 0) + "\n浏览");
        intro.setText(CyptoUtils.decryptBASE64(jsonObject.optString("intro")));

        //最新活动
        newsActivity();
    }

    private void newsActivity() {

        JSONArray zhanlan_array = jsonObject.optJSONArray("zhanlan_array");
        if(zhanlan_array != null){

            JSONObject jsonObject = zhanlan_array.optJSONObject(0);
            if(jsonObject != null){

                Glide.with(this).load(Urls.HOST + jsonObject.optString("pic")).into(newsImageView);
            }
        }

    }

    /**
     * 消息
     *
     * @param obj
     */
    @Override
    public void showNews(Base obj) {
        news.setText("");
    }

    @OnClick({R.id.gallary_follow, R.id.gallery_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gallary_follow:

                ShouCang shouCang = new ShouCang( this);
                shouCang.setId(jsonObject.optString("pub_id"));
                shouCang.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                    @Override
                    public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                        JSONObject object = base.getResultJsonObject();
                        UIHelper.toastMessage(getApplicationContext(),object.optString("message"));
                    }

                    @Override
                    public void fail(Exception paramException) {

                    }
                });
                break;
            case R.id.gallery_back:
                finish();
                break;
        }
    }
}
