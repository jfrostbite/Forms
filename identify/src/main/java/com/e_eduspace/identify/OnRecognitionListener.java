package com.e_eduspace.identify;

/**
 * Created by Administrator on 2017-06-20.
 * 识别监听器
 */

public interface OnRecognitionListener<T> {

    /**
     * 开始
     */
    void onBegined(int page);

    /**
     * 识别中
     */
    void onLoading(int page, String msg, int progress);

    /**
     * 完成
     */
    void onFinished(T t);

    /**
     * 失败
     */
    void onFailure(int page, String err);

}
