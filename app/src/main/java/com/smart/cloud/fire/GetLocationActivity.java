package com.smart.cloud.fire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.smart.cloud.fire.global.MyApp;

import fire.cloud.smart.com.smartcloudfire.R;


public class GetLocationActivity extends Activity implements View.OnClickListener{

    Context mContext;
    MapView mMapView;
    private LatLng selectedLatlng;
    private String location_address="";
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    /**
     * 定位的监听器
     */
    public MyLocationListener mMyLocationListener;
    /**
     * 当前定位的模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    /***
     * 是否是第一次定位
     */
    private volatile boolean isFristLocation = true;
    Button btn_confirm;
    ImageView image_location;
    GeoCoder geoCoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        mContext=getApplicationContext();
        btn_confirm=(Button)findViewById(R.id.confirm_btn);
        image_location=(ImageView)findViewById(R.id.location_image);
        btn_confirm.setOnClickListener(this);
        image_location.setOnClickListener(this);

        mMapView=(MapView)findViewById(R.id.bmapView) ;
        mBaiduMap = mMapView.getMap();// 获得MapView
        initMyLocation();
        geoCoder = GeoCoder.newInstance();
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //获取经纬度
                final double latitude = latLng.latitude;
                final double longitude = latLng.longitude;
                System.out.println("latitude=" + latitude + ",longitude=" + longitude);
                //先清除图层
                mBaiduMap.clear();
                mBaiduMap.setMyLocationEnabled(false);//@@6.20关闭定位图层
                // 定义Maker坐标点
                LatLng point = new LatLng(latitude, longitude);
                // 构建MarkerOption，用于在地图上添加Marker
                View viewA = LayoutInflater.from(MyApp.app).inflate(
                        R.layout.image_mark, null);
                BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                        .fromView(viewA);
                MarkerOptions options = new MarkerOptions().position(point)
                        .icon(mCurrentMarker);
                // 在地图上添加Marker，并显示
                mBaiduMap.addOverlay(options);
                findAddress(point);
            }


            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

    }

    private void findAddress(final LatLng latLng) {
        this.selectedLatlng=latLng;
        //实例化一个地理编码查询对象
//        GeoCoder geoCoder = GeoCoder.newInstance();
        //设置反地理编码位置坐标
        ReverseGeoCodeOption op = new ReverseGeoCodeOption();
        op.location(latLng);
        //发起反地理编码请求(经纬度->地址信息)
        geoCoder.reverseGeoCode(op);
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                //获取点击的坐标地址
                String address = arg0.getAddress();
                location_address=address;
                Toast.makeText(MyApp.app,"地址:"+address+"\n"+"纬度:"+latLng.latitude+"\n"+"经度:"+latLng.longitude,Toast.LENGTH_LONG).show();
                System.out.println("address="+address);
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {
            }
        });
    }

    private void initMyLocation()
    {
        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        // 设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onStart()
    {
        // 开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
        {
            mLocationClient.start();
        }
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        // 关闭图层定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();

        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_btn:
                Intent intent = new Intent();
                //把返回数据存入Intent
                Bundle bundle=new Bundle();
                bundle.putString("address", location_address);
                if(selectedLatlng!=null){
                    bundle.putDouble("lat",selectedLatlng.latitude);
                    bundle.putDouble("lon",selectedLatlng.longitude);
                }
                intent.putExtra("data",bundle);
                this.setResult(this.RESULT_OK,intent);
                finish();
                break;
            case R.id.location_image:
                mBaiduMap.clear();
                mBaiduMap.setMyLocationEnabled(true);
                isFristLocation=true;
                initMyLocation();
                break;
        }

    }

    public class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 设置自定义图标
            View viewA = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.image_mark, null);
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromView(viewA);
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
            // 第一次定位时，将地图位置移动到当前位置
            if (isFristLocation)
            {
                isFristLocation = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//                mBaiduMap.animateMapStatus(u);
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(ll).zoom(18).build()));//@@6.28
                findAddress(new LatLng(location.getLatitude(),
                        location.getLongitude()));//@@6.20
            }
        }

    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        if(mLocationClient!=null){
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
        }
        geoCoder.destroy();
        super.onDestroy();
    }

}



