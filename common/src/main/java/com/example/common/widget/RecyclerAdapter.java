package com.example.common.widget;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xhh on 2017/11/7.
 */

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>> implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data> {
    private final List<Data> mDataList;
    private AdapterListener listener;
//    构造器模块
    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> mlistener) {
        this(new ArrayList<Data>(), mlistener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> mlistener) {
        this.mDataList = dataList;
        this.listener = mlistener;
    }

    /**
     * 复写默认的布局类型返回
     *
     * @param position 坐标
     * @return 类型 其实复写之后返回的都是xml文件的id
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 获得布局类型
     *
     * @param position 坐标
     * @param data     当前的数据
     * @return xml文件的id 用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /**
     * 创建了一个viewholder
     *
     * @param parent   RecyclerView
     * @param viewType 界面的数据的显示的类型 约定为xml布局的id
     * @return viewHolder
     */
    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        /*布局布局加载器的创建*/
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        /*把xml文件id为viewType的文件初始化为一个root*/
        View root = inflater.inflate(viewType, parent, false);
        /*通过子类必须实现的一个方法 得到一个ViewHolder*/
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);
         /*通过tag获取ViewHolder简化操作*/
        root.setTag(R.id.tag_recycler_holder);
        /*点击事件*/
        root.setOnClickListener(this);
        /*长按点击事件*/
        root.setOnLongClickListener(this);

        /*进行界面的绑定*/
        holder.unbinder = ButterKnife.bind(holder, root);
        /*绑定callback*/
        holder.callback = this;
        return holder;
    }

    /**
     * 得到一个新的ViewHolder
     *
     * @param root     根布局
     * @param viewType 布局的类型，就是xml的id
     * @return viewHolder
     */
    public abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    /**
     * 绑定数据到一个holder上
     *
     * @param holder   holder
     * @param position 数据的位置信息
     */
    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        /*获取要绑定的数据*/
        Data data = mDataList.get(position);
       /*数据绑定的具体实现*/
        holder.bind(data);

    }

    /**
     * 获取当前的集合的数据量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 添加数据到集合当中
     *
     * @param data
     */
    public void add(Data data) {
        mDataList.add(data);
        /*从插入的位置进行刷新*/
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一组集合数据
     *
     * @param dataList
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeChanged(startPos, dataList.length);
        }
    }

    /**
     * 插入一组集合数据
     *
     * @param dataList
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeChanged(startPos, dataList.size());
        }
    }

    /**
     * 删除集合的数据
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合 其中包括清空
     *
     * @param dataList
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0)
            return;
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public boolean onLongClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (this.listener != null) {
//            得到ViewHodler当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
//            回调方法
            listener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (this.listener != null) {
//            得到ViewHodler当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();

//            回调方法
            listener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    //   设置适配器的监听
    public void setListener(AdapterListener<Data> adapterListener) {
        this.listener = adapterListener;
    }

    /**
     * 自定义的监听器
     *
     * @param <Data> 范型
     */
    public interface AdapterListener<Data> {
        //        当cell点击的时候触发
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        //        当cell长按的时候触发
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    /**
     * 自定义的ViewHolder
     *
     * @param <Data> 范型数据
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        protected Data mData;
        private AdapterCallback<Data> callback;
        private Unbinder unbinder;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(Data data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 当触发绑定数据的时候 的回调 必须复写
         *
         * @param data 绑定的数据
         */
        protected abstract void onBind(Data data);

        /*Holder 自己对自己对应的data进行更新*/
        public void updateData(Data data) {
            if (this.callback != null) {
                this.callback.update(data, this);
            }
        }
    }
}
