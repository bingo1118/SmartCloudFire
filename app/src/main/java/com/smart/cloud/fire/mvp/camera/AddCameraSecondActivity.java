package com.smart.cloud.fire.mvp.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fire.cloud.smart.com.smartcloudfire.R;

public class AddCameraSecondActivity extends Activity {
    private Context mContext;
    private Button add_camera_action_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera_second);
        mContext = this;
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        add_camera_action_two = (Button) findViewById(R.id.add_camera_action_two);
        add_camera_action_two.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, AddCameraThirdActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}
