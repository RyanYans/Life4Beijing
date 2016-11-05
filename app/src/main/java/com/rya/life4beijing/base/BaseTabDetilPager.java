package com.rya.life4beijing.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public abstract class BaseTabDetilPager {

    private final Activity mActivity;

    // 页面根部局
    private View mRootView;

    public Activity getmActivity() {
        return mActivity;
    }

    public View getmRootView() {
        return mRootView;
    }

    protected BaseTabDetilPager(Activity activity) {
        mActivity = activity;
        // 具体View实现返回给根布局
        mRootView = initView();
    }

    public abstract View initView();

    public void initData() {

    }
}
