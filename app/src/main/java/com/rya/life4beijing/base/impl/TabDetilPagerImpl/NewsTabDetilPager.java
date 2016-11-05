package com.rya.life4beijing.base.impl.TabDetilPagerImpl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.rya.life4beijing.base.BaseTabDetilPager;
import com.rya.life4beijing.bean.NewsData;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class NewsTabDetilPager extends BaseTabDetilPager {

    private final NewsData.DataBean.ChildrenBean mNewsData;
    private TextView mTextView;

    public NewsTabDetilPager(Activity activity, NewsData.DataBean.ChildrenBean childrenBean) {
        super(activity);

        mNewsData = childrenBean;
    }

    @Override
    public View initView() {

        mTextView = new TextView(getmActivity());

        mTextView.setTextColor(Color.RED);
        mTextView.setTextSize(24);
        mTextView.setGravity(Gravity.CENTER);

        return mTextView;
    }

    @Override
    public void initData() {
        super.initData();
        mTextView.setText(mNewsData.getTitle());
    }
}
