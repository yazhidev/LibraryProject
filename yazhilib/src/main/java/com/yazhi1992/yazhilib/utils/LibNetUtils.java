package com.yazhi1992.yazhilib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zengyazhi on 17/7/15.
 */

public class LibNetUtils {
    private LibNetUtils() {
    }

    /**
     * 检查网络连接
     *
     * @param context
     * @return true 有网络
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {     //有网络连接
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 是否是wifi状态
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                int type = networkInfo.getType();
                LibUtils.myLog("是否是wifi(1是) " + type);
                return type == ConnectivityManager.TYPE_WIFI;
            }
        }
        return false;
    }
}
