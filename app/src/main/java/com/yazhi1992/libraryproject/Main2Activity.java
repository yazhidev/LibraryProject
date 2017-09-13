package com.yazhi1992.libraryproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.yazhi1992.yazhilib.widget.LoopView.AutoLoopView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private List<String> mList;
    private MyBottomDialog mMyBottomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


//        mViewById1 = (SuperAutoLoopView) findViewById(R.id.superL);

//        mViewById1.setViews(views);

        AnimationSet animationSet = new AnimationSet(false);
        /*默认的动画*/
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1, 0.5f, 1
                , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationSet.addAnimation(scaleAnimation);
        mMyBottomDialog = new MyBottomDialog(this);
//        mMyBottomDialog.setInnerShowAnim(animationSet);

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBottomDialog.show();
            }
        });
    }
}
