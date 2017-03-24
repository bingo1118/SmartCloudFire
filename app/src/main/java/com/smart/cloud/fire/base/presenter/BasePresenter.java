package com.smart.cloud.fire.base.presenter;

/**
 * Created by Administrator on 2016/9/19.
 */

import android.content.Context;

import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.retrofit.ApiStores;
import com.smart.cloud.fire.retrofit.AppClient;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.view.NormalDialog;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V> implements Presenter<V> {
        public V mvpView;
        public ApiStores[] apiStores = {AppClient.retrofit(ConstantValues.SERVER_YOOSEE_IP_ONE).create(ApiStores.class),
                AppClient.retrofit(ConstantValues.SERVER_YOOSEE_IP_TWO).create(ApiStores.class),
                AppClient.retrofit(ConstantValues.SERVER_YOOSEE_IP_THREE).create(ApiStores.class),
                AppClient.retrofit(ConstantValues.SERVER_YOOSEE_IP_FOUR).create(ApiStores.class)};
        public ApiStores apiStores1 = AppClient.retrofit(ConstantValues.SERVER_IP_NEW).create(ApiStores.class);
        public ApiStores apiStores3 = AppClient.retrofit(ConstantValues.SERVER_IP_NEW_TEST).create(ApiStores.class);
        public ApiStores apiStores2 = AppClient.retrofit(ConstantValues.SERVER_PUSH).create(ApiStores.class);
        private CompositeSubscription mCompositeSubscription;

        @Override
        public void attachView(V mvpView) {
            this.mvpView = mvpView;
        }

        @Override
        public void detachView() {
            this.mvpView = null;
            onUnsubscribe();
        }

    @Override
    public void getArea(Area area) {

    }

    @Override
    public void getShop(ShopType shopType) {

    }

    //RXjava取消注册，以避免内存泄露
        public void onUnsubscribe() {
            if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
                mCompositeSubscription.unsubscribe();
                mCompositeSubscription=null;
            }
        }

        public void telPhoneAction(Context mContext, String phoneNum){
            if(Utils.isPhoneNumber(phoneNum)){
                NormalDialog mNormalDialog = new NormalDialog(mContext, "是否需要拨打电话：", phoneNum,
                        "是", "否");
                mNormalDialog.showNormalDialog();
            }else{
                T.showShort(mContext, "电话号码不合法");
            }
        }

        public void addSubscription(Observable observable, Subscriber subscriber) {
            if (mCompositeSubscription == null) {
                mCompositeSubscription = new CompositeSubscription();
            }
            mCompositeSubscription.add(observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber));
        }

        public void twoSubscription(Observable observable, Func1 func1,Subscriber subscriber) {
            if (mCompositeSubscription == null) {
                mCompositeSubscription = new CompositeSubscription();
            }
            mCompositeSubscription.add(observable
                    .flatMap(func1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber));
        }

    }

