package com.smart.cloud.fire.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Electric;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.electricChangeHistory.ElectricChangeHistoryActivity;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.ui.CallManagerDialogActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ElectricFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnLongClickListener,View.OnClickListener{

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
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (null != mOnLongClickListener) {
            mOnLongClickListener.onLongClick(view, (int) view.getTag());
        }
        return true;
    }

    public interface OnLongClickListener {
        void onLongClick(View view, int position);
    }


    private OnLongClickListener mOnLongClickListener = null;

    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }


    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , int data);
    }

    public ElectricFragmentAdapter(Context mContext, List<Smoke> listNormalSmoke) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.listNormalSmoke = listNormalSmoke;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
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
            View view = mInflater.inflate(R.layout.shop_info_adapter, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final Smoke normalSmoke = listNormalSmoke.get(position);

            int devType=normalSmoke.getDeviceType();
            final int state = normalSmoke.getNetState();

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

            switch (devType){
                case 35:
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).setting_button.setVisibility(View.VISIBLE);
                    if (state == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("电弧："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("电弧："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    ((ItemViewHolder) holder).setting_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopupMenu(((ItemViewHolder) holder).setting_button,normalSmoke.getMac());
                        }
                    });
                    break;
                default:
                    ((ItemViewHolder) holder).right_into_image.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).setting_button.setVisibility(View.GONE);
                    if (state == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("电气设备："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("电气设备："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
                    final int privilege = MyApp.app.getPrivilege();
                    final int eleState = normalSmoke.getElectrState();
                    //if(privilege==3){//@@8.28权限3有切换电源功能
                    if (state == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).power_button.setVisibility(View.GONE);
                    }else{
                        if(devType!=80&&devType!=81){
                            switch (eleState){
                                case 1:
                                    ((ItemViewHolder) holder).power_button.setVisibility(View.VISIBLE);
                                    ((ItemViewHolder) holder).power_button.setText("切断电源");
                                    break;
                                case 2:
                                    ((ItemViewHolder) holder).power_button.setVisibility(View.VISIBLE);
                                    ((ItemViewHolder) holder).power_button.setText("打开电源");
                                    break;
                                case 3:
                                    ((ItemViewHolder) holder).power_button.setVisibility(View.VISIBLE);
                                    ((ItemViewHolder) holder).power_button.setText("设置中");
                                    break;
                                default:
                                    ((ItemViewHolder) holder).power_button.setVisibility(View.GONE);
                                    break;
                            }
                        }
                    }

                    final TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            -0.2f, Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAction.setDuration(500);

                    ((ItemViewHolder) holder).dev_info_rela.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).show_info_text.setText("展开详情");
                    ((ItemViewHolder) holder).show_info_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String s= (String) ((ItemViewHolder) holder).show_info_text.getText();
                            if(s.equals("展开详情")){
                                ((ItemViewHolder) holder).dev_info_rela.setVisibility(View.VISIBLE);
                                ((ItemViewHolder) holder).dev_info_rela.startAnimation(mShowAction);
                                ((ItemViewHolder) holder).show_info_text.setText("收起详情");
                            }else{
                                ((ItemViewHolder) holder).dev_info_rela.setVisibility(View.GONE);
                                ((ItemViewHolder) holder).show_info_text.setText("展开详情");
                            }
                        }
                    });
                    if (state == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).online_state_image.setImageResource(R.drawable.sblb_lixian);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).online_state_image.setImageResource(R.drawable.sblb_zaixian);
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }

                    ((ItemViewHolder) holder).power_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(state==0){
                                Toast.makeText(mContext,"设备不在线",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(privilege!=3&&privilege!=4){
                                Toast.makeText(mContext,"您没有该权限",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if(eleState==2){
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage("如未排除故障，合闸将造成严重事故!");
                                builder.setTitle("警告");
                                builder.setPositiveButton("我已知晓", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        changepower(2,normalSmoke);
                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create().show();
                            }else if(eleState==1){
                                changepower(1,normalSmoke);
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

            if(normalSmoke.getRssivalue().equals("0")){
                ((ItemViewHolder) holder).rssi_value.setVisibility(View.GONE);
                ((ItemViewHolder) holder).rssi_image.setVisibility(View.GONE);
            }else{
                ((ItemViewHolder) holder).rssi_value.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).rssi_image.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).rssi_value.setText(normalSmoke.getRssivalue());
            }



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
        TextView manager_img;
        @Bind(R.id.power_button)
        TextView power_button;//@@切换电源按钮
        @Bind(R.id.right_into_image)
        ImageView right_into_image;
        @Bind(R.id.setting_button)
        TextView setting_button;
        @Bind(R.id.rssi_image)
        ImageView rssi_image;
        @Bind(R.id.voltage_image)
        ImageView voltage_image;
        @Bind(R.id.show_info_text)
        TextView show_info_text;
        @Bind(R.id.dev_info_rela)
        RelativeLayout dev_info_rela;
        @Bind(R.id.online_state_image)
        ImageView online_state_image;

        @Bind(R.id.rssi_value)
        TextView rssi_value;//@@2018.03.07

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
        //mTitles.add(position, data);
        //notifyItemInserted(position);
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

    public void changepower(final int eleState, final Smoke normalSmoke){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if(eleState==1){
            builder.setMessage("确认切断电源吗？");
        }else{
            builder.setMessage("隐患已解决，确定合闸？");
        }
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userID = SharedPreferencesManager.getInstance().getData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTNAME);
                RequestQueue mQueue = Volley.newRequestQueue(mContext);
                String url="";
                if(normalSmoke.getDeviceType()==53){
                    if(eleState==1){
                        url= ConstantValues.SERVER_IP_NEW+"EasyIot_Switch_control?devSerial="+normalSmoke.getMac()+"&eleState=2&appId=1&userId="+userID;
                    }else{
                        url=ConstantValues.SERVER_IP_NEW+"EasyIot_Switch_control?devSerial="+normalSmoke.getMac()+"&eleState=1&appId=1&userId="+userID;
                    }
                }else if(normalSmoke.getDeviceType()==75||normalSmoke.getDeviceType()==77){
                    String userid= SharedPreferencesManager.getInstance().getData(mContext,
                            SharedPreferencesManager.SP_FILE_GWELL,
                            SharedPreferencesManager.KEY_RECENTNAME);
                    if(eleState==1){
                        url= ConstantValues.SERVER_IP_NEW+"Telegraphy_Uool_control?imei="+normalSmoke.getMac()+"&deviceType="+normalSmoke.getDeviceType()+"&devCmd=12&userid="+userid;
                    }else{
                        url=ConstantValues.SERVER_IP_NEW+"Telegraphy_Uool_control?imei="+normalSmoke.getMac()+"&deviceType="+normalSmoke.getDeviceType()+"&devCmd=13&userid="+userid;
                    }
                }else{
                    if(eleState==1){
                        url= ConstantValues.SERVER_IP_NEW+"ackControl?smokeMac="+normalSmoke.getMac()+"&eleState=2&userId="+userID;
                    }else{
                        url=ConstantValues.SERVER_IP_NEW+"ackControl?smokeMac="+normalSmoke.getMac()+"&eleState=1&userId="+userID;
                    }
                }
                final ProgressDialog dialog1 = new ProgressDialog(mContext);
                dialog1.setTitle("提示");
                dialog1.setMessage("设置中，请稍候...");
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        if(dialog1.isShowing()&&millisUntilFinished<40000){
                            dialog1.setMessage("网络延迟，请耐心等待...");
                        }
                    }

                    public void onFinish() {
                        dialog1.setMessage("设置中，请稍候...");
                    }
                }.start();

                dialog1.setCanceledOnTouchOutside(false);
                dialog1.show();
//                Toast.makeText(mContext,"设置中，请稍候",Toast.LENGTH_SHORT).show();
                StringRequest stringRequest = new StringRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    int errorCode=jsonObject.getInt("errorCode");
                                    if(errorCode==0){
                                        switch (eleState){
                                            case 2:
                                                normalSmoke.setElectrState(1);
                                                break;
                                            case 1:
                                                normalSmoke.setElectrState(2);
                                                break;
                                        }
                                        notifyDataSetChanged();
                                        Toast.makeText(mContext,jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(mContext,jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dialog1.dismiss();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog1.dismiss();
                        Toast.makeText(mContext,"设置超时",Toast.LENGTH_SHORT).show();
                    }
                });
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
//                        0,
//                        0.0f));
                mQueue.add(stringRequest);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void showPopupMenu(View view, final String mac) {
        final ProgressDialog dialog1 = new ProgressDialog(mContext);
        dialog1.setTitle("提示");
        dialog1.setMessage("设置中，请稍候");
        dialog1.setCanceledOnTouchOutside(false);
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_dianhu_settting, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int command=0;
                switch (item.getItemId()) {
                    case R.id.clear_on:
                        command=4;
                        break;
                    case R.id.clear_off:
                        command=5;
                        break;
                    case R.id.reset:
                        command=6;
                        break;
                    case R.id.cheak_myself:
                        command=7;
                        break;
                }
                VolleyHelper helper=VolleyHelper.getInstance(mContext);
                RequestQueue mQueue = helper.getRequestQueue();
                String userID = SharedPreferencesManager.getInstance().getData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTNAME);
                String url= ConstantValues.SERVER_IP_NEW+"EasyIot_arc_electric?devSerial="+mac+
                        "&userId="+userID+"&appId=1&arcValue="+command;
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
                                    }
                                    dialog1.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    dialog1.dismiss();
                                }
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
                dialog1.show();

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

    private void showPopupMenu_75(View view, final String mac) {
        final ProgressDialog dialog1 = new ProgressDialog(mContext);
        dialog1.setTitle("提示");
        dialog1.setMessage("设置中，请稍候");
        dialog1.setCanceledOnTouchOutside(false);
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_dianhu_settting, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int command=0;
                switch (item.getItemId()) {
                    case R.id.clear_on:
                        command=4;
                        break;
                    case R.id.clear_off:
                        command=5;
                        break;
                    case R.id.reset:
                        command=6;
                        break;
                    case R.id.cheak_myself:
                        command=7;
                        break;
                }
                VolleyHelper helper=VolleyHelper.getInstance(mContext);
                RequestQueue mQueue = helper.getRequestQueue();
                String userID = SharedPreferencesManager.getInstance().getData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTNAME);
                String url= ConstantValues.SERVER_IP_NEW+"EasyIot_arc_electric?devSerial="+mac+
                        "&userId="+userID+"&appId=1&arcValue="+command;
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
                                    }
                                    dialog1.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    dialog1.dismiss();
                                }
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
                dialog1.show();

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

}
