package com.e_eduspace.forms.model.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IdRes;

import com.google.gson.GsonBuilder;

/**
 * Created by Administrator on 2017-07-04.
 */

public class SudokuInfo implements Parcelable {
    private String mName,mFunction;
    private @IdRes int mIconRid;
    private @IdRes int mPageImg[];
    private int mPageBegin;
    private boolean mPenTo;
    private int mPageIndex;

    public String getName() {
        return mName;
    }

    public SudokuInfo setName(String name) {
        mName = name;
        return this;
    }

    public String getFunction() {
        return mFunction;
    }

    public SudokuInfo setFunction(String function) {
        mFunction = function;
        return this;
    }

    public int getIconRid() {
        return mIconRid;
    }

    public SudokuInfo setIconRid(int iconRid) {
        mIconRid = iconRid;
        return this;
    }

    public int[] getPageImg() {
        return mPageImg;
    }

    public SudokuInfo setPageImg(int[] pageImg) {
        mPageImg = pageImg;
        return this;
    }

    public int getPageBegin() {
        return mPageBegin;
    }

    public SudokuInfo setPageBegin(int pageBegin) {
        mPageBegin = pageBegin;
        return this;
    }

    public boolean isPenTo() {
        return mPenTo;
    }

    public SudokuInfo setPenTo(boolean penTo) {
        mPenTo = penTo;
        return this;
    }

    public int getPageIndex() {
        return mPageIndex;
    }

    public SudokuInfo setPageIndex(int pageIndex) {
        mPageIndex = pageIndex - mPageBegin;
        return this;
    }

    @Override
    public String toString(){
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }


    public SudokuInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mFunction);
        dest.writeInt(this.mIconRid);
        dest.writeIntArray(this.mPageImg);
        dest.writeInt(this.mPageBegin);
        dest.writeByte(this.mPenTo ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mPageIndex);
    }

    protected SudokuInfo(Parcel in) {
        this.mName = in.readString();
        this.mFunction = in.readString();
        this.mIconRid = in.readInt();
        this.mPageImg = in.createIntArray();
        this.mPageBegin = in.readInt();
        this.mPenTo = in.readByte() != 0;
        this.mPageIndex = in.readInt();
    }

    public static final Creator<SudokuInfo> CREATOR = new Creator<SudokuInfo>() {
        @Override
        public SudokuInfo createFromParcel(Parcel source) {
            return new SudokuInfo(source);
        }

        @Override
        public SudokuInfo[] newArray(int size) {
            return new SudokuInfo[size];
        }
    };
}
