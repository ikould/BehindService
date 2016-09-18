package com.ikould.frame.application;

import android.app.Application;

import com.ikould.frame.BuildConfig;
import com.ikould.frame.handler.CrashFileSaveListener;
import com.ikould.frame.handler.CrashHandler;

/**
 * Application基类
 * <p>
 * Created by liudong on 2016/8/22.
 */
public abstract class BaseApplication extends Application implements CrashFileSaveListener {
    protected CrashHandler crashHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 设置默认异常处理Handler
         */
        if (!BuildConfig.DEBUG) {
            crashHandler = CrashHandler.getInstance(this);
            crashHandler.init(getApplicationContext());
        }
        onBaseCreate();
    }

    protected abstract void onBaseCreate();

    @Override
    public abstract void crashFileSaveTo(String filePath);
}
