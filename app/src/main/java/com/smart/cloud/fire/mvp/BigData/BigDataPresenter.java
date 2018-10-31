package com.smart.cloud.fire.mvp.BigData;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.SafeScore;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentView;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import rx.Observable;

/**
 * Created by Rain on 2018/9/21.
 */
public class BigDataPresenter extends BasePresenter<BigDataView>{
    public BigDataPresenter(BigDataView view){
        attachView(view);
    }

    public void getSmokeSummary(String userId, String privilege, String parentId, String areaId, String placeTypeId, String devType){
        Observable mObservable = apiStores1.getDevSummary(userId,privilege,parentId,areaId,placeTypeId,devType);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<SmokeSummary>() {
            @Override
            public void onSuccess(SmokeSummary model) {

                int resultCode = model.getErrorCode();
                if(resultCode==0){
                    mvpView.getOnlineSummary(model);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
            }

            @Override
            public void onCompleted() {
            }
        }));
    }

    public void getSafeScore(String userId, String privilege){
        Observable mObservable = apiStores1.getSafeScore(userId,privilege);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<SafeScore>() {
            @Override
            public void onSuccess(SafeScore model) {

                int resultCode = model.getErrorCode();
                if(resultCode==0){
                    mvpView.getSafeScore(model);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getSafeScore(null);
            }

            @Override
            public void onCompleted() {
            }
        }));
    }
}
