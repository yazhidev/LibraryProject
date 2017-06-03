package com.yazhi1992.yazhilib.widget.RoundView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by zengyazhi on 17/5/18.
 */

public class RoundLinearLayout extends LinearLayout {
    private RoundViewDelegate mDelegate;

    public RoundLinearLayout(Context context) {
        this(context, null);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDelegate = new RoundViewDelegate(this, context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(mDelegate == null) return;
        if (mDelegate.isCircleRound()) {
            mDelegate.setCornerRadius(getHeight() / 2);
        } else {
            mDelegate.setBgSelector();
        }
    }

    public RoundViewDelegate getDelegate() {
        return mDelegate;
    }
}
