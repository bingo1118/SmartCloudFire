package com.smart.cloud.fire.mvp.BigDataDetail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

import com.smart.cloud.fire.activity.AllSmoke.AllSmokeActivity;
import com.smart.cloud.fire.adapter.BigDataDetailAdapter;
import com.smart.cloud.fire.adapter.ShopSmokeAdapter;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class BigDataDetailActivity extends MvpActivity<BigDataDetailPresenter> implements BigDataDetailView{

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;

    private BigDataDetailPresenter mPresenter;
    private LinearLayoutManager linearLayoutManager;
    private BigDataDetailAdapter shopSmokeAdapter;
    private int lastVisibleItem;
    private Context mContext;
    private List<Smoke> list;
    private int loadMoreCount;
    private boolean research = false;
    private String page;
    private String userID;
    private int privilege;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_data_detail);

        ButterKnife.bind(this);
        mContext=this;
        type=getIntent().getStringExtra("type");
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        page = "1";
        list = new ArrayList<>();
        refreshListView();
        mPresenter.getAllSmoke(userID, privilege + "", page, list, 1,false,type);
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
            refreshView();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(shopSmokeAdapter==null){
                    return;
                }
                int count = shopSmokeAdapter.getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == count) {
                    if(loadMoreCount>=20){
                        page = Integer.parseInt(page) + 1 + "";
                        mPresenter.getAllSmoke(userID, privilege + "", page, list, 1,false,type);//@@7.17
                    }else{
                        T.showShort(mContext,"已经没有更多数据了");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    public void refreshView() {
        page = "1";
        list.clear();
        mPresenter.getAllSmoke(userID, privilege + "", page, list, 1,true,type);
    }

    @Override
    protected BigDataDetailPresenter createPresenter() {
        mPresenter=new BigDataDetailPresenter(this);
        return mPresenter;
    }

    @Override
    public void getDataSuccess(List<?> smokeList,boolean search) {
        research = search;
        if(search!=false&&!page.equals("1")){
            page="1";
        }//@@9.5
        loadMoreCount = smokeList.size();
        list.clear();
        list.addAll((List<Smoke>)smokeList);
        shopSmokeAdapter = new BigDataDetailAdapter(mContext, list);
        recyclerView.setAdapter(shopSmokeAdapter);
        swipereFreshLayout.setRefreshing(false);
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext, msg);
        swipereFreshLayout.setRefreshing(false);
        if(shopSmokeAdapter!=null){
            shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.NO_DATA);
        }
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingMore(List<?> smokeList) {
        loadMoreCount = smokeList.size();
        list.addAll((List<Smoke>)smokeList);
        shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.LOADING_MORE);
    }
}
