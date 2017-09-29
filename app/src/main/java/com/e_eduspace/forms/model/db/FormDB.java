package com.e_eduspace.forms.model.db;

import com.e_eduspace.forms.model.entity.FormPoint;
import com.e_eduspace.forms.model.entity.FormStroke;
import com.newchinese.coolpensdk.entity.NotePoint;

import java.util.List;

/**
 * Created by Administrator on 2017-05-27.
 * 工行表单数据库
 */

public interface FormDB {

    /**
     * 线操作
     * @param stroke
     */
    void insert(FormStroke stroke);
    void insert(FormPoint point);
    void insert(FormStroke stroke, List<NotePoint> point);
    void insertStrokes(List<FormStroke> strokes);
    void insertPoints(List<FormPoint> points);

    void delAll();

    void del(int pageIndex);

    /**
     * 根据页数更新
     * @param index
     * @param stroke
     */
    void update(int index, FormStroke stroke);

    void queryPoint(int page, OnLoadDBListener listener);

    List<FormPoint> query(int page);

    List<FormStroke> queryStroke(int page);

    void queryStroke(int page, OnLoadDBListener listener);

    List<FormStroke> queryStroke(int page, int current, int count);

    void clean();
}
