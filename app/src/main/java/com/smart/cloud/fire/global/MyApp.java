package com.smart.cloud.fire.global;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.RemoteViews;

import com.baidu.mapapi.SDKInitializer;
import com.p2p.core.update.UpdateManager;
import com.smart.cloud.fire.service.LocationService;
import com.smart.cloud.fire.ui.ForwardDownActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.squareup.leakcanary.LeakCanary;

import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/7/28.
 */
public class MyApp extends Application {
    public static MyApp app;
    public String pushState;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    public static final int NOTIFICATION_DOWN_ID = 0x53256562;
    private RemoteViews cur_down_view;
    private int privilege=-1;
    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(this);
        //启动集错程序
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
        //检查内存是否泄漏初始化，正式版应该关闭
        LeakCanary.install(this);
    }

    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

    public  void setPrivilege(int privilege){
        this.privilege = privilege;
    }

    public int getPrivilege(){
        return privilege;
    }

    public String getPushState() {
        return pushState;
    }

    public void setPushState(String pushState) {
        this.pushState = pushState;
    }

    /**
     * 创建下载图标
     */
    @SuppressWarnings("deprecation")
    public  void showDownNotification(int state,int value) {
        boolean isShowNotify = SharedPreferencesManager.getInstance().getIsShowNotify(this);
        if(isShowNotify){

            mNotificationManager = getNotificationManager();
            mNotification = new Notification();
            long when = System.currentTimeMillis();
            mNotification = new Notification(
                    R.mipmap.ic_launcher,
                    this.getResources().getString(R.string.app_name),
                    when);
            // 放置在"正在运行"栏目中
            mNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_AUTO_CANCEL;

            RemoteViews contentView = new RemoteViews(getPackageName(),
                    R.layout.notify_down_bar);
            cur_down_view = contentView;
            contentView.setImageViewResource(R.id.icon,
                    R.mipmap.ic_launcher);

            Intent intent = new Intent(this,ForwardDownActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            switch(state){
                case UpdateManager.HANDLE_MSG_DOWN_SUCCESS:
                    cur_down_view.setTextViewText(R.id.down_complete_text, this.getResources().getString(R.string.down_complete_click));
                    cur_down_view.setTextViewText(R.id.progress_value,"100%");
                    contentView.setProgressBar(R.id.progress_bar, 100, 100, false);
                    intent.putExtra("state", UpdateManager.HANDLE_MSG_DOWN_SUCCESS);
                    break;
                case UpdateManager.HANDLE_MSG_DOWNING:
                    cur_down_view.setTextViewText(R.id.down_complete_text, this.getResources().getString(R.string.down_londing_click));
                    cur_down_view.setTextViewText(R.id.progress_value,value+"%");
                    contentView.setProgressBar(R.id.progress_bar, 100, value, false);
                    intent.putExtra("state", UpdateManager.HANDLE_MSG_DOWNING);
                    break;
                case UpdateManager.HANDLE_MSG_DOWN_FAULT:
                    cur_down_view.setTextViewText(R.id.down_complete_text, this.getResources().getString(R.string.down_fault_click));
                    cur_down_view.setTextViewText(R.id.progress_value,value+"%");
                    contentView.setProgressBar(R.id.progress_bar, 100, value, false);
                    intent.putExtra("state", UpdateManager.HANDLE_MSG_DOWN_FAULT);
                    break;
            }
            mNotification.contentView = contentView;
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mNotification.contentIntent = contentIntent;

            mNotificationManager.notify(NOTIFICATION_DOWN_ID,
                    mNotification);
        }
    }

    public void hideDownNotification(){
        mNotificationManager = getNotificationManager();
        mNotificationManager.cancel(NOTIFICATION_DOWN_ID);

    }
}
