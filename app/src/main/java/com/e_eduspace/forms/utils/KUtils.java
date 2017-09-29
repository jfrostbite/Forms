package com.e_eduspace.forms.utils;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

import com.e_eduspace.forms.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by Administrator on 2017-05-09.
 * <p>
 * 通用工具类
 */

public class KUtils {

    /**
     * 获取全局Context
     */
    public static Context getApp() {
        return App.getContext();
    }

    /**
     * 获取主线程Handler
     */
    public static Handler getHandler() {
        return App.getHandler();
    }

    /**
     * 获取主线程id
     */
    public static int getMainThreadId() {
        return App.getMainTid();
    }

    /**
     * 是否运行在主线程
     */
    public static boolean isRunMainThread() {
        return Thread.currentThread().getId() == getMainThreadId();
    }

    /**
     * 在主线程运行
     */
    public static void runOnMainThread(Runnable r) {
        if (isRunMainThread()) {
            r.run();
        } else {
            getHandler().post(r);
        }
    }

    /**
     * px转dp
     */
    public static float px2dp(float px) {
        float density = getApp().getResources().getDisplayMetrics().density;
        return density / (px + 0.5f);
    }

    /**
     * dp转px
     */
    public static int dp2px(float dp) {
        float density = getApp().getResources().getDisplayMetrics().density;
        return (int) ((dp +  + 0.5f) * density);
    }

    public static boolean isEmpty(String txt) {
        if (txt != null && !"".equalsIgnoreCase(txt.trim()) && !"null".equalsIgnoreCase(txt.trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 布局填充
     */
    public static View inflatView(@LayoutRes int id) {
        return LayoutInflater.from(getApp()).inflate(id, null);
    }

    public static int[] getCenter(int width, int height) {
        int[] center = new int[2];
        int x = getApp().getResources().getDisplayMetrics().widthPixels / 2;
        int y = getApp().getResources().getDisplayMetrics().heightPixels / 2;
        center[0] = x;
        center[1] = y;
        return center;
    }

    //文件2文本
    public static String file2String(String path){
        FileReader fr = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                fr = new FileReader(file);
                br = new BufferedReader(fr);
                String line = null;
                while((line =br.readLine())!=null){
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                assert br != null;
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static FilenameFilter makeFilenameFilter (final String filterName) {
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(filterName);
            }
        };
    }
}
