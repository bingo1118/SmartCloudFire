package com.smart.cloud.fire.activity.Electric;

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
import com.smart.cloud.fire.activity.SecurityDev.OfflineSecurityDevFragment;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.AllDevFragment.AllDevFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Electric.ElectricFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.OffLineDevFragment.OffLineDevFragment;
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

public class ElectricDevActivity extends MvpActivity<ElectricDevPresenter> implements ElectricDevView {
    LinearLayout title_name_rela,title_lose_dev_rela;
    TextView title_name_tv,title_lose_dev_tv;
    Context mContext;
    private ElectricDevPresenter mElectricDevPresenter;
    private String userID;
    private int privilege;

    private ElectricFragment electricFragment;
    private OffLineElectricDevFragment offLineElectricDevFragment;
    private FragmentManager fragmentManager;
    public static final int FRAGMENT_THREE = 2;
    public static final int FRAGMENT_FIVE =4;
    private int position;
    private boolean visibility = false;
    private ShopType mShopType;
    private Area mArea;
    private String areaId = "";
    private String parentId="";//@@9.1
    private String shopTypeId = "";

    @Bind(R.id.area_condition)
    AreaChooceListView areaCondition;//区域下拉选择。。
    @Bind(R.id.online_num)
    TextView onlineNum;
    @Bind(R.id.offline_num)
    TextView offlineNum;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.turn_map_btn)
    TextView turn_map_btn;

    List<Area> parent = null;//@@8.31
    Map<String, List<Area>> map = null;//@@8.31

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApp.b=System.currentTimeMillis();
        long c1=MyApp.b-MyApp.a;
        super.onCreate(savedInstanceState);
        MyApp.b=System.currentTimeMillis();
        long c2=MyApp.b-MyApp.a;
        setContentView(R.layout.activity_all_smoke);
        MyApp.b=System.currentTimeMillis();
        long c3=MyApp.b-MyApp.a;
        //透明状态栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        MyApp.b=System.currentTimeMillis();
        long c4=MyApp.b-MyApp.a;
        mContext=this;
        MyApp.b=System.currentTimeMillis();
        long c5=MyApp.b-MyApp.a;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ButterKnife.bind(this);
        init();
        title_name_tv=(TextView )findViewById(R.id.title_name_text);
        title_lose_dev_tv=(TextView)findViewById(R.id.title_lose_dev_text) ;
        title_name_rela=(LinearLayout) findViewById(R.id.title_name) ;
        title_name_rela.setEnabled(false);
        title_name_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_lose_dev_rela.setEnabled(true);
                title_name_rela.setEnabled(false);
                mvpPresenter.unSubscribe("electric");
                position=FRAGMENT_THREE;//@@在线设备
            }
        });
        title_lose_dev_rela=(LinearLayout) findViewById(R.id.title_lose_dev) ;
        title_lose_dev_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_name_rela.setEnabled(true);
                title_lose_dev_rela.setEnabled(false);
                mvpPresenter.unSubscribe("lostSmoke");
                position=FRAGMENT_FIVE;//@@离线设备
            }
        });
        title_name_tv.setText("电气防火");
        title_lose_dev_tv.setText("离线设备");
        areaCondition.setActivity(this);//@@12.21
//        shopTypeCondition.setActivity(this);//@@12.21
        areaCondition.setHintTextColor("#ffffffff");
        areaCondition.setEditTextHint("#ffffffff");
        areaCondition.setEditTextHint("选择区域");

        MyApp.b=System.currentTimeMillis();
        long c6=MyApp.b-MyApp.a;
    }

    @OnClick({ R.id.area_condition, R.id.turn_map_btn})
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
                    String url= ConstantValues.SERVER_IP_NEW+"/getAreaInfo?userId="+userID+"&privilege="+privilege;
                    VolleyHelper.getInstance(mContext).getStringResponse(url,
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
                                        areaCondition.setItemsData2(parent,map,mElectricDevPresenter );
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
//                        case FRAGMENT_THREE:
                            mvpPresenter.getNeedElectricInfo(userID, privilege + "",parentId, areaId,"", shopTypeId,"3",electricFragment);
                            mvpPresenter.getSmokeSummary(userID,privilege+"",parentId,areaId,shopTypeId,"3",electricFragment);
//                            break;
//                        case FRAGMENT_FIVE://@@6.29
                            mvpPresenter.getNeedLossSmoke(userID, privilege + "",parentId, areaId, shopTypeId, "","3",false,0,null,offLineElectricDevFragment);
                            mvpPresenter.getSmokeSummary(userID,privilege+"",parentId,areaId,shopTypeId,"3",offLineElectricDevFragment);
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
                Intent intent=new Intent(ElectricDevActivity.this, MapActivity.class);
                intent.putExtra("devType","3");
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    @Override
    protected ElectricDevPresenter createPresenter() {
        mElectricDevPresenter=new ElectricDevPresenter(this);
        return mElectricDevPresenter;
    }

    private void init() {
        fragmentManager = getFragmentManager();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        showFragment(FRAGMENT_THREE);
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
            case FRAGMENT_THREE:
//                addFire.setVisibility(View.VISIBLE);//@@5.3
                if (electricFragment == null) {
                    offLineElectricDevFragment = new OffLineElectricDevFragment();
                    ft.add(R.id.fragment_content, offLineElectricDevFragment);
                    electricFragment = new ElectricFragment();
                    ft.add(R.id.fragment_content, electricFragment);
                } else {
                    ft.show(electricFragment);
                }
                break;
            case FRAGMENT_FIVE:
//                addFire.setVisibility(View.VISIBLE);//@@5.3
                if (offLineElectricDevFragment == null) {
                    offLineElectricDevFragment = new OffLineElectricDevFragment();
                    ft.add(R.id.fragment_content, offLineElectricDevFragment);
                } else {
                    ft.show(offLineElectricDevFragment);
                }
                break;
        }
        ft.commit();
    }

    public void hideFragment(FragmentTransaction ft) {
        //如果不为空，就先隐藏起来
        if (electricFragment != null) {
            ft.hide(electricFragment);
        }
        if (offLineElectricDevFragment != null) {
            ft.hide(offLineElectricDevFragment);
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
            case "electric":
//                mElectricDevPresenter.getSmokeSummary(userID,privilege+"","","","","3",electricFragment);
//                lin1.setVisibility(View.GONE);
//                searchFire.setVisibility(View.GONE);
//                addFire.setVisibility(View.VISIBLE);
                showFragment(FRAGMENT_THREE);
                break;
            case "lostSmoke":
//                mElectricDevPresenter.getSmokeSummary(userID,privilege+"","","","","3",offLineElectricDevFragment);
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
//            shopTypeCondition.setItemsData((ArrayList<Object>) shopTypes, mElectricDevPresenter);
//            shopTypeCondition.showPopWindow();
//            shopTypeCondition.setClickable(true);
//            shopTypeCondition.closeLoading();
        } else {
            areaCondition.setItemsData((ArrayList<Object>) shopTypes, mElectricDevPresenter);
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
//                    shopTypeCondition.searchClose();
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
        mvpPresenter.getNeedElectricInfo(userID, privilege + "",parentId, areaId,"", shopTypeId,"3",electricFragment);
        mvpPresenter.getSmokeSummary(userID,privilege+"",parentId,areaId,shopTypeId,"3",electricFragment);

        mvpPresenter.getNeedLossSmoke(userID, privilege + "",parentId, areaId, shopTypeId, "","3",false,0,null,offLineElectricDevFragment);
        mvpPresenter.getSmokeSummary(userID,privilege+"",parentId,areaId,shopTypeId,"3",offLineElectricDevFragment);

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
        electricFragment.refreshView();
        offLineElectricDevFragment.refreshView();
        areaCondition.setEditText("");
    }

}
