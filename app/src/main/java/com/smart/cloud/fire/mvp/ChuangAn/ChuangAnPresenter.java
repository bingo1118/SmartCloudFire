package com.smart.cloud.fire.mvp.ChuangAn;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.ChuangAnValue;
import com.smart.cloud.fire.global.ElectricInfo;
import com.smart.cloud.fire.global.ElectricValue;
import com.smart.cloud.fire.mvp.electric.ElectricView;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Rain on 2018/2/28.
 */
public class ChuangAnPresenter extends BasePresenter<ChuangAnView> {
    public ChuangAnPresenter(ChuangAnView electricView){
        attachView(electricView);
    }

    public void getOneElectricInfo(String userId,String privilege,String mac,boolean refresh){
        if(!refresh){
            mvpView.showLoading();
        }
        Observable mObservable = apiStores1.getOneChuangAnInfo(userId,privilege,mac);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ElectricInfo<ChuangAnValue.ChuangAnValueBean>>() {
            @Override
            public void onSuccess(ElectricInfo<ChuangAnValue.ChuangAnValueBean> model) {
                int resultCode = model.getErrorCode();
                if(resultCode==0){
                    List<ChuangAnValue.ChuangAnValueBean> electricList = model.getElectric();
                    List<ChuangAnValue.ChuangAnValueBean> electricValueBeen = new ArrayList<>();
                    if(electricList!=null){
                        mvpView.getDataSuccess(electricList);
                        return;
                    }
                }else{
                    mvpView.getDataSuccess(new ArrayList<ChuangAnValue.ChuangAnValueBean>());
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
