package com.smart.cloud.fire.activity.AddNFC;

import com.baidu.location.BDLocation;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.ArrayList;

/**
 * Created by Rain on 2017/8/15.
 */
public interface AddNFCView {
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
    void getChoiceNFCDeviceType(NFCDeviceType nfcDeviceType);
    void getNFCDeviceType(ArrayList<Object> deviceTypes);//@@8.16
    void getNFCDeviceTypeFail(String msg);//@@8.16
}
