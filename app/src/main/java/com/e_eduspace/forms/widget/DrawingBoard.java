package com.e_eduspace.forms.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.e_eduspace.forms.model.constants.Constant;
import com.newchinese.coolpensdk.manager.DrawingBoardView;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017-06-06.
 */

public class DrawingBoard extends DrawingBoardView {

    private boolean mMax = true;

    public DrawingBoard(Context context) {
        this(context, null);
    }

    public DrawingBoard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawingBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        float a = (float) width / Constant.PAGE_WIDTH;
        float b = (float) height / Constant.PAGE_HEIGHT;
        mMax &= a < b;
        if (!mMax) {
            float scale = Math.max(a, b);
            Class<DrawingBoardView> drawingBoardViewClass = DrawingBoardView.class;
            try {
                Field canvasScale = drawingBoardViewClass.getDeclaredField("canvasScale");
                canvasScale.setAccessible(true);
                canvasScale.setFloat(this, scale);
                super.setOffset(mMax ? Constant.DB_OFFSET_X : Constant.DB_OFFSET_X_BUG,Constant.DB_OFFSET_Y);
                Field foreGroundBitmap = getClass().getSuperclass().getDeclaredField("foreGroundBitmap");
                Field foreGroundCanvas = getClass().getSuperclass().getDeclaredField("foreGroundCanvas");
                Field bgCanvas = getClass().getSuperclass().getDeclaredField("bgCanvas");
                foreGroundBitmap.setAccessible(true);
                foreGroundCanvas.setAccessible(true);
                bgCanvas.setAccessible(true);
                Bitmap bitmap = (Bitmap)foreGroundBitmap.get(this);
                int h = Math.round(Constant.PAGE_HEIGHT * scale);
                Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(),h,bitmap.getConfig());
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                if (newBitmap != null) {
                    foreGroundBitmap.set(this, newBitmap);
                    foreGroundCanvas.set(this,new Canvas(newBitmap));
                    bgCanvas.set(this,new Canvas(newBitmap));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void setOffset(float offsetX, float offsetY){
        super.setOffset(mMax ? offsetX : Constant.DB_OFFSET_X_BUG ,offsetY);
    }

    @Override
    public void initListener() {
        setIsDrawViewInited(true);
    }

    /*
    public void setBackground(int rid){
        Bitmap bitmap = BitmapUtils.decodeBitmap(rid);
        mCanvas.drawBitmap(bitmap,0,0,new Paint());
    }*/
}
