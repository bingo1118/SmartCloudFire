package com.smart.cloud.fire.mvp.printScreen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;

import com.smart.cloud.fire.adapter.ImageBrowserAdapter;

import java.io.File;

import fire.cloud.smart.com.smartcloudfire.R;

public class PrintScreenActivity extends Activity {
    private Context mContext;
    File[] files;
    GridView list;
    ImageBrowserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_screen);
        mContext = this;
        if(null==files){
            files = new File[0];
        }
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        list = (GridView)findViewById(R.id.list_grid);
        adapter = new ImageBrowserAdapter(mContext);
        list.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}

