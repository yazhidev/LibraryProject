package com.yazhi1992.libraryproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyBottomDialog(MainActivity.this).show();
            }
        });

        findViewById(R.id.mytv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("--", "--");
            }
        });
    }
}
