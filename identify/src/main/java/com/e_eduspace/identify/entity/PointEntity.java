package com.e_eduspace.identify.entity;

/**
 * Created by Administrator on 2017-06-06.
 */

public interface PointEntity {
    Long getId();
    void setId(Long id);
    Float getPX();
    void setPX(Float pX);
    Float getPY();
    void setPY(Float pY);
    Float getPress();
    void setPress(Float press);
    Float getFirstPress();
    void setFirstPress(Float firstPress);
    Integer getPageIndex();
    void setPageIndex(Integer pageIndex);
    Integer getPointType();
    void setPointType(Integer pointType);
    Long getSid();
    void setSid(Long sid);
    Object newInstance();
}
