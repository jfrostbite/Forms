package com.e_eduspace.identify;

import android.content.Context;

import com.e_eduspace.identify.db.DBManager;
import com.e_eduspace.identify.db.PointRangeDB;
import com.e_eduspace.identify.entity.AreaBean;
import com.e_eduspace.identify.entity.CheckBean;
import com.e_eduspace.identify.entity.FormPage;
import com.e_eduspace.identify.entity.LineBean;
import com.e_eduspace.identify.entity.PageBean;
import com.e_eduspace.identify.entity.PointAreaEntity;
import com.e_eduspace.identify.entity.PointEntity;
import com.e_eduspace.identify.entity.StrokeEntity;
import com.e_eduspace.identify.singleLineWidget.IDentifyCertificate;
import com.e_eduspace.identify.singleLineWidget.OnTextChangedRetryListener;
import com.e_eduspace.identify.singleLineWidget.SingleLineWidgetApiImpl;
import com.e_eduspace.identify.singleLineWidget.WidgetExpress;
import com.e_eduspace.identify.utils.LogUtils;
import com.myscript.atk.core.CaptureInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017-05-25.
 * 点范围确定，识别转换封装
 * <p>
 * 功能： 1、确定点范围；2、根据已知范围点进行传入数据进行分类识别
 * <p>
 * 配置：1、数据库：名字，路径
 * 2、识别模块配置
 * 3、IDentifySingle 配置：处理超时时间、处理Loading、日志开关、区域断言、是否参与识别。
 * <p>
 * 演化：
 * 自生点范围-》assest 范围点数据导入。实现自动化
 * <p>
 * * 多线程并发 问题：
 * 创建公用builder。
 */

public class IDentifySingle implements OnTextChangedRetryListener, IDentify {

    private int mRetryTimeout;
    //超时时间单位秒
    private int mRetryCount;
    private ExecutorService mExecutor;
    private SingleLineWidgetApiImpl mSingleLineWidgetApi;
    private DBManager mManager;
    private int counter = 0;
    private int tagCounter = 0;
    PointAreaEntity pae = null;
    //添加范围点 区分 范围点所用Tag
    private String mTag;
    private FormPage mFormPage;

    private PageBean mPage = new PageBean();
    //正在识别
    private long mMillis;
    private OnRecognitionListener<PageBean> mListener;
    //基点误差
    private float[] mDeviation = {0f, 0f};
    private int mPageIndex;
    private float mRatio;
    private Future<?> mFuture;

    private IDentifySingle(Builder builder) {
        mRetryCount = builder.mRetryCount;
        mRetryTimeout = builder.mRetryTimeout;
        mManager = builder.manager;
        mExecutor = builder.mExecutor;
        mSingleLineWidgetApi = new SingleLineWidgetApiImpl(builder.mContext);
        initIdentify();
    }

    /**
     * 识别模块初始化
     */
    private void initIdentify() {
        if (mSingleLineWidgetApi == null) {
            throw new NullPointerException("识别模块初始化失败");
        }
        if (!mSingleLineWidgetApi.registerCertificate(IDentifyCertificate.getBytes())) {
            throw new RuntimeException("证书初始化失败！");
        }
        String packageCodePath = mSingleLineWidgetApi.getContext().getPackageCodePath();
        mSingleLineWidgetApi.addSearchDir("zip://" + packageCodePath + "!/assets/conf");
        //配置识别类型
        mSingleLineWidgetApi.configure(WidgetExpress.IDENTIFY_TYPE_DIGIT);
        mSingleLineWidgetApi.setRetry(3, 1);
        mSingleLineWidgetApi.setOnTextChangedListener(this);
    }

    /**
     * 初始化识别结果类
     */
    private void initPage() {
        ArrayList<AreaBean> areas = new ArrayList<>();
        for (FormPage.Area area : mFormPage.area) {
            String[] lineNames = area.names.split(",");
            ArrayList<LineBean> lines = new ArrayList<>();
            //行遍历
            for (int i = 0, len = Math.min(lineNames.length, area.types.length); i < len; i++) {
                LineBean lineBean = new LineBean().setLineName(lineNames[i]).setLineType(area.types[i]);
                lines.add(lineBean);
            }
            AreaBean areaBean = new AreaBean().setAreaName(area.name).setLines(lines);
            areas.add(areaBean);
        }

        String[] selects = mFormPage.selects.split(",");
        ArrayList<CheckBean> checkList = new ArrayList<>();
        for (int i = 0, len = Math.min(selects.length, mFormPage.selectCounts.length); i < len; i++) {
            checkList.add(new CheckBean().setCheckName(selects[i]).setCheckLength(mFormPage.selectCounts[i]));
        }
        mPage.setPageIndex(mFormPage.page);
        mPage.setAreaList(areas);
        mPage.setCheckList(checkList);
    }

    /**
     * 区域点收集方法
     * <p>
     * areas 长度必须和areaSize长度相同
     *
     * @param point 欲收集点
     */
    private void addPoint(boolean recog, PointEntity point) {
        long l = System.currentTimeMillis();
        if (l - mMillis < 100) {
            return;
        }
        mMillis = l;
        String tag = "";
        int len = 0;

        if (recog) {
            for (FormPage.Area area : mFormPage.area) {
                String[] names = area.names.split(",");
                len += names.length * 2;
                if (counter < len) {
                    tag = area.name;
                    break;
                }
            }
        } else {
            String[] selects = mFormPage.selects.split(",");
            for (int i = 0; i < selects.length; i++) {
                String name = selects[i];
                len += mFormPage.selectCounts[i] * 2;
                if (counter < len) {
                    tag = name;
                    break;
                }
            }
        }

        if (tag.equals(mTag)) {
            tagCounter++;
        } else {
            mTag = tag;
            tagCounter = 0;
        }

        if (counter % 2 != 0) {//奇数保存数据
            pae.setMaxX(point.getPX())
                    .setMaxY(point.getPY());
            LogUtils.e(pae.toString());
            addPoint(pae, recog);
        } else {//偶数创建数据对象
            pae = new PointAreaEntity().setTag(tag)
                    .setLoc(String.valueOf(tagCounter / 2))
                    .setPageIndex(point.getPageIndex())
                    .setMinX(point.getPX())
                    .setMinY(point.getPY());
        }
        counter++;
    }

    /**
     * 手动添加已知范围点
     */
    private void addPoint(PointAreaEntity pointAreaEntity, boolean recog) {
        mManager.insert(recog, pointAreaEntity);
    }

    private void old2new() {
        List<PointAreaEntity> query = mManager.query(true);
        mManager.delete(true);
        for (PointAreaEntity pointAreaEntity : query) {
            pointAreaEntity.setPageIndex(1);
            addPoint(pointAreaEntity, true);
        }
    }

    public IDentifySingle setOnRecognitionListener(OnRecognitionListener<PageBean> listener) {

        mListener = listener;
        return this;
    }

    /**
     * 重要，模块识别处理所需重要数据
     *
     * @param formPage 点集区域集合
     */
    public IDentifySingle initialize(FormPage formPage) {
        if (formPage == null) {
            throw new NullPointerException("参数错误，处理数据null");
        }
        mFormPage = formPage;
        //计算误差
        deviation();
        initPage();
        return this;
    }

    /**
     * 误差方法
     */
    private void deviation() {
        float[] base = mFormPage.base;
        if (base.length == 4) {
            for (int i = 0; i < mDeviation.length; i++) {
                mDeviation[i] = base[i + 2] - base[i];
            }
        }
    }

    /**
     * 取点位置
     */
    public void convert(final List<? extends StrokeEntity> strokes) {
        if (strokes == null || strokes.isEmpty()) {
            return;
        }
        if (mFormPage == null) {
            throw new NullPointerException("this is try running null @param areas on convert(),please invoke method initialize() before convert()");
        }
        if (mListener != null) {
            mListener.onBegined(mPage.getPageIndex());
        }
        mFuture = mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                //非参与识别
                for (StrokeEntity stroke : strokes) {
                    checkAction(stroke);
                }
                //分配数据
                for (int i = 0, len = strokes.size(); i < len; i++) {
                    StrokeEntity stroke = strokes.get(i);
                    process(stroke);
                    if (mListener != null) {
                        mListener.onLoading(mPage.getPageIndex(), "正在分配...     ", (int) (((i + 1f) / len) * 100));
                    }
                }
                LogUtils.e("数据分配完成");
                LogUtils.e("判断是否需要进行文字转换");
                identify();
            }
        });
    }

    /**
     * 处理勾选事件
     *
     * @param strokes
     */
    private boolean checkAction(StrokeEntity strokes) {
        PointEntity point = validPoint(strokes.getPointList());
        if (point == null) {
            return false;
        }
        List<CheckBean> checkList = mPage.getCheckList();
        String page = String.valueOf(mPage.getPageIndex());
        for (int i = 0, len = checkList.size(); i < len; i++) {
            CheckBean checkBean = checkList.get(i);
            int length = checkBean.getCheckLength();
            for (int j = 0; j < length; j++) {
                float[] coord = null;
                if ((coord = judge(false, point.getPX(), point.getPY(), checkBean.getCheckName(), String.valueOf(j), page)) != null) {
                    //填充行坐标
                    checkBean.setCheckValue(j);
                    checkBean.setLeft(coord[0]);
                    checkBean.setTop(coord[1]);
                    checkBean.setRight(coord[2]);
                    checkBean.setBottom(coord[3]);
                    //匹配成功。删除减少重复操纵
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 识别模块
     */
    private void identify() {
        if (mPage != null) {
            for (int i = 0, len = mPage.getAreaList().size(); i < len; i++) {
                AreaBean area = mPage.getAreaList().get(i);
                //区域识别开始
                List<LineBean> lines = area.getLines();
                //每行识别开始
                float v = (float) i / len * 100;
                for (int j = 0, size = lines.size(); j < size; j++) {
                    float progress = v + ((j + 1f) / size) * 100f / len;
                    LineBean line = lines.get(j);
                    identify(progress, line);
                }
            }
            if (mListener != null) {
                check();
                mListener.onFinished(mPage);
            }
        }
    }

    private void identify(float progress, LineBean line) {
        mSingleLineWidgetApi.clear();
        mSingleLineWidgetApi.setTag(line);
        mSingleLineWidgetApi.configure(line.getLineType());
        List<List<CaptureInfo>> lists = line.getCaptrueInfos();
        if (lists.isEmpty()) {
            return;
        }
        for (int i = 0; i < lists.size(); i++) {
            List<CaptureInfo> list = lists.get(i);
            mSingleLineWidgetApi.addStroke(list, i == lists.size() - 1);
        }
//        mSingleLineWidgetApi.setDentifying(true);
        //阻塞时方法
        //TODO
        identifying(progress);
    }

    private void identifying(float progress) {
        if (mListener != null) {
            mListener.onLoading(mPage.getPageIndex(), "正在识别...      ", (int) progress);
        }
        int count = 0;
        int retry = 0;
        do {
            LineBean tag = (LineBean) mSingleLineWidgetApi.getTag();
            LogUtils.e(tag.getLineName() + "：正在识别..." + mSingleLineWidgetApi.getStroke().size());
//            SystemClock.sleep(1000);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            count++;
            if (count > mRetryTimeout) {
                LogUtils.e(tag.getLineName() + "：进入重试...   " + mSingleLineWidgetApi.getStroke().size());
                retry++;
                count = 0;
                mSingleLineWidgetApi.retry();
            }
            if (retry > mRetryCount) {
                LogUtils.e(tag.getLineName() + "：超时跳出");
                mSingleLineWidgetApi.clear();
                break;
            }
        } while (mSingleLineWidgetApi.isDentifying());
    }

    /**
     * 区域划分核心
     *
     * @param stroke
     * @return
     */
    private boolean process(StrokeEntity stroke) {
        boolean mate = false;
        boolean over = false;
        PointEntity point = validPoint(stroke.getPointList());
        if (point == null) {
            return false;
        }
        if (mPage != null) {
            String page = String.valueOf(mPage.getPageIndex());
            for (int i = 0, len = mPage.getAreaList().size(); i < len; i++) {
                AreaBean areaBean = mPage.getAreaList().get(i);
                String areaName = areaBean.getAreaName();
                List<LineBean> lines = areaBean.getLines();
                for (int j = 0, size = areaBean.getLines().size(); j < size; j++) {//原数据顺讯
                    float[] coord = null;
                    if ((coord = judge(true, point.getPX(), point.getPY(), areaName, String.valueOf(j), page)) != null) {
                        //匹配 ，填充行
                        lines.get(j).addStrokes(stroke);
                        //填充行坐标
                        lines.get(j).setLeft(coord[0]);
                        lines.get(j).setTop(coord[1]);
                        lines.get(j).setRight(coord[2]);
                        lines.get(j).setBottom(coord[3]);
                        mate = true;
                        return true;
                    } else {
                    }
                }
            }
        }
        return false;
    }

    /**
     * 确定点范围
     *
     * @param x
     * @param y
     * @param tag
     * @param loc
     * @return
     */
    private float[] judge(boolean recog, float x, float y, String tag, String loc, String page) {
        return mManager.query(recog, x, y, tag, loc, page);
    }

    /**
     * 获取平均点
     */
    private PointEntity validPoint(List<? extends PointEntity> notePoints) {
        if (notePoints == null || notePoints.isEmpty()) {
            return null;
        }
        PointEntity pointEntity = (PointEntity) notePoints.get(0).newInstance();
//        notePoints = notePoints.subList(notePoints.size() / 3, notePoints.size());
        int size = notePoints.size()/* > 10 ? 10 : notePoints.size()*/;
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

        pointEntity.setPX(sumX / size - mDeviation[0]);
        pointEntity.setPY(sumY / size - mDeviation[1]);
        return pointEntity;
    }

    private void shutdown() {
        if (mExecutor != null && !mExecutor.isShutdown()) {
            mExecutor.shutdownNow();
        }
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
        }
    }

    @Override
    public void onTextChanged(WidgetExpress express, String text, boolean intermediate) {
        if (!intermediate) {
            LineBean line = (LineBean) mSingleLineWidgetApi.getTag();
            line.setLineValue(text);
            LogUtils.e("onTextChanged", line.getLineName() + "     ======      " + text + "----" + mSingleLineWidgetApi.getStroke().size());
        }
    }

    public static class Builder implements BuilderPort {
        //识别超时
        private int mRetryCount = 1;
        //重试超时
        private int mRetryTimeout = 9;
        //圈点
        private ExecutorService mExecutor;
        //        private Map<String, Map<String, Integer>> mAreas;
        private DBManager manager = PointRangeDB.newInstance();
        private Context mContext;
        private IDentifySingle mIDentifySingle;

        public Builder(Context context) {
            this(context, null);
        }

        public Builder(Context context, byte[] bytes) {
            this(context, bytes, false);
        }

        public Builder(Context context, byte[] bytes, boolean log) {
            if (bytes != null && bytes.length > 0) {
                IDentifyCertificate.setBytes(bytes);
            } else {
                LogUtils.e("MyScript 证书错误, 识别不可用");
            }
            if (context != null) {
                new LogUtils.Builder(context).setBorderSwitch(true).setLogSwitch(log).setGlobalTag("identify");
                manager.init(context, loadDB(context));
            }
            mExecutor = Executors.newCachedThreadPool();
        }

        public void closeDB() {
            if (mIDentifySingle != null) {
                mIDentifySingle.shutdown();
            }
            if (manager != null) {
                manager.closeDB();
            }
        }

        /**
         * 超时时间单位秒
         * 默认识别超时10秒,retry超时10秒
         */
        public Builder setTimeout(int recoginTime, int retryTime) {
            mRetryCount = recoginTime;
            mRetryTimeout = retryTime;
            return this;
        }

        private boolean loadDB(Context context) {
            mContext = context;
            boolean hasDB = true;
            try {
                InputStream fis = context.getResources().getAssets().open(Constants.POINT_DB_NAME);
                File dbFile = context.getDatabasePath(Constants.POINT_DB_NAME);
                if (!dbFile.getParentFile().exists()) {
                    hasDB = dbFile.getParentFile().mkdirs() && dbFile.createNewFile();
                } else if (!dbFile.exists()) {
                    hasDB = dbFile.createNewFile();
                } else {
                    return true;
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
            return hasDB;
        }

        /**
         * 通过build获取Identify实体类
         */
        public IDentifySingle build() {
            mIDentifySingle = new IDentifySingle(this);
            return mIDentifySingle;
        }
    }


    public void check() {
        if (mPage != null) {
            List<AreaBean> areas = mPage.getAreaList();
            if (areas != null && !areas.isEmpty()) {
                for (AreaBean area : areas) {
                    List<LineBean> lines = area.getLines();
                    if (lines != null && !lines.isEmpty()) {
                        for (LineBean line : lines) {
                            if (!line.isNormal()) {//识别失败数据重新做识别
                                LogUtils.e("非正常状态检查...");
                                recogn(line);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void recogn(LineBean line) {
        identify(100, line);
    }
}
