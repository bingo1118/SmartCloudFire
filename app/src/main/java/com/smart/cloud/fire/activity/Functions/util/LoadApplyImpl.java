package com.smart.cloud.fire.activity.Functions.util;


import com.smart.cloud.fire.activity.Functions.FunctionsActivity;
import com.smart.cloud.fire.activity.Functions.constant.Constant;
import com.smart.cloud.fire.activity.Functions.interfaces.LoadApplyInterface;
import com.smart.cloud.fire.activity.Functions.model.ApplyTable;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.main.Main3Activity;

import java.util.ArrayList;

public class LoadApplyImpl implements LoadApplyInterface {
    private FunctionsActivity moreActivity;
    private int privilege= MyApp.app.getPrivilege();
    public LoadApplyImpl(FunctionsActivity moreActivity){
        this.moreActivity = moreActivity;
    }
    @Override
    public void lodeApplyRequest() {
        ArrayList<ApplyTable> appayTableMine = (ArrayList<ApplyTable>) ACache.get(MyApp.app).getAsObject(Constant.APPLY_MINE);
        if(appayTableMine==null){
            appayTableMine= (ArrayList<ApplyTable>) ApplyTableManager.loadNewsChannelsStatic(privilege);
            ACache.get(MyApp.app).put(Constant.APPLY_MINE,appayTableMine);
        }
        moreActivity.returnMineApply(appayTableMine);
        ArrayList<ApplyTable> appayTableMore = (ArrayList<ApplyTable>) ACache.get(MyApp.app).getAsObject(Constant.APPLY_MORE);
        if(appayTableMore==null){
            appayTableMore = (ArrayList<ApplyTable>) ApplyTableManager.loadNewsChannelsMore(privilege);
            ACache.get(MyApp.app).put(Constant.APPLY_MORE,appayTableMore);
        }
        moreActivity.returnMoreNewsApply(appayTableMore);
    }

    @Override
    public void onItemSwap(ArrayList<ApplyTable> applyTableList) {
        ACache.get(MyApp.app).put(Constant.APPLY_MINE,applyTableList);
    }

    @Override
    public void onItemAddOrRemove(ArrayList<ApplyTable> mineApplyTableList, ArrayList<ApplyTable> moreApplyTableList) {
        ACache.get(MyApp.app).put(Constant.APPLY_MINE,mineApplyTableList);
        ACache.get(MyApp.app).put(Constant.APPLY_MORE,moreApplyTableList);
//        Main3Activity.getMa().applyRequest();
    }
}
