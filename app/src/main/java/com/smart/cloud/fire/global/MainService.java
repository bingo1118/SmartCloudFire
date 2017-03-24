package com.smart.cloud.fire.global;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.p2p.core.P2PHandler;
import com.smart.cloud.fire.yoosee.P2PConnect;

public class MainService extends Service {
    Context context;
    private MainThread mMainThread;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        mMainThread = new MainThread(context);
        mMainThread.go();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        Account account = AccountPersist.getInstance().getActiveAccountInfo(this);
        try{
            int codeStr1 = (int) Long.parseLong(account.rCode1);
            int codeStr2 = (int) Long.parseLong(account.rCode2);
            if(account!=null){
                boolean result = P2PHandler.getInstance().p2pConnect(account.three_number, codeStr1, codeStr2);
                if(result){
                    new P2PConnect(getApplicationContext());
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        System.out.println("onDestroy___");
        P2PHandler.getInstance().p2pDisconnect();
        Intent ii = new Intent(this,MainService.class);
        this.startService(ii);
        mMainThread.kill();
        super.onDestroy();
    }
}
