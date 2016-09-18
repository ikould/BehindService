package com.ikould.frame.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikould.frame.annotation.InjectView;
import com.ikould.frame.utils.KeyBoardUtils;

import java.lang.reflect.Field;

/**
 * 基础Fragment
 * <p>
 * Created by liudong on 2016/8/2.
 */
public abstract class BaseFragment extends Fragment {
    public View mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (setLayoutId() != 0) {
            mContentView = LayoutInflater.from(getActivity()).inflate(setLayoutId(), null);
        } else {
            mContentView = setLayoutView();
        }
        autoInjectAnnotation();
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBaseFragmentCreate(savedInstanceState);
    }

    public abstract void onBaseFragmentCreate(Bundle savedInstanceState);

    /**
     * 获取ContentView
     *
     * @return
     */
    public int setLayoutId() {
        return 0;
    }

    /**
     * 获取ContentView
     *
     * @return
     */
    public View setLayoutView() {
        return null;
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
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (tag == null) {
            fragmentTransaction.replace(id, fragment);
        } else {
            fragmentTransaction.replace(id, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
        }
        KeyBoardUtils.closeKeyboard(getActivity());
        fragmentTransaction.commitAllowingStateLoss();
    }
}
