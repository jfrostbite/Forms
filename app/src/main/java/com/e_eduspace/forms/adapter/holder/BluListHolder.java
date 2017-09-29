package com.e_eduspace.forms.adapter.holder;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.e_eduspace.forms.adapter.OnHolderClickListener;
import com.e_eduspace.forms.base.BaseViewHolder;

/**
 * Created by Administrator on 2017-05-11.
 */

public class BluListHolder extends BaseViewHolder<BluetoothDevice> implements View.OnClickListener {

    private TextView tvName;
    private BluetoothDevice mDevice;
    private OnHolderClickListener mListener;

    public BluListHolder(Context context, @LayoutRes int rid) {
        super(context, rid);
    }

    @Override
    protected void initListener() {
        tvName.setOnClickListener(this);
    }

    @Override
    protected void initView() {
//        tvName = (TextView) itemView.findViewById(R.id.tv_item_name);
    }

    @Override
    public void setData(BluetoothDevice device) {

        mDevice = device;
        tvName.setText(device.getName());
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public BaseViewHolder setOnHolderClickListener (OnHolderClickListener listener) {

        mListener = listener;
        return this;
    }
}
