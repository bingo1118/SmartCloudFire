package com.smart.cloud.fire.mvp.ChuangAn;

import com.smart.cloud.fire.global.ChuangAnValue;
import com.smart.cloud.fire.global.ElectricValue;

import java.util.List;

/**
 * Created by Rain on 2018/2/28.
 */
public interface ChuangAnView {
    void getDataSuccess(List<ChuangAnValue.ChuangAnValueBean> smokeList);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();
}
