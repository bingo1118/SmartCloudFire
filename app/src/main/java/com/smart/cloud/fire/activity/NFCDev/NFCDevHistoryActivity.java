package com.smart.cloud.fire.activity.NFCDev;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.smart.cloud.fire.activity.Map.MapActivity;
import com.smart.cloud.fire.adapter.NFCDevAdapter;
import com.smart.cloud.fire.adapter.NFCHistoryAdapter;
import com.smart.cloud.fire.adapter.ShopCameraAdapter;
import com.smart.cloud.fire.adapter.ShopSmokeAdapter;
import com.smart.cloud.fire.adapter.WiredSmokeHistoryAdapter;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.electricChangeHistory.HistoryBean;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment.WiredSmokeHistory;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.utils.VolleyHelper;
import com.smart.cloud.fire.view.XCDropDownListViewMapSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class NFCDevHistoryActivity extends Activity{

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private LinearLayoutManager linearLayoutManager;
    private NFCHistoryAdapter nfcHistoryAdapter;//@@6.29
    private Context mContext;
    private List<WiredSmokeHistory> list;
    private String UID;
    @Bind(R.id.title_tv)
    TextView title_tv;

    private int lastVisibleItem;
    private int loadMoreCount;
    private boolean research = false;
    private int page;
    private String userID;
    private int privilege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc2);

        ButterKnife.bind(this);
        mContext=this;
        UID = getIntent().getStringExtra("uid");
        title_tv.setText(getIntent().getStringExtra("name"));
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        list = new ArrayList<>();
        refreshListView();
        getNFCInfo(UID,1);
        page=1;
    }


    private void getNFCInfo(String uid,int page) {
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
//        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url="";
        if(uid.length()>0){
            url= ConstantValues.SERVER_IP_NEW+"getNFCRecord?uid="+uid+"&page="+page;
        }else{
            url= ConstantValues.SERVER_IP_NEW+"getNFCRecord?uid="+uid+"&page="+page+"&userId="+userID+"&privilege="+privilege;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode=response.getInt("errorCode");
                            if(errorCode==0){
                                List<WiredSmokeHistory> listTemp=new ArrayList<>();
                                JSONArray jsonArray=response.getJSONArray("nfcList");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    WiredSmokeHistory wiredSmokeHistory=new WiredSmokeHistory();
                                    wiredSmokeHistory.setFaultTime(jsonObject.getString("addTime"));
                                    wiredSmokeHistory.setFaultInfo(jsonObject.getString("memo"));
                                    wiredSmokeHistory.setFaultType(jsonObject.getString("devicestate"));
                                    wiredSmokeHistory.setPhoto1(jsonObject.getString("photo1"));//@@9.28
                                    wiredSmokeHistory.setUserid(jsonObject.getString("userId"));
                                    wiredSmokeHistory.setAreaName(jsonObject.getString("areaName"));
                                    wiredSmokeHistory.setDeviceName(jsonObject.getString("deviceName"));
//                                    list.add(0,wiredSmokeHistory);
                                    listTemp.add(wiredSmokeHistory);
                                }
                                if(list==null||list.size()==0){
                                    getDataSuccess(listTemp);
                                }else if(list!=null&&list.size()>=20){
                                    onLoadingMore(listTemp);
                                }
                            }else{
                                toast("获取失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast("网络错误");
            }
        });
        mQueue.add(jsonObjectRequest);
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
                list.clear();
                getNFCInfo(UID,1);
                page=1;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (research) {
                    if(nfcHistoryAdapter!=null){
                        nfcHistoryAdapter.changeMoreStatus(ShopCameraAdapter.NO_DATA);
                    }
                    return;
                }
                if(nfcHistoryAdapter==null){
                    return;
                }
                int count = nfcHistoryAdapter.getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == count) {
                    if(loadMoreCount>=20){
                        page = page+ 1;
                        getNFCInfo(UID,page);
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

    public void getDataSuccess(List<WiredSmokeHistory> listtemp) {
        if(listtemp.size()==0){
            toast("无数据");
        }
        loadMoreCount = listtemp.size();
        list.clear();
        list.addAll(listtemp);
        nfcHistoryAdapter = new NFCHistoryAdapter(mContext, this.list);//@@6.29
        recyclerView.setAdapter(nfcHistoryAdapter);
        swipereFreshLayout.setRefreshing(false);
    }


    public void onLoadingMore(List<?> smokeList) {

        loadMoreCount = smokeList.size();
        list.addAll((List<WiredSmokeHistory>)smokeList);
        nfcHistoryAdapter.changeMoreStatus(ShopSmokeAdapter.LOADING_MORE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}