package com.ikould.back.task;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.ikould.frame.utils.ToastUtils;

import java.util.List;

public class GPSTask {

    // ==== 单例 ====
    private static volatile GPSTask singleton;

    private GPSTask() {
    }

    public static GPSTask getInstance() {
        if (singleton == null) {
            synchronized (GPSTask.class) {
                if (singleton == null) {
                    singleton = new GPSTask();
                }
            }
        }
        return singleton;
    }

    // ==== 操作 ====
    private double latitude  = 0.0;
    private double longitude = 0.0;
    private LocationManager locationManager;
    private Context         context;
    // 位置提供器
    private String          locationProvider;

    public LocationManager getLocationManager() {
        return locationManager;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        findUsefulProvider();
        if (!TextUtils.isEmpty(locationProvider)) {
            getLocation();
        }
    }

    /**
     * 获取有用的位置提供器
     */
    public void findUsefulProvider() {
        List <String> providers = locationManager.getProviders(true);
        for (String str : providers) {
            Log.d("GPSTask", "findUsefulProvider: str = " + str);
        }
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Log.d("GPSTask", "findUsefulProvider: 没有可用的位置提供器");
        }
    }

    /**
     * 获取定位信息
     */
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            showLocation(location);
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
    }

    private void showLocation(Location location) {
        ToastUtils.show(context, "location.X = " + location.getLatitude() + " location,Y = " + location.getLongitude() + " location,Speed = " + location.getSpeed());
        Log.d("GPSTask", "showLocation: location.X = " + location.getLatitude() + " location,Y = " + location.getLongitude() + " location,Speed = " + location.getSpeed());
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
            Log.d("GPSTask", "onStatusChanged: provider = " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("GPSTask", "onProviderEnabled: provider = " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("GPSTask", "onProviderDisabled: provider = " + provider);

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            ToastUtils.show(context, "location.X = " + location.getLatitude() + " location,Y = " + location.getLongitude() + " location,Speed = " + location.getSpeed());
            Log.d("GPSTask", "onLocationChanged: location.X = " + location.getLatitude() + " location,Y = " + location.getLongitude() + " location,Speed = " + location.getSpeed());
        }
    };
}