package com.yazhi1992.yazhilib.widget.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;


/**
 * Created by zengyazhi on 17/7/7.
 */

public class BasePopupWindow extends PopupWindow {

    public interface OnClickPopWindowListener {
        void getChildView(View view, int layoutResId);
    }

    public static class Builder {
        private Context mContext;
        //弹窗的宽和高
        private int mWidth, mHeight;
        //是否显示背景、动画
        private boolean mIsShowBg, mIsShowAnim;
        //屏幕背景灰色程度,默认透明
        private float mBgLevel = 1;
        //动画Id
        private int mAnimationStyle;
        private View mView;
        private boolean mIsTouchable = true;
        private Window mWindow;
        private OnClickPopWindowListener mListener;
        private int mLayoutResId;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setLayoutResId(int layoutResId) {
            this.mLayoutResId = layoutResId;
            mView = LayoutInflater.from(mContext).inflate(layoutResId, null);
            return this;
        }

        public Builder setOnClickPopWindowListener(OnClickPopWindowListener listener) {
            mListener = listener;
            return this;
        }

        public Builder setWidthAndHeight(int width, int height) {
            mWidth = width;
            mHeight = height;
            return this;
        }

        public Builder setShowBg(boolean showBg) {
            mIsShowBg = showBg;
            return this;
        }

        public Builder setShowAnim(boolean showAnim) {
            mIsShowAnim = showAnim;
            return this;
        }

        public Builder setBgLevel(float bg_level) {
            this.mIsShowBg = true;
            this.mBgLevel = bg_level;
            return this;
        }

        public Builder setAnimationStyle(int animationStyle) {
            this.mAnimationStyle = animationStyle;
            return this;
        }

        public Builder setView(View view) {
            mView = view;
            return this;
        }

        public Builder setTouchable(boolean touchable) {
            mIsTouchable = touchable;
            return this;
        }

        public BasePopupWindow build() {
            BasePopupWindow basePopunWindow = new BasePopupWindow();
            if (mView != null) {
                basePopunWindow.setContentView(mView);
            } else {
                throw new IllegalArgumentException("PopupView's contentView is null");
            }

            if (mWidth == 0 || mHeight == 0) {
                //如果没设置宽高，默认是WRAP_CONTENT
                basePopunWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                basePopunWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                basePopunWindow.setWidth(mWidth);
                basePopunWindow.setHeight(mHeight);
            }

            basePopunWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));//设置透明背景
            basePopunWindow.setOutsideTouchable(mIsTouchable);//设置outside可点击
            basePopunWindow.setFocusable(mIsTouchable);

            if (mIsShowBg) {
                //设置背景灰色程度 0.0f-1.0f
                mWindow = ((Activity) mContext).getWindow();
                WindowManager.LayoutParams params = mWindow.getAttributes();
                params.alpha = mBgLevel;
                mWindow.setAttributes(params);
            }

            if (mListener != null) {
                mListener.getChildView(mView, mLayoutResId);
            }

            //设置动画
            basePopunWindow.setAnimationStyle(mAnimationStyle);

            return basePopunWindow;
        }
    }
}
