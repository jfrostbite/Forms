package com.e_eduspace.identify;

/**
 * Created by Administrator on 2017-05-25.
 */

public class Constants {
    //范围点数据库名字
    public static final String POINT_DB_NAME = "POINT_DB.db";
    public static final String POINT_RANGE_IDENTIFY_TAB_NAME = "POINT_RANGE_IDENTIFY";//参与识别点范围
    public static final String POINT_RANGE_TAB_NAME = "POINT_RANGE_";//非参与识别点范围
    //数据库版本号
    public static final int POINT_DB_VER = 4;
    //数据库字段
    public static final String POINT_RANGE_ID = "_id";
    public static final String POINT_RANGE_TAG = "tag";
    public static final String POINT_RANGE_MINX = "minX";
    public static final String POINT_RANGE_MINY = "minY";
    public static final String POINT_RANGE_MAXX = "maxX";
    public static final String POINT_RANGE_MAXY = "maxY";
    public static final String POINT_RANGE_LOC = "loc";
    public static final String POINT_RANGE_PAGE = "page";

    //创建POINT_RANGE 表
    public static final String[] CREATE_TAB_RANGES = {"create table '" + POINT_RANGE_IDENTIFY_TAB_NAME + "' ('"
            + POINT_RANGE_ID + "' integer primary key autoincrement unique, '"
            + POINT_RANGE_TAG + "' text, '"
            + POINT_RANGE_MINX + "' text, '"
            + POINT_RANGE_MINY + "' text, '"
            + POINT_RANGE_MAXX + "' text, '"
            + POINT_RANGE_MAXY + "' text, '"
            + POINT_RANGE_LOC + "' integer, '"
            + POINT_RANGE_PAGE + "' integer);"
    ,"create table '" + POINT_RANGE_TAB_NAME + "' ('"
            + POINT_RANGE_ID + "' integer primary key autoincrement unique, '"
            + POINT_RANGE_TAG + "' text, '"
            + POINT_RANGE_MINX + "' text, '"
            + POINT_RANGE_MINY + "' text, '"
            + POINT_RANGE_MAXX + "' text, '"
            + POINT_RANGE_MAXY + "' text, '"
            + POINT_RANGE_LOC + "' integer, '"
            + POINT_RANGE_PAGE + "' integer);"};
    public static final String[] UPDATE_TAB_RANGES = {"create table '" + POINT_RANGE_IDENTIFY_TAB_NAME + "' ('"
            + POINT_RANGE_ID + "' integer primary key autoincrement unique, '"
            + POINT_RANGE_TAG + "' text, '"
            + POINT_RANGE_MINX + "' text, '"
            + POINT_RANGE_MINY + "' text, '"
            + POINT_RANGE_MAXX + "' text, '"
            + POINT_RANGE_MAXY + "' text, '"
            + POINT_RANGE_LOC + "' integer, '"
            + POINT_RANGE_PAGE + "' integer);"
            ,"create table '" + POINT_RANGE_TAB_NAME + "' ('"
            + POINT_RANGE_ID + "' integer primary key autoincrement unique, '"
            + POINT_RANGE_TAG + "' text, '"
            + POINT_RANGE_MINX + "' text, '"
            + POINT_RANGE_MINY + "' text, '"
            + POINT_RANGE_MAXX + "' text, '"
            + POINT_RANGE_MAXY + "' text, '"
            + POINT_RANGE_LOC + "' integer, '"
            + POINT_RANGE_PAGE + "' integer);"};
}
