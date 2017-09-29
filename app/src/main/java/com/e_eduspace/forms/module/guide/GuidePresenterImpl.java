package com.e_eduspace.forms.module.guide;

import android.bluetooth.BluetoothDevice;

import com.e_eduspace.forms.base.BasePresenter;
import com.e_eduspace.forms.coolpen.BluManger;


/**
 * Created by Administrator on 2017-05-11.
 */

public class GuidePresenterImpl extends BasePresenter<GuideIView<BluetoothDevice>> implements GuideIPresenter<GuideIView<BluetoothDevice>> {

    @Override
    public void bluScan() {
        BluManger.newInstance().startScan();
    }

    /**
     * dialog 功能
     */
    @Override
    public void onPositive(Object... objs) {
        bluOpen();
        mIView.showLoading(null);
        mIView.resetPen();
    }

    @Override
    public void onNegative() {
        mIView.showLoading(null);
        mIView.resetPen();
    }
}
