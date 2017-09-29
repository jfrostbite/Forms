package com.e_eduspace.forms.model.entity;

import com.e_eduspace.identify.entity.StrokeEntity;
import com.newchinese.coolpensdk.entity.NoteStroke;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by Administrator on 2017-05-27.
 */

@Entity
public class FormStroke extends NoteStroke implements StrokeEntity {

    @Id(autoincrement = true)
    private Long id;
    private int strokeColor;
    @ToMany(referencedJoinProperty = "sid")
    private List<FormPoint> pointList;
    private Integer pageIndex;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1778748349)
    private transient FormStrokeDao myDao;
    @Generated(hash = 1001016166)
    public FormStroke(Long id, int strokeColor, Integer pageIndex) {
        this.id = id;
        this.strokeColor = strokeColor;
        this.pageIndex = pageIndex;
    }
    @Generated(hash = 1901025200)
    public FormStroke() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getStrokeColor() {
        return this.strokeColor;
    }
    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }
    public Integer getPageIndex() {
        return this.pageIndex;
    }
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1332768852)
    public List<FormPoint> getPointList() {
        if (pointList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FormPointDao targetDao = daoSession.getFormPointDao();
            List<FormPoint> pointListNew = targetDao._queryFormStroke_PointList(id);
            synchronized (this) {
                if (pointList == null) {
                    pointList = pointListNew;
                }
            }
        }
        return pointList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 18518400)
    public synchronized void resetPointList() {
        pointList = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 745372874)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFormStrokeDao() : null;
    }

}
