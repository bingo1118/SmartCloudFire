package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment;

import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public interface ShopInfoFragmentView {
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
