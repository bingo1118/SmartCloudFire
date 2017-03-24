package com.smart.cloud.fire.mvp.fragment.CollectFragment;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by Administrator on 2016/9/21.
 */
public class CollectFragmentPresenter extends BasePresenter<CollectFragmentView>{
    public CollectFragmentPresenter(CollectFragmentView view){
        attachView(view);
    }

    //type:1表示获取第一页的报警消息，2表示根据条件查询相应的报警消息
    public void getAllAlarm(String userId, String privilege, String page, final int type, String startTime, String endTime, String areaId, String placeTypeId){
        mvpView.showLoading();
        Observable observable=null;
        if(type==1){
            observable = apiStores1.getAllAlarm(userId,privilege,page);
        }else{
            observable = apiStores1.getNeedAlarm(userId,privilege,startTime,endTime,areaId,placeTypeId,"");
        }
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

    public void dealAlarm(String userId, String smokeMac,String privilege){
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
                    mvpView.getDataFail("取消失败");
                }else{
                    int errorCode = model.getErrorCode();
                    if(errorCode==0){
                        List<AlarmMessageModel> alarmMessageModels = model.getAlarm();
                        mvpView.dealAlarmMsgSuccess(alarmMessageModels);
                        mvpView.getDataFail("取消成功");
                    }else{
                        mvpView.getDataFail("取消失败");
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

    //type:1表示查询商铺类型，2表示查询区域类型
    public void getPlaceTypeId(String userId, String privilege, final int type){
        Observable mObservable = null;
        if(type==1){
            mObservable= apiStores1.getPlaceTypeId(userId,privilege,"").map(new Func1<HttpError,ArrayList<Object>>() {
                @Override
                public ArrayList<Object> call(HttpError o) {
                    return o.getPlaceType();
                }
            });
        }else{
            mObservable= apiStores1.getAreaId(userId,privilege,"").map(new Func1<HttpAreaResult,ArrayList<Object>>() {
                @Override
                public ArrayList<Object> call(HttpAreaResult o) {
                    return o.getSmoke();
                }
            });
        }
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ArrayList<Object>>() {
            @Override
            public void onSuccess(ArrayList<Object> model) {
                if(type==1){
                    if(model!=null&&model.size()>0){
                        mvpView.getShopType(model);
                    }else{
                        mvpView.getShopTypeFail("无数据");
                    }
                }else{
                    if(model!=null&&model.size()>0){
                        mvpView.getAreaType(model);
                    }else{
                        mvpView.getAreaTypeFail("无数据");
                    }
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误");
            }
            @Override
            public void onCompleted() {
            }
        }));
    }

    @Override
    public void getShop(ShopType shopType) {
        super.getShop(shopType);
        mvpView.getChoiceShop(shopType);
    }

    @Override
    public void getArea(Area area) {
        super.getArea(area);
        mvpView.getChoiceArea(area);
    }
}
