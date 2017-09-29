package com.e_eduspace.forms.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.DrawableRes;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017-05-27.
 */

public class BitmapUtils {

    //缩放Bitmap ,更具资源文件
    public static Bitmap decodeBitmap(@DrawableRes int rid){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //计算宽高比
        BitmapFactory.decodeResource(KUtils.getApp().getResources(), rid, options);
        //图片宽高
        int imgWidth = options.outWidth;
        int imgHeight = options.outHeight;

        //收集宽高
        int widthPixels = KUtils.getApp().getResources().getDisplayMetrics().widthPixels;
        int heightPixels = KUtils.getApp().getResources().getDisplayMetrics().heightPixels;

        int zoom = 1;
        if (imgWidth > widthPixels || imgHeight > heightPixels) {
            zoom = Math.max(imgWidth / widthPixels, imgHeight / heightPixels);
        } else {
            zoom = 1;
        }

        options.inSampleSize = zoom;

        options.inJustDecodeBounds = false;

        options.inPreferredConfig = Bitmap.Config.RGB_565;

        Bitmap bitmap = BitmapFactory.decodeResource(KUtils.getApp().getResources(), rid, options);

        return bitmap;
    }

    public static float realRatio(float width, float heght){
        return heght/width;
    }

    public static Bitmap createAlphaBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        return bitmap;
    }

    /**
     * 获取View截图
     */
    public static void snapView(View view, String path){
        if (!path.endsWith(".png")) {
            path = path + ".png";
        }
        FileOutputStream fos = null;
        Bitmap drawingCache = null;
        try {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            drawingCache = view.getDrawingCache();
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    LogUtils.e("截图路径创建失败");
                    return;
                }
            }
            fos = new FileOutputStream(file);
            drawingCache.compress(Bitmap.CompressFormat.PNG, 80, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {

                }
            }
            if (drawingCache != null && !drawingCache.isRecycled()) {
                drawingCache.recycle();
            }
            view.setDrawingCacheEnabled(false);
        }
    }

    /**
     * 获取旋转图片
     */
    public static Bitmap decodeBitmap(File file, boolean adjust, int... value){
        if (value.length != 2) {
            value = new int[]{KUtils.getApp().getResources().getDisplayMetrics().widthPixels, KUtils.getApp().getResources().getDisplayMetrics().heightPixels};
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), opts);

        int degree = adjust ? getDegree(opts, file.getAbsolutePath()) : 0;

        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        //收集宽高
        int widthPixels = value[0];
        int heightPixels = value[1];
        opts.inSampleSize = Math.max(1, Math.max(imgWidth / widthPixels, imgHeight / heightPixels));
        //获取缩放图片
        opts.inJustDecodeBounds = false;
        Bitmap srcBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);

        //旋转图片
        if (degree > 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degree);
            Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, false);
            if (!srcBitmap.isRecycled()) {
                srcBitmap.recycle();
            }
            return bitmap;
        } else {
            return srcBitmap;
        }
    }

    private static int getDegree(BitmapFactory.Options opts, String file) {
        int degree = 0;
        int rottion = 0;
        try {
            ExifInterface exi = new ExifInterface(file);
            rottion = exi.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (opts.outWidth <= 0 && opts.outHeight <= 0) {
                opts.outWidth = exi.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH,0);
                opts.outHeight = exi.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH,0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            degree = 0;
        }

        switch (rottion) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                break;
        }
        return degree;
    }
}
