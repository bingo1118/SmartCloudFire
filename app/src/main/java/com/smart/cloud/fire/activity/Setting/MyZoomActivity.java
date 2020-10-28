
package com.smart.cloud.fire.activity.Setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.p2p.core.update.UpdateManager;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.login.SplashActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import java.io.File;
import java.util.List;

import fire.cloud.smart.com.smartcloudfire.R;

public class MyZoomActivity extends Activity {

    Context mContext;
    private AlertDialog dialog_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_zoom);
        mContext=this;

        regFilter();
    }

    /**
     * 添加广播接收器件。。
     */
    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("Constants.Action.ACTION_UPDATE");
        filter.addAction("Constants.Action.ACTION_UPDATE_NO");
        mContext.registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            //已是最新版本。。
            if (intent.getAction().equals("Constants.Action.ACTION_UPDATE_NO")) {
                View view = LayoutInflater.from(mContext).inflate(
                        R.layout.dialog_update, null);
                TextView title = (TextView) view.findViewById(R.id.title_text);
                WebView content = (WebView) view
                        .findViewById(R.id.content_text);
                TextView button2 = (TextView) view
                        .findViewById(R.id.button2_text);
                ImageView minddle_image = (ImageView) view
                        .findViewById(R.id.minddle_image);
                RelativeLayout cancel_rela_dialog = (RelativeLayout) view
                        .findViewById(R.id.cancel_rela_dialog);
                title.setText("更新消息");
                content.setBackgroundColor(getResources().getColor(R.color.update_message)); // 设置背景色
                content.getBackground().setAlpha(255); // 设置填充透明度 范围：0-255
                content.loadDataWithBaseURL(null, "已是最新版本！", "text/html", "utf-8",
                        null);
                minddle_image.setVisibility(View.GONE);
                cancel_rela_dialog.setVisibility(View.GONE);
                button2.setText("确定");
                button2.setTextColor(Color.BLACK);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                dialog_update = builder.create();
                dialog_update.show();
                dialog_update.setContentView(view);
                button2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        if (null != dialog_update) {
                            dialog_update.cancel();
                        }
                    }
                });
            }
            //有新版本。。
            if (intent.getAction().equals("Constants.Action.ACTION_UPDATE")) {
                if (null != dialog_update && dialog_update.isShowing()) {
                    return;
                }
                View view = LayoutInflater.from(mContext).inflate(
                        R.layout.dialog_update, null);
                TextView title = (TextView) view.findViewById(R.id.title_text);
                WebView content = (WebView) view
                        .findViewById(R.id.content_text);
                TextView button1 = (TextView) view
                        .findViewById(R.id.button1_text);
                TextView button2 = (TextView) view
                        .findViewById(R.id.button2_text);

                title.setText("更新");
                content.setBackgroundColor(Color.WHITE); // 设置背景色
                content.getBackground().setAlpha(100); // 设置填充透明度 范围：0-255
                String data = intent.getStringExtra("message");
                final String downloadPath = intent.getStringExtra("url");
                content.loadDataWithBaseURL(null, data, "text/html", "utf-8",
                        null);
                button1.setText("立即更新");
                button2.setText("下次再说");
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != dialog_update) {
                            dialog_update.dismiss();
                            dialog_update = null;
                        }
                        if (UpdateManager.getInstance().getIsDowning()) {
                            return;
                        }
                        MyApp.app.showDownNotification(
                                UpdateManager.HANDLE_MSG_DOWNING, 0);
                        new Thread() {
                            public void run() {
                                UpdateManager.getInstance().downloadApk(handler,
                                        ConstantValues.Update.SAVE_PATH,
                                        ConstantValues.Update.FILE_NAME, downloadPath);
                            }
                        }.start();
                    }
                });
                final String ignoreVersion=intent.getStringExtra("ignoreVersion");//@@7.12
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != dialog_update) {
                            dialog_update.cancel();
                            SharedPreferencesManager.getInstance().putData(context,"ignoreVersion",ignoreVersion);//@@7.12
                        }
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                dialog_update = builder.create();
                dialog_update.show();
                dialog_update.setContentView(view);
                FrameLayout.LayoutParams layout = (FrameLayout.LayoutParams) view
                        .getLayoutParams();
                layout.width = (int) mContext.getResources().getDimension(
                        R.dimen.update_dialog_width);
                view.setLayoutParams(layout);
                dialog_update.setCanceledOnTouchOutside(false);
                Window window = dialog_update.getWindow();
                window.setWindowAnimations(R.style.dialog_normal);
            }
        }
    };
    Handler handler = new Handler() {
        long last_time;

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            int value = msg.arg1;
            switch (msg.what) {
                case UpdateManager.HANDLE_MSG_DOWNING:
                    if ((System.currentTimeMillis() - last_time) > 1000) {
                        MyApp.app.showDownNotification(
                                UpdateManager.HANDLE_MSG_DOWNING, value);
                        last_time = System.currentTimeMillis();
                    }
                    break;
                case UpdateManager.HANDLE_MSG_DOWN_SUCCESS:
                    MyApp.app.hideDownNotification();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/" + ConstantValues.Update.SAVE_PATH + "/"
                            + ConstantValues.Update.FILE_NAME);
                    if (!file.exists()) {
                        return;
                    }
//                    intent.setDataAndType(Uri.fromFile(file),
//                            ConstantValues.Update.INSTALL_APK);
//                    mContext.startActivity(intent);
                    if(Build.VERSION.SDK_INT>24){
                        Uri uri= FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName()+".fileprovider",file);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        intent.setDataAndType(uri,
                                ConstantValues.Update.INSTALL_APK);
                        List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        for (ResolveInfo resolveInfo : resInfoList) {
                            mContext.grantUriPermission(resolveInfo.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }else{
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.fromFile(file),
                                ConstantValues.Update.INSTALL_APK);
                    }

                    startActivity(intent);
                    break;
                case UpdateManager.HANDLE_MSG_DOWN_FAULT:
                    break;
            }
        }
    };
}
