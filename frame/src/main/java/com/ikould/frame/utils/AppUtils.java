package com.ikould.frame.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.List;
import java.util.logging.Logger;

//跟App相关的辅助类
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断是否安装了app
     *
     * @param context     上下文
     * @param packageName app的包名
     * @return 是否安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List <PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        boolean isInstalled = false;
        int size = pinfo.size();
        for (int i = 0; i < size; i++) {
            if (packageName.equals(pinfo.get(i).packageName)) {
                isInstalled = true;
                break;
            }
        }
        return isInstalled;
    }

    /**
     * @param context
     * @param packageName
     * @return
     * @description 调用第三方应用
     * @author Joe
     */
    public static boolean startOtherApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = null;
        try {
            intent = packageManager.getLaunchIntentForPackage(packageName);
            if (intent != null)
                context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static boolean isRunning(Context context, String packName) {
        int uid = getPackageUid(context, packName);
        if (uid > 0) {
            boolean rstA = isAppRunning(context, packName);
            boolean rstB = isProcessRunning(context, uid);
            if (rstA || rstB) {
                //指定包名的程序正在运行中
                return true;
            } else {
                //指定包名的程序未在运行中
                return false;
            }
        } else {
            //应用未安装
        }
        return false;
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     * Created by cafeting on 2017/2/4.
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List <ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    //获取已安装应用的 uid，-1 表示未安装此应用或程序异常
    public static int getPackageUid(Context context, String packageName) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                return applicationInfo.uid;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /**
     * 判断某一 uid 的程序是否有正在运行的进程，即是否存活
     * Created by cafeting on 2017/2/4.
     *
     * @param context 上下文
     * @param uid     已安装应用的 uid
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isProcessRunning(Context context, int uid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List <ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() > 0) {
            for (ActivityManager.RunningServiceInfo appProcess : runningServiceInfos) {
                if (uid == appProcess.uid) {
                    return true;
                }
            }
        }
        return false;
    }
}
