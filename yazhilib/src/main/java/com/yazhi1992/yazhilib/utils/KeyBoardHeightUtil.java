package com.yazhi1992.yazhilib.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by zengyazhi on 2018/2/6.
 */

public class KeyBoardHeightUtil {

    private static int mNavigationHeight = -1;
    private static boolean mIsShowing;

    public interface KeyBoardHeightListener {
        void onLayoutListener(int keyboardHeight, boolean isShowing);
    }

    /**
     * 监听布局变化，获取软键盘高度（建议延时225ms显示输入框）
     * @param rootView 根布局
     * @param listener
     */
    public static void getKeyBoardHeight(final View rootView, final KeyBoardHeightListener listener) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取当前界面可视部分，不包含底部导航栏，包含状态栏，以Nexus5为例，未显示软键盘是，top=60,bottom=1776；显示软键盘是，top=60,bottom=973
                rootView.getWindowVisibleDisplayFrame(rect);
                //获得屏幕整体的高度，包含底部导航栏，包含状态栏，以Nexus5为例，值为1920
                int screenHight = rootView.getRootView().getHeight();
                //计算底部导航栏高度
                if (mNavigationHeight < 0) {
                    mNavigationHeight = screenHight - rect.bottom;
                }
                //获得键盘高度
                int keyboardHeight = screenHight - rect.bottom - mNavigationHeight;
                boolean keyboardIsVisible = (double) keyboardHeight / screenHight > 0.3;
                if (mIsShowing != keyboardIsVisible && listener != null) {
                    mIsShowing = keyboardIsVisible;
                    listener.onLayoutListener(keyboardHeight, keyboardIsVisible);
                }
            }
        });
    }
}
