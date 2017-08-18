package com.smart.cloud.fire.activity.NFCDev;

import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rain on 2017/8/16.
 */
public interface NFCDevView {
    void getDataSuccess(List<?> smokeList,boolean research);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void onLoadingMore(List<?> smokeList);

    void getAreaType(ArrayList<?> shopTypes,int type);

    void getAreaTypeFail(String msg,int type);

    void unSubscribe(String type);

    void getLostCount(String count);

    void getChoiceArea(Area area);

    void getChoiceShop(ShopType shopType);

    void getSmokeSummary(SmokeSummary smokeSummary);
}
