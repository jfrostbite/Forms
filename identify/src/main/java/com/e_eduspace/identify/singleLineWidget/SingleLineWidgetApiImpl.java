package com.e_eduspace.identify.singleLineWidget;

import android.content.Context;
import android.util.AttributeSet;

import com.e_eduspace.identify.R;
import com.myscript.atk.core.CaptureInfo;
import com.myscript.atk.sltw.SingleLineWidget;
import com.myscript.atk.text.common.controller.TextController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017-05-05.
 */

public class SingleLineWidgetApiImpl extends SingleLineWidget implements WidgetExpress {

    //本次识别传入点集合
    private List<List<CaptureInfo>> mPoints = new ArrayList<>();
    //自定义识别回调
    private OnTextChangedRetryListener mListener;
    //由传入rtry参数判断是否需要重复识别
    private boolean mRetry;
    //重复识别次数
    private int mCount;
    //期望识别结果的预期效果，此处以识别文本长度为准
    private int mExpect;
    private int i;
    //重试识别字段类型，不是所有字符类型都需要重新识别
    private Integer[] mTypes;
    //手机点集合标记，重试识别是，不需要手机点坐标
    private boolean flag;
    //正在处理识别操作
    private boolean mDentifying;
    //识别进度
    private float mProgress = 1.0f;
    //识别完成，正常/非正常
    private boolean mCompletion;
    //一次性识别分割标记
    private final int IDENTIFY_SIGN = 30;

    public SingleLineWidgetApiImpl(Context context) {
        this(context,null);
    }

    public SingleLineWidgetApiImpl(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SingleLineWidgetApiImpl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addStroke(List<CaptureInfo> points, boolean last) {
        addDatas(points);
        if (mPoints.size() % IDENTIFY_SIGN == 0 || last) {
            super.clear();
            mDentifying = true;
            for (List<CaptureInfo> point : mPoints) {
                super.addStroke(point);
            }
            int i = 0;
            while (mDentifying && i < 30) {
                i++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    mDentifying = false;
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
        /*

        if(mPoints.size() < IDENTIFY_SIGN){
            super.addStroke(points);
            addDatas(points);
        } else if (mPoints.size() % IDENTIFY_SIGN == 0) {
            mDentifying = true;
            while (mDentifying) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    mDentifying = false;
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            super.clear();
//            super.addStroke(points);
            addDatas(points);
            for (List<CaptureInfo> point : mPoints) {
                super.addStroke(point);
            }
        } else if(mPoints.size() > IDENTIFY_SIGN){
            super.addStroke(points);
            addDatas(points);
        }*/
    }

    @Override
    public List<List<CaptureInfo>> getStroke() {
        return mPoints;
    }

    @Override
    public void addDatas(List<CaptureInfo> datas) {
        if (flag) {
            return;
        }
        mPoints.add(datas);
    }

    @Override
    public void retry() {
        if (!mDentifying) {
            return;
        }
        flag = true;
        super.clear();
        Executors.newFixedThreadPool(5).submit(new Callable(){
            @Override
            public Object call() throws Exception {
                for (List<CaptureInfo> mPoint : mPoints) {
                    SingleLineWidgetApiImpl.super.addStroke(mPoint);
                }
                flag = false;
                return null;
            }
        });
    }

    @Override
    public void setRetry(int count, int expect, Integer... types) {
        mCount = count;
        mExpect = expect;
        mTypes = types == null || types.length == 0 ? new Integer[]{0,1,2,3,4,5,6,7,8,9,10} : types;
    }

    @Override
    public void configure(int type) {
        switch (type) {
            case IDENTIFY_TYPE_DIGIT:
            case IDENTIFY_TYPE_DIGIT_SPACE:
                configure(getContext().getString(R.string.bundle_zh), getContext().getString(R.string.config_digit));
                break;
            case IDENTIFY_TYPE_EN:
            case IDENTIFY_TYPE_EN_SPACE:
                configure(getContext().getString(R.string.bundle_zh), getContext().getString(R.string.config_en_upcase));
                break;
            case IDENTIFY_TYPE_EMAIL:
                configure(getContext().getString(R.string.bundle_zh), getContext().getString(R.string.config_email));
                break;
            case IDENTIFY_TYPE_ID:
                configure(getContext().getString(R.string.bundle_zh), getContext().getString(R.string.config_id));
                break;
            case IDENTIFY_TYPE_U_D:
                configure(getContext().getString(R.string.bundle_zh), getContext().getString(R.string.config_ud));
                break;
            case IDENTIFY_TYPE_U_L_D:
                configure(getContext().getString(R.string.bundle_zh), getContext().getString(R.string.config_uld));
                break;
            case IDENTIFY_TYPE_Z_D:
                configure(getContext().getString(R.string.bundle_zh), getContext().getString(R.string.config_zd));
                break;
            case IDENTIFY_TYPE_A_B:
                configure(getContext().getString(R.string.bundle_zh), getContext().getString(R.string.config_ab));
                break;
            case IDENTIFY_TYPE_TEXT:
            default:
                configure(getContext().getString(R.string.bundle_zh), getContext().getString(R.string.config_text));
                break;
        }
        mRetry = mTypes != null && Arrays.asList(mTypes).contains(type) && mCount > 0 && mExpect > 0;
    }

    @Override
    public synchronized void onTextChanged(TextController ctrl, String text, boolean intermediate) {
        text = text.trim().replaceAll(" ", "");
        if (intermediate) {
            return;
        }
        if (mRetry && text.length() <= mExpect && i < mCount) {//满足重试条件
            i++;
            retry();
        } else {//正常流程
            //完成后重置标记，可控制导致的效率问题
            i =/* intermediate ? i :*/ 0;
            if(mListener != null && !this.isDisposed()) {
                mListener.onTextChanged(this,text,intermediate);
            }
            setDentifying(false);
        }
    }

    @Override
    public void clear() {
        super.clear();
        if (flag) {
            return;
        }
        if (mPoints != null) {
            mPoints.clear();
        }
    }

    @Override
    public void setOnTextChangedListener(OnTextChangedRetryListener listener) {

        mListener = listener;
    }

    public boolean isDentifying() {
        return mDentifying;
    }

    @Override
    public void setDentifying(boolean dentifying) {
        mDentifying = dentifying;
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }

    @Override
    public void setComplete() {
        mCompletion = true;
    }

    @Override
    public boolean completed() {
        return mCompletion;
    }
}
