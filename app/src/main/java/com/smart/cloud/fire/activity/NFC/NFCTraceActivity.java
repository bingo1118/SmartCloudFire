package com.smart.cloud.fire.activity.NFC;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.MyOverlayManager;
import com.smart.cloud.fire.activity.NFCDev.NFCRecordBean;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class NFCTraceActivity extends Activity {

    Context mContext;
    @Bind(R.id.bmapView)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    List<NFCRecordBean> traceNFCItems;

    String areaId;
    String begintime;
    String endtime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfctrace);
        mContext=this;

        ButterKnife.bind(this);
        mBaiduMap = mMapView.getMap();// 获得MapView

        areaId=getIntent().getStringExtra("areaId");
        begintime=getIntent().getStringExtra("begintime");
        endtime=getIntent().getStringExtra("endtime");
        initTrace();
    }

    private void initTrace() {
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url= ConstantValues.SERVER_IP_NEW+"getNFCTrace?areaId="+areaId+"&begintime="+begintime+"&endtime="+endtime;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("errorCode")=="0"){
                                JSONArray trace=jsonObject.getJSONArray("nfcTraceList");
                                for(int i=0;i<trace.length();i++){
                                    if(traceNFCItems==null){
                                        traceNFCItems=new ArrayList<>();
                                    }
                                    JSONObject temp=trace.getJSONObject(i);
                                    NFCRecordBean nfcRecordBean=new NFCRecordBean();
                                    nfcRecordBean.setUid(temp.getString("uid"));
                                    nfcRecordBean.setLongitude(temp.getString("longitude"));
                                    nfcRecordBean.setLatitude(temp.getString("latitude"));
                                    nfcRecordBean.setMemo(temp.getString("memo"));
                                    nfcRecordBean.setDevicestate(temp.getString("devicestate"));
                                    nfcRecordBean.setAddTime(temp.getString("addTime"));
                                    traceNFCItems.add(nfcRecordBean);
                                }
                                if(traceNFCItems!=null){
                                    getNFCDataSuccess(traceNFCItems);
                                }else{
                                    Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(stringRequest);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }

    @Override
    public void onResume() {
        mMapView.setVisibility(View.VISIBLE);
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.setVisibility(View.INVISIBLE);
        mMapView.onPause();
        super.onPause();
    }

    private void getNFCDataSuccess(List<NFCRecordBean> traceNFCItems) {
        mBaiduMap.clear();

        if(traceNFCItems.size()<2){
            Toast.makeText(mContext,"无轨迹",Toast.LENGTH_SHORT).show();
            return;
        }

        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < traceNFCItems.size(); i++) {
            LatLng latLng = new LatLng(Double.parseDouble(traceNFCItems.get(i)
                    .getLatitude()), Double.parseDouble(traceNFCItems.get(i)
                    .getLongitude()));
            points.add(latLng);
        }


        OverlayOptions ooPolyline = new PolylineOptions()
                .width(10)
                .color(getResources().getColor(R.color.login_btn))
                .points(points);
        mBaiduMap.addOverlay(ooPolyline);

        if (points.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : points) {
                builder = builder.include(latLng);
            }
            LatLngBounds latlngBounds = builder.build();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds,mMapView.getWidth(),mMapView.getHeight());
            mBaiduMap.animateMapStatus(u);
        }
        if(points.size()==1){
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20.0f));
        }
    }
}

