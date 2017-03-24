package com.smart.cloud.fire.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2016/9/3.
 */
public class TestAuthorityUtil {

    public static boolean testCamera(Context mContext){
        if (!( ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            T.showLong(mContext, "摄像头权限已被禁用，请在权限管理中开启摄像头权限");
            return false;
        }else{
            return true;
        }
    }

    public static boolean testPhone(Context mContext){
        if (!( ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
            T.showLong(mContext, "拨打电话权限已被禁用，请在权限管理中开启拨打电话权限");
            return false;
        }else{
            return true;
        }
    }

    public static boolean testLocation(Context mContext){
        if (!( ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            T.showLong(mContext, "GPS权限已被禁用，请在权限管理中开启GPS权限");
            return false;
        }else{
            return true;
        }
    }

}
