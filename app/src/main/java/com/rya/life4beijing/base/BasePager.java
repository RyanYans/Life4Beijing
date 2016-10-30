package com.rya.life4beijing.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rya.life4beijing.Activity.MainActicity;
import com.rya.life4beijing.R;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class BasePager {
    protected Activity mActivity;
    public ImageButton imgBtnMenu;
    public FrameLayout pagerContent;
    public TextView titleText;
    public final View mRootView;

    public Activity getmActivity() {
        return mActivity;
    }

    public ImageButton getImgBtnMenu() {
        return imgBtnMenu;
    }

    public FrameLayout getPagerContent() {
        return pagerContent;
    }

    public TextView getTitleText() {
        return titleText;
    }

    public View getmRootView() {
        return mRootView;
    }

    public BasePager(Activity activity) {
        mActivity = activity;

        mRootView = initView();
    }

    private View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);

        titleText = (TextView) view.findViewById(R.id.title_text);
        imgBtnMenu = (ImageButton) view.findViewById(R.id.imgBtn);
        pagerContent = (FrameLayout) view.findViewById(R.id.fl_pager_content);

        imgBtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //按钮打开关闭菜单
                MainActicity mActivity = (MainActicity) BasePager.this.mActivity;
                mActivity.getSlidingMenu().toggle();
            }
        });

        return view;
    }

    public void initData() {

    }

}
