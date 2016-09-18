package com.ikould.frame.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * 包相关的查询工具
 * <p>
 * Created by liudong on 2016/7/28.
 */
public class PackageUtil {

    /**
     * 判断当前应用是否在前台
     *
     * @param context
     * @return
     */
    public static boolean isCurrentAppOnForeground(Context context) {
        String currentPackageName = context.getPackageName();
        return isPackageOnForeground(context, currentPackageName);
    }

    /**
     * 判断当前应用是否在前台
     *
     * @param context
     * @return
     */
    public static boolean isPackageOnForeground(Context context, String pckName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String packageName = info.topActivity.getPackageName();
        if (pckName.equals(packageName)) {
            return true;
        }
        return false;
    }
}
