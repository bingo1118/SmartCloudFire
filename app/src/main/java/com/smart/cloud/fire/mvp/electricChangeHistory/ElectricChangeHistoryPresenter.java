package com.smart.cloud.fire.mvp.electricChangeHistory;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Rain on 2017/8/28.
 */
public class ElectricChangeHistoryPresenter extends BasePresenter<ElectricChangeHistoryView> {

    public ElectricChangeHistoryPresenter(ElectricChangeHistoryView view) {
        attachView(view);
    }

    public void getEleNeedHis(String smokeMac, final String page, final List<HistoryBean> list, final int type, boolean refresh){
        if(!refresh){
            mvpView.showLoading();
        }
        Observable mObservable = apiStores1.getEleNeedHis(smokeMac,page);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int result=model.getErrorCode();
                if(result==0){
                    List<HistoryBean> smokeList = model.getEleList();
                    if(type==1){
                        if(list==null||list.size()==0){
                            mvpView.getDataSuccess(smokeList,false);
                        }else if(list!=null&&list.size()>=20){
                            mvpView.onLoadingMore(smokeList);
                        }
                    }
                }else{
                    List<HistoryBean> mSmokeList = new ArrayList<>();
                    if(!page.equals("1")){
                        mvpView.onLoadingMore(mSmokeList);
                    }else{
                        mvpView.getDataFail("无数据");
                    }
                }
            }



            @Override
            public void onFailure(int code, String msg) {
                if(type!=1){
                    List<HistoryBean> mSmokeList = new ArrayList<>();
                    mvpView.getDataSuccess(mSmokeList,false);
                }
                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    @Override
    public void attachView(ElectricChangeHistoryView view) {
        this.mvpView = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void getArea(Area area) {

    }

    @Override
    public void getShop(ShopType shopType) {

    }
}
