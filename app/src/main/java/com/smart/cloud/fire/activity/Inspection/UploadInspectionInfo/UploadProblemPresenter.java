package com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.mvp.fragment.ConfireFireFragment.ConfireFireModel;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.view.BingoViewModel;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

public class UploadProblemPresenter extends BasePresenter<UploadProblemView> {

    public UploadProblemPresenter(UploadProblemView view) {
        attachView(view);
    }


    public void getManagers(String areaId){
        Observable mObservable=apiStores1.getManagersByAreaId(areaId);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                if(model.getErrorCode()==0){
                    mvpView.getUsers(model.getList());
                }else{
                    mvpView.getUsersFail(model.getError());
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                mvpView.getUsersFail("网络错误");
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
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ArrayList<BingoViewModel>>() {
            @Override
            public void onSuccess(ArrayList<BingoViewModel> model) {
                if(model!=null&&model.size()>0){
                    mvpView.getAreaType(model);
                }else{
                    mvpView.getAreaTypeFail("无数据");
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                mvpView.getAreaTypeFail("网络错误");
            }
            @Override
            public void onCompleted() {
            }
        }));
    }

    public void uploadHiddenDanger(String title,String address,String managers,String workerId,String areaId,String desc,
                       String imgs){
        mvpView.showLoading();
        Observable mObservable = apiStores1.uploadHiddenDanger(title,address,managers,workerId,areaId, desc,imgs);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ConfireFireModel>() {
            @Override
            public void onSuccess(ConfireFireModel model) {
                int result = model.getErrorCode();
                String error=model.getError();//@@6.15
                if(result==0){
                    mvpView.addSmokeResult("成功",0);
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


}
