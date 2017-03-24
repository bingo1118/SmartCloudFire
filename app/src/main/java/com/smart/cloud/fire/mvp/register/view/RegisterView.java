package com.smart.cloud.fire.mvp.register.view;

/**
 * Created by Administrator on 2016/9/19.
 */
public interface RegisterView {
    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void register();

    void getMesageSuccess();
}
