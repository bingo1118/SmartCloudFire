package com.smart.cloud.fire.global;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Xml;

import com.smart.cloud.fire.utils.CompareVersion;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.Utils;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/8/8.
 */
public class MainThread {
    static MainThread manager;
    boolean isRun;
    private Main main;
    Context mContext;

    public MainThread(Context mContext) {
        this.mContext = mContext;
    }

    public static MainThread getInstance() {
        return manager;
    }

    class Main extends Thread {
        @Override
        public void run() {
            isRun = true;
            while (isRun) {
                long last_check_update_time = SharedPreferencesManager
                        .getInstance().getLastAutoCheckUpdateTime(MyApp.app);
                checkUpdate(last_check_update_time);
                Utils.sleepThread(60 * 1000);
            }
        }
    }

    private UpdateInfo mUpdateInfo = new UpdateInfo();
    public int checkUpdate(long last_check_update_time) {
        try {
            long now_time = System.currentTimeMillis();
            //1000 * 60 * 60 * 12
            if ((now_time - last_check_update_time) > 1000 * 60 * 60 * 12) {
                SharedPreferencesManager.getInstance()
                        .putLastAutoCheckUpdateTime(now_time, MyApp.app);
                // 创建地址对象
                URL url = new URL(ConstantValues.UPDATE_URL);
                // 打开链接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 参数
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                if (conn.getResponseCode() == 200) {
                    InputStream input = conn.getInputStream();
                    // 解析 xml pull
                    //创建解PullParser析器
                    XmlPullParser parser = Xml.newPullParser();
                    //设置数据
                    parser.setInput(input, "GBK");
                    int eventType = XmlPullParser.START_DOCUMENT;
                    // 逐行
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:// 标签开始
                                if ("versionCode".equals(parser.getName()//获取标签名
                                )) {
                                    mUpdateInfo.versionCode = parser.nextText();//获取文本值
                                } else if ("versionName".equals(parser.getName())) {
                                    mUpdateInfo.versionName = parser.nextText();
                                } else if ("message".equals(parser.getName())) {
                                    mUpdateInfo.message = parser.nextText();
                                } else if ("url".equals(parser.getName())) {
                                    mUpdateInfo.url = parser.nextText();
                                }
                                break;
                        }
                        // 执行下一行
                        eventType = parser.next();
                    }
                    // 更新
                    String serverCode = mUpdateInfo.versionName;
                    String clientCode = getlocalVersion();
                    int result = CompareVersion.compareVersion(serverCode,clientCode);

                    if (result==1) {
                        Intent i = new Intent("Constants.Action.ACTION_UPDATE");
                        i.putExtra("url", mUpdateInfo.url);
                        i.putExtra("message", mUpdateInfo.message);
                        MyApp.app.sendBroadcast(i);
                    }
                    if(last_check_update_time==-1&&result<1){
                        Intent i = new Intent("Constants.Action.ACTION_UPDATE_NO");
                        i.putExtra("message", mUpdateInfo.message);
                        MyApp.app.sendBroadcast(i);
                    }
                }
            }
            return 0;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 1;
        }
    }

    public String getlocalVersion(){
        String localversion = null;
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            localversion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localversion;
    }

    public void kill() {
        isRun = false;
        main = null;
    }

    public void go() {
        if (null == main || !main.isAlive()) {
            main = new Main();
            main.start();
        }
    }
}
