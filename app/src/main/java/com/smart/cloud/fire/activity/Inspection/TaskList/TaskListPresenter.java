package com.smart.cloud.fire.activity.Inspection.TaskList;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.InspectionTask;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;

import rx.Observable;

public class TaskListPresenter extends BasePresenter<TaskListView>{

    public TaskListPresenter(TaskListView view) {
        attachView(view);
    }

    public void getTasks(String userId,String tlevel,String state,String startDate,String endDate){
        mvpView.showLoading();
        Observable mObservable=apiStores1.getTasks(userId,tlevel,state,startDate,endDate);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                if(model.getErrorCode()==0){
                    mvpView.getDataSuccess(model.getTasks());
                }else{
                    mvpView.getDataSuccess(new ArrayList<InspectionTask>());
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
    }
}
