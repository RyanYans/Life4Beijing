package com.rya.life4beijing.base.impl;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rya.life4beijing.Activity.MainActicity;
import com.rya.life4beijing.Fragment.LeftMenuFragment;
import com.rya.life4beijing.base.BaseMenuDetilPager;
import com.rya.life4beijing.base.BasePager;
import com.rya.life4beijing.base.impl.MenuDetilPagerImpl.FocusDetilPager;
import com.rya.life4beijing.base.impl.MenuDetilPagerImpl.InteractDetilPager;
import com.rya.life4beijing.base.impl.MenuDetilPagerImpl.NewsDetilPager;
import com.rya.life4beijing.base.impl.MenuDetilPagerImpl.PhotoDetilPager;
import com.rya.life4beijing.bean.NewsData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class NewsPager extends BasePager {


    private ArrayList<BaseMenuDetilPager> mDetilPagers;
    private NewsData mNewsData;

    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();

        // 菜单可见
        imgBtnMenu.setVisibility(View.VISIBLE);

        //获取服务器json数据
        NewsData jsonData = getJsonData();

        updateLeftMenuData(jsonData);

        updatePagerData();

        //设置当前页面显示
        setCurrentDetilPager(0);
    }

    private void updatePagerData() {
        mDetilPagers = new ArrayList<>();
        Activity mActivity = getmActivity();
        mDetilPagers.add(new NewsDetilPager(mActivity));
        mDetilPagers.add(new FocusDetilPager(mActivity));
        mDetilPagers.add(new PhotoDetilPager(mActivity));
        mDetilPagers.add(new InteractDetilPager(mActivity));

    }

    // 把页面数据添加到页面布局pagerContent(-FragmentLayout)上
    public void setCurrentDetilPager(int position) {
        //清空PagerContent内容
        pagerContent.removeAllViews();

        // 获取页面对象 -拿取页面布局
        BaseMenuDetilPager pager = mDetilPagers.get(position);
        View view = pager.getmRootView();

        // 把布局内容添加到 页面中
        FrameLayout pagerContent = getPagerContent();
        pagerContent.addView(view);

        //更新标题栏
        TextView titleText = getTitleText();
        titleText.setText(mNewsData.getData().get(position).getTitle());
    }

    private void updateLeftMenuData(NewsData jsonData) {
        MainActicity mActivity = (MainActicity) this.mActivity;

        LeftMenuFragment leftMenuFragment = mActivity.getLeftMenuFragment();

        leftMenuFragment.setMenuData(jsonData.getData());
    }

    private NewsData getJsonData() {

        // 解析 服务器返回Json
        //去本地数据

        try {
            AssetManager assets = mActivity.getAssets();
            InputStream IsJson = assets.open("categories.json");

            InputStreamReader jsonData = new InputStreamReader(IsJson);

            Gson gson = new Gson();
            mNewsData = gson.fromJson(jsonData, NewsData.class);

            return mNewsData;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
