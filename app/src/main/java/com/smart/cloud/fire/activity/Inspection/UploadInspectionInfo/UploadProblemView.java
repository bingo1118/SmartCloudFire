package com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo;

import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.User;
import com.smart.cloud.fire.view.BingoViewModel;

import java.util.ArrayList;

public interface UploadProblemView {

    void showLoading();
    void hideLoading();
    void getUsers(ArrayList<BingoViewModel> shopTypes);
    void getUsersFail(String msg);
    void getAreaType(ArrayList<BingoViewModel> shopTypes);
    void getAreaTypeFail(String msg);
    void addSmokeResult(String msg,int errorCode);
}
