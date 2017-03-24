package com.smart.cloud.fire.yoosee;

/**
 * Created by Administrator on 2016/8/4.
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.p2p.core.P2PInterface.ISetting;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.utils.SerializableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingListener implements ISetting {
    String TAG = "SDK";
    String TAG1 = "SDKfangqu";
    private static boolean isAlarming = false;
    private static String MonitorDeviceID = "";

    public static void setAlarm(boolean isAlarm) {
        SettingListener.isAlarming = isAlarm;
    }

    public static void setMonitorID(String id) {
        SettingListener.MonitorDeviceID = id;
    }

    /* ***************************************************************
     * 检查密码 开始
     */
    @Override
    public void ACK_vRetCheckDevicePassword(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetCheckDevicePassword:" + result);
        // if(result==ConstantValues.P2P_SET.ACK_RESULT.ACK_INSUFFICIENT_PERMISSIONS){
        // FList.getInstance().setDefenceState(threeNum, state)
        // }
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_CHECK_PASSWORD);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

	/*
	 * 检查密码 结束 ****************************************************************
	 */

    /* ***************************************************************
     * 获取设备各种设置回调 开始
     */
    @Override
    public void ACK_vRetGetNpcSettings(String contactId, int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetGetNpcSettings:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_GET_NPC_SETTINGS);
        MyApp.app.sendBroadcast(i);
    }

	

	/*
	 * 获取设备各种设置回调 结束
	 * ****************************************************************
	 */

	/* ***************************************************************
	 * 时间设置相关回调 开始
	 */

    @Override
    public void vRetGetDeviceTimeResult(String time) {
        // 获取设备时间回调
        Log.e(TAG, "vRetGetDeviceTimeResult:" + time);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_TIME);
        i.putExtra("time", time);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetSetDeviceTimeResult(int result) {
        // 设置设备时间回调
        Log.e(TAG, "vRetSetDeviceTimeResult:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.RET_SET_TIME);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetSetDeviceTime(int msgId, int result) {
        // 设置设备时间ACK回调
        Log.e(TAG, "ACK_vRetSetDeviceTime:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_SET_TIME);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetGetDeviceTime(int msgId, int result) {
        // 获取设备时间ACK回调
        Log.e(TAG, "ACK_vRetGetDeviceTime:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_GET_TIME);
        MyApp.app.sendBroadcast(i);
    }

	/*
	 * 时间设置相关回调 结束
	 * ****************************************************************
	 */

    /* ***************************************************************
     * 设置视频格式相关回调 开始
     */
    @Override
    public void ACK_vRetSetNpcSettingsVideoFormat(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetNpcSettingsVideoFormat:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_SET_VIDEO_FORMAT);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetSetVideoFormatResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetSetVideoFormatResult:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_VIDEO_FORMAT);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetVideoFormatResult(int type) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetVideoFormatResult:" + type);
        Intent format_type = new Intent();
        format_type.setAction(ConstantValues.P2P.RET_GET_VIDEO_FORMAT);
        format_type.putExtra("type", type);
        MyApp.app.sendBroadcast(format_type);
    }

	/*
	 * 设置视频格式相关回调 结束
	 * ****************************************************************
	 */

    /* ***************************************************************
     * 设置设备音量大小相关回调 开始
     */
    @Override
    public void ACK_vRetSetNpcSettingsVideoVolume(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetNpcSettingsVideoVolume:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_VIDEO_VOLUME);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetSetVolumeResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetSetVolumeResult:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_VIDEO_VOLUME);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetVideoVolumeResult(int value) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetVideoVolumeResult:" + value);
        Intent volume = new Intent();
        volume.setAction(ConstantValues.P2P.RET_GET_VIDEO_VOLUME);
        volume.putExtra("value", value);
        MyApp.app.sendBroadcast(volume);
    }

	/*
	 * 设置设备音量大小相关回调 结束
	 * ****************************************************************
	 */

    /* ***************************************************************
     * 修改设备密码相关回调 开始
     */
    @Override
    public void ACK_vRetSetDevicePassword(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetDevicePassword:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_DEVICE_PASSWORD);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetSetDevicePasswordResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetSetDevicePasswordResult:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_DEVICE_PASSWORD);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

	/*
	 * 修改设备密码相关回调 结束
	 * ****************************************************************
	 */

    /*
     * 设置网络类型 开始
     * ****************************************************************
     */
    @Override
    public void ACK_vRetSetNpcSettingsNetType(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetNpcSettingsNetType:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_NET_TYPE);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetSetNetTypeResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetSetNetTypeResult:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_NET_TYPE);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetNetTypeResult(int type) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetNetTypeResult:" + type);
        Intent net_type = new Intent();
        net_type.setAction(ConstantValues.P2P.RET_GET_NET_TYPE);
        net_type.putExtra("type", type);
        MyApp.app.sendBroadcast(net_type);
    }

	/*
	 * 设置网络类型 结束
	 * ****************************************************************
	 */

    /*
     * 设置WIFI 开始
     * ****************************************************************
     */
    @Override
    public void ACK_vRetSetWifi(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetWifi:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_SET_WIFI);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetGetWifiList(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetGetWifiList:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_GET_WIFI);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetWifiResult(int result, int currentId, int count,
                               int[] types, int[] strengths, String[] names) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetWifiResult:" + result + ":" + currentId);
        if (result == 1) {
            Intent i = new Intent();
            i.setAction(ConstantValues.P2P.RET_GET_WIFI);
            i.putExtra("iCurrentId", currentId);
            i.putExtra("iCount", count);
            i.putExtra("iType", types);
            i.putExtra("iStrength", strengths);
            i.putExtra("names", names);
            MyApp.app.sendBroadcast(i);
        } else {
            Intent i = new Intent();
            i.putExtra("result", result);
            i.setAction(ConstantValues.P2P.RET_SET_WIFI);
            MyApp.app.sendBroadcast(i);
        }
    }

	/*
	 * 设置WIFI 结束
	 * ****************************************************************
	 */

	/*
	 * 设置绑定报警ID 开始
	 * ****************************************************************
	 */

    @Override
    public void ACK_vRetSetAlarmBindId(int srcID, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetAlarmBindId:" + result);
        System.out.println("result1="+result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.putExtra("srcID", String.valueOf(srcID));
        i.setAction(ConstantValues.P2P.ACK_RET_SET_BIND_ALARM_ID);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetGetAlarmBindId(int srcID, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetGetAlarmBindId:" + result);
        System.out.println("result2="+result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_GET_BIND_ALARM_ID);
        i.putExtra("srcID", String.valueOf(srcID));
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetBindAlarmIdResult(int srcID, int result, int maxCount,
                                      String[] data) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetBindAlarmIdResult:" + result);
        System.out.println("result3="+result);
        if (result == 1) {
            Intent alarmId = new Intent();
            alarmId.setAction(ConstantValues.P2P.RET_GET_BIND_ALARM_ID);
            alarmId.putExtra("data", data);
            alarmId.putExtra("max_count", maxCount);
            alarmId.putExtra("srcID", String.valueOf(srcID));
            MyApp.app.sendBroadcast(alarmId);
        } else {
            Intent i = new Intent();
            i.putExtra("result", result);
            i.setAction(ConstantValues.P2P.RET_SET_BIND_ALARM_ID);
            i.putExtra("max_count", maxCount);
            i.putExtra("srcID", String.valueOf(srcID));
            MyApp.app.sendBroadcast(i);
        }
    }

	/*
	 * 设置绑定报警ID 结束
	 * ****************************************************************
	 */

    /*
     * 设置报警邮箱 开始
     * ****************************************************************
     */
    @Override
    public void ACK_vRetSetAlarmEmail(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetAlarmEmail:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_SET_ALARM_EMAIL);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetGetAlarmEmail(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetGetAlarmEmail:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_GET_ALARM_EMAIL);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetAlarmEmailResult(int result, String email) {
        // TODO Auto-generated method stub
        Log.e("option", "vRetAlarmEmailResult:" + result + ":" + email);
        byte option = (byte) (result & (1 << 0));
        byte ooo = (byte) ((result << 1) & (0x1));
        Log.e("option", "option:" + option + "ooo-->" + ooo);
        if (option == 1) {
            Intent i = new Intent();
            i.setAction(ConstantValues.P2P.RET_GET_ALARM_EMAIL);
            i.putExtra("email", email);
            i.putExtra("result", result);
            MyApp.app.sendBroadcast(i);
        } else {
            // Intent i = new Intent();
            // i.putExtra("result", result);
            // i.setAction(ConstantValues.P2P.RET_SET_ALARM_EMAIL);
            // MyApp.app.sendBroadcast(i);
        }
    }

	/*
	 * 设置报警邮箱 结束
	 * ****************************************************************
	 */

	/*
	 * 设置移动侦测相关 开始
	 * ****************************************************************
	 */

    @Override
    public void ACK_vRetSetNpcSettingsMotion(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetNpcSettingsMotion:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_SET_MOTION);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetMotionResult(int state) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetMotionResult:" + state);
        Intent motion = new Intent();
        motion.setAction(ConstantValues.P2P.RET_GET_MOTION);
        motion.putExtra("motionState", state);
        MyApp.app.sendBroadcast(motion);
    }

    @Override
    public void vRetSetMotionResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetSetMotionResult:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_MOTION);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

	/*
	 * 设置移动侦测相关 结束
	 * ****************************************************************
	 */

    /*
     * 设置蜂鸣器相关 开始
     * ****************************************************************
     */
    @Override
    public void ACK_vRetSetNpcSettingsBuzzer(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetNpcSettingsBuzzer:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_SET_BUZZER);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetBuzzerResult(int state) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetBuzzerResult:" + state);
        Intent buzzer = new Intent();
        buzzer.setAction(ConstantValues.P2P.RET_GET_BUZZER);
        buzzer.putExtra("buzzerState", state);
        MyApp.app.sendBroadcast(buzzer);
    }

    @Override
    public void vRetSetBuzzerResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetSetBuzzerResult:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_BUZZER);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

	/*
	 * 设置蜂鸣器相关 结束
	 * ****************************************************************
	 */

    /*
     * 设置录像模式相关 开始
     * ****************************************************************
     */
    @Override
    public void ACK_vRetSetNpcSettingsRecordType(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetNpcSettingsRecordType:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_SET_RECORD_TYPE);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetRecordTypeResult(int type) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetRecordTypeResult:" + type);
        Intent record_type = new Intent();
        record_type.setAction(ConstantValues.P2P.RET_GET_RECORD_TYPE);
        record_type.putExtra("type", type);
        MyApp.app.sendBroadcast(record_type);
    }

    @Override
    public void vRetSetRecordTypeResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetSetRecordTypeResult:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_RECORD_TYPE);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

	/*
	 * 设置录像模式相关 结束
	 * ****************************************************************
	 */

	/*
	 * 设置录像时长相关 开始
	 * ****************************************************************
	 */

    @Override
    public void ACK_vRetSetNpcSettingsRecordTime(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetNpcSettingsRecordTime:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_SET_RECORD_TIME);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetRecordTimeResult(int time) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetRecordTimeResult:" + time);
        Intent record_time = new Intent();
        record_time.setAction(ConstantValues.P2P.RET_GET_RECORD_TIME);
        record_time.putExtra("time", time);
        MyApp.app.sendBroadcast(record_time);
    }

    @Override
    public void vRetSetRecordTimeResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetSetRecordTimeResult:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_RECORD_TIME);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

	/*
	 * 设置录像时长相关 结束
	 * ****************************************************************
	 */

    /*
     * 设置录像计划时间 开始
     * ****************************************************************
     */
    @Override
    public void ACK_vRetSetNpcSettingsRecordPlanTime(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetNpcSettingsRecordPlanTime:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_SET_RECORD_PLAN_TIME);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetRecordPlanTimeResult(String time) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetRecordPlanTimeResult:" + time);
        Intent plan_time = new Intent();
        plan_time.setAction(ConstantValues.P2P.RET_GET_RECORD_PLAN_TIME);
        plan_time.putExtra("time", time);
        MyApp.app.sendBroadcast(plan_time);
    }

    @Override
    public void vRetSetRecordPlanTimeResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetSetRecordPlanTimeResult:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_RECORD_PLAN_TIME);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

	/*
	 * 设置录像计划时间 结束
	 * ****************************************************************
	 */

    /*
     * 防区设置相关 开始
     * ****************************************************************
     */
    @Override
    public void ACK_vRetSetDefenceArea(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG1, "ACK_vRetSetDefenceArea:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_SET_DEFENCE_AREA);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetClearDefenceAreaState(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG1, "ACK_vRetClearDefenceAreaState:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_CLEAR_DEFENCE_AREA);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetClearDefenceAreaState(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG1, "vRetClearDefenceAreaState:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.RET_CLEAR_DEFENCE_AREA);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetGetDefenceArea(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG1, "ACK_vRetGetDefenceArea:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_GET_DEFENCE_AREA);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetDefenceAreaResult(int result, ArrayList<int[]> data,
                                      int group, int item) {
        // TODO Auto-generated method stub
        Log.e(TAG1, "vRetDefenceAreaResult:" + result);
        if (result == 1) {
            Intent i = new Intent();
            i.setAction(ConstantValues.P2P.RET_GET_DEFENCE_AREA);
            i.putExtra("data", data);
            MyApp.app.sendBroadcast(i);
        } else {
            Intent i = new Intent();
            i.putExtra("result", result);
            i.setAction(ConstantValues.P2P.RET_SET_DEFENCE_AREA);
            i.putExtra("group", group);
            i.putExtra("item", item);
            MyApp.app.sendBroadcast(i);
        }
    }

	/*
	 * 防区设置相关 结束
	 * ****************************************************************
	 */



    @Override
    public void ACK_vRetSetRemoteRecord(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetRemoteRecord:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.RET_SET_REMOTE_RECORD);
        MyApp.app.sendBroadcast(i);
    }



    @Override
    public void vRetGetRemoteRecordResult(int state) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetRemoteRecordResult:" + state);
        Intent record = new Intent();
        record.setAction(ConstantValues.P2P.RET_GET_REMOTE_RECORD);
        record.putExtra("state", state);
        MyApp.app.sendBroadcast(record);
    }


    @Override
    public void vRetSetRemoteRecordResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetSetRemoteRecordResult:" + result);
        Intent record = new Intent();
        record.setAction(ConstantValues.P2P.RET_SET_REMOTE_RECORD);
        record.putExtra("state", result);
        MyApp.app.sendBroadcast(record);
    }

	/*
	 * 远程设置相关 结束
	 * ****************************************************************
	 */

    /*
     * 设置设备初始密码（仅当设备出厂化未设置密码时可用） 开始
     * ****************************************************************
     */
    @Override
    public void ACK_vRetSetInitPassword(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetInitPassword:" + result);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_INIT_PASSWORD);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetSetInitPasswordResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetSetInitPasswordResult******:" + result);

        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_INIT_PASSWORD);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

	/*
	 * 设置设备初始密码（仅当设备出厂化未设置密码时可用） 结束
	 * ****************************************************************
	 */

    /*
     * 设备检查更新 开始
     * ****************************************************************
     */
    @Override
    public void ACK_vRetGetDeviceVersion(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetGetDeviceVersion:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_GET_DEVICE_INFO);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetDeviceVersion(int result, String cur_version,
                                     int iUbootVersion, int iKernelVersion, int iRootfsVersion) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetDeviceVersion:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.putExtra("cur_version", cur_version);
        i.putExtra("iUbootVersion", iUbootVersion);
        i.putExtra("iKernelVersion", iKernelVersion);
        i.putExtra("iRootfsVersion", iRootfsVersion);
        i.setAction(ConstantValues.P2P.RET_GET_DEVICE_INFO);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetCheckDeviceUpdate(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetCheckDeviceUpdate:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_CHECK_DEVICE_UPDATE);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetDoDeviceUpdate(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetDoDeviceUpdate:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_DO_DEVICE_UPDATE);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetCancelDeviceUpdate(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetCancelDeviceUpdate:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_CANCEL_DEVICE_UPDATE);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetCancelDeviceUpdate(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetCancelDeviceUpdate:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.RET_CHECK_DEVICE_UPDATE);
        MyApp.app.sendBroadcast(i);
    }

	/*
	 * 设备检查更新 结束
	 * ****************************************************************
	 */

    @Override
    public void ACK_vRetGetRecordFileList(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetGetRecordFileList:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.setAction(ConstantValues.P2P.ACK_RET_GET_PLAYBACK_FILES);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetRecordFiles(String[] names) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetRecordFiles:");
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_PLAYBACK_FILES);
        i.putExtra("recordList", names);
        MyApp.app.sendBroadcast(i);
    }



    @Override
    public void ACK_vRetMessage(int msgId, int result) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.Action.RECEIVE_MSG);
        i.putExtra("msgFlag", msgId + "");
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }



    @Override
    public void ACK_vRetCustomCmd(int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e("my", result + "");
    }

    @Override
    public void vRetDeviceNotSupport() {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_DEVICE_NOT_SUPPORT);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetSetImageReverse(int msgId, int result) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_VRET_SET_IMAGEREVERSE);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vRetSetImageReverse(int result) {
    }

    @Override
    public void vRetGetImageReverseResult(int type) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_IMAGE_REVERSE);
        i.putExtra("type", type);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetSetInfraredSwitch(int msgId, int result) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_INFRARED_SWITCH);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vRetGetInfraredSwitch(int state) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_INFRARED_SWITCH);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetSetInfraredSwitch(int result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void ACK_vRetSetWiredAlarmInput(int msgId, int state) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_WIRED_ALARM_INPUT);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void ACK_vRetSetWiredAlarmOut(int msgId, int state) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_WIRED_ALARM_OUT);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetSetAutomaticUpgrade(int msgId, int state) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_AUTOMATIC_UPGRADE);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vRetGetWiredAlarmInput(int state) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_WIRED_ALARM_INPUT);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vRetGetWiredAlarmOut(int state) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_WIRED_ALARM_OUT);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vRetGetAutomaticUpgrade(int state) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_AUTOMATIC_UPGRAD);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vRetSetWiredAlarmInput(int state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetSetWiredAlarmOut(int state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetSetAutomaticUpgrade(int state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void ACK_VRetSetVisitorDevicePassword(int msgId, int state) {
        Log.i("dxssetting", "state-->"+state);
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_VISITOR_DEVICE_PASSWORD);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vRetSetVisitorDevicePassword(int result) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_VISITOR_DEVICE_PASSWORD);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void ACK_vRetSetTimeZone(int msgId, int state) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_TIME_ZONE);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vRetGetTimeZone(int state) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_TIME_ZONE);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vRetSetTimeZone(int result) {

    }

    @Override
    public void vRetGetSdCard(int result1, int result2, int SDcardID, int state) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_SD_CARD_CAPACITY);
        i.putExtra("total_capacity", result1);
        i.putExtra("remain_capacity", result2);
        i.putExtra("SDcardID", SDcardID);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);
        Log.e("sdid", SDcardID + "");
    }

    @Override
    public void ACK_vRetGetSDCard(int msgId, int state) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_GET_SD_CARD_CAPACITY);
        i.putExtra("result", state);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetSdFormat(int msgId, int state) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_GET_SD_CARD_FORMAT);
        i.putExtra("result", state);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetSdFormat(int result) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_SD_CARD_FORMAT);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void VRetGetUsb(int result1, int result2, int SDcardID, int state) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_USB_CAPACITY);
        i.putExtra("total_capacity", result1);
        i.putExtra("remain_capacity", result2);
        i.putExtra("SDcardID", SDcardID);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetSetGPIO(int msgId, int state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void ACK_vRetSetGPIO1_0(int msgId, int state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetGetAudioDeviceType(int type) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_AUDIO_DEVICE_TYPE);
        i.putExtra("type", type);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetSetGPIO(int result) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_GPIO);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetSetPreRecord(int msgId, int state) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_PRE_RECORD);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetPreRecord(int state) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_PRE_RECORD);
        i.putExtra("state", state);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetSetPreRecord(int result) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_PRE_RECORD);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetGetSensorSwitchs(int msgId, int state) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_GET_SENSOR_SWITCH);
        i.putExtra("result", state);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void ACK_vRetSetSensorSwitchs(int msgId, int state) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_RET_SET_SENSOR_SWITCH);
        i.putExtra("result", state);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetGetSensorSwitchs(int result, ArrayList<int[]> data) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_GET_SENSOR_SWITCH);
        i.putExtra("result", result);
        i.putExtra("data", data);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetSetSensorSwitchs(int result) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.RET_SET_SENSOR_SWITCH);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRecvSetLAMPStatus(int result) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.SET_LAMP_STATUS);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vACK_RecvSetLAMPStatus(int result, int value) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.ACK_SET_LAMP_STATUS);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vRecvGetLAMPStatus(int result) {
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.GET_LAMP_STATUS);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void vRetDefenceSwitchStatus(int result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetDefenceSwitchStatusResult(byte[] result) {
        // TODO Auto-generated method stub

    }

    //查看/设置防区通道预置位
    @Override
    public void vRetAlarmPresetMotorPos(byte[] result) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setAction(ConstantValues.P2P.MESG_TYPE_RET_ALARM_TYPE_MOTOR_PRESET_POS);
        i.putExtra("result", result);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetIpConfig(byte[] result) {
        // TODO Auto-generated method stub

    }

    /**
     * 获取访客密码
     */
    @Override
    public void vRetNPCVistorPwd(int pwd) {
        Intent i = new Intent();
        i.putExtra("visitorpwd", pwd);
        i.setAction(ConstantValues.P2P.RET_GET_VISTOR_PASSWORD);
        MyApp.app.sendBroadcast(i);

    }

    @Override
    public void ACK_vRetGetAlarmCenter(int msgId, int state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void ACK_vRetSetAlarmCenter(int msgId, int state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetGetAlarmCenter(int result, int state, String ipdress,
                                   int port, String userId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetSetAlarmCenter(int result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetDeviceNotSupportAlarmCenter() {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetAlarmEmailResultWithSMTP(int result, String email,
                                             int smtpport, byte Entry, String[] SmptMessage, byte reserve) {
        // TODO Auto-generated method stub
        if ((result & (1 << 0)) == 1) {
            Intent i = new Intent();
            i.setAction(ConstantValues.P2P.RET_GET_ALARM_EMAIL_WITHSMTP);
            i.putExtra("contectid", SmptMessage[5]);
            i.putExtra("result", result);
            i.putExtra("email", email);
            i.putExtra("smtpport", smtpport);
            i.putExtra("SmptMessage", SmptMessage);
            MyApp.app.sendBroadcast(i);
        } else {
            Intent i = new Intent();
            i.putExtra("result", result);
            i.setAction(ConstantValues.P2P.RET_SET_ALARM_EMAIL);
            MyApp.app.sendBroadcast(i);
        }
    }

    @Override
    public void vRetCheckDeviceUpdate(String contactId, int result,
                                      String cur_version, String upg_version) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetCheckDeviceUpdate:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.putExtra("cur_version", cur_version);
        i.putExtra("upg_version", upg_version);
        i.setAction(ConstantValues.P2P.RET_CHECK_DEVICE_UPDATE);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetDoDeviceUpdate(String contactId, int result, int value) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetDoDeviceUpdate:" + result);
        Intent i = new Intent();
        i.putExtra("result", result);
        i.putExtra("value", value);
        i.setAction(ConstantValues.P2P.RET_DO_DEVICE_UPDATE);
        MyApp.app.sendBroadcast(i);
    }

    //设置/查看预置位返回结果
    @Override
    public void vRetPresetMotorPos(byte[] result) {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.putExtra("result", Integer.parseInt(result[1]+""));
//		i.setAction(ConstantValues.P2P.RET_PRESET_MOTORPOS_STATUS);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetDeleteDeviceAlarmID(int result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetDeviceLanguege(int result, int languegecount,
                                   int curlanguege, int[] langueges) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetFocusZoom(int result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetGetAllarmImage(int id, String filename, int errorCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void ACK_vRetSetRemoteDefence(String contactId, int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetSetRemoteDefence:" + result);
        if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {

            Intent i = new Intent();
            i.putExtra("state",
                    ConstantValues.DefenceState.DEFENCE_STATE_WARNING_NET);
            i.putExtra("contactId", contactId);
            i.setAction(ConstantValues.P2P.RET_GET_REMOTE_DEFENCE);
            MyApp.app.sendBroadcast(i);

        } else if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {

            Intent i = new Intent();
            i.putExtra("state",
                    ConstantValues.DefenceState.DEFENCE_STATE_WARNING_PWD);
            i.putExtra("contactId", contactId);
            i.setAction(ConstantValues.P2P.RET_GET_REMOTE_DEFENCE);
            MyApp.app.sendBroadcast(i);
        }
    }

    @Override
    public void ACK_vRetGetDefenceStates(String contactId, int msgId, int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "ACK_vRetGetDefenceStates:" + result);
        if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
            Intent i = new Intent();
            i.putExtra("state",
                    ConstantValues.DefenceState.DEFENCE_STATE_WARNING_NET);
            i.putExtra("contactId", contactId);
            i.setAction(ConstantValues.P2P.RET_GET_REMOTE_DEFENCE);
            MyApp.app.sendBroadcast(i);
        } else if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
            Intent i = new Intent();
            i.putExtra("state",
                    ConstantValues.DefenceState.DEFENCE_STATE_WARNING_PWD);
            i.putExtra("contactId", contactId);
            i.setAction(ConstantValues.P2P.RET_GET_REMOTE_DEFENCE);
            MyApp.app.sendBroadcast(i);
        }
    }

    @Override
    public void vRetGetRemoteDefenceResult(String contactId, int state) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetRemoteDefenceResult:" + state);

        Intent defence = new Intent();
        defence.setAction(ConstantValues.P2P.RET_GET_REMOTE_DEFENCE);
        defence.putExtra("state", state);
        defence.putExtra("contactId", contactId);
        MyApp.app.sendBroadcast(defence);
    }

    @Override
    public void vRetGetFriendStatus(int count, String[] contactIds,
                                    int[] status, int[] types) {
        // TODO Auto-generated method stub
        Log.e(TAG, "vRetGetFriendStatus:" + count);
        Map<String,Integer> contactList = new HashMap<String,Integer>();
        for (int i = 0; i < count; i++) {
            contactList.put(contactIds[i], status[i]);
        }
//        Intent friends = new Intent();
//        friends.putExtra("contactList",(Serializable)contactList);//接收   (List<YourObject>) getIntent().getSerializable(key)
//        friends.setAction(ConstantValues.Action.GET_FRIENDS_STATE);
//        MyApp.app.sendBroadcast(friends);

        final SerializableMap myMap=new SerializableMap();
        myMap.setIntMap(contactList);
        Intent intent = new Intent();
        Bundle bundle=new Bundle();
        bundle.putSerializable("contactList", myMap);
        intent.putExtras(bundle);
        intent.setAction(ConstantValues.Action.GET_FRIENDS_STATE);
        MyApp.app.sendBroadcast(intent);
    }

    @Override
    public void vRetMessage(String contactId, String msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetSysMessage(String msg) {
        // TODO Auto-generated method stub

    }


    @Override
    public void ACK_VRetGetNvrIpcList(int msgId, int state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetSetRemoteDefenceResult(String contactId, int result) {
        // TODO Auto-generated method stub
        Intent defence = new Intent();
        defence.setAction(ConstantValues.P2P.RET_SET_REMOTE_DEFENCE);
        defence.putExtra("state", result);
        defence.putExtra("contactId", contactId);
        MyApp.app.sendBroadcast(defence);
    }

    @Override
    public void vRetCustomCmd(int contactId, int len, byte[] cmd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetFishEyeData(int iSrcID, byte[] data, int datasize) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetGetNvrIpcList(String contactId, String[] date, int number) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetSetWifiMode(String id, int result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void vRetAPModeSurpport(String id, int result) {
        // TODO Auto-generated method stub

    }

}

