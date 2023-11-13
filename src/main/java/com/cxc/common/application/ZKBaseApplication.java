package com.cxc.common.application;


import android.content.Context;

import com.tencent.mmkv.MMKV;

import androidx.multidex.MultiDexApplication;


public abstract class ZKBaseApplication extends MultiDexApplication {

    private static ZKBaseApplication mInstance;

    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        MMKV.initialize(this);
        initModuleApp();
        initModuleAppData();
        initTask();
    }


    public static ZKBaseApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mContext;
    }


    public abstract void initModuleApp();

    public abstract void initModuleAppData();

    //初始化启动时的各种task
    private void initTask() {

    }

    /**
     * 初始化涉及用到隐私相关内容的第三方（必须要同意隐私条款了才能初始化）
     */
    public void initOtherTask() {
        //确定接入极光推送后再开启
//        if (!GlobalConstant.isRelease) {
//            JPushInterface.setDebugMode(true);
//        }
//        JPushInterface.init(this);

    }

}
