package com.smart.cloud.fire.mvp.login.presenter;

import android.app.Activity;
import android.content.Context;

import com.p2p.core.utils.MD5;
import com.p2p.core.utils.MyUtils;
import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Account;
import com.smart.cloud.fire.global.AccountPersist;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.NpcCommon;
import com.smart.cloud.fire.mvp.login.model.LoginModel;
import com.smart.cloud.fire.mvp.login.view.LoginView;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;

import java.util.Random;

import rx.Observable;
import rx.functions.Func1;

/**
 *
 * Created by Administrator on 2016/9/19.
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    private int loginCount=0;
    public LoginPresenter(LoginView view) {
        attachView(view);
    }

    public void loginYooSee(final String User, final String Pwd, final Context context, final int type) {
        String AppVersion = MyUtils.getBitProcessingVersion();
        MD5 md = new MD5();
        String password = md.getMD5ofStr(Pwd);
        if(type==1){
            mvpView.showLoading();
        }
        Random random = new Random();
        int value = random.nextInt(4);
        String userId;
        if (Utils.isNumeric(User)) {
                userId = "+86-"+User;
        }else{
            mvpView.getDataFail("用户不存在");
            return;
        }
        Observable<LoginModel> observable = apiStores[value].loginYooSee(userId, password, "1", "3", AppVersion);
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {
                String errorCode = model.getError_code();
                if(errorCode.equals("0")){
                    editSharePreference(context,model,User,Pwd);
                    loginServer(User);
                }else{
                    mvpView.hideLoading();
                    switch (errorCode){
                        case "2":
                            T.showShort(context,"用户不存在");
                            break;
                        case "3":
                            T.showShort(context,"密码错误");
                            break;
                        case "9":
                            T.showShort(context,"用户名不能为空");
                            break;
                        default:
                            break;
                    }
                    if(type==0){
                        mvpView.autoLoginFail();
                    }
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if(loginCount<4){
                    loginCount=loginCount+1;
                    loginYooSee(User,Pwd,context,type);
                }else{
                    mvpView.hideLoading();
                    mvpView.getDataFail("网络错误，请检查网络");
                }
            }

            @Override
            public void onCompleted() {
            }
        }));
    }

    private void loginServer(String userId){
        Observable<LoginModel> observable = apiStores1.login(userId);
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    MyApp.app.setPrivilege(model.getPrivilege());
                    mvpView.getDataSuccess(model);
                }else{
                    mvpView.getDataFail("登录失败，请重新登录");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误，请检查网络");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    public void loginYoosee(final String User, final String Pwd, final Context context, final int type) {
        String AppVersion = MyUtils.getBitProcessingVersion();
        String userid = "+86-"+User;
        MD5 md = new MD5();
        String password = md.getMD5ofStr(Pwd);
        if(type==1){
            mvpView.showLoading();
        }
        Random random = new Random();
        int value = random.nextInt(4);
        twoSubscription(apiStores[value].loginYooSee(userid, password, "1", "3", AppVersion), new Func1<LoginModel,Observable<LoginModel>>() {
            @Override
            public Observable<LoginModel> call(LoginModel loginModel) {
                if(loginModel.getError_code().equals("0")){
                    editSharePreference(context,loginModel,User,Pwd);
                    return apiStores1.login(User);
                }else {
                    Observable<LoginModel> observable = Observable.just(loginModel);
                    return observable;
                }
            }
        },
        new SubscriberCallBack<>(new ApiCallback<LoginModel>() {
                    @Override
                    public void onSuccess(LoginModel model) {
                        String error_code = model.getError_code();
                        if(error_code==null){
                            int errorCode = model.getErrorCode();
                            switch (errorCode){
                                case 0:
                                    MyApp.app.setPrivilege(model.getPrivilege());
                                    mvpView.getDataSuccess(model);
                                    break;
                                default:
                                    if(type==0){
                                        mvpView.autoLoginFail();
                                    }
                                    T.showShort(context,"您还没有权限，请联系管理员");
                                    break;
                            }
                        }else{
                            switch (error_code){
                                case "2":
                                    T.showShort(context,"用户不存在");
                                    break;
                                case "3":
                                    T.showShort(context,"密码错误");
                                    break;
                                case "9":
                                    T.showShort(context,"用户名不能为空");
                                    break;
                                default:
                                    break;
                            }
                            if(type==0){
                                mvpView.autoLoginFail();
                            }
                        }
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        mvpView.getDataFail(msg);
                    }
                    @Override
                    public void onCompleted() {
                        if(type==1){
                            mvpView.hideLoading();
                        }
                    }
                }));
    }

    private void editSharePreference(Context mContext, LoginModel object, String userId, String userPwd){
        String userID = "0"+String.valueOf((Integer.parseInt(object.getUserID())&0x7fffffff));
        SharedPreferencesManager.getInstance().putData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS,
                userPwd);
        SharedPreferencesManager.getInstance().putData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME,
                userId);
        SharedPreferencesManager.getInstance().putData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS_NUMBER, userID);
        String codeStr1 = String.valueOf(Long.parseLong(object.getP2PVerifyCode1()));
        String codeStr2 = String.valueOf(Long.parseLong(object.getP2PVerifyCode2()));
        Account account = AccountPersist.getInstance()
                .getActiveAccountInfo(mContext);
        if (null == account) {
            account = new Account();
        }
        account.three_number = userID;
        account.phone = object.getPhoneNO();
        account.email = object.getEmail();
        account.sessionId = object.getSessionID();
        account.rCode1 = codeStr1;
        account.rCode2 = codeStr2;
        account.countryCode = object.getCountryCode();
        AccountPersist.getInstance()
                .setActiveAccount(mContext, account);
        NpcCommon.mThreeNum = AccountPersist.getInstance()
                .getActiveAccountInfo(mContext).three_number;
    }

    public void autoLogin(Activity activity){
        if(Utils.isNetworkAvailable(activity)){
            String userId = SharedPreferencesManager.getInstance().getData(activity, SharedPreferencesManager.SP_FILE_GWELL, SharedPreferencesManager.KEY_RECENTNAME);
            String userPwd = SharedPreferencesManager.getInstance().getData(activity, SharedPreferencesManager.SP_FILE_GWELL, SharedPreferencesManager.KEY_RECENTPASS);
            if(userId!=null&&userId.length()>0&&userPwd!=null&&userPwd.length()>0){
                mvpView.autoLogin(userId,userPwd);
            }else{
                mvpView.autoLoginFail();
            }
        }else {
            mvpView.autoLoginFail();
        }
    }
}
