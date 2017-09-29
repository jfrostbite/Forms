package com.e_eduspace.forms.module.fund.draw;

import com.e_eduspace.forms.base.IToolbar;
import com.e_eduspace.forms.base.IView;
import com.newchinese.coolpensdk.entity.NotePoint;
import com.newchinese.coolpensdk.manager.DrawingBoardView;

import java.util.List;

/**
 * Created by Administrator on 2017-05-26.
 */

public interface DrawIView<E> extends IView<E> , IToolbar{

    /**
     * 获取画板空间
     */
    DrawingBoardView getDrawBoard();

    /**
     * 点绘制
     */
    void drawPoint(NotePoint point);

    /**
     * 根据点绘制
     */
    void drawStroke(List<NotePoint> strokes);

    void setPageIndex(int index);

    void clean();

    void screenshot(String path);
}

