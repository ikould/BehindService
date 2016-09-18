package com.ikould.backgroundservice.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ikould.backgroundservice.CoreApplication;
import com.ikould.backgroundservice.activity.MainActivity;
import com.ikould.backgroundservice.data.AppData;
import com.ikould.frame.utils.PackageUtil;
import com.ikould.frame.utils.SuUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * 后台运行杀进程
 * <p>判断当前应用是否在前台运行，再选择是否杀死此线程
 * Created by liudong on 2016/7/1.
 */
public class BackService extends Service {
    private String TAG = "BackService";

    private Notification note = null;
    private PendingIntent pendingIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BackService", "onCreate");
        //每隔1分钟杀进程
        Observable.interval(1000, 60000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .retry()
                .subscribe(aLong -> {
                    killAllSelectApps();
                }, throwable -> {
                    Log.d(TAG, throwable.toString());
                });
        CoreApplication.getInstance().killAppsHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                killAllSelectApps();
            }
        };
    }

    /**
     * 杀所有正在运行的进程
     */
    private void killAllSelectApps() {
        if (CoreApplication.getInstance().allRunApps != null) {
            for (int i = 0; i < CoreApplication.getInstance().allRunApps.size(); i++) {
                AppData appData = CoreApplication.getInstance().allRunApps.get(i);
                if (appData != null && !PackageUtil.isPackageOnForeground(this, appData.getAppPackageName())) {
                    if (isKilled(appData.getAppPackageName())) {
                        SuUtil.kill(appData.getAppPackageName());
                    }
                }
            }
        }
    }

    /**
     * 是否和收藏的匹配
     *
     * @param strName 传入包名
     * @return
     */
    private boolean isKilled(String strName) {
        for (int i = 0; i < CoreApplication.getInstance().saveApps.size(); i++) {
            if (strName.equals(CoreApplication.getInstance().saveApps.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = intent.getStringExtra("name");
        Log.w(TAG, "name:" + name);
        Notification.Builder builder = new Notification.Builder(this).setTicker("显示于屏幕顶端状态栏的文本");
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 100, i, PendingIntent.FLAG_UPDATE_CURRENT);
        note = builder.setContentIntent(pendingIntent).setContentTitle("title").setContentText("text").getNotification();
        note.flags = Notification.FLAG_ONGOING_EVENT;
        startForeground(1, note);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CoreApplication.getInstance().killAppsHandler = null;
    }
}
