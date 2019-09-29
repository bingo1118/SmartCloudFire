package com.smart.cloud.fire.global;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.widget.RemoteViews;

import com.baidu.mapapi.SDKInitializer;
import com.hikvision.open.hikvideoplayer.HikVideoPlayerFactory;
import com.p2p.core.P2PHandler;
import com.p2p.core.update.UpdateManager;
import com.smart.cloud.fire.activity.Functions.constant.Constant;
import com.smart.cloud.fire.service.LocationService;
import com.smart.cloud.fire.ui.ForwardDownActivity;
import com.smart.cloud.fire.utils.AutoScreenUtils;
import com.smart.cloud.fire.utils.CrashHandler;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.yoosee.P2PListener;
import com.smart.cloud.fire.yoosee.SettingListener;
import com.squareup.leakcanary.LeakCanary;
//import com.taobao.sophix.PatchStatus;
//import com.taobao.sophix.SophixManager;
//import com.taobao.sophix.listener.PatchLoadStatusListener;

import org.litepal.LitePal;

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
    private static int privilege=-1;
    public LocationService locationService;
    public Vibrator mVibrator;
    public static String userid;

    public static long a;
    public static long b;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(this);
        //启动集错程序
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        //检查内存是否泄漏初始化，正式版应该关闭
        LeakCanary.install(this);
        LitePal.initialize(this);//数据库框架
        SQLiteDatabase db = LitePal.getDatabase();
        P2PHandler.getInstance().p2pInit(this,
                new P2PListener(),
                new SettingListener());
        HikVideoPlayerFactory.initLib(null, true);
//        AutoScreenUtils.AdjustDensity(this);//屏幕适配
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //Sophix 初始化
        PackageInfo packageInfo = null;
        try {
            packageInfo =  base.getPackageManager()
                    .getPackageInfo(base.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // initialize必须放在attachBaseContext最前面，初始化代码直接写在Application类里面，切勿封装到其他类。
//        SophixManager.getInstance().setContext(this)
//                .setAppVersion(packageInfo.versionName)
//                .setAesKey(null)
//                .setEnableDebug(true)
//                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
//                    @Override
//                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
//                        // 补丁加载回调通知
//                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
//                            T.showShort(app,"加载成功");
//                            // 表明补丁加载成功
//                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
//                            T.showShort(app,"重启应用可生效");
//                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
//                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
//                        } else {
//                            // 其它错误信息, 查看PatchStatus类说明
//                        }
//                    }
//                }).initialize();
//        SophixManager.getInstance().queryAndLoadNewPatch();

    }

    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

    public  void setPrivilege(int privilege){
        if(privilege==6||privilege==7){
            this.privilege=3;
        }else{
            this.privilege = privilege;
        }
        userid=SharedPreferencesManager.getInstance().getData(MyApp.app,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        Constant.APPLY_MINE="APPLY_MINE"+userid;
        Constant.APPLY_MORE="APPLY_MORE"+userid;
    }


    public static int getPrivilege(){
        //return privilege;
        return privilege;
    }

    public static String getUserID(){
        if(userid==""){
            return SharedPreferencesManager.getInstance().getData(MyApp.app,
                    SharedPreferencesManager.SP_FILE_GWELL,
                    SharedPreferencesManager.KEY_RECENTNAME);
        }else{
            return userid;
        }//@@5.5防止突然网络错误问题
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
