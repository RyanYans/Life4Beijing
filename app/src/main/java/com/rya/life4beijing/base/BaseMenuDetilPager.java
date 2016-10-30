package com.rya.life4beijing.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public abstract class BaseMenuDetilPager {

    private final Activity mActivity;

    public Activity getmActivity() {
        return mActivity;
    }

    public View getmRootView() {
        return mRootView;
    }

    private final View mRootView;

    protected BaseMenuDetilPager(Activity activity) {
        mActivity = activity;

        mRootView = initView();
    }

    public abstract View initView();

    public void initData() {

    }
}
