package com.e_eduspace.forms.adapter;

import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.e_eduspace.forms.model.constants.Constant;
import com.e_eduspace.forms.utils.BitmapUtils;
import com.e_eduspace.forms.utils.KUtils;
import com.e_eduspace.identify.LayoutWidget.FormLayout;

import java.io.File;

/**
 * Created by Administrator on 2017-05-27.
 */

public class FormVpAdapter extends PagerAdapter {

    private int[] mRids;
    private OnPageInitializedListener mListener;
    private File[] mFiles;

    public FormVpAdapter(@IdRes int[] rids){
        mRids = rids;
    }

    public FormVpAdapter(File... files){
        mFiles = files;
    }

    @Override
    public int getCount() {
        return mRids == null ? mFiles == null ? 0 : mFiles.length : mRids.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RelativeLayout rl = new RelativeLayout(KUtils.getApp());
        rl.setGravity(Gravity.CENTER);
        FormLayout fl = new FormLayout(KUtils.getApp());
        if (mRids != null) {
            fl.setBackground(new BitmapDrawable(KUtils.getApp().getResources(), BitmapUtils.decodeBitmap(mRids[position])));
        } else if (mFiles != null) {
            fl.setBackground(new BitmapDrawable(KUtils.getApp().getResources(), BitmapUtils.decodeBitmap(mFiles[position], false)));
        }
        //设置元纸张大小
        fl.primeval(Constant.PAGE_WIDTH,Constant.PAGE_HEIGHT, Constant.FL_OFFSET_X, Constant.FL_OFFSET_Y);
        rl.addView(fl);
        container.addView(rl);
        if (mListener != null) {
            mListener.OnPageInitialized(fl,position + 1);
        }
        return rl;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void addOnPageInitializedListener(OnPageInitializedListener listener){

        mListener = listener;
    }

    public interface OnPageInitializedListener {
        void OnPageInitialized(FormLayout layout, int postion);
    }
}
