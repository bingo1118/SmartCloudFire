package com.smart.cloud.fire.mvp.printScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.GridView;

import com.smart.cloud.fire.adapter.ImageBrowserAdapter;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.ui.ApMonitorActivity;

import java.io.File;

import fire.cloud.smart.com.smartcloudfire.R;

public class PrintScreenActivity extends Activity {
    private Context mContext;
    File[] files;
    GridView list;
    ImageBrowserAdapter adapter;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_screen);
        mContext = this;
        contact = (Contact) getIntent().getExtras().getSerializable("contact");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(mContext,ApMonitorActivity.class);
            i.putExtra("contact",contact);
            startActivity(i);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

