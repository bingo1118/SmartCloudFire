package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/7/29.
 */
public class ShopInfoFragmentAdapter extends BaseAdapter {
    private Context mContext;
    private List<Smoke> listNormalSmoke;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;

    public ShopInfoFragmentAdapter(Context mContext, List<Smoke> listNormalSmoke,ShopInfoFragmentPresenter mShopInfoFragmentPresenter) {
        this.listNormalSmoke = listNormalSmoke;
        this.mContext = mContext;
        this.mShopInfoFragmentPresenter = mShopInfoFragmentPresenter;
    }

    @Override
    public int getCount() {
        return listNormalSmoke.size();
    }

    @Override
    public Object getItem(int position) {
        return listNormalSmoke.get(position);
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
        holder.repeaterRela.setVisibility(View.VISIBLE);
        final Smoke normalSmoke = listNormalSmoke.get(position);
        int netStates = normalSmoke.getNetState();
        if (netStates == 0) {
            holder.categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
            holder.groupImage.setImageResource(R.drawable.yg_zj_lx);
        } else {
            holder.categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
            holder.groupImage.setImageResource(R.drawable.yg_zj_zx);
        }
        holder.groupTvAddress.setText(normalSmoke.getAddress());
        holder.groupTv.setText(normalSmoke.getName());
        holder.repeaterNameTv.setText(normalSmoke.getPlaceType());
        holder.repeaterMacTv.setText(normalSmoke.getAreaName());
        holder.groupPrincipal1.setText(normalSmoke.getPrincipal1());
        holder.groupPhone1.setText(normalSmoke.getPrincipal1Phone());
        holder.groupPrincipal2.setText(normalSmoke.getPrincipal2());
        holder.groupPhone2.setText(normalSmoke.getPrincipal2Phone());
        holder.repeaterTv2.setText(normalSmoke.getRepeater());
        holder.groupPhone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneOne = normalSmoke.getPrincipal1Phone();
                mShopInfoFragmentPresenter.telPhoneAction(mContext,phoneOne);
            }
        });
        holder.groupPhone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneTwo = normalSmoke.getPrincipal2Phone();
                mShopInfoFragmentPresenter.telPhoneAction(mContext,phoneTwo);
            }
        });
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.group_image)
        ImageView groupImage;
        @Bind(R.id.group_tv)
        TextView groupTv;
        @Bind(R.id.repeater_tv2)
        TextView repeaterTv2;
        @Bind(R.id.repeater_rela)
        RelativeLayout repeaterRela;
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
