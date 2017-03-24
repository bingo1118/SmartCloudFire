package com.smart.cloud.fire.mvp.recordProject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.smart.cloud.fire.adapter.DateNumericAdapter;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.ui.ApMonitorActivity;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.view.WheelView;

import fire.cloud.smart.com.smartcloudfire.R;

public class RecordProjectActivity extends Activity implements View.OnClickListener,
        View.OnTouchListener  {
    private Context mContext;
    private Contact contact;
    private boolean isRegFilter = false;

    RelativeLayout change_record_time, change_plan_time;
    LinearLayout record_type_radio, record_time_radio, time_picker,ontime_lin;
    ProgressBar progressBar_record_time,
            progressBar_plan_time;
    RadioButton radio_one, radio_two, radio_three;
    RadioButton radio_one_time, radio_two_time, radio_three_time;
    TextView time_text,tv_control_record;
    WheelView hour_from, minute_from, hour_to, minute_to;
    String cur_modify_plan_time;
    int cur_modify_record_type;
    int cur_modify_record_time;

    RelativeLayout change_record, change_pre_record;
    ProgressBar progressBar_record, progressBar_pre_record;
    ImageView record_img, pre_record_img;
    TextView record_text, pre_record_text;
    RelativeLayout scroll_view;
    Button bt_plan_time;
    int recordState;
    int last_record;
    int last_modify_record;
    boolean isOpenPreRecord = false;
    int last_pre_record;
    int last_modify_pre_record;
    boolean isSupportPreRecored = false;
    int type;
    String idOrIp;

    String command;
    int SDcardId;
    int sdId;
    int usbId;
    boolean isSDCard;
    int count = 0;
    boolean isDropDown=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_project);
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
        regFilter();

        showProgress_record_type();
        P2PHandler.getInstance()
                .getNpcSettings(idOrIp, contact.contactPassword);
        command=createCommand("80", "0", "00");
        P2PHandler.getInstance().getSdCardCapacity(idOrIp, contact.contactPassword,command);
    }

    public void initComponent() {
        scroll_view = (RelativeLayout) findViewById(R.id.scroll_view);
        record_type_radio = (LinearLayout) findViewById(R.id.record_type_radio);
        radio_one = (RadioButton) findViewById(R.id.radio_one);
        radio_two = (RadioButton) findViewById(R.id.radio_two);
        radio_three = (RadioButton) findViewById(R.id.radio_three);

        change_record_time = (RelativeLayout) findViewById(R.id.change_record_time);
        record_time_radio = (LinearLayout) findViewById(R.id.record_time_radio);
        progressBar_record_time = (ProgressBar) findViewById(R.id.progressBar_record_time);

        radio_one_time = (RadioButton) findViewById(R.id.radio_one_time);
        radio_two_time = (RadioButton) findViewById(R.id.radio_two_time);
        radio_three_time = (RadioButton) findViewById(R.id.radio_three_time);

        change_plan_time = (RelativeLayout) findViewById(R.id.change_plan_time);
        ontime_lin = (LinearLayout) findViewById(R.id.ontime_lin);
        progressBar_plan_time = (ProgressBar) findViewById(R.id.progressBar_plan_time);
        time_picker = (LinearLayout)findViewById(R.id.time_picker);
        time_text = (TextView) findViewById(R.id.time_text);
        tv_control_record = (TextView) findViewById(R.id.tv_control_record);
        tv_control_record.setText(contact.contactName);
        initTimePicker();
        bt_plan_time = (Button) findViewById(R.id.bt_plan_time);

        bt_plan_time.setOnClickListener(this);
        radio_one.setOnClickListener(this);
        radio_two.setOnClickListener(this);
        radio_three.setOnClickListener(this);

        radio_one_time.setOnClickListener(this);
        radio_two_time.setOnClickListener(this);
        radio_three_time.setOnClickListener(this);

        change_record = (RelativeLayout) findViewById(R.id.change_record);
        record_img = (ImageView) findViewById(R.id.record_img);
        record_text = (TextView)findViewById(R.id.record_text);
        progressBar_record = (ProgressBar) findViewById(R.id.progressBar_record);

        change_pre_record = (RelativeLayout) findViewById(R.id.change_pre_record);
        pre_record_text = (TextView) findViewById(R.id.pre_record_text);
        pre_record_img = (ImageView)findViewById(R.id.pre_record_img);
        progressBar_pre_record = (ProgressBar) findViewById(R.id.progressBar_pre_record);
        change_record.setOnClickListener(this);
        change_pre_record.setOnClickListener(this);
        change_pre_record.setClickable(false);
    }

    public void initTimePicker() {
        hour_from = (WheelView) findViewById(R.id.hour_from);
        hour_from.setViewAdapter(new DateNumericAdapter(mContext, 0, 23));
        hour_from.setCyclic(true);

        minute_from = (WheelView)findViewById(R.id.minute_from);
        minute_from.setViewAdapter(new DateNumericAdapter(mContext, 0, 59));
        minute_from.setCyclic(true);

        hour_to = (WheelView)findViewById(R.id.hour_to);
        hour_to.setViewAdapter(new DateNumericAdapter(mContext, 0, 23));
        hour_to.setCyclic(true);

        minute_to = (WheelView)findViewById(R.id.minute_to);
        minute_to.setViewAdapter(new DateNumericAdapter(mContext, 0, 59));
        minute_to.setCyclic(true);
        hour_from.setOnTouchListener(this);
        minute_from.setOnTouchListener(this);
        hour_to.setOnTouchListener(this);
        minute_to.setOnTouchListener(this);
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.P2P.ACK_RET_GET_NPC_SETTINGS);

        filter.addAction(ConstantValues.P2P.ACK_RET_SET_RECORD_TYPE);
        filter.addAction(ConstantValues.P2P.RET_SET_RECORD_TYPE);
        filter.addAction(ConstantValues.P2P.RET_GET_RECORD_TYPE);

        filter.addAction(ConstantValues.P2P.ACK_RET_SET_RECORD_TIME);
        filter.addAction(ConstantValues.P2P.RET_SET_RECORD_TIME);
        filter.addAction(ConstantValues.P2P.RET_GET_RECORD_TIME);

        filter.addAction(ConstantValues.P2P.ACK_RET_SET_RECORD_PLAN_TIME);
        filter.addAction(ConstantValues.P2P.RET_SET_RECORD_PLAN_TIME);
        filter.addAction(ConstantValues.P2P.RET_GET_RECORD_PLAN_TIME);

        filter.addAction(ConstantValues.P2P.ACK_RET_SET_REMOTE_RECORD);
        filter.addAction(ConstantValues.P2P.RET_SET_REMOTE_RECORD);
        filter.addAction(ConstantValues.P2P.RET_GET_REMOTE_RECORD);
        filter.addAction(ConstantValues.P2P.RET_GET_PRE_RECORD);
        filter.addAction(ConstantValues.P2P.ACK_RET_SET_PRE_RECORD);
        filter.addAction(ConstantValues.P2P.RET_SET_PRE_RECORD);
        filter.addAction(ConstantValues.P2P.ACK_GET_SD_CARD_CAPACITY);
        filter.addAction(ConstantValues.P2P.RET_GET_SD_CARD_CAPACITY);
        filter.addAction(ConstantValues.P2P.ACK_GET_SD_CARD_FORMAT);
        filter.addAction(ConstantValues.P2P.RET_GET_SD_CARD_FORMAT);
        filter.addAction(ConstantValues.P2P.RET_GET_USB_CAPACITY);
        filter.addAction(ConstantValues.P2P.RET_DEVICE_NOT_SUPPORT);
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(
                    ConstantValues.P2P.ACK_RET_GET_NPC_SETTINGS)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(ConstantValues.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get npc settings");
                    P2PHandler.getInstance().getNpcSettings(idOrIp,
                            contact.contactPassword);
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_GET_RECORD_TYPE)) {
                type = intent.getIntExtra("type", -1);
                updateRecordType(type);
                showRecordType();
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_SET_RECORD_TYPE)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.RECORD_TYPE_SET.SETTING_SUCCESS) {
                    P2PHandler.getInstance().getNpcSettings(idOrIp,
                            contact.contactPassword);
                    // updateRecordType(cur_modify_record_type);
                    // showRecordType();
                    Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    showRecordType();
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_GET_RECORD_TIME)) {
                int time = intent.getIntExtra("time", -1);
                if (time == ConstantValues.P2P_SET.RECORD_TIME_SET.RECORD_TIME_ONE_MINUTE) {
                    radio_one_time.setChecked(true);
                } else if (time == ConstantValues.P2P_SET.RECORD_TIME_SET.RECORD_TIME_TWO_MINUTE) {
                    radio_two_time.setChecked(true);
                } else if (time == ConstantValues.P2P_SET.RECORD_TIME_SET.RECORD_TIME_THREE_MINUTE) {
                    radio_three_time.setChecked(true);
                }
                radio_one_time.setEnabled(true);
                radio_two_time.setEnabled(true);
                radio_three_time.setEnabled(true);
                progressBar_record_time.setVisibility(RelativeLayout.GONE);
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_SET_RECORD_TIME)) {
                int result = intent.getIntExtra("result", -1);
                if (result == 0) {
                    if (cur_modify_record_time == ConstantValues.P2P_SET.RECORD_TIME_SET.RECORD_TIME_ONE_MINUTE) {
                        radio_one_time.setChecked(true);
                    } else if (cur_modify_record_time == ConstantValues.P2P_SET.RECORD_TIME_SET.RECORD_TIME_TWO_MINUTE) {
                        radio_two_time.setChecked(true);
                    } else if (cur_modify_record_time == ConstantValues.P2P_SET.RECORD_TIME_SET.RECORD_TIME_THREE_MINUTE) {
                        radio_three_time.setChecked(true);
                    }
                    radio_one_time.setEnabled(true);
                    radio_two_time.setEnabled(true);
                    radio_three_time.setEnabled(true);
                    progressBar_record_time.setVisibility(RelativeLayout.GONE);
                    Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    radio_one_time.setEnabled(true);
                    radio_two_time.setEnabled(true);
                    radio_three_time.setEnabled(true);
                    progressBar_record_time.setVisibility(RelativeLayout.GONE);
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_GET_RECORD_PLAN_TIME)) {
                String time = intent.getStringExtra("time");
                Log.e("time", time);
                String startTime1 = time.substring(0, 2);
                String startTime2 = time.substring(3, 5);
                String endTime1 = time.substring(6, 8);
                String endTime2 = time.substring(9, 11);
                if (Integer.parseInt(startTime1) < 10) {
                    startTime1 = time.substring(1, 2);
                }
                if (Integer.parseInt(startTime2) < 10) {
                    startTime2 = time.substring(4, 5);
                }
                if (Integer.parseInt(endTime1) < 10) {
                    endTime1 = time.substring(7, 8);
                }
                if (Integer.parseInt(endTime2) < 10) {
                    endTime2 = time.substring(10, 11);
                }
                hour_from.setCurrentItem(Integer.parseInt(startTime1));
                minute_from.setCurrentItem(Integer.parseInt(startTime2));
                hour_to.setCurrentItem(Integer.parseInt(endTime1));
                minute_to.setCurrentItem(Integer.parseInt(endTime2));
                Log.e("time", startTime1 + " " + startTime2);
                Log.e("time", endTime1 + " " + endTime2);
                time_text.setText(time);
                change_plan_time.setEnabled(true);
                progressBar_plan_time.setVisibility(RelativeLayout.GONE);
                time_text.setVisibility(RelativeLayout.VISIBLE);
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_SET_RECORD_PLAN_TIME)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.RECORD_PLAN_TIME_SET.SETTING_SUCCESS) {
                    time_text.setText(cur_modify_plan_time);
                    change_plan_time.setEnabled(true);
                    progressBar_plan_time.setVisibility(RelativeLayout.GONE);
                    time_text.setVisibility(RelativeLayout.VISIBLE);
                    Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    change_plan_time.setEnabled(true);
                    progressBar_plan_time.setVisibility(RelativeLayout.GONE);
                    time_text.setVisibility(RelativeLayout.VISIBLE);
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.ACK_RET_SET_RECORD_TYPE)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(ConstantValues.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set npc settings record type");
                    P2PHandler.getInstance().setRecordType(idOrIp,
                            contact.contactPassword, cur_modify_record_type);
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.ACK_RET_SET_RECORD_TIME)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(ConstantValues.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set npc settings record time");
                    P2PHandler.getInstance().setRecordType(idOrIp,
                            contact.contactPassword, cur_modify_record_type);
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.ACK_RET_SET_RECORD_PLAN_TIME)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(ConstantValues.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my",
                            "net error resend:set npc settings record plan time");
                    P2PHandler.getInstance().setRecordPlanTime(idOrIp,
                            contact.contactPassword, cur_modify_plan_time);
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_GET_REMOTE_RECORD)) {
                int state = intent.getIntExtra("state", -1);
                progressBar_record.setVisibility(RelativeLayout.GONE);
                record_img.setVisibility(RelativeLayout.VISIBLE);
                updateRecord(state);
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_SET_REMOTE_RECORD)) {
                int state = intent.getIntExtra("state", -1);
                P2PHandler.getInstance().getNpcSettings(idOrIp,
                        contact.contactPassword);
                // updateRecord(state);
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.ACK_RET_SET_REMOTE_RECORD)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(ConstantValues.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set remote record");
                    P2PHandler.getInstance().setRemoteRecord(idOrIp,
                            contact.contactPassword, last_modify_record);
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_GET_PRE_RECORD)) {
                int state = intent.getIntExtra("state", -1);
                isSupportPreRecored = true;
                change_pre_record.setClickable(true);
                if (type == ConstantValues.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_ALARM) {
                    change_pre_record.setVisibility(RelativeLayout.VISIBLE);
                }
                if (state == 1) {
                    pre_record_img
                            .setBackgroundResource(R.drawable.ic_checkbox_on);
                    last_pre_record = ConstantValues.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_ON;
                } else if (state == 0) {
                    pre_record_img
                            .setBackgroundResource(R.drawable.ic_checkbox_off);
                    last_pre_record = ConstantValues.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_OFF;
                }
                showPreRecordImg();
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.ACK_RET_SET_PRE_RECORD)) {
                int result = intent.getIntExtra("state", -1);
                if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(ConstantValues.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set npc settings record type");
                    P2PHandler.getInstance().setPreRecord(idOrIp,
                            contact.contactPassword, last_modify_pre_record);
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_SET_PRE_RECORD)) {
                int result = intent.getIntExtra("result", -1);
                if (result == 0) {
                    P2PHandler.getInstance().getNpcSettings(idOrIp,
                            contact.contactPassword);
                    Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                } else if (result == 83) {
                    Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
                }
            }else if (intent.getAction().equals(
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
                Log.e("usb","no sd card");
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
                    } else if (index == '0') {
                        usbId = SDcardId;
                    }
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
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_GET_USB_CAPACITY)) {
                Log.e("usb","get usb");
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
                    } else if (index == '0') {
                        usbId = SDcardId;
                    }
                } else {
                    Log.e("usb","no usb");
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
                        //sd_card_remainning_capacity.setBackgroundResource(R.drawable.tiao_bg_bottom);
                    }
                }
            } else if (intent.getAction().equals(
                    ConstantValues.P2P.RET_DEVICE_NOT_SUPPORT)) {

            }
        }

    };

    public void updateRecord(int state) {
        if (state == ConstantValues.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON) {
            last_record = ConstantValues.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON;
            record_img.setBackgroundResource(R.drawable.ic_checkbox_on);
        } else {
            last_record = ConstantValues.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_OFF;
            record_img.setBackgroundResource(R.drawable.ic_checkbox_off);
        }
    }

    void updateRecordType(int type) {
        if (type == ConstantValues.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_MANUAL) {
            radio_one.setChecked(true);
            hideRecordTime();
            hidePlanTime();
            showManual();
        } else if (type == ConstantValues.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_ALARM) {
            radio_two.setChecked(true);
            hidePlanTime();
            hideManual();
            showRecordTime();
        } else if (type == ConstantValues.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_TIMER) {
            radio_three.setChecked(true);
            hideRecordTime();
            hideManual();
            showPlanTime();
        }
    }

    public void showRecordType() {
        record_type_radio.setVisibility(RelativeLayout.VISIBLE);
        radio_one.setEnabled(true);
        radio_two.setEnabled(true);
        radio_three.setEnabled(true);
    }

    public void showProgress_record_type() {
        record_type_radio.setVisibility(RelativeLayout.GONE);
    }

    public void showRecordTime() {
        change_record_time.setVisibility(RelativeLayout.VISIBLE);
        record_time_radio.setVisibility(RelativeLayout.VISIBLE);
        progressBar_record_time.setVisibility(RelativeLayout.GONE);
        if (isSupportPreRecored == true) {
            change_pre_record.setVisibility(RelativeLayout.VISIBLE);
        } else {
            change_pre_record.setVisibility(RelativeLayout.GONE);
        }
    }

    public void showProgress_record_time() {
        change_record_time.setVisibility(RelativeLayout.VISIBLE);
        record_time_radio.setVisibility(RelativeLayout.VISIBLE);
        progressBar_record_time.setVisibility(RelativeLayout.VISIBLE);
    }

    public void showPlanTime() {
        change_pre_record.setVisibility(RelativeLayout.GONE);
        change_record.setVisibility(RelativeLayout.GONE);
//		time_picker.setVisibility(RelativeLayout.VISIBLE);
//		change_plan_time.setVisibility(RelativeLayout.VISIBLE);
        ontime_lin.setVisibility(RelativeLayout.VISIBLE);
//		progressBar_plan_time.setVisibility(RelativeLayout.GONE);
        time_text.setVisibility(RelativeLayout.VISIBLE);

    }

    public void showProgress_plan_time() {
        time_picker.setVisibility(RelativeLayout.VISIBLE);
        change_plan_time.setVisibility(RelativeLayout.VISIBLE);
        //change_plan_time.setEnabled(false);
        progressBar_plan_time.setVisibility(RelativeLayout.VISIBLE);
        time_text.setVisibility(RelativeLayout.GONE);
    }

    public void showManual() {
        change_record.setVisibility(RelativeLayout.VISIBLE);
        change_pre_record.setVisibility(RelativeLayout.GONE);
    }

    public void showPreRecordProgress() {
        progressBar_pre_record.setVisibility(ProgressBar.VISIBLE);
        pre_record_img.setVisibility(ImageView.GONE);
    }

    public void showPreRecordImg() {
        progressBar_pre_record.setVisibility(ProgressBar.GONE);
        pre_record_img.setVisibility(ImageView.VISIBLE);
    }

    public void hideRecordTime() {
        change_record_time.setVisibility(RelativeLayout.GONE);
        record_time_radio.setVisibility(RelativeLayout.GONE);
    }

    public void hidePlanTime() {
        ontime_lin.setVisibility(RelativeLayout.GONE);
    }

    public void hideManual() {
        change_record.setVisibility(RelativeLayout.GONE);
    }

    public void showRecordSwitchProgress() {
        progressBar_record.setVisibility(RelativeLayout.VISIBLE);
        record_img.setVisibility(RelativeLayout.GONE);
    }

    public void showRecordSwitchImg() {
        progressBar_record.setVisibility(RelativeLayout.GONE);
        record_img.setVisibility(RelativeLayout.VISIBLE);
    }

    public String createCommand(String bCommandType, String bOption,
                                String SDCardCounts) {
        return bCommandType + bOption + SDCardCounts;
    }


    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        if (arg1.getAction() == MotionEvent.ACTION_UP) {
            scroll_view.requestDisallowInterceptTouchEvent(false);
        } else {
            scroll_view.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.change_record:
                progressBar_record.setVisibility(RelativeLayout.VISIBLE);
                record_img.setVisibility(RelativeLayout.GONE);
                if (last_record == ConstantValues.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON) {
                    last_modify_record = ConstantValues.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_OFF;
                    P2PHandler.getInstance().setRemoteRecord(idOrIp,
                            contact.contactPassword, last_modify_record);
                } else {
                    last_modify_record = ConstantValues.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON;
                    P2PHandler.getInstance().setRemoteRecord(idOrIp,
                            contact.contactPassword, last_modify_record);
                }
                break;
            case R.id.radio_one:
                radio_one.setEnabled(false);
                radio_two.setEnabled(false);
                radio_three.setEnabled(false);
                cur_modify_record_type = ConstantValues.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_MANUAL;
                showRecordSwitchProgress();
                P2PHandler.getInstance().setRecordType(idOrIp,
                        contact.contactPassword, cur_modify_record_type);
                break;
            case R.id.radio_two:
                radio_one.setEnabled(false);
                radio_two.setEnabled(false);
                radio_three.setEnabled(false);
                cur_modify_record_type = ConstantValues.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_ALARM;
                showPreRecordProgress();
                P2PHandler.getInstance().setRecordType(idOrIp,
                        contact.contactPassword, cur_modify_record_type);
                break;
            case R.id.radio_three:
                radio_one.setEnabled(false);
                radio_two.setEnabled(false);
                radio_three.setEnabled(false);
                cur_modify_record_type = ConstantValues.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_TIMER;
                P2PHandler.getInstance().setRecordType(idOrIp,
                        contact.contactPassword, cur_modify_record_type);
                break;
            case R.id.radio_one_time:
                progressBar_record_time.setVisibility(RelativeLayout.VISIBLE);
                radio_one_time.setEnabled(false);
                radio_two_time.setEnabled(false);
                radio_three_time.setEnabled(false);
                cur_modify_record_time = ConstantValues.P2P_SET.RECORD_TIME_SET.RECORD_TIME_ONE_MINUTE;
                P2PHandler.getInstance().setRecordTime(idOrIp,
                        contact.contactPassword, cur_modify_record_time);
                break;
            case R.id.radio_two_time:
                progressBar_record_time.setVisibility(RelativeLayout.VISIBLE);
                radio_one_time.setEnabled(false);
                radio_two_time.setEnabled(false);
                radio_three_time.setEnabled(false);
                cur_modify_record_time = ConstantValues.P2P_SET.RECORD_TIME_SET.RECORD_TIME_TWO_MINUTE;
                P2PHandler.getInstance().setRecordTime(idOrIp,
                        contact.contactPassword, cur_modify_record_time);
                break;
            case R.id.radio_three_time:
                progressBar_record_time.setVisibility(RelativeLayout.VISIBLE);
                radio_one_time.setEnabled(false);
                radio_two_time.setEnabled(false);
                radio_three_time.setEnabled(false);
                cur_modify_record_time = ConstantValues.P2P_SET.RECORD_TIME_SET.RECORD_TIME_THREE_MINUTE;
                P2PHandler.getInstance().setRecordTime(idOrIp,
                        contact.contactPassword, cur_modify_record_time);
                break;
            case R.id.change_pre_record:
                // 1.开启
                showPreRecordProgress();
                if (last_pre_record == ConstantValues.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_ON) {
                    last_modify_pre_record = ConstantValues.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_OFF;
                    P2PHandler.getInstance().setPreRecord(idOrIp,
                            contact.contactPassword, last_modify_pre_record);
                    isOpenPreRecord = false;
                } else if (last_pre_record == ConstantValues.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_OFF) {
                    last_modify_pre_record = ConstantValues.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_ON;
                    P2PHandler.getInstance().setPreRecord(idOrIp,
                            contact.contactPassword, last_modify_pre_record);
                    isOpenPreRecord = true;
                }
                break;
            case R.id.bt_plan_time:
                showProgress_plan_time();

                cur_modify_plan_time = Utils.convertPlanTime(
                        hour_from.getCurrentItem(), minute_from.getCurrentItem(),
                        hour_to.getCurrentItem(), minute_to.getCurrentItem());
                P2PHandler.getInstance().setRecordPlanTime(idOrIp,
                        contact.contactPassword, cur_modify_plan_time);
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (isRegFilter) {
            mContext.unregisterReceiver(mReceiver);
            isRegFilter = false;
        }
        Intent it = new Intent();
        it.setAction(ConstantValues.Action.CONTROL_BACK);
        mContext.sendBroadcast(it);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(mContext,ApMonitorActivity.class);
            i.putExtra("contact", contact);
            startActivity(i);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
