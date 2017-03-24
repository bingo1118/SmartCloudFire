package com.smart.cloud.fire.mvp.login;

/**
 * Created by Administrator on 2016/9/19.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.mvp.login.model.LoginModel;
import com.smart.cloud.fire.mvp.login.presenter.LoginPresenter;
import com.smart.cloud.fire.mvp.login.view.LoginView;
import com.smart.cloud.fire.mvp.main.MainActivity;
import com.smart.cloud.fire.mvp.register.RegisterPhoneActivity;
import com.smart.cloud.fire.utils.T;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginView{
    private Context mContext;
    @Bind(R.id.login_user)
    EditText login_user;
    @Bind(R.id.login_pwd)
    EditText login_pwd;
    @Bind(R.id.login_rela2)
    RelativeLayout login_rela2;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.login_new_register)
    TextView login_new_register;
    @Bind(R.id.login_forget_pwd)
    TextView login_forget_pwd;
    private  String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        initView();
    }

    private void initView() {
        RxView.clicks(login_rela2).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        userId = login_user.getText().toString().trim();
                        String pwd = login_pwd.getText().toString().trim();
                        mvpPresenter.loginYooSee(userId,pwd,mContext,1);
                    }
                });
        RxView.clicks(login_new_register).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(mContext, RegisterPhoneActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }
                });
        RxView.clicks(login_forget_pwd).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Uri uri = Uri.parse(ConstantValues.FORGET_PASSWORD_URL);
                        Intent open_web = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(open_web);
                    }
                });
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
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void autoLogin(String userId, String pwd) {
    }

    @Override
    public void autoLoginFail() {
    }

    @Override
    public void bindAlias() {
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

}

