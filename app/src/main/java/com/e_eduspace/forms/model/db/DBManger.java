package com.e_eduspace.forms.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.e_eduspace.forms.model.constants.Constant;
import com.e_eduspace.forms.model.entity.DaoMaster;
import com.e_eduspace.forms.model.entity.DaoSession;
import com.e_eduspace.forms.model.entity.FormPoint;
import com.e_eduspace.forms.model.entity.FormPointDao;
import com.e_eduspace.forms.model.entity.FormStroke;
import com.e_eduspace.forms.model.entity.FormStrokeDao;
import com.e_eduspace.forms.utils.PointUtils;
import com.newchinese.coolpensdk.entity.NotePoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017-05-27.
 * greendao封装数据库
 * 提供 数据 增删改查
 * <p>
 * 使用前init必须初始化 一次即可
 */

public class DBManger implements FormDB {

    private DaoSession mSession;

    private HashMap<Integer, List<FormStroke>> strokeCache = new HashMap<>();
    private DaoMaster.DevOpenHelper mDevOpenHelper;


    private DBManger() {
    }

    public static DBManger newInstance() {
        return DBInstance.DB_MANGER;
    }

    public void init(Context context) {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context, Constant.DB_NAME);
        SQLiteDatabase sqlite = mDevOpenHelper.getWritableDatabase();
        mSession = new DaoMaster(sqlite).newSession();
    }

    private static interface DBInstance {
        DBManger DB_MANGER = new DBManger();
    }

    @Override
    public void insert(FormStroke stroke) {
        mSession.getFormStrokeDao().insert(stroke);
    }

    @Override
    public void insert(FormPoint point) {

    }

    @Override
    public void insert(final FormStroke stroke, final List<NotePoint> point) {
        mSession.runInTx(new Runnable() {
            @Override
            public void run() {
                mSession.getFormStrokeDao().insertOrReplace(stroke);
                Long key = mSession.getFormStrokeDao().getKey(stroke);
                ArrayList<FormPoint> formPoints = new ArrayList<>();
                for (NotePoint notePoint : point) {
                    FormPoint fromPoint = PointUtils.getFromPoint(notePoint);
                    fromPoint.setSid(key);
                    formPoints.add(fromPoint);
                }
                insertPoints(formPoints);
            }
        });
    }

    @Override
    public void insertStrokes(List<FormStroke> strokes) {
        mSession.getFormStrokeDao().insertInTx(strokes);
    }

    @Override
    public void insertPoints(final List<FormPoint> points) {
        mSession.runInTx(new Runnable() {
            @Override
            public void run() {
                mSession.getFormPointDao().insertOrReplaceInTx(points);
            }
        });
    }

    @Override
    public void delAll() {
        mSession.runInTx(new Runnable() {
            @Override
            public void run() {
                clean();
                mSession.getFormPointDao().deleteAll();
                mSession.getFormStrokeDao().deleteAll();
            }
        });
    }

    @Override
    public void del(final int pageIndex) {
        mSession.runInTx(new Runnable () {
            @Override
            public void run() {
                clean();
                mSession.getFormPointDao().queryBuilder().where(FormPointDao.Properties.PageIndex.eq(pageIndex)).buildDelete().executeDeleteWithoutDetachingEntities();
                mSession.getFormStrokeDao().queryBuilder().where(FormStrokeDao.Properties.PageIndex.eq(pageIndex)).buildDelete().executeDeleteWithoutDetachingEntities();
            }
        });
    }

    @Override
    public void update(int index, FormStroke stroke) {

    }

    @Override
    public void queryPoint(final int page, final OnLoadDBListener listener) {
        mSession.runInTx(new Runnable() {
            @Override
            public void run() {
                List<FormPoint> formPoints = mSession.getFormPointDao().queryBuilder().where(FormPointDao.Properties.PageIndex.eq(page)).list();
                for (FormPoint formPoint : formPoints) {
                    if (listener != null) {
                        listener.onLoadDB(formPoint);
                    }
                }
                if (listener != null) {
                    listener.onFinish();
                }
            }
        });
    }

    @Override
    public List<FormPoint> query(int page) {
        return mSession.getFormPointDao().queryBuilder().where(FormPointDao.Properties.PageIndex.eq(page)).build().list();
    }

    /**
     * 数据库查询，索引从1 开始
     * @param page
     * @param current 当前线程
     * @param count 线程数
     * @return
     */
    @Override
    public List<FormStroke> queryStroke(int page, int current, int count) {
        try {
            long row = mSession.getFormStrokeDao().queryBuilder().where(FormStrokeDao.Properties.PageIndex.eq(page)).buildCount().forCurrentThread().count();
            long limit = row / count;
            int offset = 0;
            if (limit < 0) {
                limit = 1;
            }
            offset = (int) (current * limit);
        /*if (mBuild != null) {
            mBuild.forCurrentThread().;
        } else {
            mBuild = mSession.getFormStrokeDao().queryBuilder().where(FormStrokeDao.Properties.PageIndex.eq(page)).build();
        }
        mBuild.setLimit((int) limit);
        mBuild.setOffset(offset);
        List<FormStroke> list = mBuild.forCurrentThread().list();*/
        /*List<FormStroke> formStrokes = strokeCache.get(page);
        if (formStrokes != null) {
            list.removeAll(formStrokes);
            formStrokes.addAll(list);
        } else {
            strokeCache.put(page, list);
        }*/
            List<FormStroke> list = mSession.getFormStrokeDao().queryBuilder().where(FormStrokeDao.Properties.PageIndex.eq(page)).offset(offset).limit((int) limit).build().forCurrentThread().list();
            for (FormStroke stroke : list) {
                stroke.setNotePointList(new ArrayList<NotePoint>(stroke.getPointList()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strokeCache.get(page);
    }

    /**
     * 数据库查询，索引从1 开始
     * @param page
     * @return
     */
    @Override
    public List<FormStroke> queryStroke(int page) {
        List<FormStroke> list = mSession.getFormStrokeDao().queryBuilder().where(FormStrokeDao.Properties.PageIndex.eq(page)).build().list();
        List<FormStroke> formStrokes = strokeCache.get(page);
        if (formStrokes != null) {
            list.removeAll(formStrokes);
            formStrokes.addAll(list);
        } else {
            strokeCache.put(page, list);
        }
        for (FormStroke stroke : list) {
            List<FormPoint> pointList = stroke.getPointList();
            ArrayList<NotePoint> notePoints = new ArrayList<>();
            notePoints.addAll(pointList);
            stroke.setNotePointList(notePoints);
        }
        return strokeCache.get(page);
    }

    @Override
    public void queryStroke(final int page, final OnLoadDBListener listener) {
        mSession.runInTx(new Runnable () {
            public void run () {
                List<FormStroke> list = mSession.getFormStrokeDao().queryBuilder().where(FormStrokeDao.Properties.PageIndex.eq(page)).build().list();
                /*List<FormStroke> formStrokes = strokeCache.get(page);
                if (formStrokes != null) {
                    list.removeAll(formStrokes);
                    formStrokes.addAll(list);
                } else {
                    strokeCache.put(page, list);
                }*/
                /*for (FormStroke stroke : list) {
                    List<FormPoint> pointList = stroke.getPointList();
                    ArrayList<NotePoint> notePoints = new ArrayList<>();
                    notePoints.addAll(pointList);
                    stroke.setNotePointList(notePoints);
                }*/
                if (listener != null) {
                    listener.onLoadDB(/*strokeCache.get(page)*/list);
                    listener.onFinish();
                }
            }
        });
    }

    @Override
    public void clean(){
        strokeCache.clear();
        mSession.clear();
    }
}
