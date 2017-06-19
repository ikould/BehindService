package com.ikould.back.task;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import java.util.Date;

public class ModelGPS {

    private static volatile ModelGPS        singleton;
    private                 LocationManager locationManager;
    private                 boolean         hasAddTestProvider;

    private ModelGPS() {
    }

    public static ModelGPS getInstance() {
        if (singleton == null) {
            synchronized (ModelGPS.class) {
                if (singleton == null) {
                    singleton = new ModelGPS();
                }
            }
        }
        return singleton;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        Log.d("ModelGPS", "init: ");
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Android 6.0 以下：是否开启【允许模拟位置】
        hasAddTestProvider = false;
        boolean canMockPosition = (Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0)
                || Build.VERSION.SDK_INT > 22;
        Log.d("ModelGPS", "init: canMockPosition = " + canMockPosition);
        if (canMockPosition && !hasAddTestProvider) {
            try {
                String providerStr = LocationManager.GPS_PROVIDER;
                LocationProvider provider = locationManager.getProvider(providerStr);
                Log.d("ModelGPS", "init: provider = " + provider);
                if (provider != null) {
                    locationManager.addTestProvider(
                            provider.getName()
                            , provider.requiresNetwork()
                            , provider.requiresSatellite()
                            , provider.requiresCell()
                            , provider.hasMonetaryCost()
                            , provider.supportsAltitude()
                            , provider.supportsSpeed()
                            , provider.supportsBearing()
                            , provider.getPowerRequirement()
                            , provider.getAccuracy());
                } else {
                    locationManager.addTestProvider(
                            providerStr
                            , true, true, false, false, true, true, true
                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                }
                locationManager.setTestProviderEnabled(providerStr, true);
                locationManager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
                // 模拟位置可用
                hasAddTestProvider = true;
                Log.d("ModelGPS", "init: 模拟位置可用");
                canMockPosition = true;
                // 模拟位置
                new RunnableMockLocation().run();
            } catch (SecurityException e) {
                Log.d("ModelGPS", "init: e = " + e);
                canMockPosition = false;
            }
        }
    }

    /**
     * 停止模拟位置，以免启用模拟数据后无法还原使用系统位置
     * 若模拟位置未开启，则removeTestProvider将会抛出异常；
     * 若已addTestProvider后，关闭模拟位置，未removeTestProvider将导致系统GPS无数据更新；
     */
    public void stopMockLocation() {
        if (hasAddTestProvider) {
            try {
                locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                // 若未成功addTestProvider，或者系统模拟位置已关闭则必然会出错
            }
            hasAddTestProvider = false;
        }
    }

    private float change;

    private class RunnableMockLocation implements Runnable {
        @Override
        public void run() {
            Log.d("ModelGPS", "RunnableMockLocation run: 开始模拟位置");
            while (true) {
                try {
                    change++;
                    if (change > 100)
                        change = 0;
                    Thread.sleep(1000);
                    if (!hasAddTestProvider) {
                        continue;
                    }
                    try {
                        // 模拟位置（addTestProvider成功的前提下）
                        String providerStr = LocationManager.GPS_PROVIDER;
                        Location mockLocation = new Location(providerStr);
                        mockLocation.setLatitude(22 + change / 5);   // 维度（度）
                        mockLocation.setLongitude(113 + change / 5);  // 经度（度）
                        mockLocation.setAltitude(30);    // 高程（米）
                        mockLocation.setBearing(180);   // 方向（度）
                        mockLocation.setSpeed(10);    //速度（米/秒）
                        mockLocation.setAccuracy(0.1f);   // 精度（米）
                        mockLocation.setTime(new Date().getTime());   // 本地时间
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                        }
                        locationManager.setTestProviderLocation(providerStr, mockLocation);
                    } catch (Exception e) {
                        // 防止用户在软件运行过程中关闭模拟位置或选择其他应用
                        Log.d("ModelGPS", "RunnableMockLocation run: 软件运行过程中关闭模拟位置或选择其他应用");
                        stopMockLocation();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}