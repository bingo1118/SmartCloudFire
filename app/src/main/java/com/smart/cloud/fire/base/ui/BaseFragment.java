package com.smart.cloud.fire.base.ui;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smart.cloud.fire.utils.LogUtils;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/7/27.
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtils.i(getFragmentName(), " onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(getFragmentName(), " onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.i(getFragmentName(), " onCreateView()");
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.i(getFragmentName(), " onViewCreated()");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.i(getFragmentName(), " onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.i(getFragmentName(), " onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(getFragmentName(), " onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i(getFragmentName(), " onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.i(getFragmentName(), " onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.i(getFragmentName(), " onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(getFragmentName(), " onDestroy()");
        onUnsubscribe();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.i(getFragmentName(), " onDetach()");
    }

    /**
     * fragment name
     */
    public abstract String getFragmentName();

    private CompositeSubscription mCompositeSubscription;

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription) {
//        if (mCompositeSubscription == null) {
        mCompositeSubscription = new CompositeSubscription();
//        }
        mCompositeSubscription.add(subscription);
    }
}
