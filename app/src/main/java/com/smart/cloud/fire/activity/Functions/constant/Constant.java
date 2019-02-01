package com.smart.cloud.fire.activity.Functions.constant;

import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

public class Constant {


    public static  String APPLY_MORE = "APPLY_MORE";
    public static  String APPLY_MINE = "APPLY_MINE";


    /**
     * 应用id获取应用类型
     * @param id
     * @return
     */
    public static String getType(String id){
        switch (id){
            case "100001":
                return "home";
            case "100002":
                return "baojie";
            case "100003":
                return "ditie";
            case "100004":
                return "gongjiao";
            case "100005":
                return "gongnuan";
            case "100006":
                return "lingyu";
            case "100007":
                return "loupan";
            case "100008":
                return "shangdian";
            case "100009":
                return "xiyiji";
            case "1000010":
                return "xuexiao";
            case "1000011":
                return "yantai";
            case "1000012":
                return "yihuan";
            case "1000013":
                return "weiyu";
        }
        return "else";
    }
}
