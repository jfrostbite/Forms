package com.e_eduspace.forms.module.fund.form;

import com.e_eduspace.forms.base.BasePresenter;
import com.e_eduspace.forms.model.constants.Constant;
import com.e_eduspace.forms.model.db.DBManger;
import com.e_eduspace.forms.model.db.OnLoadDBListener;
import com.e_eduspace.forms.utils.KUtils;
import com.e_eduspace.identify.BuilderPort;
import com.e_eduspace.identify.IDentify;
import com.e_eduspace.identify.IDentifyMulti;
import com.e_eduspace.identify.IDentifySingle;
import com.e_eduspace.identify.OnRecognitionListener;
import com.e_eduspace.identify.entity.FormPage;
import com.e_eduspace.identify.entity.PageBean;
import com.e_eduspace.identify.entity.StrokeEntity;
import com.e_eduspace.identify.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.newchinese.coolpensdk.entity.NotePoint;

import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-06-16.
 * 电子表格逻辑处理器
 */

public class FormPresenterImpl extends BasePresenter<FormIView<PageBean>> implements FormIPresenter<FormIView<PageBean>>, OnRecognitionListener<PageBean> {

    private BuilderPort mBuilder;
    private ExecutorService mExecutor;
    private boolean mFast;

    public FormPresenterImpl() {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void attach(FormIView<PageBean> iView) {
        super.attach(iView);
        mBuilder = mFast ? new IDentifyMulti.Builder(KUtils.getApp(), Constant.BYTES, true) : new IDentifySingle.Builder(KUtils.getApp(), Constant.BYTES, true);
    }

    @Override
    public void discern(final int page) {
        Observable.create(new ObservableOnSubscribe<List<FormPage>>() {
            @Override
            public void subscribe(ObservableEmitter<List<FormPage>> e) throws Exception {
                Gson gson = new GsonBuilder().serializeNulls().create();
                List<FormPage> formpage = gson.fromJson(new InputStreamReader(KUtils.getApp().getAssets().open("formpage")), new TypeToken<List<FormPage>>() {
                }.getType());
                e.onNext(formpage);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Function<List<FormPage>, IDentify>() {
            @Override
            public IDentify apply(List<FormPage> formpage) throws Exception {
                return mBuilder.build().initialize(formpage.get(page - 1));
            }
        }).map(new Function<IDentify, IDentify>() {
            @Override
            public IDentify apply(IDentify iDentify) throws Exception {
                return iDentify.setOnRecognitionListener(FormPresenterImpl.this);
            }
        }).subscribe(new Consumer<IDentify>() {
            @Override
            public void accept(final IDentify iDentify) throws Exception {
                DBManger.newInstance().queryStroke(page, new OnLoadDBListener() {

                    @Override
                    public void onLoadDB(final List<? extends StrokeEntity> strokes) {
                        iDentify.convert(strokes);
                    }

                    @Override
                    public void onLoadDB(NotePoint point) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
            }
        });
    }

    @Override
    public void onBegined(int page) {
        if (mIView != null && mIView.showing(page)) {
            KUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mIView.showLoading("开始分配...");
                }
            });
        }
    }

    @Override
    public void onLoading(int page, final String msg, final int progress) {
        if (mIView != null && mIView.getClass().isAssignableFrom(FormActivity.class) && mIView.showing(page)) {
            KUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mIView.showLoading(msg + "已完成(" + progress + "/100)");
                }
            });
        }
    }

    @Override
    public void onFinished(final PageBean pageBean) {
        LogUtils.e("识别完毕！");
        if (mIView != null && mIView.showing(pageBean.getPageIndex())) {
            KUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mIView.showLoading(null);
                }
            });
        }
        if (mIView != null) {
            KUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mIView.updateView(pageBean);
                }
            });
        }
    }

    @Override
    public void onFailure(int page, String err) {

    }

    @Override
    public void detach() {
        if (mBuilder != null) {
            mBuilder.closeDB();
        }
    }

    public FormIPresenter setFast(boolean fast) {
        mFast = fast;
        return this;
    }
}
