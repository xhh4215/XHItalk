package com.example.common.app;

import android.app.*;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xhh on 2017/11/2.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnder;

    /**
     * Fragment加载到activity的时候调用的方法
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /**
         * getArguments()fragment之间进行数据传递调用的方法
         * */
        initArgs(getArguments());
    }

    /**
     * 加载Fragment的界面的时候回调的方法
     *
     * @param inflater           加载布局资源id的对象
     * @param container          盛放View的容器
     * @param savedInstanceState 保存的数据  ？
     * @return 加载完成的view控件
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layId = getContentlayoutId();
            /**
             * param 1 加载的布局的id
             * param 2 当前布局加载的父控件
             * param 3 是否在创建的时候就加入到父布局中
             */
            View view = inflater.inflate(layId, container, false);
            initWidget(view);
            mRoot = view;
        } else {
            if (mRoot.getParent() != null) {
                //把当前的root从他的父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    /**
     * 当界面初始化完成的时候回调的方法
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*初始化数据*/
        initData();
    }

    /**
     * 初始化相关的参数
     *
     * @param bundle 参数 Bundle
     * @return 如果参数正确 true  参数错误 false
     */
    protected void initArgs(Bundle bundle) {

    }

    /**
     * 获取加载的界面资源的id 不同的activity加载不同的界面
     */
    protected abstract int getContentlayoutId();

    /**
     * 初始化控件的方法
     */
    protected void initWidget(View view) {
        mRootUnder=  ButterKnife.bind(this,view);
    }

    /**
     * 初始化数据的方法
     */
    protected void initData() {
    }
    /**
     * 返回按键触发时调用
     * return true 我以处理返回逻辑 activity 不用处理
     * return false  我没有处理 activity 处理
     * */
    public boolean onBackPress(){
        return false;
    }

}
