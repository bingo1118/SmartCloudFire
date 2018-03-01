package com.smart.cloud.fire.mvp.ChuangAn;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.smart.cloud.fire.adapter.ChuangAnAdapter;
import com.smart.cloud.fire.adapter.ElectricActivityAdapterTest;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ChuangAnValue;
import com.smart.cloud.fire.global.ElectricValue;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.LineChart.LineChartActivity;
import com.smart.cloud.fire.mvp.electric.ElectricPresenter;
import com.smart.cloud.fire.mvp.electric.ElectricView;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ChuangAnActivity extends MvpActivity<ChuangAnPresenter> implements ChuangAnView {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_fresh_layout)
    SwipeRefreshLayout swipeFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private ChuangAnPresenter mPresenter;
    private ChuangAnAdapter adapter;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;
    private String electricMac;
    private String userID;
    private int privilege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuang_an);
        mContext=this;
        electricMac = getIntent().getExtras().getString("Mac");
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        ButterKnife.bind(this);
        refreshListView();
        mPresenter.getOneElectricInfo(userID,privilege+"",electricMac,false);
    }

    private void refreshListView() {
        //设置刷新时动画的颜色，可以设置4个
        swipeFreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeFreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getOneElectricInfo(userID,privilege+"",electricMac,true);
            }
        });
    }

    @Override
    protected ChuangAnPresenter createPresenter() {
        mPresenter = new ChuangAnPresenter(this);
        return mPresenter;
    }

    @Override
    public void getDataSuccess(List<ChuangAnValue.ChuangAnValueBean> smokeList) {
        if(smokeList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
        }//@@7.7
        adapter = new ChuangAnAdapter(mContext, smokeList, mPresenter);
        recyclerView.setAdapter(adapter);
        swipeFreshLayout.setRefreshing(false);
        adapter.setOnItemClickListener(new ChuangAnAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view, ChuangAnValue.ChuangAnValueBean data){
                Intent intent = new Intent(mContext, LineChartActivity.class);
                intent.putExtra("electricMac",electricMac);
                intent.putExtra("electricNum",data.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void getDataFail(String msg) {
        swipeFreshLayout.setRefreshing(false);
        T.showShort(mContext,msg);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }
}
