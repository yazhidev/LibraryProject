package com.yazhi1992.yazhilib.widget.RoundView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zengyazhi on 17/5/17.
 */

public class RoundTextView extends TextView {
    private RoundViewDelegate mDelegate;
    private int mWidth;
    private int mHeight;

    public RoundTextView(Context context) {
        this(context, null);
    }

    public RoundTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDelegate = new RoundViewDelegate(this, context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mDelegate.isCircleRound()) {
            mDelegate.setCornerRadius(getHeight() / 2);
        } else {
            mDelegate.setBgSelector();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }
}
