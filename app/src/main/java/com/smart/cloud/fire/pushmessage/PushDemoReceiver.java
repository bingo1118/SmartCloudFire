package com.smart.cloud.fire.pushmessage;

/**
 * Created by Administrator on 2016/8/9.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.igexin.sdk.PushConsts;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.Alarm.AlarmActivity;
import com.smart.cloud.fire.mvp.Alarm.UserAlarmActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Random;

import fire.cloud.smart.com.smartcloudfire.R;

public class PushDemoReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    private byte[] payload;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                payload = bundle.getByteArray("payload");
                if(payload!=null){
                    String msg = new String(payload);
                    try {
                        JSONObject dataJson = new JSONObject(msg);
                        int deviceType = dataJson.getInt("deviceType");
                        switch (deviceType){
                            case 1://烟感
                            case 2://燃气
                                String message;
                                int alarmType = dataJson.getInt("alarmType");
                                if(deviceType==1){
                                    if(alarmType==202) {
                                        message="发生火灾";
                                    }else{
                                        message="烟感电量低，请更换电池";
                                    }
                                }else{
                                    message="燃气发生泄漏";
                                }
                                PushAlarmMsg mPushAlarmMsg = jsJson(dataJson);
                                Random random1 = new Random();
                                showDownNotification(context,message,mPushAlarmMsg,random1.nextInt(),AlarmActivity.class);
                                Intent intent1 = new Intent(context, AlarmActivity.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent1.putExtra("mPushAlarmMsg",mPushAlarmMsg);
                                intent1.putExtra("alarmMsg",message);
                                context.startActivity(intent1);
                                break;
                            case 5://电气
                                PushAlarmMsg pushAlarmMsg1 = jsJson(dataJson);
                                int alarmFamily = pushAlarmMsg1.getAlarmFamily();
                                String alarmMsg = null;
                                switch (alarmFamily){
                                    case 43://电气报警
                                        int alarmType1 = pushAlarmMsg1.getAlarmType();
                                        if(alarmType1!=0){
                                            alarmMsg = "电气探测器发出：过压报警";
                                        }
                                        break;
                                    case 36:
                                        int alarmType36 = pushAlarmMsg1.getAlarmType();
                                        switch (alarmType36){
                                            case 1:
                                                alarmMsg = "电气探测器发出：漏电流故障报警";
                                                break;
                                            case 2:
                                                alarmMsg = "电气探测器发出：温度故障报警";
                                                break;
                                            case 3:
                                                alarmMsg = "电气探测器发出：供电中断报警";
                                                break;
                                            case 4:
                                                alarmMsg = "电气探测器发出：错相报警";
                                                break;
                                            case 5:
                                                alarmMsg = "电气探测器发出：缺相报警";
                                                break;
                                            case 6:
                                                alarmMsg = "电气探测器发出：电弧故障报警";
                                                break;
                                            case 7:
                                                alarmMsg = "电气探测器发出：负载故障报警";
                                                break;
                                            case 8:
                                                alarmMsg = "电气探测器发出：短路故障报警";
                                                break;
                                            case 9:
                                                alarmMsg = "电气探测器发出：断路故障报警";
                                                break;
                                        }
                                        break;
                                    case 45://电气报警
                                        int alarmType2 = pushAlarmMsg1.getAlarmType();
                                        if(alarmType2!=0){
                                            alarmMsg = "电气探测器发出：过流报警";
                                        }
                                        break;
                                    case 44://欠压报警
                                        int alarmType3 = pushAlarmMsg1.getAlarmType();
                                        if(alarmType3!=0){
                                            alarmMsg = "电气探测器发出：欠压报警";
                                        }
                                        break;
                                    case 46://电气报警
                                        int alarmType4 = pushAlarmMsg1.getAlarmType();
                                        if(alarmType4!=0){
                                            alarmMsg = "电气探测器发出：漏电报警";
                                        }
                                        break;
                                    case 47://电气报警
                                        int alarmType5 = pushAlarmMsg1.getAlarmType();
                                        if(alarmType5!=0){
                                            alarmMsg = "电气探测器发出：温度报警";
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                Random random = new Random();
                                showDownNotification(context,alarmMsg,pushAlarmMsg1,random.nextInt(),AlarmActivity.class);
                                Intent intent2 = new Intent(context, AlarmActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent2.putExtra("mPushAlarmMsg",pushAlarmMsg1);
                                intent2.putExtra("alarmMsg",alarmMsg);
                                context.startActivity(intent2);
                                break;
                            case 6://一键报警和报警回复
                                int alarmType1 = dataJson.getInt("alarmType");
                                if(alarmType1==3){
                                    GetUserAlarm getUserAlarm = new GetUserAlarm();
                                    getUserAlarm.setAddress(dataJson.getString("address"));
                                    getUserAlarm.setAlarmSerialNumber(dataJson.getString("alarmSerialNumber"));
                                    getUserAlarm.setAlarmTime(dataJson.getString("alarmTime"));
                                    getUserAlarm.setAreaName(dataJson.getString("areaName"));
                                    getUserAlarm.setCallerId(dataJson.getString("callerId"));
                                    getUserAlarm.setInfo(dataJson.getString("info"));
                                    getUserAlarm.setLatitude(dataJson.getString("latitude"));
                                    getUserAlarm.setLongitude(dataJson.getString("longitude"));
                                    getUserAlarm.setSmoke(dataJson.getString("smoke"));
                                    getUserAlarm.setCallerName(dataJson.getString("callerName"));
                                    Random random3 = new Random();
                                    showDownNotification(context,"您收到一条紧急报警消息",getUserAlarm,random3.nextInt(),UserAlarmActivity.class);
                                    Intent intent3 = new Intent(context, UserAlarmActivity.class);
                                    intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent3.putExtra("mPushAlarmMsg",getUserAlarm);
                                    context.startActivity(intent3);
                                }else{
                                    DisposeAlarm disposeAlarm = new DisposeAlarm();
                                    disposeAlarm.setAlarmType(alarmType1);
                                    disposeAlarm.setPolice(dataJson.getString("police"));
                                    disposeAlarm.setTime(dataJson.getString("time"));
                                    disposeAlarm.setPoliceName(dataJson.getString("policeName"));
                                    Random random4 = new Random();
                                    showDownNotification(context,disposeAlarm.getPoliceName()+"警员已处理您的消息",null,random4.nextInt(),null);
                                }
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                Intent in = new Intent();
                in.putExtra("cid",cid);
                in.setAction(ConstantValues.Action.GET_GE_TUI_PUSH_CID);
                MyApp.app.sendBroadcast(in);
                break;
            default:
                break;
        }
    }

    private PushAlarmMsg jsJson(JSONObject dataJson) throws JSONException {
        PushAlarmMsg mPushAlarmMsg = new PushAlarmMsg();
        mPushAlarmMsg.setAddress(dataJson.getString("address"));
        mPushAlarmMsg.setAlarmType(dataJson.getInt("alarmType"));
        mPushAlarmMsg.setAreaId(dataJson.getString("areaId"));
        mPushAlarmMsg.setLatitude(dataJson.getString("latitude"));
        mPushAlarmMsg.setLongitude(dataJson.getString("longitude"));
        mPushAlarmMsg.setName(dataJson.getString("name"));
        mPushAlarmMsg.setPlaceAddress(dataJson.getString("placeAddress"));
        mPushAlarmMsg.setIfDealAlarm(dataJson.getInt("ifDealAlarm"));
        mPushAlarmMsg.setPrincipal1(dataJson.getString("principal1"));
        mPushAlarmMsg.setPlaceType(dataJson.getString("placeType"));
        mPushAlarmMsg.setPrincipal1Phone(dataJson.getString("principal1Phone"));
        mPushAlarmMsg.setPrincipal2(dataJson.getString("principal2"));
        mPushAlarmMsg.setPrincipal2Phone(dataJson.getString("principal2Phone"));
        mPushAlarmMsg.setAlarmTime(dataJson.getString("alarmTime"));
        mPushAlarmMsg.setDeviceType(dataJson.getInt("deviceType"));
        mPushAlarmMsg.setAlarmFamily(dataJson.getInt("alarmFamily"));
        try{
            JSONObject jsonObject =  dataJson.getJSONObject("camera");
            if(jsonObject!=null) {
                PushAlarmMsg.CameraBean cameraBean = new PushAlarmMsg.CameraBean();
                cameraBean.setCameraId(jsonObject.getString("cameraId"));
                cameraBean.setCameraPwd(jsonObject.getString("cameraPwd"));
                mPushAlarmMsg.setCamera(cameraBean);
            }
        }catch (Exception e){
        }
        mPushAlarmMsg.setMac(dataJson.getString("mac"));
        return mPushAlarmMsg;
    }



    @SuppressWarnings("deprecation")
    private void showDownNotification(Context context, String message, Serializable mPushAlarmMsg, int id, Class clazz){
        NotificationCompat.Builder m_builder = new NotificationCompat.Builder(context);
        m_builder.setContentTitle(message); // 主标题

        //从系统服务中获得通知管理器
        NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //具体的通知内容

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher); // 将PNG图片转
        m_builder.setLargeIcon(icon);

        m_builder.setSmallIcon(R.mipmap.ic_launcher); //设置小图标
        m_builder.setWhen(System.currentTimeMillis());
        m_builder.setAutoCancel(true);
        if(clazz!=null){
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//设置提示音
            m_builder.setSound(uri);
            m_builder.setContentText("点击查看详情"); //设置主要内容
            //通知消息与Intent关联
            Intent it=new Intent(context,clazz);
            it.putExtra("mPushAlarmMsg",mPushAlarmMsg);
            it.putExtra("alarmMsg",message);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent=PendingIntent.getActivity(context, id, it, PendingIntent.FLAG_CANCEL_CURRENT);
            m_builder.setContentIntent(contentIntent);
        }
        //执行通知
        nm.notify(id, m_builder.build());
    }

}

