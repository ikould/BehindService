package com.ikould.backgroundservice.data;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;

/**
 * App信息实体类
 * <p>
 * Created by liudong on 2016/6/29.
 */
public class AppData {
    private Drawable appIcon;
    private String appName;
    private String appPackageName;
    private ComponentName serviceComponentname;
    private int pid;

    public AppData() {
    }

    public AppData(Drawable appIcon, String appName, String appPackageName, ComponentName serviceComponentname) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.appPackageName = appPackageName;
        this.serviceComponentname = serviceComponentname;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public ComponentName getServiceComponentname() {
        return serviceComponentname;
    }

    public void setServiceComponentname(ComponentName serviceComponentname) {
        this.serviceComponentname = serviceComponentname;
    }

    @Override
    public String toString() {
        return "AppData{" +
                "appIcon=" + appIcon +
                ", appName='" + appName + '\'' +
                ", appPackageName='" + appPackageName + '\'' +
                ", serviceComponentname=" + serviceComponentname +
                '}';
    }
}
