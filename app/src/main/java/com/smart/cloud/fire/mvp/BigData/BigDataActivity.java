package com.smart.cloud.fire.mvp.BigData;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.ui.view.RadarView;
import com.smart.cloud.fire.ui.view.RaderWheelView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class BigDataActivity extends MvpActivity<BigDataPresenter> implements BigDataView{


    @Bind(R.id.check_radar_view)
    RadarView mRadarView;
    @Bind(R.id.check_radar_wheel_view)
    RaderWheelView mRaderWheelView;
    @Bind(R.id.wifi_acclerate_btn)
    Button acclerateBtn;
    @Bind(R.id.check_layout_top)
    RelativeLayout check_layout_top;
    @Bind(R.id.score_tv)
    TextView score_tv;
    @Bind(R.id.sum_text)
    TextView sum_text;



    private ObjectAnimator animator1 = new ObjectAnimator(), animator2 = new ObjectAnimator(), animator3 = new ObjectAnimator();

    int progress = 100;
    private int core;

    private int during = 300;//检测时间间隔


    private Random mRandom = new Random();

    private BigDataPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_data);

        ButterKnife.bind(this);
        mRadarView.start();
        showBackground(11);
        mRadarView.stop();
        mRaderWheelView.setVisibility(View.GONE);
    }

    @Override
    protected BigDataPresenter createPresenter() {
        presenter = new BigDataPresenter(this);
        return presenter;
    }



    public void showBackground(int core) {
        if (core > 90 && core <= 100) {
            check_layout_top.setAlpha(1);
            check_layout_top.setBackgroundColor(getResources().getColor(R.color.wifi_acclerate_green));
        } else if (core >= 75 && core <= 90) {
            float topAlpha = (float) ((core - 75) * 0.04);
            check_layout_top.setAlpha(1 - topAlpha);
            check_layout_top.setBackgroundColor(getResources().getColor(R.color.wifi_acclerate_orange));

        } else {
            float topAlpha = (float) ((core - 50) * 0.04);
            check_layout_top.setAlpha(1 - topAlpha);
            check_layout_top.setBackgroundColor(getResources().getColor(R.color.wifi_acclerate_red));

        }
        score_tv.setText(core+"");
        TranslateAnimation mShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF
                ,-1.0f, Animation.RELATIVE_TO_SELF,0.0f);
        mShowAnim.setDuration(500);
        sum_text.startAnimation(mShowAnim );
        sum_text.setVisibility(View.VISIBLE);
    }


}
