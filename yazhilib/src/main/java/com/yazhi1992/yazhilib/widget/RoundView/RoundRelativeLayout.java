package com.yazhi1992.yazhilib.widget.RoundView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by zengyazhi on 17/5/18.
 */

public class RoundRelativeLayout extends RelativeLayout {
    private RoundViewDelegate mDelegate;

    public RoundRelativeLayout(Context context) {
        this(context, null);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
}
