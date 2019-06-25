package com.smart.cloud.fire.activity.Inspection.PointList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.cloud.fire.adapter.AlarmMsgAdapter;
import com.smart.cloud.fire.global.Point;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class PointListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<Point> pointList;
    private PointListPresenter mPresenter;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Point data);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    public PointListAdapter(Context mContext, List<Point> electricList,PointListPresenter presenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.pointList = electricList;
        this.mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.pointlist_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Point mPoint = pointList.get(position);
        ((ItemViewHolder) holder).name_tv.setText(mPoint.getName());
        ((ItemViewHolder) holder).area_tv.setText("区域:"+mPoint.getAreaName());
        ((ItemViewHolder) holder).address_tv.setText("地址:"+mPoint.getAddress());
        holder.itemView.setTag(mPoint);
    }

    @Override
    public int getItemCount() {

        return pointList.size();
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
        @Bind(R.id.area_tv)
        TextView area_tv;
        @Bind(R.id.address_tv)
        TextView address_tv;


        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
