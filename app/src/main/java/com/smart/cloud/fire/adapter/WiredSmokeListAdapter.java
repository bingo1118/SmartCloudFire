package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment.WiredSmoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment.WiredSmokeHistoryActivity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment.WiredSmokeListActivity;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2017/6/30.
 */
public class WiredSmokeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<WiredSmoke> listNormalSmoke;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;

    public WiredSmokeListAdapter(Context mContext, List<WiredSmoke> listNormalSmoke, ShopInfoFragmentPresenter mShopInfoFragmentPresenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.listNormalSmoke = listNormalSmoke;
        this.mContext = mContext;
        this.mShopInfoFragmentPresenter = mShopInfoFragmentPresenter;
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
            final View view = mInflater.inflate(R.layout.wiredsmoke_info_adapter, parent, false);
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
            final WiredSmoke normalSmoke = listNormalSmoke.get(position);
            ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
            ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.yg_yg_zx);



            if(normalSmoke.getHostType().equals("224")){
                ((ItemViewHolder) holder).macTv.setText("区域:"+normalSmoke.getFaultCode()+"分区");//@@
                ((ItemViewHolder) holder).equipmentDesc.setText(normalSmoke.getFaultDevDesc()+"");
            }else{
                ((ItemViewHolder) holder).macTv.setText("MAC:"+normalSmoke.getFaultCode());//@@
                ((ItemViewHolder) holder).equipmentDesc.setText(normalSmoke.getFaultDevDesc());
            }//@@7.12
            if(normalSmoke.getFaultType().contains("消除")){
                ((ItemViewHolder) holder).equipmentFaultType_tv.setText("正常");
            }else{
                ((ItemViewHolder) holder).equipmentFaultType_tv.setText(normalSmoke.getFaultType());
            }
            ((ItemViewHolder) holder).updatetime_tv.setText(normalSmoke.getFaultTime());
            ((ItemViewHolder) holder).equipmentFaultInfo_tv.setText(normalSmoke.getFaultInfo());
            ((ItemViewHolder) holder).categoryGroupLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WiredSmokeHistoryActivity.class);
                    intent.putExtra("Mac",normalSmoke.getFaultCode());
                    intent.putExtra("Repeater",normalSmoke.getRepeaterMac());
                    intent.putExtra("Position",normalSmoke.getFaultDevDesc());
                    intent.putExtra("HostType",normalSmoke.getHostType());//@@@7.12
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
        @Bind(R.id.group_image)
        ImageView groupImage;
        @Bind(R.id.equipmentDesc)
        TextView equipmentDesc;//@@设备名称
        @Bind(R.id.mac_tv)
        TextView macTv;//@@设备mac
        @Bind(R.id.equipmentFaultType_tv)
        TextView equipmentFaultType_tv;
        @Bind(R.id.equipmentFaultInfo_tv)
        TextView equipmentFaultInfo_tv;
        @Bind(R.id.updatetime_tv)
        TextView updatetime_tv;
        @Bind(R.id.category_group_lin)
        LinearLayout categoryGroupLin;
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
    public void addItem(List<WiredSmoke> smokeList) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        smokeList.addAll(listNormalSmoke);
        listNormalSmoke.removeAll(listNormalSmoke);
        listNormalSmoke.addAll(smokeList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<WiredSmoke> smokeList) {
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

