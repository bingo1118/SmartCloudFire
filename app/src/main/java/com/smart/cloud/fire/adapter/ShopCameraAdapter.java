package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.ui.ApMonitorActivity;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ShopCameraAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Camera> listCamera;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;

    public ShopCameraAdapter(Context mContext, List<Camera> listCamera, ShopInfoFragmentPresenter mShopInfoFragmentPresenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.listCamera = listCamera;
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
            final Camera camera = listCamera.get(position);
            ((ItemViewHolder) holder).groupTvAddress.setText(camera.getCameraAddress());
            ((ItemViewHolder) holder).groupTv.setText(camera.getCameraName());
            ((ItemViewHolder) holder).repeaterNameTv.setText(camera.getPlaceType());
            ((ItemViewHolder) holder).repeaterMacTv.setText(camera.getAreaName());
            ((ItemViewHolder) holder).groupPrincipal1.setText(camera.getPrincipal1());
            ((ItemViewHolder) holder).groupPhone1.setText(camera.getPrincipal1Phone());
            ((ItemViewHolder) holder).groupPrincipal2.setText(camera.getPrincipal2());
            ((ItemViewHolder) holder).groupPhone2.setText(camera.getPrincipal2Phone());
            ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.yg_ygtubiao_sxj);
            ((ItemViewHolder) holder).groupPhone1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneOne = camera.getPrincipal1Phone();
                    mShopInfoFragmentPresenter.telPhoneAction(mContext, phoneOne);

                }
            });
            ((ItemViewHolder) holder).groupPhone2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneTwo = camera.getPrincipal2Phone();
                    mShopInfoFragmentPresenter.telPhoneAction(mContext, phoneTwo);
                }
            });
            ((ItemViewHolder) holder).categoryGroupLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Contact mContact = new Contact();
                    mContact.contactType = 0;
                    mContact.contactId = camera.getCameraId();
                    mContact.contactPassword = camera.getCameraPwd();
                    mContact.contactName = camera.getCameraName();
                    mContact.apModeState = 1;

                    Intent monitor = new Intent();
                    monitor.setClass(mContext, ApMonitorActivity.class);
                    monitor.putExtra("contact", mContact);
                    monitor.putExtra("connectType", ConstantValues.ConnectType.P2PCONNECT);
                    monitor.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(monitor);
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
        return listCamera.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.group_image)
        ImageView groupImage;
        @Bind(R.id.group_tv)
        TextView groupTv;
        @Bind(R.id.group_tv_address)
        TextView groupTvAddress;
        @Bind(R.id.repeater_name_tv)
        TextView repeaterNameTv;
        @Bind(R.id.repeater_mac_tv)
        TextView repeaterMacTv;
        @Bind(R.id.group_principal1)
        TextView groupPrincipal1;
        @Bind(R.id.group_phone1)
        TextView groupPhone1;
        @Bind(R.id.group_principal2)
        TextView groupPrincipal2;
        @Bind(R.id.group_phone2)
        TextView groupPhone2;
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
    public void addItem(List<Camera> cameraList) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        cameraList.addAll(listCamera);
        listCamera.removeAll(listCamera);
        listCamera.addAll(cameraList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<Camera> cameraList) {
        listCamera.addAll(cameraList);
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
