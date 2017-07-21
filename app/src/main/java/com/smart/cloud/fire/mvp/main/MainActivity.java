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
import android.support.annotation.BinderThread;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.igexin.sdk.PushManager;
import com.obsessive.zbar.CaptureActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.update.UpdateManager;
import com.smart.cloud.fire.activity.AddDev.AddDevActivity;
import com.smart.cloud.fire.activity.AddDev.ChioceDevTypeActivity;
import com.smart.cloud.fire.activity.AlarmHistory.AlarmHistoryActivity;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokeActivity;
import com.smart.cloud.fire.activity.Camera.CameraDevActivity;
import com.smart.cloud.fire.activity.Electric.ElectricDevActivity;
import com.smart.cloud.fire.activity.MyZoomActivity;
import com.smart.cloud.fire.activity.SecurityDev.SecurityDevActivity;
import com.smart.cloud.fire.activity.WiredDev.WiredDevActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MainService;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.login.SplashActivity;
import com.smart.cloud.fire.mvp.main.presenter.MainPresenter;
import com.smart.cloud.fire.mvp.main.view.MainView;
import com.smart.cloud.fire.service.RemoteService;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.view.MyRadioButton;
import com.smart.cloud.fire.yoosee.P2PListener;
import com.smart.cloud.fire.yoosee.SettingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class MainActivity extends MvpActivity<MainPresenter> implements MainView {

    private Context mContext;
    private AlertDialog dialog_update;
    @Bind(R.id.home_alarm_light)
    ImageView home_alarm_light;
    @Bind(R.id.my_image)
    ImageView my_image;
    @Bind(R.id.sxcs_btn)
    ImageView sxcs_btn;
    @Bind(R.id.tjsb_btn)
    ImageView tjsb_btn;
    @Bind(R.id.dqfh_btn)
    ImageView dqfh_btn;
    @Bind(R.id.spjk_btn)
    ImageView spjk_btn;
    @Bind(R.id.zddw_btn)
    ImageView zddw_btn;
    @Bind(R.id.xfwl_btn)
    ImageView xfwl_btn;
    @Bind(R.id.home_alarm_lin)
    LinearLayout home_alarm_lin;
    @Bind(R.id.home_alarm_info_text)
    TextView home_alarm_info_text;

    Timer getlastestAlarm;
    AnimationDrawable anim ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        regFilter();
        anim = (AnimationDrawable) home_alarm_light.getBackground();
        startService(new Intent(MainActivity.this, RemoteService.class));
        //启动个推接收推送信息。。
        PushManager.getInstance().initialize(this.getApplicationContext(), com.smart.cloud.fire.geTuiPush.DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), com.smart.cloud.fire.geTuiPush.DemoIntentService.class);
    }

    @OnClick({R.id.my_image,R.id.sxcs_btn,R.id.tjsb_btn,R.id.alarm_history_lin,R.id.dqfh_btn,R.id.spjk_btn,R.id.zddw_btn,
            R.id.xfwl_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_image:
                Intent intent = new Intent(mContext, MyZoomActivity.class);
                startActivity(intent);
                break;
            case R.id.sxcs_btn:
                Intent intent_sxcs = new Intent(mContext, AllSmokeActivity.class);
                startActivity(intent_sxcs);
                break;
            case R.id.tjsb_btn:
                Intent intent_tjsb = new Intent(mContext, ChioceDevTypeActivity.class);
                startActivity(intent_tjsb);
                break;
            case R.id.alarm_history_lin:
                Intent intent_history = new Intent(mContext, AlarmHistoryActivity.class);
                startActivity(intent_history);
                break;
            case R.id.dqfh_btn:
                Intent intent_dqfh = new Intent(mContext, ElectricDevActivity.class);
                startActivity(intent_dqfh);
                break;
            case R.id.spjk_btn:
                Intent intent_spjk = new Intent(mContext, CameraDevActivity.class);
                startActivity(intent_spjk);
                break;
            case R.id.zddw_btn:
                Intent intent_zddw = new Intent(mContext, WiredDevActivity.class);
                startActivity(intent_zddw);
                break;
            case R.id.xfwl_btn:
                Intent intent_xfwl = new Intent(mContext, SecurityDevActivity.class);
                startActivity(intent_xfwl);
            default:
                break;
        }
    }

    private void initView() {
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
                RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
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
                                            home_alarm_lin.setBackgroundResource(R.drawable.corners_shape_top);
                                        }else{
                                            anim.stop();
                                            home_alarm_info_text.setText(lasteatalarm.getString("address")
                                                    +"\n"+lasteatalarm.getString("name")+"发生报警【已处理】");
                                            home_alarm_lin.setBackgroundResource(R.drawable.corners_shape_top_normal);
                                        }
                                    }else{
                                        anim.stop();
                                        home_alarm_info_text.setText("无最新报警信息");
                                        home_alarm_lin.setBackgroundResource(R.drawable.corners_shape_top_normal);
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
                        home_alarm_lin.setBackgroundResource(R.drawable.corners_shape_top_normal);
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
        filter.addAction("Constants.Action.ACTION_UPDATE");
        filter.addAction("Constants.Action.ACTION_UPDATE_NO");
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
                PushManager.getInstance().stopService(getApplicationContext());
                unbindAlias();
                Intent in = new Intent(mContext, SplashActivity.class);
                startActivity(in);
                finish();
            }
            //已是最新版本。。
            if (intent.getAction().equals("Constants.Action.ACTION_UPDATE_NO")) {
                View view = LayoutInflater.from(mContext).inflate(
                        R.layout.dialog_update, null);
                TextView title = (TextView) view.findViewById(R.id.title_text);
                WebView content = (WebView) view
                        .findViewById(R.id.content_text);
                TextView button2 = (TextView) view
                        .findViewById(R.id.button2_text);
                ImageView minddle_image = (ImageView) view
                        .findViewById(R.id.minddle_image);
                RelativeLayout cancel_rela_dialog = (RelativeLayout) view
                        .findViewById(R.id.cancel_rela_dialog);
                title.setText("更新消息");
                content.setBackgroundColor(getResources().getColor(R.color.update_message)); // 设置背景色
                content.getBackground().setAlpha(255); // 设置填充透明度 范围：0-255
                content.loadDataWithBaseURL(null, "已是最新版本！", "text/html", "utf-8",
                        null);
                minddle_image.setVisibility(View.GONE);
                cancel_rela_dialog.setVisibility(View.GONE);
                button2.setText("确定");
                button2.setTextColor(Color.BLACK);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                dialog_update = builder.create();
                dialog_update.show();
                dialog_update.setContentView(view);
                button2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        if (null != dialog_update) {
                            dialog_update.cancel();
                        }
                    }
                });
            }
            //有新版本。。
            if (intent.getAction().equals("Constants.Action.ACTION_UPDATE")) {
                if (null != dialog_update && dialog_update.isShowing()) {
                    return;
                }
                View view = LayoutInflater.from(mContext).inflate(
                        R.layout.dialog_update, null);
                TextView title = (TextView) view.findViewById(R.id.title_text);
                WebView content = (WebView) view
                        .findViewById(R.id.content_text);
                TextView button1 = (TextView) view
                        .findViewById(R.id.button1_text);
                TextView button2 = (TextView) view
                        .findViewById(R.id.button2_text);

                title.setText("更新");
                content.setBackgroundColor(Color.WHITE); // 设置背景色
                content.getBackground().setAlpha(100); // 设置填充透明度 范围：0-255
                String data = intent.getStringExtra("message");
                final String downloadPath = intent.getStringExtra("url");
                content.loadDataWithBaseURL(null, data, "text/html", "utf-8",
                        null);
                button1.setText("立即更新");
                button2.setText("下次再说");
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != dialog_update) {
                            dialog_update.dismiss();
                            dialog_update = null;
                        }
                        if (UpdateManager.getInstance().getIsDowning()) {
                            return;
                        }
                        MyApp.app.showDownNotification(
                                UpdateManager.HANDLE_MSG_DOWNING, 0);
                        new Thread() {
                            public void run() {
                                UpdateManager.getInstance().downloadApk(handler,
                                        ConstantValues.Update.SAVE_PATH,
                                        ConstantValues.Update.FILE_NAME, downloadPath);
                            }
                        }.start();
                    }
                });
                final String ignoreVersion=intent.getStringExtra("ignoreVersion");//@@7.12
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != dialog_update) {
                            dialog_update.cancel();
                            SharedPreferencesManager.getInstance().putData(context,"ignoreVersion",ignoreVersion);//@@7.12
                        }
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                dialog_update = builder.create();
                dialog_update.show();
                dialog_update.setContentView(view);
                FrameLayout.LayoutParams layout = (FrameLayout.LayoutParams) view
                        .getLayoutParams();
                layout.width = (int) mContext.getResources().getDimension(
                        R.dimen.update_dialog_width);
                view.setLayoutParams(layout);
                dialog_update.setCanceledOnTouchOutside(false);
                Window window = dialog_update.getWindow();
                window.setWindowAnimations(R.style.dialog_normal);
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
        RequestQueue mQueue = Volley.newRequestQueue(this);
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

    Handler handler = new Handler() {
        long last_time;

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            int value = msg.arg1;
            switch (msg.what) {
                case UpdateManager.HANDLE_MSG_DOWNING:
                    if ((System.currentTimeMillis() - last_time) > 1000) {
                        MyApp.app.showDownNotification(
                                UpdateManager.HANDLE_MSG_DOWNING, value);
                        last_time = System.currentTimeMillis();
                    }
                    break;
                case UpdateManager.HANDLE_MSG_DOWN_SUCCESS:
                    MyApp.app.hideDownNotification();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/" + ConstantValues.Update.SAVE_PATH + "/"
                            + ConstantValues.Update.FILE_NAME);
                    if (!file.exists()) {
                        return;
                    }
                    intent.setDataAndType(Uri.fromFile(file),
                            ConstantValues.Update.INSTALL_APK);
                    mContext.startActivity(intent);
                    break;
                case UpdateManager.HANDLE_MSG_DOWN_FAULT:
                    break;
            }
        }
    };

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
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
