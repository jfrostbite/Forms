package com.e_eduspace.forms.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ActionProvider;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.e_eduspace.forms.R;
import com.e_eduspace.forms.utils.KUtils;
import com.e_eduspace.forms.utils.LogUtils;

/**
 * Created by Administrator on 2017-05-10.
 * <p>
 * ActionBar 踩点MenulProvider 属性自定义空间
 * <p>
 * 用于CoolPen 状态，连接，断开 使用
 * <p>
 * 事件传递
 * onclick - 》 setState -》 listener.onClicke() 事件回调
 */

public class PenActionProvider extends ActionProvider implements View.OnClickListener, View.OnLongClickListener {

    private static final int PEN_STATE_BREAK = 0;
    private static final int PEN_STATE_LOADING = 1;
    private static final int PEN_STATE_PER = 2;
    private static final int PEN_STATE_CONNECTED = 3;
    private static final int PEN_STATE_OFFLINE = 4;
    private static final int PEN_STATE_RETRY = 5;
    private static final int PEN_STATE_SCAN = 6;
    private static final int PEN_STATE_OUT = 7;
    private static final int PEN_STATE_TIMEOUT = 8;

    private TextView ibPen;

    //笔状态
    private PenState penState;
    private OnPenClickListener mListener;

    private Animation animation;

    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public PenActionProvider(Context context) {
        super(context);
        animation = AnimationUtils.loadAnimation(context, R.anim.pen_loading);
        LinearInterpolator lin = new LinearInterpolator();//匀速  Accelerateinterpolator加速  DecelerateInterpolator减速
        animation.setInterpolator(lin);//设置速率
    }

    @Override
    public View onCreateActionView() {
        int size = 64;

        ibPen = ibPen == null ? new TextView(getContext()) : ibPen;

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(size, size);

        ibPen.setLayoutParams(params);

        ibPen.setOnClickListener(this);

        ibPen.setOnLongClickListener(this);

        ibPen.setGravity(Gravity.CENTER);

        ibPen.setClickable(true);

        ibPen.setTextColor(Color.WHITE);

        return ibPen;
    }

    /**
     * 更新笔状态
     */
    public void setPenState(PenState state) {
        KUtils.getHandler().removeCallbacks(null);
        LogUtils.i(state);
        if (penState == state) {
            return;
        }
        updateState(state);
        if (mListener != null) {
            mListener.onStateChanged(penState);
        }
    }

    /**
     * 获取笔状态
     */
    public PenState getPenState() {
        return penState;
    }

    /**
     * 设置电量
     */
    public void setPenPer(String per) {
        if (PenState.PEN_STATE_PER != penState) {
            throw new RuntimeException("CoolPen连接状态错误,当前状态："+penState);
        }
        ibPen.setText(per);
        KUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setPenState(PenState.PEN_STATE_CONNECTED);
            }
        }, 3000);
    }

    /**
     * 加载状态
     * <p>
     * 超时时间最小5秒，小于5秒，默认永久等待
     */
    public void setLoadingForTimeOut(int timeout) {
//        ibPen.setText("连");
        ibPen.setAnimation(animation);
        ibPen.startAnimation(animation);
        if (timeout < 5000) {
            return;
        }
        KUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if (penState == PenState.PEN_STATE_RETRY) {
                    updateState(PenState.PEN_STATE_OFFLINE);
                } else */if (penState == PenState.PEN_STATE_LOADING) {
                    setPenState(PenState.PEN_STATE_TIMEOUT);
                } else if (penState == PenState.PEN_STATE_SCAN) {
                    updateState(PenState.PEN_STATE_BREAK);
                    clearState();
                }
            }
        }, timeout);
    }

    public void setLoading() {
        setLoadingForTimeOut(0);
    }

    /**
     * 内部状态更新
     *
     * @param state
     */
    private void updateState(PenState state) {
        if (ibPen == null) {
            throw new RuntimeException("PenActionProvider 初始化错误");
        }
        clearState();
        penState = state;
        int rid = R.drawable.pen_state_selector;
        switch (state) {
            case PEN_STATE_PER:
                rid = R.color.translate;
                break;
            case PEN_STATE_LOADING:
            case PEN_STATE_RETRY:
            case PEN_STATE_SCAN:
                rid = R.mipmap.pen_loading;
                break;
            case PEN_STATE_CONNECTED:
                ibPen.setActivated(true);
//                rid = R.mipmap.pen_succes;
                break;
            case PEN_STATE_OUT:
            case PEN_STATE_BREAK:
            case PEN_STATE_OFFLINE:
            case PEN_STATE_TIMEOUT:
            default:
//                rid = R.mipmap.pen_break;
                ibPen.setActivated(false);
                break;

        }
        ibPen.setBackgroundResource(rid);
    }

    private void clearState() {
        if (ibPen != null) {
            ibPen.clearAnimation();
            ibPen.setText("");
//            ibPen.setBackgroundResource(0);
        }
        animation.cancel();
    }

    @Override
    public void onClick(View v) {
        if (penState == PenState.PEN_STATE_BREAK) {//断开，连接
            setPenState(PenState.PEN_STATE_SCAN);
        } else if (penState == PenState.PEN_STATE_CONNECTED) {//以链接 ， 电量
            setPenState(PenState.PEN_STATE_PER);
        } else if (penState == PenState.PEN_STATE_OFFLINE) {//离线
            setPenState(PenState.PEN_STATE_RETRY);
        } else if (penState == PenState.PEN_STATE_LOADING || penState == PenState.PEN_STATE_SCAN || penState == PenState.PEN_STATE_RETRY  ) {//ing
            clearState();
            setPenState(PenState.PEN_STATE_OUT);
        }else {
            return;
        }
    }

    public void setOnPenClickListener(OnPenClickListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (getPenState().equals(PenState.PEN_STATE_CONNECTED)) {
            setPenState(PenState.PEN_STATE_OUT);
        }
        return true;
    }

    /**
     * 空间点击事件
     */
    public interface OnPenClickListener {
        void onStateChanged(PenState state);
    }

    public enum PenState {
        //断开状态
        PEN_STATE_BREAK(PenActionProvider.PEN_STATE_BREAK),
        //连接状态
        PEN_STATE_CONNECTED(PenActionProvider.PEN_STATE_CONNECTED),
        //展示点亮状态
        PEN_STATE_PER(PenActionProvider.PEN_STATE_PER),
        //正在连接状态
        PEN_STATE_LOADING(PenActionProvider.PEN_STATE_LOADING),
        //离线
        PEN_STATE_OFFLINE(PenActionProvider.PEN_STATE_OFFLINE),
        //正在重试
        PEN_STATE_RETRY(PenActionProvider.PEN_STATE_RETRY),
        //扫描设备
        PEN_STATE_SCAN(PenActionProvider.PEN_STATE_SCAN),
        //注销设备
        PEN_STATE_OUT(PenActionProvider.PEN_STATE_OUT),
        //连接超时
        PEN_STATE_TIMEOUT(PenActionProvider.PEN_STATE_TIMEOUT);

        private int state;

        PenState(int state) {
            this.state = state;
        }
    }
}
