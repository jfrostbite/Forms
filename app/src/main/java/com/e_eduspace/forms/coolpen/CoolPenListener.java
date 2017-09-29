package com.e_eduspace.forms.coolpen;

import com.newchinese.coolpensdk.entity.NotePoint;

/**
 * Created by Administrator on 2017-05-27.
 */

public interface CoolPenListener {

    void onPenDown(NotePoint point);

    void onPenUp(NotePoint point);

    void onPenDrawLine(NotePoint point);

    void onPageChanged(int index);
}
