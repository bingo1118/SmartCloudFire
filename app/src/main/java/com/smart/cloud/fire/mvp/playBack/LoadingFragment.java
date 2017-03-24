package com.smart.cloud.fire.mvp.playBack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fire.cloud.smart.com.smartcloudfire.R;


/**
 * Created by Administrator on 2016/11/17.
 */
public class LoadingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_loading, container,
                false);
        return view;
    }
}
