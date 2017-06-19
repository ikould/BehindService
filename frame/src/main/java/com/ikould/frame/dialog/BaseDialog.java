package com.ikould.frame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ikould.frame.R;
import com.ikould.frame.utils.ScreenUtils;


/**
 * 1.继承构造方法BaseDialog()
 * 2.继承setLayoutId()或者setLayoutView()
 * 3.findViewByid可使用ButterKnife
 * <p>
 * Created by liudong on 2016/9/22.
 */
public abstract class BaseDialog extends Dialog {

    private View    mContentView;
    public  Context mContext;

    private int mWidth  = 648;
    private int mHeight = 1000;

    public BaseDialog(Context context) {
        this(context, R.style.DefineDialog);
    }

    private BaseDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        init(context);
    }

    private BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        setCanceledOnTouchOutside(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mContext = context;
        if (setLayoutId() != 0) {
            mContentView = LayoutInflater.from(mContext).inflate(setLayoutId(), null);
        } else {
            mContentView = setLayoutView();
        }
        setContentView(mContentView);
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        //获得当前窗体
        Window window = getWindow();
        //重新设置
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
      /*  mWidth = ScreenUtils.dip2px(getContext(), 300); // 宽度
        mHeight = ScreenUtils.dip2px(getContext(), 1000); // 宽度*/
      /*  lp.width = mWidth;
        lp.height = mHeight;*/
        lp.alpha = 0.9f; // 透明度
        // dialog.onWindowAttributesChanged(lp);
        //(当Window的Attributes改变时系统会调用此函数)
        window.setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConfig();
    }

    /**
     * 设置尺寸
     *
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    /**
     * 设置布局id
     *
     * @return
     */
    public int setLayoutId() {
        return 0;
    }

    public View setLayoutView() {
        return null;
    }
}
