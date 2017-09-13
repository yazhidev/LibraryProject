package com.yazhi1992.yazhilib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by zengyazhi on 17/7/19.
 */

public class LibDialogUtil {

    private LibDialogUtil() {
    }

    public static void showDialog(AlertDialog dialog) {
        if(dialog == null) return;
        showDialog(dialog, "确认", "取消");
    }

    public static void showDialog(AlertDialog dialog, String positiveBtnText, String negativeBtnText) {
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(positiveBtnText);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText(negativeBtnText);
    }

    public static void hideDialog(AlertDialog dialog, Activity activity) {
        if (dialog != null && dialog.isShowing() && !activity.isFinishing()) {
            dialog.cancel();
        }
    }

    public static AlertDialog getDialog(Context context, String msg, String positiveBtnText, String negativeBtnText, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveBtnText, positiveListener)
                .setNegativeButton(negativeBtnText, negativeListener);
        return builder.create();
    }

    public static AlertDialog getDialog(Context context, String msg, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        return getDialog(context, msg, "确认", "取消", positiveListener, negativeListener);
    }

}
