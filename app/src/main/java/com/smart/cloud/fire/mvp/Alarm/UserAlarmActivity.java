package com.smart.cloud.fire.mvp.Alarm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.InitBaiduNavi;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.pushmessage.GetUserAlarm;
import com.smart.cloud.fire.utils.MusicManger;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.view.MyImageView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/10/25.
 */
public class UserAlarmActivity extends MvpActivity<AlarmPresenter> implements AlarmView {

    @Bind(R.id.alarm_fk_img)
    MyImageView alarmFkImg;
    @Bind(R.id.alarm_music_image)
    ImageView alarmMusicImage;
    @Bind(R.id.alarm_fk_center)
    RelativeLayout alarmFkCenter;
    @Bind(R.id.alarm_tc_image)
    ImageView alarmTcImage;
    @Bind(R.id.alarm_do_it_btn)
    Button alarmDoItBtn;
    @Bind(R.id.alarm_lead_to_btn)
    Button alarmLeadToBtn;
    @Bind(R.id.alarm_type)
    TextView alarmType;
    @Bind(R.id.alarm_time)
    TextView alarmTime;
    @Bind(R.id.cancel_alarm_tv)
    TextView cancelAlarmTv;
    @Bind(R.id.cancel_alarm)
    RelativeLayout cancelAlarm;
    private Context mContext;
    private GetUserAlarm getUserAlarm;
    private int TIME_OUT = 50;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_user_alarm);
        ButterKnife.bind(this);
        mContext = this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        getUserAlarm = (GetUserAlarm) getIntent().getExtras().getSerializable("mPushAlarmMsg");
        init();
        regFilter();
    }

    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("CLOSE_ALARM_ACTIVITY");
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("CLOSE_ALARM_ACTIVITY")){
                finish();
            }
        }
    };

    private void init() {
        alarmType.setText(getUserAlarm.getInfo());
        alarmTime.setText(getUserAlarm.getAlarmTime());
        alarmFkImg.setBackgroundResource(R.drawable.allarm_bg_selector);
        alarmInit();
        RxView.clicks(alarmLeadToBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Smoke mNormalSmoke = new Smoke();
                mNormalSmoke.setLongitude(getUserAlarm.getLongitude()+"");
                mNormalSmoke.setLatitude(getUserAlarm.getLatitude()+"");
                Reference<Activity> reference = new WeakReference(mContext);
                new InitBaiduNavi(reference.get(),mNormalSmoke);//导航
            }
        });
    }

    private void alarmInit() {
        final AnimationDrawable anim = (AnimationDrawable) alarmFkImg
                .getBackground();
        ViewTreeObserver.OnPreDrawListener opdl = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                anim.start();
                return true;
            }
        };
        alarmFkImg.getViewTreeObserver().addOnPreDrawListener(opdl);
    }

    @OnClick({R.id.alarm_tc_image,R.id.cancel_alarm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.alarm_tc_image:
                finish();
                break;
            case R.id.cancel_alarm:
                mvpPresenter.disposeAlarm(userID,getUserAlarm.getAlarmSerialNumber());
                break;
            default:
                break;
        }

    }

    @Override
    public void finishRequest() {
        cancelAlarm.setVisibility(View.GONE);
    }

    @Override
    public void finishActivity() {

    }

    @Override
    protected AlarmPresenter createPresenter() {
        return new AlarmPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvpPresenter.finishActivity(TIME_OUT,mContext);
        acquireWakeLock();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvpPresenter.stopMusic();
        MusicManger.getInstance().stop();
        releaseWakeLock();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void releaseWakeLock() {
        if (mWakelock != null && mWakelock.isHeld()) {
            mWakelock.release();
            mWakelock = null;
        }
    }
    private PowerManager.WakeLock mWakelock;
    private void acquireWakeLock() {
        if (mWakelock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,this.getClass().getCanonicalName());
            mWakelock.acquire();
        }
    }
}
