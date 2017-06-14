package com.yazhi1992.libraryproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yazhi1992.yazhilib.widget.LoopView.AutoLoopView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private AutoLoopView mViewById;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mViewById = (AutoLoopView) findViewById(R.id.autoLoop);
        mViewById.setBackgroundPressedColor(Color.GRAY);

        mList = new ArrayList<>();
        mList.add("这一条是测试数据1");
        mList.add("这一条是测试数据2");
        mList.add("这一条是测试数据3");
        mViewById.setItems(mList);

        mViewById.setOnAutoLoopViewClickListener(new AutoLoopView.OnAutoLoopViewClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(Main2Activity.this, mList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewById.start();
            }
        });
    }
}
