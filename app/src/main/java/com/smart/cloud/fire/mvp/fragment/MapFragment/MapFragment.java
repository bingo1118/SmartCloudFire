package com.smart.cloud.fire.mvp.fragment.MapFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.MyOverlayManager;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.ui.ApMonitorActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.view.ShowAlarmDialog;
import com.smart.cloud.fire.view.ShowSmokeDialog;
import com.smart.cloud.fire.view.XCDropDownListViewMapSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class MapFragment extends MvpFragment<MapFragmentPresenter> implements MapFragmentView {

    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.bmapView)
    MapView mMapView;
    @Bind(R.id.lin1)
    LinearLayout lin1;
    @Bind(R.id.search_fire)
    ImageView search_fire;
    @Bind(R.id.add_fire)
    ImageView add_fire;
    @Bind(R.id.area_condition)
    XCDropDownListViewMapSearch areaCondition;
    @Bind(R.id.shop_type_condition)
    XCDropDownListViewMapSearch shopTypeCondition;
    private BaiduMap mBaiduMap;
    private Context mContext;
    private String userID;
    private int privilege;
    private ShopType mShopType;
    private Area mArea;
    private String areaId = "";
    private String shopTypeId = "";
    private MapFragmentPresenter mMapFragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container,
                false);
        ButterKnife.bind(this, view);
        mBaiduMap = mMapView.getMap();// 获得MapView
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        if (privilege == 1) {
            add_fire.setVisibility(View.GONE);
        } else {
            add_fire.setVisibility(View.VISIBLE);
            add_fire.setImageResource(R.drawable.search);
        }
        mvpPresenter.getAllSmoke(userID, privilege + "");
    }

    @Override
    protected MapFragmentPresenter createPresenter() {
        mMapFragmentPresenter = new MapFragmentPresenter(MapFragment.this);
        return mMapFragmentPresenter;
    }

    @Override
    public String getFragmentName() {
        return "Map";
    }

    @Override
    public void onDestroyView() {
        mMapView.onDestroy();
        super.onDestroyView();
        if(shopTypeCondition!=null){
            if(shopTypeCondition.ifShow()){
                shopTypeCondition.closePopWindow();
            }
        }
        if(areaCondition!=null){
            if(areaCondition.ifShow()){
                areaCondition.closePopWindow();
            }
        }
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

    private MyOverlayManager mMyOverlayManager;
    @Override
    public void getDataSuccess(List<Smoke> smokeList) {
        mBaiduMap.clear();
        List<BitmapDescriptor> viewList =  initMark();
        if(mMyOverlayManager==null){
            mMyOverlayManager = new MyOverlayManager();
        }
        mMyOverlayManager.init(mBaiduMap,smokeList, mMapFragmentPresenter,viewList);
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

    private List<BitmapDescriptor> initMark(){
        View viewA = LayoutInflater.from(mContext).inflate(
                R.layout.image_mark, null);
        View viewB = LayoutInflater.from(mContext).inflate(
                R.layout.image_mark_alarm, null);
        View viewRQ = LayoutInflater.from(mContext).inflate(
                R.layout.image_rq_mark, null);
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.image_test, null);
        View view2 = LayoutInflater.from(mContext).inflate(
                R.layout.image_test2, null);
        View viewDq = LayoutInflater.from(mContext).inflate(
                R.layout.image_test_dq, null);
        View viewSG = LayoutInflater.from(mContext).inflate(
                R.layout.image_sg_mark, null);
        View viewSB = LayoutInflater.from(mContext).inflate(
                R.layout.image_sb_mark, null);
        BitmapDescriptor bdA = BitmapDescriptorFactory
                .fromView(viewA);
        BitmapDescriptor bdDq = BitmapDescriptorFactory
                .fromView(viewDq);
        BitmapDescriptor bdC = BitmapDescriptorFactory
                .fromView(viewB);
        BitmapDescriptor bdRQ = BitmapDescriptorFactory
                .fromView(viewRQ);
        BitmapDescriptor bdSG = BitmapDescriptorFactory
                .fromView(viewSG);
        BitmapDescriptor bdSB = BitmapDescriptorFactory
                .fromView(viewSB);
        BitmapDescriptor cameraImage = BitmapDescriptorFactory
                .fromView(view);
        BitmapDescriptor cameraImage2 = BitmapDescriptorFactory
                .fromView(view2);
        List<BitmapDescriptor> listView = new ArrayList<>();
        listView.add(bdA);
        listView.add(bdC);
        listView.add(bdRQ);
        listView.add(cameraImage);
        listView.add(cameraImage2);
        listView.add(bdDq);
        listView.add(bdSG);
        listView.add(bdSB);
        return listView;
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext, msg);
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
    public void getShopType(ArrayList<Object> shopTypes) {
        shopTypeCondition.setItemsData(shopTypes,mMapFragmentPresenter);
        shopTypeCondition.showPopWindow();
        shopTypeCondition.setClickable(true);
        shopTypeCondition.closeLoading();
    }

    @Override
    public void getShopTypeFail(String msg) {
        T.showShort(mContext, msg);
        shopTypeCondition.setClickable(true);
        shopTypeCondition.closeLoading();
    }

    @Override
    public void getAreaType(ArrayList<Object> shopTypes) {
        areaCondition.setItemsData(shopTypes,mMapFragmentPresenter);
        areaCondition.showPopWindow();
        areaCondition.setClickable(true);
        areaCondition.closeLoading();
    }

    @Override
    public void getAreaTypeFail(String msg) {
        T.showShort(mContext, msg);
        areaCondition.setClickable(true);
        areaCondition.closeLoading();
    }

    @Override
    public void openCamera(Camera camera) {
        Contact mContact = new Contact();
        mContact.contactType = 0;
        mContact.contactId = camera.getCameraId();
        mContact.contactPassword = camera.getCameraPwd();
        mContact.contactName = camera.getCameraName();
        mContact.apModeState = 1;
        Intent monitor = new Intent();
        monitor.setClass(mContext, ApMonitorActivity.class);
        monitor.putExtra("contact", mContact);
        monitor.putExtra("connectType", ConstantValues.ConnectType.P2PCONNECT);
        monitor.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(monitor);
    }

    @Override
    public void getChoiceArea(Area area) {
        mArea = area;
        if (mArea != null && mArea.getAreaId() != null) {
            add_fire.setVisibility(View.GONE);
            search_fire.setVisibility(View.VISIBLE);
        }
        if (mArea.getAreaId() == null && mShopType == null) {
            add_fire.setVisibility(View.VISIBLE);
            search_fire.setVisibility(View.GONE);
        } else if (mArea.getAreaId() == null && mShopType != null && mShopType.getPlaceTypeId() == null) {
            add_fire.setVisibility(View.VISIBLE);
            search_fire.setVisibility(View.GONE);
        }
    }

    @Override
    public void getChoiceShop(ShopType shopType) {
        mShopType = shopType;
        if (mShopType != null && mShopType.getPlaceTypeId() != null) {
            add_fire.setVisibility(View.GONE);
            search_fire.setVisibility(View.VISIBLE);
        }
        if (mShopType.getPlaceTypeId() == null && mArea == null) {
            add_fire.setVisibility(View.VISIBLE);
            search_fire.setVisibility(View.GONE);
        } else if (mShopType.getPlaceTypeId() == null && mArea != null && mArea.getAreaId() == null) {
            add_fire.setVisibility(View.VISIBLE);
            search_fire.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSmokeDialog(Smoke smoke) {
        View view = LayoutInflater.from(mContext).inflate(
                    R.layout.user_smoke_address_mark, null,false);
        new ShowSmokeDialog(getActivity(),view,smoke);
    }

    @Override
    public void showAlarmDialog(Smoke smoke) {
        View view = LayoutInflater.from(mContext).inflate(
                    R.layout.user_do_alarm_msg_dialog, null);
        new ShowAlarmDialog(getActivity(),view,smoke,mMapFragmentPresenter,userID);
    }

    private boolean visibility = false;

    @OnClick({R.id.search_fire, R.id.add_fire, R.id.area_condition, R.id.shop_type_condition})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_fire:
                if (shopTypeCondition.ifShow()) {
                    shopTypeCondition.closePopWindow();
                }
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                }
                if ((mShopType != null && mShopType.getPlaceTypeId() != null) || (mArea != null && mArea.getAreaId() != null)) {
                    lin1.setVisibility(View.GONE);
                    search_fire.setVisibility(View.GONE);
                    add_fire.setVisibility(View.VISIBLE);
                    areaCondition.searchClose();
                    shopTypeCondition.searchClose();
                    visibility = false;
                    if (mArea != null && mArea.getAreaId() != null) {
                        areaId = mArea.getAreaId();
                    } else {
                        areaId = "";
                    }
                    if (mShopType != null && mShopType.getPlaceTypeId() != null) {
                        shopTypeId = mShopType.getPlaceTypeId();
                    } else {
                        shopTypeId = "";
                    }
                    mvpPresenter.getNeedSmoke(userID, privilege + "", areaId, shopTypeId);
                }
                break;
            case R.id.add_fire:
                if (visibility) {
                    visibility = false;
                    lin1.setVisibility(View.GONE);
                    if (areaCondition.ifShow()) {
                        areaCondition.closePopWindow();
                    }
                    if (shopTypeCondition.ifShow()) {
                        shopTypeCondition.closePopWindow();
                    }
                } else {
                    visibility = true;
                    areaCondition.setEditText("");
                    shopTypeCondition.setEditText("");
                    areaCondition.setEditTextHint("区域");
                    shopTypeCondition.setEditTextHint("类型");
                    lin1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.shop_type_condition:
                if (shopTypeCondition.ifShow()) {
                    shopTypeCondition.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 1);
                    shopTypeCondition.setClickable(false);
                    shopTypeCondition.showLoading();
                }
                break;
            case R.id.area_condition:
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                    areaCondition.setClickable(false);
                    areaCondition.showLoading();
                }
                break;
            default:
                break;
        }
    }

}
