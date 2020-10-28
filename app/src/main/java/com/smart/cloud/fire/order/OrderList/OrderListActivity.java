package com.smart.cloud.fire.order.OrderList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.order.JobOrder;
import com.smart.cloud.fire.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class OrderListActivity extends MvpActivity<OrderListPresenter> implements OrderListEntity {

    Context mContext;
    private OrderListPresenter mPresenter;
    private LinearLayoutManager linearLayoutManager;
    private List<JobOrder> list;
    private OrderListAdapter mAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        mContext = this;
        refreshListView();
        mPresenter.getAllDev(MyApp.getUserID());
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
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //下拉刷新。。
        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getAllDev(MyApp.getUserID());
            }
        });
    }

    @Override
    protected OrderListPresenter createPresenter() {
        mPresenter = new OrderListPresenter(this);
        return mPresenter;
    }


    @Override
    public void getDataSuccess(List<JobOrder> smokeList) {
        if(smokeList.size()==0){
            T.showShort(mContext,"无工单信息");
        }else{
            list = new ArrayList<>();
            list.addAll(smokeList);
            mAdapter = new OrderListAdapter(mContext, list);
            recyclerView.setAdapter(mAdapter);
        }
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
