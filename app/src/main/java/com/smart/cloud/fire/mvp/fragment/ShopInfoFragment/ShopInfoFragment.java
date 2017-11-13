package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.AllDevFragment.AllDevFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.CameraFragment.CameraFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Electric.ElectricFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.OffLineDevFragment.OffLineDevFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment.WiredDevFragment;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.view.TopIndicator;
import com.smart.cloud.fire.view.XCDropDownListViewMapSearch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ShopInfoFragment extends MvpFragment<ShopInfoFragmentPresenter> implements ShopInfoFragmentView, TopIndicator.OnTopIndicatorListener {
    @Bind(R.id.top_indicator)
    TopIndicator topIndicator;//顶部导航。。
    @Bind(R.id.area_condition)
    XCDropDownListViewMapSearch areaCondition;//区域下拉选择。。
    @Bind(R.id.shop_type_condition)
    XCDropDownListViewMapSearch shopTypeCondition;//商铺类型下拉选择。。
    @Bind(R.id.lin1)
    LinearLayout lin1;//搜素界面。。
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.add_fire)
    ImageView addFire;//显示搜索界面按钮。。
    @Bind(R.id.search_fire)
    ImageView searchFire;//搜索按钮。。
    @Bind(R.id.lost_count)
    TextView lostCount;
    @Bind(R.id.total_num)
    TextView totalNum;
    @Bind(R.id.online_num)
    TextView onlineNum;
    @Bind(R.id.offline_num)
    TextView offlineNum;
    @Bind(R.id.smoke_total)
    LinearLayout smokeTotal;
    private Context mContext;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;
    private String userID;
    private int privilege;
    private AllDevFragment allDevFragment;
    private CameraFragment cameraFragment;
    private OffLineDevFragment offLineDevFragment;
    private FragmentManager fragmentManager;
    private ElectricFragment electricFragment;
    private WiredDevFragment wiredDevFragment;//@@6.29
    public static final int FRAGMENT_ONE = 0;
    public static final int FRAGMENT_TWO = 1;//@@6.29有线系统
    public static final int FRAGMENT_THREE = 2;
    public static final int FRAGMENT_FOUR = 3;
    public static final int FRAGMENT_FIVE =4;
    private int position;
    private boolean visibility = false;
    private ShopType mShopType;
    private Area mArea;
    private String areaId = "";
    private String shopTypeId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentManager = getChildFragmentManager();
        mContext = getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        topIndicator.setOnTopIndicatorListener(this);
        showFragment(FRAGMENT_ONE);
        addFire.setVisibility(View.VISIBLE);
        addFire.setImageResource(R.drawable.search);
        smokeTotal.setVisibility(View.VISIBLE);
//        mShopInfoFragmentPresenter.getSmokeSummary(userID,privilege+"","");
    }

    @OnClick({R.id.add_fire, R.id.area_condition, R.id.shop_type_condition, R.id.search_fire})
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
                if (!Utils.isNetworkAvailable(getActivity())) {
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
                    //判断当前在哪个子fragment。。
                    switch (position) {
                        case FRAGMENT_ONE:
                            mvpPresenter.getNeedSmoke(userID, privilege + "", areaId, shopTypeId, allDevFragment);//显示设备。。
//                            mvpPresenter.getSmokeSummary(userID,privilege+"",areaId);//显示总数。。
                            break;
                        case FRAGMENT_THREE://@@6.29序号递增
                            mvpPresenter.getNeedElectricInfo(userID, privilege + "", areaId, shopTypeId,"",electricFragment);
                            break;
                        case FRAGMENT_FOUR://@@6.29
                            break;
                        case FRAGMENT_FIVE://@@6.29
                            mvpPresenter.getNeedLossSmoke(userID, privilege + "", areaId, shopTypeId, "",false,0,null,offLineDevFragment);
//                            mvpPresenter.getNeedLossSmoke(userID, privilege + "", areaId, shopTypeId, "", false, offLineDevFragment);
//                            mvpPresenter.getSmokeSummary(userID,privilege+"",areaId);
                            break;
                        default:
                            break;
                    }
                    mShopType = null;
                    mArea = null;
                } else {
                    lin1.setVisibility(View.GONE);
                    return;
                }
                break;
            default:
                break;
        }
    }

    public void showFragment(int index) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        hideFragment(ft);
        //注意这里设置位置
        position = index;
        if (areaCondition.ifShow()) {
            areaCondition.closePopWindow();
        }//@@5.5关闭下拉选项
        if (shopTypeCondition.ifShow()) {
            shopTypeCondition.closePopWindow();
        }//@@5.5关闭下拉选项
        switch (index) {
            case FRAGMENT_ONE:
                addFire.setVisibility(View.VISIBLE);//@@5.3
            if (allDevFragment == null) {
                allDevFragment = new AllDevFragment();
                ft.add(R.id.fragment_content, allDevFragment);
            } else {
                ft.show(allDevFragment);
            }
            break;
            case FRAGMENT_TWO:
                addFire.setVisibility(View.VISIBLE);//@@5.3
                if (wiredDevFragment == null) {
                    wiredDevFragment = new WiredDevFragment();
                    ft.add(R.id.fragment_content, wiredDevFragment);
                } else {
                    ft.show(wiredDevFragment);
                }
                break;
            case FRAGMENT_THREE:
                addFire.setVisibility(View.VISIBLE);//@@5.3
                if (electricFragment == null) {
                    electricFragment = new ElectricFragment();
                    ft.add(R.id.fragment_content, electricFragment);
                } else {
                    ft.show(electricFragment);
                }
                break;
            case FRAGMENT_FOUR:
                addFire.setVisibility(View.GONE);//视频界面没有搜索功能@@5.3
                if (cameraFragment == null) {
                    cameraFragment = new CameraFragment();
                    ft.add(R.id.fragment_content, cameraFragment);
                } else {
                    ft.show(cameraFragment);
                }
                break;
            case FRAGMENT_FIVE:
                addFire.setVisibility(View.VISIBLE);//@@5.3
                if (offLineDevFragment == null) {
                    offLineDevFragment = new OffLineDevFragment();
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
        if (allDevFragment != null) {
            ft.hide(allDevFragment);
        }
        if (cameraFragment != null) {
            ft.hide(cameraFragment);
        }
        if (offLineDevFragment != null) {
            ft.hide(offLineDevFragment);
        }
        if (wiredDevFragment != null) {
            ft.hide(wiredDevFragment);
        }
        if (electricFragment != null) {
            ft.hide(electricFragment);
        }
    }

    @Override
    protected ShopInfoFragmentPresenter createPresenter() {
        mShopInfoFragmentPresenter = new ShopInfoFragmentPresenter(this, ShopInfoFragment.this);
        return mShopInfoFragmentPresenter;
    }

    @Override
    public String getFragmentName() {
        return "ShopInfoFragment";
    }

    @Override
    public void onIndicatorSelected(int index) {
        topIndicator.setTabsDisplay(mContext, index);
        switch (index) {
            case 0:
                smokeTotal.setVisibility(View.VISIBLE);
                mvpPresenter.unSubscribe("allSmoke");
                break;
            case 2:
                smokeTotal.setVisibility(View.GONE);
                mvpPresenter.unSubscribe("electric");
                break;
            case 3:
                smokeTotal.setVisibility(View.GONE);
                mvpPresenter.unSubscribe("allCamera");
                break;
            case 4:
                smokeTotal.setVisibility(View.VISIBLE);
                mvpPresenter.unSubscribe("lostSmoke");
                break;
            case 1:
                smokeTotal.setVisibility(View.GONE);
                mvpPresenter.unSubscribe("wiredSmoke");
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (allDevFragment != null) {
            allDevFragment = null;
        }
        if (cameraFragment != null) {
            cameraFragment = null;
        }
        if (offLineDevFragment != null) {
            offLineDevFragment = null;
        }
        if (electricFragment != null) {
            electricFragment = null;
        }
        if(wiredDevFragment!=null){
            wiredDevFragment=null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getDataSuccess(List<?> smokeList,boolean search) {
    }

    @Override
    public void getDataFail(String msg) {
        T.show(mContext,msg, Toast.LENGTH_SHORT);//@@4.27
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
    }

    @Override
    public void getAreaType(ArrayList<?> shopTypes, int type) {
        if (type == 1) {
            shopTypeCondition.setItemsData((ArrayList<Object>) shopTypes, mShopInfoFragmentPresenter);
            shopTypeCondition.showPopWindow();
            shopTypeCondition.setClickable(true);
            shopTypeCondition.closeLoading();
        } else {
            areaCondition.setItemsData((ArrayList<Object>) shopTypes, mShopInfoFragmentPresenter);
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
        switch (type) {
            case "allSmoke":
//                mShopInfoFragmentPresenter.getSmokeSummary(userID,privilege+"","");
                lin1.setVisibility(View.GONE);
                searchFire.setVisibility(View.GONE);
                addFire.setVisibility(View.VISIBLE);
                showFragment(FRAGMENT_ONE);
                break;
            case "allCamera":
                lin1.setVisibility(View.GONE);
                searchFire.setVisibility(View.GONE);
                addFire.setVisibility(View.VISIBLE);
                showFragment(FRAGMENT_FOUR);
                break;
            case "lostSmoke":
//                mShopInfoFragmentPresenter.getSmokeSummary(userID,privilege+"","");
                lin1.setVisibility(View.GONE);
                searchFire.setVisibility(View.GONE);
                addFire.setVisibility(View.VISIBLE);
                showFragment(FRAGMENT_FIVE);
                break;
            case "electric":
                lin1.setVisibility(View.GONE);
                searchFire.setVisibility(View.GONE);
                addFire.setVisibility(View.VISIBLE);
                showFragment(FRAGMENT_THREE);
                break;
            case "wiredSmoke":
                lin1.setVisibility(View.GONE);
                searchFire.setVisibility(View.GONE);
                addFire.setVisibility(View.VISIBLE);
                showFragment(FRAGMENT_TWO);
                break;
            default:
                break;
        }
    }

    @Override
    public void getLostCount(String count) {
        int len = count.length();
        if (len > 3) {
            lostCount.setTextSize(10);
        }
        lostCount.setText(count);
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

    @Override
    public void refreshView() {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (areaCondition.ifShow()) {
            areaCondition.closePopWindow();
        }//@@5.5关闭下拉选项
        if (shopTypeCondition.ifShow()) {
            shopTypeCondition.closePopWindow();
        }//@@5.5关闭下拉选项
    }
}
