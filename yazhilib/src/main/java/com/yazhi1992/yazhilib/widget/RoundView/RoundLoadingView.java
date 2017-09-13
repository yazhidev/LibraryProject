package com.yazhi1992.yazhilib.widget.RoundView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
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
 * <p>
 * 带有倒计时功能、loading加载中、设置圆角/可用/点击颜色的textview
 */

public class RoundLoadingView extends RelativeLayout {
    private RoundViewDelegate mDelegate;
    private TextView mTextView;
    private ProgressWheel mProgressWheel;
    //显示的文字
    private String mNormalText;
    private float mTextSize;
    private int mTextColor;
    private int mTextPressColor;
    private int mTextDisableColor;
    private Paint mTextPaint;
    //loading控件 半径
    private int mCircleRadiu;
    //loading控件 颜色
    private int mCircleColor;
    //loading控件 宽度
    private int mCircleWidth;
    private Context mContext;
    //未开启计时
    private int mSecond = -1;
    //计时器
    private VideoTimerTask mVideoTimerTask;
    private Timer mVideoTimer;
    private Handler mHandler;
    //倒计时字样
    private String mTimeText1 = "";
    private String mTimeText2 = " 秒后重获";
    //默认的是否可用状态
    private boolean mEnable;

    public RoundLoadingView(Context context) {
        this(context, null);
    }

    public RoundLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public int getCircleRadiu() {
        return mCircleRadiu;
    }

    public void setCircleRadiu(int circleRadiu) {
        mCircleRadiu = circleRadiu;
        mProgressWheel.setCircleRadius(mCircleRadiu);
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        mProgressWheel.setBarColor(mCircleColor);
    }

    public int getCircleWidth() {
        return mCircleWidth;
    }

    public void setCircleWidth(int circleWidth) {
        mCircleWidth = circleWidth;
        mProgressWheel.setBarWidth(mCircleWidth);
    }

    public RoundLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mDelegate = new RoundViewDelegate(this, context, attrs);

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundLoadingView, 0, defStyleAttr);
            mNormalText = typedArray.getString(R.styleable.RoundLoadingView_rv_text);
            mTextColor = typedArray.getColor(R.styleable.RoundLoadingView_rv_textColor, Color.parseColor("#333333"));
            mTextSize = typedArray.getInteger(R.styleable.RoundLoadingView_rv_textSize, 15);
            mTextPressColor = typedArray.getColor(R.styleable.RoundLoadingView_rv_textPressColor, Color.parseColor("#333333"));
            mTextDisableColor = typedArray.getColor(R.styleable.RoundLoadingView_rv_textDisableColor, Color.parseColor("#333333"));
            mCircleColor = typedArray.getColor(R.styleable.RoundLoadingView_rv_circleColor, Color.parseColor("#ffffff"));
            mCircleWidth = typedArray.getDimensionPixelSize(R.styleable.RoundLoadingView_rv_circleWidth, (int) CalcUtil.dp2px(context, 2));
            mCircleRadiu = typedArray.getDimensionPixelSize(R.styleable.RoundLoadingView_rv_circleRadius, (int) CalcUtil.dp2px(context, 16));
            mEnable = typedArray.getBoolean(R.styleable.RoundLoadingView_rv_enable, true);
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
        //只显示当行
        mTextView.setSingleLine(true);
        LayoutParams layoutParams1 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.addRule(CENTER_IN_PARENT);
        mTextView.setLayoutParams(layoutParams1);
        addView(mTextView);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);

        //添加加载控件
        mProgressWheel = new ProgressWheel(mContext);
        mProgressWheel.setVisibility(View.INVISIBLE);
        LayoutParams layoutParams = new LayoutParams(2 * mCircleRadiu, 2 * mCircleRadiu);
        layoutParams.addRule(CENTER_IN_PARENT);
        mProgressWheel.setLayoutParams(layoutParams);
        mProgressWheel.setBarWidth(mCircleWidth);
        mProgressWheel.setCircleRadius(mCircleRadiu);
        mProgressWheel.setBarColor(mCircleColor);
        addView(mProgressWheel);

        if (!mEnable) {
            setEnabled(false);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //文字  radiu*2  padding
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        int computeWidth = 0;
        int computeHeight = 0;

        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            computeWidth = Math.max(computeWidth, measuredWidth);
            computeHeight = Math.max(computeHeight, measuredHeight);
        }

        computeWidth += (getPaddingLeft() + getPaddingRight());
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                //wrap_content
                //不超过父控件范围
                widthSize = computeWidth > widthSize ? widthSize : computeWidth;
                break;
            case MeasureSpec.UNSPECIFIED:
                //自由发挥
                widthSize = computeWidth;
                break;
            case MeasureSpec.EXACTLY:
                break;
            default:
                break;
        }

        computeHeight += (getPaddingTop() + getPaddingBottom());
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                heightSize = computeHeight > heightSize ? heightSize : computeHeight;
                break;
            case MeasureSpec.UNSPECIFIED:
                heightSize = computeHeight;
                break;
            case MeasureSpec.EXACTLY:
                break;
            default:
                break;
        }

        setMeasuredDimension(widthSize, heightSize);
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
        if (mProgressWheel == null || loading == mProgressWheel.isSpinning() || mSecond != -1 || !isEnabled())
            return;
        if (loading) {
            //开始选旋转
            mTextView.setVisibility(View.INVISIBLE);
            mProgressWheel.setVisibility(View.VISIBLE);
            mProgressWheel.spin();
        } else {
            mTextView.setVisibility(View.VISIBLE);
            mProgressWheel.setVisibility(View.INVISIBLE);
            mProgressWheel.stopSpinning();
        }
    }

    /**
     * 设置显示文字
     *
     * @param str
     */
    public void setText(String str) {
        mNormalText = str;
        if (mTextView != null) {
            mTextView.setText(str);
        }
    }

    /**
     * 单位sp
     *
     * @param size
     */
    public void setTextSize(float size) {
        mTextSize = size;
        mTextView.setTextSize(size);
    }

    private void setTextColor() {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{new int[]{-state_pressed, state_enabled}, new int[]{state_pressed, state_enabled}, new int[]{-state_enabled}},
                new int[]{mTextColor, mTextPressColor, mTextDisableColor});
        mTextView.setTextColor(colorStateList);
    }

    public float getTextSize() {
        return mTextSize;
    }

    public int getTextPressColor() {
        return mTextPressColor;
    }

    public void setTextPressColor(int textPressColor) {
        mTextPressColor = textPressColor;
        setTextColor();
    }

    public int getTextDisableColor() {
        return mTextDisableColor;
    }

    public void setTextDisableColor(int textDisableColor) {
        mTextDisableColor = textDisableColor;
        setTextColor();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int color) {
        mTextColor = color;
        setTextColor();
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
        setEnabled(mEnable);
    }


    /**
     * 开始倒计时，可以通过{@link #setTextWhenCountDown(String, String)}设置倒计时时显示的字样
     *
     * @param time 倒计时时间
     */
    public void startTimer(int time) {
        if (!isEnabled()) return;
        stopTime();
        setLoading(false);
        mTextView.setText(mTimeText1 + time + mTimeText2);
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
                            stopTime();
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

    @Override
    public void setEnabled(boolean enabled) {
        //记录设置的状态
        mEnable = enabled;
        //如果还在倒计时则等待倒计时结束后设置状态
        if (mSecond == -1) {
            super.setEnabled(enabled);
        }
    }

    public RoundViewDelegate getDelegate() {
        return mDelegate;
    }
}
