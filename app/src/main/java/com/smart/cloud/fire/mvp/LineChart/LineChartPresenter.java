package com.smart.cloud.fire.mvp.LineChart;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.TemperatureTime;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/1.
 */
public class LineChartPresenter extends BasePresenter<LineChartView> {
    public LineChartPresenter(LineChartView view){
        attachView(view);
    }

//    @Query("userId") String userId, @Query("privilege") String privilege,
//    @Query("smokeMac") String smokeMac, @Query("electricType") String electricType,
//    @Query("electricNum") String electricNum, @Query("page") String page
    public void getElectricTypeInfo(String userId,String privilege,String mac,String electricType,String electricNum,String page,boolean refresh){
        if(!refresh){
            mvpView.showLoading();
        }
        Observable<TemperatureTime> mObservable = apiStores1.getElectricTypeInfo(userId,privilege,mac,electricType,electricNum,page);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<TemperatureTime>() {
            @Override
            public void onSuccess(TemperatureTime model) {
                int resultCode = model.getErrorCode();
                if(resultCode==0){
                    mvpView.getDataSuccess(model.getElectric());
                }else{
                    mvpView.getDataFail("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
//                mvpView.getDataFail("网络错误，请检查网络");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }
}
