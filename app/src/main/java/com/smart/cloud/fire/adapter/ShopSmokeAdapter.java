package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.activity.AllSmoke.AllSmokePresenter;
import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.mvp.ChuangAn.ChuangAnActivity;
import com.smart.cloud.fire.mvp.LineChart.LineChartActivity;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security.AirInfoActivity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security.NewAirInfoActivity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.ui.CallManagerDialogActivity;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ShopSmokeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    public ShopSmokeAdapter(Context mContext, List<Smoke> listNormalSmoke) {
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
            final View view = mInflater.inflate(R.layout.shop_info_adapter, parent, false);
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
            final Smoke normalSmoke = listNormalSmoke.get(position);
            int devType = normalSmoke.getDeviceType();
            int netStates = normalSmoke.getNetState();
            ((ItemViewHolder) holder).right_into_image.setVisibility(View.VISIBLE);//@@9.14
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
            switch (devType){
                case 41://@@NB烟感
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
                case 5://电气。。
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("电气设备："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("电气设备："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
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
                        ((ItemViewHolder) holder).smoke_name_text.setText("创安燃气："+normalSmoke.getName()+"（已离线)");
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).smoke_name_text.setText("创安燃气："+normalSmoke.getName());
                        ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
                    }
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
                case 42:
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
                            intent.putExtra("isWater","1");//@@是否为水压
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 124:
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
                            intent.putExtra("isWater","2");//@@是否为水压
                            mContext.startActivity(intent);
                        }
                    });
                    break;
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
            }

            ((ItemViewHolder) holder).address_tv.setText(normalSmoke.getAddress());
            ((ItemViewHolder) holder).mac_tv.setText(normalSmoke.getMac());//@@
            ((ItemViewHolder) holder).repeater_tv.setText(normalSmoke.getRepeater());
            ((ItemViewHolder) holder).type_tv.setText(normalSmoke.getPlaceType());
            ((ItemViewHolder) holder).area_tv.setText(normalSmoke.getAreaName());

            ((ItemViewHolder) holder).manager_img.setOnClickListener(new View.OnClickListener() {//拨打电话提示框。。
                @Override
                public void onClick(View v) {
//                    String phoneOne = normalSmoke.getPrincipal1Phone();
//                    mShopInfoFragmentPresenter.telPhoneAction(mContext,phoneOne);
                    Intent intent=new Intent(mContext, CallManagerDialogActivity.class);
                    intent.putExtra("people1",normalSmoke.getPrincipal1());
                    intent.putExtra("people2",normalSmoke.getPrincipal2());
                    intent.putExtra("phone1",normalSmoke.getPrincipal1Phone());
                    intent.putExtra("phone2",normalSmoke.getPrincipal2Phone());
                    mContext.startActivity(intent);
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
        ImageView manager_img;
        @Bind(R.id.right_into_image)
        ImageView right_into_image;
        @Bind(R.id.item_lin)
        LinearLayout item_lin;//@@8.8
        @Bind(R.id.state_name_tv)
        TextView state_name_tv;//@@11.01
        @Bind(R.id.state_tv)
        TextView state_tv;//@@11.01
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
}
