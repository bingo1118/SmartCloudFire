package com.smart.cloud.fire.mvp.fragment.MapFragment;

import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public interface MapFragmentView {
    void getDataSuccess(List<Smoke> smokeList);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void getShopType(ArrayList<Object> shopTypes);

    void getShopTypeFail(String msg);

    void getAreaType(ArrayList<Object> shopTypes);

    void getAreaTypeFail(String msg);

    void showSmokeDialog(Smoke smoke);

    void showAlarmDialog(Smoke smoke);

    void openCamera(Camera camera);

    void getChoiceArea(Area area);

    void getChoiceShop(ShopType shopType);
}
