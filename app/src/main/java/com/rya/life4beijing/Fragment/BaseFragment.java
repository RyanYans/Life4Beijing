package com.rya.life4beijing.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();

        return view;
    }

    // Fragment所依赖的Activity的oncreate执行完
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化数据
        initData();
    }

    //有子类实现，初始化Fragment 视图
    public abstract View initView();

    //有子类实现，初始化Fragment 数据
    public abstract void initData();

}
