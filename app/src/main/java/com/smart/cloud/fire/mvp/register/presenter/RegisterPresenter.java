package com.smart.cloud.fire.mvp.register.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.p2p.core.utils.MD5;
import com.p2p.core.utils.MyUtils;
import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.mvp.register.model.RegisterModel;
import com.smart.cloud.fire.mvp.register.view.RegisterView;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

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

    /**
     * 检查手机号码是否注册过。。
     * @param phoneNo
     */
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

    /**
     * 注册账号
     * @param phoneNo
     * @param pwd
     * @param rePwd
     * @param code
     * @param mContext
     */
    public void register(final String phoneNo, final String pwd, String rePwd, final String code, final Context mContext){
        MD5 md = new MD5();
        final String password = md.getMD5ofStr(pwd);
        final String rePassword = md.getMD5ofStr(rePwd);
        mvpView.showLoading();
        Random random = new Random();
        final int value = random.nextInt(4);//随机取出技威四个服务器中的一个进行访问
        twoSubscription(apiStores[value].verifyPhoneCode("86", phoneNo,code),new Func1<RegisterModel, Observable<RegisterModel>>(){
                    @Override
                    public Observable<RegisterModel> call(RegisterModel registerModel) {
                        //验证码验证通过后，在技威上进行注册
                        return apiStores[value].register("1","","86",phoneNo,password,rePassword,code,"1");
                    }
                },
                new SubscriberCallBack<>(new ApiCallback<RegisterModel>() {
                    @Override
                    public void onSuccess(RegisterModel model) {
                        String errorCode = model.getError_code();
                        switch (errorCode){
                            case "0":
                                addUser(phoneNo,pwd,mContext);//@@9.28添加技威注册后在我司服务器添加账号信息
//                                SharedPreferencesManager.getInstance().putData(mContext,
//                                        SharedPreferencesManager.SP_FILE_GWELL,
//                                        SharedPreferencesManager.KEY_RECENTPASS,
//                                        pwd);
//                                SharedPreferencesManager.getInstance().putData(mContext,
//                                        SharedPreferencesManager.SP_FILE_GWELL,
//                                        SharedPreferencesManager.KEY_RECENTNAME,
//                                        phoneNo);
//                                mvpView.register();//注册成功后跳转到欢迎页
                                break;
                            case "6":
                                mvpView.getDataFail("手机号已被注册");
                                break;
                            case "9":
                                mvpView.getDataFail("手机号码格式错误");
                                break;
                            case "18":
                                mvpView.getDataFail("验证码输入错误");
                                break;
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

    private void addUser(final String phoneNo, final String pwd, final Context mContext) {
//        final RequestQueue mQueue = Volley.newRequestQueue(mContext);
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url= ConstantValues.SERVER_IP_NEW+"AddUserAction?userId="+phoneNo+"&pwd="+pwd;
        final StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getInt("errorCode")==0){
                                SharedPreferencesManager.getInstance().putData(mContext,
                                        SharedPreferencesManager.SP_FILE_GWELL,
                                        SharedPreferencesManager.KEY_RECENTPASS,
                                        pwd);
                                SharedPreferencesManager.getInstance().putData(mContext,
                                        SharedPreferencesManager.SP_FILE_GWELL,
                                        SharedPreferencesManager.KEY_RECENTNAME,
                                        phoneNo);
                                mvpView.register();//注册成功后跳转到欢迎页
                            }else{
                                T.showShort(mContext,"注册错误");
                            }
                        } catch (JSONException e) {
                            T.showShort(mContext,"注册错误");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(mContext,"注册错误");
                Log.e("error","error");
            }
        });
        mQueue.add(stringRequest);
    }

}
