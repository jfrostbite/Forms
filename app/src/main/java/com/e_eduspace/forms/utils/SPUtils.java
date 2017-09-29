package com.e_eduspace.forms.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017-05-12.
 * <p>
 * SP 工具类
 */

public class SPUtils {

    private static final String ICBC_CONFIG_NAME = "icbc_config";
    private static SharedPreferences mSP;

    private static void init() {
        mSP = mSP == null ? KUtils.getApp().getSharedPreferences(ICBC_CONFIG_NAME, Context.MODE_ENABLE_WRITE_AHEAD_LOGGING) : mSP;
    }

    public static boolean putString(String... v) {
        if (v.length < 2) {
            throw new RuntimeException("保存SPString数据格式错误 k--v");
        }
        init();
        return mSP.edit().putString(v[0], v[1]).commit();
    }

    public static String getString(String... v) {
        if (v.length < 2) {
            throw new RuntimeException("获取SPString数据格式错误 k--d");
        }
        init();
        return mSP.getString(v[0], v[1]);
    }

    public static boolean putBoolean(String key, boolean value) {
        init();
        return mSP.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String k, boolean d) {
        init();
        return mSP.getBoolean(k, d);
    }

    public static void clean(){
        init();
        mSP.edit().clear().apply();
    }

    public static void del(String key){
        init();
        mSP.edit().remove(key).apply();
    }
}
