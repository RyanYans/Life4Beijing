package com.rya.life4beijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.rya.life4beijing.base.BasePager;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();

        titleText.setText("设置");

        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setTextColor(Color.RED);
        textView.setTextSize(24);
        textView.setGravity(Gravity.CENTER);

        pagerContent.addView(textView);

        // 菜单不可见
        imgBtnMenu.setVisibility(View.INVISIBLE);
    }
}
