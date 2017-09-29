package com.e_eduspace.identify.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.e_eduspace.identify.Constants;
import com.e_eduspace.identify.entity.PointAreaEntity;

import java.util.List;

/**
 * Created by Administrator on 2017-05-25.
 */

public abstract class DBManager {

    SQLiteDatabase mSQLiteDatabase;
    boolean mOpen_write;

    public DBManager init(Context context){
        init(context,false);
        return this;
    }

    public DBManager init(Context context, boolean open){
        if (!open) {
            mSQLiteDatabase = new DBHelper(context).getWritableDatabase();
        } else {
            mSQLiteDatabase = SQLiteDatabase.openDatabase(context.getDatabasePath(Constants.POINT_DB_NAME).getPath(), null, mOpen_write ? SQLiteDatabase.OPEN_READWRITE : SQLiteDatabase.OPEN_READONLY);
        }
        return this;
    }

    void check() {
        if (mSQLiteDatabase == null) {
            throw new RuntimeException("数据库需要初始化。");
        }
    }

    public void closeDB(){
        if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen()) {
            mSQLiteDatabase.close();
            mSQLiteDatabase = null;
        }
    }

    public void openWrite(){
        mOpen_write = true;
    }

    public abstract boolean insert(boolean recog, String... values);
    public abstract boolean delete(boolean recog, String id);
    public abstract boolean delete(boolean recog);
    public abstract boolean update(boolean recog, String id,String... values);
    public abstract float[] query(boolean recog, float x, float y, String... values);
    public abstract List<PointAreaEntity> query(boolean recog);
    public abstract void insert(boolean recog, PointAreaEntity pointEntity);
}
