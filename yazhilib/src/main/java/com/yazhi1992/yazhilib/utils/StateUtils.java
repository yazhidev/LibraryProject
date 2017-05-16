package com.yazhi1992.yazhilib.utils;

import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by zengyazhi on 17/5/16.
 */

public class StateUtils {

    /**
     * 获得软键盘是否调起
     *
     * @param rootView
     * @return
     */
    public static boolean isKeyBoardShowing(View rootView) {
        final int softHeight = 100;
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        DisplayMetrics displayMetrics = rootView.getResources().getDisplayMetrics();
        int i = rootView.getBottom() - rect.bottom;
        return i > softHeight * displayMetrics.density;
    }
}
