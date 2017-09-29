package com.e_eduspace.forms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import com.e_eduspace.forms.net.ApiService;
import com.e_eduspace.forms.net.ApiSubscriber;
import com.e_eduspace.forms.net.RetrofitClient;
import com.e_eduspace.forms.utils.RSAHelper;
import com.e_eduspace.forms.utils.LogUtils;
import com.e_eduspace.identify.Constants;
import com.e_eduspace.identify.entity.PointAreaEntity;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private SQLiteDatabase mTarget;
    private SQLiteDatabase mSource;
    private boolean recog = false;
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.e_eduspace.forms", appContext.getPackageName());

//        dbMerge(appContext);

//        net(appContext);

//        rxjava();

        String txt = "Kevin峰";
        RSAHelper rsa = new RSAHelper();
        String publicKey = rsa.getPublicKey();
        String privateKey = rsa.getPrivateKey();
        LogUtils.e("获取私钥："+privateKey);
        LogUtils.e("获取公钥："+publicKey);
        String s = rsa.encryptByPublic(txt.getBytes(), publicKey);
        LogUtils.e("公钥加密："+ s);
        LogUtils.e("公钥解密：" + new String(rsa.decryptByPublic(s, publicKey)));
        LogUtils.e("私钥解密：" + new String(rsa.decryptByPrivate(s, privateKey)));
        LogUtils.e(new String(rsa.decryptByPrivate(s, privateKey)).equals(txt));
        s = rsa.encryptByPrivate(txt.getBytes(), privateKey);
        LogUtils.e("私钥加密："+ s);
        LogUtils.e("私钥解密：" + new String(rsa.decryptByPrivate(s, privateKey)));
        LogUtils.e("公钥解密：" + new String(rsa.decryptByPublic(s, publicKey)));
        LogUtils.e(new String(rsa.decryptByPublic(s, publicKey)).equals(txt));
    }

    private void rxjava() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                LogUtils.e("创建被观察者");
                e.onNext("发送数据");
            }
        });

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.e("onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        LogUtils.e("onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("onError");
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.e("onComplete");
                    }
                });
    }

    private void net(Context appContext) {
        ApiService service = RetrofitClient.newInstance()
                .init(appContext)
                .getService(ApiService.class);

        Observable<ResponseBody> baidu = service.baidu();
        baidu.subscribe(new ApiSubscriber<Object>() {
            @Override
            protected void onResult(boolean isSucc, Object t) {
                if (isSucc) {
                    try {
                        System.out.println(((ResponseBody)t).string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(t);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void dbMerge(Context appContext) {
        String target = loadDB(appContext, "POINT_DB.db");
        String source = loadDB(appContext, "POINT_DB_3.db");

        mTarget = SQLiteDatabase.openDatabase(target, null, SQLiteDatabase.OPEN_READWRITE);
        mSource = SQLiteDatabase.openDatabase(source, null, SQLiteDatabase.OPEN_READONLY);
        List<PointAreaEntity> query = query(recog);
        for (PointAreaEntity pointAreaEntity : query) {
            insert(recog,pointAreaEntity);
            System.out.println(pointAreaEntity);
        }
    }

    private String loadDB(Context context, String name) {
        boolean hasDB = true;
        String path = null;
        try {
            InputStream fis = context.getResources().getAssets().open(name);
            File dbFile = context.getDatabasePath(name);
            path = dbFile.getPath();
            if (!dbFile.getParentFile().exists()) {
                hasDB = dbFile.getParentFile().mkdirs() && dbFile.createNewFile();
            } else if (!dbFile.exists()) {
                hasDB = dbFile.createNewFile();
            } else {
                return path;
            }
            FileOutputStream fos = new FileOutputStream(dbFile);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fis.read(bytes)) > -1) {
                fos.write(bytes, 0, len);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            hasDB = false;
        }
        return path;
    }

    public void insert(boolean recog, PointAreaEntity pointEntity) {
        ContentValues values = new ContentValues();
        values.put(Constants.POINT_RANGE_TAG, pointEntity.getTag());
        values.put(Constants.POINT_RANGE_LOC, pointEntity.getLoc());
        values.put(Constants.POINT_RANGE_MINX, pointEntity.getMinX());
        values.put(Constants.POINT_RANGE_MAXX, pointEntity.getMaxX());
        values.put(Constants.POINT_RANGE_MINY, pointEntity.getMinY());
        values.put(Constants.POINT_RANGE_MAXY, pointEntity.getMaxY());
        values.put(Constants.POINT_RANGE_PAGE, pointEntity.getPageIndex());
        long insert = mTarget.insert(recog ? Constants.POINT_RANGE_IDENTIFY_TAB_NAME : Constants.POINT_RANGE_TAB_NAME, null, values);
        LogUtils.e(insert);
    }

    public List<PointAreaEntity> query(boolean recog) {
        ArrayList<PointAreaEntity> paes = new ArrayList<>();
        Cursor cursor = mSource.query(recog ? Constants.POINT_RANGE_IDENTIFY_TAB_NAME : Constants.POINT_RANGE_TAB_NAME, new String[]{}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Constants.POINT_RANGE_ID));
            String tag = cursor.getString(cursor.getColumnIndex(Constants.POINT_RANGE_TAG));
            String loc = cursor.getString(cursor.getColumnIndex(Constants.POINT_RANGE_LOC));
            int page = cursor.getInt(cursor.getColumnIndex(Constants.POINT_RANGE_PAGE));
            float minX = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MINX));
            float minY = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MINY));
            float maxX = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MAXX));
            float maxY = cursor.getFloat(cursor.getColumnIndex(Constants.POINT_RANGE_MAXY));
            if (TextUtils.isEmpty(tag)) {
                continue;
            }
            PointAreaEntity pae = new PointAreaEntity().setId(id)
                    .setTag(tag)
                    .setLoc(loc)
                    .setPageIndex(page)
                    .setMinX(minX)
                    .setMinY(minY)
                    .setMaxX(maxX)
                    .setMaxY(maxY);
            paes.add(pae);
        }
        cursor.close();
        return paes;
    }
}
