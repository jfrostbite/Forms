package com.e_eduspace.forms.model.db;

import com.e_eduspace.identify.entity.StrokeEntity;
import com.newchinese.coolpensdk.entity.NotePoint;

import java.util.List;

/**
 * Created by Administrator on 2017-05-31.
 */

public interface OnLoadDBListener {

    void onLoadDB(List<? extends StrokeEntity> strokes);

    void onLoadDB(NotePoint point);

    void onFinish();
}
