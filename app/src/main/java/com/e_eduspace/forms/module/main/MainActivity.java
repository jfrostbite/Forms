package com.e_eduspace.forms.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.e_eduspace.forms.R;
import com.e_eduspace.forms.adapter.OnHolderClickListener;
import com.e_eduspace.forms.adapter.SudokuRvAdapter;
import com.e_eduspace.forms.base.BaseView;
import com.e_eduspace.forms.model.entity.SudokuInfo;
import com.e_eduspace.forms.module.fund.draw.DrawActivity;
import com.e_eduspace.forms.utils.LogUtils;

import java.util.List;

public class MainActivity extends BaseView<MainIPresenter, List<SudokuInfo>> implements MainIView<List<SudokuInfo>>, OnHolderClickListener<SudokuInfo> {

    private RecyclerView mRvSudoku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        mPresenter.generateData();
    }

    @Override
    public void initView() {
        mRvSudoku = (RecyclerView) findViewById(R.id.rv_main_sudoku);
        mRvSudoku.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    public void initListener() {

    }

    @Override
    public void updateView(List<SudokuInfo> list) {
        mRvSudoku.setAdapter(new SudokuRvAdapter(this).setList(list).setOnHolderClickListener(this));
    }

    @Override
    protected MainIPresenter newPresenter() {
        return new MainPresenterImpl();
    }

    @Override
    public void onHolderClick(SudokuInfo sudokuInfo) {
        LogUtils.e(sudokuInfo);
        Class cls = DrawActivity.class;
        Intent intent = new Intent(this, cls);
        intent.putExtra("sudoku",sudokuInfo);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
