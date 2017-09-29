package com.e_eduspace.identify.entity;

/**
 * Created by Administrator on 2017-06-20.
 * 勾选类别
 */

public class CheckBean {
    private String mCheckName;
    private int mCheckValue = -1;
    private int mCheckLength;

    public int getCheckLength() {
        return mCheckLength;
    }

    public CheckBean setCheckLength(int checkLength) {
        mCheckLength = checkLength;
        return this;
    }

    //行坐标
    private float mLeft,mTop,mRight,mBottom;

    public String getCheckName() {
        return mCheckName;
    }

    public CheckBean setCheckName(String checkName) {
        mCheckName = checkName;
        return this;
    }

    public int getCheckValue() {
        return mCheckValue;
    }

    public CheckBean setCheckValue(int checkValue) {
        mCheckValue = checkValue;
        return this;
    }

    public float getLeft() {
        return mLeft;
    }

    public CheckBean setLeft(float left) {
        mLeft = left;
        return this;
    }

    public float getTop() {
        return mTop;
    }

    public CheckBean setTop(float top) {
        mTop = top;
        return this;
    }

    public float getRight() {
        return mRight;
    }

    public CheckBean setRight(float right) {
        mRight = right;
        return this;
    }

    public float getBottom() {
        return mBottom;
    }

    public CheckBean setBottom(float bottom) {
        mBottom = bottom;
        return this;
    }
}
