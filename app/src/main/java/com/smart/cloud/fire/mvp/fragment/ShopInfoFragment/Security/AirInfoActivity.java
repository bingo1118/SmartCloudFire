package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class AirInfoActivity extends Activity {
    @Bind(R.id.position_text)
    TextView textView;
    @Bind(R.id.lin_color)
    LinearLayout linearLayout;
    @Bind(R.id.temperature_text)
    TextView temperature_text;
    @Bind(R.id.quality_text)
    TextView quality_text;
    @Bind(R.id.pm25_text)
    TextView pm25_text;
    @Bind(R.id.formaldehyde_text)
    TextView formaldehyde_text;
    @Bind(R.id.humidity_text)
    TextView humidity_text;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_info);
        ButterKnife.bind(this);
        mContext=this;


        Intent intent=getIntent();
        String devMac=intent.getStringExtra("Mac");
        String devPos=intent.getStringExtra("Position");
        textView.setText(devPos);
        progressBar.setVisibility(View.VISIBLE);
        String url= ConstantValues.SERVER_IP_NEW+"getEnvironmentInfo?userId=&privilege=&page=&airMac="+devMac;
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
//        RequestQueue mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode=response.getInt("errorCode");
                            if(errorCode==0){
                                temperature_text.setText(response.getInt("temperature")+"°");
                                quality_text.setText(getQuality(response.getInt("priority")));
                                pm25_text.setText(response.getString("pm25"));
                                formaldehyde_text.setText(response.getString("methanal"));
                                humidity_text.setText(response.getString("humidity"));
                            }else{
                                Toast.makeText(mContext,response.getString("error"),Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext,"获取服务器数据失败",Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }
    private String getQuality(int priority) {
        String quality="";
        switch (priority){
            case 1:
                quality="优";
                linearLayout.setBackgroundColor(Color.parseColor("#2dac5a"));
                break;
            case 2:
                quality="良";
                linearLayout.setBackgroundColor(Color.parseColor("#4278d0"));
                break;
            case 3:
                quality="中";
                linearLayout.setBackgroundColor(Color.parseColor("#e7863f"));
                break;
            case 4:
                quality="差";
                linearLayout.setBackgroundColor(Color.parseColor("#dd5c3e"));
                break;
        }
        return quality;
    }

}
