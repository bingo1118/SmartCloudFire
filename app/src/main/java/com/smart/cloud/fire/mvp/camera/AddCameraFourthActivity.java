package com.smart.cloud.fire.mvp.camera;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.view.XCDropDownListView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/27.
 */
public class AddCameraFourthActivity extends MvpActivity<AddCameraFourthPresenter> implements AddCameraFourthView {
    @Bind(R.id.camera_id)
    TextView cameraId;
    @Bind(R.id.add_repeater_mac)
    EditText addRepeaterMac;
    @Bind(R.id.scan_repeater_ma)
    ImageView scanRepeaterMa;
    @Bind(R.id.camera_name)
    TextView cameraName;
    @Bind(R.id.add_fire_mac)
    EditText addFireMac;
    @Bind(R.id.scan_er_wei_ma)
    ImageView scanErWeiMa;
    @Bind(R.id.camera_pwd)
    TextView cameraPwd;
    @Bind(R.id.add_fire_name)
    EditText addFireName;
    @Bind(R.id.add_fire_lat)
    EditText addFireLat;
    @Bind(R.id.add_fire_lon)
    EditText addFireLon;
    @Bind(R.id.add_fire_address)
    EditText addFireAddress;
    @Bind(R.id.add_fire_zjq)
    XCDropDownListView addFireZjq;
    @Bind(R.id.add_fire_type)
    XCDropDownListView addFireType;
    @Bind(R.id.add_fire_man)
    EditText addFireMan;
    @Bind(R.id.add_fire_man_phone)
    EditText addFireManPhone;
    @Bind(R.id.add_fire_man_two)
    EditText addFireManTwo;
    @Bind(R.id.add_fire_man_phone_two)
    EditText addFireManPhoneTwo;
    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout addFireDevBtn;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private AddCameraFourthPresenter mAddCameraFourthPresenter;
    private String userID;
    private String contactId;
    private Context mContext;
    private int privilege;
    private ShopType mShopType;
    private Area mArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fire);
        ButterKnife.bind(this);
        mContext = this;
        contactId = getIntent().getExtras().getString("contactId");
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        initView();
    }

    private void initView() {
        cameraName.setText("名称");
        addFireMac.setHint("名称");
        cameraPwd.setText("密码");
        addFireName.setHint("密码");
        cameraId.setText("ID");
        addRepeaterMac.setHint("ID");
        addRepeaterMac.setEnabled(false);
        addRepeaterMac.setText(contactId);
        addFireZjq.setEditTextHint("区域");
        addFireType.setEditTextHint("类型");
        scanErWeiMa.setVisibility(View.GONE);
        scanRepeaterMa.setVisibility(View.GONE);
        RxView.clicks(addFireDevBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String placeTypeId = "";
                String areaId = "";
                if(mShopType!=null){
                    placeTypeId = mShopType.getPlaceTypeId();
                }
                if(mArea!=null){
                    areaId = mArea.getAreaId();
                }
                String longitude = addFireLon.getText().toString().trim();
                String latitude = addFireLat.getText().toString().trim();
                String smokeName = addFireMac.getText().toString().trim();
                String smokeMac = addFireName.getText().toString().trim();
                String address = addFireAddress.getText().toString().trim();
                String principal1 = addFireMan.getText().toString().trim();
                String principal2 = addFireManTwo.getText().toString().trim();
                String principal1Phone = addFireManPhone.getText().toString().trim();
                String principal2Phone = addFireManPhoneTwo.getText().toString().trim();
                mvpPresenter.addCamera(contactId,smokeName,smokeMac,address,
                        longitude,latitude,principal1,principal1Phone,principal2,principal2Phone,areaId,placeTypeId);
            }
        });
    }

    @OnClick({R.id.location_image,R.id.add_fire_zjq,R.id.add_fire_type})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.location_image:
                if(Utils.isNetworkAvailable(this)){
                    mvpPresenter.startLocation();
                }
                break;
            case R.id.add_fire_zjq:
                if(addFireZjq.ifShow()){
                    addFireZjq.closePopWindow();
                }else{
                    if(Utils.isNetworkAvailable(this)){
                        mvpPresenter.getPlaceTypeId(userID,privilege+"",2);
                        addFireZjq.setClickable(false);
                        addFireZjq.showLoading();
                    }
                }
                break;
            case R.id.add_fire_type:
                if(addFireType.ifShow()){
                    addFireType.closePopWindow();
                }else{
                    mvpPresenter.getPlaceTypeId(userID,privilege+"",1);
                    addFireType.setClickable(false);
                    addFireType.showLoading();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected AddCameraFourthPresenter createPresenter() {
        mAddCameraFourthPresenter = new AddCameraFourthPresenter(this);
        return mAddCameraFourthPresenter;
    }

    @Override
    protected void onStart() {
        mvpPresenter.initLocation();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        if (addFireZjq.ifShow()) {
            addFireZjq.closePopWindow();
        }
        if (addFireType.ifShow()) {
            addFireType.closePopWindow();
        }
        mvpPresenter.stopLocation();
        super.onDestroy();
    }

    @Override
    public void getLocationData(BDLocation location) {
        addFireLon.setText(location.getLongitude() + "");
        addFireAddress.setText(location.getAddrStr());
        addFireLat.setText(location.getLatitude() + "");
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
    }

    @Override
    public void getDataSuccess(String msg) {
        T.showShort(mContext,msg);
        finish();
    }

    @Override
    public void getShopType(ArrayList<Object> shopTypes) {
        addFireType.setItemsData(shopTypes,mAddCameraFourthPresenter);
        addFireType.showPopWindow();
        addFireType.setClickable(true);
        addFireType.closeLoading();
    }

    @Override
    public void getShopTypeFail(String msg) {
        T.showShort(mContext, msg);
        addFireType.setClickable(true);
        addFireType.closeLoading();
    }

    @Override
    public void getAreaType(ArrayList<Object> shopTypes) {
        addFireZjq.setItemsData(shopTypes,mAddCameraFourthPresenter);
        addFireZjq.showPopWindow();
        addFireZjq.setClickable(true);
        addFireZjq.closeLoading();
    }

    @Override
    public void getAreaTypeFail(String msg) {
        T.showShort(mContext, msg);
        addFireZjq.setClickable(true);
        addFireZjq.closeLoading();
    }

    @Override
    public void getChoiceArea(Area area) {
        mArea = area;
    }

    @Override
    public void getChoiceShop(ShopType shopType) {
        mShopType = shopType;
    }

}
