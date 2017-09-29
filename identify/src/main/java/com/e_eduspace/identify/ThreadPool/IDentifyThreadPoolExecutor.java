package com.e_eduspace.identify.ThreadPool;


import com.e_eduspace.identify.singleLineWidget.SingleLineWidgetApiImpl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017-06-26.
 */

public class IDentifyThreadPoolExecutor extends ThreadPoolExecutor {

    private SingleLineWidgetApiImpl[] mSingleLineWidgetApis;

    public IDentifyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public void initialize(SingleLineWidgetApiImpl... singleLineWidgetApis){

        mSingleLineWidgetApis = singleLineWidgetApis;
    }

    public void execute(IDentifyTask<SingleLineWidgetApiImpl> command) {
        command.setApi(getFreeWidget());
        super.execute(command);
    }

    private SingleLineWidgetApiImpl getFreeWidget(){
        if (mSingleLineWidgetApis != null) {
            while (true) {
                for (SingleLineWidgetApiImpl api : mSingleLineWidgetApis) {
                    if (api.completed()) {
                        return api;
                    }
                }
            }
        }
        return null;
    }
}
