package com.smart.cloud.fire.activity.ChuangAnWifiSet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Rain on 2018/5/14.
 */
public class SocketUDP {
    static DatagramPacket client;

    static BufferedReader in;

    private static SocketUDP socketClient;

    private static final String TAG = "SocketClient";

    private InetAddress site;

    private int port;

    private boolean onGoinglistner = true;

    private  DatagramSocket socket;

    private Context context;

    public static synchronized SocketUDP newInstance(InetAddress site, int port) {

        if (socketClient == null) {
            socketClient = new SocketUDP(site, port);
            Log.i("newInstance", "newInstance");
        }
        return socketClient;
    }

    private SocketUDP(InetAddress site, int port) {
        this.site = site;
        this.port = port;
    }

    public String sendMsg(final byte[] msg) {
        Log.i(TAG, "into sendMsgsendMsg(final ChatMessage msg)  msg =" + msg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket == null) {
                        socket = new DatagramSocket(port);
                    }
                    client = new DatagramPacket(msg, msg.length, site, port);
                    socket.send(client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return "";
    }

    private void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void acceptMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (onGoinglistner) {
                    try {
                        if (socket == null) {
                            socket = new DatagramSocket(port);
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        byte[] data = new byte[64*1024];
                        //参数一:要接受的data 参数二：data的长度
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        data=null;
                        socket.receive(packet);
                        Log.i("cmd1", "..........");
                        //把接收到的data转换为String字符串
                        byte[] pk = packet.getData();
                        packet=null;
                        int num;
                        if((pk[2]&0xff)==9){
                            num = 15;
                        }else{
                            num = 1024;
                        }

                        byte[] result = new byte[num];
                        for(int i=0;i<num;i++){
                            result[i] = pk[i];
                        }
                        if (result != null && !result.equals("")) {
                            int cmd = result[2]&0xff;
                            cmd2(cmd,result);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void clearClient() {
        closeConnection();
    }

    public void startAcceptMessage() {
//		onGoinglistner = true;
//		this.context = mContext;
//		if(null!=context){
        acceptMsg();
//		}

    }

    private void cmd2(int cmd,byte[] result){
        switch (cmd) {
            case 9://配置设备状态回复包
                Intent unOpenOrCloseOrderPack = new Intent();
                unOpenOrCloseOrderPack.putExtra("datasByte", result);
                unOpenOrCloseOrderPack.setAction("Constants.Action.WiFiSetAck");
                MyApp.app.sendBroadcast(unOpenOrCloseOrderPack);
                break;
//            case 51://配置设备状态回复包
//                Intent unOpenOrCloseOrderPack1 = new Intent();
//                unOpenOrCloseOrderPack1.putExtra("datasByte", result);
//                unOpenOrCloseOrderPack1.setAction("Constants.Action.WiFiSetAck");
//                MyApp.app.sendBroadcast(unOpenOrCloseOrderPack1);
//                break;
            default:
                break;
        }
    }
}
