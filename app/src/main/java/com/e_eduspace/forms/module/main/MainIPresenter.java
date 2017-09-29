package com.e_eduspace.forms.module.main;

import com.e_eduspace.forms.base.IPresenter;
import com.e_eduspace.forms.base.IView;

/**
 * Created by Administrator on 2017-07-04.
 */

public interface MainIPresenter<V extends IView> extends IPresenter<V> {

    void generateData();
}
