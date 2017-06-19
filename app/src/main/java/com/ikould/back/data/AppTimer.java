package com.ikould.back.data;

import org.litepal.crud.DataSupport;

/**
 * app定时实体类
 * <p>
 * Created by ikould on 2017/6/16.
 */

public class AppTimer extends DataSupport {

    private int     id;
    // 是否自动打开
    private boolean isAutoOpen;
    // 是否自动关闭
    private boolean isAutoClose;
    private String  name;
    private String  packName;
    private int     startHour;
    private int     endHour;
    private int     startMin;
    private int     endMin;

    public AppTimer() {
    }

    public AppTimer(int id, boolean isAutoOpen, boolean isAutoClose, String name, String packName, int startHour, int endHour, int startMin, int endMin) {
        this.id = id;
        this.isAutoOpen = isAutoOpen;
        this.isAutoClose = isAutoClose;
        this.name = name;
        this.packName = packName;
        this.startHour = startHour;
        this.endHour = endHour;
        this.startMin = startMin;
        this.endMin = endMin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAutoOpen() {
        return isAutoOpen;
    }

    public void setAutoOpen(boolean autoOpen) {
        isAutoOpen = autoOpen;
    }

    public boolean isAutoClose() {
        return isAutoClose;
    }

    public void setAutoClose(boolean autoClose) {
        isAutoClose = autoClose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public int getEndMin() {
        return endMin;
    }

    public void setEndMin(int endMin) {
        this.endMin = endMin;
    }

    @Override
    public String toString() {
        return "AppTimer{" +
                "id=" + id +
                ", isAutoOpen=" + isAutoOpen +
                ", isAutoClose=" + isAutoClose +
                ", name='" + name + '\'' +
                ", packName='" + packName + '\'' +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                ", startMin=" + startMin +
                ", endMin=" + endMin +
                '}';
    }
}
