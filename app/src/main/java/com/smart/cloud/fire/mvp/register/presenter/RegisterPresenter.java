package com.smart.cloud.fire.mvp.register.presenter;

import android.content.Context;

import com.p2p.core.utils.MD5;
import com.p2p.core.utils.MyUtils;
import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.mvp.register.model.RegisterModel;
import com.smart.cloud.fire.mvp.register.view.RegisterView;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import java.util.Random;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/9/19.
 */
public class RegisterPresenter  extends BasePresenter<RegisterView> {
    public RegisterPresenter(RegisterView view) {
        attachView(view);
    }

    public void getMesageCode(String phoneNo){
        String AppVersion = MyUtils.getBitProcessingVersion();
        mvpView.showLoading();
        Random random = new Random();
        int value = random.nextInt(4);
        addSubscription(apiStores[value].getMesageCode("86", phoneNo,AppVersion),
                new SubscriberCallBack<>(new ApiCallback<RegisterModel>() {
                    @Override
                    public void onSuccess(RegisterModel model) {
                        String errorCode = model.getError_code();
                        switch (errorCode){
                            case "0":
                                mvpView.getMesageSuccess();
                                break;
                            case "6":
                                mvpView.getDataFail("手机号已被注册");
                                break;
                            case "9":
                                mvpView.getDataFail("手机号码格式错误");
                                break;
                            case "27":
                                mvpView.getDataFail("获取手机验证码超时，请稍后再试");
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        mvpView.getDataFail(msg);
                    }
                    @Override
                    public void onCompleted() {
                        mvpView.hideLoading();
                    }
                }));
    }

    public void register(final String phoneNo, final String pwd, String rePwd, final String code, final Context mContext){
        MD5 md = new MD5();
        final String password = md.getMD5ofStr(pwd);
        final String rePassword = md.getMD5ofStr(rePwd);
        mvpView.showLoading();
        Random random = new Random();
        final int value = random.nextInt(4);
        twoSubscription(apiStores[value].verifyPhoneCode("86", phoneNo,code),new Func1<RegisterModel, Observable<RegisterModel>>(){
                    @Override
                    public Observable<RegisterModel> call(RegisterModel registerModel) {
                        return apiStores[value].register("1","","86",phoneNo,password,rePassword,code,"1");
                    }
                },
                new SubscriberCallBack<>(new ApiCallback<RegisterModel>() {
                    @Override
                    public void onSuccess(RegisterModel model) {
                        String errorCode = model.getError_code();
                        switch (errorCode){
                            case "0":
                                SharedPreferencesManager.getInstance().putData(mContext,
                                        SharedPreferencesManager.SP_FILE_GWELL,
                                        SharedPreferencesManager.KEY_RECENTPASS,
                                        pwd);
                                SharedPreferencesManager.getInstance().putData(mContext,
                                        SharedPreferencesManager.SP_FILE_GWELL,
                                        SharedPreferencesManager.KEY_RECENTNAME,
                                        phoneNo);
                                mvpView.register();
                                break;
                            case "6":
                                mvpView.getDataFail("手机号已被注册");
                                break;
                            case "9":
                                mvpView.getDataFail("手机号码格式错误");
                                break;
                            case "18":
                                mvpView.getDataFail("验证码输入错误");
                            case "10":
                                mvpView.getDataFail("两次输入的密码不一致");
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        mvpView.getDataFail(msg);
                    }
                    @Override
                    public void onCompleted() {
                        mvpView.hideLoading();
                    }
                }));
    }

}
