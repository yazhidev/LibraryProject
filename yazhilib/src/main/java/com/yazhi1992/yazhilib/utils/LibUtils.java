package com.yazhi1992.yazhilib.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zengyazhi on 17/7/4.
 */

public class LibUtils {

    private static final String DEBUG_TAG = "zyz";

    private LibUtils() {
    }

    /**
     * 获得包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;

    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获得freso使用的资源图片的uri
     *
     * @param context
     * @param resourseId
     * @return
     */
    public static Uri getFrescoUriForResource(Context context, int resourseId) {
        String packageName = getPackageName(context);
        if (packageName != null) {
            Uri parse = Uri.parse("res://" + packageName + "/" + resourseId);
            return parse;
        } else {
            return null;
        }
    }

    /**
     * 获得freso使用的本地图片的uri
     *
     * @param context
     * @param localPath
     * @return
     */
    public static Uri getFrescoUriForLocalPicture(Context context, String localPath) {
        String packageName = getPackageName(context);
        if (packageName != null) {
            Uri parse = Uri.parse("file://" + packageName + localPath);
            return parse;
        } else {
            return null;
        }
    }

    /**
     * 删除文件
     *
     * @param context
     * @param path
     */
    public static void deleteFile(Context context, String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
                //通知删除文件，否则相册里会有灰色的空白图
                Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                media.setData(contentUri);
                context.sendBroadcast(media);
            }
        }
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

    /**
     * 强制隐藏软键盘
     */
    public static void hideKeyboard(final View mOutView) {
        if (LibUtils.isKeyBoardShowing(mOutView)) {
            InputMethodManager imm = (InputMethodManager) mOutView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
            if (imm.isActive()) {//如果开启
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            }
        }
    }

    /**
     * 显示软键盘
     * 需要在主线程调用，且建议使用 postDelay 延迟 100 ms 调用
     */
    public static void showKeyoard(final Context context, final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 200);
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 根据屏幕宽度与密度计算GridView显示的列数， 最少为三列，并获取Item宽度
     */
    public static int getImageItemWidth(Activity activity) {
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        int densityDpi = activity.getResources().getDisplayMetrics().densityDpi;
        int cols = screenWidth / densityDpi;
        cols = cols < 3 ? 3 : cols;
        int columnSpace = (int) (2 * activity.getResources().getDisplayMetrics().density);
        return (screenWidth - columnSpace * (cols - 1)) / cols;
    }

    /**
     * 判断SDCard是否可用
     */
    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机大小（分辨率）
     */
    public static DisplayMetrics getScreenPix(Activity activity) {
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
        return displaysMetrics;
    }

    /**
     * 读取assets文件的内容
     *
     * @param context  上下文环境
     * @param fileName 要读取的文件名称
     */
    public static String readFromAsset(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            LibUtils.myLog("ReadFromAssets Exception" + e);
        }
        return "";
    }

    public static void myLog(String msg) {
        myLog(DEBUG_TAG, msg);
    }

    public static void myLog(String title, String msg) {
        if (msg == null) {
            log(title, "msg is null");
            return;
        }
        if (msg.length() > 4000) {
            //超过长度，分段
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length()) {
                    log(title, msg.substring(i, i + 4000));
                } else {
                    log(title, msg.substring(i, msg.length()));
                }
            }
        } else {
            log(title, msg);
        }
    }

    public static void log(String title, String msg) {
        if (isMainThread()) {
            Log.e(title, "---- main ---- " + msg);
        } else {
            Log.e(title, msg);
        }
    }

    /**
     * 判断是否是主线程
     *
     * @return
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * MD5加密，32位小写
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        md5.update(str.getBytes());
        byte[] md5Bytes = md5.digest();
        StringBuilder hexValue = new StringBuilder();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 对于没有通话功能的设备，它会返回一个唯一的device ID
     *
     * @return
     */
    public static String getSerialNum() {
        String serial = Build.SERIAL;
        if (serial == null || serial.isEmpty()) {
            LibUtils.myLog("getSerialNum error");
            return "";
        } else {
            return serial;
        }
    }

    /**
     * 需要 READ_PHONE_STATE 权限。 (Android 6.0 以上需要用户手动赋予该权限)
     * 非电话设备或者 Device ID 不可用时，返回 null
     * 某些设备上该方法存在 Bug ，返回的结果可能是一串0或者一串*号
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
//        android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceId = tm.getDeviceId();
        String deviceId = "test";
        if (deviceId == null || deviceId.isEmpty()) {
            LibUtils.myLog("getDeviceId error");
            return "";
        } else {
            return deviceId;
        }
    }

    public static String getSimSerialNumber(Context context) {
//        android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String simSerialNumber = tm.getSimSerialNumber();
        String simSerialNumber = "";
        if (simSerialNumber == null || simSerialNumber.isEmpty()) {
            LibUtils.myLog("getSimSerialNumber error");
            return "";
        } else {
            return simSerialNumber;
        }
    }

    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (androidId == null || androidId.isEmpty()) {
            LibUtils.myLog("getAndroidId error");
            return "";
        } else {
            return androidId;
        }
    }

    /**
     * 获得唯一标识符
     *
     * @return
     */
    public static String getClientId(Context context) {
        String serialNum = getSerialNum();
        String androidId = getAndroidId(context);
        String strRand = "";
        for (int i = 0; i < 6; i++) {
            strRand += String.valueOf((int) (Math.random() * 10));
        }
        LibUtils.myLog("serialNum " + serialNum
                + " androidId " + androidId
                + " strRand " + strRand);
        String uniqueStr = md5(serialNum + androidId + strRand);
        LibUtils.myLog("getClientId " + uniqueStr);
        return uniqueStr;
    }

    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean listIsNullOrEmpty(List<Object> str) {
        if (str == null || str.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean notNullNorEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static boolean listNotNullNorEmpty(List<Object> list) {
        return list != null && !list.isEmpty();
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String showDecimalValue(int value) {
        String str = value + ".00";
        LibUtils.myLog("int " + value + " --> " + str);
        return str;
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String showDecimalValue(double value) {
        String str = String.format("%.2f", value);
        LibUtils.myLog("double " + value + " --> " + str);
        return str;
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String showDecimalValue(String value) {
        String str;
        if (value.contains(".")) {
            int index = value.indexOf(".");
            int length = value.length();
            if (index == length - 1) {
                //1.
                str = value + "00";
            } else if (index == length - 2) {
                //1.x
                str = value + "0";
            } else if (index == length - 3) {
                //1.12
                str = value;
            } else {
                //1.123
                str = value.substring(0, index + 3);
            }
        } else {
            str = value + ".00";
        }
        LibUtils.myLog("String " + value + " --> " + str);
        return str;
    }

    /**
     * 中文正则
     *
     * @param string
     * @return
     */
    public static String stringFilter(String string) {
        // 不允许输入中文
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        return m.replaceAll("").trim();
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    private static Properties sBuildProperties;
    private static final Object sBuildPropertiesLock = new Object();
    private static final File BUILD_PROP_FILE = new File(Environment.getRootDirectory(), "build.prop");

    private static Properties getBuildProperties() {
        synchronized (sBuildPropertiesLock) {
            if (sBuildProperties == null) {
                sBuildProperties = new Properties();
                try {
                    sBuildProperties.load(new FileInputStream(BUILD_PROP_FILE));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sBuildProperties;
    }

    public static boolean isMIUI() {
        return getBuildProperties().containsKey("ro.miui.ui.version.name");
    }

    /**
     * 检测app是否安装
     * @param packageName
     * @param context
     * @return
     */
    public static boolean isAppInstall(String packageName, Context context) {
        //判断是否安装
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getApplicationInfo(packageName ,PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 跳转app
     * @param packageName
     * @param context
     */
    public static void gotoApp(String packageName, Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * 复制文本内容放到系统剪贴板里
     * @param context
     * @param str
     */
    public static void copyToClipboard(Context context, String str) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //创建ClipData对象
        ClipData clipData = ClipData.newPlainText("taobao text", str);
        //添加ClipData对象到剪切板中
        if(cm != null) {
            cm.setPrimaryClip(clipData);
        }
    }

    /**
     * 选择浏览器打开链接
     * @param context
     * @param url
     * @return false 代表没有浏览器可选择
     */
    public static boolean chooseBrowserOpenLink(Context context, String url) {
        //从其他浏览器打开
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        if(intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
            return true;
        } else {
            return false;
        }
    }
}

