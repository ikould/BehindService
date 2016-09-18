package com.ikould.backgroundservice.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.ikould.backgroundservice.CoreApplication;
import com.ikould.backgroundservice.R;
import com.ikould.backgroundservice.config.AppConfig;
import com.ikould.backgroundservice.service.BackService;
import com.ikould.frame.annotation.InjectView;
import com.ikould.frame.fragment.BaseFragment;
import com.ikould.frame.utils.ToastUtils;

/**
 * 设置界面
 * <p>
 * Created by liudong on 2016/8/15.
 */
public class SettingFragment extends BaseFragment implements View.OnTouchListener {
    @InjectView(R.id.ll_set_clear)
    private LinearLayout mSetClear;

    @InjectView(R.id.cb_set_clear)
    private CheckBox mCbSetClear;

    @InjectView(R.id.ll_fast_clear)
    private LinearLayout mFastClear;

    @InjectView(R.id.cb_fastclear_arrow)
    private CheckBox mCbFastClear;

    @InjectView(R.id.ll_close_adv)
    private LinearLayout mCloseAdv;

    @InjectView(R.id.cb_close_adv)
    private CheckBox mCbCloseAdv;

    private AdvertListener mAdvert;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mAdvert = (AdvertListener) context;
    }

    @Override
    public void onBaseFragmentCreate(Bundle savedInstanceState) {
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mSetClear.setOnTouchListener(this);
        mFastClear.setOnTouchListener(this);
        mCloseAdv.setOnTouchListener(this);
        mCbSetClear.setChecked(AppConfig.getInstance().getAutoClear());
        mCbCloseAdv.setChecked(AppConfig.getInstance().getCloseAdvert());
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.ll_set_clear:
                touchSetClearEvent(event);
                break;
            case R.id.ll_fast_clear:
                //Arrow press
                touchFastClearEvent(event);
                break;
            case R.id.ll_close_adv:
                touchCloseAdvEvent(event);
                break;
        }
        return true;
    }

    /**
     * 清理服务
     *
     * @param event 事件
     */
    private void touchSetClearEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean isAuto = !AppConfig.getInstance().getAutoClear();
            mCbSetClear.setChecked(isAuto);
            AppConfig.getInstance().setAutoClear(isAuto);
            if (isAuto) {
                //打开服务
                startPreService();
            } else {
                //关闭服务
                closePreService();
            }
        }
    }

    /**
     * 立即清理
     *
     * @param event 事件
     */
    private void touchFastClearEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFastClear.setBackgroundResource(R.color.dark);
                mCbFastClear.setChecked(true);
                break;
            case MotionEvent.ACTION_UP:
                mFastClear.setBackgroundResource(R.color.light_dark);
                mCbFastClear.setChecked(false);
                //立即清理
                if (CoreApplication.getInstance().killAppsHandler != null) {
                    CoreApplication.getInstance().killAppsHandler.sendMessage(Message.obtain());
                    ToastUtils.show(getActivity(), "清理成功");
                } else {
                    ToastUtils.show(getActivity(), "服务未开启");
                }
                break;
        }
    }

    /**
     * 关闭广告
     *
     * @param event
     */
    private void touchCloseAdvEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean isClose = !AppConfig.getInstance().getCloseAdvert();
            mCbCloseAdv.setChecked(isClose);
            AppConfig.getInstance().setCloseAdvert(isClose);
            mAdvert.closeAdvert(isClose);
        }
    }

    /**
     * 开启前台服务
     */
    private void startPreService() {
        Intent intent = new Intent(getActivity(), BackService.class);
        getActivity().startService(intent);
    }

    /**
     * 关闭前台服务
     */
    private void closePreService() {
        Intent intent = new Intent(getActivity(), BackService.class);
        getActivity().stopService(intent);
    }

    public interface AdvertListener {
        void closeAdvert(boolean isClose);
    }
}
