package com.e_eduspace.identify.LayoutWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.e_eduspace.identify.R;
import com.e_eduspace.identify.utils.LogUtils;

/**
 * Created by Administrator on 2017-06-14.
 *
 * 表单布局
 * 利用每行自带位置坐标，设置layout。
 */

public class FormLayout extends ViewGroup {

    private float mPrimeWidth;
    private float mPrimeHeight;
    public float mRatio;
    public float mDeviationY;
    public float mDeviationX;

    public FormLayout(Context context) {
        this(context,null);
    }

    public FormLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.FormDefStyle);
    }

    public FormLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.FormLayout,defStyleAttr,0);
        mPrimeWidth = a.getFloat(R.styleable.FormLayout_prime_width, 0);
        mPrimeHeight = a.getFloat(R.styleable.FormLayout_prime_height, 0);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        //如果检测到原始纸张大小，那么进行表单布局重新测量，根据原始纸张大小确定布局大小
        if (makeRatio(measureWidth, measureHeight)) {
            measureWidth = Math.round(mRatio * mPrimeWidth);
            measureHeight = Math.round(mRatio * mPrimeHeight);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.getMode(widthMeasureSpec));
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.getMode(heightMeasureSpec));
//            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
            setMeasuredDimension(measureWidth,measureHeight);
            measureChildren(widthMeasureSpec,heightMeasureSpec);
        }
//        LogUtils.e("onMeasure====="+measureWidth+"=="+measureHeight);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /*int count = getChildCount();
        //表单纸张坐标转换成px，后重设字体大小，重新测量子view
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            int left = view.getLeft();
            int top = view.getTop();
            int right = view.getRight();
            int bottom = view.getBottom();
            int height = bottom - top;
            int measuredHeight = view.getMeasuredHeight();
            if (view.getClass().isAssignableFrom(TextView.class)) {
                TextView tv = (TextView) view;
                float textSize = tv.getTextSize();
                textSize = textSize * ((float) height / (float) measuredHeight);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                *//*float spacing = tv.getLetterSpacing();
                if (spacing > 0f) {
                    spacing = spacing / 5.66f;
                    spacing = (view.getBottom() - view.getTop()) * spacing;
//                    tv.setLetterSpacing(spacing);
                }*//*
                tv.setBackgroundColor(Color.parseColor("#66FF4081"));
            }
            measureChild(view,MeasureSpec.makeMeasureSpec(right - left,MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(bottom - top,MeasureSpec.EXACTLY));
            view.layout(left, top,right,bottom);
        }*/
    }

    /**
     * 设置原始大小以及误差
     */
    public void primeval(float primeWidth, float primeHeight, float deviationX, float deviationY){
        mPrimeWidth = primeWidth;
        mPrimeHeight = primeHeight;
        mDeviationX = deviationX;
        mDeviationY = deviationY;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));
    }

    /**
     * 计算比例
     * @param width
     * @param height
     * @return
     */
    private boolean makeRatio (int width, int height){
        mRatio = Math.min(width / mPrimeWidth, height / mPrimeHeight);
        return mPrimeHeight > 0 || mPrimeWidth > 0;
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resid) {
        super.setBackgroundResource(resid);
        int measuredHeight = getMeasuredHeight();
        int height = getHeight();
        LogUtils.e(measuredHeight + "---"+height);
    }
}
