package com.smart.cloud.fire.order.OrderList;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.order.JobOrder;
import com.smart.cloud.fire.order.OrderInfoDetail.OrderInfoActivity;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;


    private Context mContext;
    private List<JobOrder> mList;

    public OrderListAdapter(Context mContext , List<JobOrder> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.order_list_item, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            OrderListAdapter.ItemViewHolder viewHolder = new OrderListAdapter.ItemViewHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = LayoutInflater.from(mContext).inflate(R.layout.recycler_load_more_layout, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            OrderListAdapter.FootViewHolder footViewHolder = new OrderListAdapter.FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final JobOrder mJobOrder = mList.get(position);
            ((ItemViewHolder) holder).title_tv.setText(mJobOrder.getTitle());
            ((ItemViewHolder) holder).content_tv.setText(mJobOrder.getDescription());
            ((ItemViewHolder) holder).user_tv.setText(mJobOrder.getDealUserName());
            String state="";
            switch (mJobOrder.getState()){
                case 1:
                    state="未派单";
                    break;
                case 2:
                    state="未接单(点击接单)";
                    break;
                case 3:
                    state="待复核";
                    break;
                case 4:
                    state="已处理";
                    break;
                case 5:
                    state="处理中";
                    break;
                case 6:
                    state="上报审核";
                    break;
            }
            ((ItemViewHolder) holder).state_tv.setText(state);
            ((ItemViewHolder) holder).time_tv.setText(mJobOrder.getAddTime());
            ((ItemViewHolder) holder).line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mJobOrder.getState()==2){
                        VolleyHelper helper = VolleyHelper.getInstance(mContext);
                        RequestQueue mQueue = helper.getRequestQueue();
                        String url = ConstantValues.SERVER_IP_NEW + "receiveOrderInfo?jkey=" + mJobOrder.getJkey()
                                + "&userId=" + MyApp.getUserID()
                                + "&sendUser="+mJobOrder.getPrincipalUser();

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            int errorCode = response.getInt("errorcode");
                                            String error = response.getString("error");
                                            if (errorCode == 0) {
                                                T.showShort(mContext, "接单成功");
                                                Intent intent=new Intent(mContext, OrderInfoActivity.class);
                                                intent.putExtra("order",mJobOrder);
                                                mContext.startActivity(intent);
                                            } else {
                                                T.showShort(mContext, error);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                T.showShort(mContext, "网络错误");
                            }
                        });
                        mQueue.add(jsonObjectRequest);
                    }else{
                        Intent intent=new Intent(mContext, OrderInfoActivity.class);
                        intent.putExtra("order",mJobOrder);
                        mContext.startActivity(intent);
                    }
                }
            });
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

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.title_tv)
        TextView title_tv;
        @Bind(R.id.content_tv)
        TextView content_tv;
        @Bind(R.id.user_tv)
        TextView user_tv;
        @Bind(R.id.state_tv)
        TextView state_tv;
        @Bind(R.id.time_tv)
        TextView time_tv;
        @Bind(R.id.line)
        LinearLayout line;

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

    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
