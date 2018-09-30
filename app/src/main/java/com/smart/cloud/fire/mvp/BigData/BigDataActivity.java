package com.smart.cloud.fire.mvp.BigData;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.ui.view.RadarView;
import com.smart.cloud.fire.ui.view.RaderWheelView;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

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
    @Bind(R.id.sum_text1)
    TextView sum_text1;
    @Bind(R.id.sum_text2)
    TextView sum_text2;

    TranslateAnimation mShowAnim;



    private ObjectAnimator animator1 = new ObjectAnimator(), animator2 = new ObjectAnimator(), animator3 = new ObjectAnimator();

    private int core;
    private int during = 300;//检测时间间隔
    private Random mRandom = new Random();
    private BigDataPresenter presenter;
    private Context mContext;
    private String userID;
    private  int privilege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_data);

        //透明状态栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        ButterKnife.bind(this);
        mContext=this;
        privilege = MyApp.app.getPrivilege();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        mRadarView.start();
        presenter.getSmokeSummary(userID,privilege+"","","","","");
//        mRadarView.stop();
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
            check_layout_top.setAlpha(1);
            check_layout_top.setBackgroundColor(getResources().getColor(R.color.wifi_acclerate_orange));

        } else {
            check_layout_top.setAlpha(1);
            check_layout_top.setBackgroundColor(getResources().getColor(R.color.wifi_acclerate_red));

        }
        score_tv.setText(core+"");
        ScaleAnimation scaleAnimation=new ScaleAnimation(1,1.5f,1,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        score_tv.startAnimation(scaleAnimation);

    }


    @Override
    public void getOnlineSummary(SmokeSummary model) {
        core=100;
        mShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF
                ,-1.0f, Animation.RELATIVE_TO_SELF,0.0f);
        mShowAnim.setDuration(500);
        sum_text.startAnimation(mShowAnim );
        sum_text.setText("您一共"+model.getAllSmokeNumber()+"个设备，离线有"+model.getLossSmokeNumber()+"个设备，离线率"+model.getLossSmokeNumber()*100/model.getAllSmokeNumber()+"%");
        sum_text.setVisibility(View.VISIBLE);
        core=core-40*model.getLossSmokeNumber()/model.getAllSmokeNumber();
        showBackground(core);

        sum_text1.startAnimation(mShowAnim );
        sum_text1.setText("您一共"+model.getAllSmokeNumber()+"个设备，低电压设备有"+model.getLowVoltageNumber()+"个，低电率"+model.getLowVoltageNumber()*100/model.getAllSmokeNumber()+"%");
        sum_text1.setVisibility(View.VISIBLE);
        core=core-40*model.getLowVoltageNumber()/model.getAllSmokeNumber();
        showBackground(core);

//        mRadarView.stop();
    }
}
