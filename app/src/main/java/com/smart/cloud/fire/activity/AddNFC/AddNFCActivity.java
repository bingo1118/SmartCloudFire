package com.smart.cloud.fire.activity.AddNFC;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.GetLocationActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.view.XCDropDownListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

public class AddNFCActivity extends MvpActivity<AddNFCPresenter> implements AddNFCView{

    @Bind(R.id.add_fire_mac)
    EditText addFireMac;//探测器。。
    @Bind(R.id.add_fire_name)
    EditText addFireName;//设备名称。。
    @Bind(R.id.add_fire_lat)
    EditText addFireLat;//经度。。
    @Bind(R.id.add_fire_lon)
    EditText addFireLon;//纬度。。
    @Bind(R.id.add_fire_address)
    EditText addFireAddress;//设备地址。。
    @Bind(R.id.scan_er_wei_ma)
    ImageView scanErWeiMa;
    @Bind(R.id.location_image)
    ImageView locationImage;
    @Bind(R.id.add_fire_zjq)
    XCDropDownListView addFireZjq;//选择区域。。
    @Bind(R.id.add_fire_type)
    XCDropDownListView addFireType;//选择类型。。
    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout addFireDevBtn;//添加设备按钮。。
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;//加载进度。。
    @Bind(R.id.add_camera_name)
    EditText addCameraName;
    private Context mContext;
    private int privilege;
    private String userID;
    private ShopType mShopType;
    private Area mArea;
    private NFCDeviceType nfcDeviceType;//@@8.16
    private String areaId = "";
    private String shopTypeId = "";

    private boolean mWriteMode = false;
    NfcAdapter mNfcAdapter;
    AlertDialog alertDialog;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mWriteTagFilters;
    IntentFilter[] mNdefExchangeFilters;
    private Tag mDetectedTag;

    private NFCInfo nfcInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nfc);

        ButterKnife.bind(this);
        mContext = this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter==null) {
            toast("设备不支持NFC功能");
            return;
        }
        init();
        initNFC();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
        if (!mWriteMode && (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())
                ||NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()))) {//@@10.19
            byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            String UID = ByteArrayToHexString(myNFCID);
            addFireMac.setText(UID);
            if(alertDialog!=null){
                alertDialog.dismiss();
            }
        }
        // Tag writing mode
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeTag(getNoteAsNdef(), detectedTag);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Sticky notes received from Android
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
//            NdefMessage[] messages = getNdefMessages(getIntent());
//            byte[] payload = messages[0].getRecords()[0].getPayload();
//            setNoteBody(new String(payload));
//            setIntent(new Intent()); // Consume this intent.
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] myNFCID = getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID);
            String UID = ByteArrayToHexString(myNFCID);
            addFireMac.setText(UID);
            setIntent(new Intent()); // Consume this intent.
        }
        if (mNfcAdapter!=null) {
            enableNdefExchangeMode();
        }
    }

    private void initNFC() {
        // Handle all of our received NFC intents in this activity.
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(
                NfcAdapter.ACTION_TAG_DISCOVERED);//@@ 10.19 原NDEF
//        try {
//            ndefDetected.addDataType("text/plain");
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//        }//@@10.19 防止显示NDEF为空时读取不了标签ID
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };

        // Intent filters for writing to a tag
        IntentFilter tagDetected = new IntentFilter(
                NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
    }

    private void init() {
        addFireZjq.setEditTextHint("区域");
        addFireType.setEditTextHint("类型");
        RxView.clicks(addFireDevBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                addFire();
            }
        });

        nfcInfo=new NFCInfo();
    }
    private void writeNFC() {
        // Write to a tag for as long as the dialog is shown.
        disableNdefExchangeMode();
        enableTagWriteMode();


        TextView textView=new TextView(mContext);//@@10.19
        textView.setText("接触标签进行写入操作");
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(textView);
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                         @Override
                                         public void onCancel(DialogInterface dialog) {
                                             disableTagWriteMode();
                                             enableNdefExchangeMode();
                                         }
                                     });
//        alertDialog=new AlertDialog.Builder(this).setTitle("接触标签进行写入操作")
//                .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        disableTagWriteMode();
//                        enableNdefExchangeMode();
//                    }
//                }).create();
        alertDialog=builder.create();
        alertDialog.show();
    }

    /**
     * 添加设备，提交设备信息。。
     */
    private void addFire() {
        if (nfcDeviceType != null) {
            shopTypeId = nfcDeviceType.getPlaceTypeId();//@@8.16
        }
        if (mArea != null) {
            areaId = mArea.getAreaId();
        }
        String longitude = addFireLon.getText().toString().trim();
        String latitude = addFireLat.getText().toString().trim();
        String smokeName = addFireName.getText().toString().trim();
        String smokeMac = addFireMac.getText().toString().trim();
        String address = addFireAddress.getText().toString().trim();
        if(longitude.length()==0||latitude.length()==0){
            toast("请获取经纬度");
            return;
        }
        if(smokeName.length()==0||smokeName.length()==0){
            toast("请填写名称");
            return;
        }
        if(smokeMac.length()==0){
            toast("请填写探测器MAC");
            return;
        }
        if(areaId==null||areaId.length()==0){
            toast("请填选择区域");
            return;
        }
        if(shopTypeId==null||shopTypeId.length()==0){
            toast("请填选择类型");
            return;
        }
        nfcInfo=new NFCInfo(smokeMac,longitude,latitude,areaId,mArea.getAreaName(),shopTypeId,nfcDeviceType.getPlaceTypeName(),smokeName,address);
        writeNFC();
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

    @OnClick({ R.id.scan_er_wei_ma, R.id.location_image, R.id.add_fire_zjq, R.id.add_fire_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_er_wei_ma:
                // Write to a tag for as long as the dialog is shown.
                disableTagWriteMode();
                enableNdefExchangeMode();

                TextView textView=new TextView(mContext);//@@10.19
                textView.setText("接触标签进行写入操作");
                textView.setTextSize(18);
                textView.setGravity(Gravity.CENTER);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(textView);
                builder.setCancelable(true);
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        disableTagWriteMode();
                        enableNdefExchangeMode();
                    }
                });
                alertDialog=builder.create();
                alertDialog.show();
//                alertDialog=new AlertDialog.Builder(this).setTitle("接触标签获取UID")
//                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialog) {
//                                disableTagWriteMode();
//                                enableNdefExchangeMode();
//                            }
//                        }).create();
//                alertDialog.show();
                break;
            case R.id.location_image:
//                mvpPresenter.startLocation();
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
            case R.id.add_fire_type:
                if (addFireType.ifShow()) {
                    addFireType.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 1);
                    addFireType.setClickable(false);
                    addFireType.showLoading();
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
        addFireType.setItemsData(shopTypes,mvpPresenter);
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
    public void getNFCDeviceType(ArrayList<Object> deviceTypes) {
        addFireType.setItemsData(deviceTypes,mvpPresenter);
        addFireType.showPopWindow();
        addFireType.setClickable(true);
        addFireType.closeLoading();
    }

    @Override
    public void getNFCDeviceTypeFail(String msg) {
        T.showShort(mContext, msg);
        addFireType.setClickable(true);
        addFireType.closeLoading();
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
            mShopType = null;
            mArea = null;
            clearText();
            shopTypeId = "";
            addFireMac.setText("");
            addFireZjq.addFinish();
            addFireType.addFinish();
        }
    }

    @Override
    public void getChoiceArea(Area area) {
        mArea = area;
    }

    @Override
    public void getChoiceShop(ShopType shopType) {
        mShopType = shopType;
    }

    @Override
    public void getChoiceNFCDeviceType(NFCDeviceType nfcDeviceType) {
        this.nfcDeviceType=nfcDeviceType;
    }




    /**
     * 清空其他编辑框内容。。
     */
    private void clearText() {
        addFireLon.setText("");
        addFireLat.setText("");
        addFireAddress.setText("");
        addFireName.setText("");
        addFireType.setEditTextData("");
        addFireZjq.setEditTextData("");//@@10.19
        addCameraName.setText("");
    }
    //@@NFC相关>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }
    private void enableNdefExchangeMode() {
        mNfcAdapter.enableForegroundNdefPush(this, getNoteAsNdef());
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
    }
    private NdefMessage getNoteAsNdef() {
        String info=changeNFCInfoToJson(nfcInfo);
        byte[] textBytes = info.getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[] {}, textBytes);
        return new NdefMessage(new NdefRecord[] {
                textRecord
        });
    }
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    private String ByteArrayToHexString(byte [] inarray) { //converts byte arrays to string
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {
                        record
                });
                msgs = new NdefMessage[] {
                        msg
                };
            }
        } else {
            finish();
        }
        return msgs;
    }
    private void setNoteBody(String body) {
        addFireMac.setText("");
        addFireMac.setText(body);
    }
    private void disableNdefExchangeMode() {
        mNfcAdapter.disableForegroundNdefPush(this);
        mNfcAdapter.disableForegroundDispatch(this);
    }
    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] {
                tagDetected
        };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }
    boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    toast("Tag is read-only.");
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    toast("Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size
                            + " bytes.");
                    return false;
                }

                ndef.writeNdefMessage(message);
                if (ndef.canMakeReadOnly()) {
                    ndef.makeReadOnly();//@@设置标签为只读(慎用！！)
                }
                toast("写入数据成功.");
                mvpPresenter.addNFC(userID, privilege + "", nfcInfo.getDeviceName(), nfcInfo.getUid(), nfcInfo.getAddress(),
                        nfcInfo.getLon(), nfcInfo.getLat(), nfcInfo.getDeviceTypeId(),nfcInfo.getAreaId());
                mWriteMode=false;//@@10.19
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        toast("Formatted tag and wrote message");
                        return true;
                    } catch (IOException e) {
                        toast("Failed to format tag.");
                        return false;
                    }
                } else {
                    toast("Tag doesn't support NDEF.");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            toast("写入数据失败");
        }finally {
            if(alertDialog!=null){
                alertDialog.dismiss();
            }
        }

        return false;
    }


    public static String changeNFCInfoToJson(NFCInfo nfcInfo){
        try {
            JSONObject object = new JSONObject();
            String uid = nfcInfo.getUid();
            String  deviceTypeId= nfcInfo.getDeviceTypeId();
            String  deviceTypeName= nfcInfo.getDeviceTypeName();
            String  areaId= nfcInfo.getAreaId();
            String  areaName=nfcInfo.getAreaName();
            String  deviceName= nfcInfo.getDeviceName();
            String  address= nfcInfo.getAddress();
            String  longitude= nfcInfo.getLon();
            String  latitude= nfcInfo.getLat();
            object.put("uid", uid);
            object.put("deviceTypeId", deviceTypeId);
            object.put("deviceTypeName", deviceTypeName);
            object.put("areaId", areaId);
            object.put("areaName", areaName);
            object.put("deviceName", deviceName);
            object.put("address", address);
            object.put("longitude", longitude);
            object.put("latitude", latitude);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
