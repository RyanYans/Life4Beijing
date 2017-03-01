package com.rya.life4beijing.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public abstract class BaseDetilPager {

    private final Activity mActivity;

    // 页面根部局
    private final View mRootView;

    public Activity getmActivity() {
        return mActivity;
    }

    public View getmRootView() {
        return mRootView;
    }

    protected BaseDetilPager(Activity activity) {
        mActivity = activity;

        mRootView = initView();
    }

    public abstract View initView();

    public void initData() {

    }
}
