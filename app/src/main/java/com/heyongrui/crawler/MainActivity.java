package com.heyongrui.crawler;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webview;
    private EditText urlEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlEt = findViewById(R.id.url_et);
        Button startBtn = findViewById(R.id.start_btn);
        webview = findViewById(R.id.webview);
        initWebView(webview);

        startBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                String url = urlEt.getText().toString();
                if (TextUtils.isEmpty(url)) return;
                url = url.replaceAll(" ", "");
//                if (!url.contains("www.")) {
//                    url = "www." + url;
//                }
                if (!url.contains("https://")) {
                    url = "https://" + url;
                }
                urlEt.setText(url);
                webview.loadUrl(url);
                break;
        }
    }

    private void initWebView(WebView webview) {
        WebSettings webSetting = webview.getSettings();
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        //支持屏幕缩放
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);

        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true); // 推荐使用的窗口，使html界面自适应屏幕
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);// 支持javascript
        webSetting.setLoadsImagesAutomatically(true); // 支持自动加载图片
        webSetting.setGeolocationEnabled(true);
        if (getDir("appcache", 0) != null) {
            webSetting.setAppCachePath(getDir("appcache", 0).getPath());
        }
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式
        webSetting.setAppCacheEnabled(true);
        if (getDir("databases", 0) != null) {
            webSetting.setDatabasePath(getDir("databases", 0).getPath());
        }
        webSetting.setDatabaseEnabled(true);
        if (getDir("geolocation", 0) != null) {
            webSetting.setGeolocationDatabasePath(getDir("geolocation", 0)
                    .getPath());
        }
        if (Build.VERSION.SDK_INT > 8) {
            webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        }
        //设置PC浏览器标识
//        webSetting.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");


        webview.addJavascriptInterface(new InJavaScriptLocalObj(), "crawler");
        webview.setWebViewClient(new CustomWebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
