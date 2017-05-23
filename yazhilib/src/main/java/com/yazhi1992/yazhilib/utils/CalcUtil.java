package com.yazhi1992.yazhilib.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by zengyazhi on 17/5/17.
 */

public class CalcUtil {

    public static float dp2px(Context Context, int dp) {
        final float scale = Context.getResources().getDisplayMetrics().densityDpi;
        return dp * (scale / 160) + 0.5f;
    }

    public static float dx2dp(Context Context, int px) {
        final float scale = Context.getResources().getDisplayMetrics().densityDpi;
        return (px * 160) / scale + 0.5f;
    }

}
