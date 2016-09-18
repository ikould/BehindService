package com.ikould.frame.net;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 各种网络请求
 * <p>
 * Created by liudong on 2016/8/11.
 */
public class HttpsHelper {
    private static HttpsHelper instance;

    public static HttpsHelper getInstance() {
        if (instance == null) {
            synchronized (HttpsHelper.class) {
                instance = new HttpsHelper();
            }
        }
        return instance;
    }

    private HttpsHelper() {
    }

    /**
     * get请求
     */
    public void get(final String urlStr, final HttpCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.connect();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        InputStreamReader reader = new InputStreamReader(httpURLConnection.getInputStream(), "utf-8");
                        char[] chars = new char[1024];
                        StringBuilder stringBuffer = new StringBuilder();
                        while (reader.read(chars) != -1) {
                            stringBuffer.append(chars);
                        }
                        reader.close();
                        callBack.onSuccess(stringBuffer.toString().trim());
                    } else {
                        callBack.onFailure(new IOException("异常：" + code));
                    }
                    httpURLConnection.disconnect();
                } catch (IOException e) {
                    callBack.onFailure(e);
                }
            }
        }).start();
    }

    /**
     * post请求
     */
    public void post(String urlStr, Map<String, Object> maps, final HttpCallBack callBack) {
        urlStr += "?";
        if (maps != null) {
            for (Map.Entry entry : maps.entrySet()) {
                urlStr += entry.getKey().toString() + "=" + entry.getValue().toString() + "&";
            }
        }
        final String uri = urlStr.substring(0, urlStr.length() - 1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(uri);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(5000);
                    // http正文内，因此需要设为true, 默认情况下是false;
                    httpURLConnection.setDoOutput(true);
                    // 设置是否从httpUrlConnection读入，默认情况下是true;
                    httpURLConnection.setDoInput(true);
                    // Post 请求不能使用缓存
                    httpURLConnection.setUseCaches(false);
                    // 设定传送的内容类型是可序列化的java对象
                    // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
                    httpURLConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
                    httpURLConnection.connect();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        Log.d("liudong", "success");
                        InputStreamReader reader = new InputStreamReader(httpURLConnection.getInputStream(), "utf-8");
                        char[] chars = new char[1024];
                        StringBuilder builder = new StringBuilder();
                        while (reader.read(chars) != -1) {
                            builder.append(chars);
                        }
                        reader.close();
                        callBack.onSuccess(builder.toString().trim());
                    } else {
                        callBack.onFailure(new IOException("异常：" + code));
                    }
                    httpURLConnection.disconnect();
                } catch (IOException e) {
                    callBack.onFailure(e);
                }
            }
        }).start();
    }

    public interface HttpCallBack {
        void onFailure(Exception e);

        void onSuccess(String response);
    }
}

