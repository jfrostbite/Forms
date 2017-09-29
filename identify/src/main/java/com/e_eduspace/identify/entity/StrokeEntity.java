package com.e_eduspace.identify.entity;

import java.util.List;

/**
 * Created by Administrator on 2017-07-27.
 */

public interface StrokeEntity {
    Long getId();
    void setId(Long id);
    int getStrokeColor();
    void setStrokeColor(int strokeColor);
    Integer getPageIndex();
    void setPageIndex(Integer pageIndex);
    List<? extends PointEntity> getPointList();
    void resetPointList();
    void delete();
    void refresh();
    void update();
}
