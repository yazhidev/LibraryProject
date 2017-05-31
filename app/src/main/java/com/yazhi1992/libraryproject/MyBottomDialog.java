package com.yazhi1992.libraryproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.yazhi1992.yazhilib.widget.Dialog.BaseBottomDialog;

/**
 * Created by zengyazhi on 17/5/16.
 */

public class MyBottomDialog extends BaseBottomDialog<MyBottomDialog> {
    private Context mContext;
    private View mView;

    public MyBottomDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateView() {
        MyTimePicker timePicker = new MyTimePicker(mContext);
//        SinglePicker singlePicker = new SinglePicker(mContext);
//        List<String> data = new ArrayList<>();
//        data.add("121");
//        data.add("121");
//        data.add("121");
//        data.add("121");
//        data.add("121");
//        singlePicker.setData(data);
        return timePicker.getContenView();
    }

    @Override
    public void setBeforeShow() {

    }


}
