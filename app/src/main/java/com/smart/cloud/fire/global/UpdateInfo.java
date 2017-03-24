package com.smart.cloud.fire.global;

/**
 * Created by Administrator on 2016/8/8.
 */
public class UpdateInfo {
    public String versionCode = "";
    public String versionName = "";
    public String message = "";
    public String url = "";

    @Override
    public String toString() {
        return " [versionCode=" + versionCode + ", versionName=" + versionName + ", message=" + message + ", url=" + url + "]";
    }
}
