package com.smart.cloud.fire.order.OrderInfoDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.order.DealOrder.ChangeDealerActivity;
import com.smart.cloud.fire.order.DealOrder.CheakOrderActivity;
import com.smart.cloud.fire.order.DealOrder.DealOrderActivity;
import com.smart.cloud.fire.order.JobOrder;
import com.smart.cloud.fire.order.OrderInfo;
import com.smart.cloud.fire.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class OrderInfoActivity extends MvpActivity<OrderInfoPresenter> implements OrderInfoEntity {

    Context mContext;
    private OrderInfoPresenter mPresenter;
    private LinearLayoutManager linearLayoutManager;
    private List<OrderInfo> list;
    private OrderInfoAdapter mAdapter;

    JobOrder order;

    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.content_tv)
    TextView content_tv;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.commit_btn)
    Button commit_btn;
    @Bind(R.id.change_btn)
    Button change_btn;
    @Bind(R.id.cheak_btn)
    Button cheak_btn;
    @Bind(R.id.upload_btn)
    Button upload_btn;

    int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        ButterKnife.bind(this);
        mContext=this;
        refreshListView();

        order= (JobOrder) getIntent().getSerializableExtra("order");



        if(order!=null){
            title_tv.setText(order.getTitle());
        }
        state=order.getState();
        initView();
        mPresenter.getOrderInfo(order.getJkey());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getOrderInfo(order.getJkey());
    }

    private void initView() {
        if(state==2&&(MyApp.getPrivilege()==2||MyApp.getPrivilege()==3)){
            commit_btn.setVisibility(View.VISIBLE);
        }else{
            commit_btn.setVisibility(View.GONE);
        }
        if(state==2&&MyApp.getPrivilege()!=2){
            change_btn.setVisibility(View.VISIBLE);
        }else{
            change_btn.setVisibility(View.GONE);
        }
        if(state==3&&MyApp.getPrivilege()==3){
            cheak_btn.setVisibility(View.VISIBLE);
            upload_btn.setVisibility(View.VISIBLE);
        }else{
            cheak_btn.setVisibility(View.GONE);
            upload_btn.setVisibility(View.GONE);
        }
        if(order!=null){
            content_tv.setText(order.getDescription());
        }
    }

    @Override
    protected OrderInfoPresenter createPresenter() {
        mPresenter = new OrderInfoPresenter(this);
        return mPresenter;
    }


    private void refreshListView() {
        //设置刷新时动画的颜色，可以设置4个
        swipereFreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipereFreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipereFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //下拉刷新。。
        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getOrderInfo(order.getJkey());
            }
        });
    }

    @OnClick({R.id.commit_btn,R.id.change_btn,R.id.cheak_btn,})
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.cheak_btn:
                intent=new Intent(OrderInfoActivity.this, CheakOrderActivity.class);
                intent.putExtra("order",order);
                startActivity(intent);
                break;
            case R.id.commit_btn:
                intent=new Intent(OrderInfoActivity.this, DealOrderActivity.class);
                intent.putExtra("order",order);
                startActivity(intent);
                break;
            case R.id.change_btn:
                intent=new Intent(OrderInfoActivity.this, ChangeDealerActivity.class);
                intent.putExtra("order",order);
                startActivity(intent);
                break;
        }
    }



    @Override
    public void getDataSuccess(List<OrderInfo> smokeList) {
        if(smokeList.size()==0){
            T.showShort(mContext,"无工单信息");
        }else{
            list = new ArrayList<>();
            list.addAll(smokeList);
            mAdapter = new OrderInfoAdapter(mContext, list);
            recyclerView.setAdapter(mAdapter);
        }
        state=list.get(list.size()-1).getState();
        initView();
        swipereFreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void unSubscribe(String type) {

    }

    @Override
    public void getDataFail(String msg) {

    }
}
