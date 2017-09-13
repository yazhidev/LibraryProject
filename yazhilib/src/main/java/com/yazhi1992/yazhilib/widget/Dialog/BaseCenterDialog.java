package com.yazhi1992.yazhilib.widget.Dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;

/**
 * Created by zengyazhi on 17/5/16.
 */

public abstract class BaseCenterDialog<T extends BaseCenterDialog<T>> extends BaseDialog<T> {

    private AnimationSet mInnerShowAnim;
    private AnimationSet mInnerDismissAnim;
    protected long mInnerAnimationDuration = 200;
    protected boolean mIsInnerShowAnim;
    protected boolean mIsInnerDismissAnim;

    public BaseCenterDialog(Context context) {
        super(context);

        mInnerShowAnim = new AnimationSet(false);
        /*默认的动画*/
//        ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1, 0.5f, 1
//                , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        mInnerShowAnim.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        mInnerShowAnim.addAnimation(alphaAnimation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTopLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTopLayout.setGravity(Gravity.CENTER);
        getWindow().setGravity(Gravity.CENTER);
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
        if (mInnerShowAnim != null) {
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
        } else {
            super.dismiss();
        }
    }

    protected void dismissWithAnim() {
        if (mInnerDismissAnim != null) {
            mInnerDismissAnim.setDuration(mInnerAnimationDuration);
            mInnerDismissAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mIsInnerDismissAnim = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIsInnerDismissAnim = false;
                    BaseCenterDialog.super.dismiss();
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
        if (mIsInnerShowAnim || mIsInnerDismissAnim) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (mIsInnerShowAnim || mIsInnerDismissAnim) {
            return;
        }
        super.onBackPressed();
    }

    public void setInnerShowAnim(AnimationSet innerShowAnim) {
        mInnerShowAnim = innerShowAnim;
    }

    public void setInnerDismissAnim(AnimationSet innerDismissAnim) {
        mInnerDismissAnim = innerDismissAnim;
    }

    public void setInnerAnimationDuration(long innerAnimationDuration) {
        mInnerAnimationDuration = innerAnimationDuration;
    }
}
