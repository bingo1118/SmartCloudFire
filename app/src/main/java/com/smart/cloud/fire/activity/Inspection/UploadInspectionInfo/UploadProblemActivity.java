package com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.view.BingoDropDowmListView;
import com.smart.cloud.fire.view.BingoViewModel;
import com.smart.cloud.fire.view.SelectPhotoView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UploadProblemActivity extends MvpActivity<UploadProblemPresenter> implements UploadProblemView {


    @Bind(R.id.add_fire_name)
    EditText addFireName;
    @Bind(R.id.add_fire_address)
    EditText addFireAddress;
    @Bind(R.id.add_fire_zjq)
    BingoDropDowmListView addFireZjq;//选择区域
    @Bind(R.id.add_fire_point)
    BingoDropDowmListView add_fire_point;//选择管理员
    @Bind(R.id.add_fire_dev_btn)
    TextView addFireDevBtn;//添加设备按钮。。
    @Bind(R.id.select_photo_view)
    SelectPhotoView select_photo_view;//@@拍照上传
    @Bind(R.id.memo_edit)
    EditText memo_edit;

    private Context mContext;
    private int privilege;
    private String userID;
    private UploadProblemPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_problem);

        ButterKnife.bind(this);
        mContext = this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        select_photo_view.setActivity(this);
        init();
    }

    private void init() {
        addFireZjq.setEditTextHint("区域");
        add_fire_point.setEditTextHint("管理员");
        RxView.clicks(addFireDevBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                addFire();
            }
        });
    }

    @OnClick({  R.id.add_fire_zjq,R.id.add_fire_point})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_fire_zjq:
                if (addFireZjq.ifShow()) {
                    addFireZjq.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                    addFireZjq.setClickable(false);
                    addFireZjq.showLoading();
                }
                break;
            case R.id.add_fire_point:
                if (add_fire_point.ifShow()) {
                    add_fire_point.closePopWindow();
                } else {
                    if(addFireZjq.getSelecedId()==null){
                        T.showShort(mContext,"请先选择区域");
                        return;
                    }
                    mvpPresenter.getManagers(addFireZjq.getSelecedId());
//                    add_fire_point.setClickable(false);
                    add_fire_point.showLoading();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected UploadProblemPresenter createPresenter() {
        if(mPresenter==null){
            mPresenter=new UploadProblemPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 102:
                select_photo_view.onActivityResult(data);
                break;
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void getUsers(ArrayList<BingoViewModel> shopTypes) {
        add_fire_point.setItemsData(shopTypes);
        add_fire_point.showPopWindow();
        add_fire_point.setClickable(true);
        add_fire_point.closeLoading();
    }

    @Override
    public void getUsersFail(String msg) {
        T.showShort(mContext,msg);
        add_fire_point.setClickable(true);
    }

    @Override
    public void getAreaType(ArrayList<BingoViewModel> shopTypes) {
        addFireZjq.setItemsData(shopTypes);
        addFireZjq.showPopWindow();
        addFireZjq.setClickable(true);
        addFireZjq.closeLoading();
    }

    @Override
    public void getAreaTypeFail(String msg) {
        T.showShort(mContext,msg);
        addFireZjq.setClickable(true);
    }

    @Override
    public void addSmokeResult(String msg, int errorCode) {
        T.showShort(mContext, msg);
        if (errorCode == 0) {
            addFireZjq.addFinish();
            add_fire_point.addFinish();
            clearText();
        }
    }

    private void clearText() {
        addFireAddress.setText("");
        addFireName.setText("");
        addFireZjq.setEditTextData("");//@@10.19
        add_fire_point.setEditTextData("");
        select_photo_view.deleteTempPhoto();
    }

    /**
     * 添加设备，提交设备信息。。
     */
    private void addFire() {

        String managerId = addFireZjq.getSelecedId();
        String areaId = add_fire_point.getSelecedId();

        String smokeName = addFireName.getText().toString().trim();
        String address = addFireAddress.getText().toString().trim();

        String memo=memo_edit.getText().toString().trim();

        if(smokeName.length()==0||smokeName.length()==0){
            T.showShort(mContext,"请填写名称");
            return;
        }
        if(areaId==null||areaId.length()==0){
            T.showShort(mContext,"请填选择区域");
            return;
        }
        if(managerId==null||managerId.length()==0){
            T.showShort(mContext,"请填选择类型");
            return;
        }

        final String photo=System.currentTimeMillis()+"";
        String photo1="";
        if(select_photo_view.isPhotoExist()){
            photo1=photo;
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override public void call(Subscriber<? super String> subscriber) {
                    boolean isSuccess= select_photo_view.upload(photo,"hiddenDanger");
                    if(isSuccess){
                        subscriber.onNext("Success");
                    }else{
                        subscriber.onNext("Fail");
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>()
                    {
                        @Override public void onCompleted() {
                        }
                        @Override public void onError(Throwable e) {
                        }
                        @Override public void onNext(String s) {
                            if(s.equals("Success")){
                                T.showShort(mContext,"图片上传成功");
                                mvpPresenter.uploadHiddenDanger(smokeName,address,managerId,userID,areaId,memo,photo);
                            }else{
                                T.showShort(mContext,"图片上传失败");
                                mvpPresenter.uploadHiddenDanger(smokeName,address,managerId,userID,areaId,memo,photo);
                            }
                        }
                    });
        }else{
            mvpPresenter.uploadHiddenDanger(smokeName,address,managerId,userID,areaId,memo,photo);
        }


    }

}
