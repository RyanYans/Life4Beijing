package com.rya.life4beijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //禁用触摸事件 -- 不可左右滑动
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
