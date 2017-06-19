package com.ikould.frame.application;

import android.app.Application;
import android.util.Log;

import com.ikould.frame.BuildConfig;
import com.ikould.frame.handler.CrashFileSaveListener;
import com.ikould.frame.handler.CrashHandler;

import org.litepal.LitePal;
import org.litepal.parser.LitePalAttr;

import java.util.List;

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
        // 数据库初始化
        LitePal.initialize(this);
        List<String> ss = LitePalAttr.getInstance().getClassNames();
        for (String s : ss) {
            Log.d("CoreApplication", "onBaseCreate: s = " + s);
        }
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
