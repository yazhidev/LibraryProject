package com.yazhi1992.yazhilib.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by zengyazhi on 17/7/4.
 */

public class LibFileUtils {

    private LibFileUtils() {
    }

    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            FileInputStream fis = null;
            if (file.exists()) {
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                Log.e("获取文件大小", "文件不存在!");
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 将单位为 B 的值转换其他单位
     *
     * @return
     */
    public static String transferSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        boolean file1 = file.isFile();
        boolean exists = file.exists();
        boolean delete = file.delete();
        return file1 && exists && delete;
    }

}
