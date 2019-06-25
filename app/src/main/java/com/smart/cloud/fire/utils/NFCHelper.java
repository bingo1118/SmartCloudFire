package com.smart.cloud.fire.utils;

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
import android.os.Parcelable;
import android.view.Gravity;
import android.widget.TextView;

import com.smart.cloud.fire.activity.AddNFC.NFCInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class NFCHelper{

    private static Activity mContext;
    private static NFCHelper helper;
    public static NfcAdapter mNfcAdapter;

    private static NFCInfo nfcInfo;
    private static PendingIntent mNfcPendingIntent;
    private static IntentFilter[] mWriteTagFilters;
    private static IntentFilter[] mNdefExchangeFilters;

    AlertDialog alertDialog;

    private boolean mWriteMode = false;//是否处在写入模式

    public OnWriteCompeletedListener listener;

    public void setOnWriteCompeletedListener(OnWriteCompeletedListener listener){
        this.listener=listener;
    }

    public interface OnWriteCompeletedListener{
        public void onWriteCompeleted();
    }


    /**
     * 获取NFC帮助类示例
     * @param context
     * @return
     */
    public static NFCHelper getInstance(Activity context){
        if(helper==null){
            helper=new NFCHelper();
        }
        mContext=context;
        nfcInfo=new NFCInfo();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
        initNFC();
        return helper;
    }

    private static void initNFC() {
        // Handle all of our received NFC intents in this activity.
        mNfcPendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, mContext.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(
                NfcAdapter.ACTION_TAG_DISCOVERED);//@@ 10.19 原NDEF
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };

        // Intent filters for writing to a tag
        IntentFilter tagDetected = new IntentFilter(
                NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
    }

    /**
     * 设备是否支持NFC
     * @return
     */
    public static boolean isSupportNFC(){
        return mNfcAdapter!=null;
    }

    public void changeToWriteMode(){
        disableNdefExchangeMode();
        enableTagWriteMode();
    }

    public void changeToReadMode(){
        disableTagWriteMode();
        enableNdefExchangeMode();
    }

    public void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(mContext);
    }

    private void enableNdefExchangeMode() {
        mNfcAdapter.enableForegroundNdefPush(mContext, getNoteAsNdef());
        mNfcAdapter.enableForegroundDispatch(mContext, mNfcPendingIntent, mNdefExchangeFilters, null);
    }


    private void disableNdefExchangeMode() {
        mNfcAdapter.disableForegroundNdefPush(mContext);
        mNfcAdapter.disableForegroundDispatch(mContext);
    }
    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] {
                tagDetected
        };
        mNfcAdapter.enableForegroundDispatch(mContext, mNfcPendingIntent, mWriteTagFilters, null);
    }



    public boolean writeTag( Tag tag) {
        NdefMessage message=getNoteAsNdef();
        int size = message.toByteArray().length;

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    T.showShort(mContext,"Tag is read-only.");
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    T.showShort(mContext,"Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size
                            + " bytes.");
                    return false;
                }

                ndef.writeNdefMessage(message);
//                if (ndef.canMakeReadOnly()) {
//                    ndef.makeReadOnly();//@@设置标签为只读(慎用！！)
//                }
                T.showShort(mContext,"写入数据成功.");
                if(listener!=null){
                    listener.onWriteCompeleted();
                }
                mWriteMode=false;//@@10.19
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        T.showShort(mContext,"Formatted tag and wrote message");
                        return true;
                    } catch (IOException e) {
                        T.showShort(mContext,"Failed to format tag.");
                        return false;
                    }
                } else {
                    T.showShort(mContext,"Tag doesn't support NDEF.");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            T.showShort(mContext,"写入数据失败");
        }finally {
            if(alertDialog!=null){
                alertDialog.dismiss();
            }
        }

        return false;
    }

    private static NdefMessage getNoteAsNdef() {
        String info=changeNFCInfoToJson(nfcInfo);
        byte[] textBytes = info.getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[] {}, textBytes);
        return new NdefMessage(new NdefRecord[] {
                textRecord
        });
    }

    public String getUID(){
        String UID="";
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals((mContext).getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages((mContext).getIntent());
            byte[] myNFCID = (mContext).getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID);
            UID = Utils.ByteArrayToHexString(myNFCID);
            (mContext).setIntent(new Intent()); // Consume this intent.
        }
        return UID;
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
        }
        return msgs;
    }

    public void writeNFC(NFCInfo info) {
        nfcInfo=info;
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
            }
        });
        alertDialog=builder.create();
        alertDialog.show();
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
            String  producer= nfcInfo.getProducer();
            String  makeTime= nfcInfo.getMakeTime();
            String  workerPhone= nfcInfo.getWorkerPhone();
            object.put("uid", uid);
            object.put("deviceTypeId", deviceTypeId);
            object.put("deviceTypeName", deviceTypeName);
            object.put("areaId", areaId);
            object.put("areaName", areaName);
            object.put("deviceName", deviceName);
            object.put("address", address);
            object.put("longitude", longitude);
            object.put("latitude", latitude);
            object.put("producer", producer);
            object.put("makeTime", makeTime);
            object.put("workerPhone", workerPhone);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean ismWriteMode() {
        return mWriteMode;
    }

}
