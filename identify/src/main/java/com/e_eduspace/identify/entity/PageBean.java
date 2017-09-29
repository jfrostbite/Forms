package com.e_eduspace.identify.entity;

import java.util.List;

/**
 * Created by Administrator on 2017-06-15.
 * 数据识别结果
 * 页数索引从1开始
 */

public class PageBean {
    private int mPageIndex;
    private List<AreaBean> mAreaList;
    private List<CheckBean> mCheckList;

    public int getPageIndex() {
        return mPageIndex;
    }

    public PageBean setPageIndex(int pageIndex) {
        mPageIndex = pageIndex;
        return this;
    }

    public List<AreaBean> getAreaList() {
        return mAreaList;
    }

    public PageBean setAreaList(List<AreaBean> areaList) {
        mAreaList = areaList;
        return this;
    }

    public List<CheckBean> getCheckList() {
        return mCheckList;
    }

    public PageBean setCheckList(List<CheckBean> checkList) {
        mCheckList = checkList;
        return this;
    }
}
