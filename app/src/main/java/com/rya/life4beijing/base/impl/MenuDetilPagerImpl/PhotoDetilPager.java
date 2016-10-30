package com.rya.life4beijing.base.impl.MenuDetilPagerImpl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.rya.life4beijing.base.BaseMenuDetilPager;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class PhotoDetilPager extends BaseMenuDetilPager {
    public PhotoDetilPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(getmActivity());
        textView.setText("新闻中心 -- 组图");
        textView.setTextColor(Color.RED);
        textView.setTextSize(24);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
}
