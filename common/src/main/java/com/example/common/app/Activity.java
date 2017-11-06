package com.example.common.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * author xhh on 2017/11/2.
 * 公共的activity提供统一的操作
 */

public abstract class Activity extends AppCompatActivity {
    /**
     * 初始化界面
     */
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在界面未初始化之前调用的初始化窗口
        initWindows();

        if (initArgs(getIntent().getExtras())) {
            // 得到界面Id并设置到Activity界面中
            int layId = getContentLayoutId();
            setContentView(layId);

            initWdget();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 初始化窗口
     */
    protected void initWindows() {

    }

    /**
     * 初始化相关的参数
     *
     * @param bundle 参数 Bundle
     * @return 如果参数正确 true  参数错误 false
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 获取加载的界面资源的id 不同的activity加载不同的界面
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件的方法
     */
    protected void initWdget() {
       ButterKnife.bind(this);
    }

    /**
     * 初始化数据的方法
     */
    protected void initData() {
    }

    ;

    /**
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面导航返回时 finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        //得到当前activity下的所有的Fragment
        @SuppressLint("RestrictedApi")
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        /*判断是不是空的*/
        if (fragments != null && fragments.size() > 0) {
           /*便利所有的activity*/
            for (android.support.v4.app.Fragment fragment : fragments) {
                /*判断是不是我们能处理的Fragment*/
                if (fragment instanceof com.example.common.app.Fragment) {
                     /*判断是否拦截了返回键*/
                    if (((com.example.common.app.Fragment) fragment).onBackPress()) {
                           /*有直接return*/
                        return;
                    }

                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
