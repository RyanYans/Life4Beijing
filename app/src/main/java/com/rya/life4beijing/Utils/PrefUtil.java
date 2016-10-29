package com.rya.life4beijing.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Trace;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */
public class PrefUtil {

    public static boolean getBoolean(Context applicationContext, String key, boolean mode) {
        SharedPreferences sp = applicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }
    public static void setBoolean(Context applicationContext, String key, boolean value) {
        SharedPreferences sp = applicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static int getInt(Context applicationContext, String key, int defValue) {
        SharedPreferences sp = applicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }
    public static void setInt(Context applicationContext, String key, int value) {
        SharedPreferences sp = applicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static String getString(Context applicationContext, String key, String defValue) {
        SharedPreferences sp = applicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }
    public static void setString(Context applicationContext, String key, String value) {
        SharedPreferences sp = applicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.apply();
    }
}
