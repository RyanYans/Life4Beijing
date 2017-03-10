package com.rya.life4beijing.base.impl.MenuDetilPagerImpl;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rya.life4beijing.Fragment.TestFragment;
import com.rya.life4beijing.base.BaseDetilPager;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class FocusDetilPager extends BaseDetilPager {
    public FocusDetilPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(getmActivity());
        textView.setText("新闻中心 -- 专题");
        textView.setTextColor(Color.RED);
        textView.setTextSize(24);
        textView.setGravity(Gravity.CENTER);

        TestFragment testFragment = new TestFragment();
        if (testFragment.getView() == null) {
            Log.i("TAG__Erro!!", "initView: " + "Null Fragment!");
        } else {
            Log.i("TAG__Good!!", "initView: " + " not Null Fragment!");
        }

        return textView;
    }
}
