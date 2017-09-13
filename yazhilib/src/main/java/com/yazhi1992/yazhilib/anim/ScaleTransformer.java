package com.yazhi1992.yazhilib.anim;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by zengyazhi on 17/6/12.
 *
 * 辅导君首页老师图片滑动效果
 */

public class ScaleTransformer implements ViewPager.PageTransformer {
    private float mMinScale = 0.8f;
    public static final float DEFAULT_CENTER = 0.5f;

    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        page.setPivotY(pageHeight / 2);

        if (position < -1) {  // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setScaleX(mMinScale);
            page.setScaleY(mMinScale);
            page.setPivotX(pageWidth);
        } else if (position <= 1) {  // [-1,1]
            // Modify the default slide transition to shrink the page as well
            //1->2: 1[0,-1] / 2[1,0]
            //2->1: 1[-1,0] / 2[0,1]
            float scaleFactor;
            if (position < 0) {
                scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale;
            } else {
                scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale;
            }
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setPivotX(pageWidth * ((1 - position) * DEFAULT_CENTER));
        } else { //(1,Infinity]
            page.setPivotX(0);
            page.setScaleX(mMinScale);
            page.setScaleY(mMinScale);
        }
    }
}
