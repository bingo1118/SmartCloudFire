package com.smart.cloud.fire.mvp.sdcard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.ui.ApMonitorActivity;
import com.smart.cloud.fire.view.NormalDialog;

import fire.cloud.smart.com.smartcloudfire.R;

public class SDCardActivity extends Activity implements View.OnClickListener{
    private Context mContext;
    private Contact contact;
    RelativeLayout sd_format;
    String command;
    boolean isRegFilter = false;
    TextView tv_total_capacity, tv_sd_remainning_capacity,tv_sd_card;
    ImageView format_icon;
    ProgressBar progress_format;
    int SDcardId;
    int sdId;
    int usbId;
    boolean isSDCard;
    int count = 0;
    String idOrIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdcard);
        mContext = this;
        contact = (Contact) getIntent().getSerializableExtra("contact");
        idOrIp=contact.contactId;
        if(contact.ipadressAddress!=null){
            String mark=contact.ipadressAddress.getHostAddress();
            String ip=mark.substring(mark.lastIndexOf(".")+1,mark.length());
            if(!ip.equals("")&&ip!=null){
                idOrIp=ip;
            }
        }
        initComponent();
        showSDProgress();
        regFilter();
        command=createCommand("80", "0", "00");
        P2PHandler.getInstance().getSdCardCapacity(idOrIp, contact.contactPassword,command);
    }

    public void initComponent() {
        tv_sd_card = (TextView) findViewById(R.id.tv_sd_card);
        tv_total_capacity = (TextView) findViewById(R.id.tv_sd_capacity);
        tv_sd_remainning_capacity = (TextView)findViewById(R.id.tv_sd_remainning_capacity);
        sd_format = (RelativeLayout) findViewById(R.id.sd_format);
        format_icon = (ImageView) findViewById(R.id.format_icon);
        progress_format = (ProgressBar) findViewById(R.id.progress_format);
        sd_format.setOnClickListener(this);
        if (contact.contactType == P2PValue.DeviceType.NPC) {
            sd_format.setVisibility(RelativeLayout.GONE);
        }
        tv_sd_card.setText(contact.contactName);
    }

    public void regFilter() {
        isRegFilter = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.P2P.ACK_GET_SD_CARD_CAPACITY);
        filter.addAction(ConstantValues.P2P.RET_GET_SD_CARD_CAPACITY);
        filter.addAction(ConstantValues.P2P.ACK_GET_SD_CARD_FORMAT);
        filter.addAction(ConstantValues.P2P.RET_GET_SD_CARD_FORMAT);
        filter.addAction(ConstantValues.P2P.RET_GET_USB_CAPACITY);
        filter.addAction(ConstantValues.P2P.RET_DEVICE_NOT_SUPPORT);
        mContext.registerReceiver(br, filter);
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(
                    ConstantValues.P2P.ACK_GET_SD_CARD_CAPACITY)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {

                    Intent i = new Intent();
                    i.setAction(ConstantValues.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);

                } else if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get npc time");
                    P2PHandler.getInstance().getSdCardCapacity(idOrIp,
                            contact.contactPassword, command);
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_GET_SD_CARD_CAPACITY)) {
                int total_capacity = intent.getIntExtra("total_capacity", -1);
                int remain_capacity = intent.getIntExtra("remain_capacity", -1);
                int state = intent.getIntExtra("state", -1);
                SDcardId = intent.getIntExtra("SDcardID", -1);
                String id = Integer.toBinaryString(SDcardId);
                Log.e("id", "msga" + id);
                while (id.length() < 8) {
                    id = "0" + id;
                }
                char index = id.charAt(3);
                Log.e("id", "msgb" + id);
                Log.e("id", "msgc" + index);
                if (state == 1) {
                    if (index == '1') {
                        sdId = SDcardId;
                        tv_total_capacity.setText(String
                                .valueOf(total_capacity) + "M");
                        tv_sd_remainning_capacity.setText(String
                                .valueOf(remain_capacity) + "M");
                        showSDImg();
                    } else if (index == '0') {
                        usbId = SDcardId;
                    }
                } else {
                    Intent back = new Intent();
                    back.setAction(ConstantValues.Action.REPLACE_MAIN_CONTROL);
                    mContext.sendBroadcast(back);
                    Toast.makeText(mContext, "SD卡不存在", Toast.LENGTH_SHORT).show();
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.ACK_GET_SD_CARD_FORMAT)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {

                    Intent i = new Intent();
                    i.setAction(ConstantValues.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);

                } else if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get npc time");
                    P2PHandler.getInstance().setSdFormat(idOrIp,
                            contact.contactPassword, sdId);
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_GET_SD_CARD_FORMAT)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.SD_FORMAT.SD_CARD_SUCCESS) {
                    Toast.makeText(mContext, "SD卡格式化成功", Toast.LENGTH_SHORT).show();
                } else if (result == ConstantValues.P2P_SET.SD_FORMAT.SD_CARD_FAIL) {
                    Toast.makeText(mContext, "SD卡格式化失败", Toast.LENGTH_SHORT).show();
                } else if (result == ConstantValues.P2P_SET.SD_FORMAT.SD_NO_EXIST) {
                    Toast.makeText(mContext, "SD卡不存在", Toast.LENGTH_SHORT).show();
                }
                showSDImg();
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_GET_USB_CAPACITY)) {
                int total_capacity = intent.getIntExtra("total_capacity", -1);
                int remain_capacity = intent.getIntExtra("remain_capacity", -1);
                int state = intent.getIntExtra("state", -1);
                SDcardId = intent.getIntExtra("SDcardID", -1);
                String id = Integer.toBinaryString(SDcardId);
                Log.e("id", "msga" + id);
                while (id.length() < 8) {
                    id = "0" + id;
                }
                char index = id.charAt(3);
                Log.e("id", "msgb" + id);
                Log.e("id", "msgc" + index);
                if (state == 1) {
                    if (index == '1') {
                        sdId = SDcardId;
                        tv_total_capacity.setText(String
                                .valueOf(total_capacity) + "M");
                        tv_sd_remainning_capacity.setText(String
                                .valueOf(remain_capacity) + "M");
                        showSDImg();
                    } else if (index == '0') {
                        usbId = SDcardId;
                    }
                } else {
                    count++;
                    if (contact.contactType == P2PValue.DeviceType.IPC) {
                        if (count == 1) {
                            Intent back = new Intent();
                            back.setAction(ConstantValues.Action.REPLACE_MAIN_CONTROL);
                            mContext.sendBroadcast(back);
                            Toast.makeText(mContext, "SD卡不存在", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (count == 2) {
                            Intent back = new Intent();
                            back.setAction(ConstantValues.Action.REPLACE_MAIN_CONTROL);
                            mContext.sendBroadcast(back);
                            Toast.makeText(mContext, "SD卡不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_DEVICE_NOT_SUPPORT)) {
                Intent back = new Intent();
                back.setAction(ConstantValues.Action.REPLACE_MAIN_CONTROL);
                mContext.sendBroadcast(back);
                Toast.makeText(mContext, "不支持", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void showSDProgress() {
        format_icon.setVisibility(ImageView.GONE);
        progress_format.setVisibility(progress_format.VISIBLE);
        sd_format.setClickable(false);
    }

    public String createCommand(String bCommandType, String bOption,
                                String SDCardCounts) {
        return bCommandType + bOption + SDCardCounts;
    }

    public void showSDImg() {
        format_icon.setVisibility(ImageView.VISIBLE);
        progress_format.setVisibility(progress_format.GONE);
        sd_format.setClickable(true);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.sd_format:
                final NormalDialog dialog = new NormalDialog(mContext, mContext
                        .getResources().getString(R.string.sd_formatting), mContext
                        .getResources().getString(R.string.delete_sd_remind),
                        mContext.getResources().getString(R.string.ensure),
                        mContext.getResources().getString(R.string.cancel));
                dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

                    @Override
                    public void onClick() {
                        // TODO Auto-generated method stub
                        P2PHandler.getInstance().setSdFormat(idOrIp,
                                contact.contactPassword, sdId);
                        Log.e("SDcardId", "SDcardId" + SDcardId);
                    }
                });
                dialog.setOnButtonCancelListener(new NormalDialog.OnButtonCancelListener() {

                    @Override
                    public void onClick() {
                        // TODO Auto-generated method stub
                        showSDImg();
                        dialog.dismiss();
                    }
                });
                dialog.showNormalDialog();
                dialog.setCanceledOnTouchOutside(false);
                showSDProgress();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegFilter == true) {
            mContext.unregisterReceiver(br);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(mContext,ApMonitorActivity.class);
            i.putExtra("contact",contact);
            startActivity(i);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
