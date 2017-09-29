package com.e_eduspace.forms.coolpen;

import android.content.Context;

import com.e_eduspace.forms.utils.LogUtils;
import com.newchinese.coolpensdk.entity.NotePoint;
import com.newchinese.coolpensdk.entity.NoteStroke;
import com.newchinese.coolpensdk.listener.OnPointListener;
import com.newchinese.coolpensdk.manager.DrawingboardAPI;

import static com.newchinese.coolpensdk.constants.PointType.TYPE_DOWN;
import static com.newchinese.coolpensdk.constants.PointType.TYPE_MOVE;
import static com.newchinese.coolpensdk.constants.PointType.TYPE_UP;

/**
 * Created by Administrator on 2017-05-27.
 * CoolPen Drawapi2次封装。
 */

public class CoolPenManger implements OnPointListener {

    private DrawingboardAPI mInstance;
    private CoolPenListener mListener;
    private boolean mInitialize;
    private boolean mOneUp;
    private CoolPenManger(){}

    @Override
    public void onStrokeCached(int i, NoteStroke noteStroke) {
    }

    @Override
    public void onPointCatched(int i, NotePoint notePoint) {
        if (mListener == null) {
            return;
        }
        if (TYPE_DOWN == notePoint.getPointType()) {
            mOneUp = false;
            mListener.onPenDown(notePoint);
        } else if (TYPE_MOVE == notePoint.getPointType()) {
            mListener.onPenDrawLine(notePoint);
        } else if (validUp(notePoint.getPointType())/*PointType.TYPE_UP == notePoint.getPointType()*/) {
            mListener.onPenUp(notePoint);
        }
    }
    /**
     * 抬起封装，，sdk bug ，错误抬起
     * @param pointType
     * @return
     */
    private boolean validUp(Integer pointType) {
        boolean res;
        if (mOneUp) {
            res = false;
        } else {
            res = true;
            mOneUp = true;
        }
        return pointType == TYPE_UP && res;
    }

    @Override
    public void onPageIndexChanged(int formType, NotePoint notePoint) {
        LogUtils.e("第"+notePoint.getPageIndex()+"页");
        if (mListener != null) {
            mListener.onPageChanged(notePoint.getPageIndex());
        }
    }

    private interface CoolPenSingle {
        CoolPenManger COOL_PEN_MANGER = new CoolPenManger();
    }

    public static CoolPenManger newInstance(){
        return CoolPenSingle.COOL_PEN_MANGER;
    }

    public CoolPenManger init(Context context){
        if (mInitialize) {
            return this;
        }
        mInstance = DrawingboardAPI.getInstance();
        mInstance.init(context,null);
        mInitialize = true;
        return this;
    }

    public CoolPenManger setBookSize(float width, float heght) {
        if (mInitialize) {
            mInstance.setBookSize(width,heght);
        }
        return this;
    }

    public CoolPenManger setAPIOffset(float x, float y){
        if (mInitialize) {
            mInstance.setBaseOffset(x,y);
        }
        return this;
    }

    public CoolPenManger setDrawAPI(DrawingboardAPI api){
        mInstance = api;
        mInitialize = true;
        return this;
    }

    public void setOnPointChangedListener(CoolPenListener listener) {
        if (mInitialize) {
            mListener = listener;
            mInstance.setOnPointListener(this);
        } else {
            LogUtils.e("CoolPenSDK 未初始化");
        }
    }

    public int getPageIndex(NotePoint point) {
        return mInitialize ? mInstance.getPageIndex(point) : -1;
    }

    public boolean isCurrentPage(NotePoint point) {
        return mInitialize && mInstance.isSameNotePage(point);
    }
}
