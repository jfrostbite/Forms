package com.e_eduspace.forms.module.fund.form;

import com.e_eduspace.forms.base.IPresenter;
import com.e_eduspace.forms.base.IView;

/**
 * Created by Administrator on 2017-06-16.
 */

public interface FormIPresenter<V extends IView> extends IPresenter<V> {
    /**
     * 识别数据
     * @param page 指定页 从1开始
     */
    void discern(int page);
}
