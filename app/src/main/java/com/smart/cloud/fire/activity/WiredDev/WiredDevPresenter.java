package com.smart.cloud.fire.activity.WiredDev;

import com.smart.cloud.fire.activity.AllSmoke.AllSmokeView;
import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.OffLineDevFragment.OffLineDevFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security.SecurityFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment.WiredDevFragment;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rain on 2017/7/18.
 */
public class WiredDevPresenter extends BasePresenter<WiredDevView> {

    public WiredDevPresenter(WiredDevView view){
        attachView(view);
    }
    public void unSubscribe(String type){
        mvpView.hideLoading();
        onUnsubscribe();
        mvpView.unSubscribe(type);
    }
    private CompositeSubscription mCompositeSubscription;//管理subseription
    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription=null;
        }
    }
    /**
     * 获取选项列表数据。。
     * type:1表示查询商铺类型，2表示查询区域类型
     * @param userId
     * @param privilege
     * @param type
     */
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
        //跳转会shopinfofragment处理现实。。
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ArrayList<Object>>() {
            @Override
            public void onSuccess(ArrayList<Object> model) {
                if(model!=null&&model.size()>0){
                    mvpView.getAreaType(model,type);
                }else{
                    mvpView.getAreaTypeFail("无数据",type);
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                mvpView.getAreaTypeFail("网络错误",type);
            }
            @Override
            public void onCompleted() {
            }
        }));
    }
    public void getSmokeSummary(String userId,String privilege,String parentId,String areaId,String placeTypeId,String devType,final WiredDevView wiredDevView){
        Observable mObservable = apiStores1.getDevSummary(userId,privilege,parentId,areaId,placeTypeId,devType);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<SmokeSummary>() {
            @Override
            public void onSuccess(SmokeSummary model) {
                int resultCode = model.getErrorCode();
                if(resultCode==0){
                    mvpView.getSmokeSummary(model);
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
    public void getNeedLossSmoke(String userId, String privilege,String parentId, String areaId, String placeTypeId, final String page,String devType, boolean refresh, final int type, final List<Smoke> list, final OfflineWiredDevFragment offLineDevFragment){
        if(!refresh){
            offLineDevFragment.showLoading();
        }
        Observable mObservable = apiStores1.getNeedLossDev(userId,privilege,parentId,areaId,page,placeTypeId,devType);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int result=model.getErrorCode();
                if(result==0){
                    List<Smoke> smokeList = model.getSmoke();
                    if(type==1){
                        if(list==null||list.size()==0){
                            offLineDevFragment.getDataSuccess(smokeList,false);
                        }else if(list!=null&&list.size()>=20){
                            offLineDevFragment.onLoadingMore(smokeList);
                        }
                    }else{
                        offLineDevFragment.getDataSuccess(smokeList,true);
                    }
                }else{
                    List<Smoke> mSmokeList = new ArrayList<>();
                    offLineDevFragment.getDataSuccess(mSmokeList,false);
                    offLineDevFragment.getDataFail("无数据");
                }

            }

            @Override
            public void onFailure(int code, String msg) {
                offLineDevFragment.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                offLineDevFragment.hideLoading();
            }
        }));
    }
    //@@6.29获取无线终端设备
    public void getAllWiredDev(String userId, String privilege,String parentId, String areaId, String page, String placeTypeId, String devType, final List<Smoke> list, final int type, boolean refresh, final WiredDevFragment wiredDevFragment){
        if(!refresh){
            wiredDevFragment.showLoading();
        }
        Observable mObservable = apiStores1.getNeedDev2(userId,privilege,parentId,areaId,page,placeTypeId,devType);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int result=model.getErrorCode();
                if(result==0){
                    List<Smoke> smokeList = model.getSmoke();
                    if(type==1){
                        if(list==null||list.size()==0){
                            wiredDevFragment.getDataSuccess(smokeList,false);
                        }else if(list!=null&&list.size()>=20){
                            wiredDevFragment.onLoadingMore(smokeList);
                        }
                    }
                }else{
                    List<Smoke> mSmokeList = new ArrayList<>();
                    wiredDevFragment.getDataSuccess(mSmokeList,false);
                    wiredDevFragment.getDataFail("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if(type!=1){
                    List<Smoke> mSmokeList = new ArrayList<>();
                    wiredDevFragment.getDataSuccess(mSmokeList,false);
                }
                wiredDevFragment.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                wiredDevFragment.hideLoading();
            }
        }));
    }
    @Override
    public void getShop(ShopType shopType) {
        super.getShop(shopType);
        mvpView.getChoiceShop(shopType);
    }

    @Override
    public void getArea(Area area) {
        super.getArea(area);
        mvpView.getChoiceArea(area);
    }

    //@@6.29获取无线终端设备
    public void getAllWiredDev(String userId, String privilege, String page, String devType, final List<Smoke> list, final int type, boolean refresh, final WiredDevFragment wiredDevFragment){
        if(!refresh){
            wiredDevFragment.showLoading();
        }
        Observable mObservable = apiStores1.getNeedDev(userId,privilege,"",page,"",devType);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int result=model.getErrorCode();
                if(result==0){
                    List<Smoke> smokeList = model.getSmoke();
                    if(type==1){
                        if(list==null||list.size()==0){
                            wiredDevFragment.getDataSuccess(smokeList,false);
                        }else if(list!=null&&list.size()>=20){
                            wiredDevFragment.onLoadingMore(smokeList);
                        }
                    }
                }else{
                    List<Smoke> mSmokeList = new ArrayList<>();
                    wiredDevFragment.getDataSuccess(mSmokeList,false);
                    wiredDevFragment.getDataFail("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if(type!=1){
                    List<Smoke> mSmokeList = new ArrayList<>();
                    wiredDevFragment.getDataSuccess(mSmokeList,false);
                }
                wiredDevFragment.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {

                wiredDevFragment.hideLoading();
            }
        }));
    }
}
