package com.rya.life4beijing.base.impl.MenuDetilPagerImpl;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rya.life4beijing.R;
import com.rya.life4beijing.Utils.DisplayUtil;
import com.rya.life4beijing.base.BaseMenuDetilPager;
import com.rya.life4beijing.base.impl.TabDetilPagerImpl.NewsTabDetilPager;
import com.rya.life4beijing.bean.NewsData;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class NewsDetilPager extends BaseMenuDetilPager {

    @BindView(R.id.newstab_indicator)
    Indicator newstabIndicator;
    @BindView(R.id.vp_newsdetail)
    ViewPager vpNewsdetail;
    private List<NewsData.DataBean.ChildrenBean> mNewsData;
//    private ViewPager newsDetailViewPager;
    private List<NewsTabDetilPager> mNewsTabDetilList;
//    private Indicator newsTabIndicator;

    public NewsDetilPager(Activity activity, List<NewsData.DataBean.ChildrenBean> children) {
        super(activity);
        mNewsData = children;
    }

    /**
     * 初始化页面布局并返回布局对象
     *
     * @return
     */
    @Override
    public View initView() {
        View view = View.inflate(getmActivity(), R.layout.layout_newsdetil, null);

        ButterKnife.bind(this, view);

//        newsDetailViewPager = (ViewPager) view.findViewById(R.id.vp_newsdetail);
//        newsTabIndicator = (Indicator) view.findViewById(R.id.newstab_indicator);

        return view;
    }

    /**
     * 初始化页面数据
     */
    @Override
    public void initData() {
        super.initData();

        newstabIndicator.setCurrentItem(0);

        mNewsTabDetilList = new ArrayList<>();

        for (int index = 0; index < mNewsData.size(); index++) {
            NewsTabDetilPager newsTabDetilPager = new NewsTabDetilPager(getmActivity(), mNewsData.get(index));
            mNewsTabDetilList.add(newsTabDetilPager);
        }

        // 不用单独对viewpager设置setAdapter适配器，在下方initIndicatorViewPager中已经封装好
        // newsDetailViewPager.setAdapter(new NewsDetilAdapter());

        // 初始化 IndicatorViewPager 顶部指示器 + 下方显示页面
        initIndicatorViewPager();
    }

    /**
     * 初始化 顶部指示器
     */
    private void initIndicatorViewPager() {
        // 设置指示器底部样式
        newstabIndicator.setScrollBar(new ColorBar(getmActivity(), getmActivity().getResources().getColor(R.color.colorAppStyle), 5));

        float unSelectSize = 16;
        float selectSize = unSelectSize * 1.3f;
        newstabIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(getmActivity().getResources().getColor(R.color.colorAppStyle)
                , Color.GRAY).setSize(selectSize, unSelectSize));

        //结合 viewPager和indicator
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(newstabIndicator, vpNewsdetail);

        // 设置indicatorViewPager的适配器( 包括设置viewPager适配器 -- newsDetailViewPager.setAdapter(new NewsDetilAdapter()); )
        indicatorViewPager.setAdapter(new TopTabIndicatorAdapter());
    }

    /**
     * 顶部指示器 适配器
     */
    private class TopTabIndicatorAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {
        @Override
        public int getCount() {
            return mNewsTabDetilList.size();
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getmActivity().getLayoutInflater().inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;

            textView.setText(mNewsData.get(position).getTitle());

            int witdh = getTextWidth(textView);
            int padding = DisplayUtil.dipToPix(getmActivity(), 12);
            //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
            //1.3f是根据上面字体大小变化的倍数1.3f设置
            textView.setWidth((int) (witdh * 1.3f) + padding);

            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            NewsTabDetilPager pager = mNewsTabDetilList.get(position);
            // 拿到 Pager对象先初始化数据
            pager.initData();

            return pager.getmRootView();
        }

        @Override
        public int getItemPosition(Object object) {
            //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
            // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
            return PagerAdapter.POSITION_UNCHANGED;
        }

        private int getTextWidth(TextView textView) {
            if (textView == null) {
                return 0;
            }
            Rect bounds = new Rect();
            String text = textView.getText().toString();
            Paint paint = textView.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int width = bounds.left + bounds.width();
            return width;
        }
    }

    /**
     * 页面ViewPager适配器（indicator器中包含）
     */
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
            // 初始化页面详情布局数据 -- 可在构造函数中完成
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
