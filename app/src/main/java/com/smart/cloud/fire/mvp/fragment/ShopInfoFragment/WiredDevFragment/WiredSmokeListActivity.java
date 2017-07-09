package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import fire.cloud.smart.com.smartcloudfire.R;

public class WiredSmokeListActivity extends Activity {
    TextView titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wired_smoke_list);

        titleName=(TextView)findViewById(R.id.title_tv);
        titleName.setText("终端："+getIntent().getStringExtra("Mac"));
    }
}
