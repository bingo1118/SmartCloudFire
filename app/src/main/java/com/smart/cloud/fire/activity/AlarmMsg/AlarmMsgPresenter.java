package com.smart.cloud.fire.activity.AlarmMsg;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.NpcCommon;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.CollectFragment.AlarmMessageModel;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.utils.T;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rain on 2018/8/16.
 */
public class AlarmMsgPresenter extends BasePresenter<AlarmMsgView> {
    public AlarmMsgPresenter(AlarmMsgView view ) {
        attachView(view);
    }

    //type:1表示获取第一页的报警消息，2表示根据条件查询相应的报警消息,3查询
    public void getNeedAlarmMsg(String userId, String privilege, String page, final int type, String startTime, String endTime
            , String areaId, String placeTypeId,String parentId,String grade,String distance,String progress){
        mvpView.showLoading();
        if(!NpcCommon.verifyNetwork(MyApp.app)){
            T.showShort(MyApp.app,"无网络状态，加载缓存内容...");
            List<AlarmMessageModel> AlarmMsgs = LitePal.where(" mac like ? ",  "%").find(AlarmMessageModel.class);
            if(AlarmMsgs.size()>0){
                mvpView.getDataSuccess(AlarmMsgs);
                T.showShort(MyApp.app,"加载完成");
                mvpView.hideLoading();
            }else{
                T.showShort(MyApp.app,"无缓存数据");
                mvpView.hideLoading();
            }
            return;
        }
        Observable observable=null;
        observable = apiStores1.getNeedAlarmMsg(userId,privilege,startTime,endTime,areaId,placeTypeId,page,parentId,grade,distance,progress);

        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    List<AlarmMessageModel> alarmMessageModels = model.getAlarm();
                    if(type==1){
                        mvpView.getDataSuccess(alarmMessageModels);
                    }else if(type==3){
                        mvpView.getOfflineDataSuccess(alarmMessageModels);
                    }else{
                        mvpView.getDataByCondition(alarmMessageModels);
                    }

                }else{
                    List<AlarmMessageModel> alarmMessageModels = new ArrayList<AlarmMessageModel>();//@@5.3
                    if(type==3){
                        mvpView.getOfflineDataSuccess(alarmMessageModels);
                    }else{
                        mvpView.getDataSuccess(alarmMessageModels);//@@5.3
                        mvpView.getDataFail("无数据");
                    }
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    public void getNeedOrderMsg (String userId, String privilege, String page, String startTime, String endTime
            , String grade,String progress, final int type){
        mvpView.showLoading();
        if(!NpcCommon.verifyNetwork(MyApp.app)){
            T.showShort(MyApp.app,"无网络状态，加载缓存内容...");
            List<AlarmMessageModel> AlarmMsgs = LitePal.where(" mac like ? ",  "%").find(AlarmMessageModel.class);
            if(AlarmMsgs.size()>0){
                mvpView.getDataSuccess(AlarmMsgs);
                T.showShort(MyApp.app,"加载完成");
                mvpView.hideLoading();
            }else{
                T.showShort(MyApp.app,"无缓存数据");
                mvpView.hideLoading();
            }
            return;
        }
        Observable observable=null;
        observable = apiStores1.getNeedOrderMsg(userId,privilege,page,grade,progress);

        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    List<AlarmMessageModel> alarmMessageModels = model.getAlarm();
                    if(type==1){
                        mvpView.getDataSuccess(alarmMessageModels);
                    }else if(type==3){
                        mvpView.getOfflineDataSuccess(alarmMessageModels);
                    }else{
                        mvpView.getDataByCondition(alarmMessageModels);
                    }

                }else{
                    List<AlarmMessageModel> alarmMessageModels = new ArrayList<AlarmMessageModel>();//@@5.3
                    if(type==3){
                        mvpView.getOfflineDataSuccess(alarmMessageModels);
                    }else{
                        mvpView.getDataSuccess(alarmMessageModels);//@@5.3
                        mvpView.getDataFail("无数据");
                    }
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    public void dealAlarm(String userId, String smokeMac, String privilege, final int index){//@@5.19添加取消报警信息的位置
        mvpView.showLoading();
        Observable mObservable = apiStores1.dealAlarm(userId,smokeMac);
        final Observable Observable2 = apiStores1.getAllAlarm(userId,privilege,"1");
        twoSubscription(mObservable, new Func1<HttpError,Observable<HttpError>>() {
            @Override
            public Observable<HttpError> call(HttpError httpError) {
                int errorCode = httpError.getErrorCode();
                if(errorCode==0){
                    return Observable2;
                }else{
                    Observable<HttpError> observable = Observable.just(httpError);
                    return observable;
                }
            }
        },new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                List<AlarmMessageModel> list = model.getAlarm();
                if(list==null){
//                    mvpView.getDataFail("取消失败");
                }else{
                    int errorCode = model.getErrorCode();
                    if(errorCode==0){
                        List<AlarmMessageModel> alarmMessageModels = model.getAlarm();
//                        mvpView.dealAlarmMsgSuccess(alarmMessageModels);
//                        mvpView.updateAlarmMsgSuccess(alarmMessageModels);//@@5.18
                        mvpView.updateAlarmMsgSuccess(index);//@@5.18
//                        mvpView.getDataFail("取消成功");
                    }else{
//                        mvpView.getDataFail("取消失败");
                    }
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    public void dealAlarmDetail(String userId, String smokeMac, String privilege, final int index
            ,String dealPeople,String alarmTruth,String dealDetail,String image_path,String video_path){//@@5.19添加取消报警信息的位置
        mvpView.showLoading();
        Observable mObservable = apiStores1.dealAlarmDetail(userId,smokeMac,dealPeople,alarmTruth,dealDetail,image_path,video_path);
        final Observable Observable2 = apiStores1.getAllAlarm(userId,privilege,"1");
        twoSubscription(mObservable, new Func1<HttpError,Observable<HttpError>>() {
            @Override
            public Observable<HttpError> call(HttpError httpError) {
                int errorCode = httpError.getErrorCode();
                if(errorCode==0){
                    return Observable2;
                }else{
                    Observable<HttpError> observable = Observable.just(httpError);
                    return observable;
                }
            }
        },new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                List<AlarmMessageModel> list = model.getAlarm();
                if(list==null){
//                    mvpView.getDataFail("取消失败");
                }else{
                    int errorCode = model.getErrorCode();
                    if(errorCode==0){
                        List<AlarmMessageModel> alarmMessageModels = model.getAlarm();
//                        mvpView.dealAlarmMsgSuccess(alarmMessageModels);
//                        mvpView.updateAlarmMsgSuccess(alarmMessageModels);//@@5.18
                        mvpView.updateAlarmMsgSuccess(index);//@@5.18
//                        mvpView.getDataFail("取消成功");
                    }else{
//                        mvpView.getDataFail("取消失败");
                    }
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    public void dealAlarmDetailTemp(String userId, String smokeMac, String privilege, final int index
            ,String dealPeople,String alarmTruth,String dealDetail,String image_path,String video_path){//@@5.19添加取消报警信息的位置
        mvpView.showLoading();
        Observable mObservable = apiStores1.dealAlarmDetail(userId,smokeMac,dealPeople,alarmTruth,dealDetail,image_path,video_path);
        final Observable Observable2 = apiStores1.getAllAlarm(userId,privilege,"1");
        twoSubscription(mObservable, new Func1<HttpError,Observable<HttpError>>() {
            @Override
            public Observable<HttpError> call(HttpError httpError) {
                int errorCode = httpError.getErrorCode();
                if(errorCode==0){
                    return Observable2;
                }else{
                    Observable<HttpError> observable = Observable.just(httpError);
                    return observable;
                }
            }
        },new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                List<AlarmMessageModel> list = model.getAlarm();
                if(list==null){
//                    mvpView.getDataFail("取消失败");
                }else{
                    int errorCode = model.getErrorCode();
                    if(errorCode==0){
                        List<AlarmMessageModel> alarmMessageModels = model.getAlarm();
                        mvpView.dealAlarmMsgSuccess(alarmMessageModels);
//                        mvpView.updateAlarmMsgSuccess(index);//@@5.18
//                        mvpView.getDataFail("取消成功");
                    }else{
//                        mvpView.getDataFail("取消失败");
                    }
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

}
