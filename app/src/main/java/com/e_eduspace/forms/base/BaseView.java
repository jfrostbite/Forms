package com.e_eduspace.forms.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.e_eduspace.forms.R;
import com.e_eduspace.forms.coolpen.BluListeners;
import com.e_eduspace.forms.listener.DialogListener;
import com.e_eduspace.forms.model.constants.Constant;
import com.e_eduspace.forms.module.guide.GuideActivity;
import com.e_eduspace.forms.utils.KUtils;
import com.e_eduspace.forms.utils.SPUtils;
import com.e_eduspace.forms.utils.ToastUtils;
import com.e_eduspace.forms.widget.PenActionProvider;

import static com.e_eduspace.forms.widget.PenActionProvider.PenState.PEN_STATE_CONNECTED;
import static com.e_eduspace.forms.widget.PenActionProvider.PenState.PEN_STATE_OFFLINE;
import static com.e_eduspace.forms.widget.PenActionProvider.PenState.PEN_STATE_OUT;


/**
 * Created by Administrator on 2017-05-11.
 * <p>
 * 部分页面抽取
 *
 */

public abstract class BaseView<P extends IPresenter, E> extends AppCompatActivity implements IView<E>, DialogInterface.OnCancelListener,
        PenActionProvider.OnPenClickListener,DialogListener, View.OnClickListener {

    private AlertDialog.Builder mBuilder;
    private ProgressDialog mProgressDialog;
    protected PenActionProvider mActionProvider;
    private boolean isInit;
    protected P mPresenter;
    private AlertDialog mAlertDialog;
    private boolean isShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initIntent();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initPresenter();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mActionProvider != null) {
            resetPen();
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_toolbar);
        RelativeLayout rlContent = (RelativeLayout) findViewById(R.id.rl_base_content);
        LayoutInflater.from(this).inflate(layoutResID, rlContent);
    }

    public void showDialog(String msg) {
        showDialog(msg, null);
    }

    public void showDialog(String msg, final DialogListener listener) {
        if (!isShow) {
            return;
        }
        mBuilder = mBuilder == null ? new AlertDialog.Builder(this) : mBuilder;
        mAlertDialog = mAlertDialog == null ? mBuilder.setTitle("提示：")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onPositive();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onNegative();
                        }
                    }
                })
                .create() : mAlertDialog;
        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        } else {
            mAlertDialog.setMessage(msg);
        }
    }

    @Override
    public void showDialog(String title, String content, final DialogListener listener, final Object tag) {
        if (!isShow) {
            return;
        }
        RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(KUtils.dp2px(60),KUtils.dp2px(40),KUtils.dp2px(60),KUtils.dp2px(0));
        final EditText editText = new EditText(this);
        editText.setText(content);
        editText.setMaxLines(3);
        editText.setGravity(Gravity.CENTER);
        rl.addView(editText,lp);
        mBuilder = new AlertDialog.Builder(this);
        mAlertDialog = mBuilder.setTitle(title)
                .setView(rl)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onPositive(tag, editText.getText().toString().trim());
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onNegative();
                        }
                    }
                })
                .create();
        mAlertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);

        MenuItem item = menu.findItem(R.id.list_pen_info);

        mActionProvider = (PenActionProvider) MenuItemCompat.getActionProvider(item);

        createMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_pen_cancel:
                mActionProvider.setPenState(PEN_STATE_OUT);
                break;
        }
        itemSelected(item);
        return true;
    }

    protected void itemSelected(MenuItem item){

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isInit) {
            resetPen();
            windowChanged(hasFocus);
            isInit = true;
        }
    }

    /**
     * @param msg 为空，关闭
     */
    public void showLoading(String msg) {
        if (!isShow) {
            return;
        }
        if (KUtils.isEmpty(msg)) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            return;
        }
        mProgressDialog = mProgressDialog == null ? new ProgressDialog(this) : mProgressDialog;
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setOnCancelListener(this);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        } else {
            mProgressDialog.setMessage(msg);
        }
    }

    @Override
    public void bluSuccess() {
        if (mPresenter != null) {
            mActionProvider.setPenState(mPresenter.bluConnected() ? PEN_STATE_CONNECTED : KUtils.isEmpty(SPUtils.getString(Constant.BLU_MAC, "")) ? PenActionProvider.PenState.PEN_STATE_BREAK : PEN_STATE_OFFLINE);
            //画板api初始化,必须在 VP依附完成后进行
            if (mPresenter.bluConnected()) {
                mPresenter.coolPenInit();
            }
        }
    }

    @Override
    public void bluLoading(String loading) {
        if (!KUtils.isEmpty(loading)) {
            mActionProvider.setPenState(PenActionProvider.PenState.PEN_STATE_LOADING);
        } else {
            mActionProvider.setPenState(PenActionProvider.PenState.PEN_STATE_BREAK);
        }
    }

    @Override
    public void updateSignal(int rssi) {

    }

    /**
     * 此处涉及设备断开情况为： onPositive 为true时，视为设备从来没有连接过 break，否则为offline
     *
     * @param positive
     */
    @Override
    public void bluFailure(boolean positive) {
        mActionProvider.setPenState(positive ? PenActionProvider.PenState.PEN_STATE_BREAK : PEN_STATE_OFFLINE);
        showDialog("设备未连接，确定返回重新连接。", this);
    }

    @Override
    public void onStateChanged(PenActionProvider.PenState state) {
        if (mPresenter == null) {
            return;
        }
        switch (state) {
            case PEN_STATE_PER:
                mPresenter.getBattery();
                break;
            case PEN_STATE_LOADING:
            case PEN_STATE_RETRY:
                showLoading("正在连接...");
            case PEN_STATE_SCAN:
                mActionProvider.setLoadingForTimeOut(15000);
//                mActionProvider.setLoading();
                break;
            case PEN_STATE_OUT:
                SPUtils.clean();
                if (mPresenter.bluConnected()) {
                    mPresenter.bluDisConnect();
                }
                if (mPresenter.bluScanning()) {
                    mPresenter.bluDisScan();
                }
                break;
            case PEN_STATE_CONNECTED:
                showLoading(null);
                break;
            case PEN_STATE_OFFLINE:
                ToastUtils.snack(getWindow().getDecorView(), "设备连接断开");
                showLoading(null);
                break;
            case PEN_STATE_BREAK:
                ToastUtils.snack(getWindow().getDecorView(), "设备连接失败");
                showLoading(null);
                break;
            case PEN_STATE_TIMEOUT:
                ToastUtils.snack(getWindow().getDecorView(), "设备连接超时");
                showLoading("连接时间过长，请确保设备处于开启状态。");
                break;
            default:
                break;
        }
        stateAction(state);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        isShow = true;
        //蓝颜监听单例，重开界面需要重新设置View
        BluListeners.newInstance().init(this);
        if (mPresenter != null) {
            mPresenter.drawInit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShow = false;
    }

    protected void windowChanged(boolean hasFocus) {
        if (mPresenter != null) {
            mPresenter.bluInit();
        }
    }

    protected void createMenu(Menu menu) {
    }

    protected void stateAction(PenActionProvider.PenState state) {
        if (PenActionProvider.PenState.PEN_STATE_RETRY.equals(state)) {//已有设备，直接进行连接
            String mac = SPUtils.getString(Constant.BLU_MAC, "");
            mPresenter.bluConnect(mac);
        } else if (PEN_STATE_OUT.equals(state)) {//主动触发停止一切操作
            mActionProvider.setPenState(PenActionProvider.PenState.PEN_STATE_BREAK);
        } else if (PenActionProvider.PenState.PEN_STATE_SCAN.equals(state)) {//首次连接设备
            startActivity(new Intent(this, GuideActivity.class));
        }
}

    public void resetPen() {
        if (mPresenter != null) {
            mActionProvider.setOnPenClickListener(null);
            mActionProvider.setPenState(mPresenter.bluConnected() ? PEN_STATE_CONNECTED : KUtils.isEmpty(SPUtils.getString(Constant.BLU_MAC, "")) ? PenActionProvider.PenState.PEN_STATE_BREAK : PEN_STATE_OFFLINE);
            mActionProvider.setOnPenClickListener(this);
        }
    }

    @Override
    public void bluBattery(String per) {
        mActionProvider.setPenPer(per);
    }

    @Override
    public void onPositive(Object... objs) {

    }

    @Override
    public void onNegative() {

    }

    /**
     * 权限检查
     * @param requestCode
     * @param permissions
     * @return
     */
    protected boolean checkPermission(int requestCode, String... permissions) {
        boolean granted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                granted &= checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
                if (!granted) {
                    requestPermissions(permissions, requestCode);
                }
            }
            return granted;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog=null;
        }
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                onPermissionsGranted(requestCode);
            } else {
                ToastUtils.show("授权失败");
            }
        }
    }

    @Override
    public void initIntent() {

    }

    @Override
    public void initPresenter() {
        mPresenter = newPresenter();
        if (mPresenter != null) {
            mPresenter.attach(this);
        }
    }

    protected abstract P newPresenter();

    protected void onPermissionsGranted(int requestCode) {}

    @Override
    public void onClick(View v) {

    }
}
