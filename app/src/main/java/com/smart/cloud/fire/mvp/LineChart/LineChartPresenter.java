package com.smart.cloud.fire.mvp.LineChart;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.ProofGasEntity;
import com.smart.cloud.fire.global.ProofGasHistoryEntity;
import com.smart.cloud.fire.global.TemperatureTime;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

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
    public void getElectricTypeInfo(String userId,String privilege,String mac,String electricType,String electricNum,String page,boolean refresh,int devType){
        if(!refresh){
            mvpView.showLoading();
        }
        Observable<TemperatureTime> mObservable = apiStores1.getElectricTypeInfo(userId,privilege,mac,electricType,electricNum,page,devType);
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

    public void getTHDevInfoHistoryInfo(String mac,String page,String type,boolean refresh){
        if(!refresh){
            mvpView.showLoading();
        }
        Observable<TemperatureTime> mObservable = apiStores1.getTHDevInfoHistoryInfo(mac,page,type);
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

    public void getChuanganInfo(String userId,String privilege,String mac,String electricNum,String page,boolean refresh){
        if(!refresh){
            mvpView.showLoading();
        }
        Observable<TemperatureTime> mObservable = apiStores1.getChuanganHistoryInfo(userId,privilege,mac,electricNum,page);
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

    public void getWaterHistoryInfo(String userId,String privilege,String mac,String page,boolean refresh){
        if(!refresh){
            mvpView.showLoading();
        }
        Observable<TemperatureTime> mObservable = apiStores1.getWaterHistoryInfo(userId,privilege,mac,page);
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


    public void getGasHistoryInfo(String userId,String privilege,String mac,String page,boolean refresh){
        if(!refresh){
            mvpView.showLoading();
        }
        Observable<ProofGasHistoryEntity> mObservable = apiStores1.getGasHistoryInfo(userId,privilege,mac,page);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ProofGasHistoryEntity>() {
            @Override
            public void onSuccess(ProofGasHistoryEntity model) {
                int resultCode = model.getErrorCode();
                if(resultCode==0){
                    List<TemperatureTime.ElectricBean> history=new ArrayList<TemperatureTime.ElectricBean>();
                    List<ProofGasEntity> history_temp=model.getHistory();
                    for(int i=0;i<history_temp.size();i++){
                        TemperatureTime.ElectricBean electricBean=new TemperatureTime.ElectricBean();
                        electricBean.setElectricTime(history_temp.get(i).getProofGasTime());
                        electricBean.setElectricValue(history_temp.get(i).getProofGasMmol());
                        history.add(electricBean);
                    }
                    mvpView.getDataSuccess(history);
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
