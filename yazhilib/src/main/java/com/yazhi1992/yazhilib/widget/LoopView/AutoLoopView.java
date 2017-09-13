package com.yazhi1992.yazhilib.widget.LoopView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yazhi1992.yazhilib.utils.LibCalcUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyz on 2017/6/13.
 * <p>
 * 参考：https://github.com/weidongjian/androidWheelView/
 * <p>
 * 一次滚动距离 圆周=Math.PI*ViewHeight/4
 * 圆直径=ViewHeight
 * <p>
 * 是否Loop
 * <p>
 * 对齐方式：左对齐/居中对齐
 */
public class AutoLoopView extends View {

    private Paint mTextPaint;
    private float mTextSize = 15;
    //动画时间
    private long mDuration = 1000L;
    //动画间隔时间
    private long mAnimGapDuration = 2000L;
    private List<String> mItems = new ArrayList<>();
    //例如从上往下滚动，第0条->1条，则mCurrentItem = 0，即从正中间显示向下滚，同时则mCurrentItem+1从上滚动显示出来
    //所以目标position是 mCurrentItem + 1
    private int mCurrentItem = 0;
    private ValueAnimator mValueAnimator;
    private Handler mHandler;
    //单行高度
    private float mItemHeight;
    //控件高度
    private int mHeight;
    //半径
    private float mRadiu;
    //当前滚轮移动角度
    private float mRadian;
    //默认循环
    private boolean mIsLoop = true;
    //默认滚动方向为从上方滚出
    private boolean mBottomToTop = false;
    private Drawable mDefaultColor;
    private int mBgColorPressed = -1;
    private OnAutoLoopViewClickListener mOnAutoLoopViewClickListener;

    public AutoLoopView(Context context) {
        this(context, null);
    }

    public AutoLoopView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(LibCalcUtil.sp2px(context, 16));
        mItemHeight = Math.abs(mTextPaint.ascent());
    }

    public void setItems(List<String> items) {
        mItems = items;
    }

    public interface OnAutoLoopViewClickListener {
        /**
         * @param position 点击时的行号，如果正处于1->2而2还未处于正中，则仍然返回1
         */
        void onClick(int position);
    }

    public void setOnAutoLoopViewClickListener(OnAutoLoopViewClickListener onAutoLoopViewClickListener) {
        mOnAutoLoopViewClickListener = onAutoLoopViewClickListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mItems == null || mItems.isEmpty() || mHeight == 0) {
            return;
        }
        if (mCurrentItem + 1 >= mItems.size() && !mIsLoop) {
            //最后一个且不可循环就不能滚了
            canvas.drawText(mItems.get(mCurrentItem), 100, (mHeight + mItemHeight) / 2, mTextPaint);
            return;
        }
        for (int i = 0; i < 2; i++) {
            double rad;
            if (mBottomToTop) {
                rad = (mRadian - i * 90) * Math.PI / 180;
            } else {
                rad = (mRadian + i * 90) * Math.PI / 180;
            }
            canvas.save();
            double sin = Math.sin(rad);
            double cos = Math.cos(rad);
            int translateY = (int) (mRadiu * (1 - cos - sin));
            canvas.translate(0, translateY);
            //通过对文字高度做缩放来达到滚轮的效果
            canvas.scale(1, (float) cos);
            if (i == 0) {
                //文字
                canvas.drawText(mItems.get(mCurrentItem % mItems.size()), 100, (mHeight + mItemHeight) / 2, mTextPaint);
            } else {
                canvas.drawText(mItems.get((mCurrentItem + 1) % mItems.size()), 100, (mHeight + mItemHeight) / 2, mTextPaint);
            }
            canvas.restore();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mRadiu = mHeight / 2;
    }

    public void start() {
        if (mItems == null || mItems.isEmpty()) return;
        if (mValueAnimator == null) {
            //一次滚动角度为90度
            if (mBottomToTop) {
                mValueAnimator = ValueAnimator.ofFloat(0, 90);
            } else {
                mValueAnimator = ValueAnimator.ofFloat(0, -90);
            }
            mValueAnimator.setDuration(mDuration);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRadian = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });

            mHandler = new Handler();
        }
        mValueAnimator.start();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentItem + 1 < mItems.size() || mIsLoop) {
                    //超出
                    mCurrentItem++;
                    mCurrentItem = mCurrentItem % mItems.size();
                    mValueAnimator.start();
                    mHandler.postDelayed(this, mAnimGapDuration + mDuration);
                }
            }
        }, mAnimGapDuration + mDuration);
    }

    public boolean isLoop() {
        return mIsLoop;
    }

    /**
     * 设置是否循环
     *
     * @param loop
     */
    public void setLoop(boolean loop) {
        mIsLoop = loop;
    }

    public boolean isBottomToTop() {
        return mBottomToTop;
    }

    /**
     * 设置滚动方向
     *
     * @param bottomToTop true 新数据从下方滚出 ，false 新数据从上方滚动
     */
    public void setScrollDirection(boolean bottomToTop) {
        mBottomToTop = bottomToTop;
    }

    /**
     * 设置点击时的背景颜色
     *
     * @param bgColorPressed
     */
    public void setBackgroundPressedColor(int bgColorPressed) {
        mBgColorPressed = bgColorPressed;
    }

    private int mClickPosition = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDefaultColor = getBackground();
                setBackgroundDrawable(null);
                if (mBgColorPressed != -1) {
                    if (mValueAnimator.isRunning()) {
                        mClickPosition = mCurrentItem % mItems.size();
                    } else {
                        mClickPosition = (mCurrentItem + 1) % mItems.size();
                    }
                }
                setBackgroundColor(mBgColorPressed);
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(mDefaultColor);
                if (mOnAutoLoopViewClickListener != null) {
                    mOnAutoLoopViewClickListener.onClick(mClickPosition);
                }
                break;
            default:
                break;
        }
        return true;
    }
}
