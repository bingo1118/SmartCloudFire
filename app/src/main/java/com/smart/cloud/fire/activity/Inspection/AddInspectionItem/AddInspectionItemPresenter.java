package com.smart.cloud.fire.activity.Inspection.AddInspectionItem;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.smart.cloud.fire.activity.AddNFC.AddNFCView;
import com.smart.cloud.fire.activity.AddNFC.NFCDeviceType;
import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.Point;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.ConfireFireFragment.ConfireFireModel;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.service.LocationService;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

public class AddInspectionItemPresenter extends BasePresenter<AddInspectionItemView> {
        private LocationService locationService;
        public AddInspectionItemPresenter(AddInspectionItemView view) {
            attachView(view);
        }

        public void initLocation(){
            locationService = MyApp.app.locationService;
            locationService.registerListener(mListener);
        }

        public void startLocation(){
            locationService.start();
            mvpView.showLoading();
        }

        public void stopLocation(){
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }

        private BDLocationListener mListener = new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                    int result = location.getLocType();
                    switch (result){
                        case 61:
                            mvpView.getLocationData(location);
                            break;
                        case 161:
                            mvpView.getLocationData(location);
                            break;
                    }
                    locationService.stop();
                    mvpView.hideLoading();
                }
            }
        };

        public void getPointsId(String areaId){
            Observable mObservable=apiStores1.getPoints("",areaId);
            addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
                @Override
                public void onSuccess(HttpError model) {
                    if(model.getErrorCode()==0){
                        mvpView.getPoints(model.getPoints());
                    }else{
                        mvpView.getPointsFail(model.getError());
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


        //type:1表示查询设备类型，2表示查询区域类型
        public void getPlaceTypeId(String userId, String privilege, final int type){
            Observable mObservable = null;
            if(type==1){
                mObservable= apiStores1.getNFCDeviceTypeId().map(new Func1<HttpError,ArrayList<Object>>() {
                    @Override
                    public ArrayList<Object> call(HttpError o) {
                        return o.getDeviceType();
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
            addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ArrayList<Object>>() {
                @Override
                public void onSuccess(ArrayList<Object> model) {
                    if(type==1){
                        if(model!=null&&model.size()>0){
                            mvpView.getNFCDeviceType(model);//@@8.16
                        }else{
                            mvpView.getNFCDeviceTypeFail("无数据");//@@8.16
                        }
                    }else{
                        if(model!=null&&model.size()>0){
                            mvpView.getAreaType(model);
                        }else{
                            mvpView.getAreaTypeFail("无数据");
                        }
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

        public void addNFC(String userID,String privilege,String smokeName,String smokeMac,String address,String longitude,
                           String latitude,String placeTypeId,String areaId,String producer,String makeTime,String workerPhone,String makeAddress){
            mvpView.showLoading();
            Observable mObservable = apiStores1.addNFC(userID,privilege,smokeName,smokeMac,address,
                    longitude,latitude,placeTypeId,areaId,producer,makeTime,workerPhone,makeAddress);
            addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ConfireFireModel>() {
                @Override
                public void onSuccess(ConfireFireModel model) {
                    int result = model.getErrorCode();
                    String error=model.getError();//@@6.15
                    if(result==0){
                        mvpView.addSmokeResult("添加成功",0);
                    }else{
                        mvpView.addSmokeResult(error,1);//@@6.15
                    }
                }

                @Override
                public void onFailure(int code, String msg) {
                    mvpView.addSmokeResult("添加失败",1);
                }

                @Override
                public void onCompleted() {
                    mvpView.hideLoading();
                }
            }));
        }

    public void addNFCInspectItem(String userID,String privilege,String smokeName,String smokeMac,String address,String longitude,
                       String latitude,String placeTypeId,String areaId,String producer,String makeTime,String makeAddress,String memo,String pid){
        mvpView.showLoading();
        Observable mObservable = apiStores1.addNFCInfo(userID,privilege,smokeName,smokeMac,address,
                longitude,latitude,placeTypeId,areaId,producer,makeTime,memo,makeAddress,pid);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ConfireFireModel>() {
            @Override
            public void onSuccess(ConfireFireModel model) {
                int result = model.getErrorCode();
                String error=model.getError();//@@6.15
                if(result==0){
                    mvpView.addSmokeResult("添加成功",0);
                }else{
                    mvpView.addSmokeResult(error,1);//@@6.15
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.addSmokeResult("添加失败",1);
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }


    public void updateItemInfo(String userID,String smokeName,String smokeMac,String address,String placeTypeId,String memo){
        mvpView.showLoading();
        Observable mObservable = apiStores1.updateItemInfo(userID,smokeName,smokeMac,address,placeTypeId,memo);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ConfireFireModel>() {
            @Override
            public void onSuccess(ConfireFireModel model) {
                int result = model.getErrorCode();
                String error=model.getError();//@@6.15
                if(result==0){
                    mvpView.addSmokeResult("修改成功",0);
                }else{
                    mvpView.addSmokeResult(error,1);//@@6.15
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.addSmokeResult("添加失败",1);
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }


        @Override
        public void getArea(Area area) {
            mvpView.getChoiceArea(area);
        }

        @Override
        public void getShop(ShopType shopType) {
            mvpView.getChoiceShop(shopType);
        }

        @Override
        public void getPoint(Point point) {
            mvpView.getChoicePoint(point);
        }

    @Override
        public void getNFCDeviceType(NFCDeviceType nfcDeviceType) {
            mvpView.getChoiceNFCDeviceType(nfcDeviceType);//@@8.16
        }
}

