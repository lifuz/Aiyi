package com.lifuz.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prd.aiyi.R;


/**
 * Created by 半夏微凉 on 2015/4/26.
 */
public class DanCiBenFragment extends Fragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View messageLayout = inflater.inflate(R.layout.danciben_layout,
                container, false);

        return messageLayout;
    }
}
