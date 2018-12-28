package com.smart.cloud.fire.activity.AddDev;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.obsessive.zbar.AddCaptureActivity;
import com.smart.cloud.fire.activity.AddNFC.AddNFCActivity;
import com.smart.cloud.fire.mvp.camera.AddCameraFirstActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class ChioceDevTypeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chioce_dev_type);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.spjk_btn,R.id.nfc_btn,R.id.sdsr_btn,R.id.smsr_btn})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.spjk_btn:
                Intent intent5=new Intent(ChioceDevTypeActivity.this,AddCameraFirstActivity.class);
                intent5.putExtra("devType",5);
                startActivity(intent5);
                break;
            case R.id.nfc_btn:
                Intent intent6=new Intent(ChioceDevTypeActivity.this,AddNFCActivity.class);
                intent6.putExtra("devType",6);
                startActivity(intent6);
                break;
            case R.id.sdsr_btn:
                Intent intent7=new Intent(ChioceDevTypeActivity.this,AddDevActivity.class);
                intent7.putExtra("devType",1);
                startActivity(intent7);
                break;
            case R.id.smsr_btn:
                Intent intent8=new Intent(ChioceDevTypeActivity.this,AddCaptureActivity.class);
                intent8.putExtra("devType",6);
                startActivity(intent8);
                break;
        }
    }
}
