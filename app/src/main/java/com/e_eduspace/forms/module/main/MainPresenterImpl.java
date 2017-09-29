package com.e_eduspace.forms.module.main;

import android.support.annotation.IdRes;

import com.e_eduspace.forms.R;
import com.e_eduspace.forms.base.BasePresenter;
import com.e_eduspace.forms.model.constants.Constant;
import com.e_eduspace.forms.model.entity.SudokuInfo;
import com.e_eduspace.forms.utils.KUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-07-04.
 */

public class MainPresenterImpl extends BasePresenter<MainIView<List<SudokuInfo>>> implements MainIPresenter<MainIView<List<SudokuInfo>>> {


    @Override
    public void generateData() {
        ArrayList<SudokuInfo> infos = new ArrayList<>();
        @IdRes int icons[] = new int[]{R.mipmap.logo_splash,R.mipmap.logo_splash,R.mipmap.logo_splash};
        @IdRes int imgs[][] = new int[][]{Constant.FUND_PAGES, Constant.ORGA_PAGES,Constant.PERS_PAGES};
        int page[] = new int[]{0,2,6};
        String[] names = KUtils.getApp().getResources().getStringArray(R.array.sudoku_info_array);
        for (int i = 0; i < names.length; i++) {
            SudokuInfo sudokuInfo = new SudokuInfo()
                    .setName(names[i])
                    .setIconRid(icons[i])
                    .setPageImg(imgs[i])
                    .setPageBegin(page[i])
                    .setPageIndex(page[i] + 1)
                    .setPenTo(false);
            infos.add(sudokuInfo);
        }
        if (mIView != null) {
            mIView.updateView(infos);
        }
    }
}
