package com.e_eduspace.identify.entity;

import com.google.gson.GsonBuilder;

/**
 * Created by Administrator on 2017-05-25.
 * 存储点集合
 */

public class PointAreaEntity {
    private String tag,loc;
    private float minX,minY,maxX,maxY;
    private int id,pageIndex;

    public String getTag() {
        return tag;
    }

    public PointAreaEntity setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getLoc() {
        return loc;
    }

    public PointAreaEntity setLoc(String loc) {
        this.loc = loc;
        return this;
    }

    public float getMinX() {
        return minX;
    }

    public PointAreaEntity setMinX(float minX) {
        this.minX = minX;
        return this;
    }

    public float getMinY() {
        return minY;
    }

    public PointAreaEntity setMinY(float minY) {
        this.minY = minY;
        return this;
    }

    public float getMaxX() {
        return maxX;
    }

    public PointAreaEntity setMaxX(float maxX) {
        this.maxX = maxX;
        return this;
    }

    public float getMaxY() {
        return maxY;
    }

    public PointAreaEntity setMaxY(float maxY) {
        this.maxY = maxY;
        return this;
    }

    public int getId() {
        return id;
    }

    public PointAreaEntity setId(int id) {
        this.id = id;
        return this;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public PointAreaEntity setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }
}
