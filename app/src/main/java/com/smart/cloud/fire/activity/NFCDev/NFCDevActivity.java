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
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.activity.AddNFC.AddNFCMacActivity;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokeActivity;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokePresenter;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokeView;
import com.smart.cloud.fire.activity.Map.MapActivity;
import com.smart.cloud.fire.activity.NFC.ChooseConditionActivity;
import com.smart.cloud.fire.activity.UploadNFCInfo.UploadNFCInfoActivity;
import com.smart.cloud.fire.adapter.NFCDevAdapter;
import com.smart.cloud.fire.adapter.ShopCameraAdapter;
import com.smart.cloud.fire.adapter.ShopSmokeAdapter;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.global.State;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
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
    @Bind(R.id.no_online_num)
    TextView no_online_num;
    @Bind(R.id.offline_num)
    TextView offlineNum;
    @Bind(R.id.trace_search)
    ImageButton trace_search;
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
    @Bind(R.id.lin2)
    LinearLayout lin2;//搜素界面。。
    @Bind(R.id.area_condition)
    AreaChooceListView areaCondition;//区域下拉选择。。
    @Bind(R.id.shop_type_condition)
    XCDropDownListViewMapSearch shopTypeCondition;//设备类型下拉选择。。
    @Bind(R.id.state_condition)
    XCDropDownListViewMapSearch stateCondition;//状态下拉选择。。
    @Bind(R.id.search_fire)
    ImageView searchFire;//搜索按钮。。
    private boolean visibility = false;
    private ShopType mShopType;
    private State mState;
    private Area mArea;
    private String areaId = "";
    private String parentId="";//@@9.1
    private String shopTypeId = "";
    private String stateTypeId = "";

    List<Area> parent = null;//@@8.31
    Map<String, List<Area>> map = null;//@@8.31


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
        areaCondition.setIfHavaChooseAll(false);//@@11.06
        areaCondition.setActivity(this);//@@12.21
        shopTypeCondition.setActivity(this);//@@12.21
        stateCondition.setActivity(this);
        addFire.setVisibility(View.VISIBLE);//@@8.17
        addFire.setImageResource(R.drawable.search);//@@8.17
        mvpPresenter.getNFCInfo(userID, "", "",page, list, 1,false,null);
        mvpPresenter.getSmokeSummary(userID,privilege+"","","");//@@8.17
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
                mvpPresenter.getNFCInfo(userID, "" ,"", page, list, 1,true,null);
                mvpPresenter.getSmokeSummary(userID,privilege+"","","");//@@8.17
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
                        mvpPresenter.getNFCInfo(userID, "" , "", page, list, 1,true,null);//@@7.17
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

    //@@12.20
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @OnClick({R.id.state_condition,R.id.add_fire, R.id.area_condition, R.id.shop_type_condition, R.id.search_fire,R.id.turn_map_btn,R.id.trace_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_fire://显示查询条件按钮。。
                if (visibility) {
                    visibility = false;
                    lin1.setVisibility(View.GONE);
                    lin2.setVisibility(View.GONE);
                    if (areaCondition.ifShow()) {
                        areaCondition.closePopWindow();
                    }
                    if (shopTypeCondition.ifShow()) {
                        shopTypeCondition.closePopWindow();
                    }
                    if (stateCondition.ifShow()) {
                        stateCondition.closePopWindow();
                    }
                } else {
                    visibility = true;
                    areaCondition.setEditText("");
                    shopTypeCondition.setEditText("");
                    stateCondition.setEditText("");
                    areaCondition.setEditTextHint("区域");
                    shopTypeCondition.setEditTextHint("类型");
                    stateCondition.setEditTextHint("状态");
                    lin1.setVisibility(View.VISIBLE);
                    lin2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.area_condition://地区类型下拉列表。。
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                } else {
                    VolleyHelper helper=VolleyHelper.getInstance(mContext);
                    RequestQueue mQueue = helper.getRequestQueue();
//                    RequestQueue mQueue = Volley.newRequestQueue(mContext);
                    String url= ConstantValues.SERVER_IP_NEW+"getAreaInfo?userId="+userID+"&privilege="+privilege;
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
                                        areaCondition.setItemsData2(parent,map, nfcDevPresenter);
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
                if (shopTypeCondition.ifShow()) {
                    shopTypeCondition.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 1);
                    shopTypeCondition.setClickable(false);
                    shopTypeCondition.showLoading();
                }
                break;
            case R.id.state_condition://状态下拉列表。。
                if (stateCondition.ifShow()) {
                    stateCondition.closePopWindow();
                } else {
                    stateCondition.setClickable(false);
                    stateCondition.showLoading();
                    getStateType();
                }
                break;
            case R.id.search_fire://查询按钮
                if (!Utils.isNetworkAvailable(this)) {
                    return;
                }
                if (shopTypeCondition.ifShow()) {
                    shopTypeCondition.closePopWindow();
                }
                if (stateCondition.ifShow()) {
                    stateCondition.closePopWindow();
                }
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                }
                if ((mShopType != null && mShopType.getPlaceTypeId() != null) || (mArea != null && mArea.getAreaId() != null)
                        || (mState != null && mState.getStateId() != null)) {
                    lin1.setVisibility(View.GONE);
                    lin2.setVisibility(View.GONE);
                    searchFire.setVisibility(View.GONE);
                    addFire.setVisibility(View.VISIBLE);
                    areaCondition.searchClose();
                    shopTypeCondition.searchClose();
                    stateCondition.searchClose();
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
                    }
                    if (mShopType != null && mShopType.getPlaceTypeId() != null) {
                        shopTypeId = mShopType.getPlaceTypeId();
                    } else {
                        shopTypeId = "";
                    }
                    if (mState != null && mState.getStateId() != null) {
                        stateTypeId = mState.getStateId();
                    } else {
                        stateTypeId = "";
                    }
                    mvpPresenter.getNeedNFC(userID, areaId,"",shopTypeId,stateTypeId);//显示设备。。
                    mvpPresenter.getSmokeSummary(userID,privilege+"",areaId,shopTypeId);//显示总数。。;
                    mShopType = null;
                    mArea = null;
                    stateTypeId=null;
                } else {
                    lin1.setVisibility(View.GONE);
                    lin2.setVisibility(View.GONE);
                    return;
                }
                break;
            case R.id.turn_map_btn:
                Intent intent=new Intent(NFCDevActivity.this, MapActivity.class);
                intent.putExtra("devType","7");
                startActivity(intent);
                break;
            case R.id.trace_search:
                showPopupWindow(view);
//                Intent intent1=new Intent(NFCDevActivity.this, ChooseConditionActivity.class);
//                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.nfc_device_menu, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT , true);

        RelativeLayout nfc_mac_add=(RelativeLayout)contentView.findViewById(R.id.nfc_mac_add);
        if(privilege==4){
            nfc_mac_add.setVisibility(View.VISIBLE);
        }else{
            nfc_mac_add.setVisibility(View.GONE);
        }
        nfc_mac_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(mContext, AddNFCMacActivity.class);
                startActivity(intent6);
                popupWindow.dismiss();
            }
        });

        RelativeLayout nfc_all_history=(RelativeLayout)contentView.findViewById(R.id.nfc_all_history);
        nfc_all_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NFCDevHistoryActivity.class);
                intent.putExtra("uid","");
                intent.putExtra("name","历史巡检记录");
                mContext.startActivity(intent);
                popupWindow.dismiss();
            }
        });


        RadioGroup nfc_radiogroup=(RadioGroup)contentView.findViewById(R.id.nfc_radiogroup);
        RadioButton everymonth=(RadioButton)contentView.findViewById(R.id.everymonth);
        RadioButton everyweek=(RadioButton)contentView.findViewById(R.id.everyweek);
        RadioButton everyday=(RadioButton)contentView.findViewById(R.id.everyday);
        RelativeLayout track_rela=(RelativeLayout)contentView.findViewById(R.id.setting_nfc_track);
        track_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(NFCDevActivity.this, ChooseConditionActivity.class);
                startActivity(intent1);
                popupWindow.dismiss();
            }
        });
        RelativeLayout period_rela=(RelativeLayout)contentView.findViewById(R.id.setting_nfc_period);
        RelativeLayout nfc_rela=(RelativeLayout)contentView.findViewById(R.id.setting_nfc);
        nfc_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, UploadNFCInfoActivity.class);
                startActivity(intent3);
                popupWindow.dismiss();
            }
        });

        int peroid=SharedPreferencesManager.getInstance().getIntData(mContext,"NFC_period");//@@10.24
        switch (peroid){
            case 0:
                everymonth.setChecked(true);
                break;
            case 1:
                everyweek.setChecked(true);
                break;
            case 2:
                everyday.setChecked(true);
                break;
            default:
                everymonth.setChecked(true);
                break;
        }//@@10.24

        nfc_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int period=0;
                switch (checkedId){
                    case R.id.everymonth:
                        period=0;
                        break;
                    case R.id.everyweek:
                        period=1;
                        break;
                    case R.id.everyday:
                        period=2;
                        break;
                    default:
                        period=0;
                        break;
                }
                SharedPreferencesManager.getInstance().putData(mContext,"NFC_period",period);
                T.showShort(mContext,"设置成功");
            }
        });




        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        backgroundAlpha(0.5f);//@@12.20开启时其他区域半透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable( R.drawable.list_item_color_bg));
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
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

    public void getStateType() {
        ArrayList<Object>  shopTypes=new ArrayList<>();
        State a=new State();
        a.setStateId("1");
        a.setStateName("合格");
        shopTypes.add(a);
        State b=new State();
        b.setStateId("2");
        b.setStateName("不合格");
        shopTypes.add(b);
        State c=new State();
        c.setStateId("0");
        c.setStateName("待检");
        shopTypes.add(c);
        stateCondition.setItemsData( shopTypes, nfcDevPresenter);
        stateCondition.showPopWindow();
        stateCondition.setClickable(true);
        stateCondition.closeLoading();
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
    public void getChoiceState(State stateType) {
        mState = stateType;
        if (mState != null && mState.getStateId() != null) {
            addFire.setVisibility(View.GONE);
            searchFire.setVisibility(View.VISIBLE);
        }
        if (mState.getStateId() == null && mArea == null) {
            addFire.setVisibility(View.VISIBLE);
            searchFire.setVisibility(View.GONE);
        } else if (mState.getStateId() == null && mArea != null && mArea.getAreaId() == null) {
            addFire.setVisibility(View.VISIBLE);
            searchFire.setVisibility(View.GONE);
        }
    }

    @Override
    public void getSmokeSummary(SmokeSummary smokeSummary) {
        totalNum.setText(smokeSummary.getAllSmokeNumber()+"");
        onlineNum.setText(smokeSummary.getOnlineSmokeNumber()+"");
        no_online_num.setText((smokeSummary.getAllSmokeNumber()-smokeSummary.getLossSmokeNumber()-smokeSummary.getOnlineSmokeNumber())+"");
        offlineNum.setText(smokeSummary.getLossSmokeNumber()+"");
    }

}
