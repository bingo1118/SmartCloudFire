package com.smart.cloud.fire.activity.NFCDev;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import fire.cloud.smart.com.smartcloudfire.R;

public class NFCImageShowActivity extends Activity {

    ImageView image;
    ProgressBar mProgressBar;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcimage_show);

        mContext=this;
        mProgressBar=(ProgressBar)findViewById(R.id.mProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        image=(ImageView)findViewById(R.id.photo_image);
        String path=getIntent().getStringExtra("path");
        Glide.with(this)
                .load(path)
                .skipMemoryCache(true)
                .diskCacheStrategy( DiskCacheStrategy.NONE )//取消缓存图片
                .thumbnail(0.01f).listener(new RequestListener() {

            @Override
            public boolean onException(Exception arg0, Object arg1,
                                       Target arg2, boolean arg3) {
                //加载图片出错
                Toast.makeText(mContext,"无可加载的图片",Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Object arg0, Object arg1,
                                           Target arg2, boolean arg3, boolean arg4) {
                //加载图片成功
                mProgressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(image);//@@9.28
    }
}
