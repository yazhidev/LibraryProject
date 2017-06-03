package com.yazhi1992.yazhilib.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

/**
 * Created by zengyazhi on 17/5/27.
 */

public class PhoneUtils {

    /**
     * 是否为平板
     *
     * @param context
     * @return
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void myLog(String str) {
        Log.e("zyz", str);
    }
}
