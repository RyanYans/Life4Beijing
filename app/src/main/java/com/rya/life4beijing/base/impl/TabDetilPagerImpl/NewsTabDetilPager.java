package com.rya.life4beijing.base.impl.TabDetilPagerImpl;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rya.life4beijing.R;
import com.rya.life4beijing.Utils.ConstantsValue;
import com.rya.life4beijing.Utils.HttpUtil;
import com.rya.life4beijing.Utils.StreamUtil;
import com.rya.life4beijing.base.BaseTabDetilPager;
import com.rya.life4beijing.bean.NewsData;
import com.rya.life4beijing.bean.NewsTabBean;
import com.rya.life4beijing.view.DragRefreshHeaderView;
import com.rya.life4beijing.view.TopNewsViewPager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
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

    @BindView(R.id.tv_topnews_title)
    TextView topNewsTitle;
    @BindView(R.id.ll_topnews_point)
    LinearLayout topNewsPoint;
    @BindView(R.id.vp_detail)
    TopNewsViewPager vpDetail;

    private DragRefreshHeaderView  newsListView;

    private TextView mTextView;

    private static final String TAG = "NewsTabDetilPager";

    private NewsTabBean mTabData;
    private List<NewsTabBean.DataBean.TopnewsBean> mTopNewsList;
    private TopNewsDetailAdapter mNewsDetailAdapter;
    private List<NewsTabBean.DataBean.NewsBean> mNewsList;


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

        /*
        View listViewHeader = View.inflate(getmActivity(), R.layout.layout_news_listview_header, null);
        ButterKnife.bind(this, listViewHeader);

        View view = View.inflate(getmActivity(), R.layout.pager_tab_detail, null);
        ButterKnife.bind(this, view);
        */ // 如何通过butterKnife绑定多个View呢？ -- error：java.lang.IllegalStateException: Required view 'vp_detail' with ID 2131427436 for field 'vpDetail' was not found. If this view is optional add '@Nullable' (fields) or '@Optional' (methods) annotation.

        View listViewHeader = View.inflate(getmActivity(), R.layout.layout_news_listview_header, null);
        ButterKnife.bind(this, listViewHeader);

        View view = View.inflate(getmActivity(), R.layout.pager_tab_detail, null);
        View dragView = View.inflate(getmActivity(), R.layout.header_dragview, null);

        newsListView = (DragRefreshHeaderView) view.findViewById(R.id.lv_news);

        newsListView.addHeaderView(listViewHeader);

        newsListView.setOnRefreshListener(new DragRefreshHeaderView.RefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();

            }
        });

//        vp_detail = (ViewPager)view.findViewById(R.id.vp_detail);

        //initPointView();

        return view;
    }

    private void initPointView() {
        topNewsPoint.removeAllViews();
        for (int index = 0; index < mTopNewsList.size(); index++) {
            //初始化小圆点
            View pointView = new View(getmActivity());
            pointView.setBackgroundResource(R.drawable.selector_point);
            LinearLayout.LayoutParams pointLayoutParams = new LinearLayout.LayoutParams(20, 20);
            if (index == 0) {
                pointView.setEnabled(true);
            } else {
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

        mNewsList = mTabData.getData().getNews();

        mTopNewsList = mTabData.getData().getTopnews();

        // ViewPager
        if (vpDetail != null) {
            getmActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mNewsDetailAdapter = new TopNewsDetailAdapter();
                    vpDetail.setAdapter(mNewsDetailAdapter);

                    // 首次进入ViewPager, 手动设置标题
                    topNewsTitle.setText(mTopNewsList.get(0).getTitle());

                    //初始化 小圆点
                    initPointView();

                    vpDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            // 设置头条新闻标题
                            topNewsTitle.setText(mTopNewsList.get(position).getTitle());

                            // 设置头条新闻指示器
                            for (int index = 0; index < mTopNewsList.size(); index++) {
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

        // ListView
        if (newsListView != null) {
            getmActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newsListView.setAdapter(new NewsAdapter());
                }
            });
        }

        //模拟网络延迟
        try {
            new Thread().sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assert newsListView != null;
        getmActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newsListView.RefreshComplete();
            }
        });

    }

    private class TopNewsDetailAdapter extends PagerAdapter {

        public TopNewsDetailAdapter() {

        }

        @Override
        public int getCount() {
            return mTopNewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(getmActivity());
            String topImageUrl = mTopNewsList.get(position).getTopimage();
            Picasso
                    .with(getmActivity())
                    .load(topImageUrl)
                    .fit()
                    .placeholder(R.drawable.topnews_item_default)
                    .error(R.drawable.topnews_item_default)
                    .into(imageView);

            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsTabBean.DataBean.NewsBean getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getmActivity(), R.layout.item_news, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            NewsTabBean.DataBean.NewsBean newsData = getItem(position);
            viewHolder.tvNewsTitle.setText(newsData.getTitle());
            viewHolder.tvNewsTime.setText(newsData.getPubdate());
            Picasso
                    .with(getmActivity())
                    .load(newsData.getListimage())
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(viewHolder.ivNewsImg);

            return convertView;
        }
    }

    static class ViewHolder {
        @BindView(R.id.iv_news_img)
        ImageView ivNewsImg;
        @BindView(R.id.tv_news_title)
        TextView tvNewsTitle;
        @BindView(R.id.tv_news_time)
        TextView tvNewsTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
