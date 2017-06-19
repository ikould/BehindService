package com.ikould.back.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 权限申请工具类
 * <p>
 * Created by liudong on 2017/5/3.
 */

public class PermissionUtil {

    /**
     * 权限检测和申请
     *
     * @param activity
     * @param permission
     * @param requestCode
     * @return 是否有该权限
     */
    public static boolean checkOrRequestPermission(Activity activity, String permission, int requestCode) {
        // 检测权限
        boolean isPermission = ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
        //如果是android 6.0 以上
        if (Build.VERSION.SDK_INT >= 23 && !isPermission) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission}, requestCode);
        }
        return isPermission;
    }
}
