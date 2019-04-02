package com.smart.cloud.fire.mvp.fragment.ConfireFireFragment;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.smart.cloud.fire.activity.Functions.constant.Constant;
import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.service.LocationService;
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

        smokeMac=getDevType(smokeMac,"").getMac();

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
        DevType devType=getDevType(smokeMac,repeater);
        if(devType.getErrorCode()==0){
            smokeMac=devType.getDevType();
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
            if(isUploadImage){
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

    private DevType getDevType(String smokeMac,String repeater) {
        DevType devType=new DevType();
        int electrState = 0;
        String deviceType = "1";

        int macLenghth=smokeMac.length();

        String macStr = (String) smokeMac.subSequence(0, 1);
        if(macLenghth==15){
            deviceType="41";//海曼NB
        }else if(macLenghth==4){
            deviceType="68";//恒星法兰盘水压
        }else if (macLenghth==6){
            deviceType="70";//恒星水压
        }else if (macLenghth==7){
            if (macStr.equals("W")){//@@9.29 区分NB
                deviceType="69";//@@恒星水位
                smokeMac = smokeMac.substring(1, macLenghth);
            }
        }else if (macLenghth==12){
            deviceType="51";//创安
        }else if(macLenghth==16||macLenghth==18){
            switch(macStr){
                case "N":
                    smokeMac = smokeMac.substring(1, macLenghth);//直连设备
                    deviceType="41";
                    break;
                case "A":
                    smokeMac = smokeMac.substring(1, macLenghth);//三江无线传输设备
                    deviceType="119";
                    break;
                case "W":
                    if(smokeMac.endsWith("W")){
                        deviceType="19";//@@水位2018.01.02
                        smokeMac = smokeMac.substring(1, macLenghth);
                    }else if(smokeMac.endsWith("A")){
                        deviceType="124";//@@拓普水位2018.01.30
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("B")){
                        deviceType="125";//@@拓普水压2018.01.30
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else{
                        deviceType="10";//@@水压
                    }
                    smokeMac = smokeMac.replace("W","");//水压设备
                    break;
                case "Z":
                    smokeMac = smokeMac.substring(1, macLenghth);//嘉德烟感
                    deviceType="55";
                    break;
                default:
                    deviceType="21";//loraOne烟感
                    break;
            }
        }else if(smokeMac.equals(repeater)){
            deviceType="126";//海湾主机
        }else if(smokeMac.contains("-")){
            deviceType="31";//三江nb烟感
        }else{
            switch (macStr){
                case "R":
                    if (smokeMac.endsWith("R")){//@@9.29 区分NB
                        deviceType="16";//@@NB燃气
                        smokeMac = smokeMac.substring(1, macLenghth);
                    }else if(smokeMac.endsWith("N")){
                        deviceType="22";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("H")){
                        deviceType="23";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    } else if(smokeMac.endsWith("P")){
                        deviceType="72";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("J")){
                        deviceType="73";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else{
                        deviceType="2";//@@燃气
                        smokeMac = smokeMac.replace("R","");//燃气
                    }
                    break;
                case "Q":
                    deviceType="5";
                    if(smokeMac.endsWith("Q")){
                        electrState=1;
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }//@@8.26
                    if(smokeMac.endsWith("S")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=3;
                    }//@@2018.01.18 三相设备
                    if(smokeMac.endsWith("L")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;
                        deviceType="52";
                    }//@@2018.05.15 Lara电气设备
                    if(smokeMac.endsWith("N")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;
                        deviceType="53";
                    }//@@2018.05.15 Lara电气设备
                    if(smokeMac.endsWith("G")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=0;
                        deviceType="59";
                    }//@@NB北秦电气设备
                    if(smokeMac.endsWith("Z")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=0;
                        deviceType="76";
                    }//@@NB直连三相电气设备
                    if(smokeMac.endsWith("Y")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;;
                        deviceType="77";
                    }//@@NB南京三相电气设备
                    if(smokeMac.endsWith("U")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;;
                        deviceType="80";
                    }//@@NB南京优特电气设备
                    if(smokeMac.endsWith("U")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;;
                        deviceType="81";
                    }//@@lora优特电气设备
                    break;
                case "T":
                    if(smokeMac.endsWith("N")){
                        deviceType="79";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else{
                        deviceType="25";
                        smokeMac = smokeMac.substring(1, macLenghth);
                    }
                    break;
                case "A":
                    smokeMac = smokeMac.substring(1, macLenghth);
                    deviceType="119";
                    break;
                case "G":
                    smokeMac = smokeMac.substring(1, macLenghth);//声光报警器 6
                    deviceType="7";
                    break;
                case "K":
                    smokeMac = smokeMac.substring(1, macLenghth);//@@无线输出输入模块2018.01.24
                    deviceType="20";
                    break;
                case "S":
                    smokeMac = smokeMac.substring(1, macLenghth);//手动报警，显示 7
                    deviceType="8";
                    break;
                case "J":
                    smokeMac = smokeMac.substring(1, macLenghth);//三江设备
                    deviceType="9";
                    break;
                case "W":
                    if(smokeMac.endsWith("W")){
                        deviceType="19";//@@水位2018.01.02
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("C")){
                        deviceType="42";//@@NB水压
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("L")){
                        deviceType="43";//@@Lara水压
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("Y")){
                        deviceType="46";//@@NB防爆直连水位
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("Z")){
                        deviceType="44";//@@NB防爆直连水位（万科）
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("N")){
                        deviceType="78";//@@南京NB普通水压
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("G")){
                        deviceType="47";//@@NB直连水压
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("H")){
                        deviceType="48";//@@NB直连水位
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else{
                        deviceType="10";//@@水压
                        smokeMac = smokeMac.substring(0, macLenghth-0);
                    }
                    break;
                case "L":
                    smokeMac = smokeMac.substring(1, macLenghth);//红外设备
                    deviceType="11";
                    break;
                case "M":
                    smokeMac = smokeMac.substring(1, macLenghth);//门磁设备
                    deviceType="12";
                    break;
                case "N":
                    if(smokeMac.endsWith("N")){
                        deviceType="56";//@@NB-iot烟感
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("O")){
                        deviceType="57";//@@onet烟感
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("R")){
                        deviceType="45";//@@海曼气感
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("Z")){
                        deviceType="58";//@@嘉德移动烟感
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("H")){
                        deviceType="35";//@@电弧 电信
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("I")){
                        deviceType="36";//电弧
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("J")){
                        deviceType="61";//@@嘉德南京平台烟感
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("Q")){
                        deviceType="75";//@@嘉德南京平台烟感
                        electrState=1;
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else{
                        deviceType="41";
                        smokeMac = smokeMac.substring(1, macLenghth);
                    }
                    break;
                case "H":
                    smokeMac = smokeMac.substring(1, macLenghth);//空气探测器
                    deviceType="13";
                    break;
                case "Y":
                    smokeMac = smokeMac.substring(1, macLenghth);//水禁
                    deviceType="15";
                    break;
                case "P":
                    if(smokeMac.endsWith("N")){
                        deviceType="82";//2019.03.08NB直连喷淋
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else{
                        deviceType="18";
                        smokeMac = smokeMac.substring(1, macLenghth);
                    }
                    electrState=2;//@@11.01 1开2关
                    break;
                case "V":
                    if(smokeMac.endsWith("X")){
                        deviceType="27";//@@万科水浸
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("Z")){
                        deviceType="44";//@@万科水位
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("T")){
                        deviceType="26";//@@万科温湿度
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }
                    break;
            }
            if(smokeMac.length()<8){
                devType.setError("设备MAC号长度不正确");
                devType.setErrorCode(1);
            }//@@11.06限制MAC长度
            if(!Utils.isNumOrEng(smokeMac)){
                devType.setError("设备MAC仅能含有数字或字母");
                devType.setErrorCode(1);
            }
        }
        devType.setDevType(deviceType);
        devType.setElectrState(electrState);
        devType.setMac(smokeMac);
        return devType;
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

class DevType{
    private int errorCode;
    private String error;
    private String mac;
    private String devType;
    private int electrState;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public int getElectrState() {
        return electrState;
    }

    public void setElectrState(int electrState) {
        this.electrState = electrState;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
