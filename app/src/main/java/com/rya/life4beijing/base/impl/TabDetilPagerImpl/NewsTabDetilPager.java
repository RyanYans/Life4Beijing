package com.rya.life4beijing.base.impl.TabDetilPagerImpl;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rya.life4beijing.R;
import com.rya.life4beijing.Utils.ConstantsValue;
import com.rya.life4beijing.Utils.HttpUtil;
import com.rya.life4beijing.Utils.PrefUtil;
import com.rya.life4beijing.Utils.StreamUtil;
import com.rya.life4beijing.base.BaseTabDetilPager;
import com.rya.life4beijing.bean.NewsData;
import com.rya.life4beijing.bean.NewsTabBean;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 页面详情
 * <p>
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class NewsTabDetilPager extends BaseTabDetilPager {

    @BindView(R.id.vp_detail)
    ViewPager vpDetail;
    @BindView(R.id.tv_topnews_title)
    TextView topNewsTitle;
    @BindView(R.id.ll_topnews_point)
    LinearLayout topNewsPoint;

    private TextView mTextView;

    private static final String TAG = "NewsTabDetilPager";

    private NewsTabBean mTabData;
    private List<NewsTabBean.DataBean.TopnewsBean> mTopNews;
    private TopNewsDetailAdapter mNewsDetailAdapter;

    public NewsTabDetilPager(Activity activity, NewsData.DataBean.ChildrenBean childrenBean) {
        super(activity, childrenBean);

        // 设置标签页数据转向Url
        setmTabdUrl(ConstantsValue.BASE_URL + childrenBean.getUrl());

        // mNewsDetilData = childrenBean;
    }

    /**
     * 初始化 新闻详情布局
     *
     * @return 返回新闻详情页面的布局View 用于viewPager接收
     */
    @Override
    public View initView() {

        View view = View.inflate(getmActivity(), R.layout.pager_tab_detail, null);

        ButterKnife.bind(this, view);

//        vp_detail = (ViewPager)view.findViewById(R.id.vp_detail);

        //initPointView();

        return view;
    }

    private void initPointView() {
        topNewsPoint.removeAllViews();
        for (int index = 0; index < mTopNews.size(); index++) {
            //初始化小圆点
            View pointView = new View(getmActivity());
            pointView.setBackgroundResource(R.drawable.selector_point);
            LinearLayout.LayoutParams pointLayoutParams = new LinearLayout.LayoutParams(20, 20);
            if (index == 0) {
                pointView.setEnabled(true);
            }else {
                pointLayoutParams.leftMargin = 10;
                pointView.setEnabled(false);
            }
            pointView.setLayoutParams(pointLayoutParams);

            topNewsPoint.addView(pointView);
        }

    }

    @Override
    public void initData() {
        super.initData();
        //查看是否有json文件缓存
        String cacheFileStr = getCacheFile(getmChildrenBean().getTitle());
        if (cacheFileStr != null) {
            getDataFromJson(cacheFileStr);
        }

        //从服务器获取新数据
         getDataFromServer();
    }

    private String getCacheFile(String title) {
        File file = new File(getmActivity().getFilesDir().getPath(), title);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                String tabDataStr = StreamUtil.streamToString(fileInputStream);

                return tabDataStr;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void getDataFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                InputStream iStream = HttpUtil.getData(getmTabdUrl());
//                InputStreamReader iReader = new InputStreamReader(iStream);
                if (iStream != null) {
                    try {
                        String tabDataStr = StreamUtil.streamToString(iStream);

                        getDataFromJson(tabDataStr);

                        StreamUtil.writeFileToCache(getmActivity(), tabDataStr, getmChildrenBean().getTitle());
                        // PrefUtil.setString(getmActivity(), ConstantsValue.TABNEWS_JSON, tabDataStr);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.w(TAG, "run: Http Getting Error");
                }
            }
        }).start();
    }

    private void getDataFromJson(String tabDataStr) {

        Gson gson = new Gson();
        mTabData = gson.fromJson(tabDataStr, NewsTabBean.class);

        mTopNews = mTabData.getData().getTopnews();

        if (vpDetail != null) {
            getmActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mNewsDetailAdapter = new TopNewsDetailAdapter();
                    vpDetail.setAdapter(mNewsDetailAdapter);

                    // 首次进入ViewPager, 手动设置标题
                    topNewsTitle.setText(mTopNews.get(0).getTitle());

                    //初始化 小圆点
                    initPointView();

                    vpDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            topNewsTitle.setText(mTopNews.get(position).getTitle());

                            for (int index = 0; index < mTopNews.size(); index++) {
                                View pointView = topNewsPoint.getChildAt(index);
                                pointView.setEnabled(index == position);
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }
            });
        }
    }

    private class TopNewsDetailAdapter extends PagerAdapter {

        public TopNewsDetailAdapter() {

        }

        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(getmActivity());
            String topImageUrl = mTopNews.get(position).getTopimage();
            Picasso
                    .with(getmActivity())
                    .load(topImageUrl)
                    .into(imageView);

            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
