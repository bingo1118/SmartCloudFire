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
import android.widget.TextView;

import com.smart.cloud.fire.activity.NFCDev.NFCDevHistoryActivity;
import com.smart.cloud.fire.activity.NFCDev.NFCRecordBean;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security.AirInfoActivity;
import com.smart.cloud.fire.ui.CallManagerDialogActivity;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2017/8/17.
 */
public class NFCDevAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<NFCRecordBean> listNormalSmoke;

    public NFCDevAdapter(Context mContext, List<NFCRecordBean> listNormalSmoke) {
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
            final NFCRecordBean normalSmoke = listNormalSmoke.get(position);

            ((ItemViewHolder) holder).smoke_name_text.setText(normalSmoke.getDeviceName());

            ((ItemViewHolder) holder).category_group_lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NFCDevHistoryActivity.class);
                    intent.putExtra("uid",normalSmoke.getUid());
                    intent.putExtra("name",normalSmoke.getDeviceName());
                    mContext.startActivity(intent);
                }
            });


            ((ItemViewHolder) holder).address_tv.setText(normalSmoke.getAddress());
            ((ItemViewHolder) holder).mac_tv.setText(normalSmoke.getUid());//@@
            String state="待检";
            switch (normalSmoke.getDevicestate()){
                case "0":
                    state="待检";
                    ((ItemViewHolder) holder).repeater_tv.setTextColor(Color.BLACK);
                    break;
                case "1":
                    state="合格";
                    ((ItemViewHolder) holder).repeater_tv.setTextColor(Color.BLACK);
                    break;
                case "2":
                    state="不合格";
                    ((ItemViewHolder) holder).repeater_tv.setTextColor(Color.RED);
                    break;
            }
            ((ItemViewHolder) holder).repeater_tv.setText(state);
            ((ItemViewHolder) holder).repeater_name_tv.setText("状态：");
//            String type="烟感";
//            switch (normalSmoke.getDeviceType()){
//                case "1":
//                    type="消防控制室";
//                    break;
//                case "2":
//                    type="消防水泵房";
//                    break;
//                case "3":
//                    type="防排烟机房";
//                    break;
//                case "4":
//                    type="自备发电机房";
//                    break;
//                case "5":
//                    type="安全出口（防火门）";
//                    break;
//                case "6":
//                    type="疏散通道";
//                    break;
//                case "7":
//                    state="防火卷帘";
//                    break;
//                case "8":
//                    type="应急照明灯";
//                    break;
//                case "9":
//                    type="室内消火栓";
//                    break;
//                case "10":
//                    type="灭火器";
//                    break;
//                case "11":
//                    type="手动报警按钮";
//                    break;
//                case "12":
//                    type="声光报警器";
//                    break;
//                case "13":
//                    type="灯光疏散指示标志";
//                    break;
//            }
            ((ItemViewHolder) holder).type_tv.setText(normalSmoke.getDeviceTypeName());
            ((ItemViewHolder) holder).area_tv.setText(normalSmoke.getAreaName());
            ((ItemViewHolder) holder).addtime.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).addtime_tv.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).addtime_tv.setText(normalSmoke.getAddTime());

            ((ItemViewHolder) holder).manager_img.setVisibility(View.GONE);
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
        @Bind(R.id.repeater_name_tv)
        TextView repeater_name_tv;
        @Bind(R.id.area_tv)
        TextView area_tv;
        @Bind(R.id.type_tv)
        TextView type_tv;
        @Bind(R.id.address_tv)
        TextView address_tv;
        @Bind(R.id.addtime)
        TextView addtime;
        @Bind(R.id.addtime_tv)
        TextView addtime_tv;
        @Bind(R.id.manager_img)
        ImageView manager_img;
        @Bind(R.id.item_lin)
        LinearLayout item_lin;//@@8.8
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
    public void addItem(List<NFCRecordBean> smokeList) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        smokeList.addAll(listNormalSmoke);
        listNormalSmoke.removeAll(listNormalSmoke);
        listNormalSmoke.addAll(smokeList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<NFCRecordBean> smokeList) {
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

