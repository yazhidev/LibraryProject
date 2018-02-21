package com.yazhi1992.yazhilib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.SharedPreferencesCompat;

import java.util.Map;

/**
 * ProjectName：AndroidUtil
 * Description：偏好设置工具类V1版
 * <p>
 */
public final class LibSPUtils {

    private static SharedPreferences sPreferences;

    /**
     * Application中初始化
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        // 默认SharedPreferences的文件名：包名_preferences，模式默认私有访问Context.MODE_PRIVATE
        sPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences getPreferences() {
        if(sPreferences == null) {
            init(LibUtils.getContext());
        }
        return sPreferences;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key    键
     * @param object 值
     */
    public static void put(String key, Object object) {
        SharedPreferences.Editor editor = getPreferences().edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    /**
     * 获取保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key           键
     * @param defaultObject 默认值
     * @return Object，需要转成所需的类型
     */
    public static Object get(String key, Object defaultObject) {
        SharedPreferences sp = getPreferences();

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 获取String值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return String
     */
    public static String getString(final String key, final String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    /**
     * 设置String值
     *
     * @param key   键
     * @param value 值
     */
    public static void setString(final String key, final String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    /**
     * 获取Boolean值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return boolean
     */
    public static boolean getBoolean(final String key, final boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    /**
     * 设置Booelan值
     *
     * @param key   键
     * @param value 值
     */
    public static void setBoolean(final String key, final boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    /**
     * 获取Int值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return int
     */
    public static int getInt(final String key, final int defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    /**
     * 设置Int值
     *
     * @param key   键
     * @param value 值
     */
    public static void setInt(final String key, final int value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    /**
     * 获取Long值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return float
     */
    public static float getFloat(final String key, final float defaultValue) {
        return getPreferences().getFloat(key, defaultValue);
    }

    /**
     * 设置Float值
     *
     * @param key   键
     * @param value 值
     */
    public static void setFloat(final String key, final float value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putFloat(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    /**
     * 获取Long值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return long
     */
    public static long getLong(final String key, final long defaultValue) {
        return getPreferences().getLong(key, defaultValue);
    }

    /**
     * 设置Long值
     *
     * @param key   键
     * @param value 值
     */
    public static void setLong(final String key, final long value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    /**
     * 返回所有的键值对
     *
     * @return 所有的键值对
     */
    public static Map<String, ?> getAll() {
        return getPreferences().getAll();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key 键
     * @return 是|否
     */
    public static boolean contains(String key) {
        return getPreferences().contains(key);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key 键
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.remove(key);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.clear();
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }
}
