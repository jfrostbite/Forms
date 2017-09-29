package com.e_eduspace.forms.coolpen;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.e_eduspace.forms.utils.KUtils;
import com.newchinese.coolpensdk.manager.BluetoothLe;

/**
 * Created by Administrator on 2017-05-12.
 * CoolPen 蓝牙模块封装
 * 蓝牙模块二次封装
 */

public class BluManger {

    private String mMac;
    private String mName;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                mListeners.onStateChanged(state);
            }
        }
    };
    private BluListeners mListeners;

    private BluManger() {
    }

    public boolean isConnected() {
        return getBlu().getConnected();
    }

    public void setKey(String key) {
        getBlu().setKey(key);
    }

    public void clearData() {
        getBlu().sendBleInstruct(BluetoothLe.EMPTY_STORAGE_DATA);
    }

    public void openStore() {
        getBlu().sendBleInstruct(BluetoothLe.OPEN_STORAGE_CHANNEL);
    }

    public void openWrite() {
        getBlu().sendBleInstruct(BluetoothLe.OPEN_WRITE_CHANNEL);
    }

    public void readData() {
        getBlu().sendBleInstruct(BluetoothLe.READ_STORAGE_INFO);
    }

    public void startScan() {
        getBlu().startScan();
    }

    public void disConnect() {
        getBlu().disconnectBleDevice();
    }

    public void closeBle() {
        getBlu().disableBle();
    }

    public boolean isScanning() {
        return getBlu().getScanning();
    }

    public void disScan() {
        getBlu().stopScan();
    }

    private interface BluMangerAgent {
        BluManger MANGER = new BluManger();
    }

    public static BluManger newInstance() {
        return BluMangerAgent.MANGER;
    }

    public void init(Context context) {
        getBlu().init(context);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
        }
        getBlu().setScanByServiceUUID(null);
        /*getBlu().setConnectTimeOut(1500)
                .setRetryConnectEnable(true)
                *//*.setRetryConnectCount(3)*//*
                .setScanPeriod(10000);*/
    }

    public boolean isOpen() {
        return getBlu().isBluetoothOpen();
    }

    /**
     * 一键设置
     *
     * @param listeners
     */
    public void setOnListeners(BluListeners listeners, boolean checkSign) {
        mListeners = listeners;
        getBlu().setOnLeNotificationListener(listeners);
        getBlu().setOnElectricityRequestListener(listeners);
        getBlu().setOnConnectListener(listeners);
        getBlu().setOnKeyListener(listeners);
        getBlu().setOnBleScanListener(listeners);
        if (checkSign) {
            setOnSignalChangedListener(listeners);
        }
    }

    /**
     * 信号检查
     */
    private void setOnSignalChangedListener(BluListeners listeners) {
        getBlu().enableAntiLost(5000, listeners);
    }

    public void openBlu() {
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        KUtils.getApp().registerReceiver(mReceiver, intentFilter);
        getBlu().enableBluetooth();
    }

    /**
     * 连接设备，对外暴漏
     * @param obj
     */
    public void connectBlu(Object obj) {
        if (obj.getClass().isAssignableFrom(BluetoothDevice.class)) {
            connectBlu((BluetoothDevice) obj);
        } else if (obj.getClass().isAssignableFrom(String.class)) {
            connectBlu((String) obj);
        } else {
            throw new RuntimeException("连接设备类型错误.");
        }
    }

    /**
     * 连接BluetoothDevice
     *
     * @return
     */
    private void connectBlu(BluetoothDevice device) {
        mName = KUtils.isEmpty(device.getName()) ? "unKnown" : device.getName();
        mMac = device.getAddress();
        getBlu().connectBleDevice(device);
    }

    /**
     * 连接 根据mac ,时机：已经连接过
     *
     * @return
     */
    private void connectBlu(String mac) {
        mName = "unKnown";
        mMac = mac;
        getBlu().connectBleDevice(mac);
    }

    public void getBattery(){
        getBlu().sendBleInstruct(BluetoothLe.OBTAIN_ELECTRICITY);
    }

    public String getBluName() {
        return mName;
    }

    public String getMac() {
        return mMac;
    }

    public BluetoothLe getBlu() {
        return BluetoothLe.getDefault();
    }

    /**
     * 蓝牙状态监听器【表情】
     */
    public interface OnBluStateChangedListener {
        void onStateChanged(int state);
    }

    public void unRegistReceiver (){
        if (mReceiver != null) {
            KUtils.getApp().unregisterReceiver(mReceiver);
        }
    }
}
