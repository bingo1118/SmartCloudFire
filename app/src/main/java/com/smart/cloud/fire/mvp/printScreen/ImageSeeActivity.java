package com.smart.cloud.fire.mvp.printScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.lib.imagesee.FilePagerAdapter;
import com.lib.imagesee.GalleryViewPager;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import fire.cloud.smart.com.smartcloudfire.R;

public class ImageSeeActivity extends Activity {
    File[] files;
    List<String> imagePath;
    private GalleryViewPager mViewPager;
    private Context mContext;
    private int currentItem;
    String callId;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_see);
        mContext = this;
        mIntent = getIntent();
        currentItem = mIntent.getIntExtra("currentImage", 0);
        callId = getIntent().getStringExtra("callId");
        initUI(currentItem);
    }

    public void initUI(int position) {
        imagePath = new ArrayList<String>();
        String screenshotPath = Environment.getExternalStorageDirectory()
                .getPath() + "/screenshot";
        File file = new File(screenshotPath);
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (null == callId || "".equals(callId)) {
                    if (pathname.getName().endsWith(".jpg")) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (pathname.getName().startsWith(callId)) {
                        return true;
                    } else {
                        return false;
                    }
                }

            }
        };
        files = file.listFiles(filter);
        for (int i = 0, count = files.length; i < count; i++) {
            imagePath.add((files[i]).getPath());
        }

        FilePagerAdapter pagerAdapter = new FilePagerAdapter(mContext,
                imagePath);
//		pagerAdapter.setOnItemChangeListener(new OnItemChangeListener() {
//			@Override
//			public void onItemChange(int currentPosition) {
////				Toast.makeText(mContext, "Current item is " + currentPosition,
////						Toast.LENGTH_SHORT).show();
//			}
//		});

        mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(position);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
