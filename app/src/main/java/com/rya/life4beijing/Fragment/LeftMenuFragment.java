package com.rya.life4beijing.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.rya.life4beijing.activity.MainActicity;
import com.rya.life4beijing.R;
import com.rya.life4beijing.base.impl.NewsPager;
import com.rya.life4beijing.bean.NewsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class LeftMenuFragment extends BaseFragment {

    private ArrayList<String> mTitleList;
    private ListView lv_menu;
    private int currentPosition;
    private LeftMenuAdapter mLeftMenuAdapter;

    @Override

    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_left_menu, null);

        lv_menu = (ListView) view.findViewById(R.id.lv_menu);

        StatusBarUtil.setColor(mActivity, getResources().getColor(R.color.black), 25);

        return view;
    }

    @Override
    public void initData() {
        currentPosition = 0;

    }

    // 设置侧边栏数据
    public void setMenuData(List<NewsData.DataBean> data) {
        //初始化当前菜单项
        currentPosition = 0;
        // title集合
        mTitleList = new ArrayList<>();

        for (NewsData.DataBean bean : data) {
            mTitleList.add(bean.getTitle());
        }

        mLeftMenuAdapter = new LeftMenuAdapter();
        lv_menu.setAdapter(mLeftMenuAdapter);

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;

                //关闭Menu
                toggleMenu();

                // 更新当前页面
                updatePager(position);

                //更新适配器
                mLeftMenuAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updatePager(int position) {
        MainActicity mActivity = (MainActicity) this.mActivity;
        // 获取主屏幕页面
        MainContentFragment contentFragment = mActivity.getNewsPager();
        // 获取新闻中心页面
        NewsPager newsPager = contentFragment.getNewsPager();

        // 更新页面
        newsPager.setCurrentDetilPager(position);
    }

    private void toggleMenu() {
        MainActicity mActivity = (MainActicity) this.mActivity;
        SlidingMenu slidingMenu = mActivity.getSlidingMenu();
        //关闭 -- 打开
        slidingMenu.toggle();
    }

    private class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTitleList.size();
        }

        @Override
        public String getItem(int position) {
            return mTitleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(mActivity, R.layout.item_menu, null);
            TextView tv_menu_text = (TextView) view.findViewById(R.id.tv_menu_text);
            tv_menu_text.setText(getItem(position));

            // 被选条目高亮显示
            if (currentPosition == position) {
                tv_menu_text.setEnabled(true);
            } else {
                tv_menu_text.setEnabled(false);
            }

            return view;
        }
    }
}
