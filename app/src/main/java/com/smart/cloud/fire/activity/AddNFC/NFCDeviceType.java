package com.smart.cloud.fire.activity.AddNFC;

import java.io.Serializable;

/**
 * Created by Rain on 2017/8/16.
 */
public class NFCDeviceType implements Serializable {
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
