package com.mysports.basketbook;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mysports.basketbook.R;


/**
 * Created by ldong on 2016-09-22.
 */
public class Terms extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms);


        WebView WebView01 = (WebView) findViewById(R.id.webview01);
        WebView01.setWebViewClient(new WebViewClient());

        WebSettings webSettings = WebView01.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebView01.loadUrl("http://210.122.7.193:8080/pp/term.jsp");
    }
}
