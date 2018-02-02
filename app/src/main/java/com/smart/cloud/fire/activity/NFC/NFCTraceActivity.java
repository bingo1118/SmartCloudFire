package com.smart.cloud.fire.activity.NFC;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.MyOverlayManager;
import com.smart.cloud.fire.activity.NFCDev.NFCRecordBean;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.mvp.fragment.MapFragment.BaiduMapUtil;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.VolleyHelper;

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
    @Bind(R.id.id_marker_info)
    RelativeLayout mMarkerInfoLy ;
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
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
//        RequestQueue mQueue = Volley.newRequestQueue(mContext);
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
                                    nfcRecordBean.setUserId(temp.getString("userId"));
                                    nfcRecordBean.setDeviceName(temp.getString("deviceName"));
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
        mMapView.onDestroy();
        ButterKnife.unbind(this);
        super.onDestroy();
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

        final List<LatLng> points = new ArrayList<LatLng>();

        OverlayOptions overlayOptions = null;
        Marker marker = null;
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.view_nfc_hg, null);
        BitmapDescriptor view_nfc_hg = BitmapDescriptorFactory
                .fromView(view);
        View view_bhg = LayoutInflater.from(mContext).inflate(
                R.layout.view_nfc_bhg, null);
        BitmapDescriptor view_nfc_bhg = BitmapDescriptorFactory
                .fromView(view_bhg);
        for (int i = 0; i < traceNFCItems.size(); i++) {
            NFCRecordBean info=traceNFCItems.get(i);
            LatLng latLng = new LatLng(Double.parseDouble(info
                    .getLatitude()), Double.parseDouble(info
                    .getLongitude()));
            points.add(latLng);
            if(info.getDevicestate().equals("1")){
                overlayOptions = new MarkerOptions().position(latLng)
                        .icon(view_nfc_hg).zIndex(5);
            }else{
                overlayOptions = new MarkerOptions().position(latLng)
                        .icon(view_nfc_bhg).zIndex(5);
            }
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);

        }
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(final Marker marker)
            {
                //获得marker中的数据
                NFCRecordBean info = (NFCRecordBean) marker.getExtraInfo().get("info");

                //设置详细信息布局为可见
                mMarkerInfoLy.setVisibility(View.VISIBLE);
                //根据商家信息为详细信息布局设置信息
                popupInfo(mMarkerInfoLy, info);
                return true;
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener()
        {

            @Override
            public boolean onMapPoiClick(MapPoi arg0)
            {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0)
            {
                mMarkerInfoLy.setVisibility(View.GONE);
                mBaiduMap.hideInfoWindow();
            }
        });
        //@@10.16 优化缩放地图，合理显示所有覆盖物
        MapStatus.Builder builder = new MapStatus.Builder();
        mBaiduMap.addOverlay(new BaiduMapUtil().Polyline(points));
        //将所有的坐标显示出来的合理视图
        mBaiduMap.animateMapStatus(new BaiduMapUtil().setLatLngBounds(points, mMapView));
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mBaiduMap.animateMapStatus(new BaiduMapUtil().setLatLngBounds(points, mMapView));
            }
        });

//        for (int i = 0; i < traceNFCItems.size(); i++) {
//            LatLng latLng = new LatLng(Double.parseDouble(traceNFCItems.get(i)
//                    .getLatitude()), Double.parseDouble(traceNFCItems.get(i)
//                    .getLongitude()));
//            points.add(latLng);
//        }
//
//        OverlayOptions ooPolyline = new PolylineOptions()
//                .width(10)
//                .color(getResources().getColor(R.color.login_btn))
//                .points(points);
//        mBaiduMap.addOverlay(ooPolyline);
//
//        if (points.size() > 0) {
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            for (LatLng latLng : points) {
//                builder = builder.include(latLng);
//            }
//            LatLngBounds latlngBounds = builder.build();
//            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds,mMapView.getWidth(),mMapView.getHeight());
//            mBaiduMap.setMapStatus(u);
//        }
//        if(points.size()==1){
//            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20.0f));
//        }
    }

    protected void popupInfo(RelativeLayout mMarkerLy, NFCRecordBean info)
    {
        ViewHolder viewHolder = null;
        if (mMarkerLy.getTag() == null)
        {
            viewHolder = new ViewHolder();
            viewHolder.infoName = (TextView) mMarkerLy
                    .findViewById(R.id.info_name);
            viewHolder.infoState = (TextView) mMarkerLy
                    .findViewById(R.id.info_state);
            viewHolder.infoUserId = (TextView) mMarkerLy
                    .findViewById(R.id.info_userid);
            viewHolder.infoTime = (TextView) mMarkerLy
                    .findViewById(R.id.info_time);
            viewHolder.infoMemo = (TextView) mMarkerLy
                    .findViewById(R.id.info_memo);

            mMarkerLy.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) mMarkerLy.getTag();
        viewHolder.infoName.setText(info.getDeviceName());
        switch (info.getDevicestate()){
            case "0":
                viewHolder.infoState.setText("待检");
                break;
            case "1":
                viewHolder.infoState.setText("合格");
                break;
            case "2":
                viewHolder.infoState.setText("不合格");
                break;
            default:
                viewHolder.infoState.setText("待检");
                break;
        }
        viewHolder.infoUserId.setText(info.getUserId());
        viewHolder.infoTime.setText(info.getAddTime());
        viewHolder.infoMemo.setText(info.getMemo());
    }

    /**
     * 复用弹出面板mMarkerLy的控件
     *
     * @author zhy
     *
     */
    private class ViewHolder
    {
        TextView infoName;
        TextView infoState;
        TextView infoUserId;
        TextView infoTime;
        TextView infoMemo;
    }
}

