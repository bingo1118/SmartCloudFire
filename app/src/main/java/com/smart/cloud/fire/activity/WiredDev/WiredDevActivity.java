package com.smart.cloud.fire.activity.WiredDev;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokePresenter;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokeView;
import com.smart.cloud.fire.activity.Map.MapActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.AllDevFragment.AllDevFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.OffLineDevFragment.OffLineDevFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment.WiredDevFragment;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.utils.VolleyHelper;
import com.smart.cloud.fire.view.AreaChooceListView;
import com.smart.cloud.fire.view.XCDropDownListViewMapSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class WiredDevActivity extends MvpActivity<WiredDevPresenter> implements WiredDevView {
    RelativeLayout title_name_rela,title_lose_dev_rela;
    TextView title_name_tv,title_lose_dev_tv;
    Context mContext;
    private WiredDevPresenter mAllSmokePresenter;
    private String userID;
    private int privilege;

    private WiredDevFragment wiredDevFragment;
    private OfflineWiredDevFragment offLineDevFragment;
    private FragmentManager fragmentManager;
    public static final int FRAGMENT_TWO = 1;
    public static final int FRAGMENT_FIVE =4;
    private int position;
    private boolean visibility = false;
    private ShopType mShopType;
    private Area mArea;
    private String areaId = "";
    private String parentId="";//@@9.1
    private String shopTypeId = "";

//    @Bind(R.id.add_fire)
//    ImageView addFire;//显示搜索界面按钮。。
//    @Bind(R.id.lin1)
//    LinearLayout lin1;//搜素界面。。
    @Bind(R.id.area_condition)
    AreaChooceListView areaCondition;//区域下拉选择。。
//    @Bind(R.id.shop_type_condition)
//    XCDropDownListViewMapSearch shopTypeCondition;//商铺类型下拉选择。。
//    @Bind(R.id.smoke_total)
//    LinearLayout smokeTotal;
//    @Bind(R.id.total_num)
//    TextView totalNum;
    @Bind(R.id.online_num)
    TextView onlineNum;
    @Bind(R.id.offline_num)
    TextView offlineNum;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
//    @Bind(R.id.search_fire)
//    ImageView searchFire;//搜索按钮。。
    @Bind(R.id.turn_map_btn)
    RelativeLayout turn_map_btn;

    List<Area> parent = null;//@@8.31
    Map<String, List<Area>> map = null;//@@8.31

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_smoke);
        ButterKnife.bind(this);

        //透明状态栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        mContext=this;
        init();
//        addFire.setVisibility(View.VISIBLE);
//        addFire.setImageResource(R.drawable.search);
        title_name_tv=(TextView )findViewById(R.id.title_name_text);
        title_lose_dev_tv=(TextView)findViewById(R.id.title_lose_dev_text) ;
        title_name_rela=(RelativeLayout)findViewById(R.id.title_name) ;
        title_name_rela.setEnabled(false);
        title_name_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_lose_dev_rela.setEnabled(true);
                title_name_rela.setEnabled(false);
//                smokeTotal.setVisibility(View.VISIBLE);
                mvpPresenter.unSubscribe("wiredSmoke");
                position=FRAGMENT_TWO;//@@在线设备
            }
        });
        title_lose_dev_rela=(RelativeLayout)findViewById(R.id.title_lose_dev) ;
        title_lose_dev_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_name_rela.setEnabled(true);
                title_lose_dev_rela.setEnabled(false);
//                smokeTotal.setVisibility(View.VISIBLE);
                mvpPresenter.unSubscribe("lostSmoke");
                position=FRAGMENT_FIVE;//@@离线设备
            }
        });
        title_name_tv.setText("传输装置");
        title_lose_dev_tv.setText("离线设备");
        areaCondition.setActivity(this);//@@12.21
//        shopTypeCondition.setActivity(this);//@@12.21
        areaCondition.setHintTextColor("#ffffffff");
        areaCondition.setEditTextHint("#ffffffff");
        areaCondition.setEditTextHint("选择区域");
    }

    @OnClick({ R.id.area_condition,R.id.turn_map_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_fire://显示查询条件按钮。。
                if (visibility) {
                    visibility = false;
//                    lin1.setVisibility(View.GONE);
                    if (areaCondition.ifShow()) {
                        areaCondition.closePopWindow();
                    }
//                    if (shopTypeCondition.ifShow()) {
//                        shopTypeCondition.closePopWindow();
//                    }
                } else {
                    visibility = true;
                    areaCondition.setEditText("");
//                    shopTypeCondition.setEditText("");
                    areaCondition.setEditTextHint("区域");
//                    shopTypeCondition.setEditTextHint("类型");
//                    lin1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.area_condition://地区类型下拉列表。。
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                } else {
                    VolleyHelper helper=VolleyHelper.getInstance(mContext);
                    RequestQueue mQueue = helper.getRequestQueue();
//                    RequestQueue mQueue = Volley.newRequestQueue(mContext);
                    String url=ConstantValues.SERVER_IP_NEW+"/getAreaInfo?userId="+userID+"&privilege="+privilege;
                    StringRequest stringRequest = new StringRequest(url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject=new JSONObject(response);
                                        if(jsonObject.getInt("errorCode")==0){
                                            parent = new ArrayList<>();
                                            map = new HashMap<>();
                                            JSONArray jsonArrayParent=jsonObject.getJSONArray("areas");
                                            for(int i=0;i<jsonArrayParent.length();i++){
                                                JSONObject tempParent= jsonArrayParent.getJSONObject(i);
                                                Area tempArea=new Area();
                                                tempArea.setAreaId(tempParent.getString("areaId"));
                                                tempArea.setAreaName(tempParent.getString("areaName"));
                                                tempArea.setIsParent(1);
                                                parent.add(tempArea);
                                                List<Area> child = new ArrayList<>();
                                                JSONArray jsonArrayChild=tempParent.getJSONArray("areas");
                                                for(int j=0;j<jsonArrayChild.length();j++){
                                                    JSONObject tempChild= jsonArrayChild.getJSONObject(j);
                                                    Area tempAreaChild=new Area();
                                                    tempAreaChild.setAreaId(tempChild.getString("areaId"));
                                                    tempAreaChild.setAreaName(tempChild.getString("areaName"));
                                                    tempAreaChild.setIsParent(0);
                                                    child.add(tempAreaChild);
                                                }
                                                map.put(tempParent.getString("areaName"),child);
                                            }
                                        }
                                        areaCondition.setItemsData2(parent,map, mAllSmokePresenter);
                                        areaCondition.showPopWindow();
                                        areaCondition.setClickable(true);
                                        areaCondition.closeLoading();
//                                        mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error","error");
                        }
                    });
                    mQueue.add(stringRequest);
                    areaCondition.setClickable(false);
                    areaCondition.showLoading();
                }
                break;
            case R.id.shop_type_condition://商铺类型下拉列表。。
//                if (shopTypeCondition.ifShow()) {
//                    shopTypeCondition.closePopWindow();
//                } else {
//                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 1);
//                    shopTypeCondition.setClickable(false);
//                    shopTypeCondition.showLoading();
//                }
                break;
            case R.id.search_fire://查询按钮
                if (!Utils.isNetworkAvailable(this)) {
                    return;
                }
//                if (shopTypeCondition.ifShow()) {
//                    shopTypeCondition.closePopWindow();
//                }
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                }
                if ((mShopType != null && mShopType.getPlaceTypeId() != null) || (mArea != null && mArea.getAreaId() != null)) {
//                    lin1.setVisibility(View.GONE);
//                    searchFire.setVisibility(View.GONE);
//                    addFire.setVisibility(View.VISIBLE);
                    areaCondition.searchClose();
//                    shopTypeCondition.searchClose();
                    visibility = false;
                    if (mArea != null && mArea.getAreaId() != null) {
                        if(mArea.getIsParent()==1){
                            parentId= mArea.getAreaId();//@@9.1
                            areaId="";
                        }else{
                            areaId = mArea.getAreaId();
                            parentId="";
                        }
                    } else {
                        areaId = "";
                        parentId="";
                    }
                    if (mShopType != null && mShopType.getPlaceTypeId() != null) {
                        shopTypeId = mShopType.getPlaceTypeId();
                    } else {
                        shopTypeId = "";
                    }
                    //判断当前在哪个子fragment。。
//                    switch (position) {
//                        case FRAGMENT_TWO:
                            mvpPresenter.getAllWiredDev(userID, privilege + "",parentId, areaId,"", shopTypeId, "2",null,1,false,wiredDevFragment);
                            mvpPresenter.getSmokeSummary(userID,privilege+"",parentId,areaId,shopTypeId,"2",wiredDevFragment);//显示总数。。
//                            break;
//                        case FRAGMENT_FIVE://@@6.29
                            mvpPresenter.getNeedLossSmoke(userID, privilege + "",parentId,areaId, shopTypeId, "","2",false,0,null,offLineDevFragment);
//                            mvpPresenter.getNeedLossSmoke(userID, privilege + "", areaId, shopTypeId, "", false, offLineDevFragment);
                            mvpPresenter.getSmokeSummary(userID,privilege+"",parentId,areaId,shopTypeId,"2",offLineDevFragment);
//                            break;
//                        default:
//                            break;
//                    }
                    mShopType = null;
                    mArea = null;
                } else {
//                    lin1.setVisibility(View.GONE);
                    return;
                }
                break;
            case R.id.turn_map_btn:
                Intent intent=new Intent(WiredDevActivity.this, MapActivity.class);
                intent.putExtra("devType","2");
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    @Override
    protected WiredDevPresenter createPresenter() {
        mAllSmokePresenter=new WiredDevPresenter(this);
        return mAllSmokePresenter;
    }

    private void init() {
        fragmentManager = getFragmentManager();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        showFragment(FRAGMENT_TWO);
//        addFire.setVisibility(View.VISIBLE);
//        addFire.setImageResource(R.drawable.search);
//        smokeTotal.setVisibility(View.VISIBLE);
//        mAllSmokePresenter.getSmokeSummary(userID,privilege+"","","","","2");
    }

    public void showFragment(int index) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        hideFragment(ft);
        //注意这里设置位置
        position = index;
        if (areaCondition.ifShow()) {
            areaCondition.closePopWindow();
        }//@@5.5关闭下拉选项
//        if (shopTypeCondition.ifShow()) {
//            shopTypeCondition.closePopWindow();
//        }//@@5.5关闭下拉选项
        switch (index) {
            case FRAGMENT_TWO:
//                addFire.setVisibility(View.VISIBLE);//@@5.3
                if (wiredDevFragment == null) {
                    offLineDevFragment = new OfflineWiredDevFragment();
                    ft.add(R.id.fragment_content, offLineDevFragment);
                    wiredDevFragment = new WiredDevFragment();
                    ft.add(R.id.fragment_content, wiredDevFragment);
                } else {
                    ft.show(wiredDevFragment);
                }
                break;
            case FRAGMENT_FIVE:
//                addFire.setVisibility(View.VISIBLE);//@@5.3
                if (offLineDevFragment == null) {
                    offLineDevFragment = new OfflineWiredDevFragment();
                    ft.add(R.id.fragment_content, offLineDevFragment);
                } else {
                    ft.show(offLineDevFragment);
                }
                break;
        }
        ft.commit();
    }

    public void hideFragment(FragmentTransaction ft) {
        //如果不为空，就先隐藏起来
        if (wiredDevFragment != null) {
            ft.hide(wiredDevFragment);
        }
        if (offLineDevFragment != null) {
            ft.hide(offLineDevFragment);
        }
    }

    @Override
    public void getDataSuccess(List<?> smokeList, boolean research) {

    }

    @Override
    public void getSmokeSummary(SmokeSummary smokeSummary) {
//        totalNum.setText(smokeSummary.getAllSmokeNumber()+"");
        onlineNum.setText(smokeSummary.getOnlineSmokeNumber()+"");
        offlineNum.setText(smokeSummary.getLossSmokeNumber()+"");
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
    public void unSubscribe(String type) {
        switch (type) {
            case "wiredSmoke":
//                mAllSmokePresenter.getSmokeSummary(userID,privilege+"","","","","2");
//                lin1.setVisibility(View.GONE);
//                searchFire.setVisibility(View.GONE);
//                addFire.setVisibility(View.VISIBLE);
                showFragment(FRAGMENT_TWO);
                break;
            case "lostSmoke":
//                mAllSmokePresenter.getSmokeSummary(userID,privilege+"","","","","2");
//                lin1.setVisibility(View.GONE);
//                searchFire.setVisibility(View.GONE);
//                addFire.setVisibility(View.VISIBLE);
                showFragment(FRAGMENT_FIVE);
                break;
            default:
                break;
        }
    }

    @Override
    public void getAreaType(ArrayList<?> shopTypes, int type) {
        if (type == 1) {
//            shopTypeCondition.setItemsData((ArrayList<Object>) shopTypes, mAllSmokePresenter);
//            shopTypeCondition.showPopWindow();
//            shopTypeCondition.setClickable(true);
//            shopTypeCondition.closeLoading();
        } else {
            areaCondition.setItemsData((ArrayList<Object>) shopTypes, mAllSmokePresenter);
            areaCondition.showPopWindow();
            areaCondition.setClickable(true);
            areaCondition.closeLoading();
        }

    }

    @Override
    public void getAreaTypeFail(String msg, int type) {
        T.showShort(mContext, msg);
        if (type == 1) {
//            shopTypeCondition.setClickable(true);
//            shopTypeCondition.closeLoading();
        } else {
            areaCondition.setClickable(true);
            areaCondition.closeLoading();
        }
    }

    @Override
    public void getDataFail(String msg) {
        T.show(mContext,msg, Toast.LENGTH_SHORT);//@@4.27
    }

    @Override
    public void getChoiceArea(Area area) {
        mArea = area;
        if (mArea != null && mArea.getAreaId() != null) {
//            addFire.setVisibility(View.GONE);
//            searchFire.setVisibility(View.VISIBLE);
        }
        if (mArea.getAreaId() == null && mShopType == null) {
//            addFire.setVisibility(View.VISIBLE);
//            searchFire.setVisibility(View.GONE);
        } else if (mArea.getAreaId() == null && mShopType != null && mShopType.getPlaceTypeId() == null) {
//            addFire.setVisibility(View.VISIBLE);
//            searchFire.setVisibility(View.GONE);
        }
        areaCondition.searchClose();
        visibility = false;
        if (mArea != null && mArea.getAreaId() != null) {
            if(mArea.getIsParent()==1){
                parentId= mArea.getAreaId();//@@9.1
                areaId="";
            }else{
                areaId = mArea.getAreaId();
                parentId="";
            }
        } else {
            areaId = "";
            parentId="";
        }
        if (mShopType != null && mShopType.getPlaceTypeId() != null) {
            shopTypeId = mShopType.getPlaceTypeId();
        } else {
            shopTypeId = "";
        }

        mvpPresenter.getAllWiredDev(userID, privilege + "",parentId, areaId,"", shopTypeId, "2",null,1,false,wiredDevFragment);
        mvpPresenter.getSmokeSummary(userID,privilege+"",parentId,areaId,shopTypeId,"2",wiredDevFragment);//显示总数。。

        mvpPresenter.getNeedLossSmoke(userID, privilege + "",parentId,areaId, shopTypeId, "","2",false,0,null,offLineDevFragment);
        mvpPresenter.getSmokeSummary(userID,privilege+"",parentId,areaId,shopTypeId,"2",offLineDevFragment);
        mShopType = null;
        mArea = null;
    }

    @Override
    public void getChoiceShop(ShopType shopType) {
        mShopType = shopType;
        if (mShopType != null && mShopType.getPlaceTypeId() != null) {
//            addFire.setVisibility(View.GONE);
//            searchFire.setVisibility(View.VISIBLE);
        }
        if (mShopType.getPlaceTypeId() == null && mArea == null) {
//            addFire.setVisibility(View.VISIBLE);
//            searchFire.setVisibility(View.GONE);
        } else if (mShopType.getPlaceTypeId() == null && mArea != null && mArea.getAreaId() == null) {
//            addFire.setVisibility(View.VISIBLE);
//            searchFire.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadingMore(List<?> smokeList) {
    }

    @Override
    public void getLostCount(String count) {

    }

    @Override
    public void refreshView() {
        wiredDevFragment.refreshView();
        offLineDevFragment.refreshView();
        areaCondition.setEditText("");
    }

}

