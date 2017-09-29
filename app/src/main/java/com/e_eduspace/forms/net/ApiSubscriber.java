package com.e_eduspace.forms.net;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2017-07-19.
 */

public abstract class ApiSubscriber<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        onResult(true, t);
    }

    @Override
    public void onError(Throwable t) {
        String msg = t.getMessage();
        if (t.getClass().isAssignableFrom(HttpException.class)) {
            HttpException e = (HttpException) t;
            switch (e.code()) {
                case 404:
                case 502:
                    msg = "服务器异常";
                    break;
                case 504:
                    msg = "网络异常";
                    break;
            }
        }
        onResult(false, msg);
    }

    protected abstract void onResult(boolean isSucc, Object t);
}
