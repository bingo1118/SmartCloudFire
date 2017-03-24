package com.smart.cloud.fire.global;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ShopType implements Serializable{

    private String placeTypeId;

    public String getPlaceTypeName() {
        return placeTypeName;
    }

    public void setPlaceTypeName(String placeTypeName) {
        this.placeTypeName = placeTypeName;
    }

    public String getPlaceTypeId() {
        return placeTypeId;
    }

    public void setPlaceTypeId(String placeTypeId) {
        this.placeTypeId = placeTypeId;
    }

    private String placeTypeName;

}
