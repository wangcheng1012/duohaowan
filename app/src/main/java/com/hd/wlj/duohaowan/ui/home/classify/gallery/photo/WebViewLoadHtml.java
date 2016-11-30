package com.hd.wlj.duohaowan.ui.home.classify.gallery.photo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.AssetManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hd.wlj.duohaowan.R;
import com.orhanobut.logger.Logger;
import com.wlj.base.util.AppConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebViewLoadHtml extends AppCompatActivity {

    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null){

        }
        super.onCreate(savedInstanceState);
        //将屏幕设置为全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_view_load_html);

        webView = (WebView)findViewById(R.id.wv_webview);
//        url = "file:///android_asset/guide/index.htm";

        List<String> list = new ArrayList<>();
        list.add("http://img.tupianzj.com/uploads/allimg/150114/6-150114154118-50.jpg");
        list.add("http://imga1.pic21.com/bizhi/131112/03710/s05.jpg");
        list.add("http://img.hb.aicdn.com/df78d0b44a8a4e35868dc061f048329e1f2f9732d49c-OhT31d_fw658");
        list.add("http://img.hb.aicdn.com/df78d0b44a8a4e35868dc061f048329e1f2f9732d49c-OhT31d_fw658");
        list.add("http://img.hb.aicdn.com/df78d0b44a8a4e35868dc061f048329e1f2f9732d49c-OhT31d_fw658");
        list.add("http://img.hb.aicdn.com/df78d0b44a8a4e35868dc061f048329e1f2f9732d49c-OhT31d_fw658");
        list.add("http://img.hb.aicdn.com/df78d0b44a8a4e35868dc061f048329e1f2f9732d49c-OhT31d_fw658");
        list.add("http://img.hb.aicdn.com/df78d0b44a8a4e35868dc061f048329e1f2f9732d49c-OhT31d_fw658");
        list.add("http://img.hb.aicdn.com/df78d0b44a8a4e35868dc061f048329e1f2f9732d49c-OhT31d_fw658");
        list.add("http://img.hb.aicdn.com/df78d0b44a8a4e35868dc061f048329e1f2f9732d49c-OhT31d_fw658");
        list.add("http://img.hb.aicdn.com/df78d0b44a8a4e35868dc061f048329e1f2f9732d49c-OhT31d_fw658");
        list.add("http://img.hb.aicdn.com/df78d0b44a8a4e35868dc061f048329e1f2f9732d49c-OhT31d_fw658");
        modify("guide/index.htm",list);

//        loadLocalHtml(url);
    }

    /**
     * 重assets 读出啦修改后保存到sd卡再load
     * @param url
     * @return
     */
    private void modify(String url,List<String> list) {
        long l = System.currentTimeMillis();
        AssetManager assets = getResources().getAssets();
        try {
            InputStream inputStream = assets.open(url);
            ByteArrayOutputStream outByteArray = new ByteArrayOutputStream();
            
            int b;
            while (inputStream != null && (b = inputStream.read()) != -1) {
                outByteArray.write(b);
            }
            byte[] returnByte = outByteArray.toByteArray();
            String s = new String(returnByte);
            long l1 = System.currentTimeMillis();
            for (int i = 0; i < list.size(); i++) {
               s =  s.replace("http://imgs/"+(i+1)+".jpg", list.get(i) );
            }
            long l2 = System.currentTimeMillis();
            File file = new File(AppConfig.getAppConfig().getImagePath() + "/" + "index.html");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(s.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();

            long l3 = System.currentTimeMillis();
            String path = file.getPath();
            loadLocalHtml("file:///"+path);
            long l4 = System.currentTimeMillis();
            Logger.i("读 %s, 替换 %s, 写 %s,load %s , totle %s",l1 - l,l2 -l1,l3-l2 ,l4-l3 ,l4-l);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    public void loadLocalHtml(String url){
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);//开启JavaScript支持
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);//在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
                return true;
            }

            //重写此方法可以让webview处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();  //接受所有证书
            }

        });

        ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(url);

//        webView.loadData(html,"text/html; charset=UTF-8","UTF-8");
//        webView.loadData(head +
//        "{img: 'http://www.sznews.com/ent/images/attachement/jpg/site3/20140107/001e4f9d7bf91435b4fe62.jpg', x: -1000, y: 0, z: 1500, nx: 0, nz: 1 },"+
//        "{ img: 'http://121.40.177.251:3330/duohaowan/attachFiles\\/20161106\\/581ef1c8d6c4594f90fa046e\\/581ef1c3d6c4594f90fa046d', x: 0, y: 0, z: 1500, nx: 0, nz: 1 }"+
//        bottom,"text/html; charset=UTF-8",null
//        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}