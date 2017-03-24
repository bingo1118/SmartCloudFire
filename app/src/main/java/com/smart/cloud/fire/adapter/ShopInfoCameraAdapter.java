package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.ui.ApMonitorActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ShopInfoCameraAdapter extends BaseAdapter {
    private Context mContext;
    private List<Camera> listCamera;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;

    public ShopInfoCameraAdapter(Context mContext, List<Camera> listCamera, ShopInfoFragmentPresenter mShopInfoFragmentPresenter) {
        this.listCamera = listCamera;
        this.mContext = mContext;
        this.mShopInfoFragmentPresenter = mShopInfoFragmentPresenter;
    }

    @Override
    public int getCount() {
        return listCamera.size();
    }

    @Override
    public Object getItem(int position) {
        return listCamera.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_info_adapter, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Camera camera = listCamera.get(position);
        holder.groupTvAddress.setText(camera.getCameraAddress());
        holder.groupTv.setText(camera.getCameraName());
        holder.repeaterNameTv.setText(camera.getPlaceType());
        holder.repeaterMacTv.setText(camera.getAreaName());
        holder.groupPrincipal1.setText(camera.getPrincipal1());
        holder.groupPhone1.setText(camera.getPrincipal1Phone());
        holder.groupPrincipal2.setText(camera.getPrincipal2());
        holder.groupPhone2.setText(camera.getPrincipal2Phone());
        holder.groupImage.setImageResource(R.drawable.yg_ygtubiao_sxj);
        holder.groupPhone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneOne = camera.getPrincipal1Phone();
                mShopInfoFragmentPresenter.telPhoneAction(mContext, phoneOne);

            }
        });
        holder.groupPhone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneTwo = camera.getPrincipal2Phone();
                mShopInfoFragmentPresenter.telPhoneAction(mContext, phoneTwo);
            }
        });
        holder.categoryGroupLin.setOnClickListener(new View.OnClickListener() {
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
        return convertView;
    }

    static class ViewHolder {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
