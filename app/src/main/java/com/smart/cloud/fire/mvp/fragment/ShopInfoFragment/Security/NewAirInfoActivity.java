package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
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

import fire.cloud.smart.com.smartcloudfire.R;

public class NewAirInfoActivity extends FragmentActivity {

    private TextView tv_environmentQuality;
    private TextView tv_methanal;
    private TextView tv_pm25;
    private TextView tv_humidity;
    private TextView tv_temperature;
    private TextView tv_position;
    private TextView tv_time;//@@9.6
    private LinearLayout air_info_linearLayout;
    private LinearLayout btn_methanal,btn_pm25,btn_temperature,btn_humidity;

    private ViewPager viewpager;
    private ViewPagerIndicator indicator;
    private List<String> title_list= Arrays.asList("pm25","mathanal","temperature","humidity");
    private List<VpSimpleFragment> mContent=new ArrayList<VpSimpleFragment>();
    private FragmentPagerAdapter adapter;

    String devMac;
    String devPos;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_air_info);

        Intent intent=getIntent();
        devMac=intent.getStringExtra("Mac");
        devPos=intent.getStringExtra("Position");
        mContext=this;

        init();
        initViews();
        initData();

        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
                indicator.scrollBy(position, arg1);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initData() {
        for(String title:title_list){
            VpSimpleFragment fragment=VpSimpleFragment.newInstance(title,devMac);
            mContent.add(fragment);
        }
        adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mContent.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mContent.get(arg0);
            }
        };
    }

    private void initViews() {
        viewpager=(ViewPager)findViewById(R.id.viewpager);
        indicator=(ViewPagerIndicator)findViewById(R.id.indicator);
        indicator.setmViewPager(viewpager);
        indicator.setItemClickEvent();
    }


    private void init() {
        air_info_linearLayout=(LinearLayout)findViewById(R.id.airinfo_linearLayout);
        tv_environmentQuality=(TextView)findViewById(R.id.tv_info_environmentQuality);
        tv_methanal=(TextView)findViewById(R.id.tv_info_methanal);
        tv_pm25=(TextView)findViewById(R.id.tv_info_pm25);
        tv_humidity=(TextView)findViewById(R.id.tv_info_humidity);
        tv_temperature=(TextView)findViewById(R.id.tv_info_temperature);
        tv_position=(TextView)findViewById(R.id.tv_info_devname);
        tv_time=(TextView)findViewById(R.id.tv_info_time);

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
                                tv_temperature.setText(response.getInt("temperature")+"℃");
                                String quality;
                                switch (response.getInt("priority")) {
                                    case 1:
                                        quality="优";
                                        air_info_linearLayout.setBackground(getResources().getDrawable(R.drawable.bj_you));
                                        break;
                                    case 2:
                                        quality="良";
                                        air_info_linearLayout.setBackground(getResources().getDrawable(R.drawable.bj_liang));
                                        break;
                                    case 3:
                                        quality="中";
                                        air_info_linearLayout.setBackground(getResources().getDrawable(R.drawable.bj_zhong));
                                        tv_environmentQuality.setTextColor(Color.parseColor("#e7963f"));
                                        break;
                                    case 4:
                                        quality="差";
                                        air_info_linearLayout.setBackground(getResources().getDrawable(R.drawable.bj_cha));
                                        tv_environmentQuality.setTextColor(Color.parseColor("#ff0700"));
                                        break;
                                    default:
                                        quality="-";
                                }
                                tv_time.setText("更新时间:"+response.getString("dataTimes"));
                                tv_environmentQuality.setText(quality);
                                switch (response.getInt("priority2")) {
                                    case 1:
                                        tv_pm25.setTextColor(Color.parseColor("#ffffff"));
                                        break;
                                    case 2:
                                        tv_pm25.setTextColor(Color.parseColor("#ffffff"));
                                        break;
                                    case 3:
                                        tv_pm25.setTextColor(Color.parseColor("#e7963f"));
                                        break;
                                    case 4:
                                        tv_pm25.setTextColor(Color.parseColor("#ff0700"));
                                        break;
                                    default:
                                        tv_pm25.setTextColor(Color.parseColor("#ffffff"));
                                        break;
                                }
                                tv_pm25.setText(response.getString("pm25"));
                                switch (response.getInt("priority1")) {
                                    case 1:
                                        tv_methanal.setTextColor(Color.parseColor("#ffffff"));
                                        break;
                                    case 2:
                                        tv_methanal.setTextColor(Color.parseColor("#ffffff"));
                                        break;
                                    case 3:
                                        tv_methanal.setTextColor(Color.parseColor("#e7963f"));
                                        break;
                                    case 4:
                                        tv_methanal.setTextColor(Color.parseColor("#ff0700"));
                                        break;
                                    default:
                                        tv_methanal.setTextColor(Color.parseColor("#ffffff"));
                                        break;
                                }
                                tv_methanal.setText(response.getString("methanal"));
                                tv_humidity.setText(response.getString("humidity"));
                            }else{
                                air_info_linearLayout.setBackground(getResources().getDrawable(R.drawable.bj_you));
                                Toast.makeText(mContext,response.getString("error"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                air_info_linearLayout.setBackground(getResources().getDrawable(R.drawable.bj_you));
                Toast.makeText(mContext,"获取服务器数据失败",Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
        tv_position.setText(devPos);

        btn_methanal=(LinearLayout)findViewById(R.id.btn_methanal);
        btn_pm25=(LinearLayout)findViewById(R.id.btn_pm25);
        btn_temperature=(LinearLayout)findViewById(R.id.btn_temperature);
        btn_humidity=(LinearLayout)findViewById(R.id.btn_humidity);
    }



}

