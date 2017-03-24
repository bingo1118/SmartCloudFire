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
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.global.InitBaiduNavi;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.pushmessage.PushAlarmMsg;
import com.smart.cloud.fire.ui.ApMonitorActivity;
import com.smart.cloud.fire.utils.MusicManger;
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
 * Created by Administrator on 2016/9/27.
 */
public class AlarmActivity extends MvpActivity<AlarmPresenter> implements AlarmView {
    @Bind(R.id.alarm_fk_img)
    MyImageView alarmFkImg;
    @Bind(R.id.alarm_music_image)
    ImageView alarmMusicImage;
    @Bind(R.id.alarm_lead_to_btn)
    Button alarmLeadToBtn;
    @Bind(R.id.alarm_type)
    TextView mAlarmType;
    @Bind(R.id.alarm_time)
    TextView alarmTime;
    @Bind(R.id.alarm_info)
    TextView alarmInfo;
    @Bind(R.id.alarm_smoke_mark_principal)
    TextView alarmSmokeMarkPrincipal;
    @Bind(R.id.alarm_smoke_mark_phone_tv)
    TextView alarmSmokeMarkPhoneTv;
    @Bind(R.id.smoke_mark_principal)
    TextView smokeMarkPrincipal;
    @Bind(R.id.smoke_mark_phone_tv)
    TextView smokeMarkPhoneTv;
    @Bind(R.id.alarm_do_it_btn)
    Button alarmDoItBtn;
    private Context mContext;
    private PushAlarmMsg mPushAlarmMsg;
    private int TIME_OUT = 20;
    private String alarmMsg;
    private PushAlarmMsg.CameraBean cameraBean;

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
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        mContext = this;
        mPushAlarmMsg = (PushAlarmMsg) getIntent().getExtras().getSerializable("mPushAlarmMsg");
        alarmMsg = getIntent().getExtras().getString("alarmMsg");
        init();
        regFilter();
    }

    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("CLOSE_ALARM_ACTIVITY");
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("CLOSE_ALARM_ACTIVITY")) {
                finish();
            }
        }
    };

    private void init() {
        cameraBean = mPushAlarmMsg.getCamera();
        if(cameraBean!=null){
            alarmDoItBtn.setVisibility(View.VISIBLE);
        }else{
            alarmDoItBtn.setVisibility(View.GONE);
        }
        smokeMarkPrincipal.setText(mPushAlarmMsg.getPrincipal2());
        alarmSmokeMarkPrincipal.setText(mPushAlarmMsg.getPrincipal1());
        alarmSmokeMarkPhoneTv.setText(mPushAlarmMsg.getPrincipal1Phone());
        smokeMarkPhoneTv.setText(mPushAlarmMsg.getPrincipal2Phone());
        alarmInfo.setText(mPushAlarmMsg.getPlaceAddress() + mPushAlarmMsg.getAddress());
        alarmTime.setText(mPushAlarmMsg.getAlarmTime());
//        int devType = mPushAlarmMsg.getDeviceType();
        alarmFkImg.setBackgroundResource(R.drawable.allarm_bg_selector);
        mAlarmType.setTextColor(getResources().getColor(R.color.hj_color_text));
        mAlarmType.setText(alarmMsg);
//        switch (devType) {
//            case 1:
//
//                break;
//            case 2:
//                alarmFkImg.setBackgroundResource(R.drawable.allarm_bg_selector);
//                mAlarmType.setTextColor(getResources().getColor(R.color.hj_color_text));
//                mAlarmType.setText(alarmMsg);
//                break;
//            case 5:
//                alarmFkImg.setBackgroundResource(R.drawable.allarm_bg_selector);
//                mAlarmType.setTextColor(getResources().getColor(R.color.hj_color_text));
//                mAlarmType.setText(alarmMsg);
//                break;
//            default:
//                break;
//        }
        alarmInit();
        RxView.clicks(alarmLeadToBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Smoke mNormalSmoke = new Smoke();
                mNormalSmoke.setLongitude(mPushAlarmMsg.getLongitude() + "");
                mNormalSmoke.setLatitude(mPushAlarmMsg.getLatitude() + "");
                Reference<Activity> reference = new WeakReference(mContext);
                new InitBaiduNavi(reference.get(), mNormalSmoke);//导航
            }
        });
    }

    private boolean musicOpenOrClose = true;

    @OnClick({R.id.phone_lin_one, R.id.alarm_phone_lin_one, R.id.alarm_tc_image, R.id.alarm_music_image, R.id.alarm_do_it_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_lin_one:
                String phoneOne = alarmSmokeMarkPhoneTv.getText().toString().trim();
                mvpPresenter.telPhone(mContext, phoneOne);
                break;
            case R.id.alarm_phone_lin_one:
                String phoneOne2 = smokeMarkPhoneTv.getText().toString().trim();
                mvpPresenter.telPhone(mContext, phoneOne2);
                break;
            case R.id.alarm_tc_image:
                finish();
                break;
            case R.id.alarm_music_image:
                if (musicOpenOrClose) {
                    MusicManger.getInstance().stop();
                    alarmMusicImage.setImageResource(R.drawable.bj_yl_jy);
                    musicOpenOrClose = false;
                } else {
                    MusicManger.getInstance().playAlarmMusic(mContext);
                    alarmMusicImage.setImageResource(R.drawable.bj_yl);
                    musicOpenOrClose = true;
                }
                break;
            case R.id.alarm_do_it_btn:
                Contact contact = new Contact();
                contact.contactPassword = cameraBean.getCameraPwd();
                contact.contactId = cameraBean.getCameraId();
                Intent i = new Intent(mContext, ApMonitorActivity.class);
                i.putExtra("contact", contact);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }
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

    @Override
    protected AlarmPresenter createPresenter() {
        return new AlarmPresenter(this);
    }

    @Override
    public void finishRequest() {
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvpPresenter.finishActivity(TIME_OUT, mContext);
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
            mWakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
            mWakelock.acquire();
        }
    }
}
