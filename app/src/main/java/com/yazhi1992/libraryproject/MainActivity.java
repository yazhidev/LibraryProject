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
                mViewById.setLoading(false);
            }
        });

        mViewById = (LoadingTextView) findViewById(R.id.loadingView);
        mViewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
