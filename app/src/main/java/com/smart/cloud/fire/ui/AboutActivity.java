package com.smart.cloud.fire.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.smart.cloud.fire.global.ConstantValues;

import fire.cloud.smart.com.smartcloudfire.R;

public class AboutActivity extends Activity{
    private Context mContext;
    private TextView about_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mContext = this;
        about_version = (TextView) findViewById(R.id.about_version);
        String version =getlocalVersion();
        if(ConstantValues.isThe148Service()){
            about_version.setText(version+"(正式版)");
        }else {
            about_version.setText(version+"(测试版)");
        }

    }

    private String getlocalVersion(){
        String localversion = "";
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            localversion = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return localversion;
    }
}

