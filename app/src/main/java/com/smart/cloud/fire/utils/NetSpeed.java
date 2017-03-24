package com.smart.cloud.fire.utils;

/**
 * Created by Administrator on 2016/8/4.
 */

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class NetSpeed {
    private final static String TAG = "NetSpeed";
    private long preRxBytes = 0;
    private Timer mTimer = null;
    private Context mContext;
    private static NetSpeed mNetSpeed;
    private Handler mHandler;

    private NetSpeed(Context mContext, Handler mHandler) {
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    public static NetSpeed getInstant(Context mContext, Handler mHandler) {
        if (mNetSpeed == null) {
            mNetSpeed = new NetSpeed(mContext, mHandler);
        }
        return mNetSpeed;
    }

    private long getNetworkRxBytes() {
        int currentUid = getUid();
        Log.d(TAG, "currentUid =" + currentUid);
        if (currentUid < 0) {
            return 0;
        }
        long rxBytes = TrafficStats.getUidRxBytes(currentUid);
		/* 下句中if里的一般都为真，只能得到全部的网速 */
        if (rxBytes == TrafficStats.UNSUPPORTED) {
            Log.d(TAG, "getUidRxBytes fail !!!");/* 本函数可以只用下面一句即可 */
            rxBytes = TrafficStats.getTotalRxBytes();
        }
        return rxBytes;
    }

    public int getNetSpeed() {

        long curRxBytes = getNetworkRxBytes();
        long bytes = curRxBytes - preRxBytes;
        preRxBytes = curRxBytes;
        int kb = (int) Math.floor(bytes / 1024 + 0.5);
        return kb;
    }

    public void startCalculateNetSpeed() {
        preRxBytes = getNetworkRxBytes();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = getNetSpeed();
                    mHandler.sendMessage(msg);
                }
            }, 1000, 1000);
            System.out.println("startCalculateNetSpeed...");
        }

    }

    public void stopCalculateNetSpeed() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if(null!=mHandler){
            mHandler.removeMessages(1);
            mHandler=null;
            mNetSpeed=null;
        }
        System.out.println("stopCalculateNetSpeed...");
    }

    private int getUid() {
        try {
            PackageManager pm = mContext.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(
                    mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            return ai.uid;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
}


