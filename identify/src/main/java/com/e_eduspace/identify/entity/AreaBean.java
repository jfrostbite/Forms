package com.e_eduspace.identify.entity;

import java.util.List;

/**
 * Created by Administrator on 2017-06-08.
 * 范围点区域
 */

public class AreaBean {
    //区域名称
    private String mAreaName;
    //区域线
    private List<LineBean> mLines;

    public String getAreaName() {
        return mAreaName;
    }

    public AreaBean setAreaName(String areaName) {
        mAreaName = areaName;
        return this;
    }

    public List<LineBean> getLines() {
        return mLines;
    }

    public AreaBean setLines(List<LineBean> lines) {
        mLines = lines;
        return this;
    }
}
