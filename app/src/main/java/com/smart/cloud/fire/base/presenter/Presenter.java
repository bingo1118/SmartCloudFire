package com.smart.cloud.fire.base.presenter;

import com.smart.cloud.fire.activity.AddNFC.NFCDeviceType;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.Point;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.State;

/**
 * Created by Administrator on 2016/9/19.
 */

public interface Presenter<V> {

    /**
     *依附视图
     * @param view
     */
    void attachView(V view);

    /**
     * 分离视图
     */
    void detachView();

    void getArea(Area area);

    void getShop(ShopType shopType);

    void getState(State state);

    void getNFCDeviceType(NFCDeviceType nfcDeviceType);

    void getPoint(Point point);

}
