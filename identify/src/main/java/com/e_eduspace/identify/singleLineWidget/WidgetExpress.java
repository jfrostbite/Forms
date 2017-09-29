package com.e_eduspace.identify.singleLineWidget;

import android.support.annotation.IdRes;
import android.view.ViewDebug;

import com.myscript.atk.core.CaptureInfo;

import java.util.List;


/**
 * 空间扩展接口
 */
public interface WidgetExpress {

    /**
     * 识别类型
     */
    int IDENTIFY_TYPE_TEXT = 0;
    int IDENTIFY_TYPE_DIGIT = 1;
    int IDENTIFY_TYPE_DIGIT_SPACE = 2;
    int IDENTIFY_TYPE_EN = 3;
    int IDENTIFY_TYPE_EN_SPACE = 4;
    int IDENTIFY_TYPE_EMAIL = 5;
    int IDENTIFY_TYPE_ID = 6;
    int IDENTIFY_TYPE_U_D = 7;
    int IDENTIFY_TYPE_U_L_D = 8;
    int IDENTIFY_TYPE_Z_D = 9;//汉字数字
    int IDENTIFY_TYPE_A_B = 10;//汉字数字

    /**
     * 获取传入的点
     * @return
     */
    List<List<CaptureInfo>> getStroke();

    void addDatas(List<CaptureInfo> datas);

    /**
     * 重新尝试识别
     */
    void retry();

    /**
     * 设置文本变化监听器
     */
    void setOnTextChangedListener(OnTextChangedRetryListener listener);

    /**
     * 设置是否需要尝试重试
     * @param count 重试次数
     * @param expect 重试期望
     */
    void setRetry(int count, int expect, Integer... types);

//    void addStroke(List<CaptureInfo> points, boolean finish);
    void configure(int type);

    /**
     * 设置识别状态
     */
    void setDentifying(boolean dentifying);

    @ViewDebug.ExportedProperty
    Object getTag();

    Object getTag(int key);

    @IdRes
    @ViewDebug.CapturedViewProperty
    int getId();

    /**
     * 设置进度
     * @param progress
     */
    void setProgress(float progress);

    float getProgress();

    void setComplete();

    boolean completed();
}
