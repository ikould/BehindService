package com.ikould.frame.activity;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.ikould.frame.annotation.InjectView;
import com.ikould.frame.utils.KeyBoardUtils;

import java.lang.reflect.Field;

/**
 * Activity基类
 * <p>
 * Created by liudong on 2016/8/2.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public View mContentView;

    public abstract void onBaseCreate(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!showToolBar()) {
            getSupportActionBar().hide();
        }
        onBaseCreate(savedInstanceState);
        if (isFullScreen() && mContentView != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(mContentView);
    }

    /**
     * 是否显示系统标题栏
     */
    public boolean showToolBar() {
        return false;
    }

    /**
     * 获取ContentView
     *
     * @return
     */
    public void setLayoutId(int id) {
        mContentView = LayoutInflater.from(this).inflate(id, null);
        autoInjectAnnotation();
    }

    /**
     * 获取ContentView
     *
     * @return
     */
    public void setLayoutView(View view) {
        mContentView = view;
        autoInjectAnnotation();
    }


    /**
     * 是否全屏
     */
    public boolean isFullScreen() {
        return false;
    }

    /**
     * 自动注入，给View赋值
     */
    public void autoInjectAnnotation() {
        Class clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectView.class)) {
                InjectView injectView = field.getAnnotation(InjectView.class);
                int viewId = injectView.value();
                if (viewId > 0) {
                    field.setAccessible(true);
                    try {
                        field.set(this, mContentView.findViewById(viewId));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 更换Fragment
     */
    public void replaceFragment(int id, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (tag == null) {
            fragmentTransaction.replace(id, fragment);
        } else {
            fragmentTransaction.replace(id, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
        }
        KeyBoardUtils.closeKeyboard(this);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
