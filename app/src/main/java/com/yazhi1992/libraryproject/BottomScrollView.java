package com.yazhi1992.libraryproject;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by zengyazhi on 17/6/23.
 * <p>
 * 上拉回弹效果的scrollview
 */

public class BottomScrollView extends ScrollView {
    //内容view
    private View mContentView;
    private Rect mRect = new Rect();
    private boolean mScrollFromBottom = false;
    private int mBeginY = -1;

    public BottomScrollView(Context context) {
        super(context);
    }

    public BottomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mContentView = getChildAt(0);
        }
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mContentView != null) {
            int nowY = (int) ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    //到达底部后继续拖动的距离
                    int moveY;
                    if (isScrollToBottom()) {
                        mScrollFromBottom = true;
                    }
                    if (mScrollFromBottom) {
                        if (mBeginY == -1) {
                            //起始
                            mBeginY = nowY;
                        }
                        moveY = nowY - mBeginY;

                        if (mRect.isEmpty()) {
                            mRect.set(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(), mContentView.getBottom());
                        }
                        if (moveY < 0) {
                            mContentView.layout(mRect.left, mRect.top + 2 * moveY / 5,
                                    mRect.right, mRect.bottom + 2 * moveY / 5);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mRect.isEmpty()) {
                        reset();
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 还原
     */
    private void reset() {
        mBeginY = -1;
        Animation animation = new TranslateAnimation(0, 0, mContentView.getTop(),
                mRect.top);
        animation.setDuration(200);
        animation.setFillAfter(true);
        mContentView.startAnimation(animation);
        mContentView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
        mRect.setEmpty();
        mScrollFromBottom = false;
    }

    /**
     * 是否滑动到底部
     *
     * @return
     */
    private boolean isScrollToBottom() {
        int scrollY = getScrollY();
        int measuredHeight = 0;
        if (mContentView != null) {
            //内容高度
            measuredHeight = mContentView.getMeasuredHeight();
        }
        //scrollview高度
        int height = getMeasuredHeight();
        if (scrollY == measuredHeight - height) {
            //滑动到底部
            return true;
        } else {
            return false;
        }
    }
}
