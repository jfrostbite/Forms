package com.e_eduspace.identify.ThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017-06-26.
 * 线程池工具
 */

public class ThreadPool {

    private final ExecutorService mCachedThreadPool;
    private final ExecutorService mSingleThreadPool;
    private final ExecutorService mFixedThreadPool;

    private ThreadPool(){
        mCachedThreadPool = Executors.newCachedThreadPool();
        mSingleThreadPool = Executors.newSingleThreadExecutor();
        mFixedThreadPool = Executors.newFixedThreadPool(5);
    }

    private interface ThreadHolder {
        ThreadPool HOLDER = new ThreadPool();
    }

    public static ThreadPool newInstance() {
        return ThreadHolder.HOLDER;
    }

    public void submitSingle(Runnable runnable){
        mSingleThreadPool.submit(runnable);
    }

    public void submitCache(Runnable runnable){
        mCachedThreadPool.submit(runnable);
    }
    public void submitSingle(Callable callable){
        mSingleThreadPool.submit(callable);
    }

    public void submitCache(Callable callable){
        mCachedThreadPool.submit(callable);
    }
    public void submitFix(Runnable runnable){
        mFixedThreadPool.submit(runnable);
    }

    public void submitFix(Callable callable) {
        mFixedThreadPool.submit(callable);
    }

    public void shutdownFix(){
        mFixedThreadPool.shutdown();
    }
    public void shutdownSingle(){
        mSingleThreadPool.shutdown();
    }

    public void shutdownCache(){
        mCachedThreadPool.shutdown();
    }
}
