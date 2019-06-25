package com.smart.cloud.fire.activity.Inspection.PointList;

import com.smart.cloud.fire.global.Point;

import java.util.List;

public interface PointListView {

    void getDataSuccess(List<Point> pointList);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();
}
