package com.e_eduspace.identify.entity;

import android.text.TextUtils;

import com.myscript.atk.core.CaptureInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-06-08.
 * 区域内行、具体到识别的每句话
 */

public class LineBean {
    //行名称
    private String mLineName;
    //行值
    private String mLineValue;
    //行类型
    private int mLineType;
    //行坐标
    private float mLeft,mTop,mRight,mBottom;
    //行
    private List<StrokeEntity> mStrokes = new ArrayList<>();

    public String getLineName() {
        return mLineName;
    }

    public LineBean setLineName(String lineName) {
        mLineName = lineName;
        return this;
    }

    public String getLineValue() {
        return mLineValue;
    }

    public LineBean setLineValue(String lineValue) {
//        lineValue = lineValue.trim().replaceAll(" ", "");
        mLineValue = lineValue;
        return this;
    }

    public List<StrokeEntity> getStrokes() {
        return mStrokes;
    }

    public LineBean setStrokes(List<StrokeEntity> strokes) {
        mStrokes = strokes;
        return this;
    }

    public LineBean addStrokes(StrokeEntity strokes) {
        mStrokes.add(strokes);
        return this;
    }

    public int getLineType() {
        return mLineType;
    }

    public LineBean setLineType(int lineType) {
        mLineType = lineType;
        return this;
    }

    public float getLeft() {
        return mLeft;
    }

    public LineBean setLeft(float left) {
        mLeft = left;
        return this;
    }

    public float getTop() {
        return mTop;
    }

    public LineBean setTop(float top) {
        mTop = top;
        return this;
    }

    public float getRight() {
        return mRight;
    }

    public LineBean setRight(float right) {
        mRight = right;
        return this;
    }

    public float getBottom() {
        return mBottom;
    }

    public LineBean setBottom(float bottom) {
        mBottom = bottom;
        return this;
    }

    public boolean isNormal() {
        return !(mStrokes != null && !mStrokes.isEmpty()) || !TextUtils.isEmpty(mLineValue);
    }

    public List<List<CaptureInfo>> getCaptrueInfos(){
        List<List<CaptureInfo>> linesInfos = new ArrayList<>();
        if (mStrokes != null) {
            for (StrokeEntity stroke : mStrokes) {
                List<CaptureInfo> captureInfos = new ArrayList<>();
                List<? extends PointEntity> notePointList = stroke.getPointList();
                if (notePointList != null) {
                    for (PointEntity point : notePointList) {
                        CaptureInfo captureInfo = new CaptureInfo();
                        captureInfo.setX(point.getPX());
                        captureInfo.setY(point.getPY());
                        captureInfo.setF(point.getPress());
                        captureInfos.add(captureInfo);
                    }
                    linesInfos.add(captureInfos);
                }
            }
        }
        return linesInfos;
    }
}
