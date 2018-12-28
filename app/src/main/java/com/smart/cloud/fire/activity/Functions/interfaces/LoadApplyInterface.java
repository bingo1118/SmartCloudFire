package com.smart.cloud.fire.activity.Functions.interfaces;


import com.smart.cloud.fire.activity.Functions.model.ApplyTable;

import java.util.ArrayList;


/**
 * 加载应用接口
 */
public interface LoadApplyInterface {
    void lodeApplyRequest();

    void onItemSwap(ArrayList<ApplyTable> applyTableList);

    void onItemAddOrRemove(ArrayList<ApplyTable> mineApplyTableList, ArrayList<ApplyTable> moreApplyTableList);
}
