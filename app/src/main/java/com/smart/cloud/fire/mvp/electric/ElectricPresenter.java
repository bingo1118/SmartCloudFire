package com.smart.cloud.fire.mvp.electric;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.ElectricInfo;
import com.smart.cloud.fire.global.ElectricValue;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ElectricPresenter extends BasePresenter<ElectricView>{

    public ElectricPresenter(ElectricView electricView){
        attachView(electricView);
    }

    public void getOneElectricInfo(String userId,String privilege,String mac,boolean refresh){
        if(!refresh){
            mvpView.showLoading();
        }
        Observable mObservable = apiStores1.getOneElectricInfo(userId,privilege,mac);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ElectricInfo<ElectricValue>>() {
            @Override
            public void onSuccess(ElectricInfo<ElectricValue> model) {
                int resultCode = model.getErrorCode();
                if(resultCode==0){
                    List<ElectricValue> electricList = model.getElectric();
                    List<ElectricValue.ElectricValueBean> electricValueBeen = new ArrayList<>();
                    for(ElectricValue electricValue : electricList){
                        int electricType = electricValue.getElectricType();
                        List<ElectricValue.ElectricValueBean> list = electricValue.getElectricValue();
                        for(ElectricValue.ElectricValueBean electric: list){
                            electric.setElectricType(electricType);
                            electricValueBeen.add(electric);
                        }
                    }
                    mvpView.getDataSuccess(electricValueBeen);
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
}
