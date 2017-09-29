package com.e_eduspace.forms.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.e_eduspace.forms.adapter.OnHolderClickListener;

/**
 * Created by Administrator on 2017-05-11.
 *
 * RecyclerView 简单通用Adapter，需要提供Viewholer
 */

public abstract class BaseViewHolder<E> extends RecyclerView.ViewHolder {

    public BaseViewHolder(Context context, @LayoutRes int rid) {
        this(LayoutInflater.from(context).inflate(rid, null));
        initView();
        initListener();
    }

    public BaseViewHolder(View view){
        super(view);
    }

    protected abstract void initListener();

    protected abstract void initView();

    public abstract void setData(E e);

    public abstract BaseViewHolder setOnHolderClickListener (OnHolderClickListener<E> listener);
}
