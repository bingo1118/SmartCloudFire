package com.smart.cloud.fire.activity.Inspection.TaskList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smart.cloud.fire.global.InspectionTask;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<InspectionTask> mList;
    private TaskListPresenter mPresenter;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, InspectionTask data);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    public TaskListAdapter(Context mContext, List<InspectionTask> list,TaskListPresenter presenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mList = list;
        this.mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.task_pointlist_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InspectionTask mPoint = mList.get(position);
        ((ItemViewHolder) holder).name_tv.setText(mPoint.getTid());
        ((ItemViewHolder) holder).area_tv.setText("区域:"+mPoint.getArea());
        ((ItemViewHolder) holder).address_tv.setText("地址:"+mPoint.getPname());
        ((ItemViewHolder) holder).level_tv.setText("类型:"+mPoint.getLevelName());
        ((ItemViewHolder) holder).state_tv.setText("状态:"+mPoint.getState());
        ((ItemViewHolder) holder).sum_tv.setText("总数:"+mPoint.getItemNum());
        ((ItemViewHolder) holder).pass_tv.setText("合格:"+mPoint.getPass());
        ((ItemViewHolder) holder).progress_tv.setText("已检:"+mPoint.getProgress());

        holder.itemView.setTag(mPoint);
    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (InspectionTask) v.getTag());
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
        @Bind(R.id.state_tv)
        TextView state_tv;
        @Bind(R.id.pass_tv)
        TextView pass_tv;
        @Bind(R.id.progress_tv)
        TextView progress_tv;
        @Bind(R.id.sum_tv)
        TextView sum_tv;
        @Bind(R.id.level_tv)
        TextView level_tv;





        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

