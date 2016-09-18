package com.ikould.frame.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

import com.ikould.frame.R;

/**
 * Description  具有check状态的ImageView
 * Created by chenqiao on 2016/5/11.
 */
public class StatusButton extends TextView implements Checkable {
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private boolean mChecked = false;

    public StatusButton(Context context) {
        this(context, null);
    }

    public StatusButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusButton);
        mChecked = a.getBoolean(R.styleable.StatusButton_statusChecked, false);
        setChecked(mChecked);
        a.recycle();
    }

    public void setImageResource(int resId) {
        invalidate();
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (mChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}