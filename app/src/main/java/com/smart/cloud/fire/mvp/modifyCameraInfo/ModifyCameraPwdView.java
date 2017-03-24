package com.smart.cloud.fire.mvp.modifyCameraInfo;

/**
 * Created by Administrator on 2016/12/14.
 */
public interface ModifyCameraPwdView {
    void showLoading();
    void hideLoading();
    void modifyCameraPwdResult(String msg,String pwd);
    void errorMessage(String msg);
}
