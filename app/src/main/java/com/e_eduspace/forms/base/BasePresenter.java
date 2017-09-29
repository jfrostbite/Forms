package com.e_eduspace.forms.base;

import android.content.Intent;
import android.support.annotation.IdRes;

import com.e_eduspace.forms.R;
import com.e_eduspace.forms.coolpen.BluListeners;
import com.e_eduspace.forms.coolpen.BluManger;
import com.e_eduspace.forms.coolpen.CoolPenListener;
import com.e_eduspace.forms.coolpen.CoolPenManger;
import com.e_eduspace.forms.listener.DialogListener;
import com.e_eduspace.forms.model.constants.Constant;
import com.e_eduspace.forms.model.db.DBManger;
import com.e_eduspace.forms.model.entity.SudokuInfo;
import com.e_eduspace.forms.module.fund.draw.DrawActivity;
import com.e_eduspace.forms.utils.KUtils;
import com.newchinese.coolpensdk.entity.NotePoint;

/**
 * Created by Administrator on 2017-05-26.
 * 业务逻辑基类
 */

public abstract class BasePresenter<V extends IView> implements IPresenter<V>, DialogListener, CoolPenListener {

    protected V mIView;

    private boolean onlyUp;

    @Override
    public void attach(V iView) {
        mIView = iView;
    }

    @Override
    public void detach() {
        mIView = null;
//        CoolPenManger.newInstance().setOnPointChangedListener(null);
    }

    @Override
    public void showMsg(String msg) {
        mIView.showDialog(msg, this);
    }

    public void getBattery() {
        BluManger.newInstance().getBattery();
    }

    public void bluDisConnect() {
        BluManger.newInstance().disConnect();
    }

    public boolean bluConnected() {
        return BluManger.newInstance().isConnected();
    }

    @Override
    public boolean bluOpened() {
        return BluManger.newInstance().isOpen();
    }

    @Override
    public void bluConnect(Object obj) {
        BluManger.newInstance().connectBlu(obj);
    }

    @Override
    public void bluOpen() {
        if (!bluOpened()) {
            BluManger.newInstance().openBlu();
        }
    }

    @Override
    public void bluClose() {
        if (bluOpened()) {
            BluManger.newInstance().closeBle();
        }
    }

    @Override
    public boolean bluScanning() {
        return BluManger.newInstance().isScanning();
    }

    @Override
    public void bluDisScan() {
        BluManger.newInstance().disScan();
    }

    /**
     * 蓝牙初始化
     */
    @Override
    public void bluInit() {
        //窗口初始化完成后检查一次蓝牙开启状态。蓝牙初始化
        BluManger.newInstance().setOnListeners(BluListeners.newInstance().init(mIView), true);
        if (!bluOpened()) {
            showMsg("是否确认打开蓝牙？");
        }
    }

    @Override
    public void clear(int pageIndex) {
        DBManger.newInstance().del(pageIndex);
    }

    @Override
    public void onPositive(Object... objs) {

    }

    @Override
    public void onNegative() {

    }

    @Override
    public void coolPenInit() {
        CoolPenManger.newInstance().init(KUtils.getApp())
                .setBookSize(Constant.PAGE_WIDTH, Constant.PAGE_HEIGHT)
                .setAPIOffset(Constant.API_OFFSET_X, Constant.API_OFFSET_Y);
    }

    @Override
    public void drawInit() {
        CoolPenManger.newInstance().setOnPointChangedListener(this);
    }

    @Override
    public void onPenUp(NotePoint point) {
        //判断当前页面是否是绘制页面
        if (!onlyUp && mIView != null && !IDraw.class.isAssignableFrom(mIView.getClass())) {
            onlyUp = true;
            Intent intent = new Intent(KUtils.getApp(), DrawActivity.class);
            Integer pageIndex = point.getPageIndex();
            SudokuInfo sudoku = sudokuInfo(pageIndex);
            intent.putExtra("sudoku", sudoku);
            mIView.startActivity(intent);
        }
    }

    @Override
    public void onPenDrawLine(NotePoint point) {

    }

    @Override
    public void onPageChanged(int index) {

    }

    @Override
    public void onPenDown(NotePoint point) {
        onlyUp = false;
    }

    private SudokuInfo sudokuInfo(Integer pageIndex) {
        @IdRes int icons[] = new int[]{R.mipmap.logo_splash,R.mipmap.logo_splash,R.mipmap.logo_splash};
        @IdRes int imgs[][] = new int[][]{Constant.FUND_PAGES, Constant.ORGA_PAGES,Constant.PERS_PAGES};
        int page[] = new int[]{0,2,6};
        String[] names = KUtils.getApp().getResources().getStringArray(R.array.sudoku_info_array);
        SudokuInfo sudokuInfo = new SudokuInfo();
        if (pageIndex > 0 && pageIndex < 3) {
            sudokuInfo.setName(names[0])
                    .setIconRid(icons[0])
                    .setPageImg(imgs[0])
                    .setPageBegin(page[0])
                    .setPageIndex(pageIndex)
                    .setPenTo(true);
        } else if (pageIndex < 7 && pageIndex > 2) {
            sudokuInfo.setName(names[1])
                    .setIconRid(icons[1])
                    .setPageImg(imgs[1])
                    .setPageBegin(page[1])
                    .setPageIndex(pageIndex)
                    .setPenTo(true);
        } else if (pageIndex < 10 && pageIndex > 6) {
            sudokuInfo.setName(names[2])
                    .setIconRid(icons[2])
                    .setPageImg(imgs[2])
                    .setPageBegin(page[2])
                    .setPageIndex(pageIndex)
                    .setPenTo(true);
        } else {
            sudokuInfo.setName(names[0])
                    .setIconRid(icons[0])
                    .setPageImg(imgs[0])
                    .setPageBegin(page[0])
                    .setPageIndex(pageIndex)
                    .setPenTo(true);
        }
        return sudokuInfo;
    }
}
