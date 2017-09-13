package com.yazhi1992.yazhilib.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by zengyazhi on 17/5/17.
 */

public class LibCalcUtil {

    private LibCalcUtil() {
    }

    // TODO: 2017/9/13 修改为不必引用上下文
    // TODO: 2017/9/13 roundloadingview设置背景色bug
    // TODO: 2017/9/13 添加网络层

    public static float dp2px(Context Context, int dp) {
        final float scale = Context.getResources().getDisplayMetrics().densityDpi;
        return dp * (scale / 160) + 0.5f;
    }

    public static float dx2dp(Context Context, int px) {
        final float scale = Context.getResources().getDisplayMetrics().densityDpi;
        return (px * 160) / scale + 0.5f;
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 得到文件的MD5值
     *
     * @param path 文件地址
     * @return
     */
    public static String getFileMD5(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 颜色渐变
     * @param startValue 起始颜色
     * @param endValue 终止颜色
     * @param fraction 插值
     * @return
     */
    public static int evaluateColor(int startValue, int endValue, float fraction) {
        if (fraction <= 0) {
            return startValue;
        }
        if (fraction >= 1) {
            return endValue;
        }
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) | ((startR + (int) (fraction * (endR - startR))) << 16) | ((startG + (int) (fraction * (endG - startG))) << 8) | ((startB + (int) (fraction * (endB - startB))));
    }
}
