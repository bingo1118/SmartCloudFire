package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.platform.comapi.map.v;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.smart.cloud.fire.activity.NFCDev.NFCImageShowActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment.WiredSmokeHistory;
import com.smart.cloud.fire.utils.T;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2017/8/18.
 */
public class NFCHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<WiredSmokeHistory> listNormalSmoke;

    public NFCHistoryAdapter(Context mContext, List<WiredSmokeHistory> listNormalSmoke) {
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
            final View view = mInflater.inflate(R.layout.wiredsmoke_history_adapter, parent, false);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final WiredSmokeHistory normalSmoke = listNormalSmoke.get(position);


            String state="待检";
            switch (normalSmoke.getFaultType()){
                case "0":
                    state="待检";
                    break;
                case "1":
                    state="合格";
                    break;
                case "2":
                    state="不合格";
                    break;
            }
            ((ItemViewHolder) holder).device_name.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).area_name.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).state_tv.setText(state);
            ((ItemViewHolder) holder).info_tv.setText(normalSmoke.getFaultInfo());
            ((ItemViewHolder) holder).area_name_tv.setText(normalSmoke.getAreaName());
            ((ItemViewHolder) holder).device_name_tv.setText(normalSmoke.getDeviceName());
            ((ItemViewHolder) holder).alarm_time_tv.setText(normalSmoke.getFaultTime());
            ((ItemViewHolder) holder).station_name.setText("状态:");
            ((ItemViewHolder) holder).userid_rela.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).userid_tv.setText(normalSmoke.getUserid());
            if(normalSmoke.getPhoto1()!=null&&!normalSmoke.getPhoto1().equals("")){
                ((ItemViewHolder) holder).photo1_image.setVisibility(View.VISIBLE);//@@9.28
                String temp=normalSmoke.getPhoto1();
                final String temp1=ConstantValues.NFC_IMAGES+normalSmoke.getPhoto1().replace("\\","/");
                Glide.with(mContext)
//                    .load("http://139.159.209.212:51091/nfcimages/2017/1506499353470.jpg").thumbnail((float)0.0001)
                        .load(temp1).thumbnail(0.00001f).listener(new RequestListener() {

                    @Override
                    public boolean onException(Exception arg0, Object arg1,
                                               Target arg2, boolean arg3) {
                        //加载图片出错
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Object arg0, Object arg1,
                                                   Target arg2, boolean arg3, boolean arg4) {
                        //加载图片成功
                        ((ItemViewHolder) holder).photo1_image.setBackground(null);
                        return false;
                    }
                }).into(((ItemViewHolder) holder).photo1_image);//@@9.28
                ((ItemViewHolder) holder).photo1_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, NFCImageShowActivity.class);
                        intent.putExtra("path",temp1);
                        mContext.startActivity(intent);
                    }
                });
            }else{
                ((ItemViewHolder) holder).photo1_image.setVisibility(View.GONE);//@@9.28
            }
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
        @Bind(R.id.alarm_time_tv)
        TextView alarm_time_tv;//@@时间
        @Bind(R.id.device_name_tv)
        TextView device_name_tv;//@@设备名称
        @Bind(R.id.area_name_tv)
        TextView area_name_tv;//@@设备区域
        @Bind(R.id.state_tv)
        TextView state_tv;//@@设备状态
        @Bind(R.id.info_tv)
        TextView info_tv;//@@设备详情
        @Bind(R.id.userid_tv)
        TextView userid_tv;//@@10.27巡检人
        @Bind(R.id.station_name)
        TextView station_name;
        @Bind(R.id.photo1_image)
        ImageView photo1_image;//@@9.28
        @Bind(R.id.userid_rela)
        RelativeLayout userid_rela;//@@10.27
        @Bind(R.id.device_name)
        RelativeLayout device_name;//@@10.27
        @Bind(R.id.area_name)
        RelativeLayout area_name;//@@10.27
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
    public void addItem(List<WiredSmokeHistory> smokeList) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        smokeList.addAll(listNormalSmoke);
        listNormalSmoke.removeAll(listNormalSmoke);
        listNormalSmoke.addAll(smokeList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<WiredSmokeHistory> smokeList) {
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
