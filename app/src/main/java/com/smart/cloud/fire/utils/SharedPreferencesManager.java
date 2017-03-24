package com.smart.cloud.fire.utils;

/**
 * Created by Administrator on 2016/7/29.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.smart.cloud.fire.global.NpcCommon;


public class SharedPreferencesManager {
    public static final String SP_FILE_GWELL = "gwell";
    public static final String KEY_RECENTNAME = "recentName";
    public static final String KEY_RECENTPASS = "recentPass";
    public static final String USER_NAME = "userName";
    public static final String CID= "user_cid";

    public static final String KEY_RECENTNAME_ORDER = "recentName_email";
    public static final String KEY_RECENTPASS_NUMBER = "recentPass_number";

    public static final String KEY_NAMES = "names";
    public static final String KEY_UPDATE_CHECKTIME = "update_checktime";


    public static final String KEY_C_VIBRATE_STATE = "c_vibrate_state";
    public static final String KEY_C_SYS_BELL = "c_system_bell";
    public static final String KEY_C_SD_BELL = "c_sd_bell";
    public static final String KEY_C_BELL_SELECTPOS = "c_selectpos";
    public static final String KEY_C_MUTE_STATE = "c_mute_state";
    public static final String KEY_C_BELL_TYPE = "c_bell_type";
    public static final String KEY_A_VIBRATE_STATE = "a_vibrate_state";
    public static final String KEY_A_SYS_BELL = "a_system_bell";
    public static final String KEY_A_SD_BELL = "a_sd_bell";
    public static final String KEY_A_BELL_SELECTPOS = "a_selectpos";
    public static final String KEY_A_MUTE_STATE = "a_mute_state";
    public static final String KEY_A_BELL_TYPE = "a_bell_type";
    public static final String NOTIFY_VERSION = "notify_version";

    public static final String IS_REMEMBER_PASS = "is_remember_pass";
    public static final String IS_REMEMBER_PASS_EMAIL = "is_remember_pass_email";
    public static final String RECENT_LOGIN_TYPE = "recent_login_type";
    public static final String IGONORE_ALARM_TIME = "ignore_alarm_time";
    public static final String ALARM_TIME_INTERVAL = "alarm_time_interval";
    public static final String IS_AUTO_START = "is_auto_start";
    public static final String IS_EMAIL_SENDSELF = "is_email_sendself";


    public static final int TYPE_BELL_SYS = 0;
    public static final int TYPE_BELL_SD = 1;

    private String FILE_NAME="smart_home";
    private static SharedPreferencesManager manager = null;
    private SharedPreferencesManager(){}
    public static final String LAST_AUTO_CHECK_UPDATE_TIME = "last_auto_check_update_time";
    public static final String IS_SHOW_NOTIFY = "is_show_notify";

    public synchronized static SharedPreferencesManager getInstance(){
        if(null==manager){
            synchronized(SharedPreferencesManager.class){
                if(null==manager){
                    manager = new SharedPreferencesManager();
                }
            }
        }
        return manager;
    }

    public String getData(Context context, String fileName, String key) {
        SharedPreferences sf = context.getSharedPreferences(fileName,
                context.MODE_PRIVATE);
        return sf.getString(key, "");
    }

    public int getCMuteState(Context context) {
        SharedPreferences sf = context.getSharedPreferences(SP_FILE_GWELL,
                context.MODE_PRIVATE);
        return sf.getInt(NpcCommon.mThreeNum + KEY_C_MUTE_STATE, 1);
    }

    public void putData(Context context,String fileName,String key,String value){
        SharedPreferences sf = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public int getCBellType(Context context) {
        SharedPreferences sf = context.getSharedPreferences(SP_FILE_GWELL,
                context.MODE_PRIVATE);
        return sf.getInt(NpcCommon.mThreeNum + KEY_C_BELL_TYPE, TYPE_BELL_SYS);
    }

    public int getCSdBellId(Context context) {
        SharedPreferences sf = context.getSharedPreferences(SP_FILE_GWELL,
                context.MODE_PRIVATE);
        return sf.getInt(NpcCommon.mThreeNum + KEY_C_SD_BELL, -1);
    }

    public int getCVibrateState(Context context) {
        SharedPreferences sf = context.getSharedPreferences(SP_FILE_GWELL,
                context.MODE_PRIVATE);
        return sf.getInt(NpcCommon.mThreeNum + KEY_C_VIBRATE_STATE, 1);
    }

    public int getCSystemBellId(Context context) {
        SharedPreferences sf = context.getSharedPreferences(SP_FILE_GWELL,
                context.MODE_PRIVATE);
        return sf.getInt(NpcCommon.mThreeNum + KEY_C_SYS_BELL, -1);
    }

    public void putRecentLoginType(Context context,int type){
        SharedPreferences sf = context.getSharedPreferences(SP_FILE_GWELL, context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putInt(RECENT_LOGIN_TYPE, type);
        editor.commit();
    }

    public String getData(Context context,String key){
        SharedPreferences sf = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sf.getString(key, "");
    }

    public void putData(Context context,String key,String value){
        SharedPreferences sf = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public long getLastAutoCheckUpdateTime(Context context){
        SharedPreferences sf = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sf.getLong(LAST_AUTO_CHECK_UPDATE_TIME, 0);
    }

    public void putLastAutoCheckUpdateTime(long time,Context context){
        SharedPreferences sf = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putLong(LAST_AUTO_CHECK_UPDATE_TIME, time);
        editor.commit();
    }

    public boolean getIsShowNotify(Context context){
        SharedPreferences sf = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sf.getBoolean(IS_SHOW_NOTIFY,true);
    }
}

