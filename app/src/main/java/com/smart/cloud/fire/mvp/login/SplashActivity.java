package com.smart.cloud.fire.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.mvp.login.model.LoginModel;
import com.smart.cloud.fire.mvp.login.presenter.LoginPresenter;
import com.smart.cloud.fire.mvp.login.view.LoginView;
import com.smart.cloud.fire.mvp.main.MainActivity;
import com.smart.cloud.fire.utils.T;

import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SplashActivity extends MvpActivity<LoginPresenter> implements LoginView {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        mvpPresenter.autoLogin(this);
    }

    @Override
    public void getDataSuccess(LoginModel model) {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void autoLogin(String userId, String pwd) {
        mvpPresenter.loginYooSee(userId,pwd,mContext,0);
    }

    @Override
    public void autoLoginFail() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void bindAlias() {

    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }
}
