package com.smart.cloud.fire.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class BingoDialog {

    private  Window mWindow;
    private Dialog mDialog;


    public Dialog getmDialog() {
        return mDialog;
    }

    public BingoDialog(Activity activity, View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity).setView(view);
        mDialog=builder.create();
    }

    public BingoDialog(AlertDialog.Builder builder){
        mDialog=builder.create();
    }

    public void show(){
        mWindow = mDialog.getWindow();
        mWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindow.setBackgroundDrawableResource(android.R.color.white);
        mWindow.setAttributes(lp);
        mDialog.show();
    }

    public void dismiss(){
        if(mDialog!=null)
            mDialog.dismiss();
    }
}
