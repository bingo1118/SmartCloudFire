package com.smart.cloud.fire.mvp.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import fire.cloud.smart.com.smartcloudfire.R;

public class AddCameraFirstActivity extends Activity {
    private Context mContext;
    private Button next_add_camera_first_btn;
    private ImageView add_camera_one_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera_first);
        mContext = this;
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        next_add_camera_first_btn = (Button) findViewById(R.id.add_camera_action_one);
        next_add_camera_first_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, AddCameraSecondActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}
