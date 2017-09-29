package com.e_eduspace.forms.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-05-15.
 *
 */

public class ToastUtils {

    private static Toast mToast;
    private static Snackbar mSnack;

    public static void show(String msg){
        mToast = mToast == null ? Toast.makeText(KUtils.getApp(),msg,Toast.LENGTH_LONG) : mToast;
        mToast.setText(msg);
//        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void snack(@NonNull View view, String msg) {
        sanck(view,msg,false);
    }

    public static void sanck(@NonNull View view, String msg, boolean brief) {
        LogUtils.d(msg);
        mSnack = mSnack == null ? Snackbar.make(view, msg, brief ? Snackbar.LENGTH_SHORT : Snackbar.LENGTH_LONG) : mSnack;
        mSnack.setAction("提示：", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("SnackBar 被点击了");
            }
        }).show();
    }
}
