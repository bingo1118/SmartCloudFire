package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import fire.cloud.smart.com.smartcloudfire.R;

public class WiredSmokeHistoryActivity extends Activity {

    TextView titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wired_smoke_history);

        titleName=(TextView)findViewById(R.id.title_tv);
        if(getIntent().getStringExtra("HostType").equals("224")){
            titleName.setText("区域："+getIntent().getStringExtra("Mac")+"分区");
        }else{
            titleName.setText("编号："+getIntent().getStringExtra("Mac"));
        }

    }
}
