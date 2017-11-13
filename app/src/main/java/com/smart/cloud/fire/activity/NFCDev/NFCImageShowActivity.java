package com.smart.cloud.fire.activity.NFCDev;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import fire.cloud.smart.com.smartcloudfire.R;

public class NFCImageShowActivity extends Activity {

    ImageView image;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcimage_show);

        mProgressBar=(ProgressBar)findViewById(R.id.mProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        image=(ImageView)findViewById(R.id.photo_image);
        String path=getIntent().getStringExtra("path");
        Glide.with(this)
                .load(path).thumbnail(0.000001f).listener(new RequestListener() {

            @Override
            public boolean onException(Exception arg0, Object arg1,
                                       Target arg2, boolean arg3) {
                //加载图片出错
                return false;
            }

            @Override
            public boolean onResourceReady(Object arg0, Object arg1,
                                           Target arg2, boolean arg3, boolean arg4) {
                //加载图片成功
                mProgressBar.setVisibility(View.GONE);
                return false;
            }
        })
                .into(image);//@@9.28
    }
}
