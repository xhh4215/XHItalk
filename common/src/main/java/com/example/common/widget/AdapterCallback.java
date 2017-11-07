package com.example.common.widget;

import android.support.v7.widget.RecyclerView;

/**
 * Created by xhh on 2017/11/7.
 */

public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
