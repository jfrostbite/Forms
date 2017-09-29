package com.e_eduspace.forms.module;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.e_eduspace.forms.R;
import com.e_eduspace.forms.base.IToolbar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017-05-09.
 * Activity生命周期监听器，监控该app所有activity 的事件
 * <p>
 * 在此做activity的初始化操作
 */

public class ActivityLifeListener implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //实现IToolbar接口，默认使用Toolbar 方案的Activity
        if (activity instanceof IToolbar) {
            if (activity instanceof AppCompatActivity) {
                ((AppCompatActivity) activity).setSupportActionBar((Toolbar) activity.findViewById(R.id.app_toolbar));
                ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
                setToolBar(actionBar);
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                activity.setActionBar((android.widget.Toolbar) activity.findViewById(R.id.app_toolbar));
                android.app.ActionBar actionBar = activity.getActionBar();
                setToolBar(actionBar);
            }
            TextView tvTitle = (TextView) activity.findViewById(R.id.app_toolbar_title);
            if (tvTitle != null) {
                tvTitle.setText(activity.getTitle());
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private <E> void setToolBar(E e) {
        if (e == null) {
            throw new RuntimeException("错误的参数列表");
        }
        try {
            invokeMethod(e, "setDisplayShowTitleEnabled", false);
//            invokeMethod(e, "setDisplayHomeAsUpEnabled", true);
//            invokeMethod(e, "setDisplayShowHomeEnabled", true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private <E> void invokeMethod(E e, String name, Object... objs) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //更具穿进来的参数 获取参数类型
        Class<?>[] pts = new Class[objs.length];
        for (int i = 0; i < objs.length; i++) {
            pts[i] = convertType(objs[i].getClass());
        }
        Method method = e.getClass().getMethod(name, pts);
        if (method != null) {
            //强制访问
            method.setAccessible(true);
            method.invoke(e, objs);
        }
    }

    /**
     * 包装类型转换基础类型
     */
    private Class<?> convertType(Class<?> clazz){
        if (!clazz.isPrimitive()) {
//            clazz.getTypeParameters()
            if (clazz.isAssignableFrom(Boolean.class)) {
                clazz = Boolean.TYPE;
            }
        }
        return clazz;
    }
}
