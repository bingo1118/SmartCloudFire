package com.smart.cloud.fire.activity.UploadAlarmInfo;

import com.android.volley.Response;
import com.smart.cloud.fire.activity.UploadNFCInfo.FormFile;
import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;


/**
 * Created by Rain on 2018/8/7.
 */
public class UploadAlarmInfoPresenter extends BasePresenter<UploadAlarmInfoView>{

    public UploadAlarmInfoPresenter(UploadAlarmInfoView view ) {
        attachView(view);
    }


    public void uploadAlarm(String username,String mac,String alarmType){
        Observable mObservable=apiStores1.makeSureAlarm(username,mac,alarmType);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {

            @Override
            public void onSuccess(HttpError model) {
                int result=model.getErrorCode();
                mvpView.dealResult(model.getError(),result);
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.T("失败");
            }

            @Override
            public void onCompleted() {

            }
        }));
    }

    public void submitOrder(String username,String mac,String alarmTruth,String dealDetail,String imagePath,String videoPath){
        Observable mObservable=apiStores1.submitOrder(username,mac,alarmTruth,dealDetail,imagePath,videoPath);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {

            @Override
            public void onSuccess(HttpError model) {
                int result=model.getErrorCode();
                mvpView.dealResult(model.getError(),result);
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.T("失败");
            }

            @Override
            public void onCompleted() {

            }
        }));
    }
    
    public void erasure(String username,String mac,String elestate){
        Observable mObservable=apiStores1.ackNB_IOT_Control(username,mac,elestate);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int result=model.getErrorCode();
                mvpView.T(model.getError());
                mvpView.dealResult(model.getError(),result);
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.T("失败");
            }

            @Override
            public void onCompleted() {

            }
        }));
    }

    public boolean uploadImage(File file, String userId, String areaId, String uploadtime, String mac, String location){
        try {
            String requestUrl = ConstantValues.SERVER_IP_NEW+"UploadFileAction";
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", userId);
            params.put("areaId", areaId);
            params.put("time", uploadtime);
            params.put("mac", mac);
            params.put("location", location);
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)//表单类型
                    .addFormDataPart("username", userId)
                    .addFormDataPart("areaId", areaId)
                    .addFormDataPart("time", uploadtime)
                    .addFormDataPart("mac", mac)
                    .addFormDataPart("location", location);//ParamKey.TOKEN 自定义参数key常量类，即参数名
            RequestBody imageBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            builder.addFormDataPart("image", file.getName(), imageBody);//imgfile 后台接收图片流的参数名

            List<MultipartBody.Part> parts = builder.build().parts();
            Observable mObservable=apiStores1.uploadImege(parts);
            addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
                @Override
                public void onSuccess(HttpError model) {
                    mvpView.T("成功");
                }

                @Override
                public void onFailure(int code, String msg) {
                    mvpView.T("失败");
                }

                @Override
                public void onCompleted() {

                }
            }));
            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fail");
            return false;
        }
        return true;
    }
}