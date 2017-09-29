package com.e_eduspace.forms;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.e_eduspace.forms.coolpen.BluManger;
import com.e_eduspace.forms.model.db.DBManger;
import com.e_eduspace.forms.module.ActivityLifeListener;
import com.e_eduspace.forms.utils.KUtils;
import com.e_eduspace.forms.utils.LogUtils;
import com.e_eduspace.identify.IDentifySingle;

/**
 * Created by Administrator on 2017-05-09.
 * 全局App 生命周期最长
 * <p>
 * 各个activity 初始化
 */

public class App extends Application {

    private static Context mApp;
    private static Handler mHandler;
    private static int mTid;

    @Override
    public void onCreate() {
        super.onCreate();

        //数据库初始化
        DBManger.newInstance().init(this);

        //全局app初始化
        mApp = getApplicationContext();
        mHandler = new Handler(getMainLooper());
        mTid = android.os.Process.myTid();
        registerActivityLifecycleCallbacks(new ActivityLifeListener());
        BluManger.newInstance().init(this);
        new IDentifySingle.Builder(KUtils.getApp());
        new LogUtils.Builder().setGlobalTag(getPackageName() + ".kevin").setBorderSwitch(true).setLogSwitch(true);
    }

    public static Context getContext() {
        return mApp;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static int getMainTid() {
        return mTid;
    }
}
