package com.smart.cloud.fire.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    public static String isJson(String scanResult) {
        String mac;
        String devType;
        try {
            mac=new JSONObject(scanResult).getString("i");
            devType=new JSONObject(scanResult).getString("u");
            switch (devType){
                case "CN":
                    mac="N"+mac+"K";
                    break;
                case "YD":
                    mac="N"+mac+"S";
                    break;
            }
        } catch (JSONException ex) {
            return null;
        }
        return mac;
    }
}
