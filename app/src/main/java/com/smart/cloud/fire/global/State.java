package com.smart.cloud.fire.global;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rain on 2018/3/2.
 */
public class State{
    private String stateId;
    private String stateName;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }


}
