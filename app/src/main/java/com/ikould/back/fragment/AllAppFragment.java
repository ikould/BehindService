package com.ikould.back.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
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

/**
 * App列表
 * <p>
 * Created by liudong on 2016/8/14.
 */
public class AllAppFragment extends BaseFragment {
    @InjectView(R.id.rv_allApps)
    private RecyclerView mAllApps;

    private RvMainAdapter adapter;

    @Override
    public void onBaseFragmentCreate(Bundle savedInstanceState) {
        Log.d("AllAppFragment", "new AllAppFragment");
        initView();
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_home;
    }

    /**
     * View初始化
     */
    private void initView() {
        getAllApps();
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllApps();
    }

    /**
     * RecycleView初始化
     */
    private void initRecyclerView() {
        RvMainAdapter.RecycleListener recycleListener = (v, position, isChecked) -> {
            Log.d("initRecyclerView", position + "被点击");
            //SuUtil.kill(appDatas.get(position).getAppPackageName());
            CoreApplication.getInstance().savePackageToFile(CoreApplication.getInstance().allApps.get(position).getAppPackageName(), isChecked);
            if (isChecked)
                ToastUtils.show(getActivity(), CoreApplication.getInstance().allApps.get(position).getAppName() + " 成功添加到待杀进程中");
        };
        adapter = new RvMainAdapter(CoreApplication.getInstance(), CoreApplication.getInstance().allApps, recycleListener, null);
        mAllApps.setLayoutManager(new LinearLayoutManager(CoreApplication.getInstance()));
        mAllApps.setAdapter(adapter);
    }

    /**
     * 获取所有的APP信息
     */
    private void getAllApps() {
        CoreApplication.getInstance().allApps = new ArrayList<>();
        findAppInfo();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            adapter.notifyItemRangeChanged(0, CoreApplication.getInstance().allApps.size());
        }
    }

    /**
     * 获取符合应用的APP信息
     */
    private void findAppInfo() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> infoList = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        if (infoList != null && infoList.size() > 0) {
            for (ResolveInfo info : infoList) {
                if (info != null) {
                    String pkgName = info.activityInfo.packageName;
                    String name = info.activityInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                    ActivityInfo activityInfo = info.activityInfo;
                    Drawable drawable = getActivity().getPackageManager().getDrawable(pkgName, activityInfo.getIconResource(), activityInfo.applicationInfo);
                    AppData appData = new AppData();
                    appData.setAppName(name);
                    appData.setAppIcon(drawable);
                    appData.setAppPackageName(pkgName);
                    CoreApplication.getInstance().allApps.add(appData);
                    Log.d("ResolveInfo", appData.toString() + "\n");
                }
            }
        }
        CoreApplication.getInstance().allApps = getNoSameAppdata(CoreApplication.getInstance().allApps);
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
}
