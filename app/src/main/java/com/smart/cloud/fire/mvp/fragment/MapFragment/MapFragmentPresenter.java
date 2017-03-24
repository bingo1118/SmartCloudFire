package com.smart.cloud.fire.mvp.fragment.MapFragment;

import android.os.Bundle;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/9/21.
 */
public class MapFragmentPresenter extends BasePresenter<MapFragmentView> {
    public MapFragmentPresenter(MapFragmentView view) {
        attachView(view);
    }

    public void getAllSmoke(String userId, String privilege){
        mvpView.showLoading();
        Observable mObservable = apiStores1.getAllSmoke(userId,privilege,"");
        Observable Observable2 = apiStores1.getAllCamera(userId,privilege,"");
        Observable mObservable3 = Observable.merge(Observable2,mObservable);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                if(model!=null){
                    int errorCode = model.getErrorCode();
                    if(errorCode==0){
                        List<Smoke> smokes = model.getSmoke();
                        mvpView.getDataSuccess(smokes);
                    }else{
                        mvpView.getDataFail("无数据");
                    }
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

    public void getNeedSmoke(String userId, String privilege,String areaId,String placeTypeId){
        mvpView.showLoading();
        Observable mObservable = apiStores1.getNeedSmoke(userId,privilege,areaId,"",placeTypeId);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                if(model!=null){
                    int errorCode = model.getErrorCode();
                    if(errorCode==0){
                        List<Smoke> smokes = model.getSmoke();
                        mvpView.getDataSuccess(smokes);
                    }else {
                        mvpView.getAreaTypeFail("无数据");
                    }
                }else{
                    mvpView.getAreaTypeFail("无数据");
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
        final Observable mObservable = apiStores1.getAllSmoke(userId,privilege,"");
        twoSubscription(apiStores1.dealAlarm(userId, smokeMac), new Func1<HttpError,Observable<HttpError>>() {
            @Override
            public Observable<HttpError> call(HttpError httpError) {
                return mObservable;
            }
        },new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                if(model!=null){
                    int errorCode = model.getErrorCode();
                    if(errorCode==0){
                        List<Smoke> smokes = model.getSmoke();
                        mvpView.getDataSuccess(smokes);
                    }else{
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

    public void getClickDev(Bundle bundle){
        Serializable object = bundle.getSerializable("mNormalSmoke");
        boolean result = object instanceof Smoke;
        if (result) {
            Smoke normalSmoke = (Smoke) object;
            int states = normalSmoke.getIfDealAlarm();
            if (states == 1) {//无未处理报警信息，地图图标不闪
                mvpView.showSmokeDialog(normalSmoke);
            } else {//有未处理报警信息，地图图标闪动
                mvpView.showAlarmDialog(normalSmoke);
            }
        } else {
            Camera camera = (Camera) object;
            mvpView.openCamera(camera);
        }
    }

    @Override
    public void getArea(Area area) {
        mvpView.getChoiceArea(area);
    }

    @Override
    public void getShop(ShopType shopType) {
        mvpView.getChoiceShop(shopType);
    }
}
