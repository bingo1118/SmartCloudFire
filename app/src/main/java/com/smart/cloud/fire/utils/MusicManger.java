package com.smart.cloud.fire.utils;

/**
 * Created by Administrator on 2016/7/29.
 */

import android.app.Service;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import com.smart.cloud.fire.global.MyApp;

import java.util.HashMap;

import fire.cloud.smart.com.smartcloudfire.R;

public class MusicManger {
    private static MusicManger manager = null;
    private static MediaPlayer player;
    private Vibrator vibrator;
    private MusicManger(){}
    private boolean isVibrate = false;
    public synchronized static MusicManger getInstance(){
        if(null==manager){
            synchronized(MusicManger.class){
                if(null==manager){
                    manager = new MusicManger();
                }
            }
        }
        return manager;
    }

    public void playCommingMusic(){
        if(null!=player){
            return;
        }
        try {
            player = new MediaPlayer();

            int bellType = SharedPreferencesManager.getInstance().getCBellType(MyApp.app);
            HashMap<String,String> data;
            if(bellType== SharedPreferencesManager.TYPE_BELL_SYS){
                int bellId = SharedPreferencesManager.getInstance().getCSystemBellId(MyApp.app);
                data = SystemDataManager.getInstance().findSystemBellById(MyApp.app, bellId);
            }else{
                int bellId = SharedPreferencesManager.getInstance().getCSdBellId(MyApp.app);
                data = SystemDataManager.getInstance().findSdBellById(MyApp.app, bellId);
            }

            String path = data.get("path");
            if(null==path||"".equals(path)){

            }else{
                player.reset();
                player.setDataSource(path);
                player.setLooping(true);
                player.prepare();
                player.start();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            if(null!=player){
                player.stop();
                player.release();
                player = null;
            }
        }
    }


    public void playAlarmMusic(Context context){
        if(null!=player){
            return;
        }
        try {
            System.out.println("player....");
            player = MediaPlayer.create(context, R.raw.alarm);
            player.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if(null!=player){
                player.stop();
                player.release();
                player = null;
            }
        }
    }



    public void stop(){
        if(null!=player){
            player.stop();
            player.release();
            player = null;
        }
    }

    public void Vibrate(){
        if(isVibrate){
            return;
        }
        new Thread(){
            @Override
            public void run(){
                isVibrate = true;
                while(isVibrate){
                    if(null==vibrator){
                        vibrator = (Vibrator) MyApp.app.getSystemService(Service.VIBRATOR_SERVICE);
                    }
                    vibrator.vibrate(400);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void stopVibrate(){
        isVibrate = false;
    }
}

