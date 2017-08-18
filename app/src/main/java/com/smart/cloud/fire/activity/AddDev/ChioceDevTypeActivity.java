package com.smart.cloud.fire.activity.AddDev;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.smart.cloud.fire.activity.AddNFC.AddNFCActivity;
import com.smart.cloud.fire.mvp.camera.AddCameraFirstActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class ChioceDevTypeActivity extends Activity {

    @Bind(R.id.sxcs_btn)
    ImageButton sxcs_btn;
    @Bind(R.id.zddw_btn)
    ImageButton zddw_btn;
    @Bind(R.id.dqfh_btn)
    ImageButton dqfh_btn;
    @Bind(R.id.xfwl_btn)
    ImageButton xfwl_btn;
    @Bind(R.id.spjk_btn)
    ImageButton spjk_btn;
    @Bind(R.id.nfc_btn)
    ImageButton nfc_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chioce_dev_type);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.sxcs_btn,R.id.zddw_btn,R.id.dqfh_btn,R.id.xfwl_btn,R.id.spjk_btn,R.id.nfc_btn})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.sxcs_btn:
                Intent intent1=new Intent(ChioceDevTypeActivity.this,AddDevActivity.class);
                intent1.putExtra("devType",1);
                startActivity(intent1);
                break;
            case R.id.zddw_btn:
                Intent intent2=new Intent(ChioceDevTypeActivity.this,AddDevActivity.class);
                intent2.putExtra("devType",2);
                startActivity(intent2);
                break;
            case R.id.dqfh_btn:
                Intent intent3=new Intent(ChioceDevTypeActivity.this,AddDevActivity.class);
                intent3.putExtra("devType",3);
                startActivity(intent3);
                break;
            case R.id.xfwl_btn:
                Intent intent4=new Intent(ChioceDevTypeActivity.this,AddDevActivity.class);
                intent4.putExtra("devType",4);
                startActivity(intent4);
                break;
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
        }
    }
}
