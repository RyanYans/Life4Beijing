package com.rya.life4beijing.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.rya.life4beijing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

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
    private int mTempWhich = 2; // 默认为中号字体

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        ShareSDK.initSDK(this);

        initStatusBar();

        initWebView();
    }

    /*
    * 初始化webView界面及其参数
    * */
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

    /*
    * 设置字体及分享按钮的点击事件
    * */
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
                Toast.makeText(getApplicationContext(), "Sharing...", Toast.LENGTH_SHORT).show();
                showShare();
            }
        });
    }

    /*
    * 显示字体选择对话框
    * */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items = {"超大号字体", "大号字体", "中号字体", "小号字体",};

        builder.setSingleChoiceItems(items, mTempWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("字体大小 : " + which);
                mTempWhich = which;
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = webviewNewsDetial.getSettings();

                switch (mTempWhich) {
                    case 0:
                        settings.setTextZoom(200);
                        break;
                    case 1:
                        settings.setTextZoom(150);
                        break;
                    case 2:
                        settings.setTextZoom(100);
                        break;
                    case 3:
                        settings.setTextZoom(65);
                        break;
                }
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }

    /*
    * 自定义WebView内部的返回点击
    * */
    @Override
    public void onBackPressed() {
        if (webviewNewsDetial.canGoBack()) {
            webviewNewsDetial.goBack();
        } else {
            finish();
        }
    }

    /*
    * 初始化沉浸式状态栏
    * */
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

    /*
    * 显示分享对话框
    * */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
