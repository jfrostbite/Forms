package com.e_eduspace.forms.module.fund.draw;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.e_eduspace.forms.R;
import com.e_eduspace.forms.adapter.FormVpAdapter;
import com.e_eduspace.forms.base.BaseView;
import com.e_eduspace.forms.base.IDraw;
import com.e_eduspace.forms.model.constants.Constant;
import com.e_eduspace.forms.model.entity.SudokuInfo;
import com.e_eduspace.forms.module.fund.form.FormActivity;
import com.e_eduspace.forms.utils.BitmapUtils;
import com.e_eduspace.forms.utils.LogUtils;
import com.e_eduspace.forms.widget.DrawingBoard;
import com.newchinese.coolpensdk.entity.NotePoint;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class DrawActivity extends BaseView<DrawIPresneter, Object> implements DrawIView<Object>, ViewPager.OnPageChangeListener,IDraw {

    private FloatingActionButton mFab;
    private ViewPager mVpMain;
    private DrawingBoard mDbMain;
    private SudokuInfo mSudoku;
    private int mPageBegin;
    private boolean mStartScroll;
    //滚动方向 0 左；1 右
    private int mDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_draw);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mFab = (FloatingActionButton) findViewById(R.id.fab_base_action);
        mVpMain = (ViewPager) findViewById(R.id.vp_draw_main);
        mDbMain = (DrawingBoard) findViewById(R.id.db_draw_main);

        mDbMain.setOffset(Constant.DB_OFFSET_X,Constant.DB_OFFSET_Y);

        mFab.setVisibility(View.VISIBLE);

        mFab.setImageBitmap(BitmapUtils.decodeBitmap(R.mipmap.draw_ic_retry));

        mVpMain.setAdapter(new FormVpAdapter(mSudoku.getPageImg()));

        mVpMain.setCurrentItem(mSudoku.getPageIndex() - 1);
    }

    @Override
    protected void createMenu(Menu menu) {
        super.createMenu(menu);
        menu.setGroupVisible(R.id.group_draw,true);
    }

    @Override
    protected void itemSelected(MenuItem item){
        Intent intent = new Intent(this, FormActivity.class);
        switch (item.getItemId()) {
            case R.id.draw_pen_convert:
                mPresenter.checkPoints();
                intent.putExtra("sudoku",mSudoku);
                startActivity(intent);
                break;
            case R.id.draw_pen_convert_:
                mPresenter.checkPoints();
                intent.putExtra("sudoku",mSudoku);
                intent.putExtra("fast",true);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void initIntent(){
        mSudoku = getIntent().getParcelableExtra("sudoku");
        mPageBegin = mSudoku.getPageBegin();
    }

    @Override
    public void initListener() {
        mFab.setOnClickListener(this);
        mVpMain.addOnPageChangeListener(this);
    }

    @Override
    protected DrawIPresneter newPresenter() {
        return new DrawPresenterImpl();
    }

    @Override
    public void bluFailure(boolean positive) {
        super.bluFailure(positive);
    }

    @Override
    public void updateView(Object o) {

    }

    @Override
    protected void windowChanged(boolean hasFocus) {
        super.windowChanged(hasFocus);
        if (mPresenter != null && mSudoku.getPageIndex() == 1) {
            mPresenter.onDrawDB(mPageBegin + 1);
        }
    }

    @Override
    public DrawingBoard getDrawBoard() {
        return mDbMain;
    }

    @Override
    public void drawPoint(NotePoint point) {
        //判断超出范围点
        if (mPresenter.getPageIndex(point) <= mPageBegin + mSudoku.getPageImg().length && mPresenter.getPageIndex(point) > mPageBegin) {
            int pageIndex = mPresenter.getPageIndex(point) - mPageBegin;
            setPageIndex(pageIndex);
            mDbMain.drawLine(point);
        } else {
            mPresenter.showMsg("请在当前项目页面书写。");
        }
    }

    @Override
    public void drawStroke(List<NotePoint> strokes) {
        if (strokes != null && !strokes.isEmpty()) {
//            mDbMain.readPointByDataBase(strokes);
            Observable.fromArray(strokes.toArray()).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object obj) throws Exception {
                    if (obj != null && NotePoint.class.isAssignableFrom(obj.getClass())) {
                        mDbMain.drawLine((NotePoint) obj);
                    }
                }
            });
        }
    }

    @Override
    public void setPageIndex(int index) {
        if (mVpMain.getAdapter().getCount() >= index && mVpMain.getCurrentItem() != index - 1) {
            clean();
            mVpMain.setCurrentItem(index - 1);
        }
    }

    @Override
    public void clean() {
        if (mDbMain != null) {
            mDbMain.clearCanvars();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_base_action:
                mPresenter.clear(mVpMain.getCurrentItem() + 1 + mPageBegin);
                break;
        }
    }

    @Override
    public void screenshot(String path) {
        BitmapUtils.snapView(mDbMain.getRootView(),path);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e("onPageScrolled", positionOffsetPixels + "");
        if (mStartScroll) {
            mDirection = Math.round(positionOffset);
            mStartScroll = false;
        }
        if (mDirection == 0) {//左划
            mDbMain.setTranslationX(-positionOffsetPixels);
        } else {
            int translationX = mVpMain.getMeasuredWidth() - positionOffsetPixels;
            mDbMain.setTranslationX(translationX);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mPresenter.checkPoints();
        mPresenter.onDrawDB(mPageBegin + position + 1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            mStartScroll = true;
        } else if(state == ViewPager.SCROLL_STATE_IDLE){
            mStartScroll = false;
            mDbMain.setTranslationX(0);
        }
        LogUtils.w("onPageScrollStateChanged");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
