package com.smart.cloud.fire.activity.AlarmMsg;

import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.CollectFragment.AlarmMessageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rain on 2018/8/16.
 */
public interface AlarmMsgView {
    void getDataSuccess(List<AlarmMessageModel> alarmMessageModels);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void dealAlarmMsgSuccess(List<AlarmMessageModel> alarmMessageModels);

    void updateAlarmMsgSuccess(int index);//@@5.18


    void getDataByCondition(List<AlarmMessageModel> alarmMessageModels);

    void getOfflineDataSuccess(List<AlarmMessageModel> alarmMessageModels);


}
