package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.cloud.fire.global.ElectricValue;
import com.smart.cloud.fire.mvp.electricChangeHistory.ElectricChangeHistoryPresenter;
import com.smart.cloud.fire.mvp.electricChangeHistory.HistoryBean;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2017/8/28.
 */
public class ElectricChangeHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<HistoryBean> electricList;
    private ElectricChangeHistoryPresenter electricPresenter;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (ElectricValue.ElectricValueBean) v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ElectricValue.ElectricValueBean data);
    }

    public ElectricChangeHistoryAdapter(Context mContext, List<HistoryBean> electricList) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.electricList = electricList;
        this.mContext = mContext;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            final View view = mInflater.inflate(R.layout.electric_change_history_item, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            view.setOnClickListener(this);
            return viewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.recycler_load_more_layout, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            ShopSmokeAdapter.FootViewHolder footViewHolder = new ShopSmokeAdapter.FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);//@@5.13
        if (holder instanceof ItemViewHolder) {
            HistoryBean electric = electricList.get(position);
            ((ItemViewHolder) holder).electricLin.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).electric_mac.setText(electric.getMac());
            switch (electric.getUserId()){
                case "6601":
                    ((ItemViewHolder) holder).electric_userid.setText("线路已闭合");
                    break;
                case "6602":
                    ((ItemViewHolder) holder).electric_userid.setText("线路已切断");
                    break;
                case "6603":
                    ((ItemViewHolder) holder).electric_userid.setText("线路已闭合");
                    break;
                case "6604":
                    ((ItemViewHolder) holder).electric_userid.setText("线路已切断");
                    break;
                default:
                    ((ItemViewHolder) holder).electric_userid.setText(electric.getUserId());
                    break;
            }

            if(electric.getState().equals("1")){
                ((ItemViewHolder) holder).electric_state.setText("合闸");
            }else{
                ((ItemViewHolder) holder).electric_state.setText("分闸");
            }
            ((ItemViewHolder) holder).electric_time.setText(electric.getChangetime());
            ((ItemViewHolder) holder).electric_username.setText(electric.getUserName());
            holder.itemView.setTag(position);
        } else if (holder instanceof ShopSmokeAdapter.FootViewHolder) {
            ShopSmokeAdapter.FootViewHolder footViewHolder = (ShopSmokeAdapter.FootViewHolder) holder;
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
        return electricList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.electric_mac)
        TextView electric_mac;
        @Bind(R.id.electric_userid)
        TextView electric_userid;
        @Bind(R.id.electric_state)
        TextView electric_state;
        @Bind(R.id.electric_time)
        TextView electric_time;
        @Bind(R.id.electric_username)
        TextView electric_username;
        @Bind(R.id.electric_lin)
        LinearLayout electricLin;
        @Bind(R.id.tv_image)
        TextView tvImage;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //添加数据
    public void addItem(List<HistoryBean> electrics) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        electrics.addAll(electricList);
        electricList.removeAll(electricList);
        electricList.addAll(electrics);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<HistoryBean> electrics) {
        electricList.addAll(electrics);
        notifyDataSetChanged();
    }

    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }

}
