package com.smart.cloud.fire.activity.Inspection.ItemsList;

import com.smart.cloud.fire.global.NFCInfoEntity;

import java.util.List;

public interface ItemsListView {

    void getDataSuccess(List<NFCInfoEntity> pointList);

    void getDataFail(String msg);
}
