package com.rya.life4beijing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.rya.life4beijing.R;
import com.rya.life4beijing.Utils.ConstantsValue;
import com.rya.life4beijing.Utils.PrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {

    // Butter Knife 注解依赖
    @BindView(R.id.root_splash)
    LinearLayout root_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        initUI();

        initAnim();
    }

    private void initUI() {
        ButterKnife.bind(SplashActivity.this);

    }

    private void initAnim() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true);

        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(rotateAnimation);
        animSet.addAnimation(scaleAnimation);
        animSet.addAnimation(alphaAnimation);

        root_splash.startAnimation(animSet);

        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 当动画结束后
                // 是否第一次
                boolean first_enter = PrefUtil.getBoolean(getApplicationContext(), ConstantsValue.FIRST_INTER, true);
                if (first_enter) {
                    // 跳转到新手引导
                    Intent intent = new Intent(getApplicationContext(), UserGuideActivity.class);
                    startActivity(intent);
                } else {
                    // 跳转到主页面
                    Intent intent = new Intent(getApplicationContext(), MainActicity.class);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
