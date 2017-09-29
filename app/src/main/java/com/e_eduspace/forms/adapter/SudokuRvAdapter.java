package com.e_eduspace.forms.adapter;

import android.content.Context;

import com.e_eduspace.forms.R;
import com.e_eduspace.forms.adapter.holder.SudokuHolder;
import com.e_eduspace.forms.base.BaseRvAdapter;
import com.e_eduspace.forms.base.BaseViewHolder;
import com.e_eduspace.forms.model.entity.SudokuInfo;

/**
 * Created by Administrator on 2017-07-04.
 */

public class SudokuRvAdapter extends BaseRvAdapter<SudokuInfo> {
    private OnHolderClickListener<SudokuInfo> mListener;

    public SudokuRvAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder initHolder() {
        return new SudokuHolder(mContext, R.layout.main_sudoku_item).setOnHolderClickListener(mListener);
    }

    @Override
    public BaseRvAdapter setOnHolderClickListener(OnHolderClickListener<SudokuInfo> listener) {
        mListener = listener;
        return this;
    }
}
