package com.e_eduspace.identify.ThreadPool;

/**
 * Created by Administrator on 2017-06-26.
 * 识别 线程 包装
 */

abstract class IDentifyTask<T> implements Runnable {

    private T mT;

    public void setApi(T t){
        if (t == null) {
            throw new NullPointerException("t is null");
        }
        mT = t;
    }

    @Override
    public void run() {
        run(mT);
    }

    public abstract void run(T t);
}
