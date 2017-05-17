package com.yazhi1992.yazhilib.widget.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by zengyazhi on 17/5/16.
 * <p>
 * 对话框的基类
 */

public abstract class BaseDialog<T extends BaseDialog<T>> extends Dialog {
    private Context mContext;
    /*是否点击对话框以外区域关闭对话框*/
    private boolean mCancel;
    /*最顶层容器*/
    protected LinearLayout mTopLayout;
    /*用于控制布局高度的容器*/
    protected LinearLayout mControlHeightLayout;
    /*创建出来的子view*/
    protected View mOnCreateView;
    /*最大高度*/
    protected int mMaxHeight;
    /*高度比例*/
    protected float mHeightScale;
    private DisplayMetrics mDisplayMetrics;

    public BaseDialog(@NonNull Context context) {
        super(context);
        setDialogTheme();
        mContext = context;
        setCanceledOnTouchOutside(true);
    }

    /**
     * 设置对话框主题
     */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        mMaxHeight = mDisplayMetrics.heightPixels;

        mTopLayout = new LinearLayout(mContext);
        mTopLayout.setGravity(Gravity.CENTER);

        mControlHeightLayout = new LinearLayout(mContext);
        mControlHeightLayout.setOrientation(LinearLayout.VERTICAL);

        mOnCreateView = onCreateView();
        mControlHeightLayout.addView(mOnCreateView);
        mTopLayout.addView(mControlHeightLayout);

        setContentView(mTopLayout, new ViewGroup.LayoutParams(mDisplayMetrics.widthPixels, mMaxHeight));

        mTopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancel) {
                    dismiss();
                }
            }
        });

        mOnCreateView.setClickable(true);
    }

    /**
     * 当dialog依附在window上，设置对话框高度及动画
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        setBeforeShow();
        int height;
        if (mHeightScale == 0) {
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            height = (int) (mMaxHeight * mHeightScale);
        }
        mControlHeightLayout.setLayoutParams(new LinearLayout.LayoutParams(mDisplayMetrics.widthPixels, height));
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        mCancel = cancel;
    }

    /**
     * 设置高度，占屏幕高度的比例0-1
     *
     * @param heightScale
     * @return
     */
    public T heightScale(float heightScale) {
        mHeightScale = heightScale;
        return (T) this;
    }

    /**
     * 构造对话框需要显示的布局
     *
     * @return
     */
    public abstract View onCreateView();

    /**
     * 显示对话框之前的设置
     */
    public abstract void setBeforeShow();

}
