package com.smart.cloud.fire.mvp.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

import com.mediatek.elian.ElianNative;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.yoosee.UDPHelper;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fire.cloud.smart.com.smartcloudfire.R;

public class AddWaitActicity extends Activity {
    private Context mContext;
    private AnimationDrawable mAnimationDrawable;
    public Handler myhandler = new Handler();
    private long TimeOut;
    public UDPHelper mHelper;
    boolean isReceive = false;
    ElianNative elain;
    private boolean isSendWifiStop = true;
    private boolean isTimerCancel = true;
    byte type;
    int mLocalIp;
    String ssid, pwd;
    WifiManager.MulticastLock lock;
    private List<String> cameraList;
    private boolean isNeedSendWifi = true;// 二维码页面返回时不需要发包
    static {
        System.loadLibrary("elianjni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wait_acticity);
        mContext = this;
        ImageView im  = (ImageView) findViewById(R.id.connect_doorsensor_image);
        im.setBackgroundResource(R.drawable.connect_doorsensor_anim);
        mAnimationDrawable = (AnimationDrawable) im.getBackground();
        WifiManager manager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        cameraList = (List<String>) getIntent().getSerializableExtra(
                "cameraList");
        lock = manager.createMulticastLock("localWifi");
        ssid = getIntent().getStringExtra("ssidname");
        pwd = getIntent().getStringExtra("wifiPwd");
        type = getIntent().getByteExtra("type", (byte) -1);
        mLocalIp = getIntent().getIntExtra("LocalIp", -1);
        isNeedSendWifi = getIntent().getBooleanExtra("isNeedSendWifi", true);
        if (isNeedSendWifi) {
            TimeOut = 110 * 1000;
            excuteTimer();
        } else {
            TimeOut = 60 * 1000;
        }
        lock.acquire();
        mHelper = new UDPHelper(9988);
        listen();
        myhandler.postDelayed(mrunnable, TimeOut);
        mHelper.StartListen();
        startConnect();
    }

    private Timer mTimer;
    private int time;

    private void excuteTimer() {
        mTimer = new Timer();
        TimerTask mTask = new TimerTask() {
            @Override
            public void run() {
                if (time < 3) {
                    sendWifiHandler.sendEmptyMessage(0);
                } else {
                    sendWifiHandler.sendEmptyMessage(1);
                }
            }
        };
        mTimer.schedule(mTask, 500, 30 * 1000);
        isTimerCancel = false;
    }

    private Handler sendWifiHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message arg0) {
            switch (arg0.what) {
                case 0:
                    time++;
                    sendWifi();
                    break;
                case 1:
                    cancleTimer();
                    break;
                case 2:
                    stopSendWifi();
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    /**
     * 发包 20秒后停止
     */
    private void sendWifi() {
        if (elain == null) {
            elain = new ElianNative();
        }
        if (null != ssid && !"".equals(ssid)) {
            elain.InitSmartConnection(null, 1, 1);
            elain.StartSmartConnection(ssid, pwd, "", type);
            isSendWifiStop = false;
        }
        sendWifiHandler.postDelayed(stopRunnable, 20 * 1000);
    }

    public Runnable stopRunnable = new Runnable() {
        @Override
        public void run() {
            sendWifiHandler.sendEmptyMessage(2);
        }
    };

    private void cancleTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            isTimerCancel = true;
        }

    }

    /**
     * 停止发包
     */
    private void stopSendWifi() {
        if (elain != null) {
            elain.StopSmartConnection();
            isSendWifiStop = true;
        }
    }

    void listen() {
        mHelper.setCallBack(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case UDPHelper.HANDLER_MESSAGE_BIND_ERROR:
                        Toast.makeText(mContext, "端口被占用", Toast.LENGTH_SHORT).show();
                        break;
                    case UDPHelper.HANDLER_MESSAGE_RECEIVE_MSG:
                        isReceive = true;
                        // NormalDialog successdialog=new NormalDialog(mContext);
                        // successdialog.successDialog();
                        Toast.makeText(mContext, "嘿嘿！设置成功！", Toast.LENGTH_SHORT).show();
                        mHelper.StopListen();
                        Bundle bundle = msg.getData();

                        Intent it = new Intent();
                        it.setAction(ConstantValues.Action.RADAR_SET_WIFI_SUCCESS);
                        sendBroadcast(it);
                        String contactId = bundle.getString("contactId");
                        String frag = bundle.getString("frag");
                        String ipFlag = bundle.getString("ipFlag");
                        Intent add_device = new Intent(mContext,
                                AddCameraFourthActivity.class);
                        add_device.putExtra("contactId", contactId);
                        if (Integer.parseInt(frag) == ConstantValues.DeviceFlag.UNSET_PASSWORD) {
                            add_device.putExtra("isCreatePassword", true);
                        } else {
                            add_device.putExtra("isCreatePassword", false);
                        }
                        add_device.putExtra("isfactory", true);
                        add_device.putExtra("ipFlag", ipFlag);
                        startActivity(add_device);
                        stopConnect();
                        finish();
                        break;
                }
                cancleTimer();
            }

        });
    }

    private void stopConnect(){
        mAnimationDrawable.stop();
    }

    private void startConnect(){
        mAnimationDrawable.start();
    }

    public Runnable mrunnable = new Runnable() {

        @Override
        public void run() {
            if (!isReceive) {
                if (isNeedSendWifi) {
                    Toast.makeText(mContext, "设置Wi-Fi失败", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent();
                    it.setAction(ConstantValues.Action.RADAR_SET_WIFI_FAILED);
                    sendBroadcast(it);
                    // 跳转
                    finish();
                } else {
                    Toast.makeText(mContext, "设置Wi-Fi失败", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        myhandler.removeCallbacks(mrunnable);
        sendWifiHandler.removeCallbacks(stopRunnable);
        mHelper.StopListen();
        if (!isSendWifiStop) {
            stopSendWifi();
        }
        if (!isTimerCancel) {
            cancleTimer();
        }
        lock.release();
        stopConnect();
    }
}
