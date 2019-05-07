package com.smart.cloud.fire.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.activity.GasDevice.OneGasInfoActivity;
import com.smart.cloud.fire.activity.NFCDev.NFCImageShowActivity;
import com.smart.cloud.fire.activity.THDevice.OneTHDevInfoActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.mvp.ChuangAn.ChuangAnActivity;
import com.smart.cloud.fire.mvp.LineChart.LineChartActivity;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security.NewAirInfoActivity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment.WiredSmokeListActivity;
import com.smart.cloud.fire.retrofit.ApiStores;
import com.smart.cloud.fire.retrofit.AppClient;
import com.smart.cloud.fire.ui.CallManagerDialogActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Rain on 2019/2/15.
 */
public class BigDataDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnLongClickListener, View.OnClickListener{

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Smoke> listNormalSmoke;


    public interface OnLongClickListener {
        void onLongClick(View view, int position);
    }
    public interface OnClickListener{
        void onClick(View view, int position);
    }

    private OnLongClickListener mOnLongClickListener = null;
    private OnClickListener mOnClickListener=null;

    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    public void setOnClickListener(OnClickListener listener){
        mOnClickListener=listener;
    }

    @Override
    public boolean onLongClick(View view) {
        if (null != mOnLongClickListener) {
            mOnLongClickListener.onLongClick(view, (int) view.getTag());
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(null!=mOnClickListener){
            mOnClickListener.onClick(v,(int)v.getTag());
        }
    }

    public BigDataDetailAdapter(Context mContext, List<Smoke> listNormalSmoke) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.listNormalSmoke = listNormalSmoke;
        this.mContext = mContext;
    }
    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            final View view = mInflater.inflate(R.layout.detail_info_adapter, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            view.setOnLongClickListener(this);//@@
            return viewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.recycler_load_more_layout, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final Smoke normalSmoke = listNormalSmoke.get(position);
            int devType = normalSmoke.getDeviceType();
            int netStates = normalSmoke.getNetState();
            ((ItemViewHolder) holder).right_into_image.setVisibility(View.VISIBLE);//@@9.14
            if(normalSmoke.getRssivalue()==null||normalSmoke.getRssivalue().equals("0")){
                ((ItemViewHolder) holder).rssi_value.setVisibility(View.GONE);
                ((ItemViewHolder) holder).rssi_image.setVisibility(View.GONE);
            }else{
                ((ItemViewHolder) holder).rssi_value.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).rssi_image.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).rssi_value.setText(normalSmoke.getRssivalue());
            }
            ((ItemViewHolder) holder).voltage_image.setVisibility(View.VISIBLE);
            int voltage=normalSmoke.getLowVoltage();
            if(voltage==0){
                ((ItemViewHolder) holder).voltage_image.setVisibility(View.GONE);
            }else if(voltage>0&&voltage<10){
                ((ItemViewHolder) holder).voltage_image.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).voltage_image.setImageResource(R.drawable.p0);
            }else if(voltage>=10&&voltage<30){
                ((ItemViewHolder) holder).voltage_image.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).voltage_image.setImageResource(R.drawable.p1);
            }else if(voltage>=30&&voltage<60){
                ((ItemViewHolder) holder).voltage_image.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).voltage_image.setImageResource(R.drawable.p2);
            }else if(voltage>=60&&voltage<80){
                ((ItemViewHolder) holder).voltage_image.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).voltage_image.setImageResource(R.drawable.p3);
            }else if(voltage>=80){
                ((ItemViewHolder) holder).voltage_image.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).voltage_image.setImageResource(R.drawable.p4);
            }
            ((ItemViewHolder) holder).dev_hearttime_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder customizeDialog =
                            new AlertDialog.Builder(mContext);
                    final View dialogView = LayoutInflater.from(mContext)
                            .inflate(R.layout.dev_heart_time_setting,null);
                    customizeDialog.setView(dialogView);
                    final EditText heartTime_edit=(EditText) dialogView.findViewById(R.id.hearttime_value);
                    Button commit_btn=(Button)dialogView.findViewById(R.id.commit);
                    commit_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(heartTime_edit.getText().length()>0){
                                T.showShort(mContext,"设置成功");
                            }else{
                                T.showShort(mContext,"输入不合法");
                            }
                        }
                    });
                    customizeDialog.show();
                }
            });
            if(devType==18){
                ((ItemViewHolder) holder).state_name_tv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).state_tv.setVisibility(View.VISIBLE);
                if(normalSmoke.getElectrState()==1){
                    ((ItemViewHolder) holder).state_tv.setText("开");
                }else{
                    ((ItemViewHolder) holder).state_tv.setText("关");
                }
            }else{
                ((ItemViewHolder) holder).state_name_tv.setVisibility(View.GONE);
                ((ItemViewHolder) holder).state_tv.setVisibility(View.GONE);
            }//@@11.01
            ((ItemViewHolder) holder).power_button.setVisibility(View.GONE);
            ((ItemViewHolder) holder).category_group_lin.setOnClickListener(null);
            ((ItemViewHolder) holder).dev_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path= ConstantValues.NFC_IMAGES+"devimages/"+normalSmoke.getMac()+".jpg";
                    Intent intent=new Intent(mContext, NFCImageShowActivity.class);
                    intent.putExtra("path",path);
                    mContext.startActivity(intent);
                }
            });
            switch (devType){
                case 61://@@嘉德南京烟感
                case 58://@@嘉德移动烟感
                case 41://@@NB烟感
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("烟感："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("烟感："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).power_button.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).power_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final ProgressDialog dialog1 = new ProgressDialog(mContext);
                            dialog1.setTitle("提示");
                            dialog1.setMessage("设置中，请稍候");
                            dialog1.setCanceledOnTouchOutside(false);
                            dialog1.show();
                            String userid= SharedPreferencesManager.getInstance().getData(mContext,
                                    SharedPreferencesManager.SP_FILE_GWELL,
                                    SharedPreferencesManager.KEY_RECENTNAME);
                            ApiStores apiStores1 = AppClient.retrofit(ConstantValues.SERVER_IP_NEW).create(ApiStores.class);
                            Call<HttpError> call=null;
                            switch (normalSmoke.getDeviceType()){
                                case 41:
                                    call=apiStores1.NB_IOT_Control(userid,normalSmoke.getMac(),"1");
                                    break;
                                case 58:
                                    call=apiStores1.nanjing_jiade_cancel(normalSmoke.getMac(),"58","0");
                                    break;
                                case 61:
                                    call=apiStores1.nanjing_jiade_cancel(normalSmoke.getMac(),"61","0");
                                    break;
                            }
                            if (call != null) {
                                call.enqueue(new Callback<HttpError>() {
                                    @Override
                                    public void onResponse(Call<HttpError> call, retrofit2.Response<HttpError> response) {
                                        T.showShort(mContext,response.body().getError()+"");
                                        dialog1.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<HttpError> call, Throwable t) {
                                        T.showShort(mContext,"失败");
                                        dialog1.dismiss();
                                    }
                                });
                            }
                        }
                    });
                    if(normalSmoke.getElectrState()==1){
                        ((ItemViewHolder) holder).power_button.setText("已消音");
                        ((ItemViewHolder) holder).power_button.setEnabled(false);
                    }else{
                        ((ItemViewHolder) holder).power_button.setText("消音");
                    }
                    break;
                case 56://@@NBIot烟感
                    ((ItemViewHolder) holder).power_button.setVisibility(View.VISIBLE);
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("烟感："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                        ((ItemViewHolder) holder).power_button.setBackgroundColor(Color.GRAY);
                        ((ItemViewHolder) holder).power_button.setClickable(false);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("烟感："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                        ((ItemViewHolder) holder).power_button.setBackgroundColor(Color.RED);
                        ((ItemViewHolder) holder).power_button.setClickable(true);
                        ((ItemViewHolder) holder).power_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final ProgressDialog dialog1 = new ProgressDialog(mContext);
                                dialog1.setTitle("提示");
                                dialog1.setMessage("设置中，请稍候");
                                dialog1.setCanceledOnTouchOutside(false);
                                dialog1.show();
                                String userid= SharedPreferencesManager.getInstance().getData(mContext,
                                        SharedPreferencesManager.SP_FILE_GWELL,
                                        SharedPreferencesManager.KEY_RECENTNAME);
                                ApiStores apiStores1 = AppClient.retrofit(ConstantValues.SERVER_IP_NEW).create(ApiStores.class);
                                Call<HttpError> call=apiStores1.EasyIot_erasure_control(userid,normalSmoke.getMac(),"1");
                                if (call != null) {
                                    call.enqueue(new Callback<HttpError>() {
                                        @Override
                                        public void onResponse(Call<HttpError> call, retrofit2.Response<HttpError> response) {
                                            T.showShort(mContext,response.body().getError()+"");
                                            dialog1.dismiss();
                                        }

                                        @Override
                                        public void onFailure(Call<HttpError> call, Throwable t) {
                                            T.showShort(mContext,"失败");
                                            dialog1.dismiss();
                                        }
                                    });
                                }
                            }
                        });
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 57://@@
                case 55://@@嘉德烟感
                case 31://@@12.26 三江iot烟感
                case 21://@@12.01 Lora烟感
                case 1://烟感。。
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("烟感："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("烟感："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 73://南京7020燃气
                case 72://防爆燃气
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("燃气探测器："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("燃气探测器："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).category_group_lin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, OneGasInfoActivity.class);
                            intent.putExtra("Mac",normalSmoke.getMac());
                            intent.putExtra("devType",normalSmoke.getDeviceType());
                            intent.putExtra("devName",normalSmoke.getName());
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 16://@@9.29
                case 2://燃气。。
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("燃气探测器："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("燃气探测器："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 22:
                case 23:
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("燃气探测器："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("燃气探测器："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 45://海曼气感
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("HM气感："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("HM气感："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 53://NB电气
                case 52://@@Lara电气设备
                case 5://电气。。
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("电气设备："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("电气设备："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    break;
                case 36:
                case 35://NB电弧
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("NB电弧："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("NB电弧："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    break;
                case 79://南京温湿度
                case 26://万科温湿度
                case 25://温湿度传感器
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("温湿度设备："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("温湿度设备："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).category_group_lin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, OneTHDevInfoActivity.class);
                            intent.putExtra("Mac",normalSmoke.getMac());
                            intent.putExtra("Position",normalSmoke.getName());
                            intent.putExtra("devType",normalSmoke.getDeviceType()+"");
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 7://声光。。
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("声光报警器："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("声光报警器："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 20://@@无线输入输出模块
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("无线输入输出模块："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("无线输入输出模块："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 8://手动。。
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("手动报警："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("手动报警："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 9://三江设备@@5.11。。
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("三江设备："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("三江设备："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    break;
                case 12://门磁
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("门磁："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("门磁："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 11://红外
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("红外探测器："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("红外探测器："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 13://环境
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("环境探测器："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("环境探测器："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).category_group_lin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(normalSmoke.getNetState()==0){
                                Toast.makeText(mContext,"设备不在线",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent intent = new Intent(mContext, NewAirInfoActivity.class);
                            intent.putExtra("Mac",normalSmoke.getMac());
                            intent.putExtra("Position",normalSmoke.getName());
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 51://创安
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("CA燃气："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("CA燃气："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).category_group_lin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ChuangAnActivity.class);
                            intent.putExtra("Mac",normalSmoke.getMac());
                            intent.putExtra("Position",normalSmoke.getName());
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 125:
                case 78:
                case 70:
                case 68:
                case 42:
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("水压探测器："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("水压探测器："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).category_group_lin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, LineChartActivity.class);
                            intent.putExtra("electricMac",normalSmoke.getMac());
                            intent.putExtra("isWater","1");//@@是否为水压
                            intent.putExtra("devType",normalSmoke.getDeviceType());//@@是否为水压
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 10://水压设备@@5.11。。
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("水压探测器："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("水压探测器："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).category_group_lin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, LineChartActivity.class);
                            intent.putExtra("electricMac",normalSmoke.getMac());
                            intent.putExtra("isWater",normalSmoke.getDeviceType()+"");//@@是否为水压
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 43://@@lora水压
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("水压探测器："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("水压探测器："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).category_group_lin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, LineChartActivity.class);
                            intent.putExtra("electricMac",normalSmoke.getMac());
                            intent.putExtra("isWater","3");//@@是否为水压
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 124:
                case 69:
                case 46:
                case 44://万科水位
                case 19://水位设备@@2018.01.02
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("水位探测器："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("水位探测器："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).category_group_lin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, LineChartActivity.class);
                            intent.putExtra("electricMac",normalSmoke.getMac());
                            intent.putExtra("isWater",normalSmoke.getDeviceType()+"");//@@是否为水压
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 27://万科水浸
                case 15://水浸设备@@8.3。。
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("水浸："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("水浸："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 18://喷淋@@10.31。。
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("喷淋："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("喷淋："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    break;
                case 14://GPS设备@@8.8
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("GPS："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("GPS："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    break;
                case 126:
                case 119://三江有线传输装置
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("传输装置："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("传输装置："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).category_group_lin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, WiredSmokeListActivity.class);
                            intent.putExtra("Mac",normalSmoke.getMac());
                            intent.putExtra("Position",normalSmoke.getName());
                            mContext.startActivity(intent);
                        }
                    });
                    ((ItemViewHolder) holder).power_button.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).power_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final ProgressDialog dialog1 = new ProgressDialog(mContext);
                            dialog1.setTitle("提示");
                            dialog1.setMessage("设置中，请稍候");
                            dialog1.setCanceledOnTouchOutside(false);
                            dialog1.show();
                            ApiStores apiStores1 = AppClient.retrofit(ConstantValues.SERVER_IP_NEW).create(ApiStores.class);
                            Call<HttpError> call=apiStores1.cancelSound(normalSmoke.getMac());
                            if (call != null) {
                                call.enqueue(new Callback<HttpError>() {
                                    @Override
                                    public void onResponse(Call<HttpError> call, retrofit2.Response<HttpError> response) {
                                        T.showShort(mContext,response.body().getError()+"");
                                        dialog1.dismiss();
                                    }
                                    @Override
                                    public void onFailure(Call<HttpError> call, Throwable t) {
                                        T.showShort(mContext,"失败");
                                        dialog1.dismiss();
                                    }
                                });
                            }
                        }
                    });
                    break;
            }

            ((ItemViewHolder) holder).address_tv.setText(normalSmoke.getAddress());
            ((ItemViewHolder) holder).mac_tv.setText(normalSmoke.getMac());//@@
            ((ItemViewHolder) holder).repeater_tv.setText(normalSmoke.getRepeater());
            ((ItemViewHolder) holder).type_tv.setText(normalSmoke.getPlaceType());
            ((ItemViewHolder) holder).area_tv.setText(normalSmoke.getAreaName());



            ((ItemViewHolder) holder).manager_img.setOnClickListener(new View.OnClickListener() {//拨打电话提示框。。
                @Override
                public void onClick(View v) {
                    if(normalSmoke.getPrincipal1()!=null&&normalSmoke.getPrincipal1().length()>0){
                        Intent intent=new Intent(mContext, CallManagerDialogActivity.class);
                        intent.putExtra("people1",normalSmoke.getPrincipal1());
                        intent.putExtra("people2",normalSmoke.getPrincipal2());
                        intent.putExtra("phone1",normalSmoke.getPrincipal1Phone());
                        intent.putExtra("phone2",normalSmoke.getPrincipal2Phone());
                        mContext.startActivity(intent);
                    }else{
                        T.showShort(mContext,"无联系人信息");
                    }
                }
            });
            ((ItemViewHolder) holder).category_group_lin.setOnLongClickListener(this);
            ((ItemViewHolder) holder).category_group_lin.setTag(position);
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.footViewItemTv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.footViewItemTv.setText("正在加载更多数据...");
                    break;
                case NO_MORE_DATA:
                    T.showShort(mContext, "没有更多数据");
                    footViewHolder.footer.setVisibility(View.GONE);
                    break;
                case NO_DATA:
                    footViewHolder.footer.setVisibility(View.GONE);
                    break;
            }
        }

    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return listNormalSmoke.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.category_group_lin)
        LinearLayout category_group_lin;
        @Bind(R.id.smoke_name_text)
        TextView smoke_name_text;
        @Bind(R.id.mac_tv)
        TextView mac_tv;
        @Bind(R.id.repeater_tv)
        TextView repeater_tv;
        @Bind(R.id.area_tv)
        TextView area_tv;
        @Bind(R.id.type_tv)
        TextView type_tv;
        @Bind(R.id.address_tv)
        TextView address_tv;
        @Bind(R.id.manager_img)
        ImageView manager_img;
        @Bind(R.id.right_into_image)
        ImageView right_into_image;
        @Bind(R.id.item_lin)
        LinearLayout item_lin;//@@8.8
        @Bind(R.id.state_name_tv)
        TextView state_name_tv;//@@11.01
        @Bind(R.id.state_tv)
        TextView state_tv;//@@11.01
        @Bind(R.id.rssi_value)
        TextView rssi_value;//@@2018.03.07
        @Bind(R.id.xy_button)
        Button power_button;//@@2018.03.07
        @Bind(R.id.dev_image)
        TextView dev_image;//@@2018.03.07
        @Bind(R.id.dev_hearttime_set)
        TextView dev_hearttime_set;//@@2018.03.07
        @Bind(R.id.voltage_image)
        ImageView voltage_image;
        @Bind(R.id.rssi_image)
        ImageView rssi_image;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.foot_view_item_tv)
        TextView footViewItemTv;
        @Bind(R.id.footer)
        LinearLayout footer;
        public FootViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //添加数据
    public void addItem(List<Smoke> smokeList) {
        smokeList.addAll(listNormalSmoke);
        listNormalSmoke.removeAll(listNormalSmoke);
        listNormalSmoke.addAll(smokeList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<Smoke> smokeList) {
        listNormalSmoke.addAll(smokeList);
        notifyDataSetChanged();
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
