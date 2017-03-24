package com.smart.cloud.fire.mvp.LineChart;

import com.smart.cloud.fire.global.TemperatureTime;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public interface LineChartView {
    void getDataSuccess(List<TemperatureTime.ElectricBean> temperatureTimes);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();
}
