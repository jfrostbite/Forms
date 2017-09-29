package com.e_eduspace.forms.model.entity;


import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017-06-29.
 * 截图界面信息
 */

public class SnapInfo implements Serializable{

    private String mSnapInfo, mBottomInfo;
    private int page;
    private List<File> mImgPath;
    private int mfg;

    public String getSnapInfo() {
        return mSnapInfo;
    }

    public SnapInfo setSnapInfo(String snapInfo) {
        mSnapInfo = snapInfo;
        return this;
    }

    public String getBottomInfo() {
        return mBottomInfo;
    }

    public SnapInfo setBottomInfo(String bottomInfo) {
        mBottomInfo = bottomInfo;
        return this;
    }

    public List<File> getImgPath() {
        return mImgPath;
    }

    public SnapInfo setImgPath(List<File> imgPath) {
        mImgPath = imgPath;
        return this;
    }

    public int getPage() {
        return page;
    }

    public SnapInfo setPage(int page) {
        this.page = page;
        return this;
    }

    public int getFg() {
        return mfg;
    }

    public SnapInfo setFg(int fg) {
        this.mfg = fg;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }
}
