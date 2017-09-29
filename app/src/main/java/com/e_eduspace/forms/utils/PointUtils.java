package com.e_eduspace.forms.utils;

import com.e_eduspace.forms.model.entity.FormPoint;
import com.newchinese.coolpensdk.entity.NotePoint;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017-06-01.
 * SDK有关点处理工具
 */

public class PointUtils {

    /**
     * NotePoint 下强转
     */
    public static FormPoint getFromPoint(NotePoint notePoint){
        FormPoint formPoint = new FormPoint();
        formPoint.setId(null);
        formPoint.setPX(notePoint.getPX());
        formPoint.setPY(notePoint.getPY());
        formPoint.setFirstPress(notePoint.getFirstPress());
        formPoint.setTestTime(notePoint.getTestTime());
        formPoint.setPageIndex(notePoint.getPageIndex());
        formPoint.setPointType(notePoint.getPointType());
        formPoint.setPress(notePoint.getPress());
        return formPoint;
    }

    /**
     * 获取平均点
     */
    public static NotePoint validPoint(List<NotePoint> notePoints) {
        notePoints = notePoints.subList(0, notePoints.size() / 2);
        NotePoint notePoint = new NotePoint();
        if (notePoints == null || notePoints.isEmpty()) {
            notePoint.setPX(0f);
            notePoint.setPY(0f);
            return notePoint;
        }
        int size = notePoints.size() > 10 ? 10 : notePoints.size();
        float[] pxs = new float[size];
        float[] pys = new float[size];
        float sumX = 0f;
        float sumY = 0f;
        for (int i = 0; i < size; i++) {
            int anInt = new Random().nextInt(notePoints.size());
            pxs[i] = notePoints.get(anInt).getPX();
            pys[i] = notePoints.get(anInt).getPY();
        }

        for (float px : pxs) {
            sumX += px;
        }
        for (float py : pys) {
            sumY += py;
        }

        notePoint.setPX(sumX / size);
        notePoint.setPY(sumY / size);
        return notePoint;
    }
}
