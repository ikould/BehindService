package com.ikould.back.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikould.back.R;
import com.ikould.back.activity.MainActivity;
import com.ikould.back.data.AppTimer;
import com.ikould.back.dialog.WorkTimeDialog;
import com.ikould.back.task.AppStartTask;
import com.ikould.frame.annotation.InjectView;
import com.ikould.frame.fragment.BaseFragment;

/**
 * describe
 * Created by ikould on 2017/6/16.
 */

public class AppTimerFragment extends BaseFragment {

    @InjectView(R.id.iv_back)
    private ImageView mBack;

    @InjectView(R.id.app_name)
    private TextView tvAppName;

    @InjectView(R.id.tv_app_msg_start)
    private TextView mStartMsg;

    @InjectView(R.id.cb_app_time_start)
    private CheckBox mCbAppStart;

    @InjectView(R.id.tv_app_msg_close)
    private TextView mCloseMsg;

    @InjectView(R.id.cb_app_time_end)
    private CheckBox mCbAppClose;

    @InjectView(R.id.ll_app_time_open)
    LinearLayout llAppOpen;

    @InjectView(R.id.ll_app_time_close)
    LinearLayout llAppClose;

    private AppTimer openAppTimer;
    private AppTimer closeAppTimer;

    private WorkTimeDialog timeDialog;

    @Override
    public int setLayoutId() {
        return R.layout.fragment_timer;
    }

    @Override
    public void onBaseFragmentCreate(Bundle savedInstanceState) {
        Log.d("AppTimerFragment", "onBaseFragmentCreate: ");
        initConfig();
        initView();
        initListener();
    }

    private void initConfig() {
        openAppTimer = AppStartTask.getInstance().getOpenAppTimer();
        closeAppTimer = AppStartTask.getInstance().getCloseAppTimer();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        initOpenInfo();
        initCloseInfo();
    }

    /**
     * 自动开启状态初始化
     */
    private void initOpenInfo() {
        if (openAppTimer != null) {
            mCbAppStart.setChecked(openAppTimer.isAutoOpen());
            mStartMsg.setVisibility(View.VISIBLE);
            // 名字
            tvAppName.setText(openAppTimer.getName());
            // 提示信息
            mStartMsg.setText(openAppTimer.getName() + " " + getDoubleNum(openAppTimer.getStartHour()) + " : " + getDoubleNum(openAppTimer.getStartMin())
                    + " - " + getDoubleNum(openAppTimer.getEndHour()) + " : " + getDoubleNum(openAppTimer.getEndMin()));
        } else {
            mCbAppStart.setChecked(false);
            mStartMsg.setVisibility(View.GONE);
        }
    }

    /**
     * 获取双数
     *
     * @param num
     * @return
     */
    private String getDoubleNum(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    /**
     * 自动关闭状态初始化
     */
    private void initCloseInfo() {
        if (closeAppTimer != null) {
            mCbAppClose.setChecked(true);
            mCloseMsg.setVisibility(View.VISIBLE);
            // TODO
        } else {
            mCbAppClose.setChecked(false);
            mCloseMsg.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mBack.setOnClickListener(v -> {
            // 返回设置界面
            ((MainActivity) getActivity()).replaceFragment(R.id.main_fragments, new SettingFragment(), null);
        });
        llAppOpen.setOnClickListener(v -> {
            if (timeDialog == null)
                timeDialog = new WorkTimeDialog(getContext());
            timeDialog.setOnConfirmListener(onConfirmClickListener, openAppTimer);
            timeDialog.show();
        });
        llAppClose.setOnClickListener(v -> {
            // TODO
        });
        mCbAppStart.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("AppTimerFragment", "initListener: isChecked = " + isChecked);
            openAppTimer.setAutoOpen(isChecked);
            updateOpenAppTimerInfo();
        });
        mCbAppClose.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO
        });
    }

    WorkTimeDialog.OnConfirmClickListener onConfirmClickListener = appTimerData -> {
        AppStartTask.getInstance().updateInfo();
    };

    private void updateOpenAppTimerInfo() {
        AppStartTask.getInstance().updateInfo();
    }
}
