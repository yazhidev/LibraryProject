package com.yazhi1992.yazhilib.utils;

import android.content.Context;
import android.util.TypedValue;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

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

}
