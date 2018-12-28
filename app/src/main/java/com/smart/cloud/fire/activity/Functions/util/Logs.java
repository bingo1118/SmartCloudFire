package com.smart.cloud.fire.activity.Functions.util;

import android.util.Log;

public class Logs {
    public static void I(String title, String str) {
        Log.i("eee", title + " --- " + str);
    }

    public static void I(String str) {
        Log.i("eee", str);
    }
}
