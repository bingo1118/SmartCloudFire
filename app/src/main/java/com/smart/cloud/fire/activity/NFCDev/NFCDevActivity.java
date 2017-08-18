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

import com.smart.cloud.fire.activity.AllSmoke.AllSmokeActivity;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokePresenter;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokeView;
import com.smart.cloud.fire.activity.Map.MapActivity;
import com.smart.cloud.fire.adapter.NFCDevAdapter;
import com.smart.cloud.fire.adapter.ShopCameraAdapter;
import com.smart.cloud.fire.adapter.ShopSmokeAdapter;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.view.XCDropDownListViewMapSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class NFCDevActivity extends MvpActivity<NFCDevPresenter> implements NFCDevView {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.smoke_total)
    LinearLayout smokeTotal;
    @Bind(R.id.total_num)
    TextView totalNum;
    @Bind(R.id.online_num)
    TextView onlineNum;
    @Bind(R.id.offline_num)
    TextView offlineNum;
    private LinearLayoutManager linearLayoutManager;
    private NFCDevAdapter shopSmokeAdapter;
    private int lastVisibleItem;
    private Context mContext;
    private List<NFCRecordBean> list;
    private int loadMoreCount;
    private boolean research = false;
    private String page;
    private String userID;
    private int privilege;
    private NFCDevPresenter nfcDevPresenter;

    @Bind(R.id.add_fire)
    ImageView addFire;//显示搜索界面按钮。。
    @Bind(R.id.lin1)
    LinearLayout lin1;//搜素界面。。
    @Bind(R.id.area_condition)
    XCDropDownListViewMapSearch areaCondition;//区域下拉选择。。
    @Bind(R.id.shop_type_condition)
    XCDropDownListViewMapSearch shopTypeCondition;//商铺类型下拉选择。。
    @Bind(R.id.search_fire)
    ImageView searchFire;//搜索按钮。。
    private boolean visibility = false;
    private ShopType mShopType;
    private Area mArea;
    private String areaId = "";
    private String shopTypeId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcdev);

        ButterKnife.bind(this);
        mContext=this;
        smokeTotal.setVisibility(View.VISIBLE);//@@8.17
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        page = "1";
        list = new ArrayList<>();
        refreshListView();
        addFire.setVisibility(View.VISIBLE);//@@8.17
        addFire.setImageResource(R.drawable.search);//@@8.17
        mvpPresenter.getNFCInfo(userID, "" + "", page, list, 1,false);
//        mvpPresenter.getSmokeSummary(userID,privilege+"","","","1");//@@8.17
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
                page = "1";
                list.clear();
                mvpPresenter.getNFCInfo(userID, "" + "", page, list, 1,true);
//                mvpPresenter.getSmokeSummary(userID,privilege+"","","","1");
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (research) {
                    if(shopSmokeAdapter!=null){
                        shopSmokeAdapter.changeMoreStatus(ShopCameraAdapter.NO_DATA);
                    }
                    return;
                }
                if(shopSmokeAdapter==null){
                    return;
                }
                int count = shopSmokeAdapter.getItemCount();
//                int itemCount = lastVisibleItem+2;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == count) {
                    if(loadMoreCount>=20){
                        page = Integer.parseInt(page) + 1 + "";
                        mvpPresenter.getNFCInfo(userID, "" + "", page, list, 1,true);//@@7.17
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

    @OnClick({R.id.add_fire, R.id.area_condition, R.id.shop_type_condition, R.id.search_fire,R.id.turn_map_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_fire://显示查询条件按钮。。
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
            case R.id.area_condition://地区类型下拉列表。。
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                    areaCondition.setClickable(false);
                    areaCondition.showLoading();
                }
                break;
            case R.id.shop_type_condition://商铺类型下拉列表。。
                if (shopTypeCondition.ifShow()) {
                    shopTypeCondition.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 1);
                    shopTypeCondition.setClickable(false);
                    shopTypeCondition.showLoading();
                }
                break;
            case R.id.search_fire://查询按钮
                if (!Utils.isNetworkAvailable(this)) {
                    return;
                }
                if (shopTypeCondition.ifShow()) {
                    shopTypeCondition.closePopWindow();
                }
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                }
                if ((mShopType != null && mShopType.getPlaceTypeId() != null) || (mArea != null && mArea.getAreaId() != null)) {
                    lin1.setVisibility(View.GONE);
                    searchFire.setVisibility(View.GONE);
                    addFire.setVisibility(View.VISIBLE);
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
                    mvpPresenter.getNeedNFC(userID, areaId,"");//显示设备。。
//                    mvpPresenter.getSmokeSummary(userID,privilege+"",areaId,shopTypeId,"1");//显示总数。。;
                    mShopType = null;
                    mArea = null;
                } else {
                    lin1.setVisibility(View.GONE);
                    return;
                }
                break;
            case R.id.turn_map_btn:
                Intent intent=new Intent(NFCDevActivity.this, MapActivity.class);
                intent.putExtra("devType","7");
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected NFCDevPresenter createPresenter() {
        nfcDevPresenter = new NFCDevPresenter(this);
        return nfcDevPresenter;
    }


    @Override
    public void getDataSuccess(List<?> smokeList,boolean search) {
        research = search;
        loadMoreCount = smokeList.size();
        list.clear();
        list.addAll((List<NFCRecordBean>)smokeList);
        shopSmokeAdapter = new NFCDevAdapter(mContext, list);
        recyclerView.setAdapter(shopSmokeAdapter);
        swipereFreshLayout.setRefreshing(false);
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext, msg);
        swipereFreshLayout.setRefreshing(false);
        if(shopSmokeAdapter!=null){
            shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.NO_DATA);
        }
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
    public void onLoadingMore(List<?> smokeList) {
        loadMoreCount = smokeList.size();
        list.addAll((List<NFCRecordBean>)smokeList);
        shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.LOADING_MORE);
    }

    @Override
    public void getAreaType(ArrayList<?> shopTypes, int type) {
        if (type == 1) {
            shopTypeCondition.setItemsData((ArrayList<Object>) shopTypes, nfcDevPresenter);
            shopTypeCondition.showPopWindow();
            shopTypeCondition.setClickable(true);
            shopTypeCondition.closeLoading();
        } else {
            areaCondition.setItemsData((ArrayList<Object>) shopTypes, nfcDevPresenter);
            areaCondition.showPopWindow();
            areaCondition.setClickable(true);
            areaCondition.closeLoading();
        }
    }

    @Override
    public void getAreaTypeFail(String msg, int type) {
        T.showShort(mContext, msg);
        if (type == 1) {
            shopTypeCondition.setClickable(true);
            shopTypeCondition.closeLoading();
        } else {
            areaCondition.setClickable(true);
            areaCondition.closeLoading();
        }
    }

    @Override
    public void unSubscribe(String type) {
    }

    @Override
    public void getLostCount(String count) {
    }

    @Override
    public void getChoiceArea(Area area) {
        mArea = area;
        if (mArea != null && mArea.getAreaId() != null) {
            addFire.setVisibility(View.GONE);
            searchFire.setVisibility(View.VISIBLE);
        }
        if (mArea.getAreaId() == null && mShopType == null) {
            addFire.setVisibility(View.VISIBLE);
            searchFire.setVisibility(View.GONE);
        } else if (mArea.getAreaId() == null && mShopType != null && mShopType.getPlaceTypeId() == null) {
            addFire.setVisibility(View.VISIBLE);
            searchFire.setVisibility(View.GONE);
        }
    }

    @Override
    public void getChoiceShop(ShopType shopType) {
        mShopType = shopType;
        if (mShopType != null && mShopType.getPlaceTypeId() != null) {
            addFire.setVisibility(View.GONE);
            searchFire.setVisibility(View.VISIBLE);
        }
        if (mShopType.getPlaceTypeId() == null && mArea == null) {
            addFire.setVisibility(View.VISIBLE);
            searchFire.setVisibility(View.GONE);
        } else if (mShopType.getPlaceTypeId() == null && mArea != null && mArea.getAreaId() == null) {
            addFire.setVisibility(View.VISIBLE);
            searchFire.setVisibility(View.GONE);
        }
    }

    @Override
    public void getSmokeSummary(SmokeSummary smokeSummary) {
        totalNum.setText(smokeSummary.getAllSmokeNumber()+"");
        onlineNum.setText(smokeSummary.getOnlineSmokeNumber()+"");
        offlineNum.setText(smokeSummary.getLossSmokeNumber()+"");
    }

}
