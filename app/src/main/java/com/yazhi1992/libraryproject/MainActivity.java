package com.yazhi1992.libraryproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yazhi1992.yazhilib.widget.AutoEditText;
import com.yazhi1992.yazhilib.widget.RoundView.RoundLoadingView;
import com.yazhi1992.yazhilib.widget.RoundView.RoundRelativeLayout;
import com.yazhi1992.yazhilib.widget.RoundView.RoundViewDelegate;


public class MainActivity extends AppCompatActivity {

    private MyBottomDialog mMyBottomDialog;
    private RoundLoadingView mViewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyBottomDialog = new MyBottomDialog(this);
        View viewById1 = findViewById(R.id.outView);
        AutoEditText viewById = (AutoEditText) findViewById(R.id.et);
        viewById.setOutView(viewById1);

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
                mViewById.startTimer(5);
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

        mViewById = (RoundLoadingView) findViewById(R.id.loadingView);
        mViewById.setTextSize(15);
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
