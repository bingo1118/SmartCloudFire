package com.smart.cloud.fire.mvp.BigDataDetail;

import com.smart.cloud.fire.base.presenter.BasePresenter;

import java.util.List;

/**
 * Created by Rain on 2019/2/15.
 */
public interface BigDataDetailView {

    void getDataSuccess(List<?> smokeList, boolean research);
    void getDataFail(String msg);
    void showLoading();
    void hideLoading();
    void onLoadingMore(List<?> smokeList);
}
