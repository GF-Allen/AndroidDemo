package com.alenbeyond.recyclerbaseadapter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alenbeyond.recyclerbaseadapter.adapter.holder.BaseViewHolder;
import com.alenbeyond.recyclerbaseadapter.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by AlenBeyond on 2016/8/20.
 */
public abstract class BaseRecyclerAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> datas;
    protected Context context;
    protected LayoutInflater mInflater;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
        init();
    }

    public BaseRecyclerAdapter(List<T> datas, Context context) {
        this.datas = datas;
        this.context = context;
        init();
    }

    /**
     * 初始化
     */
    protected void init() {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 设置数据
     *
     * @param datas
     */
    public void setDatas(List<T> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    /**
     * 创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public abstract H onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        if (datas == null) {
            new Throwable("请设置数据");
        }
        if (onItemClickListener != null) {
            holder.mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(view, position);
                }
            });
        }
        onBindViewData((H) holder, position);
    }

    /**
     * 给ViewHolder绑定数据
     *
     * @param holder
     * @param position
     */
    public abstract void onBindViewData(H holder, int position);

    @Override
    public int getItemCount() {
        return datas.size();
    }

    protected OnItemClickListener onItemClickListener;

    /**
     * 设置Item点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
