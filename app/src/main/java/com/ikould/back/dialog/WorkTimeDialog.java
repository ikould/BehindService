package com.ikould.back.dialog;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.ikould.back.R;
import com.ikould.back.data.AppTimer;
import com.ikould.frame.dialog.BaseDialog;

/**
 * 上班时间设置
 * <p>
 * Created by ikould on 2017/6/16.
 */

public class WorkTimeDialog extends BaseDialog {

    private EditText mStartHour;
    private EditText mEndHour;
    private EditText mStartMin;
    private EditText mEndMin;
    private Button   mConfirm;
    private Button   mCancel;

    private AppTimer appTimer;

    @Override
    public int setLayoutId() {
        return R.layout.dialog_app_timer;
    }

    public WorkTimeDialog(Context context) {
        super(context);
        initView();
        initListener();
    }

    private void initView() {
        mStartHour = (EditText) findViewById(R.id.et_start_hour);
        mEndHour = (EditText) findViewById(R.id.et_end_hour);
        mStartMin = (EditText) findViewById(R.id.et_start_min);
        mEndMin = (EditText) findViewById(R.id.et_end_min);
        mConfirm = (Button) findViewById(R.id.btn_confirm);
        mCancel = (Button) findViewById(R.id.btn_cancel);
    }


    private void initListener() {
        mConfirm.setOnClickListener(v -> {
            int startHour = Integer.parseInt(mStartHour.getText().toString());
            int startMin = Integer.parseInt(mStartMin.getText().toString());
            int endHour = Integer.parseInt(mEndHour.getText().toString());
            int endMin = Integer.parseInt(mEndMin.getText().toString());
            appTimer.setStartHour(startHour);
            appTimer.setStartMin(startMin);
            appTimer.setEndHour(endHour);
            appTimer.setEndMin(endMin);
            if (onConfirmListener != null)
                onConfirmListener.onConfirmClick(appTimer);
            dismiss();
        });
        mCancel.setOnClickListener(v -> {
            dismiss();
        });
    }

    /**
     * 配置默认参数
     */
    private void initDefaultParam() {
        mStartHour.setText(String.valueOf(appTimer.getStartHour()));
        mEndHour.setText(String.valueOf(appTimer.getEndHour()));
        mStartMin.setText(String.valueOf(appTimer.getStartMin()));
        mEndMin.setText(String.valueOf(appTimer.getEndMin()));
    }

    // === 监听 ===
    private OnConfirmClickListener onConfirmListener;

    public void setOnConfirmListener(OnConfirmClickListener onConfirmListener, AppTimer appTimer) {
        this.appTimer = appTimer;
        if (appTimer != null)
            initDefaultParam();
        this.onConfirmListener = onConfirmListener;
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(AppTimer appTimer);
    }
}
