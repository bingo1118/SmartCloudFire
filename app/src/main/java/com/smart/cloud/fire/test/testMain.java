package com.smart.cloud.fire.test;

import com.baidu.mapapi.map.InfoWindow;
import com.smart.cloud.fire.test.exception.TestException;

import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class testMain {

//    static String url="http://139.159.220.138:51091/fireSystem/nanjing_zhongdian350?imeiValue=865352031960668&Overvoltage=300&Undervoltage=80&Overcurrent=700.0&Leakage_current=1000&deviceType=83&Temperature1=100&Temperature2=100&Temperature3=100&Temperature4=100";

    static String url="http://127.0.0.1:9960/fireSystem/nanjing_zhongdian350?imeiValue=865352031960668";


    public static void main(String[] args){
        try {
            InputStreamReader inputStreamReader;
            testFunction();
        } catch (TestException e) {
            e.printStackTrace();
        }
    }

    private static void testFunction() throws TestException {
        throw new TestException();
    }

}
