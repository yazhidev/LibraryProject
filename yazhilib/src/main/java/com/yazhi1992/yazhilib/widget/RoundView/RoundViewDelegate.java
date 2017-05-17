package com.yazhi1992.yazhilib.widget.RoundView;

import android.content.Context;
import android.view.View;

/**
 * Created by zengyazhi on 17/5/17.
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
    private int mTextColor;
    private int mTextColorPressed;
    /*是否圆角直径即高度*/
    private boolean mIsCircleRound;
    /*是否为圆形*/
    private boolean mIsRealCircle;
    private boolean mIsRippleEnable;
    private float[] radiusArr = new float[8];


    public RoundViewDelegate(View view, Context context) {
        mView = view;
        mContext = context;
        init();
    }

    private void init() {
        mCornerRadius_TL = 10;
    }


}
