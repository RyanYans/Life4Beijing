package com.rya.life4beijing.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rya.life4beijing.R;
import com.rya.life4beijing.Utils.ConstantsValue;
import com.rya.life4beijing.Utils.PrefUtil;

import java.util.ArrayList;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */
public class UserGuideActivity extends Activity {

    private ViewPager viewpager_guide;
    private Button btn_guide;
    private ArrayList<ImageView> imageViewList;
    private LinearLayout ll_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initUI();

        initData();

        initAdapter();
    }

    private void initAdapter() {
        InnerPgrAdapter innerPgrAdapter = new InnerPgrAdapter();
        viewpager_guide.setAdapter(innerPgrAdapter);
    }

    private void initData() {
        int[] imagesId = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

        imageViewList = new ArrayList<>();

        for (int index = 0; index < imagesId.length; index++) {
            // 初始化图片控件容器
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(imagesId[index]);
            imageViewList.add(imageView);

            //初始化小圆点
            View pointView = new View(getApplicationContext());
            pointView.setBackgroundResource(R.drawable.selector_point);
            LinearLayout.LayoutParams pointLayoutParams = new LinearLayout.LayoutParams(20, 20);
            if (index == 0) {
                pointView.setEnabled(true);
            }else {
                pointLayoutParams.leftMargin = 10;
                pointView.setEnabled(false);
            }
            pointView.setLayoutParams(pointLayoutParams);

            ll_point.addView(pointView);
        }
    }

    private void initUI() {
        viewpager_guide = (ViewPager) findViewById(R.id.viewpager_guide);
        btn_guide = (Button) findViewById(R.id.btn_guide);
        ll_point = (LinearLayout) findViewById(R.id.ll_point);

        viewpager_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 最后一个页面显示跳转button
                if (position == imageViewList.size() - 1) {
                    btn_guide.setVisibility(Button.VISIBLE);
                } else {
                    btn_guide.setVisibility(Button.INVISIBLE);
                }
                for (int index = 0; index < ll_point.getChildCount(); index++) {
                    View pointView = ll_point.getChildAt(index);
                    pointView.setEnabled(index == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 首次进入置为 false
                PrefUtil.setBoolean(getApplicationContext(), ConstantsValue.FIRST_INTER, false);

                // 跳转至主页面
                Intent intent = new Intent(getApplicationContext(), MainActicity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    class InnerPgrAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
