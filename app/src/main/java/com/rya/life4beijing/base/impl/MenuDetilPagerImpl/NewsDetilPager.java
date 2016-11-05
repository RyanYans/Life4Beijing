package com.rya.life4beijing.base.impl.MenuDetilPagerImpl;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rya.life4beijing.R;
import com.rya.life4beijing.base.BaseMenuDetilPager;
import com.rya.life4beijing.base.impl.TabDetilPagerImpl.NewsTabDetilPager;
import com.rya.life4beijing.bean.NewsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class NewsDetilPager extends BaseMenuDetilPager {
    private List<NewsData.DataBean.ChildrenBean> mNewsData;
    private ViewPager vp_newsdetil;
    private List<NewsTabDetilPager> mNewsTabDetilList;

    public NewsDetilPager(Activity activity, List<NewsData.DataBean.ChildrenBean> children) {
        super(activity);
        mNewsData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(getmActivity(), R.layout.layout_newsdetil, null);
        vp_newsdetil = (ViewPager) view.findViewById(R.id.vp_newsdetil);

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        mNewsTabDetilList = new ArrayList<>();
        for (int index = 0; index < mNewsData.size(); index++) {
            NewsTabDetilPager newsTabDetilPager = new NewsTabDetilPager(getmActivity(), mNewsData.get(index));
            mNewsTabDetilList.add(newsTabDetilPager);
        }

        vp_newsdetil.setAdapter(new NewsDetilAdapter());

    }

    private class NewsDetilAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mNewsData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsTabDetilPager newsTabDetilPager = mNewsTabDetilList.get(position);
            newsTabDetilPager.initData();
            View view = newsTabDetilPager.getmRootView();

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
