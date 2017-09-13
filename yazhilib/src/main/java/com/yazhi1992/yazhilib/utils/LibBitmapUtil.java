package com.yazhi1992.yazhilib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.fudaojun.fudaojunlib.widget.ScannerClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zengyazhi on 17/5/17.
 */

public class LibBitmapUtil {

    private LibBitmapUtil() {
    }

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
     *
     * @param bitmap
     * @param path   保存的地址
     * @return
     * @throws IOException
     */
    public static String saveBitmap(Bitmap bitmap, String path) {
        return saveBitmap(null, bitmap, path, 90, null);
    }

    /**
     * 保存bitmap到本地，并通知相册刷新图片
     *
     * @param bitmap
     * @param path   保存的地址
     * @return
     * @throws IOException
     */
    public static String saveBitmap(Context context, Bitmap bitmap, String path, int quality, ScannerClient.ScanListener listener) {
        String result = null;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
            result = path;
            insertImageToGallery(context, path, null, listener);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LibSentryUtil.captureMessage("BitmapUtils saveBitmap " + e.toString());
            result = null;
        } catch (IOException e) {
            LibSentryUtil.captureMessage("BitmapUtils saveBitmap " + e.toString());
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    /**
     * 保存图片到相册
     *
     * @param context 要使用 getApplicationContext()
     * @param path
     * @param type
     */
    public static void insertImageToGallery(Context context, String path, String type, ScannerClient.ScanListener l) {
        new ScannerClient(context, new File(path), type, l);
    }

    /**
     * 指定尺寸缩放bitmap
     *
     * @param originBitmap
     * @param width        缩放后的宽
     * @param height       缩放后的高
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap originBitmap, int width, int height) {
        //缩放至固定尺寸
        int originWidth = originBitmap.getWidth();
        int originHeight = originBitmap.getHeight();
        float scaleWidth = width * 1f / originWidth;
        float scaleHeight = height * 1f / originHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(originBitmap, 0, 0, originWidth, originHeight, matrix, true);
    }

    /**
     * 获取图片的旋转角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照指定的角度进行旋转
     *
     * @param bitmap 需要旋转的图片
     * @param degree 指定的旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBitmap;
    }

    /**
     * 获取我们需要的整理过旋转角度的Uri
     *
     * @param activity 上下文环境
     * @param path     路径
     * @return 正常的Uri
     */
    public static Uri getRotatedUri(Activity activity, String path) {
        int degree = LibBitmapUtil.getBitmapDegree(path);
        if (degree != 0) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Bitmap newBitmap = LibBitmapUtil.rotateBitmapByDegree(bitmap, degree);
            return Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), newBitmap, null, null));
        } else {
            return Uri.fromFile(new File(path));
        }
    }

    /**
     * 将图片按照指定的角度进行旋转
     *
     * @param path   需要旋转的图片的路径
     * @param degree 指定的旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(String path, int degree) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return rotateBitmapByDegree(bitmap, degree);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 计算缩放比
     *
     * @param opts
     * @return
     */
    private static void calculateSize(BitmapFactory.Options opts) {
        int w = opts.outWidth;
        int h = opts.outHeight;
        float standardW = 480f;
        float standardH = 800f;
        int zoomRatio = 1;
        if (w > h) {
            if (w > standardW)
                zoomRatio = (int) (w / standardW);
        } else {
            if (h > standardH)
                zoomRatio = (int) (h / standardH);
        }
        if (zoomRatio <= 0) zoomRatio = 1;

        opts.inSampleSize = zoomRatio;
    }

    /**
     * 计算缩放比
     *
     * @param opts
     * @return
     */
    private static void calculateSize(BitmapFactory.Options opts, float width) {
        int w = opts.outWidth;
        int h = opts.outHeight;
        float standardW = width;
        float standardH = width;
        int zoomRatio = 1;
        if (w > h) {
            if (w > standardW)
                zoomRatio = (int) (w / standardW);
        } else {
            if (h > standardH)
                zoomRatio = (int) (h / standardH);
        }
        if (zoomRatio <= 0) zoomRatio = 1;

        opts.inSampleSize = zoomRatio;
    }

    /**
     * 根据图片路径获取本地图片的Bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getBitmapByUrl(String url) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        calculateSize(options);
        options.inJustDecodeBounds = false;

        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(url);
            bitmap = BitmapFactory.decodeStream(fis, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bitmap = null;
            LibSentryUtil.captureMessage("图片地址解析错误 url:" + url);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fis = null;
            }
        }
        return bitmap;
    }

    /**
     * 根据图片路径获取本地图片的Bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getBitmapByUrl(String url, float bitmapWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        calculateSize(options, bitmapWidth);
        options.inJustDecodeBounds = false;

        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(url);
            bitmap = BitmapFactory.decodeStream(fis, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bitmap = null;
            LibSentryUtil.captureMessage("图片地址解析错误 url:" + url);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fis = null;
            }
        }
        return bitmap;
    }

    /**
     * 根据图片地址得到旋转角度
     * @param url
     * @return
     */
    public static int getPhotoDegree(String url) {
        ExifInterface exifInterface = null;
        int degree = 0;
        try {
            exifInterface = new ExifInterface(url);
            int intDegree = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (intDegree) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}
