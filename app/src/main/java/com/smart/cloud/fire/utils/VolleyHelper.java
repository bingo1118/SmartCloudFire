package com.smart.cloud.fire.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Rain on 2018/1/29.
 */
public class VolleyHelper {
    private static RequestQueue mRequestQueue;
    private static VolleyHelper mInstance;
    private Context context;

    private VolleyHelper(Context context) {
        this.context = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyHelper(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context
                    .getApplicationContext());
        }
        return mRequestQueue;
    }
    public void stopRequestQueue() {
        if (mRequestQueue != null) {
            mRequestQueue.stop();
            mRequestQueue=null;
        }
    }

}
