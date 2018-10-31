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
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.activity.UploadAlarmInfo.UploadAlarmInfoActivity;
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

public class GetTaskActivity extends MvpActivity<AlarmPresenter> implements AlarmView {
    @Bind(R.id.alarm_fk_img)
    ImageView alarmFkImg;
    @Bind(R.id.alarm_music_image)
    ImageView alarmMusicImage;
    @Bind(R.id.makesure_getalarm)
    RelativeLayout makesure_getalarm;
    @Bind(R.id.daohan_btn)
    RelativeLayout alarmLeadToBtn;
    @Bind(R.id.msg_text)
    TextView msg_text;

    private Context mContext;
    private PushAlarmMsg mPushAlarmMsg;
    private int TIME_OUT = 180;

    private MapView mMapView;
    private BaiduMap mBaiduMap;


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
        setContentView(R.layout.activity_get_task);
        ButterKnife.bind(this);
        mContext = this;
        mPushAlarmMsg = (PushAlarmMsg) getIntent().getExtras().getSerializable("mPushAlarmMsg");

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
        msg_text.setText(Html.fromHtml("<font color=\'#000\'>"+mPushAlarmMsg.getAddress()+"-"+mPushAlarmMsg.getName()
                +"发生"+"</font><font color=\'#bc1c07\'>"+"【"+mPushAlarmMsg.getAlarmTypeName()+"】"+"</font>"
                +"<font color=\'#000\'>，请尽快处理！</font>"));
        alarmInit();//imageview动画设置。。
        RxView.clicks(alarmLeadToBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Smoke mNormalSmoke = new Smoke();
                mNormalSmoke.setLongitude(mPushAlarmMsg.getLongitude());
                mNormalSmoke.setLatitude(mPushAlarmMsg.getLatitude());
                Reference<Activity> reference = new WeakReference(mContext);
                new InitBaiduNavi(reference.get(), mNormalSmoke);//导航
            }
        });
        initMap();
    }

    private void initMap() {
        mMapView = (MapView) findViewById(R.id.mmap);
        mBaiduMap = mMapView.getMap();
        //定义Maker坐标点
        LatLng point = new LatLng(Double.parseDouble(mPushAlarmMsg.getLatitude()),Double.parseDouble(mPushAlarmMsg.getLongitude()));
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.pin);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(point)
                //放大地图到20倍
                .zoom(16)
                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    private boolean musicOpenOrClose = true;

    @OnClick({ R.id.alarm_music_image,R.id.makesure_getalarm})
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.makesure_getalarm:
//                Intent intent_dealtask=new Intent(mContext, UploadAlarmInfoActivity.class);
//                intent_dealtask.putExtra("mPushAlarmMsg",mPushAlarmMsg);
//                startActivity(intent_dealtask);
//                T.showShort(mContext,"接单成功");
//                finish();
                String username = SharedPreferencesManager.getInstance().getData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTNAME);
                String url= ConstantValues.SERVER_IP_NEW+"receiveOrder?userId="+username+"&smokeMac="+mPushAlarmMsg.getMac();
                VolleyHelper helper=VolleyHelper.getInstance(mContext);
                RequestQueue mQueue = helper.getRequestQueue();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int errorCode=response.getInt("errorCode");
                                    if(errorCode==0){
                                        Intent intent_dealtask=new Intent(mContext, UploadAlarmInfoActivity.class);
                                        intent_dealtask.putExtra("mPushAlarmMsg",mPushAlarmMsg);
                                        startActivity(intent_dealtask);
                                        T.showShort(mContext,"接单成功");
                                        finish();
                                    }else{
                                        T.showShort(mContext,response.getString("error"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                mQueue.add(jsonObjectRequest);
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
        mBaiduMap.clear();
        mMapView.onDestroy();
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
        mPushAlarmMsg = (PushAlarmMsg) intent.getExtras().getSerializable("mPushAlarmMsg");
        init();
        regFilter();
        super.onNewIntent(intent);
    }
}
