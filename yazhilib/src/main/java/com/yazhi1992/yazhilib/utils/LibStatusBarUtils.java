package com.yazhi1992.yazhilib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zengyazhi on 2017/8/17.
 */

public class LibStatusBarUtils {
    private Activity mActivity;
    //状态栏颜色
    private int mColor = -1;
    //状态栏drawble
    private Drawable mDrawable;
    //是否是最外层布局是 DrawerLayout 的侧滑菜单
    private boolean mIsDrawerLayout;
    //是否包含 ActionBar
    private boolean mIsActionBar;
    //侧滑菜单页面的内容视图
    private int mContentResourseIdInDrawer;
    //是否是深色状态栏
    private boolean mIsDarkStatusBar;

    public LibStatusBarUtils(Activity activity) {
        mActivity = activity;
    }

    public static LibStatusBarUtils with(Activity activity) {
        return new LibStatusBarUtils(activity);
    }

    public int getColor() {
        return mColor;
    }

    public LibStatusBarUtils setColor(int color) {
        mColor = color;
        return this;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public LibStatusBarUtils setDrawable(Drawable drawable) {
        mDrawable = drawable;
        return this;
    }

    public boolean isDrawerLayout() {
        return mIsDrawerLayout;
    }

    public boolean isActionBar() {
        return mIsActionBar;
    }

    public LibStatusBarUtils setIsActionBar(boolean actionBar) {
        mIsActionBar = actionBar;
        return this;
    }

    public LibStatusBarUtils setIsDarkStatusBar(boolean isDark) {
        mIsDarkStatusBar = isDark;
        return this;
    }

    public boolean isDarkStatusBar() {
        return mIsDarkStatusBar;
    }

    /**
     * 是否是最外层布局为 DrawerLayout 的侧滑菜单
     *
     * @param drawerLayout 是否最外层布局为 DrawerLayout
     * @param contentId    内容视图的 id
     * @return
     */
    public LibStatusBarUtils setDrawerLayoutContentId(boolean drawerLayout, int contentId) {
        mIsDrawerLayout = drawerLayout;
        mContentResourseIdInDrawer = contentId;
        return this;
    }

    public void init() {
        setStatusBarMode(mActivity, isDarkStatusBar());
        fullScreen(mActivity);
        if (mColor != -1) {
            //设置了状态栏颜色
            addStatusViewWithColor(mActivity, mColor);
        }
        if (mDrawable != null) {
            //设置了状态栏 drawble，例如渐变色
            addStatusViewWithDrawble(mActivity, mDrawable);
        }
        if (isDrawerLayout()) {
            //未设置 fitsSystemWindows 且是侧滑菜单，需要设置 fitsSystemWindows 以解决 4.4 上侧滑菜单上方白条问题
            fitsSystemWindows(mActivity);
        }
        if (isActionBar()) {
            //要增加内容视图的 paddingTop,否则内容被 ActionBar 遮盖
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ViewGroup rootView = (ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
                rootView.setPadding(0, getStatusBarHeight(mActivity) + getActionBarHeight(mActivity), 0, 0);
            }
        }
    }

    /**
     * 去除 ActionBar 阴影
     */
    public LibStatusBarUtils clearActionBarShadow() {
        if (Build.VERSION.SDK_INT >= 21) {
            ActionBar supportActionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setElevation(0);
            }
        }
        return this;
    }

    /**
     * 设置页面最外层布局 FitsSystemWindows 属性
     *
     * @param activity
     */
    private void fitsSystemWindows(Activity activity) {
        ViewGroup contentFrameLayout = (ViewGroup) activity.findViewById(android.R.id.content);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
            //布局预留状态栏高度的 padding
            if (parentView instanceof DrawerLayout) {
                DrawerLayout drawer = (DrawerLayout) parentView;
                //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
                drawer.setClipToPadding(false);
            }
        }
    }

    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("getStatusBarHeight", result + "");
        return result;
    }

    /**
     * 获得 ActionBar 的高度
     *
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            TypedValue tv = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
            result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return result;
    }

    /**
     * 添加状态栏占位视图
     *
     * @param activity
     */
    private void addStatusViewWithColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isDrawerLayout()) {
                //要在内容布局增加状态栏，否则会盖在侧滑菜单上
                ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
                //DrawerLayout 则需要在第一个子视图即内容试图中添加padding
                View parentView = rootView.getChildAt(0);
                LinearLayout linearLayout = new LinearLayout(activity);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                View statusBarView = new View(activity);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(activity));
                statusBarView.setBackgroundColor(color);
                //添加占位状态栏到线性布局中
                linearLayout.addView(statusBarView, lp);
                //侧滑菜单
                DrawerLayout drawer = (DrawerLayout) parentView;
                //内容视图
                View content = activity.findViewById(mContentResourseIdInDrawer);
                //将内容视图从 DrawerLayout 中移除
                drawer.removeView(content);
                //添加内容视图
                linearLayout.addView(content, content.getLayoutParams());
                //将带有占位状态栏的新的内容视图设置给 DrawerLayout
                drawer.addView(linearLayout, 0);
            } else {
                //设置 paddingTop
                ViewGroup rootView = (ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
                rootView.setPadding(0, getStatusBarHeight(mActivity), 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //直接设置状态栏颜色
                    activity.getWindow().setStatusBarColor(color);
                } else {
                    //增加占位状态栏
                    ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
                    View statusBarView = new View(activity);
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            getStatusBarHeight(activity));
                    statusBarView.setBackgroundColor(color);
                    decorView.addView(statusBarView, lp);
                }
            }
        }
    }

    /**
     * 添加状态栏占位视图
     *
     * @param activity
     */
    private void addStatusViewWithDrawble(Activity activity, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //占位状态栏
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                statusBarView.setBackground(drawable);
            } else {
                statusBarView.setBackgroundDrawable(drawable);
            }
            if (isDrawerLayout()) {
                //要在内容布局增加状态栏，否则会盖在侧滑菜单上
                ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
                //DrawerLayout 则需要在第一个子视图即内容试图中添加padding
                View parentView = rootView.getChildAt(0);
                LinearLayout linearLayout = new LinearLayout(activity);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                //添加占位状态栏到线性布局中
                linearLayout.addView(statusBarView, lp);
                //侧滑菜单
                DrawerLayout drawer = (DrawerLayout) parentView;
                //内容视图
                View content = activity.findViewById(mContentResourseIdInDrawer);
                //将内容视图从 DrawerLayout 中移除
                drawer.removeView(content);
                //添加内容视图
                linearLayout.addView(content, content.getLayoutParams());
                //将带有占位状态栏的新的内容视图设置给 DrawerLayout
                drawer.addView(linearLayout, 0);
            } else {
                //增加占位状态栏，并增加状态栏高度的 paddingTop
                ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
                decorView.addView(statusBarView, lp);
                //设置 paddingTop
                ViewGroup rootView = (ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
                rootView.setPadding(0, getStatusBarHeight(mActivity), 0, 0);
            }
        }
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = window.getDecorView().getSystemUiVisibility()
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    /**
     * 通过设置全屏，设置状态栏透明 导航栏黑色
     *
     * @param activity
     */
    public static void setStatusTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();

                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
//                attributes.flags |= flagTranslucentStatus;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility()
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationHeight(Context context) {
        int result = 0;
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    /**
     * 需要MIUIV6以上
     *
     * @param activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    private void setStatusBarMode(Activity activity, boolean dark) {
        switch (LibRoomUtils.getLightStatausBarAvailableRomType()) {
            case LibRoomUtils.AvailableRomType.MIUI:
                MIUISetStatusBarLightMode(activity, dark);
                break;
            case LibRoomUtils.AvailableRomType.FLYME:
                FlymeSetStatusBarLightMode(activity, dark);
                break;
            case LibRoomUtils.AvailableRomType.ANDROID_NATIVE:
                setAndroidNativeLightStatusBar(activity, dark);
                break;
            case LibRoomUtils.AvailableRomType.NA:
                // N/A do nothing
                break;
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Activity activity, boolean dark) {
        Window window = activity.getWindow();
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            // We want to change tint color to white again.
            // You can also record the flags in advance so that you can turn UI back completely if
            // you have set other flags before, such as translucent or full screen.
            decor.setSystemUiVisibility(0);
        }
    }
}
