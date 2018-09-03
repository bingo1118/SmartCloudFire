package com.smart.cloud.fire.mvp.electric;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.adapter.ElectricActivityAdapterTest;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.ElectricValue;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.LineChart.LineChartActivity;
import com.smart.cloud.fire.mvp.electricChangeHistory.ElectricChangeHistoryActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ElectricActivity extends MvpActivity<ElectricPresenter> implements ElectricView {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_fresh_layout)
    SwipeRefreshLayout swipeFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.power_change_history_text)
    TextView power_change_history_text;//@@8.28电源切换记录
    @Bind(R.id.more)
    TextView more;//@@菜单
    private ElectricPresenter electricPresenter;
    private ElectricActivityAdapterTest electricActivityAdapter;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;
    private String electricMac;
    private int devType;//@@2018.05.15设备类型
    private String repeatMac;
    private String userID;
    private int privilege;

    private String yuzhi43="";
    private String yuzhi44="";
    private String yuzhi45="";
    private String yuzhi46="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric);
        mContext=this;
        Intent intent=getIntent();
        electricMac = getIntent().getExtras().getString("ElectricMac");
        devType = getIntent().getExtras().getInt("devType");
        repeatMac = getIntent().getExtras().getString("repeatMac");
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
//        power_change_history_text.setVisibility(View.VISIBLE);//@@8.28
        more.setVisibility(View.VISIBLE);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        registerForContextMenu(more);
        ButterKnife.bind(this);
        refreshListView();
        electricPresenter.getOneElectricInfo(userID,privilege+"",electricMac,false);
        getYuzhi(electricMac);
    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_electr, popupMenu.getMenu());
        // menu的item点击事件
        if(devType!=52&&devType!=53&&devType!=75){
            MenuItem item=popupMenu.getMenu().findItem(R.id.yuzhi_set);
            item.setVisible(false);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.change_history:
                        Intent intent=new Intent(mContext, ElectricChangeHistoryActivity.class);
                        intent.putExtra("mac",electricMac);
                        startActivity(intent);
                        break;
                    case R.id.yuzhi_set:
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.electr_threshold_setting,(ViewGroup) findViewById(R.id.rela));
                        final AlertDialog.Builder builder=new AlertDialog.Builder(mContext).setView(layout);
                        final AlertDialog dialog =builder.create();
                        final EditText high_value=(EditText)layout.findViewById(R.id.high_value);
                        high_value.setText(yuzhi43);
                        final EditText low_value=(EditText)layout.findViewById(R.id.low_value);
                        low_value.setText(yuzhi44);
                        final EditText overcurrentvalue=(EditText)layout.findViewById(R.id.overcurrentvalue);
                        overcurrentvalue.setText(yuzhi45);
                        final EditText Leakage_value=(EditText)layout.findViewById(R.id.Leakage_value);
                        Leakage_value.setText(yuzhi46);
                        Button commit=(Button)(Button)layout.findViewById(R.id.commit);
                        commit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url="";
                                try{
                                    int high=(int)Float.parseFloat(high_value.getText().toString());
                                    int low=(int)Float.parseFloat(low_value.getText().toString());
                                    float value45=Float.parseFloat(overcurrentvalue.getText().toString());
                                    int value46=(int)Float.parseFloat(Leakage_value.getText().toString());
                                    if(low<145||low>220){
                                        T.showShort(mContext,"欠压阈值设置范围为145-220V");
                                        return;
                                    }
                                    if(high<220||high>280){
                                        T.showShort(mContext,"过压阈值设置范围为220-280V");
                                        return;
                                    }
                                    if(value45<1||value45>63){
                                        T.showShort(mContext,"过流阈值设置范围为1-63A");
                                        return;
                                    }
                                    if(value46<10||value46>90){
                                        T.showShort(mContext,"漏电流阈值设置范围为10-90mA");
                                        return;
                                    }
                                    if(low>high){
                                        T.showShort(mContext,"欠压阈值不能高于过压阈值");
                                        return;
                                    }
                                    if(devType==52){
                                        url= ConstantValues.SERVER_IP_NEW+"ackControlCvls?Overvoltage="+high_value.getText().toString()
                                                +"&Undervoltage="+low_value.getText().toString()
                                                +"&Overcurrent="+value45
                                                +"&Leakage="+value46
                                                +"&repeaterMac="+repeatMac+"&smokeMac="+electricMac+"&userId="+userID;
                                    }else if(devType==53){
                                        url= ConstantValues.SERVER_IP_NEW+"EasyIot_Uool_control?Overvoltage="+high_value.getText().toString()
                                                +"&Undervoltage="+low_value.getText().toString()
                                                +"&Overcurrent="+value45
                                                +"&Leakage="+value46
                                                +"&appId=1&devSerial="+electricMac+"&userId="+userID;
                                    }else if(devType==75){
                                        url= ConstantValues.SERVER_IP_NEW+"Telegraphy_Uool_control?Overvoltage="+high_value.getText().toString()
                                                +"&Undervoltage="+low_value.getText().toString()
                                                +"&Overcurrent="+value45
                                                +"&Leakage="+value46
                                                +"&deviceType=75&devCmd=14&imei="+electricMac;
                                    }else{
                                        Toast.makeText(getApplicationContext(),"该设备不支持阈值设置", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
//                                            Toast.makeText(getApplicationContext(),"设置中，请稍后", Toast.LENGTH_SHORT).show();
                                }catch(Exception e){
                                    e.printStackTrace();
                                    T.showShort(mContext,"输入数据不完全或有误");
                                    return;
                                }
                                final ProgressDialog dialog1 = new ProgressDialog(mContext);
                                dialog1.setTitle("提示");
                                dialog1.setMessage("设置中，请稍候");
                                dialog1.setCanceledOnTouchOutside(false);
                                dialog1.show();
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
                                                        electricPresenter.getOneElectricInfo(userID,privilege+"",electricMac,false);
                                                    }else{
                                                        T.showShort(mContext,"设置失败");
                                                    }
                                                    getYuzhi(electricMac);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                dialog1.dismiss();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        T.showShort(mContext,"网络错误");
                                        dialog1.dismiss();
                                    }
                                });
                                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                mQueue.add(jsonObjectRequest);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });

        popupMenu.show();
    }


    private void refreshListView() {
        //设置刷新时动画的颜色，可以设置4个
        swipeFreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeFreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                electricPresenter.getOneElectricInfo(userID,privilege+"",electricMac,true);
                getYuzhi(electricMac);
            }
        });
    }

    @Override
    protected ElectricPresenter createPresenter() {
        electricPresenter = new ElectricPresenter(this);
        return electricPresenter;
    }

    @Override
    public void getDataSuccess(List<ElectricValue.ElectricValueBean> smokeList) {
        if(smokeList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
        }//@@7.7
        electricActivityAdapter = new ElectricActivityAdapterTest(mContext, smokeList, electricPresenter);
        recyclerView.setAdapter(electricActivityAdapter);
        swipeFreshLayout.setRefreshing(false);
        electricActivityAdapter.setOnItemClickListener(new ElectricActivityAdapterTest.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view, ElectricValue.ElectricValueBean data){
                Intent intent = new Intent(mContext, LineChartActivity.class);
                intent.putExtra("electricMac",electricMac);
                intent.putExtra("electricType",data.getElectricType());
                intent.putExtra("electricNum",data.getId());
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.power_change_history_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.power_change_history_text:
                Intent intent=new Intent(mContext, ElectricChangeHistoryActivity.class);
                intent.putExtra("mac",electricMac);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void getDataFail(String msg) {
        swipeFreshLayout.setRefreshing(false);
        T.showShort(mContext,msg);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void getYuzhi(String mac){
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        String url=ConstantValues.SERVER_IP_NEW+"getElectrAlarmThreshold?mac="+mac;
        RequestQueue mQueue = helper.getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode=response.getInt("errorCode");
                            if(errorCode==0){
                                yuzhi43=response.getString("value43");
                                yuzhi44=response.getString("value44");
                                yuzhi45=response.getString("value45");
                                yuzhi46=response.getString("value46");
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(jsonObjectRequest);
    }

}



