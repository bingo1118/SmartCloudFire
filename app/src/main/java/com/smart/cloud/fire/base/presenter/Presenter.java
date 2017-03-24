package com.smart.cloud.fire.base.presenter;

import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;

/**
 * Created by Administrator on 2016/9/19.
 */

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

    void getArea(Area area);

    void getShop(ShopType shopType);


}
