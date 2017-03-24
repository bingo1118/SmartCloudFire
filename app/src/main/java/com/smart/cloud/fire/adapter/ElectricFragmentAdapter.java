package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.cloud.fire.global.Electric;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ElectricFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Electric> listNormalSmoke;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(Electric)v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , Electric data);
    }

    public ElectricFragmentAdapter(Context mContext, List<Electric> listNormalSmoke, ShopInfoFragmentPresenter mShopInfoFragmentPresenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.listNormalSmoke = listNormalSmoke;
        this.mContext = mContext;
        this.mShopInfoFragmentPresenter = mShopInfoFragmentPresenter;
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
            View view = mInflater.inflate(R.layout.electric_adapter, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            view.setOnClickListener(this);
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
            ((ItemViewHolder) holder).repeaterRela.setVisibility(View.VISIBLE);
            final Electric normalSmoke = listNormalSmoke.get(position);
            ((ItemViewHolder) holder).address.setText(normalSmoke.getAddress());
            ((ItemViewHolder) holder).groupTv.setText(normalSmoke.getName());
            ((ItemViewHolder) holder).repeaterNameTv.setText(normalSmoke.getPlaceType());
            ((ItemViewHolder) holder).repeaterMacTv.setText(normalSmoke.getAreaName());
            int state = normalSmoke.getNetState();
            switch (state){
                case 0:
                    ((ItemViewHolder) holder).state.setText("离线");
                    break;
                case 1:
                    ((ItemViewHolder) holder).state.setText("在线");
                    break;
                default:
                    break;
            }
            ((ItemViewHolder) holder).installTime.setText(normalSmoke.getAddSmokeTime());
            String phoneOne = normalSmoke.getPrincipal1Phone();
            String phoneTwo = normalSmoke.getPrincipal2Phone();
            if(phoneOne!=null&&phoneOne.length()>0){
                ((ItemViewHolder) holder).groupPrincipal1.setText(normalSmoke.getPrincipal1());
                ((ItemViewHolder) holder).groupPhone1.setText(normalSmoke.getPrincipal1Phone());
            }else{
                ((ItemViewHolder) holder).relateManOne.setVisibility(View.GONE);
            }
            if(phoneTwo!=null&&phoneTwo.length()>0){
                ((ItemViewHolder) holder).groupPrincipal2.setText(normalSmoke.getPrincipal2());
                ((ItemViewHolder) holder).groupPhone2.setText(normalSmoke.getPrincipal2Phone());
            }else{
                ((ItemViewHolder) holder).relateManTwo.setVisibility(View.GONE);
            }
            ((ItemViewHolder) holder).electricId.setText(normalSmoke.getMac());
            ((ItemViewHolder) holder).repeaterTv2.setText(normalSmoke.getRepeater());
            ((ItemViewHolder) holder).groupPhone1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneOne = normalSmoke.getPrincipal1Phone();
                    mShopInfoFragmentPresenter.telPhoneAction(mContext, phoneOne);
                }
            });
            ((ItemViewHolder) holder).groupPhone2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneTwo = normalSmoke.getPrincipal2Phone();
                    mShopInfoFragmentPresenter.telPhoneAction(mContext, phoneTwo);
                }
            });
            holder.itemView.setTag(normalSmoke);
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
        @Bind(R.id.group_tv)
        TextView groupTv;
        @Bind(R.id.electric_id)
        TextView electricId;
        @Bind(R.id.install_time)
        TextView installTime;
        @Bind(R.id.state_tv)
        TextView stateTv;
        @Bind(R.id.state)
        TextView state;
        @Bind(R.id.repeater_tv1)
        TextView repeaterTv1;
        @Bind(R.id.repeater_tv2)
        TextView repeaterTv2;
        @Bind(R.id.repeater_rela)
        RelativeLayout repeaterRela;
        @Bind(R.id.address_tv)
        TextView addressTv;
        @Bind(R.id.address)
        TextView address;
        @Bind(R.id.repeater_name)
        TextView repeaterName;
        @Bind(R.id.repeater_name_tv)
        TextView repeaterNameTv;
        @Bind(R.id.zjq_name)
        RelativeLayout zjqName;
        @Bind(R.id.repeater_mac)
        TextView repeaterMac;
        @Bind(R.id.repeater_mac_tv)
        TextView repeaterMacTv;
        @Bind(R.id.zjq_mac)
        RelativeLayout zjqMac;
        @Bind(R.id.group_principal1)
        TextView groupPrincipal1;
        @Bind(R.id.category_group_phone)
        ImageView categoryGroupPhone;
        @Bind(R.id.group_phone1)
        TextView groupPhone1;
        @Bind(R.id.phone_lin1)
        LinearLayout phoneLin1;
        @Bind(R.id.relate_man_one)
        LinearLayout relateManOne;
        @Bind(R.id.group_principal2)
        TextView groupPrincipal2;
        @Bind(R.id.category_group_phone2)
        ImageView categoryGroupPhone2;
        @Bind(R.id.group_phone2)
        TextView groupPhone2;
        @Bind(R.id.phone_lin2)
        LinearLayout phoneLin2;
        @Bind(R.id.relate_man_two)
        LinearLayout relateManTwo;
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
    public void addItem(List<Electric> smokeList) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        smokeList.addAll(listNormalSmoke);
        listNormalSmoke.removeAll(listNormalSmoke);
        listNormalSmoke.addAll(smokeList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<Electric> smokeList) {
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
