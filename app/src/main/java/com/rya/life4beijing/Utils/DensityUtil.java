package com.rya.life4beijing.Utils;

import android.content.Context;

import com.rya.life4beijing.activity.context;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */
public class DensityUtil {
    public static int dip2px(int dip, Context ctx) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public static int px2dip(int px, Context ctx) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }
}
