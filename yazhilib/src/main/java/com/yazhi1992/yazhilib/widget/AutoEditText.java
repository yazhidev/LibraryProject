package com.yazhi1992.yazhilib.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.yazhi1992.yazhilib.utils.PhoneUtils;

/**
 * Created by zengyazhi on 17/6/6.
 * <p>
 * 软键盘显示时显示光标，软键盘隐藏是隐藏光标
 */

public class AutoEditText extends EditText {
    private boolean mIsKeyShow;

    public AutoEditText(Context context) {
        super(context);
        init();
    }

    public AutoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //设置空背景
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }
    }

    public void setOutView(final View outView) {
        outView.setFocusable(true);
        outView.setFocusableInTouchMode(true);
        outView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                boolean keyShow = PhoneUtils.isKeyBoardShowing(outView);
                if (keyShow != mIsKeyShow) {
                    mIsKeyShow = keyShow;
                    if (!PhoneUtils.isKeyBoardShowing(outView)) {
                        //隐藏键盘
                        if (isFocused()) {
                            clearFocus();
                        }
                    }
                }
            }
        });
    }
}
