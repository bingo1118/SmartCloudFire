package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hikvision.open.app.PreviewActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.ui.ApMonitorActivity;
import com.smart.cloud.fire.ui.CallManagerDialogActivity;
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

    public ShopCameraAdapter(Context mContext, List<Camera> listCamera) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.listCamera = listCamera;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final Camera camera = listCamera.get(position);
            ((ItemViewHolder) holder).address_tv.setText(camera.getCameraAddress());
            ((ItemViewHolder) holder).mac_tv.setText(camera.getCameraId());//@@
            ((ItemViewHolder) holder).repeater_tv.setVisibility(View.GONE);
            ((ItemViewHolder) holder).type_tv.setText(camera.getPlaceType());
            ((ItemViewHolder) holder).area_tv.setText(camera.getAreaName());

            ((ItemViewHolder) holder).manager_img.setOnClickListener(new View.OnClickListener() {//拨打电话提示框。。
                @Override
                public void onClick(View v) {
//                    String phoneOne = normalSmoke.getPrincipal1Phone();
//                    mShopInfoFragmentPresenter.telPhoneAction(mContext,phoneOne);
                    Intent intent=new Intent(mContext, CallManagerDialogActivity.class);
                    intent.putExtra("people1",camera.getPrincipal1());
                    intent.putExtra("people2",camera.getPrincipal2());
                    intent.putExtra("phone1",camera.getPrincipal1Phone());
                    intent.putExtra("phone2",camera.getPrincipal2Phone());
                    mContext.startActivity(intent);
                }
            });
            if (camera.getIsOnline() == 0&&camera.getVideoType()==0) {//设备不在线。。
                ((ItemViewHolder) holder).smoke_name_text.setText("摄像机："+camera.getCameraName()+"（已离线)");
                ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.RED);
            } else {//设备在线。。
                ((ItemViewHolder) holder).smoke_name_text.setText("摄像机："+camera.getCameraName());
                ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
            }

            final TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -0.2f, Animation.RELATIVE_TO_SELF, 0.0f);
            mShowAction.setDuration(500);
            ((ItemViewHolder) holder).dev_info_rela.setVisibility(View.GONE);
            ((ItemViewHolder) holder).show_info_text.setText("展开详情");
            ((ItemViewHolder) holder).show_info_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s= (String) ((ItemViewHolder) holder).show_info_text.getText();
                    if(s.equals("展开详情")){
                        ((ItemViewHolder) holder).dev_info_rela.setVisibility(View.VISIBLE);
                        ((ItemViewHolder) holder).dev_info_rela.startAnimation(mShowAction);
                        ((ItemViewHolder) holder).show_info_text.setText("收起详情");
                    }else{
                        ((ItemViewHolder) holder).dev_info_rela.setVisibility(View.GONE);
                        ((ItemViewHolder) holder).show_info_text.setText("展开详情");
                    }
                }
            });

            ((ItemViewHolder) holder).categoryGroupLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(camera.getIsOnline()==0) {//@@5.18
//                        T.show(mContext,"设备不在线", Toast.LENGTH_SHORT);
//                        return;
//                    }
                    Contact mContact = new Contact();
                    mContact.contactType = 0;
                    mContact.contactId = camera.getCameraId();
                    mContact.contactPassword = camera.getCameraPwd();
                    mContact.contactName = camera.getCameraName();
                    mContact.apModeState = 1;

                    Intent monitor = new Intent();
                    if(camera.getVideoType()==0){
                        monitor.setClass(mContext, ApMonitorActivity.class);
                    }else{
                        monitor.setClass(mContext, PreviewActivity.class);
                    }
                    monitor.putExtra("contact", mContact);
                    monitor.putExtra("connectType", ConstantValues.ConnectType.P2PCONNECT);
                    monitor.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(monitor);
                }
            });

            if (camera.getIsOnline() == 0&&camera.getVideoType()==0) {//设备不在线。。
                    ((ItemViewHolder) holder).online_state_image.setImageResource(R.drawable.sblb_lixian);
                } else {//设备在线。。
                    ((ItemViewHolder) holder).online_state_image.setImageResource(R.drawable.dev_online);
                    ((ItemViewHolder) holder).smoke_name_text.setTextColor(Color.BLACK);
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
        return listCamera.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.smoke_name_text)
        TextView smoke_name_text;
        @Bind(R.id.mac_tv)
        TextView mac_tv;
        @Bind(R.id.repeater_tv)
        TextView repeater_tv;
        @Bind(R.id.area_tv)
        TextView area_tv;
        @Bind(R.id.type_tv)
        TextView type_tv;
        @Bind(R.id.address_tv)
        TextView address_tv;
        @Bind(R.id.manager_img)
        TextView manager_img;
        @Bind(R.id.show_info_text)
        TextView show_info_text;
        @Bind(R.id.dev_info_rela)
        RelativeLayout dev_info_rela;
        @Bind(R.id.online_state_image)
        ImageView online_state_image;

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
