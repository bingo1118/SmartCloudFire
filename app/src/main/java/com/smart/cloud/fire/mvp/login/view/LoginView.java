package com.smart.cloud.fire.mvp.login.view;

import com.smart.cloud.fire.mvp.login.model.LoginModel;

/**
 * Created by Administrator on 2016/9/19.
 */
public interface LoginView {
    void getDataSuccess(LoginModel model);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void autoLogin(String userId,String pwd);

    void autoLoginFail();

    void bindAlias();
}
