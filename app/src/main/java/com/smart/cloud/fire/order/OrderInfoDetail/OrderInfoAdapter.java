package com.smart.cloud.fire.order.OrderInfoDetail;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.smart.cloud.fire.activity.NFCDev.NFCImageShowActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.order.OrderInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class OrderInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<OrderInfo> mList;

    public OrderInfoAdapter(Context mContext, List<OrderInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        final View view = LayoutInflater.from(mContext).inflate(R.layout.order_info_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        OrderInfoAdapter.ItemViewHolder viewHolder = new OrderInfoAdapter.ItemViewHolder(view);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final OrderInfo mJobOrder = mList.get(position);
        if(!StringUtils.isEmpty(mJobOrder.getDescription())){
            ((ItemViewHolder) holder).content_tv.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).content_tv.setText(mJobOrder.getDescription());
        }else{
            ((ItemViewHolder) holder).content_tv.setVisibility(View.GONE);
        }
        ((ItemViewHolder) holder).deal_tv.setText(mJobOrder.getOperateName());
        ((ItemViewHolder) holder).send_user_tv.setText("发起者:"+mJobOrder.getSendUserName());
        ((ItemViewHolder) holder).receive_user_tv.setText("执行者:"+mJobOrder.getReceiveUserName());
        ((ItemViewHolder) holder).time_tv.setText(mJobOrder.getSendTime());
        if(StringUtils.isEmpty(mJobOrder.getPicture())){
            ((ItemViewHolder) holder).image.setVisibility(View.GONE);
        }else{
            ((ItemViewHolder) holder).image.setVisibility(View.VISIBLE);
            String url= ConstantValues.NFC_IMAGES + "orderDealImg//"+ mJobOrder.getPicture()+".jpg";
            Glide.with(mContext)
                    .load(url)
                    .placeholder( R.drawable.photo_loading)
                    .thumbnail((float)0.0001)
                    .thumbnail(0.00001f).into(((ItemViewHolder) holder).image);
            ((ItemViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NFCImageShowActivity.class);
                    intent.putExtra("path",url);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.content_tv)
        TextView content_tv;
        @Bind(R.id.send_user_tv)
        TextView send_user_tv;
        @Bind(R.id.receive_user_tv)
        TextView receive_user_tv;
        @Bind(R.id.time_tv)
        TextView time_tv;
        @Bind(R.id.deal_tv)
        TextView deal_tv;
        @Bind(R.id.image)
        ImageView image;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

