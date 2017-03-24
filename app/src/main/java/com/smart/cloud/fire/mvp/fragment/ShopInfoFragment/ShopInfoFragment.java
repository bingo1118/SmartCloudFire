package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.AllDevFragment.AllDevFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.CameraFragment.CameraFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Electric.ElectricFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.OffLineDevFragment.OffLineDevFragment;
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
    TopIndicator topIndicator;
    @Bind(R.id.area_condition)
    XCDropDownListViewMapSearch areaCondition;
    @Bind(R.id.shop_type_condition)
    XCDropDownListViewMapSearch shopTypeCondition;
    @Bind(R.id.lin1)
    LinearLayout lin1;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.add_fire)
    ImageView addFire;
    @Bind(R.id.search_fire)
    ImageView searchFire;
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
    public static final int FRAGMENT_ONE = 0;
    public static final int FRAGMENT_TWO = 1;
    public static final int FRAGMENT_THREE = 2;
    public static final int FRAGMENT_FOUR = 3;
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
        mShopInfoFragmentPresenter.getSmokeSummary(userID,privilege+"","");
    }

    @OnClick({R.id.add_fire, R.id.area_condition, R.id.shop_type_condition, R.id.search_fire})
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.area_condition:
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                    areaCondition.setClickable(false);
                    areaCondition.showLoading();
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
            case R.id.search_fire:
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
                    switch (position) {
                        case FRAGMENT_ONE:
                            mvpPresenter.getNeedSmoke(userID, privilege + "", areaId, shopTypeId, allDevFragment);
                            mvpPresenter.getSmokeSummary(userID,privilege+"",areaId);
                            break;
                        case FRAGMENT_TWO:
                            mvpPresenter.getNeedElectricInfo(userID, privilege + "", areaId, shopTypeId,"",electricFragment);
                            break;
                        case FRAGMENT_THREE:
                            break;
                        case FRAGMENT_FOUR:
                            mvpPresenter.getNeedLossSmoke(userID, privilege + "", areaId, shopTypeId, "",false,0,null,offLineDevFragment);
//                            mvpPresenter.getNeedLossSmoke(userID, privilege + "", areaId, shopTypeId, "", false, offLineDevFragment);
                            mvpPresenter.getSmokeSummary(userID,privilege+"",areaId);
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
        switch (index) {
            case FRAGMENT_ONE:
                if (allDevFragment == null) {
                    allDevFragment = new AllDevFragment();
                    ft.add(R.id.fragment_content, allDevFragment);
                } else {
                    ft.show(allDevFragment);
                }
                break;
            case FRAGMENT_TWO:
                if (electricFragment == null) {
                    electricFragment = new ElectricFragment();
                    ft.add(R.id.fragment_content, electricFragment);
                } else {
                    ft.show(electricFragment);
                }
                break;
            case FRAGMENT_THREE:
                if (cameraFragment == null) {
                    cameraFragment = new CameraFragment();
                    ft.add(R.id.fragment_content, cameraFragment);
                } else {
                    ft.show(cameraFragment);
                }
                break;
            case FRAGMENT_FOUR:
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
            case 1:
                smokeTotal.setVisibility(View.GONE);
                mvpPresenter.unSubscribe("electric");
                break;
            case 2:
                smokeTotal.setVisibility(View.GONE);
                mvpPresenter.unSubscribe("allCamera");
                break;
            case 3:
                smokeTotal.setVisibility(View.VISIBLE);
                mvpPresenter.unSubscribe("lostSmoke");
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
                mShopInfoFragmentPresenter.getSmokeSummary(userID,privilege+"","");
                lin1.setVisibility(View.GONE);
                searchFire.setVisibility(View.GONE);
                addFire.setVisibility(View.VISIBLE);
                showFragment(FRAGMENT_ONE);
                break;
            case "allCamera":
                lin1.setVisibility(View.GONE);
                searchFire.setVisibility(View.GONE);
                addFire.setVisibility(View.VISIBLE);
                showFragment(FRAGMENT_THREE);
                break;
            case "lostSmoke":
                mShopInfoFragmentPresenter.getSmokeSummary(userID,privilege+"","");
                lin1.setVisibility(View.GONE);
                searchFire.setVisibility(View.GONE);
                addFire.setVisibility(View.VISIBLE);
                showFragment(FRAGMENT_FOUR);
                break;
            case "electric":
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
}
