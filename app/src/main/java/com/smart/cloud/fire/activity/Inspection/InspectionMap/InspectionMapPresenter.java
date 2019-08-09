package com.smart.cloud.fire.activity.Inspection.InspectionMap;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import rx.Observable;

public class InspectionMapPresenter extends BasePresenter<InspectionMapView>{

    public InspectionMapPresenter(InspectionMapView view) {
        attachView(view);
    }

    public void getAllItems(String userid,String pid){
        Observable mObservable=apiStores1.getAllItems(userid,pid);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                if(model.getErrorCode()==0){
                    mvpView.getDataSuccess(model.getNfcinfos());
                }else{
                    mvpView.getDataFail(model.getError());
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


}
