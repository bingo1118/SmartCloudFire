package com.smart.cloud.fire.activity.AllSmoke;

import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rain on 2017/7/15.
 */
public interface AllSmokeView {
    void getDataSuccess(List<?> smokeList,boolean research);
    void getSmokeSummary(SmokeSummary smokeSummary);
    void showLoading();
    void hideLoading();
    void unSubscribe(String type);
    void getAreaType(ArrayList<?> shopTypes, int type);
    void getAreaTypeFail(String msg,int type);
    void getDataFail(String msg);
    void getChoiceArea(Area area);
    void getChoiceShop(ShopType shopType);
    void onLoadingMore(List<?> smokeList);
    void refreshFragment();
}
