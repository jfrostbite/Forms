package com.e_eduspace.forms.adapter.holder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.e_eduspace.forms.R;
import com.e_eduspace.forms.adapter.OnHolderClickListener;
import com.e_eduspace.forms.base.BaseViewHolder;
import com.e_eduspace.forms.model.entity.SudokuInfo;

/**
 * Created by Administrator on 2017-07-04.
 */


public class SudokuHolder extends BaseViewHolder<SudokuInfo> implements View.OnClickListener {

    private TextView mTvName;
    private ImageView mIvIcon;
    private OnHolderClickListener<SudokuInfo> mListener;
    private SudokuInfo mSudokuInfo;

    public SudokuHolder(Context context, @LayoutRes int rid) {
        super(context, rid);
    }

    public SudokuHolder(View view) {
        super(view);
    }

    @Override
    protected void initListener() {
        itemView.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        mTvName = (TextView) itemView.findViewById(R.id.tv_sudoku_name);
        mIvIcon = (ImageView) itemView.findViewById(R.id.iv_sudoku_icon);
    }

    @Override
    public void setData(SudokuInfo sudokuInfo) {
        mSudokuInfo = sudokuInfo;
        mTvName.setText(sudokuInfo.getName());
        mIvIcon.setImageResource(sudokuInfo.getIconRid());
    }

    @Override
    public BaseViewHolder setOnHolderClickListener(OnHolderClickListener<SudokuInfo> listener) {

        mListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onHolderClick(mSudokuInfo);
        }
    }
}
