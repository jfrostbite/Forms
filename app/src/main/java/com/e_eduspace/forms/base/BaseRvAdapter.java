package com.e_eduspace.forms.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.e_eduspace.forms.adapter.OnHolderClickListener;

import java.util.List;

/**
 * Created by Administrator on 2017-05-11.
 */

public abstract class BaseRvAdapter<E> extends RecyclerView.Adapter {

    private List<E> mList;
    protected Context mContext;
    private OnHolderClickListener<E> mListener;

    public BaseRvAdapter(Context context){

        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = initHolder();
        parent.addView(holder.itemView);
        return holder;
    }

    protected abstract BaseViewHolder initHolder();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseViewHolder) {
            BaseViewHolder vh = (BaseViewHolder) holder;
            vh.setData(mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public BaseRvAdapter<E> setList(List<E> list){
        mList = list;
        notifyDataSetChanged();
        return this;
    }

    public abstract BaseRvAdapter setOnHolderClickListener (OnHolderClickListener<E> listener);
}
