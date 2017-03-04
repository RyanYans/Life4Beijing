package com.rya.life4beijing.base.impl.TabDetilPagerImpl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rya.life4beijing.R;
import com.rya.life4beijing.Utils.ConstantsValue;
import com.rya.life4beijing.Utils.HttpUtil;
import com.rya.life4beijing.Utils.PrefUtil;
import com.rya.life4beijing.Utils.StreamUtil;
import com.rya.life4beijing.activity.NewsDetailActicity;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 页面详情
 * <p>
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class NewsTabDetilPager extends BaseTabDetilPager implements DragRefreshHeaderView.RefreshListener, AdapterView.OnItemClickListener {

    private static final Boolean IS_MORE = true;
    @BindView(R.id.tv_topnews_title)
    TextView topNewsTitle;
    @BindView(R.id.ll_topnews_point)
    LinearLayout topNewsPoint;
    @BindView(R.id.vp_detail)
    TopNewsViewPager vpDetail;

    private DragRefreshHeaderView mNewsListView;

    private TextView mTextView;

    private static final String TAG = "NewsTabDetilPager";

    private NewsTabBean mTabData;
    private List<NewsTabBean.DataBean.TopnewsBean> mTopNewsList;
    private TopNewsDetailAdapter mNewsDetailAdapter;
    private List<NewsTabBean.DataBean.NewsBean> mNewsList;
    private String mMoreUri;
    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentItem = vpDetail.getCurrentItem();
            currentItem++;

            if (currentItem >= mTopNewsList.size()) {
                currentItem = 0;
            }

            vpDetail.setCurrentItem(currentItem);

            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };


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

        mNewsListView = (DragRefreshHeaderView) view.findViewById(R.id.lv_news);

        mNewsListView.addHeaderView(listViewHeader);

        mNewsListView.setOnRefreshListener(this);

        mNewsListView.setOnItemClickListener(this);

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
            getDataFromJson(cacheFileStr, false);
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

                        getDataFromJson(tabDataStr, false);

                        StreamUtil.writeFileToCache(getmActivity(), tabDataStr, getmChildrenBean().getTitle());
                        // PrefUtil.setString(getmActivity(), ConstantsValue.TABNEWS_JSON, tabDataStr);

                        //模拟网络延迟
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        assert mNewsListView != null;
                        getmActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getmActivity(), "iStream not null , 网络连接成功！", Toast.LENGTH_SHORT).show();

                                mNewsListView.RefreshComplete(true);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.w(TAG, "run: Http Getting Error");
                    try {
                        Thread.sleep(6500);

                        getmActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mNewsListView.RefreshComplete(false);
                                Toast.makeText(getmActivity(), "iStream is null , 网络连接失败！", Toast.LENGTH_SHORT);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void getDataFromJson(String tabDataStr, boolean loddingMore) {

        Gson gson = new Gson();
        mTabData = gson.fromJson(tabDataStr, NewsTabBean.class);

        if (!loddingMore) {
            mNewsList = mTabData.getData().getNews();

            mTopNewsList = mTabData.getData().getTopnews();


            initHeaderView();

            // ListView
            if (mNewsListView != null) {
                getmActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNewsListView.setAdapter(new NewsAdapter());
                    }
                });
            }

            if (mHandler == null) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = vpDetail.getCurrentItem();
                        currentItem++;

                        if (currentItem >= mTopNewsList.size()) {
                            currentItem = 0;
                        }

                        vpDetail.setCurrentItem(currentItem);

                        mHandler.sendEmptyMessageDelayed(0, 3000);
                    }
                };

                mHandler.sendEmptyMessageDelayed(0, 3000);

                vpDetail.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mHandler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_CANCEL:     // 事件被取消
                                mHandler.sendEmptyMessageDelayed(0, 3000);
                                break;
                            case MotionEvent.ACTION_UP:
                                mHandler.sendEmptyMessageDelayed(0, 3000);
                                break;
                        }
                        return false;
                    }
                });
            }

        } else { // 加载更多。。
            List<NewsTabBean.DataBean.NewsBean> news = mTabData.getData().getNews();
            mNewsList.addAll(news);

            getmActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mNewsDetailAdapter.notifyDataSetChanged();

                    mNewsListView.RefreshComplete(true);
                }
            });
        }
    }

    private void initHeaderView() {
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
    }

    @Override
    public void onRefresh() {
        getDataFromServer();
    }

    @Override
    public void onLoddingMore() {
        String moreUri = mTabData.getData().getMore();
        if (! TextUtils.isEmpty(moreUri)) {
            mMoreUri = ConstantsValue.BASE_URL + moreUri;
        } else {
            mMoreUri = null;
        }

        if (mMoreUri != null) {
            System.out.println(mMoreUri);
            getMoreDataFromServer();
        } else {
            mNewsListView.RefreshComplete(true);
            Toast.makeText(getmActivity(), "没有最新数据了..", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMoreDataFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {   // 模拟网络延时
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                InputStream iStream = HttpUtil.getData(mMoreUri);
//                InputStreamReader iReader = new InputStreamReader(iStream);
                if (iStream != null) {
                    try {
                        String tabDataStr = StreamUtil.streamToString(iStream);

                        getDataFromJson(tabDataStr, true);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.w(TAG, "run: Http Getting Error");
                    try {
                        Thread.sleep(2000);

                        getmActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mNewsListView.RefreshComplete(false);
                                Toast.makeText(getmActivity(), "iStream is null , 网络连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /*
    * 新闻条目 点击处理
    * */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("Position >>>> " + position);

        //获取listView的头布局的个数
        int headerViewsCount = mNewsListView.getHeaderViewsCount();
        position = position - headerViewsCount;

        int newsId = mNewsList.get(position).getId();
        String read_id = PrefUtil.getString(getmActivity(), "read_ids", "");
        List<String> read_ids = Arrays.asList(read_id.split(","));

        if (! read_ids.contains(newsId)) {
            PrefUtil.setString(getmActivity(), "read_ids", read_id + newsId + ",");

            TextView tvNewsTitle = (TextView) view.findViewById(R.id.tv_news_title);
            tvNewsTitle.setTextColor(Color.GRAY);
        }

        //  ------- 页面跳转 ---------
        Intent intent = new Intent(getmActivity(), NewsDetailActicity.class);
        intent.putExtra("web_url", mTabData.getData().getNews().get(position).getUrl());
        getmActivity().startActivity(intent);
    }

    private class TopNewsDetailAdapter extends PagerAdapter {

        TopNewsDetailAdapter() {

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

            //获取当前条目id
            int currentNewsId = mNewsList.get(position).getId();
            // 检查Item是否已读
            String read_ids = PrefUtil.getString(getmActivity(), "read_ids", "");
            List<String> ids = Arrays.asList(read_ids.split(","));
            if (ids.contains(currentNewsId + "")) {
                viewHolder.tvNewsTitle.setTextColor(Color.GRAY);
            }

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
