package com.smart.cloud.fire.mvp.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.mvp.login.LoginActivity;
import com.smart.cloud.fire.mvp.login.SplashActivity;
import com.smart.cloud.fire.mvp.register.presenter.RegisterPresenter;
import com.smart.cloud.fire.mvp.register.view.RegisterView;
import com.smart.cloud.fire.utils.T;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/19.
 */
public class RegisterPhoneActivity extends MvpActivity<RegisterPresenter> implements RegisterView {
    private Context mContext;
    @Bind(R.id.register_user)
    EditText register_user;
    @Bind(R.id.register_pwd)
    EditText register_pwd;
    @Bind(R.id.register_comfire_pwd)
    EditText register_comfire_pwd;
    @Bind(R.id.register_code)
    EditText register_code;
    @Bind(R.id.register_get_code)
    Button register_get_code;
    @Bind(R.id.register_btn_phone)
    Button register_btn_phone;
    @Bind(R.id.register_old_user_tv)
    TextView register_old_user_tv;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private String phoneNO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        mContext=this;
        doAction();
    }

    private void doAction() {
        RxView.clicks(register_get_code).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        phoneNO = register_user.getText().toString().trim();
                        mvpPresenter.getMesageCode(phoneNO);
                    }
                });
        RxView.clicks(register_btn_phone).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phoneNO = register_user.getText().toString().trim();
                        String pwd = register_pwd.getText().toString().trim();
                        String rePwd = register_comfire_pwd.getText().toString().trim();
                        String code = register_code.getText().toString().trim();
                        mvpPresenter.register(phoneNO,pwd,rePwd,code,mContext);
                    }
                });
        RxView.clicks(register_old_user_tv).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //跳转到登录界面
                        Intent intent1 = new Intent(mContext,LoginActivity.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }
                });
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
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
    public void register() {
        T.showShort(mContext,"注册成功,正在登陆");
        Intent login = new Intent(mContext, SplashActivity.class);
        startActivity(login);
        finish();
    }

    @Override
    public void getMesageSuccess() {
        T.showShort(mContext,"获取验证码成功");
    }
}
