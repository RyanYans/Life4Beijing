package com.rya.life4beijing.base.impl;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rya.life4beijing.base.BasePager;
import com.rya.life4beijing.bean.NewsData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class NewsPager extends BasePager {
    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();

        titleText.setText("新闻中心");

        TextView textView = new TextView(mActivity);
        textView.setText("新闻");
        textView.setTextColor(Color.RED);
        textView.setTextSize(24);
        textView.setGravity(Gravity.CENTER);

        pagerContent.addView(textView);

        // 菜单可见
        imgBtnMenu.setVisibility(View.VISIBLE);

        // 解析 服务器返回Json
        try {
            AssetManager assets = mActivity.getAssets();
            InputStream IsJson = assets.open("categories.json");
            InputStreamReader json = new InputStreamReader(IsJson);

            Gson gson = new Gson();
            NewsData newsData = gson.fromJson(json, NewsData.class);

            System.out.println(newsData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
