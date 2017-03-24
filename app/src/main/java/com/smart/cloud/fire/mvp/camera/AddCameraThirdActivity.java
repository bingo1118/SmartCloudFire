package com.smart.cloud.fire.mvp.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import fire.cloud.smart.com.smartcloudfire.R;

public class AddCameraThirdActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private TextView camera_wifi_name;
    private Button next_add_camera_second_btn;
    private EditText wifi_pwd;
    private String ssid;
    private int mLocalIp;
    boolean bool1, bool2, bool3, bool4;
    private byte mAuthMode;
    private byte AuthModeOpen = 0;
    private byte AuthModeWPA = 3;
    private byte AuthModeWPA1PSKWPA2PSK = 9;
    private byte AuthModeWPA1WPA2 = 8;
    private byte AuthModeWPA2 = 6;
    private byte AuthModeWPA2PSK = 7;
    private byte AuthModeWPAPSK = 4;
    private boolean isWifiOpen = false;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera_third);
        mContext = this;
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        camera_wifi_name = (TextView) findViewById(R.id.camera_wifi_name);
        camera_wifi_name.setOnClickListener(this);
        next_add_camera_second_btn = (Button) findViewById(R.id.next_add_camera_second_btn);
        next_add_camera_second_btn.setOnClickListener(this);
        wifi_pwd = (EditText) findViewById(R.id.camera_wifi_pwd);
        currentWifi();
    }

    private void currentWifi() {
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!manager.isWifiEnabled())
            return;
        WifiInfo info = manager.getConnectionInfo();
        ssid = info.getSSID();
        mLocalIp = info.getIpAddress();
        List<ScanResult> datas = new ArrayList<>();
        if (!manager.isWifiEnabled())
            return;
        manager.startScan();
        datas = manager.getScanResults();
        if (ssid == null) {
            return;
        }
        if (ssid.equals("")) {
            return;
        }
        int a = ssid.charAt(0);
        if (a == 34) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        if (!ssid.equals("<unknown ssid>") && !ssid.equals("0x")) {
            camera_wifi_name.setText(ssid);
        }
        for (int i = 0; i < datas.size(); i++) {
            ScanResult result = datas.get(i);
            if (!result.SSID.equals(ssid)) {
                continue;
            }
            if (Utils.isWifiOpen(result)) {
                type = 0;
                isWifiOpen = true;
            } else {
                type = 1;
                isWifiOpen = false;
            }
            bool1 = result.capabilities.contains("WPA-PSK");
            bool2 = result.capabilities.contains("WPA2-PSK");
            bool3 = result.capabilities.contains("WPA-EAP");
            bool4 = result.capabilities.contains("WPA2-EAP");
            if (result.capabilities.contains("WEP")) {
                this.mAuthMode = this.AuthModeOpen;
            }
            if ((bool1) && (bool2)) {
                mAuthMode = AuthModeWPA1PSKWPA2PSK;
            } else if (bool2) {
                this.mAuthMode = this.AuthModeWPA2PSK;
            } else if (bool1) {
                this.mAuthMode = this.AuthModeWPAPSK;
            } else if ((bool3) && (bool4)) {
                this.mAuthMode = this.AuthModeWPA1WPA2;
            } else if (bool4) {
                this.mAuthMode = this.AuthModeWPA2;
            } else {
                if (!bool3)
                    break;
                this.mAuthMode = this.AuthModeWPA;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_add_camera_second_btn:
                InputMethodManager manager = (InputMethodManager) getSystemService(mContext.INPUT_METHOD_SERVICE);
                if (manager != null) {
                    manager.hideSoftInputFromWindow(wifi_pwd.getWindowToken(), 0);
                }
                String wifiPwd = wifi_pwd.getText().toString();
                if (ssid == null || ssid.equals("")) {
                    Toast.makeText(mContext, "请先将手机连接无线网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ssid.equals("<unknown ssid>")) {
                    Toast.makeText(mContext, "请先将手机连接无线网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isWifiOpen) {
                    if (null == wifiPwd || wifiPwd.length() <= 0
                            && (type == 1 || type == 2)) {
                        Toast.makeText(mContext, "请输入Wi-Fi密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Intent device_network = new Intent(mContext, AddWaitActicity.class);
                device_network.putExtra("ssidname", ssid);
                device_network.putExtra("wifiPwd", wifiPwd);
                device_network.putExtra("type", mAuthMode);
                device_network.putExtra("LocalIp", mLocalIp);
                device_network.putExtra("isNeedSendWifi", true);
                startActivity(device_network);
                finish();
                break;
            default:
                break;
        }
    }
}
