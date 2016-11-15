package com.rya.life4beijing.base;

import android.app.Activity;
import android.view.View;

import com.rya.life4beijing.bean.NewsData;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public abstract class BaseTabDetilPager {

    private final Activity mActivity;

    private NewsData.DataBean.ChildrenBean mChildrenBean;

    private String mTabdUrl;

    // 页面根部局
    private View mRootView;

    public String getmTabdUrl() {
        return mTabdUrl;
    }

    public void setmTabdUrl(String mTabdUrl) {
        this.mTabdUrl = mTabdUrl;
    }

    public NewsData.DataBean.ChildrenBean getmChildrenBean() {
        return mChildrenBean;
    }

    public Activity getmActivity() {
        return mActivity;
    }

    public View getmRootView() {
        return mRootView;
    }

    protected BaseTabDetilPager(Activity activity, NewsData.DataBean.ChildrenBean childrenBean) {
        mActivity = activity;
        mChildrenBean = childrenBean;
        // 具体View实现返回给根布局
        mRootView = initView();
    }

    public abstract View initView();

    public void initData() {

    }
}
