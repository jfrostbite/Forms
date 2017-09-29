package com.e_eduspace.identify.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.e_eduspace.identify.Constants;
import com.e_eduspace.identify.entity.PointAreaEntity;
import com.e_eduspace.identify.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-05-25.
 * 区域点数据库实现
 */

public class PointRangeDB extends DBManager {

    private interface DBSINGLE {
        DBManager DB_INSTANCE = new PointRangeDB();
    }

    public static DBManager newInstance() {
        return DBSINGLE.DB_INSTANCE;
    }

    @Override
    public boolean insert(boolean recog, String... values) {
        if (values.length / 2 != 0) {
            throw new RuntimeException("参数错误，参数必须为k-v键值对");
        }
        check();
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i += 2) {
            cv.put(values[i], values[i + 1]);
        }
        long insert = mSQLiteDatabase.insert(recog ? Constants.POINT_RANGE_IDENTIFY_TAB_NAME : Constants.POINT_RANGE_TAB_NAME, null, cv);
        return insert > -1;
    }

    @Override
    public void insert(boolean recog, PointAreaEntity pointEntity) {
        check();
        ContentValues values = new ContentValues();
        values.put(Constants.POINT_RANGE_TAG, pointEntity.getTag());
        values.put(Constants.POINT_RANGE_LOC, pointEntity.getLoc());
        values.put(Constants.POINT_RANGE_MINX, pointEntity.getMinX());
        values.put(Constants.POINT_RANGE_MAXX, pointEntity.getMaxX());
        values.put(Constants.POINT_RANGE_MINY, pointEntity.getMinY());
        values.put(Constants.POINT_RANGE_MAXY, pointEntity.getMaxY());
        values.put(Constants.POINT_RANGE_PAGE, pointEntity.getPageIndex());
        long insert = mSQLiteDatabase.insert(recog ? Constants.POINT_RANGE_IDENTIFY_TAB_NAME : Constants.POINT_RANGE_TAB_NAME, null, values);
        LogUtils.e(insert);
    }

    @Override
    public boolean delete(boolean recog, String id) {
        check();
        int delete = mSQLiteDatabase.delete(recog ? Constants.POINT_RANGE_IDENTIFY_TAB_NAME : Constants.POINT_RANGE_TAB_NAME, Constants.POINT_RANGE_ID + "=?", new String[]{id});
        return delete > 0;
    }

    @Override
    public boolean delete(boolean recog) {
        check();
        int delete = mSQLiteDatabase.delete(recog ? Constants.POINT_RANGE_IDENTIFY_TAB_NAME : Constants.POINT_RANGE_TAB_NAME, null, null);
        return delete > 0;
    }

    @Override
    public boolean update(boolean recog, String id, String... values) {
        if (values.length / 2 != 0) {
            throw new RuntimeException("参数错误，参数必须为k-v键值对");
        }
        check();
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i += 2) {
            cv.put(values[i], values[i + 1]);
        }
        int update = mSQLiteDatabase.update(recog ? Constants.POINT_RANGE_IDENTIFY_TAB_NAME : Constants.POINT_RANGE_TAB_NAME, cv, Constants.POINT_RANGE_ID + "=?", new String[]{id});
        return update > 0;
    }

    @Override
    public List<PointAreaEntity> query(boolean recog) {
        check();
        ArrayList<PointAreaEntity> paes = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query(recog ? Constants.POINT_RANGE_IDENTIFY_TAB_NAME : Constants.POINT_RANGE_TAB_NAME, new String[]{}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Constants.POINT_RANGE_ID));
            String tag = cursor.getString(cursor.getColumnIndex(Constants.POINT_RANGE_TAG));
            String loc = cursor.getString(cursor.getColumnIndex(Constants.POINT_RANGE_LOC));
            int page = cursor.getInt(cursor.getColumnIndex(Constants.POINT_RANGE_PAGE));
            float minX = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MINX));
            float minY = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MINY));
            float maxX = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MAXX));
            float maxY = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MAXY));
            if (TextUtils.isEmpty(tag)) {
                continue;
            }
            PointAreaEntity pae = new PointAreaEntity().setId(id)
                    .setTag(tag)
                    .setLoc(loc)
                    .setPageIndex(page)
                    .setMinX(minX)
                    .setMinY(minY)
                    .setMaxX(maxX)
                    .setMaxY(maxY);
            paes.add(pae);
        }
        cursor.close();
        return paes;
    }

    /**
     * 根据参数 判断点xy 条件参数，判断该点是否在已知范围内。
     *
     * @param x
     * @param y
     * @param values
     * @return
     */
    @Override
    public float[] query(boolean recog, float x, float y, String... values) {
        float[] res = new float[0];
        try {
            check();
            res = null;
            Cursor cursor = null;
            cursor = mSQLiteDatabase.query(recog ? Constants.POINT_RANGE_IDENTIFY_TAB_NAME : Constants.POINT_RANGE_TAB_NAME, new String[]{}, Constants.POINT_RANGE_TAG + "=? and " + Constants.POINT_RANGE_LOC + "=? and " + Constants.POINT_RANGE_PAGE + "=?", values, null, null, null);
            if (cursor.moveToNext()) {
                float minX = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MINX));
                float minY = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MINY));
                float maxX = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MAXX));
                float maxY = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MAXY));
                cursor.close();
                boolean matching = x > minX && x < maxX && y > minY && y < maxY;
                if (matching) {
                    res = new float[4];
                    res[0] = minX;
                    res[1] = minY;
                    res[2] = maxX;
                    res[3] = maxY;
                } else {
                    res = null;
                }
            } else {
                res = null;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return res;
    }
}
