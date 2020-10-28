package com.smart.cloud.fire.order.DealOrder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fire.cloud.smart.com.smartcloudfire.R;

public class DealerAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater li;
    private ArrayList<DealerBean> dataList;

    public DealerAdapter(Context ctx,ArrayList<DealerBean> dataList) {
        this.ctx = ctx;
        this.li = LayoutInflater.from(ctx);
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public DealerBean getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_dealer, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();// get convertView's holder

        holder.name_tv.setText(getItem(position).getNamed());
        holder.id_tv.setText(getItem(position).getUserId());

        return convertView;
    }

    class ViewHolder {
        TextView name_tv;
        TextView id_tv;


        public ViewHolder(View convertView){
            name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            id_tv = (TextView) convertView.findViewById(R.id.id_tv);
            convertView.setTag(this);
        }
    }


}


