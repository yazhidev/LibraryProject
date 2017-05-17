package com.yazhi1992.yazhilib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by zengyazhi on 17/5/17.
 */

public class BitmapUtil {

    /**
     * 根据drawble文件名得到bitmap
     *
     * @param context 用来获得Resources
     * @param width   想要获得的bitmap的宽
     * @param height  想要获得的bitmap的高
     * @param drawble drawble图片名称
     * @return
     */
    protected static Bitmap getBitmap(Context context, int width, int height, int drawble) {
        // 得到图像
        Resources resources = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawble);

        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();

        float scaleX = (float) width / bWidth;
        float scaleY = (float) height / bHeight;

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);

        return Bitmap.createBitmap(bitmap, 0, 0, bWidth, bHeight, matrix, true);
    }
}
