package com.smart.cloud.fire.activity.THDevice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.adapter.HostAdapter;
import com.smart.cloud.fire.adapter.ShopSmokeAdapter;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.mvp.LineChart.LineChartActivity;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class OneTHDevInfoActivity extends Activity {

    @Bind(R.id.device_name)
    TextView device_name;
    @Bind(R.id.temperature_text)
    TextView temperature_text;
    @Bind(R.id.t_low)
    TextView t_low;
    @Bind(R.id.t_top)
    TextView t_top;
    @Bind(R.id.humidity_text)
    TextView humidity_text;
    @Bind(R.id.h_low)
    TextView h_low;
    @Bind(R.id.h_top)
    TextView h_top;
    @Bind(R.id.update_time)
    TextView update_time;
    @Bind(R.id.temperature_yuzhi_set)
    RelativeLayout temperature_yuzhi_set;
    @Bind(R.id.humidity_yuzhi_set)
    RelativeLayout humidity_yuzhi_set;
    @Bind(R.id.main_srl)
    SwipeRefreshLayout swipeRefreshLayout;

    Context mContext;
    String mac;
    String devName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_thdev_info);

        ButterKnife.bind(this);
        mContext=this;
        Intent intent=getIntent();
        mac=intent.getStringExtra("Mac");
        devName=intent.getStringExtra("Position");
        device_name.setText(devName);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();
                swipeRefreshLayout.setRefreshing(false);//设置不刷新
            }
        });
        getdata();
    }

    private void getdata() {
        getTHData();
        getYuzhi();
    }

    private void getYuzhi() {
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url= ConstantValues.SERVER_IP_NEW+"getTHAlarmThreshold?mac="+mac;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode=response.getInt("errorCode");
                            if(errorCode==0){
                                t_low.setText((response.getString("value307")==null?"--":response.getString("value307"))+"℃");
                                t_top.setText((response.getString("value308")==null?"--":response.getString("value308"))+"℃");
                                h_low.setText((response.getString("value407")==null?"--":response.getString("value407"))+"%");
                                h_top.setText((response.getString("value408")==null?"--":response.getString("value408"))+"%");
                            }else{
                                T.showShort(mContext,"无数据");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(mContext,"网络错误");
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    public void getTHData(){
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url= ConstantValues.SERVER_IP_NEW+"getOneTHDeviceInfo?mac="+mac;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode=response.getInt("errorCode");
                            if(errorCode==0){
                                temperature_text.setText(response.getString("temperature")+"℃");
                                humidity_text.setText(response.getString("humidity")+"%");
                                if(response.getString("time")!=null){
                                    update_time.setVisibility(View.VISIBLE);
                                    update_time.setText("更新时间:"+response.getString("time"));
                                }
                            }else{
                                T.showShort(mContext,"无数据");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(mContext,"网络错误");
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    @OnClick({R.id.temperature_yuzhi_set,R.id.humidity_yuzhi_set,R.id.temperature_rela,R.id.humidity_rela})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.temperature_yuzhi_set:
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.water_threshold_setting,(ViewGroup) findViewById(R.id.rela));
                final EditText high_value=(EditText)layout.findViewById(R.id.high_value);
                final EditText low_value=(EditText)layout.findViewById(R.id.low_value);
                Button commit=(Button) layout.findViewById(R.id.commit);
                commit.setVisibility(View.GONE);
                TextView title=(TextView)layout.findViewById(R.id.title_text);
                TextView high_value_name=(TextView)layout.findViewById(R.id.high_value_name);
                TextView low_value_name=(TextView)layout.findViewById(R.id.low_value_name);
                title.setText("温度阈值设置");
                high_value_name.setText("高温阈值（℃）:");
                low_value_name.setText("低温阈值（℃）:");
                new AlertDialog.Builder(this).setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url="";
                                try{
                                    float high=Float.parseFloat(high_value.getText().toString());
                                    float low=Float.parseFloat(low_value.getText().toString());
                                    if(low>high){
                                        T.showShort(mContext,"低温阈值不能高于高温阈值");
                                        return;
                                    }
                                    url= ConstantValues.SERVER_IP_NEW+"reSetAlarmNum?mac="+mac+"&threshold207="+low+"&threshold208="+high+"&type=1";
                                }catch(Exception e){
                                    e.printStackTrace();
                                    T.showShort(mContext,"输入数据不完全或有误");
                                    return;
                                }
                                VolleyHelper helper=VolleyHelper.getInstance(mContext);
                                RequestQueue mQueue = helper.getRequestQueue();
//                            RequestQueue mQueue = Volley.newRequestQueue(context);
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    int errorCode=response.getInt("errorCode");
                                                    if(errorCode==0){
                                                        T.showShort(mContext,"设置成功");
                                                        getYuzhi();
                                                    }else{
                                                        T.showShort(mContext,"设置失败");
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                    }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    T.showShort(mContext,"网络错误");
                }
            });
            mQueue.add(jsonObjectRequest);
        }
        }).show();
                break;
            case R.id.humidity_yuzhi_set:
                LayoutInflater inflater2 = getLayoutInflater();
                View layout2 = inflater2.inflate(R.layout.water_threshold_setting,(ViewGroup) findViewById(R.id.rela));
                final EditText high_value2=(EditText)layout2.findViewById(R.id.high_value);
                final EditText low_value2=(EditText)layout2.findViewById(R.id.low_value);
                Button commit1=(Button) layout2.findViewById(R.id.commit);
                commit1.setVisibility(View.GONE);
                TextView title2=(TextView)layout2.findViewById(R.id.title_text);
                TextView high_value_name2=(TextView)layout2.findViewById(R.id.high_value_name);
                TextView low_value_name2=(TextView)layout2.findViewById(R.id.low_value_name);
                title2.setText("湿度阈值设置");
                high_value_name2.setText("高阈值（%）:");
                low_value_name2.setText("低阈值（%）:");
                new AlertDialog.Builder(this).setView(layout2)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url="";
                                try{
                                    float high=Float.parseFloat(high_value2.getText().toString());
                                    float low=Float.parseFloat(low_value2.getText().toString());
                                    if(low>high){
                                        T.showShort(mContext,"低阈值不能高于高阈值");
                                        return;
                                    }
                                    url= ConstantValues.SERVER_IP_NEW+"reSetAlarmNum?mac="+mac+"&threshold207="+low+"&threshold208="+high+"&type=2";
                                }catch(Exception e){
                                    e.printStackTrace();
                                    T.showShort(mContext,"输入数据不完全或有误");
                                    return;
                                }
                                VolleyHelper helper=VolleyHelper.getInstance(mContext);
                                RequestQueue mQueue = helper.getRequestQueue();
//                            RequestQueue mQueue = Volley.newRequestQueue(context);
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    int errorCode=response.getInt("errorCode");
                                                    if(errorCode==0){
                                                        T.showShort(mContext,"设置成功");
                                                        getYuzhi();
                                                    }else{
                                                        T.showShort(mContext,"设置失败");
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        T.showShort(mContext,"网络错误");
                                    }
                                });
                                mQueue.add(jsonObjectRequest);
                            }
                        }).show();
                break;
            case R.id.temperature_rela:
                Intent intent=new Intent(this, LineChartActivity.class);
                intent.putExtra("electricMac",mac);
                intent.putExtra("isWater","tem");
                startActivity(intent);
                break;
            case R.id.humidity_rela:
                Intent intent1=new Intent(this, LineChartActivity.class);
                intent1.putExtra("electricMac",mac);
                intent1.putExtra("isWater","hum");
                startActivity(intent1);
                break;
        }
    }
}
