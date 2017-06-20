package com.smart.cloud.fire.mvp.fragment.MapFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    @Bind(R.id.search_fire_btn)
    Button search_fire_btn;
    @Bind(R.id.add_fire)
    ImageView add_fire;
    @Bind(R.id.area_condition)
    XCDropDownListViewMapSearch areaCondition;
    @Bind(R.id.shop_type_condition)
    XCDropDownListViewMapSearch shopTypeCondition;
    @Bind(R.id.spinner)
    Spinner spinner;//@@
    @Bind(R.id.area_search)
    EditText area_search;//@@
    @Bind(R.id.lin_search)
    LinearLayout lin_search;//@@
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
        mvpPresenter.getPlaceTypeId(userID, privilege + "", 3);//@@
        if (privilege == 1) {
            add_fire.setVisibility(View.GONE);//权限为1时没有搜索功能。。
        } else {
            add_fire.setVisibility(View.VISIBLE);
            add_fire.setImageResource(R.drawable.search);
        }
//        mvpPresenter.getAllSmoke(userID, privilege + "");//获取所有设备并显示。。
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

    /**
     * 初始化各种设备的标记图标。。
     * @return
     */
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
        View viewSY = LayoutInflater.from(mContext).inflate(
                R.layout.image_sy_mark, null);//@@水压5.4
        View viewSY_BJ = LayoutInflater.from(mContext).inflate(
                R.layout.image_sy_bj_mark, null);//@@水压报警5.4
        View viewSJSB = LayoutInflater.from(mContext).inflate(
                R.layout.image_sjsb_mark, null);//@@三江设备5.4
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
        BitmapDescriptor syImage = BitmapDescriptorFactory
                .fromView(viewSY);//@@5.4
        BitmapDescriptor sy_bj_Image = BitmapDescriptorFactory
                .fromView(viewSY_BJ);//@@5.4
        BitmapDescriptor sjsbImage = BitmapDescriptorFactory
                .fromView(viewSJSB);//@@5.4
        List<BitmapDescriptor> listView = new ArrayList<>();
        listView.add(bdA);
        listView.add(bdC);
        listView.add(bdRQ);
        listView.add(cameraImage);
        listView.add(cameraImage2);
        listView.add(bdDq);
        listView.add(bdSG);
        listView.add(bdSB);
        listView.add(syImage);
        listView.add(sy_bj_Image);
        listView.add(sjsbImage);
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

    /**
     * 显示商铺类型列表。。
     * @param shopTypes
     */
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

    /**
     *显示区域列表。。
     * @param shopTypes
     */
    @Override
    public void getAreaType(ArrayList<Object> shopTypes) {
        areaCondition.setItemsData(shopTypes,mMapFragmentPresenter);
        areaCondition.showPopWindow();
        areaCondition.setClickable(true);
        areaCondition.closeLoading();
    }

    ArrayList<Object> arealist;//@@
    //@@获取区域列表。。
    @Override
    public void getAreaList(ArrayList<Object> shopTypes) {
        arealist=shopTypes;
        String[] mItems=new String[shopTypes.size()];
        for(int i=0;i<arealist.size();i++){
            mItems[i]=((Area)arealist.get(i)).getAreaName();
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_dropdown_item, mItems);
        //绑定 Adapter到控件
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int selectedAreaId=SharedPreferencesManager.getInstance().getIntData(mContext,"selectedAreaId");
        if(selectedAreaId>=arealist.size()){
            selectedAreaId=0;//@@5.27切换帐号的时候区域数目不一样会闪退
        }
        spinner.setSelection(selectedAreaId);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mvpPresenter.getNeedSmoke(userID, privilege + "", ((Area)arealist.get(position)).getAreaId(), "");//获取按照要求获取设备。。
                SharedPreferencesManager.getInstance().putData(mContext,"selectedAreaId",position);
                SharedPreferencesManager.getInstance().putData(mContext,"selectedAreaNum",((Area)arealist.get(position)).getAreaId());//@@5.18
                TextView tv=(TextView)view;
                tv.setTextColor(mContext.getResources().getColor(R.color.white));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void getAreaTypeFail(String msg) {
        T.showShort(mContext, msg);
        mBaiduMap.clear();//@@5.27无数据时清除所有标记
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

    @OnClick({R.id.search_fire_btn,R.id.search_fire, R.id.add_fire, R.id.area_condition, R.id.shop_type_condition})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_fire_btn://@@4.27
                String search=area_search.getText().toString();//@@4.27
                area_search.setText("");
                if(search.length()!=0&&search!=null){
                    lin_search.setVisibility(View.GONE);//@@4.27
                    mvpPresenter.getSearchSmoke(userID, privilege + "",search );//@@4.27获取查询内容获取设备。。
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){
                        imm.hideSoftInputFromWindow(search_fire_btn.getWindowToken(),0);//隐藏输入软键盘@@4.28
                    }
                }else{
                    T.show(mContext,"查询内容不能为空",Toast.LENGTH_SHORT);
                }
                break;
            case R.id.search_fire:
                if (shopTypeCondition.ifShow()) {
                    shopTypeCondition.closePopWindow();
                }
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                }
                if ((mShopType != null && mShopType.getPlaceTypeId() != null) || (mArea != null && mArea.getAreaId() != null)) {
                    lin_search.setVisibility(View.GONE);
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
                }
                break;
            case R.id.add_fire:
                if (visibility) {
                    visibility = false;
                    lin_search.setVisibility(View.GONE);
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
                    lin_search.setVisibility(View.VISIBLE);
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
