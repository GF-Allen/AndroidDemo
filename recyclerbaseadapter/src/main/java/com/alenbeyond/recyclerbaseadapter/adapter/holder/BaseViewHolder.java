package com.alenbeyond.recyclerbaseadapter.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by AlenBeyond on 2016/8/20.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public View mRootView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mRootView = itemView;
        initView();
    }

    /**
     * 初始化页面组件
     */
    protected abstract void initView();
}
