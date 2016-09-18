package com.ikould.backgroundservice.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ikould.backgroundservice.R;
import com.ikould.backgroundservice.config.AppConfig;
import com.ikould.backgroundservice.fragment.AllAppFragment;
import com.ikould.backgroundservice.fragment.RunAppFragment;
import com.ikould.backgroundservice.fragment.SettingFragment;
import com.ikould.backgroundservice.service.BackService;
import com.ikould.frame.activity.BaseActivity;
import com.ikould.frame.annotation.InjectView;
import com.ikould.frame.utils.RootUtil;
import com.ikould.frame.widget.StatusButton;

import cn.waps.AppConnect;

public class MainActivity extends BaseActivity implements View.OnClickListener, SettingFragment.AdvertListener {
    public static String TAG = "MainActivityTag";

    @InjectView(R.id.main_fragments)
    private FrameLayout mFragments;
    @InjectView(R.id.tv_main_allApp)
    private StatusButton mAllApps;
    @InjectView(R.id.tv_main_run)
    private StatusButton mMainRun;
    @InjectView(R.id.tv_main_setting)
    private StatusButton mMainSet;
    @InjectView(R.id.miniAdLinearLayout)
    LinearLayout mMiniAdLinearLayout;

    private StatusButton[] tableLists;
    private int currentIndex;//当前table下标

    @Override
    public void onBaseCreate(Bundle savedInstanceState) {
        setLayoutId(R.layout.activity_main);
        initConfig();
        //初始化View
        initView();
        //开启前台服务
        if (AppConfig.getInstance().getAutoClear()) {
            startPreService();
        }
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        //申请Root权限
        RootUtil.getSystemCommand(getApplicationContext());
        //广告Open
        if (!AppConfig.getInstance().getCloseAdvert()) {
            advertOpen();
        }
    }

    /**
     * 开启前台服务
     */
    private void startPreService() {
        Intent intent = new Intent(this, BackService.class);
        startService(intent);
    }

    /**
     * 初始化View相关操作
     */
    private void initView() {
        initTable();
        initListener();
        //初始化Fragment
        replaceFragment(R.id.main_fragments, new RunAppFragment(), null);
        selectTable(1);
        currentIndex = 1;
    }

    /**
     * 初始化Table
     */
    private void initTable() {
        tableLists = new StatusButton[]{mAllApps, mMainRun, mMainSet};
    }

    /**
     * 选择table下标
     */
    private void selectTable(int index) {
        for (int i = 0; i < tableLists.length; i++) {
            if (i == index) {
                tableLists[i].setChecked(true);
                tableLists[i].setTextColor(getResources().getColor(R.color.black));
            } else {
                tableLists[i].setChecked(false);
                tableLists[i].setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    /**
     * 广告处理
     * 采用2种广告：
     * 1.自定义广告 (互动广告可替代)
     * 2.分享广告
     * 3.新卸载广告接口（待定）
     */
    private void advertOpen() {
        //1.互动广告接口
       /* LinearLayout adlayout = (LinearLayout) findViewById(R.id.ll_ad);
        AppConnect.getInstance(this).showBannerAd(this, adlayout);*/

        //2.分享广告接口
        //调用以下接口打开分享广告墙：
        //AppConnect.getInstance(this).showShareOffers(this);

        //3.迷你广告
        //设置迷你广告背景颜色
        AppConnect.getInstance(this).setAdBackColor(Color.argb(50, 120, 240, 120));
        //设置迷你广告广告诧颜色
        AppConnect.getInstance(this).setAdForeColor(Color.BLACK);
        //若未设置以上两个颜色，则默认为黑底白字
        AppConnect.getInstance(this).showMiniAd(this, mMiniAdLinearLayout, 10); //默认 10 秒切换一次广告
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initListener() {
        mAllApps.setOnClickListener(this);
        mMainSet.setOnClickListener(this);
        mMainRun.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main_allApp:
                if (currentIndex != 0) {
                    currentIndex = 0;
                    replaceFragment(R.id.main_fragments, new AllAppFragment(), null);
                    selectTable(currentIndex);
                }
                break;
            case R.id.tv_main_run:
                if (currentIndex != 1) {
                    currentIndex = 1;
                    replaceFragment(R.id.main_fragments, new RunAppFragment(), null);
                    selectTable(currentIndex);
                }
                break;
            case R.id.tv_main_setting:
                if (currentIndex != 2) {
                    currentIndex = 2;
                    replaceFragment(R.id.main_fragments, new SettingFragment(), null);
                    selectTable(currentIndex);
                }
                break;
        }
        Log.d("liudong", "currentIndex:" + currentIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //广告Close
        AppConnect.getInstance(this).close();
    }

    @Override
    public void closeAdvert(boolean isClose) {
        if (isClose) {
            mMiniAdLinearLayout.setVisibility(View.GONE);
        } else {
            mMiniAdLinearLayout.setVisibility(View.VISIBLE);
        }
    }
}
