package com.e_eduspace.forms.model.entity;

import com.e_eduspace.identify.entity.PointEntity;
import com.newchinese.coolpensdk.entity.NotePoint;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2017-05-27.
 */

@Entity
public class FormPoint extends NotePoint implements PointEntity {
    @Id(autoincrement = true)
    private Long id;

    private Float pX;
    private Float pY;
    private Float press;
    private Float firstPress;
    private Integer pageIndex;
    private Integer pointType;
    private Long sid;
    @Generated(hash = 1886362693)
    public FormPoint(Long id, Float pX, Float pY, Float press, Float firstPress,
            Integer pageIndex, Integer pointType, Long sid) {
        this.id = id;
        this.pX = pX;
        this.pY = pY;
        this.press = press;
        this.firstPress = firstPress;
        this.pageIndex = pageIndex;
        this.pointType = pointType;
        this.sid = sid;
    }
    @Generated(hash = 163610871)
    public FormPoint() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Float getPX() {
        return this.pX;
    }
    public void setPX(Float pX) {
        this.pX = pX;
    }
    public Float getPY() {
        return this.pY;
    }
    public void setPY(Float pY) {
        this.pY = pY;
    }
    public Float getPress() {
        return this.press;
    }
    public void setPress(Float press) {
        this.press = press;
    }
    public Float getFirstPress() {
        return this.firstPress;
    }
    public void setFirstPress(Float firstPress) {
        this.firstPress = firstPress;
    }
    public Integer getPageIndex() {
        return this.pageIndex;
    }
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
    public Integer getPointType() {
        return this.pointType;
    }
    public void setPointType(Integer pointType) {
        this.pointType = pointType;
    }
    public Long getSid() {
        return this.sid;
    }
    public void setSid(Long sid) {
        this.sid = sid;
    }
    public Object newInstance(){
        return new FormPoint();
    }
}
