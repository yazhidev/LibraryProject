package com.yazhi1992.yazhilib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yazhi1992.yazhilib.R;
import com.yazhi1992.yazhilib.utils.CalcUtil;

/**
 * viewpager指示器
 *
 * @author zyz 2016.08.16 更新于 2017.06.12
 */
public class ViewPagerIndicator extends View implements ViewPager.OnPageChangeListener {

    private Paint mPaint;
    //总页数
    private int mTotalPages;
    //总长度
    private int mAllDistance;
    //当前页
    private int mCurrentPage;
    //偏移量
    private float mCurrentPageOffset;
    //两个圆之间的距离
    private int mSpacing;
    //半径
    private int mRadius;
    //长条距离
    private int mLongDistance;
    //短条距离
    private int mShortDistance;
    //长条变短条的变化距离
    private int mDistance;
    private int mColor;
    private int mUnselectedColor;
    // 滑动过程中是否缓慢过度
    private boolean animated = true;
    private Point mCenter;
    private boolean mNeedPaint = true;

    public void setSelectedColor(int color) {
        mColor = color;
    }

    public void setUnselectedColor(int unselectedColor) {
        mUnselectedColor = unselectedColor;
    }

    public ViewPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        mRadius = (int) CalcUtil.dp2px(context, 4);
        mShortDistance = (int) CalcUtil.dp2px(context, 10);
        mLongDistance = (int) CalcUtil.dp2px(context, 30);
        mDistance = mLongDistance - mShortDistance;
        mSpacing = (int) CalcUtil.dp2px(context, 5) + mRadius * 2;

        mPaint.setStrokeWidth(mRadius * 2);
    }

    public int getSpacing() {
        return mSpacing;
    }

    public int getRadius() {
        return mRadius;
    }

    public int getLongDistance() {
        return mLongDistance;
    }

    public void setLongDistance(int longDistance) {
        mLongDistance = longDistance;
    }

    public int getShortDistance() {
        return mShortDistance;
    }

    public void setShortDistance(int shortDistance) {
        mShortDistance = shortDistance;
    }

    // 初始化
    private void init(Context context) {
        mColor = getResources().getColor(R.color.mainRed);
        mUnselectedColor = getResources().getColor(R.color.gray);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND); //设置画笔为圆形
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (animated) {
            mCurrentPage = position % mTotalPages;
            mCurrentPageOffset = positionOffset;
            invalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (!animated) {
            mCurrentPage = position % mTotalPages;
            mCurrentPageOffset = 0;
            invalidate();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_SETTLING) { // 松开手
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCenter == null) {
            mCenter = getCenter();
        }

        float startX = mCenter.x - mAllDistance / 2;

        for (int i = 0; i < mTotalPages; i++) {
            if (i == mCurrentPage) {
                //往右翻页，offset 0->1，早offset=1时mCurrentPage变成目标页
                //往左翻页，mCurrentPage马上变成目标页，offset 1->0
                mPaint.setColor(CalcUtil.evaluateColor(mColor, mUnselectedColor, mCurrentPageOffset));
                float offset = mDistance * (1 - mCurrentPageOffset);
                canvas.drawLine(startX, mCenter.y, startX + mShortDistance + offset, mCenter.y, mPaint);
                startX += mShortDistance + offset + mSpacing;
            } else if (i == (mCurrentPage + 1) % mTotalPages) {
                mPaint.setColor(CalcUtil.evaluateColor(mColor, mUnselectedColor, 1 - mCurrentPageOffset));
                float offset = mDistance * mCurrentPageOffset;
                canvas.drawLine(startX, mCenter.y, startX + mShortDistance + offset, mCenter.y, mPaint);
                startX += mShortDistance + offset + mSpacing;
            } else {
                mPaint.setColor(mUnselectedColor);
                canvas.drawLine(startX, mCenter.y, startX + mShortDistance, mCenter.y, mPaint);
                startX += mShortDistance + mSpacing;
            }
        }
    }

    private float getFirstDotPosition(float centerX) {
        float centerIndex = mTotalPages % 2 == 0 ? (mTotalPages - 1) / 2 : mTotalPages / 2;
        //下取整，返回小于或等于x，并且与之最接近的整数。如果x是正数，则把小数“舍”；如果x是负数，则把小数“入”。
        float spacingMult = (float) Math.floor(centerIndex);
        if (mTotalPages % 2 == 0)
            spacingMult += 0.5;
        return centerX - ((mSpacing + mRadius * 2) * spacingMult);
    }

    /**
     * 获得控件中心点的坐标
     *
     * @return 中心点的坐标
     */
    private Point getCenter() {
        return new Point((getRight() - getLeft()) / 2, (getBottom() - getTop()) / 2);
    }

    /**
     * 设置指示器的数量
     *
     * @param totalPages
     */
    public void setTotalPages(int totalPages) {
        mTotalPages = totalPages;
        mAllDistance = mLongDistance + (totalPages - 1) * (mShortDistance + mSpacing);
        invalidate();
    }

    /**
     * 设定指示器的半径
     *
     * @param size
     */
    public void setRadius(int size) {
        mRadius = size;
    }

    /**
     * 设定指示器的间距
     *
     * @param spacing
     */
    public void setSpacing(int spacing) {
        mSpacing = spacing;
    }

}
