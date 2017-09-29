package com.e_eduspace.forms.module.fund.draw;

import com.e_eduspace.forms.base.IPresenter;
import com.e_eduspace.forms.model.entity.FormStroke;
import com.newchinese.coolpensdk.entity.NotePoint;

import java.util.List;

/**
 * Created by Administrator on 2017-05-26.
 * 绘制逻辑
 */

public interface DrawIPresneter extends IPresenter<DrawIView> {

    /**
     * 获取当前页
     */
    int getPageIndex(NotePoint point);

    /**
     * 数据库取点绘制
     * @param position
     */
    void onDrawDB(int position);

    /**
     * 保存数据
     * @param stroke
     * @param points
     */
    void serialize(FormStroke stroke, List<NotePoint> points);

    void checkPoints();
}
