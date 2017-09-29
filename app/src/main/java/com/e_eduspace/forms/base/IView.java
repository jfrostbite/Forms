package com.e_eduspace.forms.base;

import android.content.Intent;

import com.e_eduspace.forms.listener.DialogListener;


/**
 * Created by Administrator on 2017-05-09.
 * MVP 公用View接口
 *
 * O  用于子功能模块 ui更新 数据类型确定
 */

public interface IView<E> {
    void initIntent();
    //初始化
    void initView();

    //监听初始化
    void initListener();

    //ui刷新
    void updateView(E e);

    void showDialog(String msg);

    void showDialog(String msg, DialogListener listener);

    void showDialog(String title, String content, DialogListener listener, Object tag);

    void showLoading(String msg);

    void initPresenter();

    void bluBattery(String per);

    /**
     * 蓝牙设备连接成功
     */
    void bluSuccess();

    void bluFailure(boolean positive);

    /**
     * 连接中
     */
    void bluLoading(String loading);

    /**
     * 更新信号
     * @param rssi
     */
    void updateSignal(int rssi);

    void startActivity(Intent intent);
}
