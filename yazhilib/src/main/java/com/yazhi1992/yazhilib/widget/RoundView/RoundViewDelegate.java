package com.yazhi1992.yazhilib.widget.RoundView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.yazhi1992.yazhilib.R;
import com.yazhi1992.yazhilib.utils.CalcUtil;

import static android.R.attr.enabled;
import static android.R.attr.state_enabled;
import static android.R.attr.state_focused;
import static android.R.attr.state_pressed;

/**
 * Created by zengyazhi on 17/5/17.
 * <p>
 * 参考、致谢：https://github.com/H07000223/FlycoRoundView
 */

public class RoundViewDelegate {
    /*持有View*/
    private View mView;
    private Context mContext;
    private int mStrokeWidth;
    /*各种半径*/
    private int mCornerRadius;
    private int mCornerRadius_TL;
    private int mCornerRadius_TR;
    private int mCornerRadius_BL;
    private int mCornerRadius_BR;
    /*各种颜色*/
    private int mBackgroundColor;
    private int mBackgroundColorPressed;
    private int mStrokeColor;
    private int mStrokeColorPressed;
    private int mTextColorPressed;
    private int mBackgroundDisableColor;
    private int mTextDisableColor;
    private int mStrokeDisableColor;
    /*是否圆角直径即高度*/
    private boolean mIsCircleRound;
    private float[] mRadiusArr = new float[8];
    private GradientDrawable mGdBackground = new GradientDrawable();
    private GradientDrawable mGdBackgroundPressed = new GradientDrawable();
    private GradientDrawable mGdBackgroundDisable = new GradientDrawable();


    public RoundViewDelegate(View view, Context context, AttributeSet attrs) {
        mView = view;
        mContext = context;
        init(attrs);
    }

    // TODO: 17/5/22 background 默认属性无效
    private void init(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.RoundTextView);
        mBackgroundColor = ta.getColor(R.styleable.RoundTextView_rv_backgroundColor, Color.TRANSPARENT);
        mBackgroundColorPressed = ta.getColor(R.styleable.RoundTextView_rv_backgroundPressColor, Integer.MAX_VALUE);
        mCornerRadius = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius, 0);
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_strokeWidth, 0);
        mStrokeColor = ta.getColor(R.styleable.RoundTextView_rv_strokeColor, Color.TRANSPARENT);
        mStrokeColorPressed = ta.getColor(R.styleable.RoundTextView_rv_strokePressColor, Integer.MAX_VALUE);
        mTextColorPressed = ta.getColor(R.styleable.RoundTextView_rv_textPressColor, Integer.MAX_VALUE);
        mIsCircleRound = ta.getBoolean(R.styleable.RoundTextView_rv_isCircleRound, false);
        mTextDisableColor = ta.getColor(R.styleable.RoundTextView_rv_textDisableColor, Integer.MAX_VALUE);
        mStrokeDisableColor = ta.getColor(R.styleable.RoundTextView_rv_strokeDisableColor, Integer.MAX_VALUE);
        mBackgroundDisableColor = ta.getColor(R.styleable.RoundTextView_rv_backgroundDisableColor, Integer.MAX_VALUE);
        mCornerRadius_TL = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_TL, 0);
        mCornerRadius_TR = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_TR, 0);
        mCornerRadius_BL = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_BL, 0);
        mCornerRadius_BR = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_BR, 0);
        ta.recycle();
    }

    private void setDrawble(GradientDrawable gd, int bgColor, int strokeColor) {
        gd.setColor(bgColor);

        if (mCornerRadius_TL > 0 || mCornerRadius_TR > 0 || mCornerRadius_BL > 0 || mCornerRadius_BR > 0) {
            mRadiusArr[0] = mCornerRadius_TL;
            mRadiusArr[1] = mCornerRadius_TL;
            mRadiusArr[2] = mCornerRadius_TR;
            mRadiusArr[3] = mCornerRadius_TR;
            mRadiusArr[4] = mCornerRadius_BL;
            mRadiusArr[5] = mCornerRadius_BL;
            mRadiusArr[6] = mCornerRadius_BR;
            mRadiusArr[7] = mCornerRadius_BR;
            gd.setCornerRadii(mRadiusArr);
        } else {
            gd.setCornerRadius(mCornerRadius);
        }

        gd.setStroke(mStrokeWidth, strokeColor);
    }

    public void setBgSelector() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        setDrawble(mGdBackground, mBackgroundColor, mStrokeColor);

        //只要有一个状态匹配，背景就会被换掉
        //点击状态下
        stateListDrawable.addState(new int[]{-state_pressed, state_enabled}, mGdBackground);
        if (mBackgroundColorPressed != Integer.MAX_VALUE || mStrokeColorPressed != Integer.MAX_VALUE) {
            setDrawble(mGdBackgroundPressed, mBackgroundColorPressed == Integer.MAX_VALUE ? mBackgroundColor : mBackgroundColorPressed, mStrokeColorPressed == Integer.MAX_VALUE ? mStrokeColor : mStrokeColorPressed);
            stateListDrawable.addState(new int[]{state_pressed, state_enabled}, mGdBackgroundPressed);
        }

        //可用状态下
        if (mBackgroundDisableColor != Integer.MAX_VALUE || mStrokeDisableColor != Integer.MAX_VALUE) {
            setDrawble(mGdBackgroundDisable, mBackgroundDisableColor == Integer.MAX_VALUE ? mBackgroundColor : mBackgroundDisableColor, mStrokeDisableColor == Integer.MAX_VALUE ? mStrokeColor : mStrokeDisableColor);
            stateListDrawable.addState(new int[]{-state_enabled}, mGdBackgroundDisable);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
            mView.setBackground(stateListDrawable);
        } else {
            //noinspection deprecation
            mView.setBackgroundDrawable(stateListDrawable);
        }

        if (mView instanceof TextView) {
            if (mTextColorPressed != Integer.MAX_VALUE) {
                ColorStateList textColors = ((TextView) mView).getTextColors();
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{new int[]{-state_pressed, state_enabled}, new int[]{state_pressed, state_enabled}, new int[]{-state_enabled}},
                        new int[]{textColors.getDefaultColor(), mTextColorPressed, mTextDisableColor});
                ((TextView) mView).setTextColor(colorStateList);
            }
        }
    }

    public boolean isCircleRound() {
        return mIsCircleRound;
    }

    public void setCornerRadius(int cornerRadius) {
        mCornerRadius = (int) CalcUtil.dp2px(mContext, cornerRadius);
        setBgSelector();
    }
}
