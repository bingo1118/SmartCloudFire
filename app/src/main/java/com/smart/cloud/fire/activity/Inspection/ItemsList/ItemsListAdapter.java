package com.smart.cloud.fire.activity.Inspection.ItemsList;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.UpdateItemInfoActivity;
import com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo.UploadInspectionInfoActivity;
import com.smart.cloud.fire.activity.UploadNFCInfo.UploadNFCInfoActivity;
import com.smart.cloud.fire.global.NFCInfoEntity;
import com.smart.cloud.fire.global.Point;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<NFCInfoEntity> itemsList;
    private ItemsListPresenter mPresenter;
    private ItemsListAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private String tid;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Point data);
    }

    public void setOnRecyclerViewItemClickListener(ItemsListAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    public ItemsListAdapter(Context mContext, List<NFCInfoEntity> electricList,ItemsListPresenter presenter,String tid) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.itemsList = electricList;
        this.mPresenter = presenter;
        this.tid=tid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.inspection_itemlist_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemsListAdapter.ItemViewHolder viewHolder = new ItemsListAdapter.ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NFCInfoEntity mPoint = itemsList.get(position);
        ((ItemsListAdapter.ItemViewHolder) holder).name_tv.setText(mPoint.getDeviceName());
        String state="";
        if(mPoint.getIscheck()!=null){
            ((ItemViewHolder) holder).state_tv.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).turn_to_update.setVisibility(View.GONE);
            if(tid==null){
                ((ItemViewHolder) holder).turn_to_insp.setVisibility(View.GONE);
            }else{
                ((ItemViewHolder) holder).turn_to_insp.setVisibility(View.VISIBLE);
            }
            switch (mPoint.getIscheck()){
                case "0":
                    state="未检";
                    ((ItemViewHolder) holder).turn_to_insp.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).modify.setVisibility(View.GONE);
                    break;
                case "1":
                    if(mPoint.getQualified().equals("1")){
                        state="合格";
                    }else{
                        state="不合格";
                    }
                    ((ItemViewHolder) holder).turn_to_insp.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).modify.setVisibility(View.VISIBLE);
                    break;
            }
        }else{
            ((ItemViewHolder) holder).state_tv.setVisibility(View.GONE);
            ((ItemViewHolder) holder).turn_to_insp.setVisibility(View.GONE);
            ((ItemViewHolder) holder).turn_to_update.setVisibility(View.VISIBLE);
        }

        ((ItemViewHolder) holder).worker_tv.setText("地址:"+mPoint.getAddress());
        ((ItemViewHolder) holder).state_tv.setText("状态:"+state);
        ((ItemViewHolder) holder).id_tv.setText("ID:"+mPoint.getUid());

        ((ItemViewHolder) holder).turn_to_insp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, UploadInspectionInfoActivity.class);
                intent.putExtra("uid",mPoint.getUid());
                intent.putExtra("tid",tid);
                intent.putExtra("memo",mPoint.getMemo());
                mContext.startActivity(intent);
            }
        });
        ((ItemViewHolder) holder).modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, UploadInspectionInfoActivity.class);
                intent.putExtra("uid",mPoint.getUid());
                intent.putExtra("tid",tid);
                intent.putExtra("memo",mPoint.getMemo());
                intent.putExtra("modify","1");
                mContext.startActivity(intent);
            }
        });
        ((ItemViewHolder) holder).turn_to_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, UpdateItemInfoActivity.class);
                intent.putExtra("uid",mPoint.getUid());
                mContext.startActivity(intent);
            }
        });
        holder.itemView.setTag(mPoint);
    }

    @Override
    public int getItemCount() {

        return itemsList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Point) v.getTag());
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_tv)
        TextView name_tv;
        @Bind(R.id.state_tv)
        TextView state_tv;
        @Bind(R.id.id_tv)
        TextView id_tv;
        @Bind(R.id.worker_tv)
        TextView worker_tv;
        @Bind(R.id.turn_to_insp)
        Button turn_to_insp;
        @Bind(R.id.turn_to_update)
        Button turn_to_update;
        @Bind(R.id.modify)
        Button modify;


        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
