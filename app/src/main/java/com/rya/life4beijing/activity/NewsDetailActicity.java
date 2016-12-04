package com.rya.life4beijing.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

public class NewsDetailActicity extends Activity implements View.OnClickListener{
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
        String web_url = getIntent().getStringExtra("web_url");
        webviewNewsDetial.loadUrl(web_url);

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

        initListener();



    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgTextsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseDialog();
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items = {"超大号字体", "大号字体", "中号字体", "小号字体",};
        builder.setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (webviewNewsDetial.canGoBack()) {
            webviewNewsDetial.goBack();
        } else {
            finish();
        }
    }

    private void initStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorAppStyle), 25);
    }

    @Override
    public void onClick(View v) {
        finish();
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_textsize:
                System.out.println("Img_textSize click!~");
                break;
            case R.id.img_share:
                System.out.println("Img_share click!~");
                break;
        }
    }
}
