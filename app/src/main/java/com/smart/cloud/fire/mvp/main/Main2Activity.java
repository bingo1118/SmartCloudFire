package com.smart.cloud.fire.mvp.main;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.igexin.sdk.PushManager;
import com.p2p.core.P2PHandler;
import com.p2p.core.update.UpdateManager;
import com.smart.cloud.fire.activity.AddDev.ChioceDevTypeActivity;
import com.smart.cloud.fire.activity.AlarmHistory.AlarmHistoryActivity;
import com.smart.cloud.fire.activity.AlarmMsg.AlarmMsgActivity;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokeActivity;
import com.smart.cloud.fire.activity.Camera.CameraDevActivity;
import com.smart.cloud.fire.activity.Electric.ElectricDevActivity;
import com.smart.cloud.fire.activity.Host.HostActivity;
import com.smart.cloud.fire.activity.NFCDev.NFCDevActivity;
import com.smart.cloud.fire.activity.Setting.MyZoomActivity;
import com.smart.cloud.fire.activity.SecurityDev.SecurityDevActivity;
import com.smart.cloud.fire.activity.WiredDev.WiredDevActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MainService;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.SafeScore;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.Alarm.GetTaskActivity;
import com.smart.cloud.fire.mvp.BigData.BigDataActivity;
import com.smart.cloud.fire.mvp.login.SplashActivity;
import com.smart.cloud.fire.mvp.main.presenter.MainPresenter;
import com.smart.cloud.fire.mvp.main.view.MainView;
import com.smart.cloud.fire.service.RemoteService;
import com.smart.cloud.fire.ui.view.RadarView;
import com.smart.cloud.fire.ui.view.RaderWheelView;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;
import com.smart.cloud.fire.yoosee.P2PListener;
import com.smart.cloud.fire.yoosee.SettingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class  Main2Activity extends MvpActivity<MainPresenter> implements MainView {

    private Context mContext;
    private AlertDialog dialog_update;
    @Bind(R.id.check_radar_view)
    RadarView mRadarView;
    @Bind(R.id.check_radar_wheel_view)
    RaderWheelView mRaderWheelView;
    @Bind(R.id.home_alarm_light)
    ImageView home_alarm_light;
    @Bind(R.id.my_image)
    ImageView my_image;
    @Bind(R.id.sxcs_btn)
    RelativeLayout sxcs_btn;
    @Bind(R.id.tjsb_btn)
    RelativeLayout tjsb_btn;
    @Bind(R.id.dqfh_btn)
    RelativeLayout dqfh_btn;
    @Bind(R.id.spjk_btn)
    RelativeLayout spjk_btn;
    @Bind(R.id.zddw_btn)
    RelativeLayout zddw_btn;
    @Bind(R.id.xfwl_btn)
    RelativeLayout xfwl_btn;
    @Bind(R.id.nfc_btn)
    RelativeLayout nfc_btn;
    @Bind(R.id.home_alarm_lin)
    LinearLayout home_alarm_lin;
    @Bind(R.id.home_alarm_info_text)
    TextView home_alarm_info_text;
    @Bind(R.id.history_alarm)
    TextView history_alarm;
    @Bind(R.id.score_tv)
    TextView score_tv;
    @Bind(R.id.scan_result_text)
    TextView scan_result_text;
    @Bind(R.id.check_layout_top)
    RelativeLayout check_layout_top;
    Timer getlastestAlarm;
    AnimationDrawable anim ;

    MainPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_3);

        //透明状态栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        ButterKnife.bind(this);
        mContext = this;
        initView();
        regFilter();
        dealWithScan();
        getHistoryCore();
        anim = (AnimationDrawable) home_alarm_light.getBackground();
        startService(new Intent(Main2Activity.this, RemoteService.class));
        //启动个推接收推送信息。。
        PushManager.getInstance().initialize(this.getApplicationContext(), com.smart.cloud.fire.geTuiPush.DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), com.smart.cloud.fire.geTuiPush.DemoIntentService.class);
    }

    private void getHistoryCore() {
        String username = SharedPreferencesManager.getInstance().getData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTNAME);
                mRadarView.start();
                String url= ConstantValues.SERVER_IP_NEW+"getHistorSafeScore?userId="+username;
                VolleyHelper helper=VolleyHelper.getInstance(mContext);
                RequestQueue mQueue = helper.getRequestQueue();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.getInt("errorCode")==0){
                                        showBackground(response.getInt("safeScore"));
                                    }
                                    T.showShort(mContext,response.getString("error"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mRadarView.stop();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        T.showShort(mContext,"获取历史分数错误");
                        mRadarView.stop();
                    }
                });
                mQueue.add(jsonObjectRequest);
    }

    private void dealWithScan() {
        mRadarView.start();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int privilege = MyApp.app.getPrivilege();
                String userID = SharedPreferencesManager.getInstance().getData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTNAME);
                presenter.getSmokeSummary(userID,privilege+"","","","","");
//                presenter.getSafeScore(userID,privilege+"");
               }
        };
        timer.schedule(timerTask,5000);
    }

    @Override
    public void getOnlineSummary(SmokeSummary model) {
        scan_result_text.setText("您一共"+model.getAllSmokeNumber()+"个设备，离线设备有"+model.getLossSmokeNumber()
                +"个，离线率："+model.getLossSmokeNumber()*100/model.getAllSmokeNumber()
                +"%；低电压设备"+model.getLowVoltageNumber() +"个");
    }

    @Override
    public void getSafeScore(SafeScore model) {
        if(model!=null){
            showBackground((int) model.getSafeScore());
        }else{
            T.showShort(mContext,"获取评分失败");
        }
        mRadarView.stop();
    }

    public void showBackground(int core) {
        if (core > 90 && core <= 100) {
            check_layout_top.setAlpha(1);
            check_layout_top.setBackground(getResources().getDrawable(R.drawable.scan_back_hao));
        } else if (core >= 75 && core <= 90) {
            check_layout_top.setAlpha(1);
            check_layout_top.setBackground(getResources().getDrawable(R.drawable.scan_back_zhong));

        } else {
            check_layout_top.setAlpha(1);
            check_layout_top.setBackground(getResources().getDrawable(R.drawable.scan_back_cha));

        }

        score_tv.setText(core+"");
        ScaleAnimation scaleAnimation=new ScaleAnimation(1,1.5f,1,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        score_tv.startAnimation(scaleAnimation);

    }

    @OnClick({R.id.scan_btn,R.id.my_image,R.id.sxcs_btn,R.id.tjsb_btn,R.id.dqfh_btn,R.id.spjk_btn,R.id.zddw_btn,
            R.id.xfwl_btn,R.id.nfc_btn,R.id.alarm_msg,R.id.history_alarm})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.scan_btn:
//                dealWithScan();
                intent=new Intent(mContext, BigDataActivity.class);
                startActivity(intent);
                break;
            case R.id.alarm_msg:
                intent=new Intent(mContext, AlarmMsgActivity.class);
                startActivity(intent);
                break;
            case R.id.my_image:
                intent = new Intent(mContext, MyZoomActivity.class);
                startActivity(intent);
                break;
            case R.id.sxcs_btn2:
            case R.id.sxcs_btn:
                intent = new Intent(mContext, AllSmokeActivity.class);
                startActivity(intent);
                break;
            case R.id.tjsb_btn:
                intent = new Intent(mContext, ChioceDevTypeActivity.class);
                startActivity(intent);
                break;
            case R.id.history_alarm:
                intent = new Intent(mContext, AlarmHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.dqfh_btn:
                intent = new Intent(mContext, ElectricDevActivity.class);
                startActivity(intent);
                break;
            case R.id.spjk_btn2:
            case R.id.spjk_btn:
                intent = new Intent(mContext, CameraDevActivity.class);
                startActivity(intent);
                break;
            case R.id.zddw_btn:
                intent = new Intent(mContext, WiredDevActivity.class);
                startActivity(intent);
                break;
            case R.id.xfwl_btn:
                intent = new Intent(mContext, SecurityDevActivity.class);
                startActivity(intent);
                break;
            case R.id.nfc_btn:
                intent = new Intent(mContext, NFCDevActivity.class);
                startActivity(intent);
                break;
            case R.id.zjgl_btn:
                intent= new Intent(mContext, HostActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void initView() {
        if(MyApp.app.getPrivilege()==1){//@@9.29 1级权限显示
//            zddw_btn.setVisibility(View.GONE);
//            dqfh_btn.setVisibility(View.GONE);
//            xfwl_btn.setVisibility(View.GONE);
////            spjk_btn.setVisibility(View.GONE);
//            tjsb_btn.setVisibility(View.GONE);
//            nfc_btn.setVisibility(View.GONE);
//            zjgl_btn.setVisibility(View.GONE);
//            main_line.setVisibility(View.GONE);
//            main_line1.setVisibility(View.VISIBLE);
        }else{
//            main_line1.setVisibility(View.GONE);
//            main_line.setVisibility(View.VISIBLE);
        }
        P2PHandler.getInstance().p2pInit(this,
                new P2PListener(),
                new SettingListener());
        connect();
        getlastestAlarm=new Timer();
        getlastestAlarm.schedule(new TimerTask() {
            @Override
            public void run() {
                String username = SharedPreferencesManager.getInstance().getData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTNAME);
                int privilege = MyApp.app.getPrivilege();
                String url= ConstantValues.SERVER_IP_NEW+"getLastestAlarm?userId="+username+"&privilege="+privilege;
                VolleyHelper helper=VolleyHelper.getInstance(mContext);
                RequestQueue mQueue = helper.getRequestQueue();
//                RequestQueue mQueue = Volley.newRequestQueue(MyApp.app);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int errorCode=response.getInt("errorCode");
                                    if(errorCode==0){
                                        JSONObject lasteatalarm=response.getJSONObject("lasteatAlarm");
                                        if(lasteatalarm.getString("ifDealAlarm")=="0"){
                                            anim.start();
                                            home_alarm_info_text.setText(lasteatalarm.getString("address")
                                                    +"\n"+lasteatalarm.getString("name")+"发生报警");
//                                            home_alarm_lin.setBackgroundResource(R.drawable.corners_shape_top);
                                        }else{
                                            anim.stop();
                                            home_alarm_info_text.setText(lasteatalarm.getString("address")
                                                    +"\n"+lasteatalarm.getString("name")+"发生报警【已处理】");
//                                            home_alarm_lin.setBackgroundResource(R.drawable.corners_shape_top_normal);
                                        }
                                    }else{
                                        anim.stop();
                                        home_alarm_info_text.setText("无最新报警信息");
//                                        home_alarm_lin.setBackgroundResource(R.drawable.corners_shape_top_normal);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        anim.stop();
                        home_alarm_info_text.setText("未获取到数据");
//                        home_alarm_lin.setBackgroundResource(R.drawable.corners_shape_top_normal);
                    }
                });
                mQueue.add(jsonObjectRequest);
            }
        },0,10000);
    }

    private void connect() {
        Intent service = new Intent(mContext, MainService.class);//检查更新版本服务。。
        startService(service);
    }

    /**
     * 添加广播接收器件。。
     */
    private void regFilter() {
        IntentFilter filter = new IntentFilter();
//        filter.addAction("Constants.Action.ACTION_UPDATE");
//        filter.addAction("Constants.Action.ACTION_UPDATE_NO");
        filter.addAction("APP_EXIT");
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            //退出。。
            if (intent.getAction().equals("APP_EXIT")) {
                SharedPreferencesManager.getInstance().putData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTPASS,
                        "");
                SharedPreferencesManager.getInstance().removeData(mContext,
                        "LASTAREANAME");//@@11.13
                SharedPreferencesManager.getInstance().removeData(mContext,
                        "LASTAREAID");//@@11.13
                SharedPreferencesManager.getInstance().removeData(mContext,
                        "LASTAREAISPARENT");//@@11.13
                PushManager.getInstance().stopService(getApplicationContext());
                unbindAlias();
                Intent in = new Intent(mContext, SplashActivity.class);
                startActivity(in);
                finish();
            }
        }
    };

    /**
     * 个推解绑@@5.16
     */
    private void unbindAlias() {
        String userCID = SharedPreferencesManager.getInstance().getData(this,SharedPreferencesManager.SP_FILE_GWELL,"CID");//@@
        String username = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        String url= ConstantValues.SERVER_IP_NEW+"loginOut?userId="+username+"&alias="+username+"&cid="+userCID+"&appId=1";//@@5.27添加app编号
//        RequestQueue mQueue = Volley.newRequestQueue(this);
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        try {
//                            Toast.makeText(mContext,response.getString("error"),Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    @Override
    protected MainPresenter createPresenter() {
        presenter=new MainPresenter(this);
        return presenter;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mvpPresenter.exitBy2Click(mContext);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void exitBy2Click(boolean isExit) {
        if (isExit) {
            //moveTaskToBack(false);
            moveTaskToBack(true);//@@5.31
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
        getlastestAlarm.cancel();
        VolleyHelper.getInstance(mContext).stopRequestQueue();
    }

    private void alarmInit() {
        //imageview动画设置。。
        final AnimationDrawable anim = (AnimationDrawable) home_alarm_light.getBackground();
        ViewTreeObserver.OnPreDrawListener opdl = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                anim.start();
                return true;
            }
        };
        home_alarm_light.getViewTreeObserver().addOnPreDrawListener(opdl);
    }

}
