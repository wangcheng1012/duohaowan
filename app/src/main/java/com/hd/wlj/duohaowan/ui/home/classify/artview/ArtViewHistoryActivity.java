package com.hd.wlj.duohaowan.ui.home.classify.artview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
//		settings.setAllowFileAccess(true);
//		settings.setBuiltInZoomControls(true);//会出现放大缩小的按钮
//		settings.setDisplayZoomControls(true);
        //手势放大网页以后，自动缩小回了原来的尺寸的解决
        settings.setUseWideViewPort(true);//可任意比例缩放。但会造成下部很大空白，"<meta name=\"viewport\" 控制
//		settings.setLoadWithOverviewMode(true);//初始大小为屏幕宽，会导致文字缩小
//		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url,
                                      Bitmap favicon) {
                view.getSettings().setBlockNetworkImage(false);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setBlockNetworkImage(true);
                super.onPageFinished(view, url);
            }

        });

        webView.loadData(getHtmlData(CyptoUtils.decryptBASE64(content)), "text/html; charset=UTF-8", "UTF-8");
    }

    @Override
    public void showComment() {

    }

    private String getHtmlData(String bodyHTML) {
        /**
         * telephone=no就禁止了把数字转化为拨号链接！
         * telephone=yes就开启了把数字转化为拨号链接，要开启转化功能，这个meta就不用写了,在默认是情况下就是开启！
         * viewport的理解:http://www.cnblogs.com/2050/p/3877280.html
         */
        String head =  "<head>" +
                "<meta charset=\"utf-8\" />"+
                " <meta name=\"format-detection\" content=\"telephone=no\" />"+
                "<meta name=\"viewport\" content=\"user-scalable=no, initial-scale=1, maximum-scale=2, minimum-scale=1, width=device-width, height=device-height \" />" +
//	      <!-- WARNING: for iOS 7, remove the width=device-width and height=device-height attributes. See https://issues.apache.org/jira/browse/CB-4323 -->
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}
