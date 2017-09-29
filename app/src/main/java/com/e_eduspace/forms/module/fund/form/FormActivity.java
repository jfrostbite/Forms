package com.e_eduspace.forms.module.fund.form;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e_eduspace.forms.R;
import com.e_eduspace.forms.adapter.FormVpAdapter;
import com.e_eduspace.forms.base.BaseView;
import com.e_eduspace.forms.base.IToolbar;
import com.e_eduspace.forms.model.constants.Constant;
import com.e_eduspace.forms.model.entity.SudokuInfo;
import com.e_eduspace.forms.utils.BitmapUtils;
import com.e_eduspace.forms.utils.KUtils;
import com.e_eduspace.identify.LayoutWidget.FormLayout;
import com.e_eduspace.identify.ThreadPool.ThreadPool;
import com.e_eduspace.identify.entity.AreaBean;
import com.e_eduspace.identify.entity.CheckBean;
import com.e_eduspace.identify.entity.LineBean;
import com.e_eduspace.identify.entity.PageBean;
import com.e_eduspace.identify.singleLineWidget.WidgetExpress;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FormActivity extends BaseView<FormIPresenter, PageBean> implements FormIView<PageBean>, IToolbar, ViewPager.OnPageChangeListener, FormVpAdapter.OnPageInitializedListener {


    private ViewPager mViewPager;
    private FormVpAdapter mAdapter;
    //布局缓存
    private Map<Integer, FormLayout> mLayoutCache = new TreeMap<>();
    private PageBean mPageBean;
    private boolean mFast;
    private SudokuInfo mSudoku;
    private int mPageBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_form);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                lineAction(v);
                break;
        }
    }

    private void lineAction(View v) {
        if (v != null && v.getClass().isAssignableFrom(TextView.class)) {
            LineBean line = (LineBean) v.getTag();
            showDialog("修改", line.getLineValue(),this,v);
        }
    }

    public void initIntent(){
        mFast = getIntent().getBooleanExtra("fast", false);
        mSudoku = getIntent().getParcelableExtra("sudoku");
        mPageBegin = mSudoku.getPageBegin();
    }

    @Override
    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_form_content);
        mAdapter = new FormVpAdapter(mSudoku.getPageImg());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mSudoku.getPageImg().length);
    }

    @Override
    public void initListener() {
        mViewPager.addOnPageChangeListener(this);
        mAdapter.addOnPageInitializedListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void updateView(PageBean pageBean) {
        mPageBean = pageBean;
        if (pageBean != null) {
            int pageIndex = pageBean.getPageIndex() - mPageBegin;
            final FormLayout layout = mLayoutCache.get(pageIndex);
            if (layout != null && !pageBean.getAreaList().isEmpty()) {
                for (AreaBean areaBean : pageBean.getAreaList()) {
                    for (LineBean lineBean : areaBean.getLines()) {
                        if (!KUtils.isEmpty(lineBean.getLineValue())) {
                            addView(layout, lineBean, pageBean.getPageIndex());
                        }
                    }
                }
            }
            List<CheckBean> checkList = pageBean.getCheckList();
            for (CheckBean checkBean : checkList) {
                if (checkBean.getCheckValue() != -1) {
                    addView(layout, checkBean,pageBean.getPageIndex());
                }
            }
            //截图
//            screenShot(pageBean.getPageBegin());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addView(FormLayout layout, LineBean lineBean, int pageIndex) {
        //每页起始点坐标
        float bx = (pageIndex - 1) * Constant.PAGE_WIDTH;
        TextView tv = new TextView(this);
        tv.setText(lineBean.getLineValue());
        //设置位置
        int left = (int) ((lineBean.getLeft() - bx) * layout.mRatio);
        int top = (int) ((lineBean.getTop() + layout.mDeviationY) * layout.mRatio);
        int right = (int) ((lineBean.getRight() - bx + layout.mDeviationX) * layout.mRatio);
        int bottom = (int) ((lineBean.getBottom() + layout.mDeviationY) * layout.mRatio);
        int width = right + 10 - left;
        int height = bottom - top;
        tv.layout(left, top, right + 10 , bottom);
        if (lineBean.getLineType() == WidgetExpress.IDENTIFY_TYPE_DIGIT_SPACE || lineBean.getLineType() == WidgetExpress.IDENTIFY_TYPE_EN_SPACE) {
            tv.setLetterSpacing(Constant.TEXT_SPACE);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        }
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setBackgroundColor(getResources().getColor(R.color.clear_pink));
        tv.setTag(lineBean);
        tv.setOnClickListener(this);
        layout.addView(tv,new ViewGroup.LayoutParams(width, height));
    }

    private void addView(FormLayout layout, CheckBean checkBean, int pageIndex) {
        //每页起始点坐标
        float bx = (pageIndex - 1) * Constant.PAGE_WIDTH;
        TextView tv = new TextView(this);
        tv.setText("✔");
        int left = (int) ((checkBean.getLeft() - bx + layout.mDeviationX) * layout.mRatio);
        int top = (int) ((checkBean.getTop() + layout.mDeviationY) * layout.mRatio);
        int right = (int) ((checkBean.getRight() - bx + layout.mDeviationX) * layout.mRatio);
        int bottom = (int) ((checkBean.getBottom() + layout.mDeviationY) * layout.mRatio);
        tv.layout(left, top, right, bottom);
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv,new ViewGroup.LayoutParams(right + 10 - left,bottom - top));
    }

    @Override
    public FormIPresenter newPresenter() {
        return new FormPresenterImpl().setFast(mFast);
    }

    @Override
    protected void createMenu(Menu menu) {
        super.createMenu(menu);
        menu.removeItem(R.id.list_pen_cancel);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * VP初始化监听
     *
     * @param layout
     * @param postion 从1开始
     */
    @Override
    public void OnPageInitialized(FormLayout layout, int postion) {
        //缓存复用
        if (mLayoutCache.containsKey(postion)) {
            layout = mLayoutCache.get(postion);
        } else {
            layout.primeval(Constant.PAGE_WIDTH,Constant.PAGE_HEIGHT, Constant.FL_OFFSET_X, mPageBegin == 0 ? Constant.FL_OFFSET_Y : -2f);
            mLayoutCache.put(postion, layout);
            mPresenter.discern(postion + mPageBegin);
        }
    }

    @Override
    public boolean showing(int page) {
        return mViewPager.getCurrentItem() + 1 + mPageBegin == page;
    }

    @Override
    public void screenShot(final int pageIndex) {
        //截图
        ThreadPool.newInstance().submitCache(new Runnable() {
            @Override
            public void run() {
                BitmapUtils.snapView(mLayoutCache.get(pageIndex), new File(Constant.SCREEN_FILE_DIR, Constant.FORM_SCREEN + pageIndex).getPath());
            }
        });
    }

    @Override
    public void onPositive(Object... objs) {
        super.onPositive(objs);
        if (objs.length == 2) {
            if (objs[0].getClass().isAssignableFrom(TextView.class) && objs[1].getClass().isAssignableFrom(String.class)) {
                TextView tv = (TextView) objs[0];
                LineBean line = (LineBean) tv.getTag();
                String txt = (String) objs[1];
                line.setLineValue(txt);
                tv.setText(txt);
            }
        }
    }
}
