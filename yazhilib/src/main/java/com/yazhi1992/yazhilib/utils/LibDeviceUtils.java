package com.yazhi1992.yazhilib.utils;

import android.os.Build;

/**
 * Created by zengyazhi on 2018/5/12.
 */

public class LibDeviceUtils {

    //获取品牌
    public static String getBand() {
        return Build.BRAND;
    }

    //获取型号
    public static String getModel() {
        return Build.MODEL;
    }

    //获取系统版本号
    public static String getAndroidVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }
}
