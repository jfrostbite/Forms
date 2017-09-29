package com.e_eduspace.forms;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.e_eduspace.forms.net.ApiService;
import com.e_eduspace.forms.net.ApiSubscriber;
import com.e_eduspace.forms.net.RetrofitClient;
import com.e_eduspace.identify.Constants;
import com.e_eduspace.identify.entity.PointAreaEntity;
import com.e_eduspace.identify.utils.LogUtils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private SQLiteDatabase mTarget;
    private SQLiteDatabase mSource;
    private boolean recog = true;

    @Test
    public void addition_isCorrect() throws Exception {

//        dbCopy();
//        https();
/*
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                System.out.println("创建被观察者在：" + Thread.currentThread().getName());
                e.onNext("被观察这发送的数据");
            }
        });

        Observable<String> observable1 = observable.subscribeOn(NewThreadScheduler.instance()).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                System.out.println("转换线程在：" + Thread.currentThread().getName());
                s += "， 在map中进行转换了";
                return s;
            }
        });

        observable1.subscribe();*/
/*
        int j = 0;
        for (int i = 0; i < 10; i++) {
            System.out.println("for 循环进行中..." + i);

            do {
                j++;
                if (j == 3) {
                    break;
                }
                System.out.println("while 循环进行中..." + i);
            } while (true);
        }*/

        double a = 0.8115354;
        double floor = Math.floor(a);
        System.out.println(floor);


    }


    private void https() {
        String path = System.getProperty("user.dir");
        File file = new File(new File(path), "srca.cer");
        ApiService api = RetrofitClient.newInstance().init(null, file).getService(ApiService.class);
        api.baidu().subscribe(new ApiSubscriber<ResponseBody>() {
            @Override
            protected void onResult(boolean isSucc, Object t) {
                if (isSucc) {
                    try {
                        System.out.println(((ResponseBody) t).string());
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

    private void test1(String... a) {
        System.out.println(a);
    }

    private void dbCopy() {
        String dir = System.getProperty("user.dir") + File.separator;
        System.out.println(dir);

        mTarget = SQLiteDatabase.openDatabase(dir + "POINT_DB.db", null, SQLiteDatabase.OPEN_READWRITE);
        mSource = SQLiteDatabase.openDatabase(dir + "POINT_DB_2.db", null, SQLiteDatabase.OPEN_READONLY);
        List<PointAreaEntity> query = query(recog);
        for (PointAreaEntity pointAreaEntity : query) {
            insert(recog, pointAreaEntity);
        }
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


    private void test() {
    /*Gson gson = new GsonBuilder().serializeNulls().create();
    List<FormPage> formpage = gson.fromJson(new InputStreamReader(new FileInputStream(new File(new File("E:\\Android Project\\forms\\app\\src\\main\\assets"), "formpage"))), new TypeToken<List<FormPage>>() {
    }.getType());
    for (FormPage formPage : formpage) {
        System.out.println(formpage);
    }*/
//        System.out.println(3d / 10d);

        /*for (int i = 0; i < 1600; i++) {
            int len = new Random().nextInt(100);
            float v1 =  (i) / 1600f * 100;
            for (int i1 = 0; i1 < len; i1++) {
                System.out.println(v1 + ((i1 + 1f) / len) * 100/1600f);
            }
        }*/

        /*File file = new File(System.getProperty("user.dir")+File.separator+"cache");
        File file1 = new File(file, "name.txt");
        file1.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(file1);
        fos.write("dsaf".getBytes());
        fos.close();
        System.out.println(file1.getParentFile().getAbsolutePath());
        System.out.println(file1.getParentFile().getPath());*/
        System.out.println(10 / 3);
    }

    class Mission implements Runnable {
        @Override
        public void run() {

        }
    }
}