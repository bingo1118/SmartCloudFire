package com.smart.cloud.fire.activity.GasDevice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.LineChart.LineChartActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class OneGasInfoActivity extends Activity {

    Context mContext;
    @Bind(R.id.device_name)
    TextView device_name;
    @Bind(R.id.update_time)
    TextView update_time;
    @Bind(R.id.gas_text)
    TextView gas_text;
    @Bind(R.id.gas_dw)
    TextView gas_dw;
    @Bind(R.id.temperature_text)
    TextView temperature_text;
    @Bind(R.id.gas_type)
    TextView gas_type;
    @Bind(R.id.temp_rela)
    RelativeLayout temp_rela;
    @Bind(R.id.switch_lin)
    LinearLayout switch_lin;
    @Bind(R.id.state_switch)
    Switch state_switch;
    @Bind(R.id.unit_switch)
    Switch unit_switch;
    @Bind(R.id.update_state)
    TextView update_state;
    @Bind(R.id.device_mac)
    TextView device_mac;

    String smokeMac;
    int devType;
    String smokeName;

    private CompoundButton.OnCheckedChangeListener listener1,listener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_gas_info);

        ButterKnife.bind(this);
        mContext=this;
        smokeName=getIntent().getStringExtra("devName");
        smokeMac=getIntent().getStringExtra("Mac");
        devType=getIntent().getIntExtra("devType",72);
        switch (devType){
            case 72:
                temp_rela.setVisibility(View.VISIBLE);
                break;
            case 73:
                switch_lin.setVisibility(View.VISIBLE);
                break;
            default:
                temp_rela.setVisibility(View.VISIBLE);
                break;
        }
        getdata();
    }

    private void getdata() {
        String userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        int privilege = MyApp.app.getPrivilege();
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url= ConstantValues.SERVER_IP_NEW+"getGasHistoryInfo?userId="+userID+"&privilege="+privilege+"&smokeMac="+smokeMac+"&page=0";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode=response.getInt("errorCode");
                            if(errorCode==0){
                                JSONObject object=response.getJSONArray("history").getJSONObject(0);
                                device_name.setText(smokeName);
                                gas_type.setText(object.getString("proofGasType"));
                                device_mac.setText("ID:"+smokeMac);
                                if(devType==73){
                                    update_state.setVisibility(View.VISIBLE);
                                    switch (object.getString("proofGasTemp")){
                                        case "1":
                                            update_state.setText("状态:报警");
                                            break;
                                        case "2":
                                            update_state.setText("状态:机械手故障");
                                            break;
                                        case "3":
                                            update_state.setText("状态:通讯故障");
                                            break;
                                        case "4":
                                            update_state.setText("状态:主电");
                                            break;
                                        case "5":
                                            update_state.setText("状态:备电");
                                            break;
                                        default:
                                            update_state.setText("状态:主电");
                                            break;
                                    }
                                }
                                temperature_text.setText(object.getString("proofGasTemp")+"℃");

                                if(object.getString("proofGasTime")!=null){
                                    update_time.setVisibility(View.VISIBLE);
                                    update_time.setText("更新时间:"+object.getString("proofGasTime"));
                                }
                                if(devType==73){
                                    gas_text.setText(object.getString("proofGasMmol"));
                                    gas_dw.setText("PPM");
                                    if(object.getString("proofGasState").equals("1")){
                                        state_switch.setChecked(true);
                                    }else{
                                        state_switch.setChecked(false);
                                    }
                                    if(object.getString("proofGasUnit").equals("1")){
                                        unit_switch.setChecked(true);
                                    }else{
                                        unit_switch.setChecked(false);
                                    }
                                    listener1=new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            long lastTime=SharedPreferencesManager.getInstance().getLongData(mContext,"GasControlTime",smokeMac);
                                            if((System.currentTimeMillis()-lastTime)<20000){
                                                T.showShort(mContext,"操作太频繁，请稍后再试");
                                                if(isChecked){
                                                    resetSwitch(16);
                                                }else{
                                                    resetSwitch(17);
                                                }
                                                return;
                                            }
                                            if(isChecked){
                                                sendCom(16);
                                            }else{
                                                sendCom(17);
                                            }
                                            SharedPreferencesManager.getInstance().putLongData(mContext,
                                                    "GasControlTime",smokeMac,
                                                    System.currentTimeMillis());
                                        }
                                    };
                                    state_switch.setOnCheckedChangeListener(listener1);
                                    listener2=new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            long lastTime=SharedPreferencesManager.getInstance().getLongData(mContext,"GasControlTime",smokeMac);
                                            if((System.currentTimeMillis()-lastTime)<20000){
                                                T.showShort(mContext,"操作太频繁，请稍后再试");
                                                if(isChecked){
                                                    resetSwitch(18);
                                                }else{
                                                    resetSwitch(19);
                                                }
                                                return;
                                            }
                                            if(isChecked){
                                                sendCom(18);
                                            }else{
                                                sendCom(19);
                                            }
                                            SharedPreferencesManager.getInstance().putLongData(mContext,
                                                    "GasControlTime",smokeMac,
                                                    System.currentTimeMillis());
                                        }
                                    };
                                    unit_switch.setOnCheckedChangeListener(listener2);
                                }else{
                                    gas_text.setText(object.getString("proofGasMmol"));
                                    gas_dw.setText(object.getString("proofGasUnit"));
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

    private void sendCom(final int i) {
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url= ConstantValues.SERVER_IP_NEW+"nanjing_ranqi_7020?imeiValue="+smokeMac+"&deviceType="+devType+"&state="+i;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode=response.getInt("errorCode");
                            if(errorCode==0){
                                T.showShort(mContext,"设置成功");
                            }else{
                                T.showShort(mContext,"设置失败");
                                resetSwitch(i);
                            }
                        } catch (JSONException e) {
                            T.showShort(mContext,"设置失败");
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

    private void resetSwitch(int i) {
        state_switch.setOnCheckedChangeListener(null);
        unit_switch.setOnCheckedChangeListener(null);
        switch (i){
            case 16:
                state_switch.setChecked(false);
                break;
            case 17:
                state_switch.setChecked(true);
                break;
            case 18:
                unit_switch.setChecked(false);
                break;
            case 19:
                unit_switch.setChecked(true);
                break;
        }
        state_switch.setOnCheckedChangeListener(listener1);
        unit_switch.setOnCheckedChangeListener(listener2);
    }

    @OnClick({R.id.gas_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gas_history:
                Intent intent=new Intent(mContext, LineChartActivity.class);
                intent.putExtra("electricMac",smokeMac);
                intent.putExtra("isWater","gas");
                startActivity(intent);
                break;
        }
    }

}
