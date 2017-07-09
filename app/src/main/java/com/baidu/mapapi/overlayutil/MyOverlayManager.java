package com.baidu.mapapi.overlayutil;

/**
 * Created by Administrator on 2016/7/28.
 */

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.MapFragment.MapFragmentPresenter;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.ArrayList;
import java.util.List;

public class MyOverlayManager extends OverlayManager {
    private static List<Smoke> mapNormalSmoke;
    private MapFragmentPresenter mMapFragmentPresenter;
    private List<BitmapDescriptor> viewList;

    public  MyOverlayManager(){
    }

    public void init(BaiduMap baiduMap,List<Smoke> mapNormalSmoke, MapFragmentPresenter mMapFragmentPresenter,List<BitmapDescriptor> viewList){
        initBaiduMap(baiduMap);
        this.mapNormalSmoke = mapNormalSmoke;
        this.mMapFragmentPresenter = mMapFragmentPresenter;
        this.viewList = viewList;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle bundle = marker.getExtraInfo();
        mMapFragmentPresenter.getClickDev(bundle);
        return true;
    }

    @Override
    public boolean onPolylineClick(Polyline arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<OverlayOptions> getOverlayOptions() {
        // TODO Auto-generated method stub
        List<OverlayOptions> overlayOptionses = new ArrayList<>();
        if(mapNormalSmoke!=null&&mapNormalSmoke.size()>0){
            ArrayList<BitmapDescriptor> giflist = new ArrayList<>();
            giflist.add(viewList.get(0));
            giflist.add(viewList.get(1));
            ArrayList<BitmapDescriptor> giflistRQ = new ArrayList<>();
            giflistRQ.add(viewList.get(2));
            giflistRQ.add(viewList.get(1));
            ArrayList<BitmapDescriptor> giflist2 = new ArrayList<>();
            giflist2.add(viewList.get(3));
            giflist2.add(viewList.get(4));
            ArrayList<BitmapDescriptor> giflistDq = new ArrayList<>();
            giflistDq.add(viewList.get(5));
            giflistDq.add(viewList.get(1));
            ArrayList<BitmapDescriptor> giflistSG = new ArrayList<>();
            giflistSG.add(viewList.get(6));
            giflistSG.add(viewList.get(1));
            ArrayList<BitmapDescriptor> giflistSB = new ArrayList<>();
            giflistSB.add(viewList.get(7));
            giflistSB.add(viewList.get(1));
            ArrayList<BitmapDescriptor> giflistSY = new ArrayList<>();//@@水压5.4
            giflistSY.add(viewList.get(8));
            giflistSY.add(viewList.get(9));
            ArrayList<BitmapDescriptor> giflistSJSB = new ArrayList<>();//@@三江设备5.4
            giflistSJSB.add(viewList.get(10));
            giflistSJSB.add(viewList.get(1));
            for (Smoke smoke : mapNormalSmoke) {
                Camera mCamera = smoke.getCamera();
                int alarmState = smoke.getIfDealAlarm();
                Bundle bundle = new Bundle();
                if(mCamera!=null&&mCamera.getLatitude()!=null&&mCamera.getLatitude().length()>0){
                    double latitude = Double.parseDouble(mCamera.getLatitude());
                    double longitude = Double.parseDouble(mCamera.getLongitude());
                    LatLng latLng = new LatLng(latitude, longitude);
                    bundle.putSerializable("mNormalSmoke",mCamera);
                    markMap(latLng,overlayOptionses,alarmState,giflist2,viewList.get(3),bundle);
                }else{
                    if(smoke.getLatitude().length()==0||smoke.getLongitude().length()==0){
                       continue;
                    }//@@
                    double latitude = Double.parseDouble(smoke.getLatitude());
                    double longitude = Double.parseDouble(smoke.getLongitude());

                    LatLng latLng = new LatLng(latitude, longitude);
                    bundle.putSerializable("mNormalSmoke",smoke);
                    int devType = smoke.getDeviceType();
                    switch (devType){
                        case 1:
                            markMap(latLng,overlayOptionses,alarmState,giflist,viewList.get(0),bundle);
                            break;
                        case 2:
                            markMap(latLng,overlayOptionses,alarmState,giflistRQ,viewList.get(2),bundle);
                            break;
                        case 5:
                            markMap(latLng,overlayOptionses,alarmState,giflistDq,viewList.get(5),bundle);
                            break;
                        case 7:
                            markMap(latLng,overlayOptionses,alarmState,giflistSG,viewList.get(6),bundle);
                            break;
                        case 8:
                            markMap(latLng,overlayOptionses,alarmState,giflistSB,viewList.get(7),bundle);
                            break;
                        case 9:
                            markMap(latLng,overlayOptionses,alarmState,giflistSJSB,viewList.get(10),bundle);
                            break;
                        case 10:
                            markMap(latLng,overlayOptionses,alarmState,giflistSY,viewList.get(8),bundle);
                            break;
                    }
                }
            }
        }
        return overlayOptionses;
    }

    private void markMap(LatLng latLng,List<OverlayOptions> overlayOptions,int alarmState,
                         ArrayList<BitmapDescriptor> bitmapDescriptors,BitmapDescriptor bitmapDescriptor, Bundle bundle){
        if(alarmState==0){
            overlayOptions.add(new MarkerOptions().position(latLng).icons(bitmapDescriptors).extraInfo(bundle)
                    .zIndex(0).period(10));
//                    .animateType(MarkerOptions.MarkerAnimateType.drop));//取消下落动画。。
        }else{
            overlayOptions.add(new MarkerOptions().position(latLng).icon(bitmapDescriptor).extraInfo(bundle)
                    .zIndex(0).draggable(false)////设置标记不可拖拽@@5.18
                    .perspective(true));
//                    .animateType(MarkerOptions.MarkerAnimateType.drop));//取消下落动画。。
        }
    }
}

