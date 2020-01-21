package com.smart.cloud.fire.mvp.LineChart;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.TemperatureTime;
import com.smart.cloud.fire.ui.BingoSerttingDialog;
import com.smart.cloud.fire.utils.BingoDialog;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;
import com.smart.cloud.fire.view.LochoLineChartView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by Administrator on 2016/11/1.
 */
public class LineChartActivity extends MvpActivity<LineChartPresenter> implements LineChartView {
    /*=========== 控件相关 ==========*/
    @Bind(R.id.lvc_main)
    LochoLineChartView mLineChartView;//线性图表控件
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.btn_next)
    ImageView btnNext;
    @Bind(R.id.btn_before)
    ImageView btnBefore;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.water_threshold)
    TextView water_threshold;//@@2018.01.03水位阈值设置
    @Bind(R.id.btn_new)
    ImageView btnNew;
    @Bind(R.id.yuzhi_line)
    LinearLayout yuzhi_line;
    @Bind(R.id.high_value)
    TextView high_value;
    @Bind(R.id.low_value)
    TextView low_value;
    @Bind(R.id.more)
    TextView more;//@@菜单
    private LineChartPresenter lineChartPresenter;



    private Context context;
    private String userID;
    private int privilege;
    private String electricMac;
    private String electricType;
    private String electricNum;
    private int page = 1;
    private List<TemperatureTime.ElectricBean> electricBeen;
    private boolean haveDataed = true;


    private int isWater=0;
    private int devType;
    String axisYText="";

    String threshold_h;
    String threshold_l;
    String getdatatime;
    String uploaddatatime;

    Window win;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        ButterKnife.bind(this);
        context = this;
        userID = MyApp.getUserID();
        privilege = MyApp.app.getPrivilege();

        electricMac = getIntent().getExtras().getString("electricMac");
        electricType = getIntent().getExtras().getInt("electricType") + "";
        electricNum = getIntent().getExtras().getInt("electricNum") + "";
        isWater=getIntent().getExtras().getInt("isWater",0);//@@12.15
        devType=getIntent().getExtras().getInt("devType");
        electricBeen = new ArrayList<>();

        getData();
        initListener();
    }

    private void getData() {
        switch (isWater){
            case 0:
            case LochoLineChartView.TYPE_ELECTRIC:
                mvpPresenter.getElectricTypeInfo(userID, privilege + "", electricMac, electricType, electricNum, page + "", false,devType);
                switch (electricType) {
                    case "6":
                        axisYText="电压值(V)";
                        titleTv.setText("电压折线图");
                        break;
                    case "7":
                        axisYText="电流值(A)";
                        titleTv.setText("电流折线图");
                        break;
                    case "8":
                        axisYText="漏电流(mA)";
                        titleTv.setText("漏电流线图");
                        break;
                    case "9":
                        axisYText="温度值(℃)";
                        titleTv.setText("温度折线图");
                        break;
                }
                break;
            case LochoLineChartView.TYPE_CHUANAN:
                mvpPresenter.getChuanganInfo(userID, privilege + "", electricMac, electricNum, page + "", false);
                axisYText="燃气值";
                titleTv.setText("历史燃气值折线图");
                break;
            case LochoLineChartView.TYPE_TEM:
                mvpPresenter.getTHDevInfoHistoryInfo(electricMac,page+"", LineChartPresenter.TYPE_TEM,false);
                axisYText="温度值(℃)";
                titleTv.setText("历史温度折线图");
                break;
            case LochoLineChartView.TYPE_HUM:
                mvpPresenter.getTHDevInfoHistoryInfo(electricMac,page+"", LineChartPresenter.TYPE_HUM,false);
                axisYText="湿度值（%）";
                titleTv.setText("历史湿度折线图");
                break;
            case LochoLineChartView.TYPE_GAS:
                mvpPresenter.getGasHistoryInfo(userID, privilege + "",electricMac,page+"",false);
                axisYText="燃气值";
                titleTv.setText("历史燃气值折线图");
                break;
            case LochoLineChartView.TYPE_WATER_PRESURE:
                water_threshold.setVisibility(View.VISIBLE);//@@2018.01.03
                yuzhi_line.setVisibility(View.VISIBLE);
                getYuzhi();
                mvpPresenter.getWaterHistoryInfo(userID, privilege + "", electricMac, page + "", false);
                axisYText="水压值(kPa)";
                titleTv.setText("历史水压值折线图");
                break;
            case LochoLineChartView.TYPE_WATER_PRESURE_WITH_MORE:
                more.setVisibility(View.VISIBLE);
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupMenu(v);
                    }
                });
                yuzhi_line.setVisibility(View.VISIBLE);
                getYuzhi();
                mvpPresenter.getWaterHistoryInfo(userID, privilege + "", electricMac, page + "", false);
                axisYText="水压值(kPa)";
                titleTv.setText("历史水压值折线图");
                break;
            case LochoLineChartView.TYPE_WATER_LEVEL:
                water_threshold.setVisibility(View.VISIBLE);//@@2018.01.03
                yuzhi_line.setVisibility(View.VISIBLE);
                getYuzhi();
                mvpPresenter.getWaterHistoryInfo(userID, privilege + "", electricMac, page + "", false);
                axisYText="水位值(m)";
                titleTv.setText("历史水位值折线图");
                break;
        }
    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_water, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.yuzhi_set:
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.water_threshold_setting,(ViewGroup) findViewById(R.id.rela));
                        BingoDialog dialog=new BingoDialog(mActivity,layout);
                        final EditText high_value=(EditText)layout.findViewById(R.id.high_value);
                        final EditText low_value=(EditText)layout.findViewById(R.id.low_value);
                        Button commit=(Button)layout.findViewById(R.id.commit);
                        commit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url="";
                                try{
                                    float high=Float.parseFloat(high_value.getText().toString());
                                    float low=Float.parseFloat(low_value.getText().toString());
                                    if(low>high){
                                        T.showShort(context,"低阈值不能高于高阈值");
                                        return;
                                    }
                                    url= ConstantValues.SERVER_IP_NEW+"reSetAlarmNum?mac="+electricMac+"&threshold207="+low+"&threshold208="+high;
                                }catch(Exception e){
                                    e.printStackTrace();
                                    T.showShort(context,"输入数据不完全或有误");
                                    return;
                                }
                                VolleyHelper.getInstance(context).getJsonResponse(url,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    T.showShort(context,response.getString("error"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        T.showShort(context,"网络错误");
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                        TextView title=(TextView)layout.findViewById(R.id.title_text);
                        TextView high_value_name=(TextView)layout.findViewById(R.id.high_value_name);
                        TextView low_value_name=(TextView)layout.findViewById(R.id.low_value_name);
                        if(isWater==LochoLineChartView.TYPE_WATER_PRESURE||isWater==LochoLineChartView.TYPE_WATER_PRESURE_WITH_MORE){
                            title.setText("水压阈值设置");
                            high_value_name.setText("高水压阈值（kpa）:");
                            low_value_name.setText("低水压阈值（kpa）:");
                        }else{
                            title.setText("水位阈值设置");
                            high_value_name.setText("高水位阈值（m）:");
                            low_value_name.setText("低水位阈值（m）:");
                        }
                        dialog.show();
                        break;
                    case R.id.bodongyuzhi_set:
                        LayoutInflater inflater1 = getLayoutInflater();
                        View layout1 = inflater1.inflate(R.layout.water_threshold_setting,(ViewGroup) findViewById(R.id.rela));
                        AlertDialog.Builder builder1=new AlertDialog.Builder(context).setView(layout1);
                        final AlertDialog dialog1=builder1.create();
                        final EditText high_value1=(EditText)layout1.findViewById(R.id.high_value);
                        final EditText low_value1=(EditText)layout1.findViewById(R.id.low_value);
                        TextView title1=(TextView)layout1.findViewById(R.id.title_text);
                        TextView high_value_name1=(TextView)layout1.findViewById(R.id.high_value_name);
                        TextView low_value_name1=(TextView)layout1.findViewById(R.id.low_value_name);
                        Button commit1=(Button)layout1.findViewById(R.id.commit);
                        commit1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url="";
                                try{
                                    int high=(int)Float.parseFloat(high_value1.getText().toString());
                                    int low=(int)Float.parseFloat(low_value1.getText().toString());
                                    url= ConstantValues.SERVER_IP_NEW+"set_water_wave_Control?smokeMac="+electricMac+"&waveValue="+high+"&waveTime="+low;
                                }catch(Exception e){
                                    e.printStackTrace();
                                    T.showShort(context,"输入数据不完全或有误");
                                    return;
                                }
                                VolleyHelper.getInstance(context).getJsonResponse(url,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    T.showShort(context,response.getString("error"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        T.showShort(context,"网络错误");
                                    }
                                });
                                dialog1.dismiss();
                            }
                        });
                        title1.setText("波动阈值设置");
                        high_value_name1.setText("波动阈值（kpa）:");
                        low_value_name1.setText("上传时间间隔（min）:");
//                        win = dialog1.getWindow();
//                        win.getDecorView().setPadding(0, 0, 0, 0);
//                        WindowManager.LayoutParams lp1 = win.getAttributes();
//                        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
//                        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                        win.setAttributes(lp1);
                        dialog1.show();
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

    private void initListener() {
        //节点点击事件监听
        mLineChartView.setOnValueTouchListener(new ValueTouchListener());
    }



    private void getYuzhi() {
        String url= ConstantValues.SERVER_IP_NEW+"getWaterAlarmThreshold?mac="+electricMac+"&deviceType="+devType;
        VolleyHelper.getInstance(context).getJsonResponse(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode=response.getInt("errorCode");
                            if(errorCode==0){
                                threshold_h=response.has("value208")?response.getString("value208"):response.getString("thresholdA");
                                threshold_l=response.has("value207")?response.getString("value207"):response.getString("thresholdB");
                                try {
                                    getdatatime=response.getString("ackTimes");
                                    uploaddatatime=response.getString("waveValue");
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(isWater==LochoLineChartView.TYPE_WATER_PRESURE||isWater==LochoLineChartView.TYPE_WATER_PRESURE_WITH_MORE){
                                    low_value.setText("低水压阈值："+threshold_l+"kpa");
                                    high_value.setText("高水压阈值："+threshold_h+"kpa");
                                }else{
                                    low_value.setText("低水位阈值："+threshold_l+"m");
                                    high_value.setText("高水位阈值："+threshold_h+"m");
                                }
                            }else{
                                if(isWater==LochoLineChartView.TYPE_WATER_PRESURE||isWater==LochoLineChartView.TYPE_WATER_PRESURE_WITH_MORE){
                                    low_value.setText("低水压阈值:未设置");
                                    high_value.setText("高水压阈值:未设置");
                                }else{
                                    low_value.setText("低水位阈值:未设置");
                                    high_value.setText("高水位阈值:未设置");
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(context,"网络错误");
            }
        });
    }


    @Override
    public void getDataSuccess(List<TemperatureTime.ElectricBean> temperatureTimes) {
        int len = temperatureTimes.size();
        if (len == 6) {
            btnNext.setClickable(true);
            btnNext.setBackgroundResource(R.drawable.next_selector);
            electricBeen.clear();
            electricBeen.addAll(temperatureTimes);
        } else if (len < 6&&electricBeen.size()>0) {
            btnNext.setClickable(false);
            btnNext.setBackgroundResource(R.mipmap.next_an);
            for (int i = 0; i < len; i++) {
                electricBeen.remove(0);
                TemperatureTime.ElectricBean tElectricBean = temperatureTimes.get(i);
                electricBeen.add(tElectricBean);
            }
        }else if(len>0&&len < 6&&electricBeen.size()==0){
            btnNext.setClickable(false);
            btnNext.setBackgroundResource(R.mipmap.next_an);
            electricBeen.clear();
            electricBeen.addAll(temperatureTimes);
        }else{
            T.showShort(mActivity,"无数据");
        }
        mLineChartView.initChartView(axisYText,electricBeen,electricType,isWater);
    }

    @Override
    public void getDataFail(String msg) {
//        page= page-1;
        btnNext.setClickable(false);
        btnNext.setBackgroundResource(R.mipmap.next_an);
        T.showShort(context, msg);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }


    /**
     * 节点触摸监听
     */
    private class ValueTouchListener implements LineChartOnValueSelectListener {
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            switch (electricType) {
                case "6":
                    Toast.makeText(LineChartActivity.this, "电压值为: " + value.getY() + "V", Toast.LENGTH_SHORT).show();
                    break;
                case "7":
                    Toast.makeText(LineChartActivity.this, "电流值为: " + value.getY() + "A", Toast.LENGTH_SHORT).show();
                    break;
                case "8":
                    Toast.makeText(LineChartActivity.this, "漏电流值为: " + value.getY() + "mA", Toast.LENGTH_SHORT).show();
                    break;
                case "9":
                    Toast.makeText(LineChartActivity.this, "温度值为: " + value.getY() + "℃", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    if(isWater!=0){
                        if(isWater==LochoLineChartView.TYPE_WATER_PRESURE||isWater==LochoLineChartView.TYPE_WATER_PRESURE_WITH_MORE){
                            Toast.makeText(LineChartActivity.this, "水压值为: " + value.getY() + "kPa", Toast.LENGTH_SHORT).show();
                        }else if(isWater==LochoLineChartView.TYPE_TEM){
                            Toast.makeText(LineChartActivity.this, "温度值为: " + value.getY() + "℃", Toast.LENGTH_SHORT).show();
                        }else if(isWater==LochoLineChartView.TYPE_HUM){
                            Toast.makeText(LineChartActivity.this, "湿度值为: " + value.getY() + "%", Toast.LENGTH_SHORT).show();
                        }else if(isWater==LochoLineChartView.TYPE_CHUANAN){
                            Toast.makeText(LineChartActivity.this, "燃气值为: " + value.getY() + "%", Toast.LENGTH_SHORT).show();
                        }else if(isWater==LochoLineChartView.TYPE_GAS){
                            Toast.makeText(LineChartActivity.this, "燃气值为: " + value.getY() , Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LineChartActivity.this, "水位值为: " + value.getY() + "m", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }

        @Override
        public void onValueDeselected() {
        }
    }

    @Override
    protected LineChartPresenter createPresenter() {
        lineChartPresenter = new LineChartPresenter(this);
        return lineChartPresenter;
    }

    @OnClick({R.id.btn_next, R.id.btn_before,R.id.btn_new,R.id.water_threshold})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                page = page + 1;
                if (page == 2) {
                    btnBefore.setClickable(true);
                    btnBefore.setBackgroundResource(R.drawable.before_selector);
                }
                getData();
                break;
            case R.id.btn_before:
                if (page > 1) {
                    page = page - 1;
                    if (page == 1) {
                        btnBefore.setClickable(false);
                        btnBefore.setBackgroundResource(R.mipmap.prve_an);
                    }
                    getData();
                }
                break;
            case R.id.btn_new:
                page = 1;
                btnBefore.setClickable(false);
                btnBefore.setBackgroundResource(R.mipmap.prve_an);
                btnNext.setClickable(true);
                btnNext.setBackgroundResource(R.drawable.next_selector);
                getData();
                break;
            case R.id.water_threshold:
                List<BingoSerttingDialog.SettingItem> itemlist=new ArrayList<>();
                BingoSerttingDialog dialog;
                if(isWater==LochoLineChartView.TYPE_WATER_PRESURE||isWater==LochoLineChartView.TYPE_WATER_PRESURE_WITH_MORE){
                    itemlist.add(new BingoSerttingDialog.SettingItem("高水压阈值(kpa)",threshold_h,"3000-0",3000,0));
                    itemlist.add(new BingoSerttingDialog.SettingItem("低水压阈值(kpa)",threshold_l,"3000-0",3000,0));
                    if(devType==78||devType==47){
                        itemlist.add(new BingoSerttingDialog.SettingItem("上报时间（min）",uploaddatatime,"",0,0));
                    }else if(devType==100){
                        itemlist.add(new BingoSerttingDialog.SettingItem("上报时间（min）",uploaddatatime,"600-0",600,0));
                        itemlist.add(new BingoSerttingDialog.SettingItem("采集时间（min）",getdatatime,"600-0",600,0));

                    }
                    dialog=new BingoSerttingDialog(this,itemlist,"水压阈值设置");
                }else{
                    itemlist.add(new BingoSerttingDialog.SettingItem("高水位阈值(m)",threshold_h,"6-0",6,0));
                    itemlist.add(new BingoSerttingDialog.SettingItem("低水位阈值(m)",threshold_l,"6-0",6,0));
                    if(devType==48||devType==101){
                        itemlist.add(new BingoSerttingDialog.SettingItem("上报时间（min）",uploaddatatime,"600-0",600,0));
                        itemlist.add(new BingoSerttingDialog.SettingItem("采集时间（min）",getdatatime,"600-0",600,0));
                    }
                    dialog=new BingoSerttingDialog(this,itemlist,"水位阈值设置");
                }
                dialog.setmOnCommitListener(new BingoSerttingDialog.OnCommitListener() {
                    @Override
                    public void onCimmit(List<BingoSerttingDialog.SettingItem> items, boolean isTrueData) {
                        String url="";
                        try{
                            float high=Float.parseFloat(items.get(0).getContent());
                            float low=Float.parseFloat(items.get(1).getContent());
                            float uploadtime;
                            float getdatatime = 0;
                            if(low>high){
                                T.showShort(context,"低阈值不能高于高阈值");
                                return;
                            }
                            if(devType==78||devType==85||devType==97||devType==98){
                                uploadtime=Float.parseFloat(items.get(2).getContent().length()>0?items.get(2).getContent().toString():"0");
                                url= ConstantValues.SERVER_IP_NEW+"nanjing_set_water_data?imeiValue="+electricMac+"&deviceType="+devType
                                        +"&hight_set="+high+"&low_set="+low+"&send_time="+uploadtime+"&collect_time="+getdatatime;
                            }else if(devType==47||devType==48){
                                uploadtime=Float.parseFloat(items.get(2).getContent().length()>0?items.get(2).getContent().toString():"0");
                                getdatatime=Float.parseFloat(items.get(3).getContent().length()>0?items.get(3).getContent().toString():"0");
                                if(getdatatime>uploadtime){
                                    T.showShort(context,"采样时间不能高于上报时间");
                                    return;
                                }
                                url= ConstantValues.SERVER_IP_NEW+"set_water_level_Control?smokeMac="+electricMac+"&deviceType="+devType
                                        +"&hvalue="+high+"&lvalue="+low+"&waveValue="+uploadtime+"&waveTime="+getdatatime;
                            }else if(devType==100||devType==101){
                                uploadtime=Float.parseFloat(items.get(2).getContent().length()>0?items.get(2).getContent().toString():"0");
                                getdatatime=Float.parseFloat(items.get(3).getContent().length()>0?items.get(3).getContent().toString():"0");
                                if(getdatatime>uploadtime){
                                    T.showShort(context,"采样时间不能高于上报时间");
                                    return;
                                }
                                url= ConstantValues.SERVER_IP_NEW+"nanjing_set_water_data?imeiValue="+electricMac+"&deviceType="+devType
                                        +"&hight_set="+high+"&low_set="+low+"&send_time="+uploadtime+"&collect_time="+getdatatime+"&lowpow_set=0";
                            }else{
                                url= ConstantValues.SERVER_IP_NEW+"reSetAlarmNum?mac="+electricMac+"&threshold207="+low+"&threshold208="+high;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            T.showShort(context,"输入数据不完全或有误");
                            return;
                        }
                        VolleyHelper.getInstance(context).getJsonResponse(url,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            int errorCode=response.getInt("errorCode");
                                            if(errorCode==0){
                                                getYuzhi();
                                            }
                                            T.showShort(context,response.getString("error"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        T.showShort(context,"网络错误");
                                    }
                                });
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }
    private void testdialog() {
        List<BingoSerttingDialog.SettingItem> itemlist=new ArrayList<>();
        itemlist.add(new BingoSerttingDialog.SettingItem("高水压阈值","100","100-1200",1200,100));
        itemlist.add(new BingoSerttingDialog.SettingItem("低水压阈值","30","1-90",90,1));
        BingoSerttingDialog dialog=new BingoSerttingDialog(this,itemlist,"");
        dialog.setmOnCommitListener(new BingoSerttingDialog.OnCommitListener() {
            @Override
            public void onCimmit(List<BingoSerttingDialog.SettingItem> items, boolean isTrueData) {
                if(isTrueData){
                    T.showShort(mActivity,items.get(0).getContent()+"      "+items.get(1).getContent());
                    dialog.dismiss();
                }else{
                    T.showShort(mActivity,"data error");
                }

            }
        });
        dialog.show();
    }
}
