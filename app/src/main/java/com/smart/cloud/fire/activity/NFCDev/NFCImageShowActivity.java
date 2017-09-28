package com.smart.cloud.fire.activity.NFCDev;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import fire.cloud.smart.com.smartcloudfire.R;

public class NFCImageShowActivity extends Activity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcimage_show);

        image=(ImageView)findViewById(R.id.photo_image);
        String path=getIntent().getStringExtra("path");
        Glide.with(this)
                .load(path).thumbnail(0.0001f)
                .into(image);//@@9.28
    }
}
