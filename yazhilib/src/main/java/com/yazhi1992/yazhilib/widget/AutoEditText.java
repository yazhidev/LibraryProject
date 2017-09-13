package com.yazhi1992.yazhilib.widget;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.yazhi1992.yazhilib.utils.LibUtils;


/**
 * Created by zengyazhi on 17/6/6.
 * <p>
 * 软键盘显示时显示光标，软键盘隐藏是隐藏光标
 */

public class AutoEditText extends EditText implements TextWatcher {
    private boolean mIsKeyShow;
    //最大输入字数
    private int mMaxLenght;
    private onTextLengthListener mOnTextLengthListener;

    public interface onTextLengthListener{
        void onTextLengthChanged(int length);
    }

    /**
     * 设置文字字数监听
     * @param onTextLengthListener
     */
    public void setOnTextLengthListener(onTextLengthListener onTextLengthListener) {
        mOnTextLengthListener = onTextLengthListener;
    }

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

    /**
     * 设置自动隐藏光标
     * @param outView 页面最外层的view
     */
    public void setAutoHideSelection(final View outView) {
        outView.setFocusable(true);
        outView.setFocusableInTouchMode(true);
        outView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                boolean keyShow = LibUtils.isKeyBoardShowing(outView);
                if (keyShow != mIsKeyShow) {
                    mIsKeyShow = keyShow;
                    if (!LibUtils.isKeyBoardShowing(outView)) {
                        //隐藏键盘
                        if (isFocused()) {
                            clearFocus();
                        }
                    }
                }
            }
        });
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mMaxLenght > 0) {
            //如果超出最大输入字数则截取掉
            if (s.length() > mMaxLenght) {
                String substring = String.valueOf(s).substring(0, mMaxLenght);
                setText(substring);
                setSelection(substring.length());
            } else {
                if(mOnTextLengthListener != null) {
                    mOnTextLengthListener.onTextLengthChanged(s.length());
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置最大输入字数
     * @param maxLenght
     */
    public void setMaxLenght(int maxLenght) {
        mMaxLenght = maxLenght;
    }
}
