package com.example.networkmonitordemo;

import android.app.Application;

import com.example.networkmonitordemo.utils.T;

/**
 * @Author: 薛志辉
 * @Date: 2019/11/1 14:03
 * @Description: 2531295581
 */
public class App extends Application {
    private static App        instance;
    @Override
    public void onCreate() {
        super.onCreate();
        T.init(this);
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
