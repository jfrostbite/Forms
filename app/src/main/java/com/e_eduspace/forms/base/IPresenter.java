package com.e_eduspace.forms.base;

/**
 * Created by Administrator on 2017-05-09.
 * MVP 基础业务逻辑
 *
 * VIew 绑定
 */

public interface IPresenter<V extends IView> {

    void attach(V v);

    void detach();

    void showMsg(String msg);

    /**
     * 获取电量
     */
    void getBattery();

    /**
     * 断开
     */
    void bluDisConnect();

    boolean bluOpened();

    boolean bluConnected();

    void bluInit();

    /**
     * 连接
     */
    void bluConnect(Object obj);

    /**
     * 关闭蓝牙
     */
    void bluClose();

    /**
     * 蓝牙扫描状态
     * @return
     */
    boolean bluScanning();

    /**
     * 停止扫描蓝牙
     */
    void bluDisScan();

    /**
     * 打开蓝牙
     */
    void bluOpen();

    void clear(int pageIndex);

    /**
     * 绘制初始化
     */
    void coolPenInit();

    /**
     * 绘制API初始化
     */
    void drawInit();
}
