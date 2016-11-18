package com.rya.life4beijing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rya.life4beijing.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class DragRefreshHeaderView extends ListView {

    private static final int STATE_NULL = 0;
    private static final int STATE_PULL_DOWN_REFRESH = 1;
    private static final int STATE_RELEASE_UP_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_header_tittle)
    TextView tvHeaderTittle;
    @BindView(R.id.tv_header_time)
    TextView tvHeaderTime;

    private View mHederView;
    private int mHeight;
    private int startY;
    private int moveY;
    private int dy;
    private RotateAnimation mPullDownRotateAnimation;
    private int mCurrentState;
    private RotateAnimation mReleaseUpRotateAnimation;

    public DragRefreshHeaderView(Context context) {
        this(context, null);
    }

    public DragRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();

        initAnimation();
    }

    private void initAnimation() {
        mPullDownRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mPullDownRotateAnimation.setDuration(300);
        mPullDownRotateAnimation.setFillAfter(true);

        mReleaseUpRotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mReleaseUpRotateAnimation.setDuration(300);
        mReleaseUpRotateAnimation.setFillAfter(true);
    }

    private void initHeaderView() {
        mCurrentState = STATE_NULL;

        mHederView = View.inflate(getContext(), R.layout.header_dragview, null);

        ButterKnife.bind(this, mHederView);

        this.addHeaderView(mHederView);

        mHederView.measure(0, 0);

        mHeight = mHederView.getMeasuredHeight();

        mHederView.setPadding(0, -mHeight, 0, 0);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                // 假如按住ViewPager部分拉动下滑，判断 - 难点
                if (startY == -1) {
                    startY = (int) ev.getY();
                }
                moveY = (int) ev.getY();

                dy = moveY - startY;

                int firstVisiblePosition = getFirstVisiblePosition();

                if (firstVisiblePosition != 0) {
                    mCurrentState = STATE_NULL;
                }

                if (firstVisiblePosition == 0 && dy > 0 && mCurrentState != STATE_REFRESHING) {
                    int headerPadding = dy - mHeight;
                    mHederView.setPadding(0, headerPadding, 0, 0);

                    if (headerPadding >= 0 && mCurrentState != STATE_RELEASE_UP_REFRESH) {
                        stateChange(STATE_RELEASE_UP_REFRESH);
                    } else if (headerPadding < 0 && mCurrentState != STATE_PULL_DOWN_REFRESH) {
                        stateChange(STATE_PULL_DOWN_REFRESH);
                    }

                    return true;
                } else {
                    startY = moveY;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentState == STATE_PULL_DOWN_REFRESH) {
                    mCurrentState = STATE_NULL;
                    mHederView.setPadding(0, -mHeight, 0, 0);
                } else if (mCurrentState == STATE_RELEASE_UP_REFRESH) {
                    stateChange(STATE_REFRESHING);
                    mHederView.setPadding(0, 0, 0, 0);
                }

                break;
            default:
                break;
        }


        return super.onTouchEvent(ev);
    }

    private void stateChange(int state) {
        switch (state) {
            case STATE_PULL_DOWN_REFRESH:
                mCurrentState = STATE_PULL_DOWN_REFRESH;
                tvHeaderTittle.setText("下拉刷新");
                ivArrow.startAnimation(mPullDownRotateAnimation);
                break;
            case STATE_RELEASE_UP_REFRESH:
                mCurrentState = STATE_RELEASE_UP_REFRESH;
                tvHeaderTittle.setText("松开立即刷新");
                ivArrow.startAnimation(mReleaseUpRotateAnimation);

                break;
            case STATE_REFRESHING:
                mCurrentState = STATE_REFRESHING;
                tvHeaderTittle.setText("正在刷新...");
                ivArrow.clearAnimation();
                ivArrow.setVisibility(INVISIBLE);
                pbLoading.setVisibility(VISIBLE);

                //回掉给上级调用者监听者
                if (mListener != null) {
                    mListener.onRefresh();
                }
                break;
            default:
                break;
        }
    }

    public void RefreshComplete() {
        mCurrentState = STATE_NULL;
        mHederView.setPadding(0, -mHeight, 0, 0);
        tvHeaderTittle.setText("下拉刷新");
        ivArrow.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
    }

    private RefreshListener mListener;

    public void setOnRefreshListener(RefreshListener listener) {
        this.mListener = listener;
    }

    public interface RefreshListener {
        public void onRefresh();
    }

}
