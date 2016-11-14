package com.rya.life4beijing.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.text.BreakIterator;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class TopNewsViewPager extends ViewPager {

    private float startX;
    private float startY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 不要拦截事件
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();

                float dx = moveX - startX;
                float dy = moveY - startY;

                System.out.println(dx + " >>>>>> " + dy);

                // 左右滑动
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0) {
                        // 向左划页
                        if (getCurrentItem() == 0) {
                            // 第一个Viewpager， 不禁止拦截事件
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }

                    } else if (dx < 0) {
                        // 向右划页
                        // 获取适配器页面总数
                        int count = getAdapter().getCount();
                        if (getCurrentItem() == (count - 1)) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
