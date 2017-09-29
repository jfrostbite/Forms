package com.e_eduspace.forms.module.fund.form;

import com.e_eduspace.forms.base.IToolbar;
import com.e_eduspace.forms.base.IView;

/**
 * Created by Administrator on 2017-06-16.
 * 电子表单页面
 */

public interface FormIView<E> extends IView<E>,IToolbar {

    /**
     * 并发更新线程，判断当前展示页
     * @param page
     * @return
     */
    boolean showing(int page);

    /**
     * 截图
     * @param pageIndex
     */
    void screenShot(int pageIndex);
}
