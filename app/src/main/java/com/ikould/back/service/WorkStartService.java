package com.ikould.back.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ikould.back.CoreApplication;
import com.ikould.back.task.AppStartTask;

import java.util.Timer;
import java.util.TimerTask;

/**
 * describe
 * Created by ikould on 2017/6/16.
 */

public class WorkStartService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initWorkThread();
    }

    /**
     * 初始化定时服务
     */
    private void initWorkThread() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                CoreApplication.getInstance().handler.post(() -> AppStartTask.getInstance().wakeApp(WorkStartService.this));
            }
        };
        timer.schedule(timerTask, 1000, 15000);
    }
}
