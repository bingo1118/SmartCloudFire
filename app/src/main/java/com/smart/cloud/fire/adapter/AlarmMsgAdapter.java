package com.smart.cloud.fire.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.activity.AlarmMsg.DealMsgDetailActivity;
import com.smart.cloud.fire.activity.UploadAlarmInfo.UploadAlarmInfoActivity;
import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.InitBaiduNavi;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.fragment.CollectFragment.AlarmMessageModel;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.pushmessage.PushAlarmMsg;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

/**
 * Created by Rain on 2018/8/16.
 */
public class AlarmMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private List<AlarmMessageModel> messageModelList;
    private Activity mContext;
    private BasePresenter collectFragmentPresenter;
    private String userId;
    private String privilege;

    public AlarmMsgAdapter(Activity mContext, List<AlarmMessageModel> messageModelList
            , BasePresenter collectFragmentPresenter, String userId, String privilege) {
        this.mInflater = LayoutInflater.from(mContext);
        this.messageModelList = messageModelList;
        this.mContext = mContext;
        this.collectFragmentPresenter = collectFragmentPresenter;
        this.userId = userId;
        this.privilege = privilege;
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
            final View view = mInflater.inflate(R.layout.alarm_msg_item, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            ItemViewHolder viewHolder = new ItemViewHolder(view);
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
            final AlarmMessageModel mNormalAlarmMessage = messageModelList.get(position);
            final int alarmType = mNormalAlarmMessage.getAlarmType();
            int ifDeal = mNormalAlarmMessage.getIfDealAlarm();
            String msg_contant=mNormalAlarmMessage.getAreaName()+"的";
            ((ItemViewHolder) holder).address_tv.setText(mNormalAlarmMessage.getAddress());
            ((ItemViewHolder) holder).msg_time.setText(mNormalAlarmMessage.getAlarmTime());
            ((ItemViewHolder) holder).msg_grade_image.setBackgroundResource(R.drawable.bjrw_jj);
            ((ItemViewHolder) holder).msg_grade_tv.setText("紧急");
            ((ItemViewHolder) holder).msg_grade_tv.setTextColor(Color.parseColor("#fe0000"));

            ((ItemViewHolder) holder).msg_progress_tv.setOnClickListener(null);

            if (ifDeal == 2) {
                ((ItemViewHolder) holder).msg_deal_btn.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).msg_progress_image.setVisibility(View.GONE);
                ((ItemViewHolder) holder).msg_progress_tv.setVisibility(View.GONE);
            } else if(ifDeal==3){
                ((ItemViewHolder) holder).msg_deal_btn.setVisibility(View.GONE);
                ((ItemViewHolder) holder).msg_progress_image.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).msg_progress_tv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).msg_progress_image.setBackgroundResource(R.drawable.bjrw_cs);
                ((ItemViewHolder) holder).msg_progress_tv.setText("处理中");
                ((ItemViewHolder) holder).msg_progress_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, UploadAlarmInfoActivity.class);
                        PushAlarmMsg mPushAlarmMsg=new PushAlarmMsg();
                        mPushAlarmMsg.setMac(mNormalAlarmMessage.getMac());
                        mPushAlarmMsg.setName(mNormalAlarmMessage.getName());
                        mPushAlarmMsg.setAddress(mNormalAlarmMessage.getAddress());
                        mPushAlarmMsg.setAlarmTypeName(mNormalAlarmMessage.getAlarmTypeName());
                        mPushAlarmMsg.setAlarmTime(mNormalAlarmMessage.getAlarmTime());
                        intent.putExtra("mPushAlarmMsg",mPushAlarmMsg);
                        intent.putExtra("mac",mNormalAlarmMessage.getMac());
                        intent.putExtra("alarm",mNormalAlarmMessage.getAlarmType()+"");
                        mContext.startActivityForResult(intent,6);
                    }
                });
                ((ItemViewHolder) holder).msg_progress_tv.setTextColor(mContext.getResources().getColor(R.color.login_btn));
            }else if(ifDeal==1){
                ((ItemViewHolder) holder).msg_deal_btn.setVisibility(View.GONE);
                ((ItemViewHolder) holder).msg_progress_image.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).msg_progress_tv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).msg_progress_image.setBackgroundResource(R.drawable.bjrw_ycl);
                ((ItemViewHolder) holder).msg_progress_tv.setText("已处理");
                ((ItemViewHolder) holder).msg_progress_tv.setTextColor(Color.parseColor("#3681dd"));
                ((ItemViewHolder) holder).msg_progress_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, DealMsgDetailActivity.class);
                        intent.putExtra("id",mNormalAlarmMessage.getId());
                        mContext.startActivity(intent);
                    }
                });
            }
            int devType= mNormalAlarmMessage.getDeviceType();
            switch (devType){
                case 61:
                case 58:
                case 56:
                case 57:
                case 55:
                case 41:
                case 31:
                case 21:
                case 1:
                    msg_contant+="烟感:"+mNormalAlarmMessage.getName();
                    if (alarmType == 202) {
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.yanwu);
                    } else if(alarmType==67){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.zijian);
                    }else if(alarmType==14){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.chaichu);
                    }else if(alarmType==15){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.fchf);
                        setNormal(holder);
                    }else if(alarmType==103){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.gaowen);
                    }else if(alarmType==102){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.huifu);
                        setNormal(holder);
                    }else if(alarmType==106){

                    }else if(alarmType==109){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gz);
                    }else if(alarmType==110){
//                        message="发生烟雾故障报警恢复";
                    }else if(alarmType==111){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gz);
                    }else if(alarmType==112){
//                        message="发生温湿度故障报警恢复";
                    }else if(alarmType==113){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.yanwu);
                    }else if(alarmType == 193){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_ddy);
                        setNormal(holder);
                    }else{
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.weizhi);
                    }
                    break;
                case 25:
                    msg_contant+="温湿度设备:"+mNormalAlarmMessage.getName();
                    if (alarmType == 308) {
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.gaowen);
                    }else if(alarmType == 307){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.diwen);
                    }else if(alarmType == 407){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.dishidu);
                    }else if(alarmType == 408){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.gaoshidu);
                    }else if(alarmType == 193){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_ddy);
                        setNormal(holder);
                    }else{
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.weizhi);
                    }
                    break;
                case 36:
                case 35:
                    msg_contant+="NB电弧:"+mNormalAlarmMessage.getName();
                    if (alarmType == 53) {
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.baojing);
                    } else if(alarmType == 36){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gz);
                    }else if(alarmType == 54){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gz);
                    }
                    break;
                case 124:
                case 69:
                case 19:
                    msg_contant+="水位设备:"+mNormalAlarmMessage.getName();
                    if (alarmType == 207) {
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.dishuiwei);
                    } else if(alarmType == 208){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.gaoshuiwei);
                    }else if(alarmType == 193){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_ddy);
                        setNormal(holder);
                    }else if(alarmType == 136){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gz);
                    }else if(alarmType == 36){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gz);
                    }else{
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.weizhi);
                    }
                    break;
                case 18://@@10.31 喷淋
                    msg_contant+="喷淋设备:"+mNormalAlarmMessage.getName();
                    if (alarmType == 202||alarmType==66) {
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_huojing);
                    }else if(alarmType == 201){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_hz);
                    }else if(alarmType == 203){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_huojing);
                    }else if(alarmType == 193){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_ddy);
                        setNormal(holder);
                    }else{
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.weizhi);
                    }
                    break;
                case 45:
                    msg_contant+="气感设备:"+mNormalAlarmMessage.getName();
                    if (alarmType == 71) {
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.baojing);
                    } else if(alarmType == 72){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.baojing);
                    }else if(alarmType == 73){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.duanlu);
                    }else if(alarmType == 74){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.duanlu1);
                    }else if(alarmType == 70){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.huifu);
                        setNormal(holder);
                    }else if(alarmType == 193){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_ddy);
                        setNormal(holder);
                    }else{
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.weizhi);
                    }
                    break;
                case 16:
                case 22:
                case 23:
                case 51:
                case 2:
                    msg_contant+="燃气设备:"+mNormalAlarmMessage.getName();
                    ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.ranqi);
                    break;
                case 125:
                case 70:
                case 43:
                case 42:
                case 10://@@4.28
                    int alarmFamily10 = mNormalAlarmMessage.getAlarmFamily();//@@水压值8.31
                    String alarmFamilys=mNormalAlarmMessage.getAlarmFamilys();
                    msg_contant+="水压设备:"+mNormalAlarmMessage.getName();
                    switch(alarmType){
                        case 193://低电压@@
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_ddy);
                            setNormal(holder);
                            break;
                        case 209://低水压@@
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.dishuiya);
                            break;
                        case 218://高水压@@
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.gaoshuiya);
                            break;
                        case 217://水压升高@@
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.shuiyashenggao);
                            break;
                        case 210://水压降低@@
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.shuiyajiangdi);;
                            break;
                        case 136:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gz);
                            break;
                        case 36:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gz);
                            break;

                    }
                    break;
                case 59:
                case 52:
                case 53:
                case 5:
                    msg_contant+="电气设备:"+mNormalAlarmMessage.getName();
                    int alarmFamily = mNormalAlarmMessage.getAlarmFamily();
                    switch (alarmType){
                        case 136:
                        case 36:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gz);
                            break;
                        case 43:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gy);
                            break;
                        case 44:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_qy);
                            break;
                        case 45:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gl);
                            break;
                        case 46:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_ld);
                            break;
                        case 47:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gw);
                            break;
                        case 48:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_hz);//@@6.28
                            break;
                        case 49:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.duanlu);
                            break;
                        case 50:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.guore);//@@6.28
                            break;
                        case 143:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gy);
                            break;
                        case 144:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_qy);
                            break;
                        case 145:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gl);
                            break;
                        case 146:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_ld);
                            break;
                        case 147:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_gw);
                            break;
                        case 51:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_fz);
                            break;
                        case 148:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_hz);//@@6.28
                            break;
                        case 52:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.duanlu1);//@@12.26
                            break;
                        default:
                            ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.weizhi);
                            break;
                    }
                    break;
                case 7:
                    msg_contant+="声光设备:"+mNormalAlarmMessage.getName();
                    ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.shengguang);
                    break;
                case 8:
                    msg_contant+="手报设备:"+mNormalAlarmMessage.getName();
                    ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.shoubao);
                    break;
                case 11://@@8.3
                    msg_contant+="红外设备:"+mNormalAlarmMessage.getName();
                    if (alarmType == 202||alarmType == 206) {
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.baojing);//@@8.10
                    } else if(alarmType == 193){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_ddy);
                        setNormal(holder);
                    }else{
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.weizhi);
                    }
                    break;
                case 12://@@8.3
                    msg_contant+="门磁设备:"+mNormalAlarmMessage.getName();
                    if (alarmType == 202||alarmType == 205) {
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.baojing);//@@8.10
                    } else if(alarmType == 193){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_ddy);
                        setNormal(holder);
                    }else{
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.weizhi);
                    }
                    break;
                case 15://@@8.3
                    msg_contant+="水浸设备:"+mNormalAlarmMessage.getName();
                    if (alarmType == 202||alarmType == 221) {
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.baojing);//@@8.10
                    } else if(alarmType == 193){
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.xx_ddy);
                        setNormal(holder);
                    }else{
                        ((ItemViewHolder) holder).msg_type.setBackgroundResource(R.drawable.weizhi);
                    }
                    break;
                
            }
            msg_contant+="（设备号："+mNormalAlarmMessage.getMac()+")"+"发生报警，请尽快处理！";
            ((ItemViewHolder) holder).msg_contant_tv.setText(msg_contant);
            RxView.clicks(((ItemViewHolder) holder).msg_distance_tv).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    Smoke smoke = new Smoke();
                    smoke.setLatitude(mNormalAlarmMessage.getLatitude());
                    smoke.setLongitude(mNormalAlarmMessage.getLongitude());
                    Reference<Activity> reference = new WeakReference(mContext);
                    new InitBaiduNavi(reference.get(), smoke);
                }
            });


            //取消报警
            RxView.clicks(((ItemViewHolder) holder).msg_deal_btn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
//                    Intent intent=new Intent(mContext, UploadAlarmInfoActivity.class);
//                    intent.putExtra("mac",mNormalAlarmMessage.getMac());
//                    intent.putExtra("alarm",mNormalAlarmMessage.getAlarmType()+"");
//                    mContext.startActivity(intent);

//                    collectFragmentPresenter.dealAlarm(userId, mNormalAlarmMessage.getMac(), privilege,messageModelList.indexOf(mNormalAlarmMessage));//@@5.19添加index位置参数
                }
            });

            ((ItemViewHolder) holder).msg_deal_btn.setOnClickListener(this);
            ((ItemViewHolder) holder).msg_deal_btn.setTag(position);

            holder.itemView.setTag(position);
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

    private void setNormal(RecyclerView.ViewHolder holder) {
        ((ItemViewHolder) holder).msg_grade_image.setBackgroundResource(R.drawable.bjrw_yb);
        ((ItemViewHolder) holder).msg_grade_tv.setText("一般");
        ((ItemViewHolder) holder).msg_grade_tv.setTextColor(Color.parseColor("#505050"));
    }

    @Override
    public void onClick(View view) {
        if(mOnClickListener!=null){
            mOnClickListener.onClick(view, (int) view.getTag());
        }
    }

    public interface onClickListener{
        public void onClick(View view, int position);
    };

    private onClickListener mOnClickListener;

    public void setOnClickListener(onClickListener listener){
        this.mOnClickListener=listener;
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
        return messageModelList.size();
    }



    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.msg_contant_tv)
        TextView msg_contant_tv;//消息内容
        @Bind(R.id.msg_type)
        ImageView msg_type;//报警类型
        @Bind(R.id.address_tv)
        TextView address_tv;//报警地址
        @Bind(R.id.msg_time)
        TextView msg_time;//报警时间
        @Bind(R.id.msg_grade_image)
        ImageView msg_grade_image;//报警等级图标
        @Bind(R.id.msg_grade_tv)
        TextView msg_grade_tv;//报警等级
        @Bind(R.id.msg_distance_tv)
        TextView msg_distance_tv;//报警距离
        @Bind(R.id.msg_progress_image)
        ImageView msg_progress_image;//报警进度图标
        @Bind(R.id.msg_progress_tv)
        TextView msg_progress_tv;//报警进度
        @Bind(R.id.msg_deal_btn)
        Button msg_deal_btn;//立即处理


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
    public void addItem(List<AlarmMessageModel> alarmMessageModelList) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        alarmMessageModelList.addAll(messageModelList);
        messageModelList.removeAll(messageModelList);
        messageModelList.addAll(alarmMessageModelList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<AlarmMessageModel> alarmMessageModelList) {
        messageModelList.addAll(alarmMessageModelList);
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

    //@@5.18
    public void setList(int index) {
        this.messageModelList.get(index).setIfDealAlarm(1);//@@5.19
        notifyDataSetChanged();//@@5.19
    }
}
