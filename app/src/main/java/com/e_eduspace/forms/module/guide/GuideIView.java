package com.e_eduspace.forms.module.guide;

import com.e_eduspace.forms.base.IToolbar;
import com.e_eduspace.forms.base.IView;

/**
 * Created by Administrator on 2017-05-11.
 * 主页UI刷新
 */

public interface GuideIView<E> extends IView<E>,IToolbar {

    /**
     * 蓝牙设备连接成功
     */
    void bluSuccess();

    /**
     * 连接失败（是否主动）
     */
    void bluFailure(boolean positive);

    /**
     * 获取电量
     */
    void bluBattery(String per);


    /**
     * 重置pen
     */
    void resetPen();
}
