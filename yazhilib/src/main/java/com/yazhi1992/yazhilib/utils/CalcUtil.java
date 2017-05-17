package com.yazhi1992.yazhilib.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by zengyazhi on 17/5/17.
 */

public class CalcUtil {

    protected static float dp2px(Context Context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Context.getResources().getDisplayMetrics());
    }

}
