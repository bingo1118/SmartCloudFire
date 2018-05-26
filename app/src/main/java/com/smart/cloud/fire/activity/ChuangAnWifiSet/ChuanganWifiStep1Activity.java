package com.smart.cloud.fire.activity.ChuangAnWifiSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import fire.cloud.smart.com.smartcloudfire.R;

public class ChuanganWifiStep1Activity extends Activity {

    private Context mContext;
    private ImageView next_action;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuangan_wifi_step1);
        mContext = this;
        ImageView liuchengtu1 = (ImageView) findViewById(R.id.liuchengtu1_image);
        next_action = (ImageView) findViewById(R.id.next_action);
        next_action.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(checkWifi()){
                    Intent intent = new Intent(ChuanganWifiStep1Activity.this,ChuanganWifiStep2Activity.class);
                    startActivity(intent);
//					finish();
                }else{
                    Toast.makeText(mContext, "请连接WiFi",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private boolean checkWifi() {
        boolean isWifiConnect = false;
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (int i = 0; i<networkInfos.length; i++) {
            if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                if(networkInfos[i].getType() == cm.TYPE_MOBILE) {
                    isWifiConnect = false;
                }
                if(networkInfos[i].getType() == cm.TYPE_WIFI) {
                    isWifiConnect = true;
                }
            }
        }
        return isWifiConnect;
    }
}
