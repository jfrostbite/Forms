package com.e_eduspace.identify.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.e_eduspace.identify.Constants;


/**
 * Created by Administrator on 2017-05-25.
 */

class DBHelper extends SQLiteOpenHelper {
    private Context mContext;

    DBHelper(Context context) {
        super(context, Constants.POINT_DB_NAME, null, Constants.POINT_DB_VER);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : Constants.CREATE_TAB_RANGES) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*for (String sql : Constants.UPDATE_TAB_RANGES) {
            db.execSQL(sql);
        }*/
        db.execSQL("alter table POINT_RANGE_IDENTIFY add "+ Constants.POINT_RANGE_PAGE+" integer");
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }
}
