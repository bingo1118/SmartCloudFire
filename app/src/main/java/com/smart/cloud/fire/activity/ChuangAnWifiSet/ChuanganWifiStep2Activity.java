package com.smart.cloud.fire.activity.ChuangAnWifiSet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.p2p.core.global.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;

import fire.cloud.smart.com.smartcloudfire.R;

public class ChuanganWifiStep2Activity extends Activity {

    private Context mContext;
    private TextView device_wifi_name,wifi_name;
    private Button next_action_two;
    private EditText wifi_pwd;
    private SocketUDP mSocketUDPClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuangan_wifi_step2);
        mContext=this;
        init();
    }

    private void init() {
        device_wifi_name = (TextView) findViewById(R.id.device_wifi_name);
        device_wifi_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        device_wifi_name.setTextColor(getResources().getColor(R.color.wifi_change_color));
        next_action_two = (Button) findViewById(R.id.next_action_two_btn);
        wifi_name = (TextView) findViewById(R.id.wifi_name);
        wifi_pwd = (EditText) findViewById(R.id.wifi_pwd);

        wifi_name.setText(getResources().getString(R.string.introducedNextOneActivity_current_wifi)+getWIfi());
        wifi_name.setTextColor(getResources().getColor(R.color.wifi_change_color));
        try {
            mSocketUDPClient = SocketUDP.newInstance(
                    InetAddress.getByName("255.255.255.255"), 8266);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        mSocketUDPClient.startAcceptMessage();
        device_wifi_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        next_action_two.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String wifiName = getWIfi().replaceAll("\"", "");
                String wifiPwd = wifi_pwd.getText().toString();
                if(wifiName==null||wifiPwd==null){
                    Toast.makeText(mContext, R.string.introducedNextOneActivity_wifi_cannot_null,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(wifiName.length()==0||wifiPwd.length()==0){
                    Toast.makeText(mContext, R.string.introducedNextOneActivity_wifi_cannot_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(mContext, ChuanganWifiStep3Activity.class);
                intent.putExtra("serverTime", "");
                intent.putExtra("wifiName", wifiName);
                intent.putExtra("wifiPwd", wifiPwd);
                startActivity(intent);
                finish();
            }
        });
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        wifi_name.setText(getResources().getString(R.string.introducedNextOneActivity_current_wifi)+getWIfi());
    }

    private String getWIfi(){
        WifiManager wifiMgr = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        return wifiId;
    }
}
