package com.rya.life4beijing.base.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rya.life4beijing.activity.MainActicity;
import com.rya.life4beijing.Fragment.LeftMenuFragment;
import com.rya.life4beijing.Utils.ConstantsValue;
import com.rya.life4beijing.Utils.HttpUtil;
import com.rya.life4beijing.Utils.PrefUtil;
import com.rya.life4beijing.Utils.StreamUtil;
import com.rya.life4beijing.base.BaseMenuDetilPager;
import com.rya.life4beijing.base.BasePager;
import com.rya.life4beijing.base.impl.MenuDetilPagerImpl.FocusDetilPager;
import com.rya.life4beijing.base.impl.MenuDetilPagerImpl.InteractDetilPager;
import com.rya.life4beijing.base.impl.MenuDetilPagerImpl.NewsDetilPager;
import com.rya.life4beijing.base.impl.MenuDetilPagerImpl.PhotoDetilPager;
import com.rya.life4beijing.bean.NewsData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class NewsPager extends BasePager {

    private static final int UPDATE_NEWSDATA = 200;
    private ArrayList<BaseMenuDetilPager> mDetilPagers;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_NEWSDATA:
                    updatePager();
            }
        }
    };

    private NewsData mNewsData;

    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();

        titleText.setText("新闻");
        // 菜单可见
        imgBtnMenu.setVisibility(View.VISIBLE);

        //获取服务器json数据
        getJsonData();
    }

    private void updatePager() {
        // 更新侧滑栏数据
        updateLeftMenuData(mNewsData);

        // 更新当前页面数据
        updatePagerData();

        //设置当前页面
        setCurrentDetilPager(0);
    }

    private void updatePagerData() {
        mDetilPagers = new ArrayList<>();
        Activity mActivity = getmActivity();
        mDetilPagers.add(new NewsDetilPager(mActivity, mNewsData.getData().get(0).getChildren()));
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
        //初始化数据
        pager.initData();

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

    private void getJsonData() {

        //查看是否有缓存, 有缓存先立刻显示

        if (PrefUtil.getBoolean(mActivity, ConstantsValue.HAS_JSON_CACHE, false)) {
            File file = new File(mActivity.getFilesDir().getPath(), ConstantsValue.GROBAL_JSON);
            //有缓存 直接读取
            if (file.exists()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    String jsonStr = StreamUtil.streamToString(fileInputStream);

                    mNewsData = new Gson().fromJson(jsonStr, NewsData.class);

                    sendHandlerMessage(UPDATE_NEWSDATA);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 获取服务器信息
        getDataFromNet();

        //去本地数据
        /*try {
            AssetManager assets = mActivity.getAssets();
            InputStream IsJson = assets.open("categories.json");

            InputStreamReader json = new InputStreamReader(IsJson);

            Gson gson = new Gson();
            NewsData jsonData = gson.fromJson(json, NewsData.class);

            return jsonData;

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    // 从远程服务器获取数据
    private void getDataFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 解析 服务器返回Json
                InputStream json = HttpUtil.getData(ConstantsValue.NEWSPAGER_URI);

                if (json != null) {
                    try {
                        String jsonStr = StreamUtil.streamToString(json);

                        Gson gson = new Gson();
                        NewsData newsData = gson.fromJson(jsonStr, NewsData.class);
                        mNewsData = newsData;

                        sendHandlerMessage(UPDATE_NEWSDATA);

                        // 更新 缓存配置信息
                        PrefUtil.setBoolean(mActivity, ConstantsValue.HAS_JSON_CACHE, true);
                        // 缓存文件
                        StreamUtil.writeFileToCache(mActivity, jsonStr, ConstantsValue.GROBAL_JSON);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void sendHandlerMessage(int updateMode) {
        Message msg = Message.obtain();
        msg.what = updateMode;
        mHandler.sendMessage(msg);
    }
}
