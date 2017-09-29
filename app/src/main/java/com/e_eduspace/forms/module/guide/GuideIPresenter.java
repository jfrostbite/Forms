package com.e_eduspace.forms.module.guide;

import com.e_eduspace.forms.base.IPresenter;
import com.e_eduspace.forms.base.IView;

/**
 * Created by Administrator on 2017-05-11.
 * 主页 业务逻辑
 */

public interface GuideIPresenter<V extends IView> extends IPresenter<V> {

    /**
     * 获取蓝牙列表
     */
    void bluScan();

    /**
     * 连接
     */
    void bluConnect(Object obj);

    /**
     * 初始化
     */
    void bluInit();
}
