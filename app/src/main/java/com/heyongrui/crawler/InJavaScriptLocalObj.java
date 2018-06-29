package com.heyongrui.crawler;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by lambert on 2018/6/28.
 */

public class InJavaScriptLocalObj {
    @JavascriptInterface
    public void getSource(String html) {
        Log.i("html=", html);
    }
}
