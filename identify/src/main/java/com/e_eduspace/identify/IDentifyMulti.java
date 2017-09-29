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
import java.util.concurrent.Callable;
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
 * 3、IDentifyMulti 配置：处理超时时间、处理Loading、日志开关、区域断言、是否参与识别。
 * <p>
 * 演化：
 * 自生点范围-》assest 范围点数据导入。实现自动化
 * <p>
 * * 多线程并发 问题：
 * 创建公用builder。
 */

public class IDentifyMulti implements OnTextChangedRetryListener, IDentify {

    private int mRetryTimeout;
    //超时时间单位秒
    private int mRetryCount;
    private ExecutorService mExecutor;
    private SingleLineWidgetApiImpl[] mSingleLineWidgetApi;
    private Context mContext;
    private DBManager mManager;
    private int counter = 0;
    private int tagCounter = 0;
    PointAreaEntity pae = null;
    //添加范围点 区分 范围点所用Tag
    private String mTag;
    private FormPage mFormPage;
    private volatile PageBean mPage = new PageBean();
    //正在识别
    private long mMillis;
    private OnRecognitionListener<PageBean> mListener;
    //基点误差
    private float[] mDeviation = {0f, 0f};
    private int mPageIndex;
    private float mRatio;
    private List<Future> mFutures = new ArrayList<>();

    private IDentifyMulti(Builder builder) {
        mRetryCount = builder.mRetryCount;
        mRetryTimeout = builder.mRetryTimeout;
        mManager = builder.manager;
        mContext = builder.mContext;
        mExecutor = Executors.newCachedThreadPool();
    }

    /**
     * 识别模块初始化
     */
    private void initIdentify(int i) {
        mSingleLineWidgetApi[i] = new SingleLineWidgetApiImpl(mContext);
        if (!mSingleLineWidgetApi[i].registerCertificate(IDentifyCertificate.getBytes())) {
            throw new RuntimeException("证书初始化失败！");
        }
        String packageCodePath = mSingleLineWidgetApi[i].getContext().getPackageCodePath();
        mSingleLineWidgetApi[i].addSearchDir("zip://" + packageCodePath + "!/assets/conf");
        //配置识别类型
        mSingleLineWidgetApi[i].setRetry(5, 1);
        mSingleLineWidgetApi[i].setOnTextChangedListener(this);
    }

    /**
     * 初始化识别结果类
     *
     * @param formPage
     */
    private void initPage(FormPage formPage) {
        ArrayList<AreaBean> areas = new ArrayList<>();

        for (FormPage.Area area : formPage.area) {
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

        String[] selects = formPage.selects.split(",");
        ArrayList<CheckBean> checkList = new ArrayList<>();
        for (int i = 0, len = Math.min(selects.length, formPage.selectCounts.length); i < len; i++) {
            checkList.add(new CheckBean().setCheckName(selects[i]).setCheckLength(formPage.selectCounts[i]));
        }
        mPage.setPageIndex(formPage.page);
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
        if (l - mMillis < 500) {
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

    public IDentifyMulti setOnRecognitionListener(OnRecognitionListener<PageBean> listener) {

        mListener = listener;
        return this;
    }

    /**
     * 重要，模块识别处理所需重要数据
     *
     * @param formPage 点集区域集合
     */
    public IDentifyMulti initialize(FormPage formPage) {
        if (formPage == null) {
            throw new NullPointerException("参数错误，处理数据null");
        }
        mFormPage = formPage;
        mPageIndex = formPage.page;
        int size = formPage.area.size();
        //计算区块占比
        mRatio = 100f / size;
        mSingleLineWidgetApi = new SingleLineWidgetApiImpl[size];
        for (int i = 0; i < size; i++) {
            initIdentify(i);
        }
        //计算误差
        deviation();
        initPage(formPage);
        return this;
    }

    /**
     * 误差方法，暂时无用，保留，可删
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
        if (mListener != null) {
            mListener.onBegined(mPageIndex);
        }
        mFutures.add(mExecutor.submit(new Runnable() {
            private long time = 0;

            @Override
            public void run() {
                //非参与识别
                time = System.currentTimeMillis();
                List<CheckBean> checkCache = mPage.getCheckList();
                for (int i = 0, len = strokes.size(); i < len; i++) {
                    StrokeEntity stroke = strokes.get(i);
                    checkAction(checkCache, stroke);
                    if (mListener != null) {
                        mListener.onLoading(mPageIndex, "正在初始化...     ", (int) ((i + 1f) / len * 100));
                    }
                }
                LogUtils.e("CheckAction耗时：" + (System.currentTimeMillis() - time));
                time = System.currentTimeMillis();
                //分配数据
                List<AreaBean> areaCache = mPage.getAreaList();
                for (int i = 0, len = strokes.size(); i < len; i++) {
                    StrokeEntity stroke = strokes.get(i);
                    process(areaCache, stroke);
                    if (mListener != null) {
                        mListener.onLoading(mPageIndex, "正在解析...     ", (int) (((i + 1f) / len) * 100));
                    }
                }
                LogUtils.e("分配数据耗时：" + (System.currentTimeMillis() - time));
                LogUtils.e("数据分配完成");
                LogUtils.e("判断是否需要进行文字转换");
                identify();
            }
        }));
    }

    /**
     * 处理勾选事件
     *
     * @param strokes
     */
    private CheckBean checkAction(List<CheckBean> checkList, StrokeEntity strokes) {
        PointEntity point = validPoint(strokes.getPointList());
        if (point == null) {
            return null;
        }
        String page = String.valueOf(mPageIndex);
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
                    return checkBean;
                }
            }
        }
        return null;
    }

    /**
     * 识别模块
     */
    private void identify() {
        if (mPage != null) {
            for (int i = 0, len = mPage.getAreaList().size(); i < len; i++) {
                final AreaBean area = mPage.getAreaList().get(i);
                final int finalI = i;
                mFutures.add(mExecutor.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        iDentifyTask(finalI, area);
                        return null;
                    }
                }));
            }
        }
    }

    /**
     * 区域并发
     *
     * @param i
     * @param area
     */
    private void iDentifyTask(int i, AreaBean area) {
        //区域识别开始
        List<LineBean> lines = area.getLines();
        //每行识别开始
//                float v = (float) i / len * 100;
        for (int j = 0, size = lines.size(); j < size; j++) {
            LineBean line = lines.get(j);
            float progress = (j + 1f) / size;
            identify(mSingleLineWidgetApi[i], line, progress);
        }
        //识别完毕
        mSingleLineWidgetApi[i].setComplete();
        boolean isFinished = true;
        for (SingleLineWidgetApiImpl api : mSingleLineWidgetApi) {
            isFinished &= api.completed();
        }
        if (isFinished && mListener != null) {
            check();
            mListener.onFinished(mPage);
        }
    }

    private void identify(SingleLineWidgetApiImpl singleLineWidgetApi, LineBean line, float progress) {
        singleLineWidgetApi.clear();
        singleLineWidgetApi.setTag(line);
        singleLineWidgetApi.configure(line.getLineType());
        List<List<CaptureInfo>> lists = line.getCaptrueInfos();
        if (lists.isEmpty()) {
            return;
        }
        for (int i = 0; i < lists.size(); i++) {
            List<CaptureInfo> list = lists.get(i);
            singleLineWidgetApi.addStroke(list, i == lists.size() - 1);
        }
        //设置正在识别
//        singleLineWidgetApi.setDentifying(true);
        //设置识别进度
        singleLineWidgetApi.setProgress(progress);
        //阻塞时方发
        //TODO
        identifying(singleLineWidgetApi);
    }

    private void identifying(SingleLineWidgetApiImpl slwapi) {
        int progress = 0;
        for (int j = 0; j < mSingleLineWidgetApi.length; j++) {
            progress += mSingleLineWidgetApi[j].getProgress() * mRatio;
        }
        if (mListener != null) {
            mListener.onLoading(mPageIndex, "正在识别...      ", (int) (progress));
        }
        int count = 0;
        int retry = 0;
        while (slwapi.isDentifying()) {
            LineBean tag = (LineBean) slwapi.getTag();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            LogUtils.e(tag.getLineName() + "：正在识别...");
            count++;
            if (count > mRetryTimeout) {
                LogUtils.e(tag.getLineName() + "：进入重试...");
                retry++;
                count = 0;
                slwapi.retry();
            }
            if (retry > mRetryCount) {
                LogUtils.e(tag.getLineName() + "：超时跳出");
                break;
            }
        }
    }

    /**
     * 区域划分核心
     *
     * @param areaCache
     * @param stroke
     * @return
     */
    private boolean process(List<AreaBean> areaCache, StrokeEntity stroke) {
        boolean mate = false;
        boolean over = false;
        PointEntity point = validPoint(stroke.getPointList());
        if (point == null) {
            return false;
        }
        if (mPage != null) {
            String page = String.valueOf(mPageIndex);
            for (int i = 0, len = areaCache.size(); i < len; i++) {
                AreaBean areaBean = areaCache.get(i);
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

    private void shutdown(){
        if (mExecutor != null && !mExecutor.isShutdown()) {
            mExecutor.shutdownNow();
        }
        for (Future future : mFutures) {
            if (future != null && !future.isCancelled()) {
                future.cancel(true);
            }
        }
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

    @Override
    public void onTextChanged(WidgetExpress express, String text, boolean intermediate) {
        if (!intermediate) {
            LineBean line = (LineBean) express.getTag();
            line.setLineValue(text);
            LogUtils.e("onTextChanged", line.getLineName() + "     ======      " + text);
        }
    }

    public static class Builder implements BuilderPort {
        //识别超时
        private int mRetryCount = 1;
        //重试超时
        private int mRetryTimeout = 5;
        private DBManager manager = PointRangeDB.newInstance();
        private Context mContext;
        private IDentifyMulti mIDentifyMulti;

        public Builder(Context context) {
            this(context, null);
        }

        public Builder(Context context, byte[] bytes){
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
        }

        /**
         * 超时时间单位秒
         * 默认识别超时10秒,retry超时10秒
         */
        public Builder setTimeout(int retryCount, int retryTime) {
            mRetryCount = retryCount;
            mRetryTimeout = retryTime;
            return this;
        }

        public void closeDB() {
            if (mIDentifyMulti != null) {
                mIDentifyMulti.shutdown();
            }
            if (manager != null) {
                manager.closeDB();
            }
        }

        public Builder openWriteDB(){
            if (manager != null) {
                manager.openWrite();
            }
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
        public IDentifyMulti build() {
            mIDentifyMulti = new IDentifyMulti(this);
            return mIDentifyMulti;
        }
    }

    public void check() {
        LogUtils.e("非正常状态检查...");
        if (mPage != null) {
            List<AreaBean> areas = mPage.getAreaList();
            if (areas != null && !areas.isEmpty()) {
                for (AreaBean area : areas) {
                    List<LineBean> lines = area.getLines();
                    if (lines != null && !lines.isEmpty()) {
                        for (LineBean line : lines) {
                            if (!line.isNormal()) {//识别失败数据重新做识别
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
        identify(mSingleLineWidgetApi[0],line,mSingleLineWidgetApi[0].getProgress());
    }
}
