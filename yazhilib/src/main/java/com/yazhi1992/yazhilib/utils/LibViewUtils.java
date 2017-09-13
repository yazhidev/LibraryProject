package com.yazhi1992.yazhilib.utils;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 动画的工具类
 *
 * @author zyz 2016.08.16
 */
public class LibViewUtils {

    /**
     * 根据drawble文件名得到bitmap
     *
     * @param context   用来获得Resources
     * @param width     想要获得的bitmap的宽
     * @param height    想要获得的bitmap的高
     * @param drawble   drawble图片名称
     * @return
     */
    public static Bitmap getBitmap(Context context, int width, int height, int drawble) {
        // 得到图像
        Resources resources = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawble);

        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();

        float scaleX = (float) width / bWidth;
        float scaleY = (float) height / bHeight;

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);

        return Bitmap.createBitmap(bitmap, 0, 0, bWidth, bHeight, matrix, true);
    }

    /**
     * X、Y轴平移+渐变的动画
     *
     * @param targetView 播放动画的对象
     * @param startLocationY Y轴的起始点
     * @param endLocationY Y轴的终点
     * @param startAlpha 渐变动画开始时的透明度
     * @param endAlpha 渐变动画结束时的透明度
     * @param duration 动画的时间
     */
    public static void translateAndAlpha(float startLocationX, float endLocationX, float startLocationY,
        float endLocationY, float startAlpha, float endAlpha, long duration, View... targetView) {
        if (targetView == null)
            return;
        final ArrayList<View> targetViews = new ArrayList<>(Arrays.asList(targetView));
        PropertyValuesHolder scale = PropertyValuesHolder.ofFloat("sca", startAlpha, endAlpha);
        PropertyValuesHolder tranX = PropertyValuesHolder.ofFloat("trax", startLocationX, endLocationX);
        PropertyValuesHolder tranY = PropertyValuesHolder.ofFloat("tray", startLocationY, endLocationY);
        ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(scale, tranX, tranY);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float sca = (float) animation.getAnimatedValue("sca");
                float trax = (float) animation.getAnimatedValue("trax");
                float tray = (float) animation.getAnimatedValue("tray");
                for (View item : targetViews) {
                    item.setTranslationX(trax);
                    item.setTranslationY(tray);
                    item.setAlpha(sca);
                }
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                for (View item : targetViews) {
                    item.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    /**
     * X、Y轴平移+渐变的动画
     *
     * @param targetView 播放动画的对象
     * @param startLocationY Y轴的起始点
     * @param endLocationY Y轴的终点 例如startY 0， endY 10，即为下降10(虽然一个View可能有margin，0指的是起始点)
     * @param duration 动画的时间
     */
    public static ValueAnimator translate(float startLocationX, float endLocationX, float startLocationY,
                                          float endLocationY, long duration, View... targetView) {
        if (targetView == null)
            return null;
        final ArrayList<View> targetViews = new ArrayList<>(Arrays.asList(targetView));
        PropertyValuesHolder tranX = PropertyValuesHolder.ofFloat("trax", startLocationX, endLocationX);
        PropertyValuesHolder tranY = PropertyValuesHolder.ofFloat("tray", startLocationY, endLocationY);
        ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(tranX, tranY);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float trax = (float) animation.getAnimatedValue("trax");
                float tray = (float) animation.getAnimatedValue("tray");
                for (View item : targetViews) {
                    item.setTranslationX(trax);
                    item.setTranslationY(tray);
                }
            }
        });
        valueAnimator.start();
        return valueAnimator;
    }

    /**
     * 渐变动画
     * @param targetView 播放动画的控件
     * @param startAlpha 渐变的起始值
     * @param endAlpha 渐变的终止值
     * @param duration 动画的时间
     */
    public static ValueAnimator AlphaAnimation(final View targetView, float startAlpha, final float endAlpha, long duration) {
        if(startAlpha == 0 && endAlpha == 1) {
            targetView.setVisibility(View.VISIBLE);
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startAlpha, endAlpha);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float sca = (float) animation.getAnimatedValue();
                targetView.setAlpha(sca);
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (endAlpha <= 0) {
                    targetView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
        return valueAnimator;
    }

    /**
     * 设置控件是否可见
     * @param Visibility 是否可见,View.BISIBLE、View.GONE等
     * @param view 控件 
     */
    public static void setViewVisiblity(int Visibility, View... view) {
        ArrayList<View> viewArrayList = new ArrayList<>(Arrays.asList(view));
        for (View item : viewArrayList) {
            item.setVisibility(Visibility);
        }
    }

    /**
     * 控件执行渐变动画
     * @param alpha 渐变的值
     * @param view 目标控件
     */
    public static void setViewsAlpha(float alpha, View... view) {
        ArrayList<View> viewArrayList = new ArrayList<>(Arrays.asList(view));
        for (View item : viewArrayList) {
            item.setAlpha(alpha);
        }
    }

    /**
     * 控件执行渐变动画
     * @param alpha 渐变的值
     * @param view 目标控件
     */
    public static void setsetTranslationX(float alpha, View... view) {
        ArrayList<View> viewArrayList = new ArrayList<>(Arrays.asList(view));
        for (View item : viewArrayList) {
            item.setTranslationX(alpha);
        }
    }

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @SuppressWarnings("static-access")
    public static float dp2Px(Context context, float value) {
        if (context == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return typedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }
}
