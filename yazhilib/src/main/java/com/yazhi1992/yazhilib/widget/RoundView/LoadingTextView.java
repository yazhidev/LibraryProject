package com.yazhi1992.yazhilib.widget.RoundView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yazhi1992.yazhilib.R;
import com.yazhi1992.yazhilib.utils.CalcUtil;
import com.yazhi1992.yazhilib.widget.ProgressWheel.ProgressWheel;

import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.state_enabled;
import static android.R.attr.state_pressed;

/**
 * Created by zengyazhi on 17/6/1.
 *
 * 带有倒计时功能、loading加载中、设置圆角/可用/点击颜色的textview
 */

public class LoadingTextView extends RelativeLayout {
    private RoundViewDelegate mDelegate;
    protected TextView mTextView;
    protected ProgressWheel mProgressWheel;
    //显示的文字
    private String mNormalText;
    private int mTextSize;
    private int mTextColor;
    private int mTextPressColor;
    private int mTextDisableColor;
    //loading控件 半径
    private int mCircleRadiu;
    //loading控件 颜色
    private int mCircleColor;
    //loading控件 宽度
    private int mCircleWidth;
    private Context mContext;
    private int mSecond = -1; //未开启计时
    private VideoTimerTask mVideoTimerTask;//计时器
    private Timer mVideoTimer;
    private Handler mHandler;
    //倒计时字样
    private String mTimeText1 = "";
    private String mTimeText2 = " 秒后重获";

    public LoadingTextView(Context context) {
        this(context, null);
    }

    public LoadingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mDelegate = new RoundViewDelegate(this, context, attrs);

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingTextView, 0, defStyleAttr);
            mNormalText = typedArray.getString(R.styleable.LoadingTextView_rv_text);
            mTextColor = typedArray.getColor(R.styleable.LoadingTextView_rv_textColor, Color.parseColor("#333333"));
            mTextSize = typedArray.getInteger(R.styleable.LoadingTextView_rv_textSize, 15);
            mTextPressColor = typedArray.getColor(R.styleable.LoadingTextView_rv_textPressColor, Color.parseColor("#333333"));
            mTextDisableColor = typedArray.getColor(R.styleable.LoadingTextView_rv_textDisableColor, Color.parseColor("#333333"));
            mCircleColor = typedArray.getColor(R.styleable.LoadingTextView_rv_circleColor, Color.parseColor("#ffffff"));
            mCircleWidth = typedArray.getDimensionPixelSize(R.styleable.LoadingTextView_rv_circleWidth, (int) CalcUtil.dp2px(context, 2));
            mCircleRadiu = typedArray.getDimensionPixelSize(R.styleable.LoadingTextView_rv_circleRadius, (int) CalcUtil.dp2px(context, 16));
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        //添加textview
        mTextView = new TextView(context);
        if (mNormalText != null && !mNormalText.isEmpty()) {
            mTextView.setText(mNormalText);
        }
        mTextView.setTextSize(mTextSize);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{new int[]{-state_pressed, state_enabled}, new int[]{state_pressed, state_enabled}, new int[]{-state_enabled}},
                new int[]{mTextColor, mTextPressColor, mTextDisableColor});
        mTextView.setTextColor(colorStateList);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setDuplicateParentStateEnabled(true);
        mTextView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mTextView);

        //添加加载控件
        mProgressWheel = new ProgressWheel(mContext);
        LayoutParams layoutParams = new LayoutParams(2 * mCircleRadiu, 2 * mCircleRadiu);
        layoutParams.addRule(CENTER_IN_PARENT);
        mProgressWheel.setLayoutParams(layoutParams);
        mProgressWheel.setBarWidth(mCircleWidth);
        mProgressWheel.setCircleRadius(mCircleRadiu);
        mProgressWheel.setBarColor(mCircleColor);
        addView(mProgressWheel);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mDelegate.isCircleRound()) {
            mDelegate.setCornerRadius(getHeight() / 2);
        } else {
            mDelegate.setBgSelector();
        }
    }

    /**
     * 设置加载状态
     *
     * @param loading true加载/false停止加载
     */
    public void setLoading(boolean loading) {
        if (mProgressWheel == null || loading == mProgressWheel.isSpinning()) return;
        if (loading) {
            //开始选旋转
            mTextView.setText("");
            mProgressWheel.spin();
        } else {
            mProgressWheel.stopSpinning();
            mTextView.setText(mNormalText);
        }
    }

    /**
     * 设置显示文字
     * @param str
     */
    public void setText(String str) {
        mNormalText = str;
        if (mTextView != null) {
            mTextView.setText(str);
        }
    }

    /**
     * 是否正在加载
     *
     * @return
     */
    public boolean isLoading() {
        return mProgressWheel.isSpinning();
    }


    /**
     * 开始倒计时
     *
     * @param time
     */
    private void startTime(int time) {
        if (time > 0) {
            // 时间
            mSecond = time;
            mVideoTimer = new Timer(true);
            mVideoTimerTask = new VideoTimerTask();
            mVideoTimer.schedule(mVideoTimerTask, 1000, 1000);
        }
    }

    /**
     * 停止计时
     */
    private void stopTime() {
        if (null != mVideoTimer) {
            mVideoTimer.cancel();
            mVideoTimer = null;
            mVideoTimerTask.cancel();
            mVideoTimerTask = null;
            mSecond = -1;
        }
        setEnabled(true);
    }


    /**
     * 开始倒计时，可以通过{@link #setTextWhenCountDown(String, String)}设置倒计时时显示的字样
     *
     * @param time 倒计时时间
     */
    public void startTimer(int time) {
        stopTime();
        mTextView.setText(mTimeText1 + time + mTimeText2);
        setLoading(false);
        setEnabled(false);
        if (mHandler == null) {
            mHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case UPDAT_WALL_TIME_TIMER_TASK:
                            updateText();
                            break;
                        case END_TIMER_TASK:
                            mTextView.setText(mNormalText);
                            setEnabled(true);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
        startTime(time);
    }


    /**
     * 记时器
     */
    private class VideoTimerTask extends TimerTask {
        public void run() {
            --mSecond;
            if (mSecond < 0) {
                stopTime();
                return;
            }
            mHandler.sendEmptyMessage(UPDAT_WALL_TIME_TIMER_TASK);
        }
    }

    /**
     * 设置倒计时时字样
     *
     * @param text1
     * @param text2
     */
    public void setTextWhenCountDown(String text1, String text2) {
        mTimeText1 = text1;
        mTimeText2 = text2;
    }

    /**
     * 更新textview字样
     */
    private void updateText() {
        if (mSecond == 0) {
            mHandler.sendEmptyMessage(END_TIMER_TASK);
            return;
        }
        if (mTextView != null) {
            mTextView.setText(mTimeText1 + mSecond + mTimeText2);
        }
    }

    private static final int UPDAT_WALL_TIME_TIMER_TASK = 1;
    private static final int END_TIMER_TASK = 2;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mProgressWheel == null || !mProgressWheel.isSpinning()) {
            return super.onTouchEvent(event);
        } else {
            //加载时屏蔽点击事件
            return true;
        }
    }
}
