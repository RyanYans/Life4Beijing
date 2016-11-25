package com.rya.life4beijing.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.jaeger.library.StatusBarUtil;
import com.rya.life4beijing.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class NewsDetailActicity extends Activity {
    @BindView(R.id.img_back)
    ImageButton imgBack;
    @BindView(R.id.img_share)
    ImageButton imgShare;
    @BindView(R.id.img_textsize)
    ImageButton imgTextsize;
    @BindView(R.id.webview_news_detial)
    WebView webviewNewsDetial;
    @BindView(R.id.pb_news_detail_lodding)
    ProgressBar pbNewsDetailLodding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        initStatusBar();


        initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webviewNewsDetial.loadUrl("http://www.itheima.com");

        WebSettings settings = webviewNewsDetial.getSettings();
        settings.setBuiltInZoomControls(true);  // 设置缩放按钮
        settings.setUseWideViewPort(true);  // 支持双击缩放
        settings.setJavaScriptEnabled(true);    // 支持页面Js

        webviewNewsDetial.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbNewsDetailLodding.setVisibility(View.VISIBLE);
                System.out.println("页面加载...");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("页面加载完毕...");
                pbNewsDetailLodding.setVisibility(View.INVISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        webviewNewsDetial.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                System.out.println(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });

    }

    private void initStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorAppStyle), 25);
    }
}
