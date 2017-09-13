package com.yazhi1992.libraryproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.yazhi1992.yazhilib.widget.Dialog.BaseBottomDialog;
import com.yazhi1992.yazhilib.widget.Dialog.BaseCenterDialog;

/**
 * Created by zengyazhi on 17/5/16.
 */

public class MyBottomDialog extends BaseCenterDialog<MyBottomDialog> {
    private Context mContext;
    private View mView;

    public MyBottomDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateView() {
        return LayoutInflater.from(mContext).inflate(R.layout.dialog, null);
    }

    @Override
    public void setBeforeShow() {

    }


}
