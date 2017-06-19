package com.ikould.back.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.ikould.back.CoreApplication;
import com.ikould.back.utils.Constants;
import com.ikould.frame.utils.FileUtils;

import java.io.File;

/**
 * App相关配置
 * <p>
 * Created by liudong on 2016/8/12.
 */
public class AppConfig {
    /**
     * 常量
     */
    private static AppConfig         fInstance;
    private        SharedPreferences sharedPrefs;
    /**
     * 根目录
     **/
    private        String            ROOT_PATH;
    /**
     * 缓存路径
     */
    public         String            CACHE_DIR;
    /**
     * WEB缓存
     */
    public         String            CACHE_WEB_DIR;
    /**
     * web database
     */
    public         String            CACHE_WEB_DATABASE;
    /**
     * web定位
     */
    public         String            CACHE_WEB_LOCAL_DIR;
    /**
     * APP根目录
     **/
    public         String            APP_PATH;
    /**
     * 下载地址
     */
    public         String            DOWNLOAD_DIR;
    /**
     * 其它资料
     */
    public         String            OTHER_DIR;

    private void initDir() {
        // 根目录
        ROOT_PATH = FileUtils.getRootPath() + File.separator + "Ikould";
        FileUtils.initDirctory(ROOT_PATH);
        // 应用根目录
        APP_PATH = ROOT_PATH + File.separator + "BackgroundService";
        FileUtils.initDirctory(APP_PATH);
        // 缓存
        CACHE_DIR = APP_PATH + File.separator + "Cache";
        FileUtils.initDirctory(CACHE_DIR);
        // web缓存
        CACHE_WEB_DIR = CACHE_DIR + File.separator + "WebCache";
        FileUtils.initDirctory(CACHE_DIR);
        // web local
        CACHE_WEB_LOCAL_DIR = CACHE_DIR + File.separator + "WebLocal";
        FileUtils.initDirctory(CACHE_WEB_LOCAL_DIR);
        // database
        CACHE_WEB_DATABASE = CACHE_DIR + File.separator + "WebDatabase";
        FileUtils.initDirctory(CACHE_WEB_DATABASE);
        // 下载
        DOWNLOAD_DIR = APP_PATH + File.separator + "DownLoad";
        FileUtils.initDirctory(DOWNLOAD_DIR);
        //其他资料
        OTHER_DIR = APP_PATH + File.separator + "Other";
        FileUtils.initDirctory(OTHER_DIR);
    }


    public static AppConfig getInstance() {
        if (fInstance == null) {
            fInstance = new AppConfig();
        }
        return fInstance;
    }

    private AppConfig() {
        sharedPrefs = CoreApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);// .MODE_PRIVATE);
        initDir();
    }

    /**
     * 设置是否打开自动清除服务
     */
    public void setAutoClear(boolean isAuto) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(Constants.AUTO_CLEAR, isAuto);
        editor.apply();
    }

    public boolean getAutoClear() {
        return sharedPrefs.getBoolean(Constants.AUTO_CLEAR, false);
    }

    /**
     * 设置是否开启广告
     */
    public void setCloseAdvert(boolean isClose) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(Constants.CLOSE_ADVERT, isClose);
        editor.apply();
    }

    public boolean getCloseAdvert() {
        return sharedPrefs.getBoolean(Constants.CLOSE_ADVERT, false);
    }

    /**
     * 是否插入数据库
     */
    public void setIsFirstAppTimer(boolean isFirstAppTimer){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(Constants.FIRST_APP_TIMER, isFirstAppTimer);
        editor.apply();
    }

    public boolean getIsFirstAppTimer() {
        return sharedPrefs.getBoolean(Constants.FIRST_APP_TIMER, true);
    }
}
