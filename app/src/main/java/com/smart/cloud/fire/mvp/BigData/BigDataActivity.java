package com.smart.cloud.fire.mvp.BigData;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.smart.cloud.fire.global.SafeScore;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.BigDataDetail.BigDataDetailActivity;
import com.smart.cloud.fire.ui.view.CircleProgressBar;
import com.smart.cloud.fire.ui.view.RadarView;
import com.smart.cloud.fire.ui.view.RaderWheelView;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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


    @Bind(R.id.circleProgressBar)
    CircleProgressBar circleProgressBar;
    @Bind(R.id.check_layout_top)
    RelativeLayout check_layout_top;
    @Bind(R.id.fault_rela)
    RelativeLayout fault_rela;
    @Bind(R.id.offline_rela)
    RelativeLayout offline_rela;
    @Bind(R.id.yangan_rela)
    RelativeLayout yangan_rela;
    @Bind(R.id.water_rela)
    RelativeLayout water_rela;
    @Bind(R.id.lowpower_rela)
    RelativeLayout lowpower_rela;

    @Bind(R.id.fault_text)
    TextView fault_text;
    @Bind(R.id.offline_text)
    TextView offline_text;
    @Bind(R.id.yangan_text)
    TextView yangan_text;
    @Bind(R.id.water_text)
    TextView water_text;
    @Bind(R.id.lowpower_text)
    TextView lowpower_text;
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
        presenter.getSafeScore(userID,privilege+"");
//        mRadarView.stop();
    }

    @Override
    protected BigDataPresenter createPresenter() {
        presenter = new BigDataPresenter(this);
        return presenter;
    }






    @Override
    public void getOnlineSummary(SmokeSummary model) {

    }

    SafeScore model;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(model==null){
                model=new SafeScore();
            }
            switch (msg.what){
//                case 0:
//                    fault_rela.startAnimation(mShowAnim );
//                    fault_text.setText("一共"+model.getTotalSum()+"个设备");
//                    fault_rela.setVisibility(View.VISIBLE);
//                    break;
                case 1:
                    offline_rela.startAnimation(mShowAnim );
                    offline_text.setText("离线设备:"+model.getOffline()+"分");
                    offline_rela.setVisibility(View.VISIBLE);
                    offline_rela.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(BigDataActivity.this, BigDataDetailActivity.class);
                            i.putExtra("type","2");
                            startActivity(i);
                        }
                    });
//                    core=core+(int)model.getOffline();
//                    circleProgressBar.setProgress(core,true);
                    break;
                case 2:
                    yangan_rela.startAnimation(mShowAnim );
                    yangan_text.setText("高频报警设备:"+model.getHistoriAlarm()+"分");
                    yangan_rela.setVisibility(View.VISIBLE);
//                    core=core+(int)model.getHistoriAlarm();
//                    circleProgressBar.setProgress(core,true);
                    break;
                case 3:
                    water_rela.startAnimation(mShowAnim );
                    water_text.setText("实时报警设备:"+model.getRealtimeAlarm()+"分");
                    water_rela.setVisibility(View.VISIBLE);
                    core=(int)model.getSafeScore();
                    circleProgressBar.setProgress(core,true);
                    break;
            }
        }
    };

    @Override
    public void getSafeScore(final SafeScore model) {
        this.model=model;
        core=0;
        circleProgressBar.setProgress(core,true);
        mShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF
                ,-1.0f, Animation.RELATIVE_TO_SELF,0.0f);
        mShowAnim.setDuration(500);


        // 初始化定时器
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int i=0;
            Message msg=new Message();
            @Override
            public void run() {
                if(i>2){
                    cancel();
                }
                Message msg=new Message();
                msg.what=i;
                handler.sendMessage(msg);
                i++;
            }
        }, 1000, 2000);


    }
}
