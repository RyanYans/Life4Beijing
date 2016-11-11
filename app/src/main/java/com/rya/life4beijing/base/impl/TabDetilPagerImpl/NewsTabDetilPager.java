package com.rya.life4beijing.base.impl.TabDetilPagerImpl;

import android.app.Activity;
import android.graphics.Color;
import android.nfc.Tag;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rya.life4beijing.Utils.ConstantsValue;
import com.rya.life4beijing.Utils.HttpUtil;
import com.rya.life4beijing.Utils.StreamUtil;
import com.rya.life4beijing.base.BaseTabDetilPager;
import com.rya.life4beijing.bean.NewsData;
import com.rya.life4beijing.bean.NewsTabBean;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 页面详情
 *
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class NewsTabDetilPager extends BaseTabDetilPager {

    private TextView mTextView;
    private static final String TAG = "NewsTabDetilPager";

    public NewsTabDetilPager(Activity activity, NewsData.DataBean.ChildrenBean childrenBean) {
        super(activity, childrenBean);

        // 设置标签页数据转向Url
        setmTabdUrl(ConstantsValue.BASE_URL + childrenBean.getUrl());

        // mNewsDetilData = childrenBean;
    }

    /**
     * 初始化 新闻详情布局
     * @return 返回新闻详情页面的布局View 用于viewPager接收
     */
    @Override
    public View initView() {

        mTextView = new TextView(getmActivity());

        mTextView.setTextColor(Color.RED);
        mTextView.setTextSize(24);
        mTextView.setGravity(Gravity.CENTER);

        mTextView.setText(getmChildrenBean().getTitle());

        return mTextView;
    }

    @Override
    public void initData() {
        super.initData();
        // mTextView.setText(mNewsDetilData.getTitle());

        getDataFromServer();
    }

    private void getDataFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("url ........." + getmTabdUrl());

                InputStream iStream = HttpUtil.getData(getmTabdUrl());
//                InputStreamReader iReader = new InputStreamReader(iStream);
                if (iStream != null) {
                    try {
                        String tabDataStr = StreamUtil.streamToString(iStream);

                        Gson gson = new Gson();
                        NewsTabBean tabData = gson.fromJson(tabDataStr, NewsTabBean.class);

                        System.out.println(TAG + " >>>>>>> " + tabData.getData().getTitle());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.w(TAG, "run: Http Getting Error");
                }

            }
        }).start();


    }
}
