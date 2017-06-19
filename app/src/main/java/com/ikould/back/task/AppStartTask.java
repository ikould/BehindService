package com.ikould.back.task;

import android.content.Context;
import android.util.Log;

import com.ikould.back.CoreApplication;
import com.ikould.back.config.AppConfig;
import com.ikould.back.data.AppTimer;
import com.ikould.back.database.AppTimerHelper;
import com.ikould.frame.utils.AppUtils;

import java.util.Calendar;

/**
 * App启动任务类
 * <p> 当前只处理一条数据 </p>
 */
public class AppStartTask {

    private static final String WAKE_PACKAGE_NAME = "com.alibaba.android.rimet";

    private static AppStartTask singleton;
    private        AppTimer     openAppTimer;

    private AppStartTask() {
        initTime();
    }

    public static AppStartTask getInstance() {
        if (singleton == null) {
            synchronized (AppStartTask.class) {
                if (singleton == null) {
                    singleton = new AppStartTask();
                }
            }
        }
        return singleton;
    }

    /**
     * 设置时间
     */
    public void initTime() {
        boolean isFirst = AppConfig.getInstance().getIsFirstAppTimer();
        if (isFirst) {
            insertDefaultAppTimer();
            AppConfig.getInstance().setIsFirstAppTimer(false);
        }
        openAppTimer = AppTimerHelper.getInstance().findByPackName(WAKE_PACKAGE_NAME);
        Log.d("AppStartTask", "initTime: openAppTimer = " + openAppTimer);
    }

    /**
     * 获取打开的AppTimer
     *
     * @return
     */
    public AppTimer getOpenAppTimer() {
        return openAppTimer;
    }

    /**
     * 获取AppTimer
     *
     * @return
     */
    public AppTimer getCloseAppTimer() {
        return null;
    }

    /**
     * 更新数据
     */
    public void updateInfo() {
        AppTimerHelper.getInstance().update(openAppTimer);
    }

    /**
     * 插入默认信息
     * 钉钉 9:00 - 9:30 开启
     */
    private void insertDefaultAppTimer() {
        AppTimer appTimer = new AppTimer();
        appTimer.setAutoClose(false);
        appTimer.setName("钉钉");
        appTimer.setPackName(WAKE_PACKAGE_NAME);
        appTimer.setAutoOpen(true);
        appTimer.setStartHour(9);
        appTimer.setEndHour(9);
        appTimer.setStartMin(0);
        appTimer.setEndMin(30);
        AppTimerHelper.getInstance().insert(appTimer);
    }

    /**
     * 唤醒APP
     *
     * @param context
     */
    public void wakeApp(Context context) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        Log.d("AppStartTask", "onReceive: hour = " + hour + " min = " + min + " second = " + second);
        Log.d("AppStartTask", "wakeApp: openAppTimer = " + openAppTimer.toString());
        if (openAppTimer.isAutoOpen()
                && hour > openAppTimer.getStartHour() && hour < openAppTimer.getEndHour()
                && min > openAppTimer.getStartMin() && min < openAppTimer.getEndMin()) {
            CoreApplication.getInstance().handler.post(() -> {
                Log.d("AppStartTask", "wakeApp: handler");
                // 是否已经唤醒
                if (!AppUtils.isRunning(context, openAppTimer.getPackName())) {
                    Log.d("AppStartTask", "wakeApp: ");
                    // 唤醒APP
                    AppUtils.startOtherApp(context, openAppTimer.getPackName());
                }
            });
        }
    }
}