package com.rya.life4beijing.Fragment;

import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jaeger.library.StatusBarUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.rya.life4beijing.activity.MainActicity;
import com.rya.life4beijing.R;
import com.rya.life4beijing.base.BasePager;
import com.rya.life4beijing.base.impl.GovAffarisPager;
import com.rya.life4beijing.base.impl.HomePager;
import com.rya.life4beijing.base.impl.NewsPager;
import com.rya.life4beijing.base.impl.SettingPager;
import com.rya.life4beijing.base.impl.SmartServerPager;
import com.rya.life4beijing.view.NoScrollViewPager;

import java.util.ArrayList;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class MainContentFragment extends BaseFragment {

    private NoScrollViewPager vp_content;
    private ArrayList<BasePager> pagers;
    private RadioGroup rg_botton;

    @Override
    public void setInitialSavedState(SavedState state) {
        super.setInitialSavedState(state);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_main_content, null);

        vp_content = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rg_botton = (RadioGroup) view.findViewById(R.id.rg_botton);

        StatusBarUtil.setColor(mActivity, getResources().getColor(R.color.colorAppStyle), 25);

        return view;
    }

    @Override
    public void initData() {
        pagers = new ArrayList<>();

        pagers.add(new HomePager(mActivity));
        pagers.add(new NewsPager(mActivity));
        pagers.add(new SmartServerPager(mActivity));
        pagers.add(new GovAffarisPager(mActivity));
        pagers.add(new SettingPager(mActivity));

        vp_content.setAdapter(new ContentAdapter());

        // 首次进入，先初始化首页数据。
        initFirstLuanchData();

        rg_botton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        vp_content.setCurrentItem(0,false);
                        setSlidingMenuEnble(false);
                        break;
                    case R.id.rb_newscenter:
                        vp_content.setCurrentItem(1, false);
                        setSlidingMenuEnble(true);
                        break;
                    case R.id.rb_smartserver:
                        vp_content.setCurrentItem(2, false);
                        setSlidingMenuEnble(true);
                        break;
                    case R.id.rb_gov:
                        vp_content.setCurrentItem(3, false);
                        setSlidingMenuEnble(true);
                        break;
                    case R.id.rb_setting:
                        vp_content.setCurrentItem(4, false);
                        setSlidingMenuEnble(false);
                        break;
                    default:
                        break;
                }
            }
        });

        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // 当页面被选中时, 加载页面。
            @Override
            public void onPageSelected(int position) {
                pagers.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initFirstLuanchData() {
        pagers.get(0).initData();
        setSlidingMenuEnble(false);
    }

    private void setSlidingMenuEnble(boolean enble) {
        MainActicity mActivity = (MainActicity) this.mActivity;

        if (enble) {
            mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.LEFT);
        } else {
            mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    public NewsPager getNewsPager() {
        return (NewsPager) pagers.get(1);
    }


    private class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pagers.get(position);
            View view = basePager.mRootView;

//            basePager.initData();         -- 避免 ViewPager 预先加载  -- 耗能

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }
    }
}
