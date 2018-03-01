package com.smart.cloud.fire.mvp.electricChangeHistory;

import java.util.List;

/**
 * Created by Rain on 2017/8/28.
 */
public interface ElectricChangeHistoryView {
    void getDataSuccess(List<?> smokeList, boolean research);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void onLoadingMore(List<?> smokeList);
}
