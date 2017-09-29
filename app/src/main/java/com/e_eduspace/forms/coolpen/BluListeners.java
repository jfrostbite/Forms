package com.e_eduspace.forms.coolpen;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;

import com.e_eduspace.forms.base.IView;
import com.e_eduspace.forms.listener.DialogListener;
import com.e_eduspace.forms.model.constants.Constant;
import com.e_eduspace.forms.utils.KUtils;
import com.e_eduspace.forms.utils.LogUtils;
import com.e_eduspace.forms.utils.SPUtils;
import com.newchinese.coolpensdk.listener.OnBleScanListener;
import com.newchinese.coolpensdk.listener.OnConnectListener;
import com.newchinese.coolpensdk.listener.OnElectricityRequestListener;
import com.newchinese.coolpensdk.listener.OnKeyListener;
import com.newchinese.coolpensdk.listener.OnLeNotificationListener;
import com.newchinese.coolpensdk.listener.OnReadRssiListener;

/**
 * Created by Administrator on 2017-05-11.
 * 蓝牙相关接口集合。单利
 */

public class BluListeners extends BluetoothGattCallback implements OnConnectListener, OnBleScanListener, OnReadRssiListener, OnLeNotificationListener, OnElectricityRequestListener, OnKeyListener, DialogListener,
        BluManger.OnBluStateChangedListener {

    private IView mMainView;

    private BluListeners() {

    }

    private interface BluListenerManger {
        BluListeners ins = new BluListeners();
    }

    public static BluListeners newInstance() {
        return BluListenerManger.ins;
    }

    /**
     * 必须初始化
     *
     * @param iView
     * @return
     */
    public BluListeners init(IView iView) {
        mMainView = iView;
        return this;
    }

    /**
     * 扫描结果
     *
     * @param bluetoothDevice
     * @param i
     * @param bytes
     */
    @Override
    public void onScanResult(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
        if (mMainView != null) {
            mMainView.updateView(bluetoothDevice);
        }
    }

    /**
     * 停止扫描
     */
    @Override
    public void onScanCompleted() {
//        mMainView.bluLoading(null);
    }

    /**
     * 连接成功
     */
    @Override
    public void onConnected() {

        //保存数据
        SPUtils.putString(Constant.BLU_NAME, BluManger.newInstance().getBluName());
        SPUtils.putString(Constant.BLU_MAC, BluManger.newInstance().getMac());
        if (mMainView != null) {
            mMainView.bluSuccess();
        }
    }

    /**
     * 连接关闭
     */
    @Override
    public void onDisconnected() {
        LogUtils.i("onDisconnected");
        if (mMainView != null) {
            mMainView.bluFailure(KUtils.isEmpty(SPUtils.getString(Constant.BLU_MAC, "")));
        }
    }

    @Override
    public void onFailed(int err) {
        LogUtils.i("onFailed",err);
        if (err != 1) {
            if (mMainView != null) {
                mMainView.bluFailure(KUtils.isEmpty(SPUtils.getString(Constant.BLU_MAC, "")));
            }
        }
    }

    @Override
    public void isConnecting() {
        if (mMainView != null) {
            mMainView.bluLoading("正在连接...");
        }
    }

    @Override
    public void onElectricityDetected(String s) {
        String str = s.substring(s.length() - 2, s.length());
        str = Integer.valueOf(str, 16).toString();
        if (mMainView != null) {
            mMainView.bluBattery(str);
        }
    }

    @Override
    public void onKeyGenerated(String s) {
        SPUtils.putString(Constant.BLU_KEY, s);
    }

    @Override
    public void onSetLocalKey() {
        String key = SPUtils.getString(Constant.BLU_KEY, "");
        BluManger.newInstance().setKey(key);
    }

    @Override
    public void onReadHistroyInfo() {
        BluManger.newInstance().clearData();
    }

    @Override
    public void onHistroyInfoDetected() {
        if (mMainView != null) {
            mMainView.showDialog("是否打开历史通道", this);
        }
        BluManger.newInstance().openStore();
    }

    @Override
    public void onHistroyInfoDeleted() {
        BluManger.newInstance().openWrite();
    }

    /**
     * 防丢信号
     *
     * @param i
     */
    @Override
    public void onSuccess(int i) {
        if (mMainView!=null) {
            mMainView.updateSignal(i);
        }
    }

    /**
     * dialog 功能
     */
    @Override
    public void onPositive(Object... objs) {
        BluManger.newInstance().readData();
    }

    @Override
    public void onNegative() {
        BluManger.newInstance().clearData();
    }

    @Override
    public void onStateChanged(int state) {
        if (state == BluetoothAdapter.STATE_ON) {//蓝牙打开
            BluManger.newInstance().unRegistReceiver();
//            mMainView.resetPen();
        }
    }
}
