package com.smart.cloud.fire.mvp.fragment.ConfireFireFragment;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.smart.cloud.fire.activity.Functions.constant.Constant;
import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.DeviceType;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.service.LocationService;
import com.smart.cloud.fire.utils.DeviceTypeUtils;
import com.smart.cloud.fire.utils.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ConfireFireFragmentPresenter extends BasePresenter<ConfireFireFragmentView> {
    private LocationService locationService;
    public ConfireFireFragmentPresenter(ConfireFireFragmentView view) {
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

    public void getOneSmoke(String userId,String privilege,String smokeMac){
        mvpView.showLoading();

        smokeMac=DeviceTypeUtils.getDevType(smokeMac,"").getMac();

        if(smokeMac!=null&&smokeMac.length()>0){
            Observable mObservable = apiStores1.getOneSmoke(userId,smokeMac,privilege);
            addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ConfireFireModel>() {
                @Override
                public void onSuccess(ConfireFireModel model) {
                    int errorResult = model.getErrorCode();
                    if(errorResult==0){
                        mvpView.getDataSuccess(model.getSmoke());
                    }
                }

                @Override
                public void onFailure(int code, String msg) {
                    mvpView.getDataFail("网络错误");
                }

                @Override
                public void onCompleted() {
                    mvpView.hideLoading();
                }
            }));
        }else {//@@10.18
            mvpView.hideLoading();
        }
    }

    //type:1表示查询商铺类型，2表示查询区域类型
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
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ArrayList<Object>>() {
            @Override
            public void onSuccess(ArrayList<Object> model) {
                if(type==1){
                    if(model!=null&&model.size()>0){
                        mvpView.getShopType(model);
                    }else{
                        mvpView.getShopTypeFail("无数据");
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

    public void addSmoke(String userID,String privilege,String smokeName,String smokeMac,String address,String longitude,
                         String latitude,String placeAddress,String placeTypeId,String principal1,String principal1Phone,String principal2,
                         String principal2Phone,String areaId,String repeater,String camera,boolean isUploadImage){
        int electrState=0;//@@8.26电气开关
        if(longitude.length()==0||latitude.length()==0){
            mvpView.addSmokeResult("请获取经纬度",1);
            return;
        }
        if(smokeName.length()==0||smokeName.length()==0){
            mvpView.addSmokeResult("请填写名称",1);
            return;
        }
        if(smokeMac.length()==0){
            mvpView.addSmokeResult("请填写探测器MAC",1);
            return;
        }
        if(areaId==null||areaId.length()==0){
            mvpView.addSmokeResult("请填选择区域",1);
            return;
        }
        //@@
        if(placeTypeId==null||placeTypeId.length()==0){
            mvpView.addSmokeResult("请填选择类型",1);
            return;
        }

        String deviceType="1";//烟感。。
        DeviceType devType= DeviceTypeUtils.getDevType(smokeMac,repeater);
        if(devType.getErrorCode()==0){
            smokeMac=devType.getMac();
            electrState=devType.getElectrState();
            deviceType=devType.getDevType();
        }else{
            mvpView.addSmokeResult(devType.getError(),1);
            return;
        }

        mvpView.showLoading();
        Observable mObservable =null;
        if(smokeMac.length()==15&&(deviceType.equals("41")||deviceType.equals("45"))){
            mObservable = apiStores1.addHeiMenSmoke(userID,smokeName,privilege,smokeMac,address,
                    longitude,latitude,placeAddress,placeTypeId,principal1,principal1Phone,principal2,
                    principal2Phone,areaId,repeater,camera,deviceType,electrState+"");
        }else{
            if(isUploadImage){//上传图片成功
                mObservable = apiStores1.addSmoke(userID,smokeName,privilege,smokeMac,address,
                        longitude,latitude,placeAddress,placeTypeId,principal1,principal1Phone,principal2,
                        principal2Phone,areaId,repeater,camera,deviceType,electrState+"",smokeMac+".jpg");
            }else{
                mObservable = apiStores1.addSmoke(userID,smokeName,privilege,smokeMac,address,
                        longitude,latitude,placeAddress,placeTypeId,principal1,principal1Phone,principal2,
                        principal2Phone,areaId,repeater,camera,deviceType,electrState+"","");
            }

        }

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

    @Override
    public void getArea(Area area) {
        mvpView.getChoiceArea(area);
    }

    @Override
    public void getShop(ShopType shopType) {
        mvpView.getChoiceShop(shopType);
    }


}
