package com.smart.cloud.fire.mvp.Alarm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.global.InitBaiduNavi;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.pushmessage.PushAlarmMsg;
import com.smart.cloud.fire.pushmessage.PushWiredSmokeAlarmMsg;
import com.smart.cloud.fire.ui.ApMonitorActivity;
import com.smart.cloud.fire.utils.MusicManger;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;
import com.smart.cloud.fire.view.MyImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
    @Bind(R.id.alarm_mac)
    TextView alarmMac;
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
    @Bind(R.id.make_sure_upload_btn)
    Button make_sure_upload_btn;//@@报警上传
    @Bind(R.id.deal_voice_btn)
    Button deal_voice_btn;//@@报警上传
    @Bind(R.id.lin_principal)
    LinearLayout lin_principa1;
    @Bind(R.id.lin_principa2)
    LinearLayout lin_principa2;
    @Bind(R.id.stop_alarm)
    TextView stop_alarm;
    @Bind(R.id.alarm_tb_image)
    ImageView alarm_tb_image;
    @Bind(R.id.makesure_getalarm)
    Button makesure_getalarm;

    private Context mContext;
    private PushAlarmMsg mPushAlarmMsg;
    private int TIME_OUT = 20;
    private String alarmMsg;
    private PushAlarmMsg.CameraBean cameraBean;
    private int ifWiredSmoke=0;
    private PushWiredSmokeAlarmMsg pushWiredSmokeAlarmMsg;//@@6.30

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title
        //在锁屏状态下弹出。。
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        mContext = this;
        ifWiredSmoke=getIntent().getIntExtra("isWiredAlarmMsg",0);//@@6.30
        if(ifWiredSmoke==1){
            pushWiredSmokeAlarmMsg= (PushWiredSmokeAlarmMsg) getIntent().getExtras().getSerializable("mPushAlarmMsg");
        }else{
            mPushAlarmMsg = (PushAlarmMsg) getIntent().getExtras().getSerializable("mPushAlarmMsg");
        }
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

    /**
     * 根据推送过来的PushAlarmMsg对象填充数据。。
     */
    private void init() {
        String uploadpeople=null;
        if(mPushAlarmMsg!=null){
            uploadpeople=mPushAlarmMsg.getUploadpeople();
        }

        if(uploadpeople!=null&&uploadpeople.length()>0){
            alarm_tb_image.setImageResource(R.drawable.upload_alarm);
            makesure_getalarm.setVisibility(View.VISIBLE);
            String finalUploadpeople = uploadpeople;
            makesure_getalarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userid= MyApp.getUserID();
                    String url= ConstantValues.SERVER_IP_NEW+"makeSureGetUpload?userId="+userid+"&uploadpeolpe="+ finalUploadpeople;
                    VolleyHelper.getInstance(mContext).getStringResponse(url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject=new JSONObject(response);
                                        int errorCode=jsonObject.getInt("errorCode");
                                                if(errorCode==0){
                                                    T.showShort(mContext,"已回复");
                                                }else{
                                                    T.showShort(mContext,"失败");
                                                }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        T.showShort(mContext,"设置失败");
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                            T.showShort(mContext,"设置失败");
                        }
                    });
                }
            });
        }else{
            alarm_tb_image.setImageResource(R.drawable.baojing_tb);
            makesure_getalarm.setVisibility(View.GONE);
        }
        if(mPushAlarmMsg!=null){
            if(mPushAlarmMsg.getDeviceType()==18||mPushAlarmMsg.getDeviceType()==19||
                    mPushAlarmMsg.getDeviceType()==1||mPushAlarmMsg.getDeviceType()==5){
                stop_alarm.setVisibility(View.VISIBLE);
            }else{
                stop_alarm.setVisibility(View.GONE);
            }//@@11.01
        }
        if(ifWiredSmoke==1){
            alarmLeadToBtn.setVisibility(View.GONE);
            lin_principa1.setVisibility(View.GONE);
            lin_principa2.setVisibility(View.GONE);
            alarmDoItBtn.setVisibility(View.GONE);
            smokeMarkPrincipal.setVisibility(View.GONE);
            alarmSmokeMarkPrincipal.setVisibility(View.GONE);
            alarmSmokeMarkPhoneTv.setVisibility(View.GONE);
            smokeMarkPhoneTv.setVisibility(View.GONE);
            String alarmMsg=pushWiredSmokeAlarmMsg.getFaultInfo();
            if(alarmMsg.length()==0||alarmMsg==null||alarmMsg.equals("null")){
                alarmMsg="";
            }//@@7.10
            alarmInfo.setText("终端："+pushWiredSmokeAlarmMsg.getRepeater()+" \r编号："+pushWiredSmokeAlarmMsg.getFaultCode()+"\n详情："+ alarmMsg);
            alarmTime.setText(pushWiredSmokeAlarmMsg.getFaultTime());
            alarmMac.setText(pushWiredSmokeAlarmMsg.getFaultDevDesc());//@@2018.01.03
//        int devType = mPushAlarmMsg.getDeviceType();
            alarmFkImg.setBackgroundResource(R.drawable.allarm_bg_selector);
            mAlarmType.setTextColor(getResources().getColor(R.color.hj_color_text));
            if(pushWiredSmokeAlarmMsg.getFaultCode().equals("0")){
                mAlarmType.setText("主机发生:"+pushWiredSmokeAlarmMsg.getFaultDevDesc());
                alarmMac.setText("ID:"+pushWiredSmokeAlarmMsg.getRepeater());//@@2018.01.03
            }else{
                mAlarmType.setText(this.alarmMsg);
            }


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
            alarmInit();//imageview动画设置。。
//            RxView.clicks(alarmLeadToBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
//                @Override
//                public void call(Void aVoid) {
//                    Smoke mNormalSmoke = new Smoke();
//                    mNormalSmoke.setLongitude(mPushAlarmMsg.getLongitude() + "");
//                    mNormalSmoke.setLatitude(mPushAlarmMsg.getLatitude() + "");
//                    Reference<Activity> reference = new WeakReference(mContext);
//                    new InitBaiduNavi(reference.get(), mNormalSmoke);//导航
//                }
//            });
        }else{
            if(mPushAlarmMsg.getDeviceType()==58||mPushAlarmMsg.getDeviceType()==61){
                deal_voice_btn.setVisibility(View.VISIBLE);
                deal_voice_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog dialog1 = new ProgressDialog(mContext);
                        dialog1.setTitle("提示");
                        dialog1.setMessage("设置中，请稍候");
                        dialog1.setCanceledOnTouchOutside(false);
                        dialog1.show();
                        String userid= MyApp.getUserID();

                        String url="";
                        switch (mPushAlarmMsg.getDeviceType()){
                            case 41:
                                url= ConstantValues.SERVER_IP_NEW+"ackNB_IOT_Control?userId="+userid+"&smokeMac="+mPushAlarmMsg.getMac()+"&eleState=1";
                                break;
                            case 58:
                                url= ConstantValues.SERVER_IP_NEW+"nanjing_jiade_cancel?&imeiValue="+mPushAlarmMsg.getMac()+"&deviceType=58";
                                break;
                            case 61:
                                url= ConstantValues.SERVER_IP_NEW+"nanjing_jiade_cancel?&imeiValue="+mPushAlarmMsg.getMac()+"&deviceType=61";
                                break;

                        }
                        VolleyHelper.getInstance(mContext).getStringResponse(url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject=new JSONObject(response);
                                            T.showShort(mContext,jsonObject.getString("error"));
                                            dialog1.dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            T.showShort(mContext,"设置失败");
                                            dialog1.dismiss();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TAG", error.getMessage(), error);
                                T.showShort(mContext,"设置失败");
                                dialog1.dismiss();
                            }
                        });
                    }
                });
            }else{
                deal_voice_btn.setVisibility(View.GONE);
            }
            cameraBean = mPushAlarmMsg.getCamera();
            if(cameraBean!=null&&cameraBean.getCameraId()!=null&&cameraBean.getCameraPwd()!=null){
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
            alarmMac.setText("ID:"+mPushAlarmMsg.getMac());//@@2018.01.03
//        int devType = mPushAlarmMsg.getDeviceType();
            alarmFkImg.setBackgroundResource(R.drawable.allarm_bg_selector);
            mAlarmType.setTextColor(getResources().getColor(R.color.hj_color_text));
//            mAlarmType.setText(mPushAlarmMsg.getName()+alarmMsg);
            if(mPushAlarmMsg.getDeviceType()==18){
                if(mPushAlarmMsg.getAlarmType()==202){
                    mAlarmType.setText(mPushAlarmMsg.getName()+alarmMsg+"(喷淋阀已打开)");
                }else{
                    mAlarmType.setText(mPushAlarmMsg.getName()+alarmMsg);
                }
            }else if(mPushAlarmMsg.getDeviceType()==119){
                mAlarmType.setText("附近设备联动:"+mPushAlarmMsg.getName()+alarmMsg);
            }else{
                mAlarmType.setText(mPushAlarmMsg.getName()+alarmMsg);
            }//@@11.01
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
            alarmInit();//imageview动画设置。。
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

    }

    private boolean musicOpenOrClose = true;

    @OnClick({R.id.phone_lin_one, R.id.alarm_phone_lin_one, R.id.alarm_tc_image
            , R.id.alarm_music_image, R.id.alarm_do_it_btn,R.id.stop_alarm,R.id.make_sure_upload_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_lin_one:
                String phoneOne2 = smokeMarkPhoneTv.getText().toString().trim();
                mvpPresenter.telPhone(mContext, phoneOne2);
                break;
            case R.id.alarm_phone_lin_one:
                String phoneOne1 = alarmSmokeMarkPhoneTv.getText().toString().trim();
                mvpPresenter.telPhone(mContext, phoneOne1);
                break;
            case R.id.alarm_tc_image:
                finish();
                break;
            case R.id.alarm_music_image:
                if (musicOpenOrClose) {
                    SharedPreferencesManager.getInstance().putData(mContext,
                            "AlarmVoice",
                            "closealarmvoice",
                            System.currentTimeMillis());//@@
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
                final Contact contact = new Contact();
                contact.contactPassword = cameraBean.getCameraPwd();
                contact.contactId = cameraBean.getCameraId();
                Intent i = new Intent(mContext, ApMonitorActivity.class);
                i.putExtra("contact", contact);
                startActivity(i);
                finish();
                break;
            case R.id.stop_alarm:
                String url= ConstantValues.SERVER_IP_NEW+"StopAlarm?mac="+mPushAlarmMsg.getMac();
                VolleyHelper.getInstance(mContext).getStringResponse(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    if(jsonObject.get("errorCode").equals(0)){
                                        T.showShort(mContext,"已确认");
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error","error");
                    }
                });
                break;
            case R.id.make_sure_upload_btn:
                String username = MyApp.getUserID();
                String url1= ConstantValues.SERVER_IP_NEW+"makeSureAlarm?userId="+username
                        +"&smokeMac="+mPushAlarmMsg.getMac()+"&alarmType="+mPushAlarmMsg.getAlarmType();
                VolleyHelper.getInstance(mContext).getJsonResponse(url1,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int errorCode=response.getInt("errorCode");
                                    if(errorCode==0){
                                        T.showShort(MyApp.app,"上报成功");
                                    }else{
                                        T.showShort(MyApp.app,"上报失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        T.showShort(MyApp.app,"上报失败");
                    }
                });
                break;
            default:
                break;
        }
    }

    private void alarmInit() {
        //imageview动画设置。。
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

    @Override
    protected void onNewIntent(Intent intent) {
        ButterKnife.bind(this);
        mContext = this;
        if(ifWiredSmoke==1){//@@6.30
            pushWiredSmokeAlarmMsg= (PushWiredSmokeAlarmMsg)intent.getExtras().getSerializable("mPushAlarmMsg");
        }else{
            mPushAlarmMsg = (PushAlarmMsg) intent.getExtras().getSerializable("mPushAlarmMsg");
        }
        alarmMsg = intent.getExtras().getString("alarmMsg");
        init();
        regFilter();
        super.onNewIntent(intent);
    }
}
