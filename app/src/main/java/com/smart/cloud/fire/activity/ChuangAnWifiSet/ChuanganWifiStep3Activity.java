package com.smart.cloud.fire.activity.ChuangAnWifiSet;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.activity.AddDev.AddDevActivity;
import com.smart.cloud.fire.utils.ASCIIToString;
import com.smart.cloud.fire.utils.ByteToString;
import com.smart.cloud.fire.utils.CRC16;
import com.smart.cloud.fire.utils.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import fire.cloud.smart.com.smartcloudfire.R;

public class ChuanganWifiStep3Activity extends Activity implements View.OnClickListener {

    private Context mContext;
    private Button action_one;
    private TextView wifi_name;
    private String wifiName, wifiPwd;
    private Timer mTimer, mTimer1;
    private SocketUDP mSocketUDPClient;
    private WifiManager wifiManager = null;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuangan_wifi_step3);
        mContext = this;
        wifiName = getIntent().getExtras().getString("wifiName");
        wifiPwd = getIntent().getExtras().getString("wifiPwd");
        init();
        regFilter();
    }
    private void init() {
        // TODO Auto-generated method stub
        action_one = (Button) findViewById(R.id.action_one);
        action_one.setOnClickListener(this);
        wifi_name = (TextView) findViewById(R.id.wifi_name);
        progress=(ProgressBar)findViewById(R.id.progress);
        wifi_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        wifi_name.setTextColor(getResources().getColor(
                R.color.wifi_change_color));
        wifi_name.setOnClickListener(this);
        connectUDP();
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("Constants.Action.WiFiSetAck");
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            if(arg1.getAction().equals("Constants.Action.WiFiSetAck")){
                byte[] datas = arg1.getExtras().getByteArray("datasByte");
                if(datas!=null&& datas.length>12){
                    byte[] mac={datas[6],datas[7],datas[8],datas[9],datas[10],datas[11]};
                    String id= Utils.bytesToHexString(mac);
                    Intent intent=new Intent(mContext, AddDevActivity.class);
                    intent.putExtra("mac",id);
                    startActivity(intent);
                    progress.setVisibility(View.GONE);
                    finish();
                }
            }
        }
    };



    private void connectUDP() {
        try {
            mSocketUDPClient = SocketUDP.newInstance(
                    InetAddress.getByName("255.255.255.255"), 8266);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        mSocketUDPClient.startAcceptMessage();
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.wifi_name:
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                break;
            case R.id.action_one:
                // connectTCP();
                String devWifiName = wifi_name.getText().toString().trim();
                if (mSocketUDPClient!=null) {
                    //开始配置设备
                    mSocketUDPClient.sendMsg(SendServerOrder.WifiSetOrder(wifiName,wifiPwd));
                    progress.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(mContext, R.string.please_choose_right_devide_wifi, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if(mTimer1!=null){
            mTimer1.cancel();
            mTimer1=null;
        }
        super.onDestroy();
        if (mSocketUDPClient != null) {
            mSocketUDPClient.clearClient();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return true;

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void removeWifi(){
        int netID = 0;
        mSocketUDPClient.clearClient();
        WifiManager wifiManager = (WifiManager) mContext
                .getSystemService(Service.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssidStr = info.getSSID();
        List<WifiConfiguration> existingConfigs = wifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.toString().equals(ssidStr)) {
                netID = existingConfig.networkId;
            }
        }
        if(ssidStr.startsWith("\""+"HRSST_")&&netID!=0&&ssidStr.length()==20){
            wifiManager.removeNetwork(netID);
        }

        finish();
    }

    private void removeWifi2(){
        mSocketUDPClient.clearClient();
        int netID = 0;
        WifiManager wifiManager = (WifiManager) mContext
                .getSystemService(Service.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssidStr = info.getSSID();
        List<WifiConfiguration> existingConfigs = wifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.toString().equals(ssidStr)) {
                netID = existingConfig.networkId;
            }
        }
        if(ssidStr.startsWith("\""+"HRSST_")&&netID!=0&&ssidStr.length()==20){
            wifiManager.removeNetwork(netID);
        }

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        wifi_name.setText(getWIfi());
        try {
            mSocketUDPClient = SocketUDP.newInstance(
                    InetAddress.getByName("255.255.255.255"), 8266);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private String getWIfi(){
        WifiManager wifiMgr = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        int wifiState = wifiMgr.getWifiState();
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        return wifiId;
    }

}
