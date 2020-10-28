package com.smart.cloud.fire.activity.AddNFC;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.GetLocationActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.view.XCDropDownListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

public class AddVirtualPointActivity extends MvpActivity<AddNFCPresenter> implements AddNFCView{


    @Bind(R.id.add_fire_name)
    EditText addFireName;//设备名称。。
    @Bind(R.id.add_fire_lat)
    EditText addFireLat;//经度。。
    @Bind(R.id.add_fire_lon)
    EditText addFireLon;//纬度。。
    @Bind(R.id.add_fire_address)
    EditText addFireAddress;//设备地址。。
    @Bind(R.id.location_image)
    ImageView locationImage;
    @Bind(R.id.add_fire_zjq)
    XCDropDownListView addFireZjq;//选择区域。。
    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout addFireDevBtn;//添加设备按钮。。
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;//加载进度。。
    @Bind(R.id.add_distance)
    EditText add_distance;
    @Bind(R.id.add_memo)
    EditText add_memo;

    private Context mContext;
    private int privilege;
    private String userID;
    private Area mArea;
    private String areaId = "";

    AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_virtual_point);

        ButterKnife.bind(this);
        mContext = this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();

        init();
    }

    private void init() {
        addFireZjq.setEditTextHint("区域");
        RxView.clicks(addFireDevBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                addFire();
            }
        });
    }


    /**
     * 添加设备，提交设备信息。。
     */
    private void addFire() {
        if (mArea != null) {
            areaId = mArea.getAreaId();
        }
        String longitude = addFireLon.getText().toString().trim();
        String latitude = addFireLat.getText().toString().trim();
        String smokeName = addFireName.getText().toString().trim();
        String address = addFireAddress.getText().toString().trim();
        String distance=add_distance.getText().toString().trim();
        String memo=add_memo.getText().toString().trim();

        if(longitude.length()==0||latitude.length()==0){
            T.showShort(mContext,"请获取经纬度");
            return;
        }
        if(smokeName.length()==0||smokeName.length()==0){
            T.showShort(mContext,"请填写名称");
            return;
        }

        if(areaId==null||areaId.length()==0){
            T.showShort(mContext,"请填选择区域");
            return;
        }
        if(distance==null||distance.length()==0){
            distance="100";
        }

        mvpPresenter.addVirtualPoint(areaId,smokeName,memo,longitude,latitude,address,distance,MyApp.getUserID());
    }

    @Override
    protected AddNFCPresenter createPresenter() {
        AddNFCPresenter addNFCPresenter = new AddNFCPresenter(this);
        return addNFCPresenter;
    }




    @Override
    public void onDestroy() {
        mvpPresenter.stopLocation();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        mvpPresenter.initLocation();
        super.onStart();
    }

    @OnClick({  R.id.location_image, R.id.add_fire_zjq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.location_image:
                Intent intent=new Intent(mContext, GetLocationActivity.class);
                startActivityForResult(intent,1);//@@6.20
                break;
            case R.id.add_fire_zjq:
                if (addFireZjq.ifShow()) {
                    addFireZjq.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                    addFireZjq.setClickable(false);
                    addFireZjq.showLoading();
                }
                break;
            default:
                break;
        }
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
        T.showShort(mContext, msg);
    }

    @Override
    public void getDataSuccess(Smoke smoke) {

    }

    @Override
    public void getShopType(ArrayList<Object> shopTypes) {

    }

    @Override
    public void getShopTypeFail(String msg) {
        T.showShort(mContext, msg);
    }

    @Override
    public void getNFCDeviceType(ArrayList<Object> deviceTypes) {

    }

    @Override
    public void getNFCDeviceTypeFail(String msg) {
        T.showShort(mContext, msg);
    }

    @Override
    public void getAreaType(ArrayList<Object> shopTypes) {
        addFireZjq.setItemsData(shopTypes,mvpPresenter);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1://@@6.20
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle=data.getBundleExtra("data");
                    addFireLat.setText(String.format("%.8f",bundle.getDouble("lat")));
                    addFireLon.setText(String.format("%.8f",bundle.getDouble("lon")));
                    addFireAddress.setText(bundle.getString("address"));
                }
                break;
        }

    }



    @Override
    public void addSmokeResult(String msg, int errorCode) {
        T.showShort(mContext, msg);
        if (errorCode == 0) {
            mArea = null;
            clearText();
            addFireZjq.addFinish();
        }
    }

    @Override
    public void getChoiceArea(Area area) {
        mArea = area;
    }

    @Override
    public void getChoiceShop(ShopType shopType) {

    }

    @Override
    public void getChoiceNFCDeviceType(NFCDeviceType nfcDeviceType) {

    }




    /**
     * 清空其他编辑框内容。。
     */
    private void clearText() {
        addFireLon.setText("");
        addFireLat.setText("");
        addFireAddress.setText("");
        addFireName.setText("");
        addFireZjq.setEditTextData("");//@@10.19
        add_memo.setText("");//@@11.28
        add_distance.setText("");//@@11.28
    }
}
