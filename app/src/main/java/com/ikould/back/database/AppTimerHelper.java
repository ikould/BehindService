package com.ikould.back.database;

import android.content.ContentValues;
import android.util.Log;

import com.ikould.back.data.AppTimer;

import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * AppTimer 数据库帮助类
 * <p>
 * Created by ikould on 2017/6/7.
 */

public class AppTimerHelper {

    // ======== 单例 ==========
    private static AppTimerHelper instance;

    public static AppTimerHelper getInstance() {
        if (instance == null)
            synchronized (AppTimerHelper.class) {
                if (instance == null)
                    instance = new AppTimerHelper();
            }
        return instance;
    }

    private AppTimerHelper() {
    }

    // ======== 操作 =======

    /**
     * 插入
     */
    public void insert(AppTimer appTimer) {
        // 当前业务 -> 若有 playId path 相同的则不插入
        AppTimer sameAppTimer = DataSupport.where("packName = ?", appTimer.getPackName()).findFirst(AppTimer.class);
        if (sameAppTimer == null) {
            appTimer.save();
        }
    }


    /**
     * 通过包名获取AppTimer
     *
     * @param packName 包名
     * @return
     */
    public AppTimer findByPackName(String packName) {
        return DataSupport.where("packName = ?", packName).findFirst(AppTimer.class);
    }

    /**
     * 刷新
     *
     * @param appTimer
     */
    public void update(AppTimer appTimer) {
        ContentValues values = new ContentValues();
        values.put("isAutoOpen", appTimer.isAutoOpen() ? "1" : "0");
        values.put("isAutoClose", appTimer.isAutoClose() ? "1" : "0");
        values.put("name", appTimer.getName());
        values.put("packName", appTimer.getPackName());
        values.put("startHour", appTimer.getStartHour());
        values.put("endHour", appTimer.getEndHour());
        values.put("startMin", appTimer.getStartMin());
        values.put("endMin", appTimer.getEndMin());
        DataSupport.update(AppTimer.class, values, appTimer.getId());
    }

    /**
     * 查询所有
     *
     * @return 查询结果
     */
    public List <AppTimer> findAll() {
        return DataSupport.findAll(AppTimer.class);
    }
}
