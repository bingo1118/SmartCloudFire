package com.smart.cloud.fire.activity.Inspection.AddInspectionItem;

import com.baidu.location.BDLocation;
import com.smart.cloud.fire.activity.AddNFC.NFCDeviceType;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.Point;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.ArrayList;

public interface AddInspectionItemView {
    void getLocationData(BDLocation location);
    void showLoading();
    void hideLoading();
    void getDataFail(String msg);
    void getDataSuccess(Smoke smoke);
    void getPoints(ArrayList<Object> shopTypes);
    void getPointsFail(String msg);
    void getShopType(ArrayList<Object> shopTypes);
    void getShopTypeFail(String msg);
    void getAreaType(ArrayList<Object> shopTypes);
    void getAreaTypeFail(String msg);
    void addSmokeResult(String msg,int errorCode);
    void getChoiceArea(Area area);
    void getChoiceShop(ShopType shopType);
    void getChoicePoint(Point point);
    void getChoiceNFCDeviceType(NFCDeviceType nfcDeviceType);
    void getNFCDeviceType(ArrayList<Object> deviceTypes);//@@8.16
    void getNFCDeviceTypeFail(String msg);//@@8.16
}
