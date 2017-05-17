package com.yazhi1992.yazhilib.base;

import android.animation.AnimatorSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by zengyazhi on 17/5/16.
 */

public abstract class BaseAnimatorSet {
    protected long mDuration = 500;
    protected AnimatorSet mAnimatorSet = new AnimatorSet();
    /*最顶层容器*/
    protected RelativeLayout mTopLayout;
    /*用于控制布局高度的容器*/
    protected RelativeLayout mControlHeightLayout;
    /*创建出来的子view*/
    protected View mOnCreateView;
    /*对话框高度*/
    protected int mMaxHeight;


}
