package com.smart.cloud.fire.Volley;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;


/**
 * Created by Rain on 2018/12/27.
 */
public class FastJsonRequest<T> extends Request<T> {

    private final Listener<T> mListener;
    private final Map<String, String> mParams;
    private Map<String, String> mHeaders;
    private Class<T> mClass;

    public FastJsonRequest(int method, String url, Map<String, String> params,
                           Map<String, String> headers, Class<T> mClass, Listener<T> listener,
                           ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mParams = params;
        mHeaders = headers;
        this.mClass = mClass;
    }

    public FastJsonRequest(String url, Map<String,String> params,
                           Map<String,String> headers, Class<T> mClass, Listener<T> listener,
                           ErrorListener errorListener) {
        this(null == params ? Method.GET : Method.POST, url, params, headers,
                mClass, listener, errorListener);
    }

    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public Map<String,String> getHeaders() throws AuthFailureError {
        if (null == mHeaders) {
            mHeaders = Collections.emptyMap();
        }
        return mHeaders;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Gson gson = new Gson();

            JsonObject j=new JsonObject();
            return Response.success(gson.fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
