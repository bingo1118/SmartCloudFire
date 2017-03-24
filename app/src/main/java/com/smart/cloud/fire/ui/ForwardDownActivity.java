package com.smart.cloud.fire.ui;

/**
 * Created by Administrator on 2016/9/19.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.p2p.core.update.UpdateManager;
import com.smart.cloud.fire.global.ConstantValues;

import java.io.File;


public class ForwardDownActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        int state = this.getIntent().getIntExtra("state", -1);
        System.out.println("state====f======"+state);
        switch(state){
            case UpdateManager.HANDLE_MSG_DOWN_SUCCESS:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File file = new File(Environment.getExternalStorageDirectory()+"/"+ ConstantValues.Update.SAVE_PATH+"/"+ ConstantValues.Update.FILE_NAME);
                if(!file.exists()){
                    return;
                }
                intent.setDataAndType(Uri.fromFile(file), ConstantValues.Update.INSTALL_APK);
                this.startActivity(intent);
                break;
            case UpdateManager.HANDLE_MSG_DOWNING:
                UpdateManager.getInstance().cancelDown();
                break;
            case UpdateManager.HANDLE_MSG_DOWN_FAULT:

                break;
        }
        finish();
    }
}


