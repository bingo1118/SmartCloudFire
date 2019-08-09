package com.smart.cloud.fire.activity.Inspection.InspectionMap;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.MyOverlayManager;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.NFCInfoEntity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspectionMapActivity extends MvpActivity<InspectionMapPresenter> implements InspectionMapView{

    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.bmapView)
    MapView mMapView;

    private Context mContext;
    private String userID;
    private BaiduMap mBaiduMap;
    private InspectionMapPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_map);
        ButterKnife.bind(this);
        mBaiduMap = mMapView.getMap();// 获得MapView
        mContext = this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);

        mPresenter.getAllItems(userID,"");
    }

    @Override
    protected InspectionMapPresenter createPresenter() {
        if(mPresenter==null){
            mPresenter=new InspectionMapPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void getDataSuccess(List<NFCInfoEntity> pointList, int sum, int pass, int checked) {

    }

    private MyOverlayManager mMyOverlayManager;
    @Override
    public void getDataSuccess(List<NFCInfoEntity> pointList) {
        mBaiduMap.clear();
        List<BitmapDescriptor> viewList =  initMark();
        if(mMyOverlayManager==null){
            mMyOverlayManager = new MyOverlayManager();
        }
        mMyOverlayManager.initInspection(mContext,mBaiduMap,pointList,viewList);
        mMyOverlayManager.removeFromMap();
        mBaiduMap.setOnMarkerClickListener(mMyOverlayManager);
        mMyOverlayManager.addToMap();
        mMyOverlayManager.zoomToSpan();
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMyOverlayManager.zoomToSpan();
            }
        });
    }

    /**
     * 初始化各种设备的标记图标。。
     * @return
     */
    private List<BitmapDescriptor> initMark(){
        View viewA = LayoutInflater.from(mContext).inflate(
                R.layout.image_mark, null);
        BitmapDescriptor bdA = BitmapDescriptorFactory
                .fromView(viewA);
        List<BitmapDescriptor> listView = new ArrayList<>();
        listView.add(bdA);
        return listView;
    }

    @Override
    public void getDataFail(String msg) {

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
}
