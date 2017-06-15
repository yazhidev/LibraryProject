package com.yazhi1992.libraryproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
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

//        mViewById1 = (SuperAutoLoopView) findViewById(R.id.superL);
        List<View> views = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            //设置滚动的单个布局
            View moreView = LayoutInflater.from(this).inflate(R.layout.layout_loop_view, null);
            //初始化布局的控件
            TextView tv1 = (TextView) moreView.findViewById(R.id.tv);
            //进行对控件赋值
            tv1.setText(mList.get(i));

            //添加到循环滚动数组里面去
            views.add(moreView);
        }

//        mViewById1.setViews(views);


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
