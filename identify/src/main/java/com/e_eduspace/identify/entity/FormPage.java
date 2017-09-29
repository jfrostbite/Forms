package com.e_eduspace.identify.entity;

import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017-05-27.
 */

public class FormPage{

    public int page;
    public String selects;
    public int[] selectCounts;
    public float[] base;
    public List<Area> area;

    public static class Area {
        //Area中元素
        public String name;

        //elements中元素
        public String names;
        public int[] types;
    }

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }
}
