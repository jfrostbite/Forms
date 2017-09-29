package com.e_eduspace.forms.module.guide;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.e_eduspace.forms.R;
import com.e_eduspace.forms.base.BaseView;
import com.e_eduspace.forms.coolpen.BluManger;
import com.e_eduspace.forms.model.constants.Constant;
import com.e_eduspace.forms.module.main.MainActivity;
import com.e_eduspace.forms.utils.KUtils;
import com.e_eduspace.forms.widget.PenActionProvider;

import java.util.HashSet;
import java.util.Set;

import static com.e_eduspace.forms.widget.PenActionProvider.PenState.PEN_STATE_CONNECTED;

public class GuideActivity extends BaseView<GuideIPresenter, BluetoothDevice> implements GuideIView<BluetoothDevice>, ActionMenuView.OnMenuItemClickListener {

    private ActionMenuView mAmv;

    //蓝牙设备缓存
    private Set<BluetoothDevice> mDevices = new HashSet<>();
    private TextView mTvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_guide);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDevices.clear();
        if (mAmv != null) {
            mAmv.getMenu().removeGroup(R.id.group_blu_list);
        }
    }

    @Override
    public void initView() {
        mTvTip = (TextView) findViewById(R.id.tv_guide_tip);
    }

    @Override
    public void initListener() {
        mTvTip.setOnClickListener(this);
    }

    @Override
    protected GuideIPresenter newPresenter() {
        return new GuidePresenterImpl();
    }

    @Override
    public void updateView(BluetoothDevice device) {
        if (!mDevices.add(device)) {//设备已存在
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("device", device);
        String name = device.getName();
        mAmv.getMenu().add(R.id.group_blu_list, 0, 0, KUtils.isEmpty(name) ? "未知设备" : name).setIntent(intent);
    }

    /**
     * 此处实现，蓝牙列表操作
     *
     * @param menu
     */
    @Override
    protected void createMenu(Menu menu) {
        mAmv = (ActionMenuView) findViewById(R.id.amv_toolbar_menu);
        mAmv.setPopupTheme(R.style.AppTheme_PopupOverlay);
        mAmv.setOverflowIcon(null);
        mAmv.setOnMenuItemClickListener(this);
        getMenuInflater().inflate(R.menu.menu_blu_list, mAmv.getMenu());
    }

    /**
     * 区分点击事件类型
     * 未连接2种状态，连接设备，展示列表，打开蓝牙
     * <p>
     * 状态已处理，无需进行状态处理，可能导致死循环。
     *
     * @param state 该状态为现有状态，根据现有状态进行事件操作即可
     */
    @Override
    protected void stateAction(PenActionProvider.PenState state) {
        super.stateAction(state);
        if (!BluManger.newInstance().isOpen()) {
            mPresenter.showMsg("确认打开蓝牙？");
            return;
        }
        if (PenActionProvider.PenState.PEN_STATE_SCAN.equals(state)) {//首次连接设备，展示列表
            if (checkPermission(Constant.BLU_SCAN_PER, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                mPresenter.bluScan();
            }
            if (mAmv != null && !mAmv.isOverflowMenuShowing()) {
                mAmv.showOverflowMenu();
            }
        } else if (PenActionProvider.PenState.PEN_STATE_BREAK.equals(state) || PenActionProvider.PenState.PEN_STATE_OFFLINE.equals(state)) {//连接失败
            setTipState(-1);
        } else if (PenActionProvider.PenState.PEN_STATE_LOADING.equals(state)) {//正在连接
            setTipState(0);
        } else if (PEN_STATE_CONNECTED.equals(state)) {
            setTipState(1);
        }
    }

    @Override
    protected void windowChanged(boolean hasFocus) {
        super.windowChanged(hasFocus);

        if (mPresenter.bluConnected()) {
            setTipState(1);
        }
    }

    private void setTipState(int state) {
        CharSequence tip = "";
        switch (state) {
            case -1:
                tip = getResources().getText(R.string.guide_tip_1);
                mTvTip.setActivated(false);
                mTvTip.setClickable(false);
                break;
            case 0:
                tip = getResources().getText(R.string.guide_tip_2);
                mTvTip.setActivated(false);
                mTvTip.setClickable(false);
                break;
            case 1:
                tip = getResources().getText(R.string.guide_tip_3);
                mTvTip.setActivated(true);
                mTvTip.setClickable(true);
                break;
        }
        mTvTip.setText(tip);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent = item.getIntent();
        BluetoothDevice device = intent.getParcelableExtra("device");
        if (!mPresenter.bluConnected()) {
            mPresenter.bluConnect(device.getAddress());
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_guide_tip:
                if (mPresenter.bluConnected()) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onPermissionsGranted(int requestCode) {
        super.onPermissionsGranted(requestCode);
        if (requestCode == Constant.BLU_SCAN_PER) {
            mPresenter.bluScan();
        }
    }
}
