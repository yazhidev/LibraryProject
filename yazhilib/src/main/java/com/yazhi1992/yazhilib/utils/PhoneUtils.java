package com.yazhi1992.yazhilib.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.io.File;

/**
 * Created by zengyazhi on 17/5/27.
 */

public class PhoneUtils {

    private PhoneUtils() {
    }

    /**
     * 是否为平板
     *
     * @param context
     * @return
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获得保存图片的地址
     *
     * @return
     */
    public static String getSavePicDir() {
        String path = Environment.getExternalStorageDirectory() + "/MyPic";
        File localFile = new File(path);
        if (!localFile.exists()) {
            //如果没有则创建文件夹
            if (!localFile.mkdirs()) {
                //创建文件夹失败
                path = Environment.getExternalStorageDirectory() + "";
            }
        }
        return path;
    }

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

    public static void myLog(String str) {
        Log.e("zyz", str);
    }
}
