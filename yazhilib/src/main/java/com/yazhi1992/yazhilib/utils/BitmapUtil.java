package com.yazhi1992.yazhilib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    public static Bitmap getBitmap(Context context, int width, int height, int drawble) {
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

    /**
     * 保存bitmap到本地
     * @param bitmap
     * @param path 保存的地址
     * @return
     * @throws IOException
     */
    public static void saveBitmap(Bitmap bitmap, String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            localFile.mkdirs();
        }
        return path;
    }

    /**
     * 指定尺寸缩放bitmap
     * @param originBitmap
     * @param width 缩放后的宽
     * @param height 缩放后的高
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap originBitmap , int width, int height) {
        //缩放至固定尺寸
        int originWidth = originBitmap.getWidth();
        int originHeight = originBitmap.getHeight();
        float scaleWidth = width * 1f / originWidth;
        float scaleHeight = height * 1f / originHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(originBitmap, 0, 0, originWidth, originHeight, matrix, true);
    }
}
