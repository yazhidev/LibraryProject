package com.yazhi1992.libraryproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yazhi1992.yazhilib.widget.RoundView.LoadingTextView;


public class MainActivity extends AppCompatActivity {

    private MyBottomDialog mMyBottomDialog;
    private LoadingTextView mViewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyBottomDialog = new MyBottomDialog(this);

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

        mViewById = (LoadingTextView) findViewById(R.id.loadingView);
        mViewById.setTextSize(15);
        mViewById.setTextWhenCountDown("还需要等待", "秒后重获");
        mViewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewById.startTimer(5);

//                mViewById.setLoading(true);
//                mViewById.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mViewById.startTimer(5);
//                    }
//                }, 2000);
            }
        });
    }
}
