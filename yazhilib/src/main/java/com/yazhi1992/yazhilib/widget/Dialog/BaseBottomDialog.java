package com.yazhi1992.yazhilib.widget.Dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

/**
 * Created by zengyazhi on 17/5/16.
 */

public abstract class BaseBottomDialog<T extends BaseBottomDialog<T>> extends BaseDialog<T> {

    protected Animation mInnerShowAnim;
    protected Animation mInnerDismissAnim;
    protected long mInnerAnimationDuration = 350;
    protected boolean mIsInnerShowAnim;
    protected boolean mIsInnerDismissAnim;

    public BaseBottomDialog(Context context) {
        super(context);

        /*默认的动画*/
        mInnerShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0);

        mInnerDismissAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTopLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTopLayout.setGravity(Gravity.BOTTOM);
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        showWithAnim();
    }

    @Override
    public void dismiss() {
        dismissWithAnim();
    }

    protected void showWithAnim() {
        if(mInnerShowAnim != null) {
            mInnerShowAnim.setDuration(mInnerAnimationDuration);
            mInnerShowAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mIsInnerShowAnim = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIsInnerShowAnim = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mControlHeightLayout.startAnimation(mInnerShowAnim);
        }else {
            super.dismiss();
        }
    }

    protected void dismissWithAnim() {
        if(mInnerDismissAnim != null) {
            mInnerDismissAnim.setDuration(mInnerAnimationDuration);
            mInnerDismissAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mIsInnerDismissAnim = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIsInnerDismissAnim = false;
                    BaseBottomDialog.super.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mControlHeightLayout.startAnimation(mInnerDismissAnim);
        } else {
            super.dismiss();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIsInnerShowAnim || mIsInnerDismissAnim) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if(mIsInnerShowAnim || mIsInnerDismissAnim) {
            return;
        }
        super.onBackPressed();
    }
}
