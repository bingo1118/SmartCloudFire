package com.smart.cloud.fire.global;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/22.
 */
public class Area implements Serializable{
    private String areaId;
    private String areaName;
    private int isParent;//@@9.1

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }
}
