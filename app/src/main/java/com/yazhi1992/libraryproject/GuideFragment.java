package com.yazhi1992.libraryproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zyz on 16/11/21.
 */
public class GuideFragment extends Fragment {

    private static final String GET_POSITION = "get_position";
    private View mView;
    private int[] mSources;
    private TextView mTv;

    public GuideFragment() {

    }

    public static GuideFragment getInstance(int position) {
        GuideFragment guideFragment = new GuideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(GET_POSITION, position);
        guideFragment.setArguments(bundle);
        return guideFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_guide, null);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTv = (TextView) mView.findViewById(R.id.tv);
        int position = getArguments().getInt(GET_POSITION);
        mTv.setText(position + "");
    }
}
