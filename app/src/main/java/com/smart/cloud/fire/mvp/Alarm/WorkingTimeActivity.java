package com.smart.cloud.fire.mvp.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.utils.MusicManger;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class WorkingTimeActivity extends MvpActivity<AlarmPresenter> implements AlarmView {


    @Bind(R.id.stop_alarm)
    TextView stop_alarm;
    @Bind(R.id.worktime_text)
    TextView worktime_text;

    private Context mContext;
    private int TIME_OUT = 20;
    private String userID;

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
        setContentView(R.layout.activity_working_time);
        ButterKnife.bind(this);
        mContext = this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);

        String worktime=getIntent().getStringExtra("time");
        if(null!=worktime){
            worktime_text.setText("上班时间："+worktime);
        }
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


    }

    private boolean musicOpenOrClose = true;

    @OnClick({R.id.stop_alarm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stop_alarm:
                VolleyHelper helper= VolleyHelper.getInstance(mContext);
                RequestQueue mQueue = helper.getRequestQueue();
//                RequestQueue mQueue = Volley.newRequestQueue(mContext);
                String url= ConstantValues.SERVER_IP_NEW+"dealAlarm?userId="+userID+"&smokeMac="+userID;
                StringRequest stringRequest = new StringRequest(url,
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
                mQueue.add(stringRequest);
                break;
            default:
                break;
        }
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
        super.onNewIntent(intent);
    }
}

