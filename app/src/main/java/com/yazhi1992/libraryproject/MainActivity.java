package com.yazhi1992.libraryproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.yazhi1992.yazhilib.anim.ScaleTransformer;
import com.yazhi1992.yazhilib.utils.CalcUtil;
import com.yazhi1992.yazhilib.widget.AutoEditText;
import com.yazhi1992.yazhilib.widget.RoundView.RoundLoadingView;
import com.yazhi1992.yazhilib.widget.RoundView.RoundRelativeLayout;
import com.yazhi1992.yazhilib.widget.RoundView.RoundViewDelegate;
import com.yazhi1992.yazhilib.widget.ViewPagerIndicator;


public class MainActivity extends AppCompatActivity {

    private MyBottomDialog mMyBottomDialog;
    private RoundLoadingView mViewById;
    private ViewPager mVp;
    private ViewPagerIndicator mIndicator;
    private int mPosition;

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return GuideFragment.getInstance(position % 5);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPosition = 1;

        mViewById = (RoundLoadingView) findViewById(R.id.loadingView);
        mVp = (ViewPager) findViewById(R.id.vp);
        mVp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        int count = Integer.MAX_VALUE / 2;
        mVp.setCurrentItem(count - (count % 5));
        mVp.setPageMargin((int) CalcUtil.dp2px(this, 10));
        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mIndicator.setRadius(20);
        mVp.setOffscreenPageLimit(3);
        mVp.setPageTransformer(true, new ScaleTransformer());
        mIndicator.setTotalPages(5);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mMyBottomDialog = new MyBottomDialog(this);
        View viewById1 = findViewById(R.id.outView);
        AutoEditText viewById = (AutoEditText) findViewById(R.id.et);
        viewById.setOnTextLengthListener(new AutoEditText.onTextLengthListener() {
            @Override
            public void onTextLengthChanged(int length) {
                mViewById.setText(length + "-");
            }
        });
        viewById.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewById.setAutoHideSelection(viewById1);
        viewById.setMaxLenght(5);
        viewById1.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            }
        });


        RoundRelativeLayout myRela = (RoundRelativeLayout) findViewById(R.id.myrela);
        RoundViewDelegate delegate = myRela.getDelegate();
        delegate.setBackgroundColor(Color.BLACK);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mViewById.startTimer(5);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewById.setLoading(!mViewById.isLoading());
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewById.setEnabled(!mViewById.isEnabled());
            }
        });

//        mViewById.setTextSize(15);
        mViewById.setTextColor(Color.GREEN);
        mViewById.getDelegate().setCornerRadius(10);
        mViewById.setTextWhenCountDown("还需要等待", "秒后重获");
        mViewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewById.setLoading(true);
                mViewById.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mViewById.startTimer(5);
                    }
                }, 2000);
            }
        });

    }

}
