package com.rya.life4beijing.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.rya.life4beijing.Fragment.LeftMenuFragment;
import com.rya.life4beijing.Fragment.MainContentFragment;
import com.rya.life4beijing.R;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */
public class MainActicity extends SlidingFragmentActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //兼容 含有虚拟按键的手机如neuxe huawei.. 避免虚拟按键覆盖
        initCompat();

        initMenu();

        initView();
    }

    //兼容 含有虚拟按键的手机如neuxe huawei.. 避免虚拟按键覆盖
    private void initCompat() {
        this.setSlidingActionBarEnabled(false);
    }

    private void initMenu() {
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.LEFT);
        slidingMenu.setBehindOffset(400);
    }

    private void initView() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.ll_left_menu, new LeftMenuFragment());
        transaction.replace(R.id.ll_main_content, new MainContentFragment());

        transaction.commit();
    }
}
