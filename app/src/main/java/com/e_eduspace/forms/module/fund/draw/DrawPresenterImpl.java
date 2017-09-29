package com.e_eduspace.forms.module.fund.draw;

import android.graphics.Color;

import com.e_eduspace.forms.base.BasePresenter;
import com.e_eduspace.forms.coolpen.CoolPenManger;
import com.e_eduspace.forms.model.db.DBManger;
import com.e_eduspace.forms.model.entity.FormPoint;
import com.e_eduspace.forms.model.entity.FormStroke;
import com.newchinese.coolpensdk.entity.NotePoint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017-05-26.
 * 绘制逻辑
 */

public class DrawPresenterImpl extends BasePresenter<DrawIView> implements DrawIPresneter {

    private ExecutorService mExecutor;
    private List<NotePoint> mPoints = new ArrayList<>();
    private Future<String> mFuture;

    public DrawPresenterImpl() {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * 数据库取点绘制逻辑
     * 可根据页数获取当前页面数据库数据
     * <p>
     * 利用线程池 防止瞎逼操作，导致SDK崩溃。
     *
     * @param position
     */
    @Override
    public void onDrawDB(final int position) {
        if (mFuture != null) {
            if (!mFuture.isDone()) {
                mFuture.cancel(true);
            }
        }

        if (mIView != null) {
            mIView.clean();
            mIView.showLoading("正在加载数据...");
        }
        mFuture = mExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                List<FormPoint> list = DBManger.newInstance().query(position);
                if (mIView != null) {
                    mIView.drawStroke(list);
                    mIView.showLoading(null);
                }
                return null;
            }
        });
    }

    @Override
    public int getPageIndex(NotePoint point) {
        return CoolPenManger.newInstance().getPageIndex(point);
    }

    @Override
    public void onPenDown(NotePoint point) {
        mPoints.clear();
        mPoints.add(point);
        mIView.drawPoint(point);
    }

    @Override
    public void onPenUp(NotePoint point) {
        FormStroke stroke = new FormStroke();
        stroke.setStrokeColor(Color.BLACK);
        stroke.setPageIndex(point.getPageIndex());
        stroke.setId(null);
        //点序列化
        serialize(stroke, mPoints);
    }


    @Override
    public void onPenDrawLine(NotePoint point) {
        mPoints.add(point);
        mIView.drawPoint(point);
    }

    @Override
    public void serialize(FormStroke stroke, List<NotePoint> points) {
        if (points != null && !points.isEmpty()) {
            ArrayList<NotePoint> point = new ArrayList<>(points);
            mPoints.clear();
            DBManger.newInstance().insert(stroke, point);
        }
    }

    @Override
    public void checkPoints() {
        if (mPoints != null && !mPoints.isEmpty()) {
            FormStroke stroke = new FormStroke();
            stroke.setStrokeColor(Color.BLACK);
            stroke.setPageIndex(mPoints.get(0).getPageIndex());
            stroke.setId(null);
            //点序列化
            serialize(stroke, mPoints);
        }
    }

    @Override
    public void clear(int pageIndex) {
        super.clear(pageIndex);
        mIView.getDrawBoard().clearCanvars();
    }
}
