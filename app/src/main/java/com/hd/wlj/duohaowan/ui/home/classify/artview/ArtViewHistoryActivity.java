package com.hd.wlj.duohaowan.ui.home.classify.artview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsPresenterImpl;
import com.hd.wlj.duohaowan.ui.home.classify.work.DetailsView;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragmentActivity;
import com.wlj.base.util.CyptoUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 艺术观 历史
 */
public class ArtViewHistoryActivity extends BaseFragmentActivity implements DetailsView {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.artview_history_webview)
    WebView webView;
    private DetailsPresenterImpl detailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_artview_history);
        ButterKnife.bind(this);

        titleTitle.setText("详情");
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        detailsPresenter = new DetailsPresenterImpl(this);
        detailsPresenter.attachView(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        detailsPresenter.loadDetailsData(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailsPresenter.detachView();
    }

    @Override
    public void showDetailsData(Base paramBase) {
        JSONObject jsonObject = paramBase.getResultJsonObject();
        String content = jsonObject.optString("content");
        titleTitle.setText( jsonObject.optString("name"));

        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.loadData(CyptoUtils.decryptBASE64(content), "text/html; charset=UTF-8", "UTF-8");


    }

    @Override
    public void showComment() {

    }
}
