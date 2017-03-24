package com.smart.cloud.fire.mvp.fragment.ConfireFireFragment;

import com.baidu.location.BDLocation;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/21.
 */
public interface ConfireFireFragmentView {
    void getLocationData(BDLocation location);
    void showLoading();
    void hideLoading();
    void getDataFail(String msg);
    void getDataSuccess(Smoke smoke);
    void getShopType(ArrayList<Object> shopTypes);
    void getShopTypeFail(String msg);
    void getAreaType(ArrayList<Object> shopTypes);
    void getAreaTypeFail(String msg);
    void addSmokeResult(String msg,int errorCode);
    void getChoiceArea(Area area);
    void getChoiceShop(ShopType shopType);
}
