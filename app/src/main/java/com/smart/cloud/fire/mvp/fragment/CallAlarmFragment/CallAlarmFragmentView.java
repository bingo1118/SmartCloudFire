package com.smart.cloud.fire.mvp.fragment.CallAlarmFragment;

import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */
public interface CallAlarmFragmentView {
    void getCurrentTime(String time);
    void stopCountDown(String msg);
    void sendAlarmMessage(String result);
    void getDataResult(String result);
    void getDataSuccess(List<Smoke> list);
}
