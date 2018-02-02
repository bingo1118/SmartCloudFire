package com.smart.cloud.fire.activity.Host;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokeActivity;
import com.smart.cloud.fire.adapter.HostAdapter;
import com.smart.cloud.fire.adapter.ShopSmokeAdapter;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class HostActivity extends Activity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;

    private String userID;
    private int privilege;
    private Context mContext;
    private int loadMoreCount;
    private boolean research = false;
    private LinearLayoutManager linearLayoutManager;
    private HostAdapter hostAdapter;
    private String page;
    private int lastVisibleItem;
    private List<Smoke> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        ButterKnife.bind(this);
        mContext=this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        refreshListView();
        list = new ArrayList<>();
        page = "1";
        getData(page);
    }

    private void refreshListView() {
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
                page = "1";
                getData(page);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(research){//@@9.5 条件查询分页
                    if(hostAdapter==null){
                        return;
                    }
                    int count = hostAdapter.getItemCount();
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == count) {
                        if(loadMoreCount>=20){
                            page = Integer.parseInt(page) + 1 + "";
                            getData(page);
                        }else{
                            T.showShort(mContext,"已经没有更多数据了");
                        }
                    }
                    return;
                }//@@9.5
                if(hostAdapter==null){
                    return;
                }
                int count = hostAdapter.getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == count) {
                    if(loadMoreCount>=20){
                        page = Integer.parseInt(page) + 1 + "";
                        getData(page);
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

    private void getData(String page1){
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
//        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url= ConstantValues.SERVER_IP_NEW+"getRepeaterInfo?userId="+userID+"&privilege="+privilege+"&page="+page1;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode=response.getInt("errorCode");
                            if(errorCode==0){
                                JSONArray jsonArray=response.getJSONArray("repeater");
                                List<Smoke> list_temp=new ArrayList<>();
                                for(int i=0;i<jsonArray.length();i++){
                                    Smoke temp=new Smoke();
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String hoststate=jsonObject.getString("hoststate");
                                    if(hoststate==null||hoststate.equals("")){
                                        temp.setElectrState(0);
                                    }else{
                                        temp.setElectrState(Integer.parseInt(hoststate));
                                    }
                                    temp.setRepeater(jsonObject.getString("repeaterMac"));
                                    list_temp.add(temp);
                                }
                                if(page.equals("1")){
                                    loadMoreCount = list_temp.size();
                                    list.clear();
                                    list.addAll((List<Smoke>)list_temp);
                                    hostAdapter = new HostAdapter(mContext, list);
                                    recyclerView.setAdapter(hostAdapter);
                                    swipereFreshLayout.setRefreshing(false);
                                }else{
                                    loadMoreCount = list_temp.size();
                                    list.addAll((List<Smoke>)list_temp);
                                    hostAdapter.changeMoreStatus(ShopSmokeAdapter.LOADING_MORE);
                                }
                            }else{
                                T.showShort(mContext,"无数据");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(mContext,"网络错误");
            }
        });
        mQueue.add(jsonObjectRequest);
    }
}
