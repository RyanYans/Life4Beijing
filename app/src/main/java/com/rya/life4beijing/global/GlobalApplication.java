package com.rya.life4beijing.global;

import android.app.Application;
import android.content.Context;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class GlobalApplication extends Application {

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public Context getmApplicationContext() {
        return this.context;
    }
}
