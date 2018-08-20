package com.smart.cloud.fire.activity.AlarmMsg;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.CollectFragment.AlarmMessageModel;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

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

    //type:1表示获取第一页的报警消息，2表示根据条件查询相应的报警消息
    public void getNeedAlarmMsg(String userId, String privilege, String page, final int type, String startTime, String endTime
            , String areaId, String placeTypeId,String parentId,String grade,String distance,String progress){
        mvpView.showLoading();
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
                    }else{
                        mvpView.getDataByCondition(alarmMessageModels);
                    }

                }else{
                    List<AlarmMessageModel> alarmMessageModels = new ArrayList<AlarmMessageModel>();//@@5.3
                    mvpView.getDataSuccess(alarmMessageModels);//@@5.3
                    mvpView.getDataFail("无数据");
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

}
