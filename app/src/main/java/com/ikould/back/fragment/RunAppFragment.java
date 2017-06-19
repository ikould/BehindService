package com.ikould.back.fragment;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ikould.back.CoreApplication;
import com.ikould.back.R;
import com.ikould.back.adapter.RvMainAdapter;
import com.ikould.back.data.AppData;
import com.ikould.frame.annotation.InjectView;
import com.ikould.frame.fragment.BaseFragment;
import com.ikould.frame.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 有服务的APP
 * <p>
 * Created by liudong on 2016/8/14.
 */
public class RunAppFragment extends BaseFragment {
    @InjectView(R.id.rv_runApps)
    private RecyclerView mRunApps;

    private RvMainAdapter adapter;

    @Override
    public void onBaseFragmentCreate(Bundle savedInstanceState) {
        initView();
        Log.d("liudong", "new RunAppFragment");
        updateMsgs();
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_service;
    }

    /**
     * 初始化View
     */
    private void initView() {
        updateAllRunApps();
        initRecycleView();
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        RvMainAdapter.RecycleListener recycleListener = (v, position, isChecked) -> {
            Log.d("initRecyclerView", position + "被点击");
            CoreApplication.getInstance().savePackageToFile(CoreApplication.getInstance().allRunApps.get(position).getAppPackageName(), isChecked);
            ToastUtils.show(getActivity(), CoreApplication.getInstance().allRunApps.get(position).getAppName() + " 成功添加到待杀进程中");
        };
        adapter = new RvMainAdapter(CoreApplication.getInstance(), CoreApplication.getInstance().allRunApps, recycleListener, "");
        mRunApps.setLayoutManager(new LinearLayoutManager(CoreApplication.getInstance()));
        mRunApps.setAdapter(adapter);
    }

    /**
     * 找到所有正在运行的服务
     */
    private void updateAllRunApps() {
        CoreApplication.getInstance().allRunApps = null;
        CoreApplication.getInstance().allRunApps = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();
        //获取正在运行的服务
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> mServiceInfos = activityManager.getRunningServices(30);
        for (ActivityManager.RunningServiceInfo mServiceInfo : mServiceInfos) {
            String packageName = mServiceInfo.process;
            //通过包名获取服务的App信息
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                if (applicationInfo != null) {
                    Drawable appIcon = applicationInfo.loadIcon(packageManager);
                    String appName = applicationInfo.loadLabel(packageManager).toString();
                    ComponentName serviceComponentname = mServiceInfo.service;
                    AppData appData = new AppData(appIcon, appName, packageName, serviceComponentname);
                    appData.setPid(mServiceInfo.pid);
                    CoreApplication.getInstance().allRunApps.add(appData);
                }
                Log.d("FindService", packageName);
            } catch (PackageManager.NameNotFoundException e) {
                Log.d("FindService", packageName + ":当前包名有问题");
            }
        }
        CoreApplication.getInstance().allRunApps = getNoSameAppdata(CoreApplication.getInstance().allRunApps);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            adapter.notifyItemRangeChanged(0, CoreApplication.getInstance().allRunApps.size());
        }
    }

    /**
     * 去除相同的包名
     *
     * @param appDatas
     * @return
     */
    private List<AppData> getNoSameAppdata(List<AppData> appDatas) {
        //去除相同的包名
        List<AppData> listTemp = new ArrayList<>();
        for (int i = 0; i < appDatas.size(); i++) {
            boolean isSame = false;
            for (int j = 0; j < i; j++) {
                if (appDatas.get(i).getAppPackageName().equals(appDatas.get(j).getAppPackageName())) {
                    isSame = true;
                }
            }
            if (!isSame) {
                listTemp.add(appDatas.get(i));
            }
        }
        return listTemp;
    }

    private Subscription updateSub;

    /**
     * 每隔5秒刷新
     */
    public void updateMsgs() {
        unSubscribe(updateSub);
        updateSub = Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(along -> {
                    updateAllRunApps();
                }, throwable -> {
                    Log.d("liudong", throwable.toString());
                });
    }

    private void unSubscribe(Subscription sub) {
        if (sub != null) {
            sub.unsubscribe();
            sub = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMsgs();
    }

    @Override
    public void onPause() {
        super.onPause();
        unSubscribe(updateSub);
    }
}
