package com.smart.cloud.fire.mvp.fragment.CallAlarmFragment;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/10/13.
 */
public class CallAlarmFragmentPresenter extends BasePresenter<CallAlarmFragmentView> {
    private  Subscription mSubscription;
    public CallAlarmFragmentPresenter(CallAlarmFragmentView view){
        attachView(view);
    }

    public void countdown(int time, final Smoke smoke, final String userId, final String privilege, final String info){
        if(smoke==null){
            mvpView.getDataResult("网络错误，请检查网络是否通畅");
            return;
        }
        if (time < 0) time = 0;
        final int countTime = time;
        Observable<Integer> mObservable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);
        mSubscription = mObservable.doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mvpView.getCurrentTime(countTime+"");
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        textAlarm(userId,privilege,smoke.getMac(),info);
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(Integer integer) {
                        mvpView.getCurrentTime(integer.toString());
                    }
                });
    }

    public void stopCountDown(){
        if(mSubscription!=null){
            if(!mSubscription.isUnsubscribed()){
                mSubscription.unsubscribe();
                mSubscription=null;
                mvpView.stopCountDown("已取消报警");
            }
        }
    }

    private void textAlarm( String userId,String privilege, String smokeMac,String info){
        Observable mObservable = apiStores1.textAlarm(userId,privilege,smokeMac,info);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                mvpView.sendAlarmMessage("已发送报警消息");
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.sendAlarmMessage("发送报警消息失败，请检查网络");
            }

            @Override
            public void onCompleted() {

            }
        }));
    }

    public void getAllSmoke(String userId, String privilege){
        Observable mObservable = apiStores1.getAllSmoke(userId,privilege,"");
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                if(model!=null){
                    int errorCode = model.getErrorCode();
                    if(errorCode==0){
                        List<Smoke> smokes = model.getSmoke();
                        mvpView.getDataSuccess(smokes);
                    }else{
                        mvpView.getDataResult("无数据");
                    }
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataResult("网络错误");
            }
            @Override
            public void onCompleted() {
            }
        }));
    }
}
